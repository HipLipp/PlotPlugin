package de.iils.dc43.plotplugin.modules;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

import de.iils.dc43.persistence.SharedEditingDomainUtil;
import de.iils.dc43.persistence.console.SharedConsoleUtil;
import de.iils.dc43.plotplugin.Activator;

public class PlotPluginMain {

	private static String CONSOLENAME = "UML <-> PlotPlugin";

	public static String findProjectNameForModelURI(URI umlFileURI) {
		String projectName = umlFileURI.toPlatformString(true);
		if (projectName == null) {
			// guess project name
			String[] split = umlFileURI.toString().split("/");
			projectName = split[split.length - 2];
			return projectName;
		}
		while (projectName.startsWith("/") || projectName.startsWith("\\")) {
			projectName = projectName.substring(1);
		}
		int pos = projectName.indexOf('/');
		if (pos != -1) {
			projectName = projectName.substring(0, pos);
		}
		return projectName;
	}

	public static void run(URI umlFileURI) {
		String projectName = findProjectNameForModelURI(umlFileURI);
		MessageConsoleStream consoleStream = SharedConsoleUtil
				.getMessageStream(CONSOLENAME);

		// get the Workspace Path from Project-Preferences
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		TransactionalEditingDomain domain = SharedEditingDomainUtil
				.getSharedEditingDomain("de.iils.dc43.diagram.main.editingDomain");
		if (!umlFileURI.isPlatformResource()) {
			umlFileURI = URI
					.createPlatformResourceURI(
							umlFileURI.segment(umlFileURI.segmentCount() - 2)
									+ "/"
									+ umlFileURI.segment(umlFileURI
											.segmentCount() - 1), true);
		}
		// load Model resource
		Resource umlModelResource = domain.getResourceSet().getResource(
				umlFileURI, true);
		// umlModelResource.unload();
		// umlModelResource = RESOURCE_SET.getResource(umlfileURI, true);

		// Open Model

		Model umlModel = (Model) EcoreUtil
				.getObjectByType(umlModelResource.getContents(),
						UMLPackage.eINSTANCE.getModel());

		// Transform UML into the Java Model
		System.out.println("herrrrrr");
		PlotPluginUtility.readSlotValue(umlModel);
		PlotPluginUtility.readSlotCSV(umlModel);

	}

}

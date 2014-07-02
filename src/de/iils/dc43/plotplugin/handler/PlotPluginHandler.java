package de.iils.dc43.plotplugin.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.iils.dc43.application.handlers.ui.ExportDownHandler;
import de.iils.dc43.persistence.Feature;
import de.iils.dc43.persistence.SystemCheck;
import de.iils.dc43.plotplugin.modules.PlotPluginMain;

public class PlotPluginHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!SystemCheck.getInstance().getActivationStatus(Feature.NONE, true))
			return null;
		ExportDownHandler.registerCommandWithIcon(event.getCommand(),
				de.iils.dc43.plotplugin.Activator.PLUGIN_ID, "iconplot.png");
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		IStructuredSelection sel = (IStructuredSelection) selection;
		IResource coreresource = (IResource) sel.getFirstElement();
		String resourcepath = "file:/" + coreresource.getLocation().toString();
		URI umlfileURI = URI.createURI(resourcepath);
		PlotPluginMain.run(umlfileURI);
		return null;
	}

}

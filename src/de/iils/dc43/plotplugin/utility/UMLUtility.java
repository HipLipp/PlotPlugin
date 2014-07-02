package de.iils.dc43.plotplugin.utility;

import java.util.Collections;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.uml2.uml.InstanceSpecification;

import de.iils.dc43.persistence.SharedEditingDomainUtil;
import de.iils.dc43.persistence.uml.UML2Application;

public class UMLUtility extends UML2Application {

	public static boolean getBooleanFromSlot(
			InstanceSpecification instanceSpecification, String slotName)
			throws Exception {

		if (getSlotStringValueByString(instanceSpecification, slotName)
				.equalsIgnoreCase("true")) {
			return true;
		} else if (getSlotStringValueByString(instanceSpecification, slotName)
				.equalsIgnoreCase("false")) {
			return false;
		} else {
			Logger.getLogger().warn(
					"Instance: " + instanceSpecification.getName()
							+ " has no slot value for slot: " + slotName);
			throw new Exception("Instance: " + instanceSpecification.getName()
					+ " has no boolean slot value for slot: " + slotName);
		}

	}

	public static double getDoubleFromSlot(
			InstanceSpecification instanceSpecification, String slotName)
			throws Exception {

		if (getSlotStringValueByString(instanceSpecification, slotName) != null) {
			return getSlotDoubleValueByString(instanceSpecification, slotName);
		} else {
			Logger.getLogger().warn(
					"Instance: " + instanceSpecification.getName()
							+ " has no slot value for slot: " + slotName);
			throw new Exception("Instance: " + instanceSpecification.getName()
					+ " has no slot value for slot: " + slotName);
		}
	}

	public static String getStringFromSlot(
			InstanceSpecification instanceSpecification, String slotName)
			throws Exception {

		if (getSlotStringValueByString(instanceSpecification, slotName) != null) {
			return getSlotStringValueByString(instanceSpecification, slotName);
		} else {
			Logger.getLogger().warn(
					"Instance: " + instanceSpecification.getName()
							+ " has no slot value for slot: " + slotName);
			throw new Exception("Instance: " + instanceSpecification.getName()
					+ " has no slot value for slot: " + slotName);
		}
	}

	public static void setSlotbyDouble(final InstanceSpecification uml,
			final String slot, final Double value) throws Exception {

		TransactionalEditingDomain domain = SharedEditingDomainUtil
				.getSharedEditingDomain("de.iils.dc43.diagram.main.editingDomain");

		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				domain, "save", Collections.EMPTY_LIST) {

			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws org.eclipse.core.commands.ExecutionException {
				UML2Application.setSlotStringValueByString(uml, slot, value);
				return null;
			}

		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(command,
					new NullProgressMonitor(), null);
		} catch (org.eclipse.core.commands.ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setSlotbyString(final InstanceSpecification uml,
			final String slot, final String value) throws Exception {

		TransactionalEditingDomain domain = SharedEditingDomainUtil
				.getSharedEditingDomain("de.iils.dc43.diagram.main.editingDomain");

		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				domain, "save", Collections.EMPTY_LIST) {

			@Override
			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws org.eclipse.core.commands.ExecutionException {
				UML2Application.setSlotStringValueByString(uml, slot, value);
				return null;
			}

		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(command,
					new NullProgressMonitor(), null);
		} catch (org.eclipse.core.commands.ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

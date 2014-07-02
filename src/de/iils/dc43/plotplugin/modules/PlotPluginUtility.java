package de.iils.dc43.plotplugin.modules;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Stereotype;

import de.iils.dc43.plotplugin.plots.PlotDatabase;
import de.iils.dc43.plotplugin.utility.ArrayListDoubleUtility;
import de.iils.dc43.plotplugin.utility.Logger;
import de.iils.dc43.plotplugin.utility.UMLUtility;

public class PlotPluginUtility extends UMLUtility {
	/**
	 * Searching for Slot with Applied Stereotype and adding Name and Value to
	 * DB
	 * 
	 * @param umlModel
	 */
	public static void readSlotValue(Model umlModel) {

		Set<InstanceSpecification> instances = getAllInstances(umlModel
				.getNestedPackage("Instances"));

		for (InstanceSpecification instance : instances) {

			for (Slot slot : instance.getSlots()) {

				Stereotype applicableStereotype = slot
						.getAppliedStereotype("PlotProfile::watchSlot");
				if (applicableStereotype != null) {
					String name = slot.getDefiningFeature().getName();

					try {// Try to get double Value and add to DB
						Double doubleFromSlot = getDoubleFromSlot(instance,
								name);
						PlotDatabase.getDatabase().addSlotValueToDB(
								instance.getName() + "_" + name,
								doubleFromSlot, false);
					} catch (Exception e) {
						Logger.getLogger().info(
								"Slot " + name + " of Instance "
										+ instance.getName()
										+ " is not a double");

					}

				}

			}

		}

	}

	/**
	 * Searching for Slot with Applied Stereotype and adding CSV File to DB
	 * 
	 * @param umlModel
	 */
	public static void readSlotCSV(Model umlModel) {

		Set<InstanceSpecification> instances = getAllInstances(umlModel
				.getNestedPackage("Instances"));

		for (InstanceSpecification instance : instances) {

			for (Slot slot : instance.getSlots()) {

				Stereotype applicableStereotype = slot
						.getAppliedStereotype("PlotProfile::readCSVFile");
				if (applicableStereotype != null) {
					String name = slot.getDefiningFeature().getName();

					try {
						String slotStringValueByString = getSlotStringValueByString(
								instance, name);

						if (slotStringValueByString.contains(".csv")) {
							PlotDatabase.getDatabase().updateFromFile(
									slotStringValueByString, "-");// TODO
																	// Seperator

						} else {
							Logger.getLogger().info(
									"Slot " + name + " of Instance "
											+ instance.getName()
											+ " do not contain a csv File");
						}
					} catch (Exception e) {

					}

				}

			}

		}
	}

	/**
	 * Add a slotValue into the DB. Remember: the variableName must be unique,
	 * since all names may only be contained once. If there is no value for this
	 * name in the DB, it will create a new series.
	 */

	public static void addValueToDB(String variableName, double value,
			boolean eraseOldData) {
		PlotDatabase.getDatabase().addSlotValueToDB(variableName, value,
				eraseOldData);
	}

	/**
	 * Get ArrayList<Double> for wanted variableName out of the DB
	 * 
	 * @param variableName
	 * @return ArrayList<Double> or null, if DB to not contains Variable
	 */
	public static ArrayList<Double> getSeriesFromDB(String variableName) {

		ArrayList<Double> valuesByVariableName = PlotDatabase.getDatabase()
				.getValuesByVariableName(variableName);

		return valuesByVariableName;
	}

	/**
	 * 
	 * Get the max Value of the Series(selected by Series Name)
	 * 
	 * @param variableName
	 * @return max Value
	 */
	public static double getMaxOfSeriesFromDB(String variableName) {
		ArrayList<Double> seriesFromDB = getSeriesFromDB(variableName);
		double max = ArrayListDoubleUtility.getMax(seriesFromDB);
		return max;
	}

	/**
	 * 
	 * Get the min Value of the Series(selected by Series Name)
	 * 
	 * @param variableName
	 * @return min Value
	 */
	public static double getMinOfSeriesFromDB(String variableName) {
		ArrayList<Double> seriesFromDB = getSeriesFromDB(variableName);
		double min = ArrayListDoubleUtility.getMin(seriesFromDB);
		return min;
	}

	/**
	 * 
	 * Get the mean Value of the Series(selected by Series Name)
	 * 
	 * @param variableName
	 * @return mean Value
	 */
	public static double getMeanOfSeriesFromDB(String variableName) {
		ArrayList<Double> seriesFromDB = getSeriesFromDB(variableName);
		double mean = ArrayListDoubleUtility.getMean(seriesFromDB);
		return mean;
	}

}

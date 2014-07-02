package de.iils.dc43.plotplugin.plots;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.iils.dc43.transformationengine.popup.actions.TransformationRunner;

public class PlotDatabase {

	// ------- V A R I A B L E S ------- //
	private static org.apache.log4j.Logger logger = TransformationRunner
			.getLogger(PlotDatabase.class);
	public Map<String, ArrayList<Double>> plotDatasets = new HashMap<String, ArrayList<Double>>();
	private static PlotDatabase plotDatabase;

	// ------------- E N D ------------- //

	/**
	 * Constructor
	 */
	private PlotDatabase() {
	}

	public static PlotDatabase getDatabase() {
		if (plotDatabase == null) {
			plotDatabase = new PlotDatabase();
		}
		return plotDatabase;
	}

	/**
	 * Add a new dataset to the database. If a dataset with the same name
	 * already exists, the routine returns false. Otherwise on success it
	 * returns true. If you want to overwrite it, use the other add routine.
	 * 
	 * @param setName
	 * @param values
	 * @return
	 */
	public boolean addDatasetToDatabase(String datasetName,
			ArrayList<Double> values) {

		if (plotDatasets.containsKey(datasetName)) {
			return false;
		}

		plotDatasets.put(datasetName, values);

		return true;
	}

	/**
	 * Add a new dataset to the database. If a dataset with the same name
	 * already exists one has the option to overwrite it.
	 * 
	 * @param datasetName
	 * @param values
	 * @param overwriteOldData
	 */
	public void addDatasetToDatabase(String datasetName,
			ArrayList<Double> values, boolean overwriteOldData) {

		if (plotDatasets.containsKey(datasetName) && overwriteOldData) {
			plotDatasets.put(datasetName, values);
		} else if (!plotDatasets.containsKey(datasetName)) {
			plotDatasets.put(datasetName, values);
		}

	}

	public Map<String, ArrayList<Double>> getDatasets() {
		return plotDatasets;
	}

	/**
	 * Get all string keys as a list.
	 * 
	 * @return
	 */
	public ArrayList<String> getAllDBKeys() {

		ArrayList<String> allKeys = new ArrayList<String>();
		for (Entry<String, ArrayList<Double>> dataset : plotDatasets.entrySet()) {
			allKeys.add(dataset.getKey());
		}

		return allKeys;

	}

	/**
	 * Get ArrayList<Double> for wanted variableName out of the DB
	 * 
	 * @param variableName
	 * @return ArrayList<Double> or null, if DB to not contains Variable
	 */
	public ArrayList<Double> getValuesByVariableName(String variableName) {
		if (plotDatasets.containsKey(variableName)) {
			return null;
		}
		ArrayList<Double> arrayList = plotDatasets.get(variableName);
		return arrayList;

	}

	/**
	 * Read a CSV file into the DB.
	 * 
	 * @param filepath
	 * @param seperator
	 */
	private void readCSVFileIntoDatabase(String filepath, String seperator,
			boolean overWriteOldData) {
		try {
			CSVFileReader fileReader = new CSVFileReader();
			fileReader.readFile(filepath, seperator, overWriteOldData);
		} catch (FileNotFoundException e) {
			logger.warn("Could not read file:" + filepath + " into the DB.");
			e.printStackTrace();
		}
	}

	/**
	 * Update all datasets from files.
	 * 
	 * @param filepaths
	 * @param seperators
	 */
	public void updateDataFromFile(List<String> filepaths,
			List<String> seperators) {

		if (filepaths.size() != seperators.size())
			return;

		int ii = 0;
		for (String filepath : filepaths) {
			String seperator = seperators.get(ii);
			readCSVFileIntoDatabase(filepath, seperator, true);
		}

	}

	public void updateFromFile(String filepath, String seperator) {
		readCSVFileIntoDatabase(filepath, seperator, true);
	}

	/**
	 * Add a slotValue into the DB. Remember: the variableName must be unique,
	 * since all names may only be contained once. If there is no value for this
	 * name in the DB, it will create a new series.
	 * 
	 * @param variableName
	 * @param value
	 */
	public void addSlotValueToDB(String variableName, double value,
			boolean eraseOldData) {

		ArrayList<Double> dataset = null;
		if (!plotDatabase.plotDatasets.containsKey(variableName)
				|| eraseOldData) {
			dataset = new ArrayList<Double>();
			plotDatabase.plotDatasets.put(variableName, dataset);
		} else {
			dataset = plotDatabase.plotDatasets.get(variableName);
		}

		dataset.add(value);
	}

	/**
	 * Reset DB. Removes all data.
	 */
	public void resetDB() {
		plotDatabase.plotDatasets.clear();
	}

}

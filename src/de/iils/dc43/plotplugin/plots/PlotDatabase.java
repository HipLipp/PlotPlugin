package de.iils.dc43.plotplugin.plots;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.iils.dc43.transformationengine.popup.actions.TransformationRunner;

public class PlotDatabase {

	// ------- V A R I A B L E S ------- //
	private static org.apache.log4j.Logger logger = TransformationRunner
			.getLogger(PlotDatabase.class);
	private static PlotDatabase plotDatabase;
	private Map<String, ArrayList<Double>> plotDatasets = new HashMap<String, ArrayList<Double>>();
	private Map<String, Boolean[]> variableSelection = new TreeMap<String, Boolean[]>();

	// ------------- E N D ------------- //

	/**
	 * Constructor
	 */
	private PlotDatabase() {
	}

	/**
	 * Get Singleton.
	 * 
	 * @return
	 */
	public static PlotDatabase getDatabase() {
		if (plotDatabase == null) {
			plotDatabase = new PlotDatabase();
		}
		return plotDatabase;
	}

	/**
	 * Menu with correct vars.
	 * 
	 * @return
	 */
	public Map<String, Boolean[]> getVariableSelectionFromDB() {
		if (!variableSelection.containsKey("EntryID")) {
			variableSelection.put("EntryID", new Boolean[] { false, false });
		}
		return variableSelection;
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
	public boolean addDatasetToDatabase(String datasetName, ArrayList<Double> values) {

		if (plotDatasets.containsKey(datasetName)) {
			return false;
		}

		plotDatasets.put(datasetName, values);
		variableSelection.put(datasetName, new Boolean[] { false, false });

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
	public void addDatasetToDatabase(String datasetName, ArrayList<Double> values,
			boolean overwriteOldData) {

		if (plotDatasets.containsKey(datasetName) && overwriteOldData) {
			plotDatasets.put(datasetName, values);
			variableSelection.put(datasetName, new Boolean[] { false, false });
		} else if (!plotDatasets.containsKey(datasetName)) {
			plotDatasets.put(datasetName, values);
			variableSelection.put(datasetName, new Boolean[] { false, false });
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
	public ArrayList<String> getAllDBVariableNames() {

		ArrayList<String> allKeys = new ArrayList<String>();
		for (Entry<String, ArrayList<Double>> dataset : plotDatasets.entrySet()) {
			allKeys.add(dataset.getKey());
		}

		return allKeys;

	}

	/**
	 * Get ArrayList<Double> for variableName out of the DB
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
	private void readCSVFileIntoDatabase(String filepath, String seperator, boolean overWriteOldData) {
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
	public void updateDataFromFile(List<String> filepaths, List<String> seperators) {

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
	public void addSlotValueToDB(String variableName, double value, boolean eraseOldData) {

		ArrayList<Double> dataset = null;
		if (!plotDatabase.getDatasets().containsKey(variableName)) {
			dataset = new ArrayList<Double>();
			plotDatabase.getDatasets().put(variableName, dataset);
			variableSelection.put(variableName, new Boolean[] { false, false });
		} else if (plotDatabase.getDatasets().containsKey(variableName) && eraseOldData) {
			dataset = new ArrayList<Double>();
			plotDatabase.getDatasets().put(variableName, dataset);
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
		variableSelection.clear();
		variableSelection.put("EntryID", new Boolean[] { false, false });
	}

	// //////////////////// S E L E C T I O N S T U F F ////////////////////////
	/**
	 * Show the variable keyName as either x==0 or y==1.
	 * 
	 * @param keyName
	 * @param column
	 */
	public void showValue(String keyName, int column) {
		if (column == 0) {
			showAsXValue(keyName);
		} else if (column == 1) {
			showAsYValue(keyName);
		} else {
			System.out.println("Error Column is not available");

		}

	}

	/**
	 * Use the variable: keyName as X-Value.
	 * 
	 * @param keyName
	 */
	private void showAsXValue(String keyName) {

		if (!variableSelection.containsKey(keyName))
			return;

		for (Entry<String, Boolean[]> entry : variableSelection.entrySet()) {
			// String key = entry.getKey();
			Boolean[] value = entry.getValue();
			value[0] = false;
		}
		Boolean[] value = variableSelection.get(keyName);
		value[0] = true;
		this.variableSelection.put(keyName, value);
	}

	/**
	 * Use the variable: keyName as X-Value.
	 * 
	 * @param keyName
	 */
	private void showAsYValue(String keyName) {

		if (!variableSelection.containsKey(keyName))
			return;

		Boolean[] value = variableSelection.get(keyName);
		value[1] = !value[1];
		this.variableSelection.put(keyName, value);
	}

	/**
	 * Get the xVariable Name.
	 * 
	 * @return
	 */
	public String getXVariableName() {
		for (Entry<String, Boolean[]> entry : variableSelection.entrySet()) {
			if (entry.getValue()[0]) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Get the xVariable Names as ArrayList.
	 * 
	 * @return names
	 */
	public ArrayList<String> getYVariableNames() {
		ArrayList<String> names = new ArrayList<>();
		for (Entry<String, Boolean[]> entry : variableSelection.entrySet()) {
			if (entry.getValue()[0]) {
				names.add(entry.getKey());
			}
		}
		return names;
	}

}

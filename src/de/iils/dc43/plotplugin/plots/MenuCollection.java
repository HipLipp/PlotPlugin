package de.iils.dc43.plotplugin.plots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MenuCollection {

	private Map<String, Boolean[]> store;// = new HashMap();

	/**
	 * Construct the menu.
	 * 
	 * @param itemNames
	 */
	public MenuCollection(ArrayList<String> itemNames) {

		this.store = new HashMap<String, Boolean[]>();

		for (String itemName : itemNames) {
			// First entity == X - Status
			// second Entity == Y - Status
			Boolean[] xyValue = new Boolean[] { false, false };
			this.store.put(itemName, xyValue);
		}

	}

	/**
	 * Get the storage.
	 * 
	 * @return
	 */
	public Map<String, Boolean[]> getStore() {
		return store;
	}

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

		if (!store.containsKey(keyName))
			return;

		for (Entry<String, Boolean[]> entry : store.entrySet()) {
			// String key = entry.getKey();
			Boolean[] value = entry.getValue();
			value[0] = false;
		}
		Boolean[] value = store.get(keyName);
		value[0] = true;
		this.store.put(keyName, value);
	}

	/**
	 * Use the variable: keyName as X-Value.
	 * 
	 * @param keyName
	 */
	private void showAsYValue(String keyName) {

		if (!store.containsKey(keyName))
			return;

		Boolean[] value = store.get(keyName);
		value[1] = !value[1];
		this.store.put(keyName, value);
	}

	/**
	 * Get the xVariable Name.
	 * 
	 * @return
	 */
	public String getXVariableName() {
		for (Entry<String, Boolean[]> entry : store.entrySet()) {
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
		for (Entry<String, Boolean[]> entry : store.entrySet()) {
			if (entry.getValue()[0]) {
				names.add(entry.getKey());
			}
		}
		return names;
	}

}

package de.iils.dc43.plotplugin.plots;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.math.plot.Plot2DPanel;

import de.iils.dc43.transformationengine.popup.actions.TransformationRunner;

public class PlotFactory {

	// ------- V A R I A B L E S ------- //
	private static org.apache.log4j.Logger logger = TransformationRunner
			.getLogger(PlotFactory.class);

	// ------------- E N D ------------- //

	/**
	 * Create a standard plot.
	 * 
	 * @return
	 */
	public static Plot2DPanel create2DStandardPlot() {

		Plot2DPanel plot2DPanel = new Plot2DPanel();
		plot2DPanel.setLegendOrientation(Plot2DPanel.SOUTH);

		return plot2DPanel;
	}

	/**
	 * Update the Plot from the current data in the DB.
	 * 
	 * @param plot2DPanel
	 * @param varSelection
	 */
	public static void updatePlot2DFromDB(Plot2DPanel plot2DPanel) {

		Map<String, Boolean[]> varSelection = PlotDatabase.getDatabase()
				.getVariableSelectionFromDB();
		plot2DPanel.removeAllPlots();
		Map<String, ArrayList<Double>> datasets = PlotDatabase.getDatabase().getDatasets();

		// X-Value
		double[] xValues = null;
		String xVarName = null;
		for (Entry<String, Boolean[]> selector : varSelection.entrySet()) {
			if (selector.getValue()[0]) {

				if (selector.getKey() == "EntryID") {
					xVarName = selector.getKey();

				} else {
					xValues = convertDoubleListToDoubleArray(datasets.get(selector.getKey()));
					xVarName = selector.getKey();
				}

			}
		}

		if (xVarName == null) {
			return;
		}
		plot2DPanel.setAxisLabel(0, xVarName); // X-Label
		plot2DPanel.setAxisLabel(1, "");

		// Y-Value
		for (Entry<String, Boolean[]> selector : varSelection.entrySet()) {
			if (selector.getValue()[1]) {

				double[] yValues = convertDoubleListToDoubleArray(datasets.get(selector.getKey()));

				if (xVarName == "EntryID") {
					xValues = getXEntry(yValues);
					plot2DPanel.addLinePlot(selector.getKey(), xValues, yValues);

				} else {
					plot2DPanel.addLinePlot(selector.getKey(), xValues, yValues);
				}

			}
		}

		plot2DPanel.updateUI();

	}

	/**
	 * Convert List<Double> to double[].
	 * 
	 * @param List
	 *            <Double>
	 * @return double[]
	 */
	private static double[] convertDoubleListToDoubleArray(List<Double> doubles) {
		double[] ret = new double[doubles.size()];
		Iterator<Double> iterator = doubles.iterator();
		int ii = 0;
		while (iterator.hasNext()) {
			ret[ii] = iterator.next().doubleValue();
			ii++;
		}
		return ret;
	}

	private static double[] getXEntry(double[] yValues) {
		double[] xValues = new double[yValues.length];
		for (int i = 0; i < xValues.length; i++) {
			xValues[i] = i;
		}

		return xValues;
	}
}

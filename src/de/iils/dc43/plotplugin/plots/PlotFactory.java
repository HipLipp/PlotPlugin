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

		PlotVariableSelection varSelection = PlotDatabase.getDatabase()
				.getVariableSelectionFromDB();
		plot2DPanel.removeAllPlots();
		Map<String, ArrayList<Double>> datasets = PlotDatabase.getDatabase().getDatasets();

		// X-Value
		double[] xValues = null;
		String xVarName = null;
		for (Entry<String, Boolean[]> selector : varSelection.getStore().entrySet()) {
			if (selector.getValue()[0]) {
				xValues = convertDoubleListToDoubleArray(datasets.get(selector.getKey()));
				xVarName = selector.getKey();
			}
		}

		if (xValues == null) {
			// logger.info("Could not update plot, missing valid X-Variable name.");
			return;
		}
		plot2DPanel.setAxisLabel(0, xVarName); // X-Label
		plot2DPanel.setAxisLabel(1, "");

		// Y-Value
		for (Entry<String, Boolean[]> selector : varSelection.getStore().entrySet()) {
			if (selector.getValue()[1]) {
				double[] yValues = convertDoubleListToDoubleArray(datasets.get(selector.getKey()));
				plot2DPanel.addLinePlot(selector.getKey(), xValues, yValues);
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

}

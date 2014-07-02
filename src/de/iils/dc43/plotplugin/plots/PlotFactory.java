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

	public PlotFactory() {

	}

	public static Plot2DPanel create2DStandardPlot() {
		Plot2DPanel plot2DPanel = new Plot2DPanel();

		double[] yValues = convertDoubleListToDoubleArray(PlotDatabase.getDatabase().getDatasets()
				.entrySet().iterator().next().getValue());
		String name = PlotDatabase.getDatabase().getDatasets().entrySet().iterator().next()
				.getKey();

		plot2DPanel.addLinePlot(name, yValues);

		return plot2DPanel;
	}

	/**
	 * Create a 2D Plot and use all variables which can be found in the DB. If
	 * the X-Axis Name can not be found the creation will fail.
	 * 
	 * @param xVariableName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Plot2DPanel create2DPlotWithAllData(String xVariableName)
			throws NoSuchFieldException {

		Plot2DPanel plot2DPanel = new Plot2DPanel();
		PlotDatabase plotDatabase = PlotDatabase.getDatabase();
		Map<String, ArrayList<Double>> datasets = plotDatabase.getDatasets();

		if (!datasets.containsKey(xVariableName)) {
			logger.warn("Can not find X-Variable name:" + xVariableName + " in plotDatabase.");
			throw new NoSuchFieldException();
		}

		// X Values
		double[] xValues = convertDoubleListToDoubleArray(datasets.get(xVariableName));

		for (Entry<String, ArrayList<Double>> dataset : datasets.entrySet()) {

			String datasetName = dataset.getKey();
			ArrayList<Double> datasetValues = dataset.getValue();

			if (datasetName.equals(xVariableName))
				continue;

			double[] yValues = convertDoubleListToDoubleArray(datasetValues);

			plot2DPanel.addLinePlot(datasetName, xValues, yValues);

		}

		return plot2DPanel;
	}

	/**
	 * 
	 * @param xVariableName
	 * @param yVariableNames
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static Plot2DPanel create2DPlotByVariableNames(String xVariableName,
			ArrayList<String> yVariableNames) throws NoSuchFieldException {

		Plot2DPanel plot2DPanel = new Plot2DPanel();
		PlotDatabase plotDatabase = PlotDatabase.getDatabase();
		Map<String, ArrayList<Double>> datasets = plotDatabase.getDatasets();

		if (!datasets.containsKey(xVariableName)) {
			logger.warn("Can not find X-Variable name:" + xVariableName + " in plotDatabase.");
			throw new NoSuchFieldException();
		}

		// X Values
		double[] xValues = convertDoubleListToDoubleArray(datasets.get(xVariableName));

		for (String yVariableName : yVariableNames) {

			if (!datasets.containsKey(yVariableName)) {
				logger.warn("Could not find Y-Variable:" + yVariableName + " in DB.");
				continue;
			}

			ArrayList<Double> datasetValues = datasets.get(yVariableName);
			double[] yValues = convertDoubleListToDoubleArray(datasetValues);
			plot2DPanel.addLinePlot(yVariableName, xValues, yValues);

		}

		return plot2DPanel;
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

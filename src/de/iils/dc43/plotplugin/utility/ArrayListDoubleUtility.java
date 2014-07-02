package de.iils.dc43.plotplugin.utility;

import java.util.ArrayList;

public class ArrayListDoubleUtility {

	public static double getMax(ArrayList<Double> list) {
		double maxValue = list.get(0);
		for (Double entity : list) {
			if (entity > maxValue) {
				maxValue = entity;
			}
		}
		return maxValue;
	}

	public static double getMin(ArrayList<Double> list) {
		double minValue = list.get(0);
		for (Double entity : list) {
			if (entity > minValue) {
				minValue = entity;
			}
		}
		return minValue;
	}

	public static double getMean(ArrayList<Double> list) {
		double mean = 0;
		for (Double entity : list) {
			mean += entity;
		}
		mean = mean / list.size();
		return mean;
	}

}

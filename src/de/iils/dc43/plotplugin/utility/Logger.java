package de.iils.dc43.plotplugin.utility;

import de.iils.dc43.transformationengine.popup.actions.TransformationRunner;

public class Logger {

	private static org.apache.log4j.Logger logger = TransformationRunner
			.getLogger(Logger.class);

	public static org.apache.log4j.Logger getLogger() {
		return logger;
	}

}

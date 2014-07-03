package de.iils.dc43.plotplugin;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.iils.dc43.logging.LoggingPlugin;
import de.iils.dc43.logging.PluginLogManager;
import de.iils.dc43.plotplugin.views.PlotPluginView;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.iils.dc43.plotplugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static boolean fallbackLoggerConfigured = false;
	private PluginLogManager logManager;

	private static boolean alreadyStarted = false;;

	private static Object mutex = new Object();

	private static PlotPluginView plotPluginView;

	private static IWorkbenchPage workbenchPage;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// System.err.println("try to start");
		if (!alreadyStarted) {
			alreadyStarted = true;
			// System.err.println("started: " + Thread.currentThread().getId());
			super.start(context);
			plugin = this;
			logManager = LoggingPlugin.createLogManager(this, true);
		} else {
			// System.err.println("already started");
		}
		// System.err.println("end: " + Thread.currentThread().getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (logManager != null) {
			logManager.shutdown();
			logManager = null;
		}

		plugin = null;
		super.stop(context);

		// System.err.println("stopped: " + Thread.currentThread().getId());
		alreadyStarted = false;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private static void configureFallbackLogger() {
		Logger pluginLogger = Logger.getLogger(PLUGIN_ID);
		pluginLogger.addAppender(new ConsoleAppender(new PatternLayout("%-5p [%C{1}:%L]: %m%n")));
		fallbackLoggerConfigured = true;
	}

	public static Logger getLogger(Class<?> c) {
		Activator activator = getDefault();
		if (activator == null) {
			// fallback logger for use outside of osgi/eclipse (e.g. for unit
			// tests)
			if (!fallbackLoggerConfigured) {
				configureFallbackLogger();
			}
			Logger logger = Logger.getLogger(c);
			logger.addAppender(new ConsoleAppender(new PatternLayout("%-5p [%C{1}:%L]: %m%n")));
			return logger;
		}
		return getDefault().logManager.getLogger(c.getName());
	}

	public static void generateViewsFromEngine() {
		findWorkbenchPage();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				createViews();
			}
		});
	}

	public static void generateViewsFromUI() {
		findWorkbenchPage();
		createViews();
	}

	private static void createViews() {

		try {
			plotPluginView = (PlotPluginView) workbenchPage.showView(PlotPluginView.ID);
			workbenchPage.showView(PlotPluginView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private static void findWorkbenchPage() {
		// if no page
		if (workbenchPage == null) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			// if active
			if (window != null) {
				workbenchPage = window.getActivePage();
				// if Engine
			} else {
				IWorkbenchWindow[] wins = PlatformUI.getWorkbench().getWorkbenchWindows();
				IWorkbenchPage[] pages = null;
				for (IWorkbenchWindow win : wins) {
					pages = win.getPages();
				}
				workbenchPage = pages[0];
			}
		}
	}
}

// }

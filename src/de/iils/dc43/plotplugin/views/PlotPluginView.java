package de.iils.dc43.plotplugin.views;

import java.awt.Frame;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;
import org.math.plot.Plot2DPanel;

import de.iils.dc43.plotplugin.plots.PlotDatabase;
import de.iils.dc43.plotplugin.plots.PlotFactory;
import de.iils.dc43.plotplugin.plots.PlotVariableSelection;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class PlotPluginView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.iils.dc43.plotplugin.views.PlotPluginView";

	private Frame frame;

	private DropDownAction xValueDownMenu;
	private DropDownAction yValueDownMenu;

	private PlotVariableSelection menuCollection;

	private Plot2DPanel active2DPlot;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	/**
	 * The constructor.
	 */
	public PlotPluginView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.EMBEDDED
				| SWT.NO_BACKGROUND);
		frame = SWT_AWT.new_Frame(composite);

		// Update DB DEBUG
		// PlotDatabase.getDatabase().updateFromFile("...","...");

		// Constructor for Plot
		active2DPlot = PlotFactory.create2DStandardPlot();
		frame.add(active2DPlot);

		// Toolbar
		createToolBar();

		// Browser browser = BrowserFactory.create();
		// browser.addLoadListener(new LoadAdapter() {
		// @Override
		// public void onFinishLoadingFrame(FinishLoadingEvent event) {
		// if (event.isMainFrame()) {
		// System.out.println("HTML is loaded.");
		// }
		// }
		// });
		// browser.loadURL("http://www.google.com");

	}

	/**
	 * Create two drop down menus for the selection of the X- and Y-Values.
	 * There can only be ONE X-Value but multiple Y-Values.
	 * 
	 * THIS GOD DAMN MENU TOOK US MULTIPLE DAYS SO FAR AND I HOPE NEVER,
	 * NEEEEEVER TO DO THIS AGAIN.
	 */
	private void createToolBar() {

		menuCollection = new PlotVariableSelection(PlotDatabase.getDatabase()
				.getAllDBKeys());

		xValueDownMenu = new DropDownAction("X-Value", menuCollection, 0);
		yValueDownMenu = new DropDownAction("Y-Value[s]", menuCollection, 1);

		IToolBarManager toolBar = getViewSite().getActionBars()
				.getToolBarManager();
		toolBar.add(xValueDownMenu);
		toolBar.add(yValueDownMenu);

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		if (active2DPlot != null) {
			PlotFactory.updatePlot2DFromDB(active2DPlot, menuCollection);
		}
	}

	/**
	 * Class for the dropdown menu. I do not recommend copying from this one.
	 * 
	 * @author phoenix
	 * 
	 */
	class DropDownAction extends Action {
		public DropDownAction(String name,
				final PlotVariableSelection menuCollection, final int column) {
			super(name, Action.AS_DROP_DOWN_MENU);

			setMenuCreator(new IMenuCreator() {
				@Override
				public Menu getMenu(Control parent) {
					Menu menu = new Menu(parent);

					for (final Entry<String, Boolean[]> entry : menuCollection
							.getStore().entrySet()) {

						// Hide entry EntryID for y-Menu
						if (column == 1 && entry.getKey() == "EntryID") {
							continue;
						}

						final MenuItem item = new MenuItem(menu, SWT.CHECK);
						item.setText(entry.getKey());
						item.setSelection(entry.getValue()[column]);
						item.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								menuCollection.showValue(entry.getKey(), column);

								// Update plot when there was a selection
								PlotFactory.updatePlot2DFromDB(active2DPlot,
										menuCollection);
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {

							}
						});
					}

					return menu;
				}

				@Override
				public Menu getMenu(Menu parent) {
					return null;
				}

				@Override
				public void dispose() {
				}
			});
		}

		@Override
		public void run() {
		}
	}
}
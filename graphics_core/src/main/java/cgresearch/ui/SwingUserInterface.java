/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.bricks.IUserInterface;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.picking.CgApplicationPickable;
import cgresearch.ui.AnimationTimerPanel;
import cgresearch.ui.ControllerPane;
import cgresearch.ui.IApplicationControllerGui;
import cgresearch.ui.LoggerPane;
import cgresearch.ui.menu.CgApplicationMenu;
import cgresearch.ui.menu.FileMenu;
import cgresearch.ui.menu.HelpMenu;
import cgresearch.ui.menu.ResourcesMenu;
import cgresearch.ui.menu.TimerMenu;
import cgresearch.ui.resources.IGlslShaderCompiler;
import cgresearch.ui.resources.ResourceManagerEditor;

/**
 * Base class for all applications.
 * 
 * @author Philipp Jenke
 * 
 */
public class SwingUserInterface implements IUserInterface {

	private JFrame frame;

	/**
	 * Reference to the controller pane on the top left.
	 */
	private ControllerPane controllerPane;

	/**
	 * Menu bar of the CGAplication
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * List of pickable interfaces.
	 */
	private List<CgApplicationPickable> pickables = new ArrayList<CgApplicationPickable>();

	/**
	 * Base application.
	 */
	private final CgApplication application;

	/**
	 * Constructor.
	 */
	public SwingUserInterface(CgApplication application) {
		this(application, null);
	}

	/**
	 * Constructor.
	 */
	public SwingUserInterface(CgApplication application,
			IGlslShaderCompiler glslShaderCompiler) {
		this.application = application;
		frame = new JFrame();

		frame.getContentPane().setLayout(new BorderLayout());

		assemblePanels(application);

		// Setup menu
		frame.setJMenuBar(menuBar);

		// Toolbar
		JToolBar toolBar = getToolBar();
		if (toolBar != null) {
			frame.getContentPane().add(toolBar, BorderLayout.WEST);
		}

		// This is required to show the menu in front of the GL canvas
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		registerApplicationMenu(new FileMenu());
		registerApplicationMenu(new TimerMenu());
		registerApplicationMenu(new HelpMenu());
		registerApplicationMenu(new ResourcesMenu(new ResourceManagerEditor(
				ResourceManager.getShaderManagerInstance(), "Shaders",
				glslShaderCompiler), application.getCgRootNode()));

		frame.setTitle("Computer Graphics Research, HAW Hamburg");
		frame.setSize(400, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
	}

	private void assemblePanels(CgApplication application) {
		// Top-right: material,
		JScrollPane scrollPaneEdit = new JScrollPane();
		scrollPaneEdit.setMinimumSize(new Dimension(0, 0));
		scrollPaneEdit.setPreferredSize(new Dimension(300, 200));

		// Top-left: scene graph
		controllerPane = new ControllerPane(application.getCgRootNode(),
				scrollPaneEdit);
		JScrollPane panelController = new JScrollPane(controllerPane);
		panelController.setMinimumSize(new Dimension(0, 0));
		panelController.setPreferredSize(new Dimension(300, 400));

		// Top: scene graph + material/edit
		JSplitPane topSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				panelController, scrollPaneEdit);
		topSplitPane.setResizeWeight(0.5);

		// bottom-bottom: Console
		JScrollPane consolePanel = new JScrollPane(new LoggerPane());
		consolePanel.setMinimumSize(new Dimension(0, 0));
		consolePanel.setPreferredSize(new Dimension(500, 100));

		// Bottom: Animation Timer + Console
		JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new AnimationTimerPanel(), consolePanel);

		// Main split pane: left and right
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				topSplitPane, bottomSplitPane);
		mainSplitPane.setResizeWeight(0.65);
		frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);
	}

	/**
	 * Return toolbar object.
	 */
	protected JToolBar getToolBar() {
		return null;
	}

	/**
	 * Register an additional menu in the menu bar. This can be done by all
	 * implementing subclasses/applications.
	 */
	public void registerApplicationMenu(CgApplicationMenu cgApplicationMenu) {
		cgApplicationMenu.setRootNode(application.getCgRootNode());
		menuBar.add(cgApplicationMenu);
		menuBar.revalidate();
	}

	/**
	 * Register a controller widget - displayed in the controller pane.
	 */
	public void registerApplicationGUI(IApplicationControllerGui controllerGui) {
		controllerGui.setRootNode(application.getCgRootNode());
		controllerPane.addTab(controllerGui.getName(), controllerGui);
		controllerGui.init();
	}

	/**
	 * Register a pickable interface.
	 */
	protected void registerApplicationPickable(CgApplicationPickable pickable) {
		pickables.add(pickable);
	}
}

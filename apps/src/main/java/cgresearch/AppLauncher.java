package cgresearch;

import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.bricks.IRenderFrame;
import cgresearch.graphics.bricks.IUserInterface;
//import cgresearch.rendering.jmonkey.JMonkeyFrame;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;
import cgresearch.ui.IApplicationControllerGui;
import cgresearch.ui.SwingUserInterface;

/**
 * Convenience class to launch cgapplication apps
 * 
 * @author Philipp Jenke
 *
 */
public class AppLauncher {

	/**
	 * Available rendering systems.
	 */
	public enum RenderSystem {
		NONE, JOGL, JMONKEY
	};

	/**
	 * Available user interfaces
	 */
	public enum UI {
		NONE, SWING, JOGL_SWING
	}

	/**
	 * Reference to the central app
	 */
	private CgApplication app = null;

	/**
	 * User interface system
	 */
	private IUserInterface uiSystem = null;

	/**
	 * Rendering system
	 */
	private IRenderFrame<?> renderSystem = null;

	/**
	 * Singleton instance.
	 */
	private static AppLauncher instance = null;

	/**
	 * Constructor.
	 */
	private AppLauncher() {
		app = null;
		uiSystem = null;
		renderSystem = null;
	}

	/**
	 * Get singleton instance
	 */
	public static AppLauncher getInstance() {
		if (instance == null) {
			instance = new AppLauncher();
		}
		return instance;
	}

	/**
	 * Create app, use this method first
	 */
	public void create(CgApplication app) {
		if (this.app != null) {
			Logger.getInstance().error("App should only be set once!");
			return;
		}
		this.app = app;
		new ConsoleLogger(VerboseMode.NORMAL);
	}

	/**
	 * Set rendering system.
	 */
	public void setRenderSystem(RenderSystem rS) {
		if (this.renderSystem != null) {
			Logger.getInstance()
					.error("Render system should only be set once!");
			return;
		}

		switch (rS) {
		case JOGL:
			renderSystem = new JoglFrame(app);
			break;
		case JMONKEY:
			Logger.getInstance().error("Not implemented");
			//renderSystem = new JMonkeyFrame(app);
			break;
		default:
			Logger.getInstance().equals("Did not render system.");
		}
	}

	/**
	 * Set UI system.
	 */
	public void setUiSystem(UI ui) {
		if (this.uiSystem != null) {
			Logger.getInstance().error("UI system should only be set once!");
			return;
		}

		switch (ui) {
		case SWING:
			if (renderSystem != null) {
				uiSystem = new SwingUserInterface(app,
						renderSystem.getShaderCompiler());
			} else {
				uiSystem = new SwingUserInterface(app);
			}
			break;
		case JOGL_SWING:
			if (renderSystem instanceof JoglFrame) {
				uiSystem = new JoglSwingUserInterface(app,
						(JoglFrame) renderSystem);
			} else {
				Logger.getInstance().message(
						"Cannot create Jogl-Swing-UI without valid JoglFrame.");
			}
			break;
		default:
			Logger.getInstance().equals("Did not create UI system.");
		}
	}

	/**
	 * Add custom user instance.
	 */
	public void addCustomUi(IApplicationControllerGui customUi) {
		if (uiSystem instanceof SwingUserInterface) {
			((SwingUserInterface) uiSystem).registerApplicationGUI(customUi);
		} else {
			Logger.getInstance().message(
					"Custom UI not compatible with UI system.");
		}
	}

	/**
	 * Add a renderable to the render system if possible.
	 */
	public void addRenderable(JoglRenderable renderable) {
		if (renderSystem instanceof JoglFrame) {
			((JoglFrame) renderSystem).addRenderable(renderable);
		} else {
			Logger.getInstance().message(
					"Cannot add renderable to current render system.");
		}
	}
}

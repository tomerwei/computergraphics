package cgresearch;

import cgresearch.core.logging.Logger;
import cgresearch.rendering.core.IRenderObjectsFactory;
import cgresearch.rendering.jogl.core.JoglRenderNode;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;

/**
 * Convenience class to launch cgapplication apps
 * 
 * @author Philipp Jenke
 *
 */
public class JoglAppLauncher extends AppLauncher {

	/**
	 * Constructor.
	 */
	private JoglAppLauncher() {
		super();
	}

	/**
	 * Register a custom RenderObjectFactory. setRenderSystem(RenderSystem.JOGL)
	 * has to be called first!
	 * 
	 * ATTENTION: Currently only RenderSystem.JOGL is supported.
	 * 
	 * To fix: Accept different types for T in IRenderObjectsFactory
	 * <T> (Currently only JoglRenderNode is accepted) and forward to
	 * renderSystem.registerRenderObjectsFactory(factory)
	 * 
	 * @param factory
	 *            The RenderObjectFactory to be added to the rendersystem.
	 */
	public void registerRenderObjectsFactory(IRenderObjectsFactory<JoglRenderNode> factory) {
		if (null == renderSystem) {
			Logger.getInstance().error("Render system has to be set first! - Use setRenderSystem(RenderSystem rS)");
			return;
		}
		if (renderSystem instanceof JoglFrame) {
			((JoglFrame) renderSystem).registerRenderObjectsFactory(factory);
		} else {
			Logger.getInstance().error("Currently only RenderSystem.JOGL supported.");
		}
	}

	/**
	 * Get singleton instance
	 */
	public static JoglAppLauncher getInstance() {
		if (instance == null) {
			instance = new JoglAppLauncher();
		}
		return (JoglAppLauncher) instance;
	}

	/**
	 * Set rendering system.
	 */
	public void setRenderSystem(RenderSystem rS) {
		if (this.renderSystem != null) {
			Logger.getInstance().error("Render system should only be set once!");
			return;
		}

		if (rS == RenderSystem.JOGL) {
			renderSystem = new JoglFrame(app);
		} else {
			super.setRenderSystem(rS);
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

		if (ui == UI.JOGL_SWING) {
			if (renderSystem instanceof JoglFrame) {
				uiSystem = new JoglSwingUserInterface(app, (JoglFrame) renderSystem);
			} else {
				Logger.getInstance().message("Cannot create Jogl-Swing-UI without valid JoglFrame.");
			}
		} else {
			super.setUiSystem(ui);
		}
	}

	/**
	 * Add a renderable to the render system if possible.
	 */
	public void addRenderable(JoglRenderable renderable) {
		if (renderSystem instanceof JoglFrame) {
			((JoglFrame) renderSystem).addRenderable(renderable);
		} else {
			Logger.getInstance().message("Cannot add renderable to current render system.");
		}
	}

	/**
	 * Getter for the current JoglFrame
	 * Only possible if current renderSystem is set to UI.JOGL_SWING
	 * @return renderSystem:JoglFrame
	 */
	public JoglFrame getJoglFrame() {
		if (!(renderSystem instanceof JoglFrame)) {
			Logger.getInstance().error("renderSystem != JoglFrame. Can not return System");
		}
		return (JoglFrame) renderSystem;
	}
}

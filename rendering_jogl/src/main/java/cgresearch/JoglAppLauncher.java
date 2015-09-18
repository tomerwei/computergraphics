package cgresearch;

import cgresearch.AppLauncher;
import cgresearch.core.logging.Logger;
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
}

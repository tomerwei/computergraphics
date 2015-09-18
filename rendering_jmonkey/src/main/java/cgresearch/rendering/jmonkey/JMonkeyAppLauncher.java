package cgresearch.rendering.jmonkey;

import cgresearch.AppLauncher;
import cgresearch.core.logging.Logger;
import cgresearch.rendering.jmonkey.JMonkeyFrame;

/**
 * Convenience class to launch cgapplication apps
 * 
 * @author Philipp Jenke
 *
 */
public class JMonkeyAppLauncher extends AppLauncher {

  /**
   * Get singleton instance
   */
  public static JMonkeyAppLauncher getInstance() {
    if (instance == null) {
      instance = new JMonkeyAppLauncher();
    }
    return (JMonkeyAppLauncher) instance;
  }

  /**
   * Set rendering system.
   */
  public void setRenderSystem(RenderSystem rS) {
    if (this.renderSystem != null) {
      Logger.getInstance().error("Render system should only be set once!");
      return;
    }

    if (rS == RenderSystem.JMONKEY) {
      renderSystem = new JMonkeyFrame(app);
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

    super.setUiSystem(ui);
  }
}

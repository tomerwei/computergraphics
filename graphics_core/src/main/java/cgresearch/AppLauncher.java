package cgresearch;

import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.bricks.IRenderFrame;
import cgresearch.graphics.bricks.IUserInterface;
import cgresearch.ui.IApplicationControllerGui;
import cgresearch.ui.SwingUserInterface;
import cgresearch.ui.menu.CgApplicationMenu;

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
  protected CgApplication app = null;

  /**
   * User interface system
   */
  protected IUserInterface uiSystem = null;

  /**
   * Rendering system
   */
  protected IRenderFrame<?> renderSystem = null;

  /**
   * Singleton instance.
   */
  protected static AppLauncher instance = null;

  /**
   * Constructor.
   */
  protected AppLauncher() {
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
      Logger.getInstance().error("Render system should only be set once!");
      return;
    }

    Logger.getInstance().error("Default AppLauncher does not support render systems.");
  }

  /**
   * Set UI system.
   */
  public void setUiSystem(UI ui) {
    if (this.uiSystem != null) {
      Logger.getInstance().error("UI system should only be set once!");
      return;
    }

    if (ui == UI.SWING) {
      uiSystem = new SwingUserInterface(app);
    } else {
      Logger.getInstance().error("UI system " + ui + " not supported by default AppLauncher.");
    }
  }

  /**
   * Add custom user instance.
   */
  public void addCustomUi(IApplicationControllerGui customUi) {
    if (uiSystem instanceof SwingUserInterface) {
      ((SwingUserInterface) uiSystem).registerApplicationGUI(customUi);
    } else {
      Logger.getInstance().message("Custom UI not compatible with UI system. Looking for JoglAppLauncher?");
    }
  }

  /**
   * Add a custom menu entry.
   * 
   * @param menu
   *          Menu.
   */
  public void addCustomMenu(CgApplicationMenu menu) {
    uiSystem.registerApplicationMenu(menu);
  }
}

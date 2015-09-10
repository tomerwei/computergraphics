package cgresearch.rendering.jogl.ui;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.ui.SwingUserInterface;

/**
 * Specialized version of the UI for the combination JOGL + Swing
 * 
 * @author Philipp Jenke
 *
 */
public class JoglSwingUserInterface extends SwingUserInterface {

  /**
   * Constructor
   */
  public JoglSwingUserInterface(CgApplication app) {
    this(app, null);
  }

  /**
   * Constructor
   */
  public JoglSwingUserInterface(CgApplication app, JoglFrame frame) {
    super(app, (frame != null) ? frame.getShaderCompiler() : null);
    JoglMenu joglMenu = new JoglMenu();
    registerApplicationMenu(joglMenu);
  }
}

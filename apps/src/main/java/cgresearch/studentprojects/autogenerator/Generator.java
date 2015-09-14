package cgresearch.studentprojects.autogenerator;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.rendering.jogl.JoglAppLauncher;

public class Generator extends CgApplication {

  /**
   * Constructor
   */

  public Generator() {

  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {

    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    Generator frame = new Generator();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(frame);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    // GeneratorGUI gui = new GeneratorGUI(frame.getCgRootNode());
    GeneratorGUI2D gui = new GeneratorGUI2D(frame.getCgRootNode());
    appLauncher.addCustomUi(gui);

  }
}

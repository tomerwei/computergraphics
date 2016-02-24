/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.urbanscene;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.LightSource.Type;
import cgresearch.projects.urbanscene.UrbanSceneGenerator;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 */
public class UrbanScene extends CgApplication {
  /**
   * Constructor.
   */
  public UrbanScene() {
    setupLight();
    getCgRootNode().setAllowShadows(true);
    UrbanSceneGenerator generator = new UrbanSceneGenerator();
    getCgRootNode().addChild(generator.buildScene(1, 1));
    getCgRootNode().addChild(new CoordinateSystem());
  }

  /**
   * Setup light positions.
   */
  private void setupLight() {
    getCgRootNode().clearLights();
    LightSource light1 = new LightSource(Type.POINT);
    light1.setPosition(VectorMatrixFactory.newIVector3(3, 10, 3));
    getCgRootNode().addLight(light1);
    
    LightSource light2 = new LightSource(Type.POINT);
    light2.setPosition(VectorMatrixFactory.newIVector3(10, 10, 3));
    getCgRootNode().addLight(light2);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new UrbanScene();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

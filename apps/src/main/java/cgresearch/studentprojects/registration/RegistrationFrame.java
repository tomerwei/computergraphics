package cgresearch.studentprojects.registration;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Initial frame for the registration project (Jäckel)
 *
 */
public class RegistrationFrame extends CgApplication {
  /**
   * Constructor
   */
  public RegistrationFrame() {
    // Testing code: load a triangle mesh and display it
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
    getCgRootNode().addChild(new CgNode(meshes.get(0), "mesh"));
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    RegistrationFrame app = new RegistrationFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

}

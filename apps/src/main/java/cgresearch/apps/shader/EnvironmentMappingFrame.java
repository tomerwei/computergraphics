/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.shader;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * @author Philipp Jenke
 * 
 */
public class EnvironmentMappingFrame extends CgApplication {

  /**
   * Filename for the mesh obj file.
   */
  private final String objFilename = "meshes/bunny.obj";

  /**
   * Constructor.
   */
  public EnvironmentMappingFrame() {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes.size() > 0) {
      ITriangleMesh meshEarth = meshes.get(0);

      String textureId = "ENV_MAP";
      ResourceManager.getTextureManagerInstance().addResource(textureId,
          new CgTexture("textures/environment_parkinglot.jpg"));
      meshEarth.getMaterial().setTextureId(textureId);
      CgNode earthNode = new CgNode(meshEarth, "Shader Sphere", true);
      meshEarth.getMaterial().setShaderId(Material.SHADER_ENVIRONMENT_MAPPING);
      getCgRootNode().addChild(earthNode);
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    EnvironmentMappingFrame app = new EnvironmentMappingFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

}

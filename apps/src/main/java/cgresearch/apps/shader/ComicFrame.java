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
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Test the comic shader
 * 
 * @author Philipp Jenke
 */
public class ComicFrame extends CgApplication {

  /**
   * Filename for the mesh obj file.
   */
  private final String objFilename = "meshes/bunny.obj";

  /**
   * Constructor.
   */
  public ComicFrame() {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    for (int i = 0; i < meshes.size(); i++) {
      ITriangleMesh mesh = meshes.get(i);
      mesh.getMaterial().setShaderId(Material.SHADER_COMIC);
      mesh.getMaterial().addShaderId(Material.SHADER_SILHOUETTE);
      mesh.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);
      CgNode earthNode = new CgNode(mesh, "comic shader", true);
      getCgRootNode().addChild(earthNode);
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    ComicFrame app = new ComicFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

}

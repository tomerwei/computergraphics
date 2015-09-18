/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.trianglemeshes;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.PlyFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class PlyTriangleMesh extends CgApplication {

  /**
   * Constructor.
   */
  public PlyTriangleMesh() {
    String objFilename = "meshes/huge_bunny.ply";
    PlyFileReader reader = new PlyFileReader();
    ITriangleMesh mesh = reader.readFile(objFilename);
    if (mesh == null) {
      return;
    }

    // String texId = "tex_id_dhl_logo";
    // ResourceManager.getTextureManagerInstance().addResource(texId,
    // new CgTexture("textures/lego.png"));
    // mesh.getMaterial().setTextureId(texId);

    mesh.fitToUnitBox();
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    mesh.getMaterial().setRenderMode(Normals.PER_VERTEX);

    getCgRootNode().addChild(new CgNode(mesh, "ply"));
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new PlyTriangleMesh();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

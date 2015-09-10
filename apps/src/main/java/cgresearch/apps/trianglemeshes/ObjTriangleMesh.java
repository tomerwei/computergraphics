/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.trianglemeshes;

import java.util.List;

import cgresearch.AppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.NodeMerger;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class ObjTriangleMesh extends CgApplication {

  /**
   * Constructor.
   */
  public ObjTriangleMesh() {
    // loadFenja();
    loadLotrCubeWithTextureAtlas();
  }

  public void loadLotrCubeWithTextureAtlas() {
    String objFilename = "meshes/bunny.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh mesh = meshes.get(0);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));
    String LOTR_TEXTURE_ATLAS = "LOTR_TEXTURE_ATLAS";
    ResourceManager.getTextureManagerInstance().addResource(LOTR_TEXTURE_ATLAS,
        new CgTexture("textures/lotr_texture_atlas.png"));
    mesh.getMaterial().setTextureId(LOTR_TEXTURE_ATLAS);
    getCgRootNode().addChild(new CgNode(mesh, "mesh"));
  }

  public void loadFenja() {
    String objFilename = "meshes/fenja02.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    // CgNode node = new CgNode(null, "cube");
    for (int i = 0; i < meshes.size(); i++) {
      ITriangleMesh mesh = meshes.get(i);
      // String texId = "tex_id_dhl_logo";
      // ResourceManager.getTextureManagerInstance().addResource(texId,
      // new CgTexture("textures/lego.png"));
      // mesh.getMaterial().setTextureId(texId);
      mesh.fitToUnitBox();
      mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR2));
      NodeMerger.merge(mesh, 1e-5);
      mesh.computeTriangleNormals();
      mesh.computeVertexNormals();
      mesh.getMaterial().setRenderMode(Normals.PER_VERTEX);
      getCgRootNode().addChild(new CgNode(mesh, "mesh"));
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new ObjTriangleMesh();
    AppLauncher.getInstance().create(app);
    AppLauncher.getInstance().setRenderSystem(RenderSystem.JOGL);
    AppLauncher.getInstance().setUiSystem(UI.JOGL_SWING);
  }
}

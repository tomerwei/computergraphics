/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.halfedge;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Central widget to test the half edge functionality.
 * 
 * @author Philipp Jenke
 * 
 */
public class HalfEdgeFrame extends CgApplication {
  /**
   * Constructor.
   */
  public HalfEdgeFrame() {
    // Load cuboid from file to texture mesh
    String sphereObjFilename = "meshes/cube.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(sphereObjFilename);
    if (meshes.size() == 0) {
      return;
    }
    HalfEdgeTriangleMesh heMesh = HalfEdgeTriangleMeshTools.fromMesh(meshes.get(0));
    heMesh.getMaterial().setRenderMode(Normals.PER_FACET);
    heMesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    heMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    // heMesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    CgNode node = new CgNode(heMesh, "half edge mesh");
    getCgRootNode().addChild(node);
  }

  /**
   * Program entry point
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new HalfEdgeFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

}

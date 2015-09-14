/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.halfedge;

import java.util.List;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.halfedge.TriangleMeshHalfEdgeConverter;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.JoglAppLauncher;

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
    for (int i = 0; i < meshes.size(); i++) {
      ITriangleMesh mesh = meshes.get(i);
      // Create shape and insert into scene graph
      getCgRootNode().addChild(new CgNode(mesh, "Triangle mesh for HalfEdge structure."));

      // Create half edge data structure
      TriangleMeshHalfEdgeConverter converter = new TriangleMeshHalfEdgeConverter();
      converter.convert(mesh);
    }
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

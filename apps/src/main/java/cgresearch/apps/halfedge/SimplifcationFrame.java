/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.halfedge;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * Central widget to test the half edge simplification functionality.
 * 
 * @author Philipp Jenke
 * 
 */
public class SimplifcationFrame extends CgApplication implements ActionListener {

  /**
   * Half edge data structure
   */
  private HalfEdgeTriangleMesh heMesh = null;

  /**
   * Constructor.
   */
  public SimplifcationFrame() {
    // Add a toobar
    SimplificationToolbar toolbar = new SimplificationToolbar(this);
    toolbar.setSize(200, 50);
    toolbar.setVisible(true);

    // Load cuboid from file to texture mesh
    String sphereObjFilename = "meshes/sphere.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(sphereObjFilename);
    if (meshes.size() == 0) {
      return;
    }
    heMesh = HalfEdgeTriangleMeshTools.fromMesh(meshes.get(0));
    heMesh.getMaterial().setRenderMode(Normals.PER_FACET);
    heMesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    heMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    heMesh.checkConsistency();

    // Create a triangle mesh for rendering
    createShapeAndAddToSceneGraph();
  }

  /**
   * Create a shape object from the half edge data structure and add it to the
   * scene graph.
   */
  private void createShapeAndAddToSceneGraph() {
    CgNode meshNode = new CgNode(heMesh, "Mesh");
    getCgRootNode().addChild(meshNode);
  }

  /**
   * Apply one simplification step
   */
  private void simplify() {
    HalfEdgeTriangleMeshTools.collapse(heMesh, heMesh.getHalfEdge(0));
    heMesh.checkConsistency();
    heMesh.updateRenderStructures();
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals(SimplificationToolbar.ACTION_COMMMAND_SIMPLIFY)) {
      simplify();
    }
  }

  /**
   * Program entry point
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    SimplifcationFrame frame = new SimplifcationFrame();
    appLauncher.create(frame);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new SimplificationToolbar(frame));
  }

}

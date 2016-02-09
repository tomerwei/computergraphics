/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.simplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.QuadricErrorMetricsSimplification2D;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.HalfEdgeTriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.ui.IApplicationControllerGui;

/**
 * Toolbar to control the simplification process.
 * 
 * @author Philipp Jenke
 * 
 */
public class SimplificationToolbar extends IApplicationControllerGui implements ActionListener {
  private static final long serialVersionUID = -6897703772468146995L;

  /**
   * Half edge data structure
   */
  private final HalfEdgeTriangleMesh heMesh;

  private final QuadricErrorMetricsSimplification2D simplification2D;
  private final Polygon polygon;

  /**
   * Action command for the simplify button.
   */
  public static final String ACTION_COMMMAND_SIMPLIFY_2D = "ACTION_COMMMAND_SIMPLIFY_2D";
  public static final String ACTION_COMMMAND_RESET_2D = "ACTION_COMMMAND_RESET_2D";
  public static final String ACTION_COMMMAND_SIMPLIFY_3D = "ACTION_COMMMAND_SIMPLIFY_3D";
  public static final String ACTION_COMMMAND_RESET_3D = "ACTION_COMMMAND_RESET_3D";

  /**
   * Constructor
   */
  public SimplificationToolbar(HalfEdgeTriangleMesh heMesh, Polygon polygon) {
    this.heMesh = heMesh;
    this.polygon = polygon;
    simplification2D = new QuadricErrorMetricsSimplification2D(polygon);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel label2D = new JLabel("2D");
    add(label2D);

    JButton buttonReset2D = new JButton("Reset");
    buttonReset2D.setActionCommand(ACTION_COMMMAND_RESET_2D);
    buttonReset2D.addActionListener(this);
    add(buttonReset2D);

    JButton buttonSimplify2D = new JButton("Simplify");
    buttonSimplify2D.setActionCommand(ACTION_COMMMAND_SIMPLIFY_2D);
    buttonSimplify2D.addActionListener(this);
    add(buttonSimplify2D);

    JLabel label3D = new JLabel("3D");
    add(label3D);

    JButton buttonReset3D = new JButton("Reset");
    buttonReset3D.setActionCommand(ACTION_COMMMAND_RESET_3D);
    buttonReset3D.addActionListener(this);
    add(buttonReset3D);

    JButton buttonSimplify = new JButton("Simplify (3D)");
    buttonSimplify.setActionCommand(ACTION_COMMMAND_SIMPLIFY_3D);
    buttonSimplify.addActionListener(this);
    add(buttonSimplify);

    reset2D();
    reset3D();
  }

  private void reset3D() {
    // Load cuboid from file to texture mesh
    String sphereObjFilename = "meshes/sphere.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(sphereObjFilename);
    if (meshes.size() == 0) {
      return;
    }
    TriangleMeshTransformation.scale(meshes.get(0), 0.5);
    HalfEdgeTriangleMeshTools.fromMesh(heMesh, meshes.get(0));
    heMesh.getMaterial().setRenderMode(Normals.PER_FACET);
    heMesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    heMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    heMesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    heMesh.checkConsistency();
    heMesh.updateRenderStructures();
  }

  private void reset2D() {
    polygon.clear();
    polygon.addPoint(VectorMatrixFactory.newIVector3(-1, -1, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(-1, -0.5, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(-1, 0.5, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(-1, 1, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(1, 1, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(1, -1, 0));
    polygon.getMaterial().setRenderMode(Normals.PER_VERTEX);
    polygon.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    polygon.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    polygon.updateRenderStructures();
    simplification2D.reset();
  }

  @Override
  public String getName() {
    return "Simplification";
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals(SimplificationToolbar.ACTION_COMMMAND_SIMPLIFY_2D)) {
      simplify2D();
    } else if (event.getActionCommand().equals(SimplificationToolbar.ACTION_COMMMAND_SIMPLIFY_3D)) {
      simplify3D();
    } else if (event.getActionCommand().equals(SimplificationToolbar.ACTION_COMMMAND_RESET_2D)) {
      reset2D();
    } else if (event.getActionCommand().equals(SimplificationToolbar.ACTION_COMMMAND_RESET_3D)) {
      reset3D();
    }
  }

  /**
   * Apply one simplification step in 2D.
   */
  private void simplify2D() {
    simplification2D.simplify();
    polygon.updateRenderStructures();
  }

  /**
   * Apply one simplification step in 3D.
   */
  private void simplify3D() {
    HalfEdgeTriangleMeshTools.collapse(heMesh, heMesh.getHalfEdge(0));
    heMesh.checkConsistency();
    heMesh.updateRenderStructures();
  }

}

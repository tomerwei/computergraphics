package cgresearch.apps.subdivision;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.Subdivision2D;
import cgresearch.graphics.algorithms.Subdivision3D;
import cgresearch.graphics.datastructures.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.ui.IApplicationControllerGui;

public class SubdivisionGui extends IApplicationControllerGui implements ActionListener, KeyListener {

  private static final long serialVersionUID = 1L;

  private static final String ACTION_COMMAND_SPLIT_2D = "ACTION_COMMAND_SPLIT";
  private static final String ACTION_COMMAND_AVERAGE_2D = "ACTION_COMMAND_AVERAGE";
  private static final String ACTION_COMMAND_SUBDIVIDE_2D = "ACTION_COMMAND_SUBDIVIDE";
  private static final String ACTION_COMMAND_RESET = "ACTION_COMMAND_RESET";
  private static final String ACTION_COMMAND_SUBDIVIDE_3D = "ACTION_COMMAND_SUBDIVIDE_3D";

  private Polygon polygon;
  private Subdivision2D subdivision2d;
  private ITriangleMesh mesh;
  private Subdivision3D subdivision3D;

  /**
   * Constructor.
   */
  public SubdivisionGui(Polygon polygon, ITriangleMesh mesh) {
    this.polygon = polygon;
    this.mesh = mesh;
    subdivision2d = new Subdivision2D(polygon);
    subdivision3D = new Subdivision3D(mesh);

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JButton buttonReset = new JButton("Reset");
    buttonReset.addActionListener(this);
    buttonReset.setActionCommand(ACTION_COMMAND_RESET);
    add(buttonReset);

    JLabel label2D = new JLabel("2D: Midpoint");
    add(label2D);

    JButton buttonSplit = new JButton("Split");
    buttonSplit.addActionListener(this);
    buttonSplit.setActionCommand(ACTION_COMMAND_SPLIT_2D);
    add(buttonSplit);

    JButton buttonAverage = new JButton("Average");
    buttonAverage.addActionListener(this);
    buttonAverage.setActionCommand(ACTION_COMMAND_AVERAGE_2D);
    add(buttonAverage);

    JButton buttonSubdivide = new JButton("Subdivide");
    buttonSubdivide.addActionListener(this);
    buttonSubdivide.setActionCommand(ACTION_COMMAND_SUBDIVIDE_2D);
    add(buttonSubdivide);

    JLabel label3D = new JLabel("3D: Loop");
    add(label3D);

    JButton buttonSubdivide3D = new JButton("Subdivide");
    buttonSubdivide3D.addActionListener(this);
    buttonSubdivide3D.setActionCommand(ACTION_COMMAND_SUBDIVIDE_3D);
    add(buttonSubdivide3D);

    reset();

    // Debugging
    // subdivision3D.subdivide();
    // updateRenderStructures();
  }

  @Override
  public String getName() {
    return "Subdivision";
  }

  private void reset() {
    // Init polygon
    polygon.clear();
    polygon.addPoint(VectorMatrixFactory.newIVector3(-2, 0, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(0, 2, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(2, 0, 0));
    polygon.addPoint(VectorMatrixFactory.newIVector3(0, -2, 0));
    polygon.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    polygon.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);

    // Init mesh
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);
    mesh.getMaterial().setReflectionSpecular(Material.PALETTE1_COLOR4.multiply(0.1));
    mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setRenderMode(Normals.PER_FACET);

    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/icosahedron.obj");
    //List<ITriangleMesh> meshes = reader.readFile("meshes/wolf.obj");
    mesh.copyFrom(meshes.get(0));
//    mesh.copyFrom(NodeMerger.merge(mesh, 1e-5));
//    mesh.fitToUnitBox();
    // mesh.copyFrom(TriangleMeshFactory.createCube());

    polygon.updateRenderStructures();
    mesh.updateRenderStructures();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_COMMAND_SPLIT_2D)) {
      subdivision2d.split();
      updateRenderStructures();
    } else if (e.getActionCommand().equals(ACTION_COMMAND_AVERAGE_2D)) {
      subdivision2d.average();
      updateRenderStructures();
    } else if (e.getActionCommand().equals(ACTION_COMMAND_SUBDIVIDE_2D)) {
      subdivision2d.subdivide();
      updateRenderStructures();
    } else if (e.getActionCommand().equals(ACTION_COMMAND_RESET)) {
      reset();
      updateRenderStructures();
    } else if (e.getActionCommand().equals(ACTION_COMMAND_SUBDIVIDE_3D)) {
      subdivision3D.subdivide();
      updateRenderStructures();
    }
  }

  private void updateRenderStructures() {
    if (polygon != null) {
      polygon.updateRenderStructures();
    }
    if (mesh != null) {
      mesh.updateRenderStructures();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}

package cgresearch.apps.subdivision;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.NodeMerger;
import cgresearch.graphics.algorithms.Subdivision2D;
import cgresearch.graphics.algorithms.Subdivision3D;
import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.PolygonIO;
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
  private static final String ACTION_COMMAND_COMBO = "ACTION_COMMAND_COMBO";

  private Polygon polygon;
  private Subdivision2D subdivision2d;
  private ITriangleMesh mesh;
  private Subdivision3D subdivision3D;
  private JComboBox<String> comboSelect3Ddataset;

  private enum Mesh {
    HALF_SPHERE, CUBE, ICOSAHEDRON, WOLF, MONKEY, GLIDER
  }

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
    buttonReset.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(buttonReset);

    JLabel label2D = new JLabel("2D: Midpoint");
    label2D.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(label2D);

    JButton buttonSplit = new JButton("Split");
    buttonSplit.addActionListener(this);
    buttonSplit.setActionCommand(ACTION_COMMAND_SPLIT_2D);
    buttonSplit.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(buttonSplit);

    JButton buttonAverage = new JButton("Average");
    buttonAverage.addActionListener(this);
    buttonAverage.setActionCommand(ACTION_COMMAND_AVERAGE_2D);
    buttonAverage.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(buttonAverage);

    JButton buttonSubdivide = new JButton("Subdivide");
    buttonSubdivide.addActionListener(this);
    buttonSubdivide.setActionCommand(ACTION_COMMAND_SUBDIVIDE_2D);
    buttonSubdivide.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(buttonSubdivide);

    JLabel label3D = new JLabel("3D: Loop");
    label3D.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(label3D);

    comboSelect3Ddataset = new JComboBox<>();
    for (Mesh meshType : Mesh.values()) {
      comboSelect3Ddataset.addItem(meshType.toString());
    }
    comboSelect3Ddataset.setMaximumSize(new Dimension(100, 20));
    comboSelect3Ddataset.setAlignmentX(Component.CENTER_ALIGNMENT);
    comboSelect3Ddataset.setSelectedIndex(0);
    comboSelect3Ddataset.addActionListener(this);
    comboSelect3Ddataset.setActionCommand(ACTION_COMMAND_COMBO);
    add(comboSelect3Ddataset);

    JButton buttonSubdivide3D = new JButton("Subdivide");
    buttonSubdivide3D.addActionListener(this);
    buttonSubdivide3D.setActionCommand(ACTION_COMMAND_SUBDIVIDE_3D);
    buttonSubdivide3D.setAlignmentX(Component.CENTER_ALIGNMENT);
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
    // 2D
    PolygonIO polygonIO = new PolygonIO();
    polygon.clear();
    polygon.addPoint(VectorFactory.createVector3(-2, 0, 0));
    polygon.addPoint(VectorFactory.createVector3(0, 2, 0));
    polygon.addPoint(VectorFactory.createVector3(2, 0, 0));
    polygon.addPoint(VectorFactory.createVector3(0, -2, 0));
//    polygonIO.writePolygon(polygon, "/Users/abo781/abo781/code/computergraphics/assets/polygons/square.polygon");

    polygon.copy(polygonIO.readPolygon("polygons/bird.polygon"));
    polygon.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    polygon.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);
    polygon.getMaterial().setPointSphereSize(0.05);
    polygon.getMaterial().setShowPointSpheres(true);
    polygon.updateRenderStructures();

    // 3D
    reset3D(Mesh.valueOf((String) comboSelect3Ddataset.getSelectedItem()));
  }

  public void reset3D(Mesh meshType) {
    // Init mesh
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);
    mesh.getMaterial().setReflectionSpecular(Material.PALETTE1_COLOR4.multiply(0.1));
    mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setRenderMode(Normals.PER_FACET);
    loadMesh(meshType);
    mesh.updateRenderStructures();
  }

  private void loadMesh(Mesh meshType) {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = null;
    switch (meshType) {
      case HALF_SPHERE:
        mesh.copyFrom(TriangleMeshFactory.createHalfSphere(20));
        break;
      case CUBE:
        mesh.copyFrom(TriangleMeshFactory.createCube());
        break;
      case ICOSAHEDRON:
        meshes = reader.readFile("meshes/icosahedron.obj");
        mesh.copyFrom(meshes.get(0));
        break;
      case WOLF:
        meshes = reader.readFile("meshes/wolf.obj");
        mesh.copyFrom(meshes.get(0));
        mesh.copyFrom(NodeMerger.merge(mesh, 1e-5));
        mesh.fitToUnitBox();
        break;
      case MONKEY:
        meshes = reader.readFile("meshes/monkey.obj");
        mesh.copyFrom(meshes.get(0));
        mesh.copyFrom(NodeMerger.merge(mesh, 1e-5));
        mesh.fitToUnitBox();
        break;
      case GLIDER:
        meshes = reader.readFile("sketchup/glider.obj");
        mesh.copyFrom(meshes.get(0));
        mesh.copyFrom(NodeMerger.merge(mesh, 1e-5));
        mesh.fitToUnitBox();
        break;
    }
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
    } else if (e.getActionCommand().equals(ACTION_COMMAND_COMBO)) {
      reset3D(Mesh.valueOf((String) comboSelect3Ddataset.getSelectedItem()));
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

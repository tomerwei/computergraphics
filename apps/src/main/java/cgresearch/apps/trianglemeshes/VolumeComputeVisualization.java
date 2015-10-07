/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.trianglemeshes;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;

/**
 * Visualisation of the volume computation of a closed triangle mesh using
 * spats.
 * 
 * Use the animation timer slider to select the current triangle.
 * 
 * Use the GUI to show/hide the spat volume.
 * 
 * @author Philipp Jenke
 * 
 */
public class VolumeComputeVisualization extends CgApplication implements Observer {

  private ITriangleMesh spatMesh;
  private ITriangleMesh mesh;
  boolean showSpat = false;

  /**
   * Constructor.
   */
  public VolumeComputeVisualization() {
    // Mesh to compute volume for
    String objFilename = "meshes/cube.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null || meshes.size() == 0) {
      Logger.getInstance().error("Invalid mesh: " + objFilename);
      return;
    }
    mesh = meshes.get(0);
    TriangleMeshTransformation.scale(mesh, 0.5);
    TriangleMeshTransformation.translate(mesh, VectorMatrixFactory.newIVector3(1, 1, 1));

    mesh.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));
    mesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
    getCgRootNode().addChild(new CgNode(mesh, "mesh"));

    // Spat mesh
    spatMesh = new TriangleMesh();
    spatMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    spatMesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
    getCgRootNode().addChild(new CgNode(spatMesh, "spat"));

    // Coordinate System
    getCgRootNode().addChild(new CoordinateSystem());

    AnimationTimer.getInstance().addObserver(this);
    AnimationTimer.getInstance().setMinValue(0);
    AnimationTimer.getInstance().setMaxValue(mesh.getNumberOfTriangles());

    AnimationTimer.getInstance().setValue(0);
    updateSpatMesh();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof AnimationTimer) {
      updateSpatMesh();
    }
  }

  private void updateSpatMesh() {
    int index = (int) (AnimationTimer.getInstance().getValue());
    if (index < 0 || index >= mesh.getNumberOfTriangles()) {
      return;
    }

    spatMesh.clear();
    Triangle triangle = mesh.getTriangle(index);
    spatMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(0, 0, 0)));
    Vertex a = new Vertex(mesh.getVertex(triangle.getA()));
    Vertex b = new Vertex(mesh.getVertex(triangle.getB()));
    Vertex c = new Vertex(mesh.getVertex(triangle.getC()));
    spatMesh.addVertex(a);
    spatMesh.addVertex(b);
    spatMesh.addVertex(c);
    if (showSpat) {
      spatMesh.addTriangle(new Triangle(0, 1, 2));
      spatMesh.addTriangle(new Triangle(0, 2, 3));
      spatMesh.addTriangle(new Triangle(0, 1, 3));
    }
    spatMesh.addTriangle(new Triangle(1, 2, 3));
    spatMesh.computeTriangleNormals();
    spatMesh.computeVertexNormals();

    // Set color depending on spat volume sign
    double spatVolume = a.getPosition().multiply(b.getPosition().cross(c.getPosition()));
    if (spatVolume > 0) {
      spatMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25));
    } else {
      spatMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.25, 0.75, 0.25));
    }
    spatMesh.updateRenderStructures();
  }

  public void toggleShowSpat() {
    showSpat = !showSpat;
    updateSpatMesh();
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    VolumeComputeVisualization app = new VolumeComputeVisualization();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    JoglAppLauncher.getInstance().addCustomUi(new VolumeComputeGUI(app));
  }

  public boolean getShowSpat() {
    return showSpat;
  }
}

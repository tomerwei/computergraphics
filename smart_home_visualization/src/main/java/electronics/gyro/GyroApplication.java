package electronics.gyro;

import java.util.List;
import java.util.Observable;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.Transformation;

/**
 * Visualize the orientation of a gyro sensor connected to a Arduino.
 * 
 * Requires command line VM arguments:
 * -Djava.library.path=/Users/abo781/abo781/code/computergraphics/libs/native/
 * osx/
 * 
 * Arduino Sketch: ArdulinkProtocol.ino
 * 
 * @author Philipp Jenke
 */
public class GyroApplication extends CgApplication {

  private enum MeshType {
    SPACESHIP, CUBOID
  }

  /**
   * Logic
   */
  private GyroApplicationModel model = null;

  private Transformation transformation = new Transformation();

  public GyroApplication() {
    model = new GyroApplicationModel();
    loadMesh(MeshType.SPACESHIP);
    getCgRootNode().addChild(new CoordinateSystem());
    AnimationTimer.getInstance().startTimer(100);
  }

  /**
   * Load mesh to represent current orientation.
   */
  public void loadMesh(MeshType type) {
    ITriangleMesh mesh = null;
    switch (type) {
      case SPACESHIP:
        mesh = new TriangleMesh();
        ObjFileReader reader = new ObjFileReader();
        List<ITriangleMesh> meshes = reader.readFile("meshes/modul1.obj");
        for (ITriangleMesh subMesh : meshes) {
          mesh.unite(subMesh);
        }
        mesh.fitToUnitBox();
        TriangleMeshTransformation.multiply(mesh,
            MatrixFactory.createRotationMatrix(VectorFactory.createVector3(0, 1, 0), 21.0 * Math.PI / 180.0));
        break;
      case CUBOID:
        mesh = TriangleMeshFactory.createCube();
        TriangleMeshTransformation.scale(mesh, VectorFactory.createVector3(1, 0.333, 0.666));
        break;
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    // mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.7, 0.7, 0.7));
    mesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    mesh.getMaterial().setReflectionSpecular(VectorFactory.createVector3(0.1, 0.1, 0.1));
    CgNode transformationNode = new CgNode(transformation, "6DOF");
    CgNode meshNode = new CgNode(mesh, "sensor");
    transformationNode.addChild(meshNode);
    getCgRootNode().addChild(transformationNode);
  }

  private GyroApplicationModel getModel() {
    return model;
  }

  @Override
  public void update(Observable o, Object arg) {
    transformation.setTransformation(model.getOrientation());
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    GyroApplication gyroApp = new GyroApplication();
    appLauncher.create(gyroApp);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new GyroApplicationGui(gyroApp.getModel()));
  }
}

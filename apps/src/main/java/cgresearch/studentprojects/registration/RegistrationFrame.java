package cgresearch.studentprojects.registration;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.apps.trianglemeshes.ObjTriangleMesh;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.ConsoleLogger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Matrix4;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;

/**
 * Initial frame for the registration project (J�ckel)
 *
 */
public class RegistrationFrame extends CgApplication {

  public static IPointCloud Base = new PointCloud();
  public static IPointCloud Register = new PointCloud();

  /**
   * Constructor
   */
  public RegistrationFrame() {
    // Testing code: load a triangle mesh and display it
    // ObjFileReader reader = new ObjFileReader();
    // List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
    // getCgRootNode().addChild(new CgNode(meshes.get(0), "mesh"));

    // Old version: Jäckel, few points
    // loadIPointCloud();

    // New version: Cubes (JNK)
    loadTestData();
  }

  private void loadTestData() {
    // Load cube from file
    ObjFileReader reader = new ObjFileReader();
    ITriangleMesh cubeMesh = reader.readFile("meshes/cube.obj").get(0);
    // Created point cloud from cube
    IPointCloud basePointCloud = TriangleMeshSampler.sample(cubeMesh, 500);
    basePointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
    // Set point color
    for (int i = 0; i < basePointCloud.getNumberOfPoints(); i++) {
      basePointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR0);
    }

    // Rotation of the second point cloud: 10 degrees in degrees - transformed
    // to radiens. Rotation axis: (1,1,1)
    double rotationAngle = 10 * Math.PI / 180;
    // Transform mesh for second cube
    TriangleMeshTransformation.transform(cubeMesh,
        VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(1, 1, 1), rotationAngle));
    IPointCloud registerPointCloud = TriangleMeshSampler.sample(cubeMesh, 500);
    registerPointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
    // Set point color
    for (int i = 0; i < registerPointCloud.getNumberOfPoints(); i++) {
      registerPointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
    }

    CgNode basePointCloudNode = new CgNode(basePointCloud, "pointCloud");
    getCgRootNode().addChild(basePointCloudNode);
    CgNode registerPointCloudNode = new CgNode(registerPointCloud, "pointCloud2");
    getCgRootNode().addChild(registerPointCloudNode);

  }

  public void loadIPointCloud() {
    IVector3 position1 = VectorMatrixFactory.newIVector3(0, 0, 0);
    IVector3 position2 = VectorMatrixFactory.newIVector3(0, 0, 2);
    IVector3 position3 = VectorMatrixFactory.newIVector3(2, 0, 2);
    IVector3 position4 = VectorMatrixFactory.newIVector3(2, 0, 0);

    IVector3 position5 = VectorMatrixFactory.newIVector3(0, 0, 2);
    IVector3 position6 = VectorMatrixFactory.newIVector3(0, 2, 0);
    IVector3 position7 = VectorMatrixFactory.newIVector3(0, 2, 2);
    IVector3 position8 = VectorMatrixFactory.newIVector3(0, 0, 0);

    IVector3 color = VectorMatrixFactory.newIVector3(Math.random(), Math.random(), Math.random());
    IVector3 normal = VectorMatrixFactory.newIVector3(Math.random(), Math.random(), Math.random());

    // IPointCloud pointCloud = new PointCloud();
    Base.addPoint(new Point(position1, color, normal));
    Base.addPoint(new Point(position2, color, normal));
    Base.addPoint(new Point(position3, color, normal));
    Base.addPoint(new Point(position4, color, normal));

    // IPointCloud pointCloud2 = new PointCloud();
    Register.addPoint(new Point(position5, color, normal));
    Register.addPoint(new Point(position6, color, normal));
    Register.addPoint(new Point(position7, color, normal));
    Register.addPoint(new Point(position8, color, normal));

    // ObjFileReader reader = new ObjFileReader();
    // List<ITriangleMesh> meshes = reader.readFile("meshes/cube.obj");
    // IPointCloud pointCloud3 = PointCloudFactory.createDummyPointCloud();
    // ITriangleMesh mesh = meshes.get(0);
    // pointCloud3 = TriangleMeshSampler.sample(mesh, 5000);
    //
    // //verschobener W�rfel...leider noch nicht verschoben.
    // ObjFileReader reader1 = new ObjFileReader();
    // List<ITriangleMesh> meshes1 = reader.readFile("meshes/cube.obj");
    // IPointCloud pointCloud4 = PointCloudFactory.createDummyPointCloud();
    // ITriangleMesh mesh1 = meshes.get(0);
    // pointCloud4 = TriangleMeshSampler.sample(mesh, 5000);

    //
    CgNode pointCloudNode = new CgNode(Base, "pointCloud");
    getCgRootNode().addChild(pointCloudNode);

    CgNode pointCloudNode2 = new CgNode(Register, "pointCloud2");
    getCgRootNode().addChild(pointCloudNode2);

    // CgNode pointCloudNode3 = new CgNode(pointCloud3, "pointCloud3");
    // getCgRootNode().addChild(pointCloudNode3);

  }

  public static void main(String[] args) {
    // ResourcesLocator.getInstance().parseIniFile("resources.ini");
    // RegistrationFrame app = new RegistrationFrame();
    // JoglFrame frame = new JoglFrame(app);
    // new JoglSwingUserInterface(app, frame);
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    RegistrationFrame app = new RegistrationFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    RegistrationButton button = new RegistrationButton();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    // appLauncher.addCustomMenu(new RegistrationMenu());
    appLauncher.addCustomUi(button);

    // CgNode pointCloudNode = new CgNode(pointCloud, "point cloud");
    // getCgRootNode().addChild(pointCloudNode);

  }

}

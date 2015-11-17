package cgresearch.studentprojects.shadowvolumes;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.*;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;

import java.util.List;
import java.util.Observable;

/**
 * Demo frame to present shadow volumes
 *
 * @author Marcel Kuhn
 *
 */
public class ShadowVolumeDemo extends CgApplication {

  private LightSource lightSource = new LightSource(LightSource.Type.POINT, LightSource.ShadowType.HARD);
  private double alpha = 0;

  /**
   * Constructor
   */
  public ShadowVolumeDemo() {
    loadScene();
  }

  private void loadScene() {
    // Remove existing light sources
    getCgRootNode().clearLights();

    // Load elements
    createEnvironment();
    //loadHulk();
    //loadCube();
    loadObject();

    // Set light source
    lightSource.setPosition(VectorMatrixFactory.newIVector3(2, 5, 2));
    lightSource.setDirection(VectorMatrixFactory.newIVector3(0, 0,1));
    getCgRootNode().addLight(lightSource);
    getCgRootNode().setAllowShadows(true);
  }

  private void createEnvironment() {
    ITriangleMesh room = new TriangleMesh();

    // Corner points
    IVector3 pA = VectorMatrixFactory.newIVector3(0, 0, 0);
    IVector3 pB = VectorMatrixFactory.newIVector3(0, 0, 1);
    IVector3 pC = VectorMatrixFactory.newIVector3(1, 0, 1);
    IVector3 pD = VectorMatrixFactory.newIVector3(1, 0, 0);
    IVector3 pE = VectorMatrixFactory.newIVector3(0, 1, 0);
    IVector3 pF = VectorMatrixFactory.newIVector3(1, 1, 0);

    // Add vertices to mesh
    room.addVertex(new Vertex(pA));
    room.addVertex(new Vertex(pB));
    room.addVertex(new Vertex(pC));
    room.addVertex(new Vertex(pD));
    room.addVertex(new Vertex(pE));
    room.addVertex(new Vertex(pF));

    room.addTextureCoordinate(VectorMatrixFactory.newIVector3(0, 0, 0));
    room.addTextureCoordinate(VectorMatrixFactory.newIVector3(0, 1, 0));
    room.addTextureCoordinate(VectorMatrixFactory.newIVector3(1, 0, 0));
    room.addTextureCoordinate(VectorMatrixFactory.newIVector3(1, 1, 0));

    // Create floor
    Triangle t1 = new Triangle(0, 1, 2, 0, 1, 3);
    Triangle t2 = new Triangle(0, 2, 3, 0, 3, 2);

    // Create wall
    Triangle t3 = new Triangle(0, 5, 4, 0, 3, 2);
    Triangle t4 = new Triangle(0, 3, 5, 0, 1, 3);

    // Add triangles to mesh and compute normals
    room.addTriangle(t1);
    room.addTriangle(t2);
    room.addTriangle(t3);
    room.addTriangle(t4);
    room.computeTriangleNormals();
    room.getMaterial().setThrowsShadow(false);

    String texId = "tex_id_dhl_logo";
    ResourceManager.getTextureManagerInstance().addResource(texId, new CgTexture("textures/android.png"));
    room.getMaterial().setTextureId(texId);
    room.getMaterial().setShaderId(Material.SHADER_TEXTURE_PHONG);

    // Position environment into the middle
    Transformation t = new Transformation();
    t.addTranslation(VectorMatrixFactory.newIVector3(-0.5, 0, -0.5));

    // Add Environment to the scene graph
    CgNode node = new CgNode(t, "Translation");
    node.addChild(new CgNode(room, "room"));
    getCgRootNode().addChild(node);

    AnimationTimer.getInstance().startTimer(50);
  }

  private void loadCube() {
    String objFilename = "meshes/cube.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    Transformation t = new Transformation();
    t.addScale(.3);
    t.addTranslation(VectorMatrixFactory.newIVector3(0, 1.5, -0.5));
    //hulk.getMaterial().setShaderId(Material.SHADER_TEXTURE_PHONG);
    hulk.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.8, 0.9, 1.0));
    hulk.getMaterial().setReflectionAmbient(VectorMatrixFactory.newIVector3(0.25, 0.25, 0.25));
    CgNode node = new CgNode(t, "Scale");
    node.addChild(new CgNode(hulk, "cube"));
    getCgRootNode().addChild(node);
  }

  private void loadObject() {
    String objFilename = "meshes/cow.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    hulk.getMaterial().setShaderId(Material.SHADER_TEXTURE_PHONG);
    Transformation t = new Transformation();
    t.addScale(1);
    t.addTranslation(VectorMatrixFactory.newIVector3(0, 0.25, 0));
    //t.addTransformation(VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(0,1,0), 90));
    CgNode node = new CgNode(t, "Scale");
    node.addChild(new CgNode(hulk, "hulk"));
    getCgRootNode().addChild(node);
  }

  private void loadHulk() {
    String objFilename = "meshes/hulk/Hulk.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    //hulk.getMaterial().setShaderId(Material.SHADER_TEXTURE);
    Transformation t = new Transformation();
    t.addScale(.1);
    // t.addTranslation(VectorMatrixFactory.newIVector3(0, 0, -3.5));
    CgNode node = new CgNode(t, "Scale");
    node.addChild(new CgNode(hulk, "hulk"));
    getCgRootNode().addChild(node);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof AnimationTimer) {
      lightSource
          .setPosition(VectorMatrixFactory.newIVector3(2.0 * Math.sin(alpha) + 0.5, 5, 2.0 * Math.cos(alpha) + 5));
      alpha += 0.05;
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new ShadowVolumeDemo();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
    appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
  }
}

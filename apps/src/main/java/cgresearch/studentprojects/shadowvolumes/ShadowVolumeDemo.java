package cgresearch.studentprojects.shadowvolumes;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
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

  private LightSource lightSource = new LightSource(LightSource.Type.POINT, LightSource.ShadowType.HARD, -1);
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
    loadRoom();
//  createEnvironment();
//    //loadHulk();
//    loadCube();
    loadObject();
    loadObject2();
    loadObject3();

    // Set light source
    lightSource.setPosition(VectorMatrixFactory.newIVector3(0, 30, 0));
    //lightSource.setDirection(VectorMatrixFactory.newIVector3(0, 0,1));
    lightSource.setColor(VectorMatrixFactory.newIVector3(1,1,1));
    getCgRootNode().addLight(lightSource);
    getCgRootNode().setAllowShadows(true);
  }

  private void loadRoom() {
    ITriangleMesh mesh = loadObject("meshes/scene_room/room_01.obj");
    if (mesh != null) {
      CgNode node = new CgNode(mesh, "room");
      getCgRootNode().addChild(node);
    }
  }

  public void loadCube() {
    String objFilename = "meshes/cube.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    Transformation t = new Transformation();
    t.addScale(20);
    t.addTranslation(VectorMatrixFactory.newIVector3(0, 1.5, -0.5));
    hulk.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    //hulk.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.8, 0.9, 1.0));
    //hulk.getMaterial().setReflectionAmbient(VectorMatrixFactory.newIVector3(0.25, 0.25, 0.25));
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
    hulk.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    Transformation t = new Transformation();
    t.addScale(25);
    t.addTranslation(VectorMatrixFactory.newIVector3(-0.5, 0.25, 0));
    t.addTransformation(VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(0,1,0),
            MathHelpers.degree2radiens(270)));
    CgNode node = new CgNode(t, "Scale");
    node.addChild(new CgNode(hulk, "hulk"));
    getCgRootNode().addChild(node);
  }

  private void loadObject2() {
    String objFilename = "meshes/cow.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    hulk.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    Transformation t = new Transformation();
    t.addScale(25);
    t.addTranslation(VectorMatrixFactory.newIVector3(0.5, 0.25, 0));
    t.addTransformation(VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(0,1,0),
            MathHelpers.degree2radiens(90)));
    CgNode node = new CgNode(t, "Scale2");
    node.addChild(new CgNode(hulk, "hulk2"));
    getCgRootNode().addChild(node);
  }

  private void loadObject3() {
    String objFilename = "meshes/cow.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh hulk = meshes.get(0);
    hulk.getMaterial().setThrowsShadow(true);
    hulk.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    Transformation t = new Transformation();
    t.addScale(20);
    t.addTranslation(VectorMatrixFactory.newIVector3(0, 0.25, 0));
    t.addTransformation(VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(0,1,0),
            MathHelpers.degree2radiens(135)));
    CgNode node = new CgNode(t, "Scale2");
    node.addChild(new CgNode(hulk, "hulk2"));
    getCgRootNode().addChild(node);
  }

  public void loadHulk() {
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

  private ITriangleMesh loadObject(String file) {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(file);
    if (meshes == null) {
      return null;
    }
    ITriangleMesh mesh = meshes.get(0);
    mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
    return mesh;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof AnimationTimer) {
    //  lightSource
    //      .setPosition(VectorMatrixFactory.newIVector3(2.0 * Math.sin(alpha) + 0.5, 5, 2.0 * Math.cos(alpha) + 5));
    //  alpha += 0.05;
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

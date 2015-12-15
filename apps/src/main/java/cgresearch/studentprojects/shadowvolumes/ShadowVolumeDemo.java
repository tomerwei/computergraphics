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

    // Load room
    CgNode node = loadRoom();
    // Add objects
    if (node != null) {
      loadTable("Table", VectorMatrixFactory.newIVector3(), 0, node);
      loadChair("Chair 1", VectorMatrixFactory.newIVector3(-5, 0, 0), 90, node);
      loadChair("Chair 2", VectorMatrixFactory.newIVector3(5, 0, 0), 270, node);
    }

    // Set light source
    lightSource.setPosition(VectorMatrixFactory.newIVector3(0, 30, 0));
    lightSource.setColor(VectorMatrixFactory.newIVector3(1,1,1));
    getCgRootNode().addLight(lightSource);
    getCgRootNode().setAllowShadows(true);
    getCgRootNode().setUseBlending(true);
    //AnimationTimer.getInstance().startTimer(200);
  }

  private CgNode loadRoom() {
    ITriangleMesh mesh = getObject("meshes/scene_room/room_01.obj");
    if (mesh != null) {
      mesh.getMaterial().setThrowsShadow(false);
      CgNode node = new CgNode(mesh, "Room");
      getCgRootNode().addChild(node);
      return node;
    }
    return null;
  }

  private boolean loadChair(String name, IVector3 translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_room/chair_01.obj", translate, rotation, parent);
  }

  private boolean loadTable(String name, IVector3 translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_room/table_01.obj", translate, rotation, parent);
  }

  private boolean loadObject(String name, String path, IVector3 translate, int rotation, CgNode parent) {
    CgNode node = getCgNode(path, translate, rotation);
    if (node != null) {
      node.getChildNode(0).setName(name);
      parent.addChild(node);
      return true;
    }
    return false;
  }

  private CgNode getCgNode(String meshPath, IVector3 translate, int rotation) {
    ITriangleMesh mesh = getObject(meshPath);
    if (mesh != null) {
      mesh.getMaterial().setThrowsShadow(true);
      Transformation t = new Transformation();
      if (translate != null) {
        t.addTranslation(translate);
      }
      if (rotation != 0) {
        t.addTransformation(VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(0,1,0),
                MathHelpers.degree2radiens(rotation)));
      }
      CgNode transformation = new CgNode(t, "Transformation");
      transformation.addChild(new CgNode(mesh, "Mesh"));
      return transformation;
    }
    return null;
  }

  private ITriangleMesh getObject(String file) {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(file);
    if (meshes == null) {
      return null;
    }
    ITriangleMesh mesh = meshes.get(0);
    mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
    mesh.getMaterial().setTransparency(1);
    return mesh;
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

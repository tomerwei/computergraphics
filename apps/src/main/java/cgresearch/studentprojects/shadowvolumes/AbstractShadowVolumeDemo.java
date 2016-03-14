package cgresearch.studentprojects.shadowvolumes;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.*;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
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
public abstract class AbstractShadowVolumeDemo extends CgApplication {

  CgNode loadRoom() {
    return loadScene("meshes/scene_room/room_01.obj", "Room");
  }

  CgNode loadHouseEntrance() {
    return loadScene("meshes/scene_halloween/scene_01.obj", "Entrance");
  }

  private CgNode loadScene(String path, String name) {
    ITriangleMesh mesh = getObject(path);
    if (mesh != null) {
      mesh.getMaterial().setThrowsShadow(false);
      CgNode node = new CgNode(mesh, name);
      getCgRootNode().addChild(node);
      return node;
    }
    return null;
  }

  boolean loadChair(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_room/chair_01.obj", translate, rotation, parent);
  }

  boolean loadTable(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_room/table_01.obj", translate, rotation, parent);
  }

  boolean loadPumpkinHalloween(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_01.obj", translate, rotation, parent);
  }

  boolean loadPumpkinOrange(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_01.obj", translate, rotation, parent);
  }

  boolean loadPumpkinRed(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_02.obj", translate, rotation, parent);
  }

  boolean loadPumpkinLightGreen(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_03.obj", translate, rotation, parent);
  }

  boolean loadPumpkinFlatRed(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_04.obj", translate, rotation, parent);
  }

  boolean loadPumpkinGreen(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_05.obj", translate, rotation, parent);
  }

  boolean loadPumpkinFlatGreen(String name, Vector translate, int rotation, CgNode parent) {
    return loadObject(name, "meshes/scene_halloween/pumpkin_small_06.obj", translate, rotation, parent);
  }

  private boolean loadObject(String name, String path, Vector translate, int rotation, CgNode parent) {
    CgNode node = getCgNode(path, translate, rotation);
    if (node != null) {
      node.getChildNode(0).setName(name);
      parent.addChild(node);
      return true;
    }
    return false;
  }

  private CgNode getCgNode(String meshPath, Vector translate, int rotation) {
    ITriangleMesh mesh = getObject(meshPath);
    if (mesh != null) {
      mesh.getMaterial().setThrowsShadow(true);
      Transformation t = new Transformation();
      if (translate != null) {
        t.addTranslation(translate);
      }
      if (rotation != 0) {
        t.addTransformation(MatrixFactory.createRotationMatrix(VectorFactory.createVector3(0,1,0),
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
}

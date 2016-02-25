package cgresearch.studentprojects.viewfrustum;

/**
 * Testframe fuer View Frustum Culling
 */

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyPointCloud;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.rendering.jogl.misc.OctreeFactoryStrategyScene;
import cgresearch.rendering.jogl.misc.ViewFrustumCulling;

public class FrustumTestFrame extends CgApplication {
  public static OctreeFactoryStrategyScene scene;

  public static final double objectsTransparency = 0.5;
  public static CgRootNode rootNode;

  public FrustumTestFrame() {

    getCgRootNode().setUseBlending(true);
    getCgRootNode().setUseViewFrustumCulling(true); // TODO HIER View
                                                    // Frustum
                                                    // Culling einschalten

    ITriangleMesh cow = loadMesh("meshes/cow.obj");
    ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
    ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
    ITriangleMesh bunnyDown = loadMesh("meshes/bunny.obj");
    ITriangleMesh bunnyUp = loadMesh("meshes/bunny.obj");
    ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
    //
    //// // ############### Transformations ###############
    TriangleMeshTransformation.translate(cow, VectorFactory.createVector3(1.0, 0.0, 9.0));
    TriangleMeshTransformation.translate(bunny,
        VectorFactory.createVector3(0.0, 0.0 /* 1.15 */, 9.0));
    TriangleMeshTransformation.scale(bunny, 3.0);
    TriangleMeshTransformation.scale(fenja, 0.1);
    TriangleMeshTransformation.translate(fenja, VectorFactory.createVector3(0.5, 0.0, 25.0));
    // TriangleMeshTransformation.scale(bunnyDown, 0.1);
    TriangleMeshTransformation.scale(bunnyDown, 3.0);
    TriangleMeshTransformation.translate(bunnyDown, VectorFactory.createVector3(0.0, 0.0, -8.0));
    TriangleMeshTransformation.scale(bunnyUp, 3.0);
    TriangleMeshTransformation.translate(bunnyUp, VectorFactory.createVector3(0.0, 2.0, 0.5));
    TriangleMeshTransformation.scale(pumpkin, 0.02);
    TriangleMeshTransformation.translate(pumpkin, VectorFactory.createVector3(0.0, 0.0, 23.5));
    // // ############### Transformationen ###############
    //
    getCgRootNode().addChild(new CgNode(cow, "cow"));
    getCgRootNode().addChild(new CgNode(bunny, "bunny"));
    getCgRootNode().addChild(new CgNode(fenja, "fenja"));
    getCgRootNode().addChild(new CgNode(bunnyDown, "bunnyDown"));
    getCgRootNode().addChild(new CgNode(bunnyUp, "bunnyUp"));
    getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));

    // UrbanSceneGenerator usg = new UrbanSceneGenerator();
    // CgNode usNode = usg.buildScene(2, 2);
    // getCgRootNode().addChild(usNode);

    rootNode = getCgRootNode();
  }

  /**
   * erzeugt einen Octree fuer eine PointCloud
   */
  public OctreeNode<Integer> createPointCloudOctree(IPointCloud pCloud) { // TODO
    OctreeFactoryStrategyPointCloud octreeFactoryStrategyPCloud = new OctreeFactoryStrategyPointCloud(pCloud);
    OctreeFactory<Integer> octreeFactoryPointCloud = new OctreeFactory<Integer>(octreeFactoryStrategyPCloud);
    OctreeNode<Integer> octreePointCloudRoot = octreeFactoryPointCloud.create(7, 20);
    return octreePointCloudRoot;
  }

  /**
   * laedt eine Punktwolke
   */
  public IPointCloud loadPointCloud() {
    IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
    return pointCloud;
  }

  /**
   * erzeugt ein TriangleMesh aus der angegebenen Datei
   */
  public ITriangleMesh loadMesh(String path) {
    String objFilename = path;
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return null;
    }
    meshes.get(0).getMaterial().setTransparency(objectsTransparency);
    meshes.get(0).getMaterial().setRenderMode(Normals.PER_FACET);
    meshes.get(0).getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    return meshes.get(0);
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");

    CgApplication app = new FrustumTestFrame();
    if (!rootNode.useViewFrustumCulling()) {
      ViewFrustumCulling vfc = new ViewFrustumCulling(Camera.getInstance(), Camera.getInstance().getNearClippingPlane(),
          Camera.getInstance().getFarClippingPlane(), rootNode);
      // 0.0, 230, rootNode);
      vfc.computeVisibleScenePart(app.getCgRootNode());
    }
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

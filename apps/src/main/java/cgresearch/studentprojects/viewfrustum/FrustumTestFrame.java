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
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyPointCloud;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.Transformation;
import cgresearch.projects.urbanscene.UrbanSceneGenerator;
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

    // ITriangleMesh cow = loadMesh("meshes/cow.obj");
    // ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
    // ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
    // ITriangleMesh bunnyDown = loadMesh("meshes/bunny.obj");
    // ITriangleMesh bunnyUp = loadMesh("meshes/bunny.obj");
    // ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
    //
    // // // ############### Transformations ###############
    //// TriangleMeshTransformation.translate(cow,
    // VectorFactory.createVector3(1.0, 0.0, 9.0));
    // TriangleMeshTransformation.translate(cow,
    // VectorFactory.createVector3(0.0, 0.0, 0.0));
    // TriangleMeshTransformation.translate(bunny,
    // VectorFactory.createVector3(0.0, 0.0 /* 1.15 */, 9.0));
    // TriangleMeshTransformation.scale(bunny, 3.0);
    // TriangleMeshTransformation.scale(fenja, 0.1);
    // TriangleMeshTransformation.translate(fenja,
    // VectorFactory.createVector3(0.5, 0.0, 25.0));
    // // TriangleMeshTransformation.scale(bunnyDown, 0.1);
    // TriangleMeshTransformation.scale(bunnyDown, 3.0);
    // TriangleMeshTransformation.translate(bunnyDown,
    // VectorFactory.createVector3(0.0, 0.0, -8.0));
    // TriangleMeshTransformation.scale(bunnyUp, 3.0);
    // TriangleMeshTransformation.translate(bunnyUp,
    // VectorFactory.createVector3(0.0, 2.0, 0.5));
    // TriangleMeshTransformation.scale(pumpkin, 0.02);
    // TriangleMeshTransformation.translate(pumpkin,
    // VectorFactory.createVector3(0.0, 0.0, 23.5));
    // // ############### Transformationen ###############
    //
    // getCgRootNode().addChild(new CgNode(cow, "cow"));
    // getCgRootNode().addChild(new CgNode(bunny, "bunny"));
    // getCgRootNode().addChild(new CgNode(fenja, "fenja"));
    // getCgRootNode().addChild(new CgNode(bunnyDown, "bunnyDown"));
    // getCgRootNode().addChild(new CgNode(bunnyUp, "bunnyUp"));
    // getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));

    // ######################################################################################################################################
    // UrbanSceneGenerator usg = new UrbanSceneGenerator();
    //// CgNode usNode = usg.buildScene(-4, -4);
    // CgNode usNode = usg.buildScene(8, 8);
    // getCgRootNode().addChild(usNode);

    // ######################################################################################################################################
    ITriangleMesh bunny1 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny1, VectorFactory.createVector3(0.0, 0.0, 0.0));
    ITriangleMesh bunny2 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny2, VectorFactory.createVector3(0.0, 0.0, -1.0));
    ITriangleMesh bunny3 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny3, VectorFactory.createVector3(0.0, 0.0, 1.0));
    ITriangleMesh bunny4 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny4, VectorFactory.createVector3(-1.0, 0.0, 0));
    ITriangleMesh bunny5 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny5, VectorFactory.createVector3(1.0, 0.0, 0));
    ITriangleMesh bunny6 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny6, VectorFactory.createVector3(0.0, -1.0, 0));
    ITriangleMesh bunny7 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny7, VectorFactory.createVector3(0.0, 1.0, 0));

    ITriangleMesh bunny8 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny8, VectorFactory.createVector3(0.0, 0.0, -2));
    ITriangleMesh bunny9 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny9, VectorFactory.createVector3(0.0, 0.0, 2));
    ITriangleMesh bunny10 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny10, VectorFactory.createVector3(0.0, 2.0, 0));
    ITriangleMesh bunny11 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny11, VectorFactory.createVector3(0, -2.0, 0));
    ITriangleMesh bunny12 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny12, VectorFactory.createVector3(2.0, 0.0, 0));
    ITriangleMesh bunny13 = loadMesh("meshes/bunny.obj");
    TriangleMeshTransformation.translate(bunny13, VectorFactory.createVector3(-2.0, 0.0, 0));

    getCgRootNode().addChild(new CgNode(bunny1, "bunny1"));
    getCgRootNode().addChild(new CgNode(bunny2, "bunny2"));
    getCgRootNode().addChild(new CgNode(bunny3, "bunny3"));
    getCgRootNode().addChild(new CgNode(bunny4, "bunny4"));
    getCgRootNode().addChild(new CgNode(bunny5, "bunny5"));
    getCgRootNode().addChild(new CgNode(bunny6, "bunny6"));
    getCgRootNode().addChild(new CgNode(bunny7, "bunny7"));
    getCgRootNode().addChild(new CgNode(bunny8, "bunny8"));
    getCgRootNode().addChild(new CgNode(bunny9, "bunny9"));
    getCgRootNode().addChild(new CgNode(bunny10, "bunny10"));
    getCgRootNode().addChild(new CgNode(bunny11, "bunny11"));
    getCgRootNode().addChild(new CgNode(bunny12, "bunny12"));
    getCgRootNode().addChild(new CgNode(bunny13, "bunny13"));

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

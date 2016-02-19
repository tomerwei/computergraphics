package cgresearch.studentprojects.viewfrustum;

/**
 * Testframe fuer View Frustum Culling
 */

import java.util.ArrayList;
import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
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
    getCgRootNode().setUseViewFrustumCulling(true); // TODO HIER View Frustum
                                                    // Culling einschalten

    ITriangleMesh cow = loadMesh("meshes/cow.obj");
    ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
    ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
    ITriangleMesh fenjaDown = loadMesh("meshes/fenja02.obj");
    ITriangleMesh fenjaUp = loadMesh("meshes/fenja02.obj");
    ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");

    // ############### Transformationen ###############
    TriangleMeshTransformation.translate(cow, VectorMatrixFactory.newVector(1.0, 0.0, 9.0));
    TriangleMeshTransformation.translate(bunny, VectorMatrixFactory.newVector(0.0, 0.0 /*1.15*/, 9.0));
    TriangleMeshTransformation.scale(bunny, 3.0);
    TriangleMeshTransformation.scale(fenja, 0.1);
    TriangleMeshTransformation.translate(fenja, VectorMatrixFactory.newVector(0.5, 0.0, 25.0));
    TriangleMeshTransformation.scale(fenjaDown, 0.1);
    TriangleMeshTransformation.translate(fenjaDown, VectorMatrixFactory.newVector(2.0, -8.0, 8.0));
    TriangleMeshTransformation.scale(fenjaUp, 0.1);
    TriangleMeshTransformation.translate(fenjaUp, VectorMatrixFactory.newVector(-1.0, 2.0, 0.5));
    TriangleMeshTransformation.scale(pumpkin, 0.02);
    TriangleMeshTransformation.translate(pumpkin, VectorMatrixFactory.newVector(0.0, 0.0, 20.5));
    // ############### Transformationen ###############

    getCgRootNode().addChild(new CgNode(cow, "cow"));
    getCgRootNode().addChild(new CgNode(bunny, "bunny"));
    getCgRootNode().addChild(new CgNode(fenja, "fenja"));
    getCgRootNode().addChild(new CgNode(fenjaDown, "fenjaDown"));
    getCgRootNode().addChild(new CgNode(fenjaUp, "fenjaUp"));
    getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));
    
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
    if(!rootNode.useViewFrustumCulling()){
        ViewFrustumCulling vfc = new ViewFrustumCulling(Camera.getInstance(), 0.1, 10.0, rootNode);
        vfc.computeVisibleScenePart(app.getCgRootNode()); 
    }
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

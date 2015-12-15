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
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.PointCloudFactory;
import cgresearch.graphics.datastructures.primitives.Cuboid;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyPointCloud;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyTriangleMesh;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.graphics.scenegraph.Transformation;

public class FrustumTestFrame extends CgApplication {
    
    public static OctreeFactoryStrategyScene scene;

  public FrustumTestFrame() {
      
      ArrayList<CgNode> objects = new ArrayList<CgNode>();
      
      

    // berechne View Frustum und zeichne Ebenen
    ViewFrustumCulling vfc = new ViewFrustumCulling(Camera.getInstance());
    // ViewFrustumCulling test = new ViewFrustumCulling();
    // test = test.getTest();
    ITriangleMesh frustum = vfc.getFrustumMesh(vfc.getCorners());
     getCgRootNode().addChild(new CgNode(frustum, "frustum"));

    // Lade meshes
     ITriangleMesh cow = loadMesh("meshes/cow.obj");
     ITriangleMesh bunny = loadMesh("meshes/bunny.obj");
     ITriangleMesh fenja = loadMesh("meshes/fenja02.obj");
     ITriangleMesh fenjaDown = loadMesh("meshes/fenja02.obj");
     ITriangleMesh fenjaUp = loadMesh("meshes/fenja02.obj");
     ITriangleMesh pumpkin = loadMesh("meshes/pumpkin.obj");
     IPointCloud pc = loadPointCloud();
     objects.add(new CgNode(cow, "cow"));
     objects.add(new CgNode(bunny, "bunny"));
     objects.add(new CgNode(fenja," fenja"));
     objects.add(new CgNode(fenjaDown,"fenjaDown"));
     objects.add(new CgNode(fenjaUp, "fenjaUp"));
     objects.add(new CgNode(pumpkin, "pumpkin"));
     objects.add(new CgNode(pc, "pointCloud"));
     
//     getCgRootNode().addChild(new CgNode(cow, "cow"));
//     getCgRootNode().addChild(new CgNode(bunny, "bunny"));
//     getCgRootNode().addChild(new CgNode(fenja, "fenja"));
//     getCgRootNode().addChild(new CgNode(fenjaDown, "fenjaDown"));
//     getCgRootNode().addChild(new CgNode(fenjaUp, "fenjaUp"));
//     getCgRootNode().addChild(new CgNode(pumpkin, "pumpkin"));
//     getCgRootNode().addChild(new CgNode(pc, "pointCloud"));
//     

    // ############### Transformation ###############
     TriangleMeshTransformation.translate(bunny,
     VectorMatrixFactory.newIVector3(1.2, 1.0, -0.9));
     TriangleMeshTransformation.scale(bunny, 4.0);
     TriangleMeshTransformation.scale(fenja, 0.1);
     TriangleMeshTransformation.translate(fenja,
     VectorMatrixFactory.newIVector3(0.0, -1.0, -0.5));
     TriangleMeshTransformation.scale(fenjaDown, 0.1);
     TriangleMeshTransformation.translate(fenjaDown,
     VectorMatrixFactory.newIVector3(0.0, -4.0, -0.5));
     TriangleMeshTransformation.scale(fenjaUp, 0.1);
     TriangleMeshTransformation.translate(fenjaUp,
     VectorMatrixFactory.newIVector3(0.0, 2.0, -0.5));
     TriangleMeshTransformation.scale(pumpkin, 0.03);
    // ############### Transformation ###############

    // ############### Transformation fuer Testfrustum ###############
    // TriangleMeshTransformation.scale(cow, 4.5);
    // TriangleMeshTransformation.transform(cow,
    // VectorMatrixFactory.newIMatrix3(Math.cos(-0.6108), 0.0,
    // Math.sin(-0.6108), 0.0, 1.0, 0.0, -(Math.sin(-0.6108)), 0.0,
    // Math.cos(-0.6108)));
    // TriangleMeshTransformation.translate(cow,
    // VectorMatrixFactory.newIVector3(1.0, 1.0, -0.9));
    // System.out.println("BB = " + cow.getBoundingBox());
    // ############### Transformation fuer Testfrustum ###############

    OctreeNode<Integer> octreeCow = createMeshOctree(cow);
    // getCgRootNode().addChild(new CgNode(octreeCow, "octree_cow"));

     OctreeNode<Integer> octreeBunny = createMeshOctree(bunny);
    // // getCgRootNode().addChild(new CgNode(bunny, "bunny"));
    //
     OctreeNode<Integer> octreeFenja = createMeshOctree(fenja);
    //
     OctreeNode<Integer> octreeFenjaDown = createMeshOctree(fenjaDown);
    
     OctreeNode<Integer> octreeFenjaUp = createMeshOctree(fenjaUp);
    
    OctreeNode<Integer> octreePumpkin = createMeshOctree(pumpkin);
    
    OctreeNode<Integer> octreePointCloud = createPointCloudOctree(pc);
    
    ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
    octrees.add(octreeCow);
    octrees.add(octreeBunny);
    octrees.add(octreeFenja);
    octrees.add(octreeFenjaDown);
    octrees.add(octreeFenjaUp);
    octrees.add(octreePumpkin);
    octrees.add(octreePointCloud);
    

    OctreeNode<Integer> octreeScene = createSceneOctree(objects);
    ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();
    extractNodesOfFrustum(vfc, octreeScene, visibleNodes);
   
    
//    for(int i = 0; i < visibleNodes.size(); i++){
//        System.out.println("VISIBLE NODES = " + visibleNodes.size());
//        getCgRootNode().addChild(new CgNode(visibleNodes.get(i), "octreeScene"));
//    }
//    getCgRootNode().addChild(new CgNode(octreeScene, "octreeScene"));

    Camera.getInstance().setEye(VectorMatrixFactory.newIVector3(0.0, 0.0, 10.0));

    // ITriangleMesh frustum_parallel = test.getFrustumMesh(test.get_corners());

    // getCgRootNode().addChild(new CgNode(frustum_parallel,
    // "frustum_parallel"));


    for(int j = 0; j < objects.size(); j++){
        for(int i = 0; i < visibleNodes.size(); i++){
            addVisibleElementsToScene(vfc, octreeCow, cow, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreeBunny, bunny, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreeFenja, fenja, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreeFenjaDown, fenjaDown, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreeFenjaUp, fenjaUp, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreePumpkin, pumpkin, visibleNodes.get(i));
            addVisibleElementsToScene(vfc, octreePointCloud, pc, visibleNodes.get(i));
        }
    }
    
//     addVisibleElementsToScene(vfc, octreeBunny, bunny, octreeScene);
//     addVisibleElementsToScene(vfc, octreeFenja, fenja, octreeScene);
//     addVisibleElementsToScene(vfc, octreeFenjaDown, fenjaDown, octreeScene);
//     addVisibleElementsToScene(vfc, octreeFenjaUp, fenjaUp, octreeScene);
//     addVisibleElementsToScene(vfc, octreePumpkin, fenjaUp, octreeScene);


//    getCgRootNode().addChild(new CoordinateSystem());
  }

  public void addVisibleElementsToScene(ViewFrustumCulling vfc, OctreeNode<Integer> tree, ICgNodeContent content, OctreeNode<Integer> scene) {
    ICgNodeContent visible = vfc.extractNodeContent(tree, content, scene);
    if (visible != null) {
        getCgRootNode().addChild(new CgNode(visible, "visible_object"));
    }
  }
  
  /**
   * zeichnet die Elemente in der ArrayList, indem diese dem scenegraph
   * hinzugefuegt werden
   */
  public void draw(ArrayList<OctreeNode<Integer>> draw) {
    if (draw != null) {
      for (int i = 0; i < draw.size(); i++) {
        getCgRootNode().addChild(new CgNode(draw.get(i), "to_draw".concat(String.valueOf(i))));
      }
    }
  }

  /**
   * erzeugt einen Octree fuer die Szene
   */
  public OctreeNode<Integer> createSceneOctree(ArrayList<CgNode> objects) {
    OctreeFactoryStrategyScene octreeFactoryStrategyScene = new OctreeFactoryStrategyScene(objects);
    scene = octreeFactoryStrategyScene;
    OctreeFactory<Integer> octreeFactoryScene = new OctreeFactory<Integer>(octreeFactoryStrategyScene);
    OctreeNode<Integer> octreeSceneRoot = octreeFactoryScene.create(7, 2);
    return octreeSceneRoot;
  }

  /**
   * erzeugt einen Octree fuer ein TriangleMesh
   */
  public OctreeNode<Integer> createMeshOctree(ITriangleMesh mesh) {
    OctreeFactoryStrategyTriangleMesh octreeFactoryStrategyMesh = new OctreeFactoryStrategyTriangleMesh(mesh);
    OctreeFactory<Integer> octreeFactoryMesh = new OctreeFactory<Integer>(octreeFactoryStrategyMesh);
    OctreeNode<Integer> octreeMeshRoot = octreeFactoryMesh.create(7, 20);
    return octreeMeshRoot;
  }

  /**
   * erzeugt einen Octree fuer eine PointCloud
   */
  public OctreeNode<Integer> createPointCloudOctree(IPointCloud pCloud) {
    OctreeFactoryStrategyPointCloud octreeFactoryStrategyPCloud = new OctreeFactoryStrategyPointCloud(pCloud);
    OctreeFactory<Integer> octreeFactoryPointCloud = new OctreeFactory<Integer>(octreeFactoryStrategyPCloud);
    OctreeNode<Integer> octreePointCloudRoot = octreeFactoryPointCloud.create(7, 20);
    return octreePointCloudRoot;
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
    meshes.get(0).getMaterial().setRenderMode(Normals.PER_FACET);
    meshes.get(0).getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    return meshes.get(0);
  }

  /**
   * laedt eine Punktwolke
   */
  public IPointCloud loadPointCloud() {
    IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
    return pointCloud;
  }

  public void extractNodesOfFrustum(ViewFrustumCulling vfc, OctreeNode<Integer> node, ArrayList<OctreeNode<Integer>> nodesInFrustum){
      
      if(node.getNumberOfChildren() >0){
          for(int i = 0; i<node.getNumberOfChildren(); i++){
              extractNodesOfFrustum(vfc, node.getChild(i), nodesInFrustum);
          }
      }
      else{
          if(vfc.isObjectInFrustum(node) == 0 || vfc.isObjectInFrustum(node) == 2){
              nodesInFrustum.add(node);
          }
      }
  }
  
  // Testframe
  public static void main(String[] args) {

    ResourcesLocator.getInstance().parseIniFile("resources.ini");

    CgApplication app = new FrustumTestFrame();

    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();

    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);

  }
  
  

}

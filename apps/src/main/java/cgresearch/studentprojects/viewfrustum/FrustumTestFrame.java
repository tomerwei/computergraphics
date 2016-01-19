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
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.graphics.scenegraph.Transformation;

public class FrustumTestFrame extends CgApplication {
    
  public FrustumTestFrame(CgNode root, double nearDistance, double farDistance) {
      
      getCgRootNode().setUseBlending(true);
      ArrayList<CgNode> objects = new ArrayList<CgNode>();
      objects = traversalOctreeNode(root, objects);

    // berechne View Frustum und zeichne Ebenen
      
      ViewFrustumCulling vfc = new ViewFrustumCulling(Camera.getInstance(), nearDistance, farDistance);
//     ViewFrustumCulling vfc = new ViewFrustumCulling();
//     vfc = vfc.getTest();
    ITriangleMesh frustum = vfc.getFrustumMesh(vfc.getCorners());
     getCgRootNode().addChild(new CgNode(frustum, "frustum"));
    

     //hole leafNodes
     
     buildSceneGraph(root);

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
     
     //erzeuge Octrees fuer jedes Mesh
    ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
     for(int i = 0; i < objects.size(); i++){
         ITriangleMesh mesh = (ITriangleMesh) objects.get(i).getContent();
         TriangleMeshTools.cleanup(mesh);
         octrees.add(createMeshOctree((TriangleMesh)objects.get(i).getContent()));
     }

     //erzeuge Octree fuer Szene und extrahiere Nodes, die im Frustum liegen
    if(objects.size() > 0){
        OctreeNode<Integer> octreeScene = createSceneOctree(objects);
        ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();
        extractNodesOfFrustum(vfc, octreeScene, visibleNodes);
        
        // alles zuerst invisible setzen
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).setVisible(false); //TODO hier muss eigentlich false stehen, true nur fuer Debugging
        }
        
//        getCgRootNode().addChild(new CgNode(octreeScene, "oScene"));
    
       
    //    for(int i = 0; i < visibleNodes.size(); i++){
    //        getCgRootNode().addChild(new CgNode(visibleNodes.get(i), "octreeScene"));
    //    }
    
        // ITriangleMesh frustum_parallel = test.getFrustumMesh(test.get_corners());
    
        // getCgRootNode().addChild(new CgNode(frustum_parallel,
        // "frustum_parallel"));
    
        //fuege sichtbare Elemente der Szene hinzu
        for(int j = 0; j < objects.size(); j++){
            for(int k = 0; k < visibleNodes.size(); k++){
                addVisibleElementsToScene(vfc, octrees.get(j), objects.get(j), visibleNodes.get(k));
            }
        }
     }
  }
  
public FrustumTestFrame(CgNode root) {
      
      ArrayList<CgNode> objects = new ArrayList<CgNode>();
      objects = traversalOctreeNode(root, objects);

    // berechne View Frustum und zeichne Ebenen
      
      ViewFrustumCulling vfc = new ViewFrustumCulling(Camera.getInstance());
//     ViewFrustumCulling vfc = new ViewFrustumCulling();
//     vfc = vfc.getTest();
      ITriangleMesh frustum = vfc.getFrustumMesh(vfc.getCorners());
      frustum.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      frustum.getMaterial().setTransparency(0.1);
      getCgRootNode().addChild(new CgNode(frustum, "frustum"));
    

     //hole leafNodes
     
     buildSceneGraph(root);

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
     
     //erzeuge Octrees fuer jedes Mesh
    ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
     for(int i = 0; i < objects.size(); i++){
         ITriangleMesh mesh = (ITriangleMesh) objects.get(i).getContent();
         TriangleMeshTools.cleanup(mesh);
         octrees.add(createMeshOctree((TriangleMesh)objects.get(i).getContent()));
     }

     //erzeuge Octree fuer Szene und extrahiere Nodes, die im Frustum liegen
    if(objects.size() > 0){
        OctreeNode<Integer> octreeScene = createSceneOctree(objects);
        ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();
        extractNodesOfFrustum(vfc, octreeScene, visibleNodes);
        
        // alles zuerst invisible setzen
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).setVisible(false);
        }
        
    
       
    //    for(int i = 0; i < visibleNodes.size(); i++){
    //        getCgRootNode().addChild(new CgNode(visibleNodes.get(i), "octreeScene"));
    //    }
    
        // ITriangleMesh frustum_parallel = test.getFrustumMesh(test.get_corners());
    
        // getCgRootNode().addChild(new CgNode(frustum_parallel,
        // "frustum_parallel"));
    
        //fuege sichtbare Elemente der Szene hinzu
        for(int j = 0; j < objects.size(); j++){
            for(int k = 0; k < visibleNodes.size(); k++){
    //            System.out.println("HIER");
                addVisibleElementsToScene(vfc, octrees.get(j), objects.get(j), visibleNodes.get(k));
            }
        }
     }
  }
  
  /**
   * wird aufgerufen, wenn ein OctreeNode komplett innerhalb des Frustums liegt, um an
   * seine Leafnodes zu kommen
   * 
   * @param node
   *          der OctreeNode, der komplett im Frustum liegt
   * @param objects
   *          ArrayList, der die leafnodes hinzugefuegt werden
   * @return
   */
  public ArrayList<CgNode> traversalOctreeNode(CgNode node,
      ArrayList<CgNode> objects) {
      if (node.getNumChildren() > 0) {
          for (int i = 0; i < node.getNumChildren(); i++) {
              traversalOctreeNode(node.getChildNode(i), objects);
          }
      }
      else{
          if(node.getContent() != null && (node.getContent().getClass()== TriangleMesh.class || node.getContent().getClass() == PointCloud.class)){
              node.getContent().getMaterial().setTransparency(0.5);;
              objects.add(node);
          }
      }
    return objects;
  }
  
  /**
   * einmalig den Szenegraph aufbauen
   * @param root Wurzelknoten
   */
  public void buildSceneGraph(CgNode root){
      getCgRootNode().addChild(root);
      if(root.getNumChildren()>0){
          for(int i = 0; i < root.getNumChildren(); i++){
              buildSceneGraph(root.getChildNode(i));
          }
      }
  }

  public void addVisibleElementsToScene(ViewFrustumCulling vfc, OctreeNode<Integer> tree, CgNode node, OctreeNode<Integer> scene) {
    ICgNodeContent partlyVisible = vfc.extractNodeContent(tree, node, scene);
//    System.out.println("8");
    if (vfc.isObjectInFrustum(node.getContent()) == 2 ) {
        CgNode newNode = new CgNode(partlyVisible, "partyVisible_object");
        getCgRootNode().addChild(newNode);
//        
//        node.setVisible(true);
//        System.out.println("VISIBLE AUF TRUE GESETZT");
    }
//    else{
//        node.setVisible(false);
//        System.out.println("VISIBLE AUF FALSE GESETZT");
//    }
    partlyVisible = null;
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
    OctreeFactory<Integer> octreeFactoryScene = new OctreeFactory<Integer>(octreeFactoryStrategyScene);
    OctreeNode<Integer> octreeSceneRoot = octreeFactoryScene.create(7, 10);
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
   * laedt eine Punktwolke
   */
  public IPointCloud loadPointCloud() {
    IPointCloud pointCloud = PointCloudFactory.createDummyPointCloud();
    return pointCloud;
  }

  /**
   * fuegt einer Liste die LeafNodes des SceneOctrees hinzu, die im Frustum liegen oder es schneiden
   * @param vfc ViewFrustumCulling, fuer welches der SzeneOctree getestet wird
   * @param node SceneOctree
   * @param nodesInFrustum Liste, der die sichtbaren Nodes hinzugefuegt werden
   */
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
}

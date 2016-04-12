package cgresearch.rendering.jogl.misc;

/**
 * Funktionalitaeten zum Berechnen des View Frustum Culling
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.graphics.algorithms.TriangleMeshTools;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.primitives.Cuboid;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyTriangleMesh;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;

public class ViewFrustumCulling implements Observer {

  private OctreeNode<Integer> octreeScene;
  private ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
  private ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();
  private ArrayList<CgNode> objects = new ArrayList<CgNode>();
  private HashSet<CgNode> testedObjects = new HashSet<CgNode>();

  private ViewFrustum viewFrustum;
  private CgRootNode rootNode;

  private boolean initialised = false;

  public ViewFrustumCulling(CgRootNode rootNode) {
    this.rootNode = rootNode;
  }

  /**
   * intialises frustu, Sets the nodes in the view frustum visible and selects
   * visible triangles in triangle meshes.
   */
  public void computeVisibleScenePart(CgRootNode rootNode) {
    if (!initialised) {
      initialised = true;
      viewFrustum = new ViewFrustum(Camera.getInstance(), rootNode);
      if (rootNode.useViewFrustumCulling()) {
        Camera.getInstance().addObserver(this);
      }
      rebuildOctree();
      update(null, null);
      if (!rootNode.useViewFrustumCulling()) {
        ITriangleMesh frustum = viewFrustum.getFrustumMesh(viewFrustum.getCorners());
        frustum.getMaterial().setTransparency(ViewFrustum.OBJECTSTRANSPARENCY);
        rootNode.addChild(new CgNode(frustum, "frustum"));
      }
    } else {
      for (int j = 0; j < objects.size(); ++j) {
        for (int k = 0; k < visibleNodes.size(); ++k) {
          extractNodeContent(octrees.get(j), objects.get(j), visibleNodes.get(k));
        }
      }
    }
  }

  /**
   * checks if an object lies in the frustum and needs to be toggled in
   * visibility
   * 
   * @param octreeNode
   *          octree of the object
   * @param toDraw
   *          list for objects which are partially visible
   * @return list with triangles of partially visible objects //TODO not used
   *         now
   */
  public ArrayList<OctreeNode<Integer>> checkOctree(OctreeNode<Integer> octreeNode, CgNode node,
      ArrayList<OctreeNode<Integer>> leafNodes) {
    node.setVisible(false);
    final int result = viewFrustum.isObjectInFrustum(octreeNode);
    if (result == ViewFrustum.INSIDE) {
      testedObjects.add(node);
      node.setVisible(true);
    }
    return null;
  }

  /**
   * if visible scene nodes intersect an objectOctree, the object´s visibility is
   * tested
   * 
   * @param objectOctree
   *          the octree of the node
   * @param node
   *          the node
   * @param sceneOctree
   *          the octree of the scene
   **/

  public void extractNodeContent(OctreeNode<Integer> octreeNode, CgNode node, OctreeNode<Integer> sceneOctree) {
    // if the node is already tested and set visible, it must not be tested
    // anymore, because it may be set invisible then
    if ((!testedObjects.contains(node))) {
      if (boxesIntersect(sceneOctree.getBoundingBox(), octreeNode.getBoundingBox())) {
        ArrayList<OctreeNode<Integer>> toDraw = new ArrayList<OctreeNode<Integer>>();
        ArrayList<OctreeNode<Integer>> leafNodes = new ArrayList<OctreeNode<Integer>>();
        checkOctree(octreeNode, node, leafNodes);
      } else {
        // boxes of sceneOctree and objectOctree do not intersect, so
        // the object can not be visible
        node.setVisible(false);
      }

    }
  }

  /**
   * checks if two boxes intersect
   * 
   * @param one
   *          first bounding box
   * @param two
   *          second bounding box
   * @return intersection
   */
  public boolean boxesIntersect(BoundingBox one, BoundingBox two) {
    if (two.getUpperRight().get(ViewFrustum.X) < one.getLowerLeft().get(ViewFrustum.X)) {
      return false;
    }
    if (two.getLowerLeft().get(ViewFrustum.X) > one.getUpperRight().get(ViewFrustum.X)) {
      return false;
    }
    if (two.getUpperRight().get(ViewFrustum.Y) < one.getLowerLeft().get(ViewFrustum.Y)) {
      return false;
    }
    if (two.getLowerLeft().get(ViewFrustum.Y) > one.getUpperRight().get(ViewFrustum.Y)) {
      return false;
    }
    if (two.getUpperRight().get(ViewFrustum.Z) < one.getLowerLeft().get(ViewFrustum.Z)) {
      return false;
    }
    if (two.getLowerLeft().get(ViewFrustum.Z) > one.getUpperRight().get(ViewFrustum.Z)) {
      return false;
    }
    return true;
  }

  /**
   * extracts all children of a node and adds them to a list
   * 
   * @param node
   *          nodes whose children should be extracted
   * @return list with all children
   */
  public ArrayList<CgNode> traversalNode(CgNode node) {
    ArrayList<CgNode> objects = new ArrayList<CgNode>();
    final int numberOfChildren = node.getNumChildren();
    if (numberOfChildren == 0 && node.getContent().getClass() == TriangleMesh.class) {
      node.getContent().getMaterial().setTransparency(ViewFrustum.OBJECTSTRANSPARENCY);
      node.setVisible(true);
      objects.add(node);
    }
    // Also call for child nodes
    for (int i = 0; i < node.getNumChildren(); ++i) {
      objects.addAll(traversalNode(node.getChildNode(i)));
    }
    return objects;
  }

  /**
   * extracts the nodes of the sceneOctree which are visible or intersect the
   * frustum
   * 
   * @param node
   *          sceneOctree
   * @param nodesInFrustum
   *          list to which the visible or intersected nodes are added
   */
  public void extractNodesOfFrustum(OctreeNode<Integer> node, ArrayList<OctreeNode<Integer>> nodesInFrustum) {
    final int numberOfChildren = node.getNumberOfChildren();

    if (numberOfChildren == 0) {
      viewFrustum.addVisibleNode(node, nodesInFrustum);
    }
    for (int i = 0; i < numberOfChildren; ++i) {
      extractNodesOfFrustum(node.getChild(i), nodesInFrustum);
    }
  }
  @Override
  /*
   * (non-Javadoc)
   * 
   * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
   * called when camera is moved, calculates new frustum and sets visibility of
   * objects
   */
  public void update(Observable arg0, Object arg1) {
    visibleNodes.clear();
    testedObjects.clear();
    
    // Update camera values
    viewFrustum.setEye(Camera.getInstance().getEye());
    viewFrustum.setUp(Camera.getInstance().getUp().getNormalized());
    viewFrustum.setCameraRight(Camera.getInstance().getUp()
        .cross(Camera.getInstance().getEye().subtract(Camera.getInstance().getRef())).getNormalized());
    
    // Recalculate planes of frustum
    viewFrustum.calcPlanesOfFrustum(Camera.getInstance().getNearClippingPlane(),
        Camera.getInstance().getFarClippingPlane(), 1.0);
    
    // Check which scene octree nodes are inside the new frustum
    if (objects.size() > 0) {
      extractNodesOfFrustum(octreeScene, visibleNodes);
      if (visibleNodes.size() > 0) {
        computeVisibleScenePart(rootNode);
      } else {
        if (visibleNodes.size() == 0) {
          for (int j = 0; j < objects.size(); ++j) {
            objects.get(j).setVisible(false);
          }
        }
      }
    }
  }

  /**
   * builds the octree for the scene and generates octrees for every mesh of the
   * scene
   */
  public void rebuildOctree() {
    if (rootNode.getNumChildren() == 0) {
      System.out.println("Achtung: keine Meshes verfuegbar");
      return;
    }
    objects = traversalNode(rootNode);
    // generate octree for every mesh
    for (int i = 0; i < objects.size(); ++i) {
      final ITriangleMesh obj = (ITriangleMesh) objects.get(i).getContent();
      octrees.add(createMeshOctree(obj));
      TriangleMeshTools.cleanup(obj);
    }
    octreeScene = createSceneOctree(objects);
//    rootNode.addChild(new CgNode(octreeScene, "octreeScene")); // rootNode.addChild(new
    
     /* Debug
       Cuboid cube = new Cuboid(new
      Vector(octreeScene.getLowerLeft().get(0) + octreeScene.getLength()/2, //
      octreeScene.getLowerLeft().get(1) + octreeScene.getLength()/2, //
      octreeScene.getLowerLeft().get(2) + octreeScene.getLength()/2), //
      octreeScene.getLength(), octreeScene.getLength(),
      octreeScene.getLength()); // cube.getMaterial().setTransparency(0.3); //
      
      CgNode(octreeScene, "os"));
      */
     
  }

  /**
   * generates an octree for the scene
   */
  public OctreeNode<Integer> createSceneOctree(ArrayList<CgNode> objects) {
    return new OctreeFactory<Integer>(new OctreeFactoryStrategyScene(objects)).create(7, 5);
  }

  /**
   * generates an octree for the given triangle mesh
   */
  public OctreeNode<Integer> createMeshOctree(ITriangleMesh mesh) {
    return new OctreeFactory<Integer>(new OctreeFactoryStrategyTriangleMesh(mesh)).create(7, 20);
  }
}

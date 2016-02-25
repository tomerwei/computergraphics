package cgresearch.rendering.jogl.misc;

/**
 * Funktionalitaeten zum Berechnen des View Frustum Culling
 */

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.graphics.algorithms.TriangleMeshTools;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.primitives.Sphere;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyTriangleMesh;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.rendering.jogl.core.IRenderContent;
import cgresearch.core.math.VectorFactory;

public class ViewFrustumCulling implements Observer {

  private OctreeNode<Integer> octreeScene;
  private ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
  private static ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();
  private ArrayList<CgNode> objects = new ArrayList<CgNode>();
  private ArrayList<CgNode> testedObjects = new ArrayList<CgNode>();

  public static boolean initialised = false;

  // Camera parameters
  private Vector eye;
  double angle;
  private Vector refPoint;
  private Vector up;
  private Vector cameraRight;
  private Plane[] frustum;
  private CgRootNode rootNode;
  private Sphere[] planeSpheres;
  private Arrow[] planeArrows;

  // corners of frustum
  private Vector[] cornerPoints;

  // possible positions of objects
  public static final int INSIDE = 0;
  public static final int OUTSIDE = 1;
  public static final int INTERSECTED = 2;

  // indices for coordinate
  public static final int X = 0;
  public static final int Y = 1;
  public static final int Z = 2;

  // transparencies
  public static final double FRUSTUMTRANSPARENCY = 0.5;
  public static final double OBJECTSTRANSPARENCY = 0.5;

  // number of planes of frustum
  public static final int PLANES = 6;

  // number of corners of frustum and bounding boxes
  public static final int CORNERS = 8;

  // indices for corner points
  private static final int FBR = 0, FBL = 1, FTR = 2, FTL = 3, NBR = 4, NBL = 5, NTR = 6, NTL = 7;

  // indices for planes
  private static final int NEAR = 0, FAR = 1, LEFT = 2, RIGHT = 3, TOP = 4, BOTTOM = 5;

  /**
   * constructor
   * 
   * @param cam
   *          camera
   * @param nearDistance
   *          distance of near clipping (related to camera eye at 0,0,5)
   * @param farDistance
   *          distance of far clipping (related to camera eye at 0,0,5)
   * @param rootNode
   *          rootNode of the scene
   */
  public ViewFrustumCulling(Camera cam, double nearDistance, double farDistance, CgRootNode rootNode) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = cam.getRef();
    this.up = cam.getUp();
    this.cameraRight = VectorFactory.createVector3(1.0, 0.0, 0.0);
    this.cameraRight.normalize();
    frustum = new Plane[PLANES];
    cornerPoints = new Vector[CORNERS];
    this.rootNode = rootNode;
    // for Debugging :
    this.planeSpheres = new Sphere[PLANES];
    this.planeArrows = new Arrow[PLANES];

  }

  /**
   * constructor
   * 
   * @param cam
   *          camera
   * @param rootNode
   *          rootNode of the scene
   */
  public ViewFrustumCulling(Camera cam, CgRootNode rootNode) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = cam.getRef();
    this.up = cam.getUp();
    this.cameraRight = VectorFactory.createVector3(1.0, 0.0, 0.0);
    this.cameraRight.normalize();
    frustum = new Plane[PLANES];
    cornerPoints = new Vector[CORNERS];
    this.rootNode = rootNode;
    // for Debugging :
    this.planeSpheres = new Sphere[PLANES];
    this.planeArrows = new Arrow[PLANES];

  }

  /**
   * constructor for testing
   */
  public ViewFrustumCulling() {

  }

  /**
   * test frustum in form of a cube
   */
  public ViewFrustumCulling getTest() {
    ViewFrustumCulling vfc = new ViewFrustumCulling();
    Vector fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl;
    nbr = VectorFactory.createVector3(1.0, -1.0, -1.0);
    nbl = VectorFactory.createVector3(-1.0, -1.0, -1.0);
    ntr = VectorFactory.createVector3(1.0, 1.0, -1.0);
    ntl = VectorFactory.createVector3(-1.0, 1.0, -1.0);
    fbr = VectorFactory.createVector3(1.0, -1.0, 1.0);
    fbl = VectorFactory.createVector3(-1.0, -1.0, 1.0);
    ftr = VectorFactory.createVector3(1.0, 1.0, 1.0);
    ftl = VectorFactory.createVector3(-1.0, 1.0, 1.0);
    Vector[] corner_points = { fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl };
    vfc.setCornerPoints(corner_points);

    Plane[] planes = new Plane[6];
    planes[0] = vfc.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(-1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, 1.0))); // near
    planes[1] = vfc.calcPlane(VectorFactory.createVector3(-1.0, 1.0, -1.0),
        VectorFactory.createVector3(-1.0, -1.0, -1.0), (VectorFactory.createVector3(1.0, -1.0, -1.0))); // far
    planes[1].setNormal(planes[1].getNormal().multiply(-1.0));
    planes[2] = vfc.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(-1.0, -1.0, 1.0),
        (VectorFactory.createVector3(-1.0, -1.0, -1.0))); // left
    planes[2].setNormal(planes[2].getNormal().multiply(-1.0));
    planes[3] = vfc.calcPlane(VectorFactory.createVector3(1.0, 1.0, 1.0), VectorFactory.createVector3(1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, -1.0))); // right
    planes[4] = vfc.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(1.0, 1.0, 1.0),
        (VectorFactory.createVector3(1.0, 1.0, -1.0))); // top
    planes[5] = vfc.calcPlane(VectorFactory.createVector3(-1.0, -1.0, 1.0), VectorFactory.createVector3(1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, -1.0))); // bottom
    planes[5].setNormal(planes[5].getNormal().multiply(-1.0));
    vfc.setFrustum(planes);

    return vfc;
  }

  /**
   * Getter
   */
  public Vector[] getCorners() {
    return cornerPoints;
  }

  /**
   * Getter
   */
  public Plane[] getFrustum() {
    return frustum;
  }

  /**
   * Setter
   */
  public void setFrustum(Plane[] frustum) {
    this.frustum = frustum;
  }

  /**
   * Setter
   */
  public void setEye(Vector eye) {
    this.eye = eye;
  }

  /**
   * Setter
   */
  public void setAngle(double angle) {
    this.angle = angle;
  }

  /**
   * Setter
   */
  public void setRefPoint(Vector refPoint) {
    this.refPoint = refPoint;
  }

  /**
   * Setter
   */
  public void setUp(Vector up) {
    this.up = up;
  }

  /**
   * Setter
   */
  public void setCameraRight(Vector cameraRight) {
    this.cameraRight = cameraRight;
  }

  /**
   * Setter
   */
  public void setCornerPoints(Vector[] cornerPoints) {
    this.cornerPoints = cornerPoints;
  }

  /**
   * calculates the planes of the frustum
   * 
   * @param nearDistance
   *          near clipping distance
   * @param farDistance
   *          far clipping distance
   * @param viewRatio
   *          ratio of height and width of near- and far plane
   */
  public void calcPlanesOfFrustum(double nearDistance, double farDistance, double viewRatio) {
    Vector nearCenter, farCenter; // distances
    double nearHeight, farHeight, nearWidth, farWidth; // sizes
    Vector farBottomRight, farBottomLeft, farTopRight, farTopLeft; // corners
    Vector nearBottomRight, nearBottomLeft, nearTopRight, nearTopleft; // corners
    Plane nearPlane, farPlane, bottomPlane, topPlane, leftPlane, rightPlane; // planes

    // calculate near and far center
    nearCenter = (this.eye.add((this.refPoint.subtract(this.eye)).getNormalized())).multiply(nearDistance);
    farCenter = (this.eye.add((this.refPoint.subtract(this.eye)).getNormalized())).multiply(farDistance);

    // calculate near and far height
    nearHeight = 2 * Math.tan(this.angle / 2) * nearDistance;
    farHeight = 2 * Math.tan(this.angle / 2) * farDistance;

    // calculate near and far width
    nearWidth = nearHeight * viewRatio;
    farWidth = farHeight * viewRatio;

    // calculate corner points of planes
    farBottomRight =
        (farCenter.subtract(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FBR] = farBottomRight;

    farBottomLeft =
        (farCenter.subtract(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FBL] = farBottomLeft;

    farTopRight = (farCenter.add(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FTR] = farTopRight;

    farTopLeft = (farCenter.add(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FTL] = farTopLeft;

    nearBottomRight =
        (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NBR] = nearBottomRight;

    nearBottomLeft =
        (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NBL] = nearBottomLeft;

    nearTopRight = (nearCenter.add(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NTR] = nearTopRight;

    nearTopleft =
        nearCenter.add(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NTL] = nearTopleft;

    // Debugging:
    // System.out.println("farBottomRight = " + cornerPoints[FBR]);
    // System.out.println("farBottomLeft = " + cornerPoints[FBL]);
    // System.out.println("farTopRight = " + cornerPoints[FTR]);
    // System.out.println("farTopLeft = " + cornerPoints[FTL]);
    // System.out.println("nearBottomRight = " + cornerPoints[NBR]);
    // System.out.println("nearBottomLeft = " + cornerPoints[NBL]);
    // System.out.println("nearTopRight = " + cornerPoints[NTR]);
    // System.out.println("nearTopleft = " + cornerPoints[NTL]);

    // build planes
    // normals must be oriented inside the frustum, so some have to be
    // inverted
    // near
    nearPlane = calcPlane(cornerPoints[NTR], cornerPoints[NBR], cornerPoints[NTL]);
    if (nearPlane.getNormal().get(Z) < 0 || nearPlane.getNormal().get(Z) == -0) {
      nearPlane.setNormal(nearPlane.getNormal().multiply(-1.0));
    }
    this.frustum[NEAR] = nearPlane;
    buildSphereAndArrow(nearPlane, NEAR, NTR, NBL);
    // System.out.println("near_plane normal = "+ nearPlane.getNormal() );

    // fern
    farPlane = calcPlane(cornerPoints[FTR], cornerPoints[FBR], cornerPoints[FTL]);
    if (farPlane.getNormal().get(Z) > 0) {
      farPlane.setNormal(farPlane.getNormal().multiply(-1.0)); // the
                                                               // normals
    }
    // must point inwards the frustum, so they have to be inverted
    this.frustum[FAR] = farPlane;
    buildSphereAndArrow(farPlane, FAR, FTR, FBL);
    // System.out.println("far_plane normal = "+ farPlane.getNormal() );

    // left
    leftPlane = calcPlane(cornerPoints[NTL], cornerPoints[NBL], cornerPoints[FTL]);
    if (leftPlane.getNormal().get(X) < 0 || leftPlane.getNormal().get(X) == -0) {
      leftPlane.getNormal().set(X, leftPlane.getNormal().get(X) * -1);
    }
    if (leftPlane.getNormal().get(Y) < 0 || leftPlane.getNormal().get(Y) == -0) {
      leftPlane.getNormal().set(Y, leftPlane.getNormal().get(Y) * -1);
    }
    this.frustum[LEFT] = leftPlane;
    buildSphereAndArrow(leftPlane, LEFT, FTL, NBL);
    // System.out.println("left_plane normal = "+ leftPlane.getNormal() );

    // right
    rightPlane = calcPlane(cornerPoints[NTR], cornerPoints[NBR], cornerPoints[FTR]);
    if (rightPlane.getNormal().get(X) > 0) {
      rightPlane.getNormal().set(X, rightPlane.getNormal().get(X) * -1);
    }
    if (rightPlane.getNormal().get(Y) < 0 || rightPlane.getNormal().get(Y) == -0) {
      rightPlane.getNormal().set(Y, rightPlane.getNormal().get(Y) * -1);
    }
    if (rightPlane.getNormal().get(Z) < 0 || rightPlane.getNormal().get(Z) == -0) {
      rightPlane.getNormal().set(Z, rightPlane.getNormal().get(Z) * -1);
    }
    this.frustum[RIGHT] = rightPlane;
    buildSphereAndArrow(rightPlane, RIGHT, FTR, NBR);
    // System.out.println("right_plane normal = "+ rightPlane.getNormal() );

    // top
    topPlane = calcPlane(cornerPoints[NTR], cornerPoints[FTR], cornerPoints[NTL]);
    if (topPlane.getNormal().get(Y) > 0) {
      topPlane.getNormal().set(Y, topPlane.getNormal().get(Y) * -1);
    }
    if (topPlane.getNormal().get(X) < 0 || topPlane.getNormal().get(X) == -0.0) {
      topPlane.getNormal().set(X, topPlane.getNormal().get(X) * -1);
    }
    if (topPlane.getNormal().get(Z) < 0 || topPlane.getNormal().get(Z) == -0.0) {
      topPlane.getNormal().set(Z, topPlane.getNormal().get(Z) * -1);
    }
    this.frustum[TOP] = topPlane;
    buildSphereAndArrow(topPlane, TOP, FTR, NTL);
    // System.out.println("top_plane normal = "+ topPlane.getNormal() );

    // bottom
    bottomPlane = calcPlane(cornerPoints[NBR], cornerPoints[FBR], cornerPoints[NBL]);
    if (bottomPlane.getNormal().get(Y) < 0 || bottomPlane.getNormal().get(Y) == -0.0) {
      bottomPlane.getNormal().set(Y, bottomPlane.getNormal().get(Y) * -1);
    }
    if (bottomPlane.getNormal().get(X) < 0 || bottomPlane.getNormal().get(X) == -0.0) {
      bottomPlane.getNormal().set(X, bottomPlane.getNormal().get(X) * -1);
    }
    this.frustum[BOTTOM] = bottomPlane;
    buildSphereAndArrow(bottomPlane, BOTTOM, FBR, NBL);
    // System.out.println("bottom_plane normal = "+
    // bottomPlane.getNormal());

  }

  /**
   * generates a plane of three given corner points
   * 
   * @param pointOne
   *          first corner of plane
   * @param pointTwo
   *          second corner of plane
   * @param pointThree
   *          third corner of plane
   * @return plane object formed by corner points
   */
  public Plane calcPlane(Vector pointOne, Vector pointTwo, Vector pointThree) {
    Vector spanU = pointTwo.subtract(pointOne);
    Vector spanV = pointThree.subtract(pointOne);
    Vector normal = (spanV.cross(spanU));
    normal.normalize();
    return new Plane(pointOne, normal);
  }

  /**
   * calculates a bounding box for an octreeNode
   * 
   * @param obj
   *          octreeNode whose Bounding Box should be calculated
   * @return Bounding Box of octreeNode
   */
  private BoundingBox getOctreeNodeBox(OctreeNode<Integer> obj) {
    Vector ur = VectorFactory.createVector3(((OctreeNode) obj).getLowerLeft().get(0) + ((OctreeNode) obj).getLength(),
        ((OctreeNode) obj).getLowerLeft().get(1) + ((OctreeNode) obj).getLength(),
        ((OctreeNode) obj).getLowerLeft().get(2) + ((OctreeNode) obj).getLength());
    return new BoundingBox(((OctreeNode) obj).getLowerLeft(), ur);
  }

  /**
   * checks if an octreeNode is inside or outside the frustum or intersects it
   * 
   * @param octreeNode
   *          octreeNode whose position should be checked
   * @param isSceneOctreeNode
   *          says if it is an octreeNode of the octree for the scene
   * @return 0 : INSIDE, 1 : OUTSIDE, 2 : INTERSECTED
   */
  public int isObjectInFrustum(OctreeNode<Integer> octreeNode, boolean isSceneOctreeNode) {
    BoundingBox bb = getOctreeNodeBox(octreeNode);
    Vector[] corner_points = calcCornerPointsOfBoundingBox(bb);
    int count = 0;
    for (int i = 0; i < frustum.length; i++) {
      for (int j = 0; j < corner_points.length; j++) {
        if (frustum[i].computeSignedDistance(corner_points[j]) >= 0.0) {
          count++;
        }
      }
    }
    if (count > 36) { // 36
      return INSIDE;
    }
    // octreeNodes of the sceneOctree may be bigger than the frustum,
    // so the condition for intersection must be softened
    if (isSceneOctreeNode && count > 0) {
      return INTERSECTED;
    }
    return OUTSIDE;
  }

  /**
   * generates a triangle mesh for the frustum
   * 
   * @param corner_points
   *          corners of the frustum
   * @return triangle mesh for frustum
   */
  public TriangleMesh getFrustumMesh(Vector[] corner_points) {
    TriangleMesh mesh = new TriangleMesh();
    // near
    mesh.addTriangle(new Triangle(NBR, NBL, NTL));
    mesh.addTriangle(new Triangle(NBR, NTR, NTL));
    // far
    mesh.addTriangle(new Triangle(FBR, FTR, FTL));
    mesh.addTriangle(new Triangle(FBL, FBR, FTL));
    // right
    mesh.addTriangle(new Triangle(FBR, FTR, NTR));
    mesh.addTriangle(new Triangle(FBR, NBR, NTR));
    // left
    mesh.addTriangle(new Triangle(NBL, NTL, FTL));
    mesh.addTriangle(new Triangle(NBL, FBL, FTL));
    // top
    mesh.addTriangle(new Triangle(NBR, NBL, FBL));
    mesh.addTriangle(new Triangle(NBR, FBR, FBL));
    // bottom
    mesh.addTriangle(new Triangle(FTR, NTR, NTL));
    mesh.addTriangle(new Triangle(FTR, FTL, NTL));

    for (int i = 0; i < corner_points.length; i++) {
      mesh.addVertex(new Vertex(corner_points[i], VectorFactory.createVector3(1, 0, 0)));
    }
    mesh.getMaterial().setTransparency(FRUSTUMTRANSPARENCY);
    mesh.getMaterial().setRenderMode(Normals.PER_FACET);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    return mesh;
  }

  /**
   * calculates the corners of a bounding box
   * 
   * @param bBox
   *          Bounding box whose corner points should be calculated
   * @return array with corner points
   */
  private Vector[] calcCornerPointsOfBoundingBox(BoundingBox bBox) {

    Vector[] cornerPoints = new Vector[8];
    for (int i = 0; i < cornerPoints.length; ++i) {
      cornerPoints[i] = VectorFactory.createVector3(0, 0, 0);
    }

    Vector center = bBox.getCenter();
    Vector lnl, lfl, lfr, lnr, ufr, unr, unl, ufl; // corner points of
                                                   // bounding box

    // low near left
    lnl = bBox.getLowerLeft();
    cornerPoints[0] = lnl;

    // low far left
    double z = lnl.get(Z) + (center.get(Z) - lnl.get(Z)) * 2.0;
    lfl = VectorFactory.createVector3(lnl.get(X), lnl.get(Y), z);
    cornerPoints[1] = lfl;

    // low far right
    double x = lfl.get(X) + (center.get(X) - lnl.get(X)) * 2.0;
    lfr = VectorFactory.createVector3(x, lfl.get(Y), lfl.get(Z));
    cornerPoints[2] = lfr;

    // low near right
    x = bBox.getLowerLeft().get(X) + (center.get(X) - bBox.getLowerLeft().get(X)) * 2.0;
    lnr = VectorFactory.createVector3(x, lnl.get(Y), lnl.get(Z));
    cornerPoints[3] = lnr;

    // up far right
    ufr = bBox.getUpperRight();
    cornerPoints[4] = ufr;

    // up near right
    z = ufr.get(Z) - (ufr.get(Z) - center.get(Z)) * 2.0;
    unr = VectorFactory.createVector3(ufr.get(X), ufr.get(Y), z);
    cornerPoints[5] = unr;

    // up near left
    x = unr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    unl = VectorFactory.createVector3(x, unr.get(Y), unr.get(Z));
    cornerPoints[6] = unl;

    // up far left
    x = ufr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    ufl = VectorFactory.createVector3(x, ufr.get(Y), ufr.get(Z));
    cornerPoints[7] = ufl;

    return cornerPoints;

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
  public ArrayList<OctreeNode<Integer>> checkOctree(OctreeNode<Integer> octreeNode,
      ArrayList<OctreeNode<Integer>> toDraw, CgNode node) {
    node.setVisible(false);
    int result = isObjectInFrustum(octreeNode, false);
    if (result == INSIDE) {
      if (!testedObjects.contains(node)) {
        testedObjects.add(node);
      }
      node.setVisible(true);
      return null;
    }
    if (result == INTERSECTED) {
      node.setVisible(false); // TODO
      // toDraw = traversalOctreeNodeIntersected(octreeNode, toDraw);
      // return toDraw;
    }
    return null;
  }

  /**
   * if visible scene nodes intersect an objectOctree, the objects visibility is
   * tested , builds a new triangle mesh for objects which are only partially
   * visible,
   * 
   * 
   * @param nodesInFrustum
   *          octreeNodes of objects which are in the frustum
   * @param node
   *          node which contains the object which should be drawn partially
   * @return new mesh only with visible triangles
   */
  public ICgNodeContent extractNodeContent(OctreeNode<Integer> objectOctree, CgNode node,
      OctreeNode<Integer> sceneOctree) {
    // if the node is already tested und set visible, it must not be tested
    // anymore, because it may be set invisible then
    if ((!testedObjects.contains(node))) {
      if (boxesIntersect(sceneOctree.getBoundingBox(), objectOctree.getBoundingBox())) {
        ArrayList<OctreeNode<Integer>> toDraw = new ArrayList<OctreeNode<Integer>>();
        ICgNodeContent contentToDraw = null;
        checkOctree(objectOctree, toDraw, node);

        if (toDraw.size() > 0) {
          if (node.getContent().getClass() == TriangleMesh.class) {
            contentToDraw = new TriangleMesh();
            for (int m = 0; m < ((TriangleMesh) node.getContent()).getNumberOfVertices(); m++) {
              ((ITriangleMesh) contentToDraw).addVertex(((TriangleMesh) node.getContent()).getVertex(m));
            }
            for (int i = 0; i < toDraw.size(); i++) {
              for (int j = 0; j < toDraw.get(i).getNumberOfElements(); j++) {
                ITriangle t = ((TriangleMesh) node.getContent()).getTriangle(toDraw.get(i).getElement(j));
                ((ITriangleMesh) contentToDraw).addTriangle(t);
              }
            }
            ((ITriangleMesh) contentToDraw).computeVertexNormals();
            ((ITriangleMesh) contentToDraw).computeTriangleNormals();
            contentToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
            TriangleMeshTools.cleanup((ITriangleMesh) contentToDraw);
          }

          if (node.getContent().getClass() == PointCloud.class) {
            contentToDraw = new PointCloud();
            for (int i = 0; i < toDraw.size(); i++) {
              for (int j = 0; j < toDraw.get(i).getNumberOfElements(); j++) {
                ((PointCloud) contentToDraw)
                    .addPoint(((PointCloud) node.getContent()).getPoint(toDraw.get(i).getElement(j)));
              }
            }
          }
          contentToDraw.getMaterial().setTransparency(OBJECTSTRANSPARENCY);
          contentToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
          return contentToDraw;
        }
      } else {
        // boxes of sceneOctree and objectOctree do not intersect, so
        // the object can not be visible
        node.setVisible(false);
      }
    }
    return null;
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
    if (two.getUpperRight().get(X) < one.getLowerLeft().get(X)) {
      return false;
    }
    if (two.getLowerLeft().get(X) > one.getUpperRight().get(X)) {
      return false;
    }
    if (two.getUpperRight().get(Y) < one.getLowerLeft().get(Y)) {
      return false;
    }
    if (two.getLowerLeft().get(Y) > one.getUpperRight().get(Y)) {
      return false;
    }
    if (two.getUpperRight().get(Z) < one.getLowerLeft().get(Z)) {
      return false;
    }
    if (two.getLowerLeft().get(Z) > one.getUpperRight().get(Z)) {
      return false;
    }
    return true;
  }

  /**
   * intialises frustu, Sets the nodes in the view frustum visible and selects
   * visible triangles in triangle meshes.
   */
  public void computeVisibleScenePart(CgRootNode rootNode) {
    if (!initialised) {
      initialised = true;
      if (rootNode.useViewFrustumCulling()) {
        Camera.getInstance().addObserver(this); // TODO
      }
      // Debugging for live Modus, deactivated
      for (int i = 0; i < 6; i++) {
        this.planeSpheres[i] = new Sphere();
        this.planeArrows[i] = new Arrow();
        // if (rootNode.useViewFrustumCulling()) {
        // rootNode.addChild(new CgNode(planeSpheres[i], "sphere"));
        // rootNode.addChild(new CgNode(planeArrows[i],
        // "normalArrow".concat(Integer.toString(i))));
        // }

      }
      calcPlanesOfFrustum(Camera.getInstance().getNearClippingPlane(), Camera.getInstance().getFarClippingPlane(), 1.0); //
      rebuildOctree();
      visibleNodes.clear();
      extractNodesOfFrustum(octreeScene, visibleNodes);
      if (!rootNode.useViewFrustumCulling()) {
        ITriangleMesh frustum = getFrustumMesh(this.getCorners());
        frustum.getMaterial().setTransparency(OBJECTSTRANSPARENCY);
        rootNode.addChild(new CgNode(frustum, "frustum"));
      }
    }
    for (int j = 0; j < objects.size(); j++) {
      for (int k = 0; k < visibleNodes.size(); k++) {
        addVisibleElementsToScene(octrees.get(j), objects.get(j), visibleNodes.get(k));
      }
    }
  }

  /**
   * extracts all children of a node and adds them to a list
   * 
   * @param node
   *          nodes whose children should be extracted
   * @param objects
   *          list for the children
   * @return list with all children
   */
  public ArrayList<CgNode> traversalNode(CgNode node, ArrayList<CgNode> objects) {
    if (node.getNumChildren() > 0) {
      for (int i = 0; i < node.getNumChildren(); i++) {
        traversalNode(node.getChildNode(i), objects);
      }
    } else {
      if (node.getContent() != null
          && ((node.getContent().getClass() == TriangleMesh.class || node.getContent().getClass() == PointCloud.class))
          && node.getContent().getClass() != Sphere.class && node.getContent().getClass() != Arrow.class) {
        node.getContent().getMaterial().setTransparency(OBJECTSTRANSPARENCY);
        node.setVisible(true);
        objects.add(node);
      }
    }
    return objects;
  }

  /**
   * generates an octree for the given triangle mesh
   */
  public OctreeNode<Integer> createMeshOctree(ITriangleMesh mesh) {
    OctreeFactoryStrategyTriangleMesh octreeFactoryStrategyMesh = new OctreeFactoryStrategyTriangleMesh(mesh);
    OctreeFactory<Integer> octreeFactoryMesh = new OctreeFactory<Integer>(octreeFactoryStrategyMesh);
    OctreeNode<Integer> octreeMeshRoot = octreeFactoryMesh.create(7, 20);
    return octreeMeshRoot;
  }

  /**
   * generates an octree for the scene
   */
  public OctreeNode<Integer> createSceneOctree(ArrayList<CgNode> objects) {
    OctreeFactoryStrategyScene octreeFactoryStrategyScene = new OctreeFactoryStrategyScene(objects);
    OctreeFactory<Integer> octreeFactoryScene = new OctreeFactory<Integer>(octreeFactoryStrategyScene);
    OctreeNode<Integer> octreeSceneRoot = octreeFactoryScene.create(7, 2);
    return octreeSceneRoot;
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
    for (int i = 0; i < node.getNumberOfChildren(); i++) {
      isNodeInFrustum(node.getChild(i), nodesInFrustum);
    }
  }

  /**
   * adds all (sceneOctree) nodes to a list which are visible or intersected by
   * the frustum
   * 
   * @param node
   *          node who should be tested
   * @param nodesInFrustum
   *          Liste, der die sichtbaren Nodes hinzugefuegt werden
   */
  public void isNodeInFrustum(OctreeNode<Integer> node, ArrayList<OctreeNode<Integer>> nodesInFrustum) {
    if (isObjectInFrustum(node, true) == INSIDE || isObjectInFrustum(node, true) == INTERSECTED) {
      nodesInFrustum.add(node);
    }
  }

  /**
   * 
   * @param tree
   *          tree of the node
   * @param node
   *          node who should be tested
   * @param scene
   *          octree for the scene
   */
  public void addVisibleElementsToScene(OctreeNode<Integer> tree, CgNode node, OctreeNode<Integer> scene) {
    ICgNodeContent partlyVisible = extractNodeContent(tree, node, scene);
    // if (isObjectInFrustum(tree) == INTERSECTED) {
    // Objekte, die teilweise sichtbar sind, werden aktuell noch nicht
    // hinzugefuegt
    // CgNode newNode = new CgNode(partlyVisible, "partyVisible_object");
    // rootNode.addChild(newNode);
    // }
    partlyVisible = null;
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
    testedObjects.clear();
    visibleNodes.clear();

    // reset visibility
    // for (int i = 0; i < objects.size(); i++) {
    // objects.get(i).setVisible(true);
    // }

    this.eye = Camera.getInstance().getEye();
    this.up = Camera.getInstance().getUp();

    calcPlanesOfFrustum(Camera.getInstance().getNearClippingPlane(), Camera.getInstance().getFarClippingPlane(), 1.0);
    extractNodesOfFrustum(octreeScene, visibleNodes);
    if (visibleNodes.size() > 0) {
      computeVisibleScenePart(rootNode);
    } else if (visibleNodes.size() == 0) {
      for (int i = 0; i < objects.size(); i++) {
        objects.get(i).setVisible(false);
      }
    }
  }

  /**
   * builds the octree for the scene and generates octrees for every mesh of the
   * scene
   */
  public void rebuildOctree() {
    objects.clear();
    objects = traversalNode(rootNode, objects);
    this.octreeScene = createSceneOctree(objects);

    // generate octree for every mesh
    for (int i = 0; i < objects.size(); i++) {
      ITriangleMesh mesh = (ITriangleMesh) objects.get(i).getContent();
      TriangleMeshTools.cleanup(mesh);
      octrees.add(createMeshOctree((TriangleMesh) objects.get(i).getContent()));
    }
  }

  /**
   * Debugging
   */
  public void buildSphereAndArrow(Plane plane, int planeIndex, int firstIndex, int secondIndex) {
    // Logger.getInstance().message("buildSphereAndArrow");
    Vector tmp;
    tmp =
        cornerPoints[firstIndex].subtract((cornerPoints[firstIndex].subtract(cornerPoints[secondIndex])).multiply(0.5));
    // System.out.println("tmp =" + tmp);
    this.planeArrows[planeIndex].getStart().set(tmp.get(X), tmp.get(Y), tmp.get(Z));
    // this.planeArrows[planeIndex].getStart().set(plane.getPoint().get(X),
    // plane.getPoint().get(Y), plane.getPoint().get(Z));
    // System.out.println("start =" +
    // this.planeArrows[planeIndex].getStart());
    this.planeArrows[planeIndex].getEnd().set(tmp.get(X) + plane.getNormal().get(X) * 2,
        tmp.get(Y) + plane.getNormal().get(Y) * 2, tmp.get(Z) + plane.getNormal().get(Z) * 2);
    // System.out.println("end =" + this.planeArrows[planeIndex].getEnd());
    this.planeSpheres[planeIndex].getCenter().set(tmp.get(X), tmp.get(Y), tmp.get(Z));
    // System.out.println("sphereCenter =" +
    // this.planeSpheres[planeIndex].getCenter());
    this.planeArrows[planeIndex].updateRenderStructures();
  }

}

package cgresearch.rendering.jogl.misc;

import java.util.ArrayList;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgRootNode;

public class ViewFrustum  {
  
  // number of planes of frustum
  public static final int PLANES = 6;

  // number of corners of frustum and bounding boxes
  public static final int CORNERS = 8;
  
  // indices for corner points
  public static final int FBR = 0, FBL = 1, FTR = 2, FTL = 3, NBR = 4, NBL = 5, NTR = 6, NTL = 7;

  // indices for planes
  public static final int NEAR = 0, FAR = 1, LEFT = 2, RIGHT = 3, TOP = 4, BOTTOM = 5;
  
  // indices for coordinate
  public static final int X = 0;
  public static final int Y = 1;
  public static final int Z = 2;
  
  // possible positions of objects
  public static final int INSIDE = 0;
  public static final int OUTSIDE = 1;
  public static final int INTERSECTED = 2;
  
  // transparencies
  public static final double FRUSTUMTRANSPARENCY = 0.5;
  public static final double OBJECTSTRANSPARENCY = 0.5;
  
  // Camera parameters
  private Vector eye;
  private double angle;
  private Vector refPoint;
  private Vector up;
  private Vector cameraRight;
  private Plane[] frustum;

  // corners of frustum
  private Vector[] cornerPoints;
  
  /**
   * constructor
   * 
   * @param cam
   *          camera
   * @param rootNode
   *          rootNode of the scene
   */
  public ViewFrustum(Camera cam, CgRootNode rootNode) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = cam.getRef();
    this.up = cam.getUp();
    this.cameraRight = up.cross(eye.subtract(refPoint)).getNormalized();
    this.cameraRight.normalize();
    frustum = new Plane[PLANES];
    cornerPoints = new Vector[CORNERS];
  }

  /**
   * constructor for testing
   */
  public ViewFrustum() {

  }

  /**
   * test frustum with the form of a cube
   */
  public ViewFrustum getTestFrustum() {
    ViewFrustum vf = new ViewFrustum();
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
    vf.setCornerPoints(corner_points);

    Plane[] planes = new Plane[6];
    planes[0] = vf.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(-1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, 1.0))); // near
    planes[1] = vf.calcPlane(VectorFactory.createVector3(-1.0, 1.0, -1.0),
        VectorFactory.createVector3(-1.0, -1.0, -1.0), (VectorFactory.createVector3(1.0, -1.0, -1.0))); // far
    planes[1].setNormal(planes[1].getNormal().multiply(-1.0));
    planes[2] = vf.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(-1.0, -1.0, 1.0),
        (VectorFactory.createVector3(-1.0, -1.0, -1.0))); // left
    planes[2].setNormal(planes[2].getNormal().multiply(-1.0));
    planes[3] = vf.calcPlane(VectorFactory.createVector3(1.0, 1.0, 1.0), VectorFactory.createVector3(1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, -1.0))); // right
    planes[4] = vf.calcPlane(VectorFactory.createVector3(-1.0, 1.0, 1.0), VectorFactory.createVector3(1.0, 1.0, 1.0),
        (VectorFactory.createVector3(1.0, 1.0, -1.0))); // top
    planes[5] = vf.calcPlane(VectorFactory.createVector3(-1.0, -1.0, 1.0), VectorFactory.createVector3(1.0, -1.0, 1.0),
        (VectorFactory.createVector3(1.0, -1.0, -1.0))); // bottom
    planes[5].setNormal(planes[5].getNormal().multiply(-1.0));
    vf.setFrustum(planes);

    return vf;
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
    nearCenter = this.eye.add((this.refPoint.subtract(this.eye)).getNormalized().multiply(Math.abs(nearDistance)));
    farCenter = this.eye.add((this.refPoint.subtract(this.eye)).getNormalized().multiply(Math.abs(farDistance)));
    
    // calculate near and far height
    nearHeight = 2 * Math.tan(this.angle / 2) * Math.abs(nearDistance);
    farHeight = 2 * Math.tan(this.angle / 2) * Math.abs(farDistance);
    // calculate near and far width
    nearWidth = Math.abs(nearHeight) * viewRatio;
    farWidth = Math.abs(farHeight) * viewRatio;
   
    // calculate corner points of planes
    farBottomLeft = (farCenter.subtract(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FBL] = farBottomLeft;

    farBottomRight = (farCenter.subtract(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FBR] = farBottomRight;

    farTopRight = (farCenter.add(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FTR] = farTopRight;

    farTopLeft = (farCenter.add(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[FTL] = farTopLeft;

    nearBottomRight = (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NBR] = nearBottomRight;

    nearBottomLeft = (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NBL] = nearBottomLeft;

    nearTopRight = (nearCenter.add(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NTR] = nearTopRight;

    nearTopleft = (nearCenter.add(this.up.multiply(nearHeight * 0.5)).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[NTL] = nearTopleft;

    // build planes
    // normals must be oriented inside the frustum, so some have to be inverted
    // near
    nearPlane = calcPlane(cornerPoints[NTR], cornerPoints[NBR], cornerPoints[NTL]);
    Vector d  = this.refPoint.subtract(this.eye);
    nearPlane.setNormal(d);
    nearPlane.getNormal().normalize();
    this.frustum[NEAR] = nearPlane;
    // far
    farPlane = calcPlane(cornerPoints[FTR], cornerPoints[FBR], cornerPoints[FTL]);
    farPlane.setNormal(new Vector(d.get(X) * -1 ,d.get(Y) * -1 , d.get(Z) * -1));
    farPlane.getNormal().normalize();
    this.frustum[FAR] = farPlane;
    // left
    leftPlane = calcPlane(cornerPoints[FTL], cornerPoints[FBL], cornerPoints[NTL]);
    leftPlane.setNormal(new Vector(cameraRight.get(X), cameraRight.get(Y), cameraRight.get(Z)));
    leftPlane.getNormal().normalize();
    this.frustum[LEFT] = leftPlane;
    // right
    rightPlane = calcPlane(cornerPoints[FTR], cornerPoints[FBR], cornerPoints[NTR]);
    rightPlane.getNormal().set(cameraRight.get(X) * -1, cameraRight.get(Y) * -1, cameraRight.get(Z) * -1);
    rightPlane.getNormal().normalize();
    this.frustum[RIGHT] = rightPlane;
    // top
    topPlane = calcPlane(cornerPoints[FTR], cornerPoints[NTR], cornerPoints[FTL]);
    topPlane.getNormal().set(up.get(X) * -1, up.get(Y) * -1, up.get(Z) * -1);
    topPlane.getNormal().normalize();
    this.frustum[TOP] = topPlane;
    // bottom
    bottomPlane = calcPlane(cornerPoints[FBR], cornerPoints[NBR], cornerPoints[FBL]);
    bottomPlane.setNormal(up);
    bottomPlane.getNormal().normalize();
    this.frustum[BOTTOM] = bottomPlane;
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
   * checks if an octreeNode is inside or outside the frustum or intersects it
   * 
   * @param octreeNode
   *          octreeNode whose position should be checked
   * @param isSceneOctreeNode
   *          says if it is an octreeNode of the octree for the scene
   * @return 0 : INSIDE, 1 : OUTSIDE, 2 : INTERSECTED
   */
  public int isObjectInFrustum(OctreeNode<Integer> octreeNode, boolean isSceneOctreeNode) {
    final BoundingBox bb = getOctreeNodeBox(octreeNode);
    final Vector[] corner_points = calcCornerPointsOfBoundingBox(bb);
    int count = 0;
    for (int i = 0; i < PLANES; i++) {
      for (int j = 0; j < CORNERS; j++) {
        if (frustum[i].computeSignedDistance(corner_points[j]) >= -0.0) {
          ++count;
        }
      }
    }
    if (count == 48) { 
      return INSIDE;
    }
    // octreeNodes of the sceneOctree may be bigger than the frustum,
    // so the condition for intersection must be softened
    return (isSceneOctreeNode && count > 0) ? INTERSECTED : OUTSIDE;
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
  public void addVisibleNode(final OctreeNode<Integer> node,
                              final ArrayList<OctreeNode<Integer>> nodesInFrustum) {
    if (   isObjectInFrustum(node, true) == INSIDE 
        || isObjectInFrustum(node, true) == INTERSECTED) {
      nodesInFrustum.add(node);
    }
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
  private Plane calcPlane(Vector pointOne, Vector pointTwo, Vector pointThree) {
    Vector spanU = pointTwo.subtract(pointOne);
    Vector spanV = pointThree.subtract(pointOne);
    Vector normal = spanV.cross(spanU).getNormalized();
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
    Vector ur = VectorFactory.createVector3(
        ((OctreeNode<Integer>) obj).getLowerLeft().get(X) + ((OctreeNode<Integer>) obj).getLength(),
        ((OctreeNode<Integer>) obj).getLowerLeft().get(Y) + ((OctreeNode<Integer>) obj).getLength(),
        ((OctreeNode<Integer>) obj).getLowerLeft().get(Z) + ((OctreeNode<Integer>) obj).getLength());
    return new BoundingBox(((OctreeNode<Integer>) obj).getLowerLeft(), ur);
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

}

package cgresearch.rendering.jogl.misc;

/**
 * Funktionalitaeten zum Berechnen des View Frustum Culling
 */

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.graphics.algorithms.TriangleMeshTools;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.primitives.Plane;
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
import cgresearch.core.math.VectorMatrixFactory;

public class ViewFrustumCulling implements Observer {

  private OctreeNode<Integer> octreeScene;
  private ArrayList<OctreeNode<Integer>> octrees = new ArrayList<OctreeNode<Integer>>();
  private static ArrayList<OctreeNode<Integer>> visibleNodes = new ArrayList<OctreeNode<Integer>>();

  public static final double frustumTransparency = 0.5;
  public static final double objectsTransparency = 0.5;

  // Kameraparameter
  private Vector eye;
  double angle;
  private Vector refPoint;
  private Vector up;
  private Vector cameraRight;
  private Plane[] frustum;
  private CgRootNode rootNode;

  // Eckpunkte des Frustums
  private Vector[] cornerPoints;

  // Positionen der Objekte
  public static final int INSIDE = 0;
  public static final int OUTSIDE = 1;
  public static final int INTERSECT = 2;

  // Indizes fuer Koordinates
  public static final int X = 0;
  public static final int Y = 1;
  public static final int Z = 2;

  // Indizes fuer Eckpunkte
  private static final int fbr = 0, fbl = 1, ftr = 2, ftl = 3, nbr = 4, nbl = 5, ntr = 6, ntl = 7;
  // Indizes fuer Ebenen
  private static final int near = 0, far = 1, left = 2, right = 3, top = 4, bottom = 5;

  private ArrayList<CgNode> objects = new ArrayList<CgNode>();

  /**
   * Konstruktor
   * 
   * @param cam
   *          Kamera
   * @param nearDistance
   *          Distanz der nahen Ebene zur Kamera, ausgehend vom Augpunkt (0,0,5)
   * @param farDistance
   *          Distanz der fernen Ebene zur Kamera, ausgehend vom Augpunkt
   *          (0,0,5) merke: die nahe Ebene ist immer hinter und dichter als die
   *          ferne Ebene, nearDistance muss also immer kleiner sein als
   *          farDistance
   */
  public ViewFrustumCulling(Camera cam, double nearDistance, double farDistance, CgRootNode rootNode) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = cam.getRef();
    this.up = cam.getUp();
    this.cameraRight = VectorMatrixFactory.newVector(1.0, 0.0, 0.0);
    this.cameraRight.normalize();
    frustum = new Plane[6];
    cornerPoints = new Vector[8];
    this.rootNode = rootNode;

    // dritter Parameter beeinflusst die Breite des Frustums
    if (rootNode.useViewFrustumCulling()) {
      Camera.getInstance().addObserver(this); // TODO
    }
    calcPlanesOfFrustum(nearDistance, farDistance, 1.0); //
    rebuildOctree();
    extractNodesOfFrustum(this, octreeScene, visibleNodes);
  }

  /**
   * Konstruktor
   * 
   * @param cam
   *          Kamera
   * @param nearDistance
   *          Distanz der nahen Ebene zur Kamera, ausgehend vom Augpunkt (0,0,5)
   * @param farDistance
   *          Distanz der fernen Ebene zur Kamera, ausgehend vom Augpunkt
   *          (0,0,5) merke: die nahe Ebene ist immer hinter und dichter als die
   *          ferne Ebene, nearDistance muss also immer kleiner sein als
   *          farDistance
   */
  public ViewFrustumCulling(Camera cam, CgRootNode rootNode) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = cam.getRef();
    this.up = cam.getUp();
    this.cameraRight = VectorMatrixFactory.newVector(1.0, 0.0, 0.0);
    System.out.println("CAMERA RIGHT =" + cameraRight);
    this.cameraRight.normalize();
    frustum = new Plane[6];
    cornerPoints = new Vector[8];
    this.rootNode = rootNode;

    // dritter Parameter beeinflusst die Breite des Frustums
    if (rootNode.useViewFrustumCulling()) {
      Camera.getInstance().addObserver(this); // TODO
    }
    calcPlanesOfFrustum(cam.getNearClippingPlane(), cam.getFarClippingPlane(), 1.0); //
    rebuildOctree();
    extractNodesOfFrustum(this, octreeScene, visibleNodes);
  }

  /**
   * Konstruktor
   */
  public ViewFrustumCulling() {

  }

  /**
   * gibt ein TestFrustum mit der Form eines Wuerfels zurueck
   */
  public ViewFrustumCulling getTest() {
    ViewFrustumCulling vfc = new ViewFrustumCulling();
    Vector fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl;
    nbr = VectorMatrixFactory.newVector(1.0, -1.0, -1.0);
    nbl = VectorMatrixFactory.newVector(-1.0, -1.0, -1.0);
    ntr = VectorMatrixFactory.newVector(1.0, 1.0, -1.0);
    ntl = VectorMatrixFactory.newVector(-1.0, 1.0, -1.0);
    fbr = VectorMatrixFactory.newVector(1.0, -1.0, 1.0);
    fbl = VectorMatrixFactory.newVector(-1.0, -1.0, 1.0);
    ftr = VectorMatrixFactory.newVector(1.0, 1.0, 1.0);
    ftl = VectorMatrixFactory.newVector(-1.0, 1.0, 1.0);
    Vector[] corner_points = { fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl };
    vfc.setCornerPoints(corner_points);

    Plane[] planes = new Plane[6];
    planes[0] = vfc.calcPlane(VectorMatrixFactory.newVector(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newVector(-1.0, -1.0, 1.0), (VectorMatrixFactory.newVector(1.0, -1.0, 1.0))); // near
    planes[1] = vfc.calcPlane(VectorMatrixFactory.newVector(-1.0, 1.0, -1.0),
        VectorMatrixFactory.newVector(-1.0, -1.0, -1.0), (VectorMatrixFactory.newVector(1.0, -1.0, -1.0))); // far
    planes[1].setNormal(planes[1].getNormal().multiply(-1.0));
    planes[2] = vfc.calcPlane(VectorMatrixFactory.newVector(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newVector(-1.0, -1.0, 1.0), (VectorMatrixFactory.newVector(-1.0, -1.0, -1.0))); // left
    planes[2].setNormal(planes[2].getNormal().multiply(-1.0));
    planes[3] = vfc.calcPlane(VectorMatrixFactory.newVector(1.0, 1.0, 1.0),
        VectorMatrixFactory.newVector(1.0, -1.0, 1.0), (VectorMatrixFactory.newVector(1.0, -1.0, -1.0))); // right
    planes[4] = vfc.calcPlane(VectorMatrixFactory.newVector(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newVector(1.0, 1.0, 1.0), (VectorMatrixFactory.newVector(1.0, 1.0, -1.0))); // top
    planes[5] = vfc.calcPlane(VectorMatrixFactory.newVector(-1.0, -1.0, 1.0),
        VectorMatrixFactory.newVector(1.0, -1.0, 1.0), (VectorMatrixFactory.newVector(1.0, -1.0, -1.0))); // bottom
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
   * berechnet die eingrenzenden Ebenen des Frustums
   * 
   * @param nearDistance
   *          Distanz zur nahen Ebene
   * @param farDistance
   *          Distanz zur fernen Ebene
   * @param viewRatio
   *          Breite des Frustums
   */
  public void calcPlanesOfFrustum(double nearDistance, double farDistance, double viewRatio) {
    Vector nearCenter, farCenter; // distances
    double nearHeight, farHeight, nearWidth, farWidth; // sizes
    Vector farBottomRight, farBottomLeft, farTopRight, farTopLeft; // corners
    Vector nearBottomRight, nearBottomLeft, nearTopRight, nearTopleft; // corners
    Plane nearPlane, farPlane, bottomPlane, topPlane, leftPlane, rightPlane; // planes

    // Berechne CenterPunkte fuer nahe und ferne Ebene
    nearCenter = this.eye.add((this.refPoint.subtract(this.eye)).getNormalized()).multiply(nearDistance);
    farCenter = this.eye.add((this.refPoint.subtract(this.eye)).getNormalized()).multiply(farDistance);

    // Berechne Breite und Hoehe der nahen und fernen Ebene
    // Sonderfall, vertausche die Hoehen
    nearHeight = 2 * Math.tan(this.angle / 2) * nearDistance;
    farHeight = 2 * Math.tan(this.angle / 2) * farDistance;

    nearWidth = nearHeight * viewRatio;
    farWidth = farHeight * viewRatio;

    // Berechne Eckpunkte der Ebenen

    farBottomRight =
        (farCenter.subtract(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbr] = farBottomRight;

    farBottomLeft =
        (farCenter.subtract(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbl] = farBottomLeft;

    farTopRight = (farCenter.add(this.up.multiply(farHeight * 0.5)).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftr] = farTopRight;

    farTopLeft = (farCenter.add(this.up.multiply(farHeight * 0.5)).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftl] = farTopLeft;

    nearBottomRight =
        (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbr] = nearBottomRight;

    nearBottomLeft =
        (nearCenter.subtract(this.up.multiply(nearHeight * 0.5)).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbl] = nearBottomLeft;

    nearTopRight = (nearCenter.add(this.up.multiply(nearHeight * 0.5)).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntr] = nearTopRight;

    nearTopleft =
        nearCenter.add(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntl] = nearTopleft;

    // Debugging
    // System.out.println("farBottomRight = " + cornerPoints[fbr]);
    // System.out.println("farBottomLeft = " + cornerPoints[fbl]);
    // System.out.println("farTopRight = " + cornerPoints[ftr]);
    // System.out.println("farTopLeft = " + cornerPoints[ftl]);
    // System.out.println("nearBottomRight = " + cornerPoints[nbr]);
    // System.out.println("nearBottomLeft = " + cornerPoints[nbl]);
    // System.out.println("nearTopRight = " + cornerPoints[ntr]);
    // System.out.println("nearTopleft = " + cornerPoints[ntl]);

    // Berechne Ebene
    System.out.println("Ebenen berechnen");
    // nah
    nearPlane = calcPlane(cornerPoints[ntr], cornerPoints[nbr], cornerPoints[ntl]);
    if (nearPlane.getNormal().get(Z) < 0) {
      nearPlane.setNormal(nearPlane.getNormal().multiply(-1.0)); // die Normalen
    } // zeigen nach
      // au�en, also
      // muessen
      // teilweise
      // umgedreht
      // werden
    this.frustum[near] = nearPlane;
    System.out.println("near_plane normal = " + nearPlane.getNormal());

    // fern
    farPlane = calcPlane(cornerPoints[ftr], cornerPoints[fbr], cornerPoints[ftl]);
    if (farPlane.getNormal().get(Z) > 0) {
      farPlane.setNormal(farPlane.getNormal().multiply(-1.0)); // the normals
    }
    // must point inwards the frustum, so they have to be inverted
    this.frustum[far] = farPlane;
    System.out.println("far_plane normal = " + farPlane.getNormal());

    // links
    leftPlane = calcPlane(cornerPoints[ntl], cornerPoints[nbl], cornerPoints[ftl]);
    if (leftPlane.getNormal().get(X) < 0) {
      leftPlane.getNormal().set(X, leftPlane.getNormal().get(X) * -1);
    }
    if (leftPlane.getNormal().get(Y) < 0) {
      leftPlane.getNormal().set(Y, leftPlane.getNormal().get(Y) * -1);
    }
    this.frustum[left] = leftPlane;
    System.out.println("left_plane normal = " + leftPlane.getNormal());

    // rechts
    rightPlane = calcPlane(cornerPoints[ntr], cornerPoints[nbr], cornerPoints[ftr]);
    if (rightPlane.getNormal().get(X) > 0) {
      rightPlane.getNormal().set(X, rightPlane.getNormal().get(X) * -1);
    }
    if (rightPlane.getNormal().get(Y) < 0 || rightPlane.getNormal().get(Y) == -0) {
      rightPlane.getNormal().set(Y, rightPlane.getNormal().get(Y) * -1);
    }
    if (rightPlane.getNormal().get(Z) < 0) {
      rightPlane.getNormal().set(Z, rightPlane.getNormal().get(Z) * -1);
    }
    this.frustum[right] = rightPlane;
    System.out.println("right_plane normal = " + rightPlane.getNormal());

    // oben
    topPlane = calcPlane(cornerPoints[ntr], cornerPoints[ftr], cornerPoints[ntl]);
    if (topPlane.getNormal().get(Y) > 0) {
      topPlane.getNormal().set(Y, topPlane.getNormal().get(Y) * -1);
    }
    if (topPlane.getNormal().get(X) < 0 || topPlane.getNormal().get(X) == -0) {
      topPlane.getNormal().set(X, topPlane.getNormal().get(X) * -1);
    }
    if (topPlane.getNormal().get(Z) < 0) {
      topPlane.getNormal().set(Z, topPlane.getNormal().get(Z) * -1);
    }
    this.frustum[top] = topPlane;
    System.out.println("top_plane normal = " + topPlane.getNormal());

    // unten
    bottomPlane = calcPlane(cornerPoints[nbr], cornerPoints[fbr], cornerPoints[nbl]);
    if (bottomPlane.getNormal().get(Y) < 0) {
      bottomPlane.getNormal().set(Y, bottomPlane.getNormal().get(Y) * -1);
    }
    if (bottomPlane.getNormal().get(X) < 0) {
      bottomPlane.getNormal().set(X, bottomPlane.getNormal().get(X) * -1);
    }
    this.frustum[bottom] = bottomPlane;
    System.out.println("bottom_plane normal = " + bottomPlane.getNormal());

  }

  /**
   * berechnet eine Ebene aus drei Eckpunkten
   * 
   * @param point_one
   *          erster Eckpunkt
   * @param point_two
   *          zweiter Eckpunkt
   * @param point_three
   *          dritter Eckpunkt
   * @return berechnete Ebene
   */
  public Plane calcPlane(Vector point_one, Vector point_two, Vector point_three) {
    Vector spanU = point_two.subtract(point_one);
    Vector spanV = point_three.subtract(point_one);
    Vector normal = (spanV.cross(spanU));
    normal.normalize();
    return new Plane(point_one, normal);
  }

  /**
   * berechnet, ob point im Frustum liegt
   * 
   * @param point
   *          zu berechnender Punkt
   * @return ob point im Frustum liegt
   */
  public boolean isPointInFrustum(Vector point) {

    boolean result = true;
    for (int i = 0; i < this.frustum.length; i++) {
      if (frustum[i].computeSignedDistance(point) > 0)
        return false;
    }
    return result;

  }

  /**
   * prueft, ob ein Objekt im Frustum liegt
   * 
   * @param obj
   *          Objekt, das geprueft werden soll
   * @return 0 : inside, 1 : outside, 2 : intersect
   */
  public int isObjectInFrustum(ICgNodeContent obj) {
    int result = INSIDE;
    int in = 0;
    int out = 0;
    BoundingBox bb;

    if (obj.getClass() == OctreeNode.class) {
      Vector ur =
          VectorMatrixFactory.newVector(((OctreeNode) obj).getLowerLeft().get(0) + ((OctreeNode) obj).getLength(),
              ((OctreeNode) obj).getLowerLeft().get(1) + ((OctreeNode) obj).getLength(),
              ((OctreeNode) obj).getLowerLeft().get(2) + ((OctreeNode) obj).getLength());
      bb = new BoundingBox(((OctreeNode) obj).getLowerLeft(), ur);
    } else {
      bb = obj.getBoundingBox();
    }

    Vector[] corner_points = calcCornerPointsOfBoundingBox(bb);

    for (int i = 0; i < frustum.length; i++) {
      in = 0;
      out = 0;

      for (int j = 0; j < corner_points.length && (in == 0 || out == 0); j++) {
        // fuer jede Ecke der Bounding Box
        // pruefe, ob sie innerhalb oder ausserhalb liegt
        if (frustum[i].computeSignedDistance(corner_points[j]) > 0) {
          in++;
        } else {
          out++;
        }
      }

      if (in == 0) {
        // System.out.println("RETURN OUTSIDE");
        return OUTSIDE;
      } else if (out > 0) { // in > 0 && out > 0 -> intersect
        // System.out.println("RETURN INTERSECT");
        result = INTERSECT;
      }
    }
    // System.out.println("RETURN INSIDE");
    return result;

  }

  /**
   * erzeugt aus den Eckpunkten des View Frustums ein TriangleMesh
   * 
   * @param corner_points
   *          Eckpunkte des View Frustums
   * @return erzeugtes TriangleMesh, das das View Frustum eingrenzt
   */
  public TriangleMesh getFrustumMesh(Vector[] corner_points) {
    TriangleMesh mesh = new TriangleMesh();
    // nahe Ebene
    mesh.addTriangle(new Triangle(4, 6, 7)); // near right
    mesh.addTriangle(new Triangle(4, 5, 7)); // near left
    // ferne Ebene
    mesh.addTriangle(new Triangle(0, 2, 3)); // far left
    mesh.addTriangle(new Triangle(0, 1, 3)); // far left
    // rechte Ebene
    mesh.addTriangle(new Triangle(0, 2, 6)); // right right
    mesh.addTriangle(new Triangle(0, 4, 6)); // left left
    // linke Ebene
    mesh.addTriangle(new Triangle(5, 7, 3));
    mesh.addTriangle(new Triangle(5, 1, 3));
    // obere Ebene
    mesh.addTriangle(new Triangle(4, 5, 1));
    mesh.addTriangle(new Triangle(4, 0, 1));
    // untere Ebene
    mesh.addTriangle(new Triangle(2, 6, 7));
    mesh.addTriangle(new Triangle(2, 3, 7));

    for (int i = 0; i < corner_points.length; i++) {
      mesh.addVertex(new Vertex(corner_points[i], VectorMatrixFactory.newVector(1, 0, 0)));
    }
    mesh.getMaterial().setTransparency(frustumTransparency);
    mesh.getMaterial().setRenderMode(Normals.PER_FACET);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    return mesh;
  }

  /**
   * berechnet die Eckpunkte einer Bounding box
   * 
   * @param b_box
   *          b_box
   * @return Eckpunkte
   */
  private Vector[] calcCornerPointsOfBoundingBox(BoundingBox b_box) {
    // nach
    // http://www.lighthouse3d.com/tutorials/view-frustum-culling/geometric-approach-testing-boxes/

    Vector[] cornerPoints = new Vector[8];
    for (int i = 0; i < cornerPoints.length; ++i) {
      cornerPoints[i] = VectorMatrixFactory.newVector(3);
    }

    Vector center = b_box.getCenter();
    Vector lnl, lfl, lfr, lnr, ufr, unr, unl, ufl; // corner points of
                                                   // bounding box

    // berechne Eckpunkte der Bonding Box
    // low near left
    lnl = b_box.getLowerLeft();
    cornerPoints[0] = lnl;

    // low far left
    double z = lnl.get(Z) + (center.get(Z) - lnl.get(Z)) * 2.0;
    lfl = VectorMatrixFactory.newVector(lnl.get(X), lnl.get(Y), z);
    cornerPoints[1] = lfl;

    // low far right
    double x = lfl.get(X) + (center.get(X) - lnl.get(X)) * 2.0;
    lfr = VectorMatrixFactory.newVector(x, lfl.get(Y), lfl.get(Z));
    cornerPoints[2] = lfr;

    // low near right
    x = b_box.getLowerLeft().get(X) + (center.get(X) - b_box.getLowerLeft().get(X)) * 2.0;
    lnr = VectorMatrixFactory.newVector(x, lnl.get(Y), lnl.get(Z));
    cornerPoints[3] = lnr;

    // up far right
    ufr = b_box.getUpperRight();
    cornerPoints[4] = ufr;

    // up near right
    z = ufr.get(Z) - (ufr.get(Z) - center.get(Z)) * 2.0;
    unr = VectorMatrixFactory.newVector(ufr.get(X), ufr.get(Y), z);
    cornerPoints[5] = unr;

    // up near left
    x = unr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    unl = VectorMatrixFactory.newVector(x, unr.get(Y), unr.get(Z));
    cornerPoints[6] = unl;

    // up far left
    x = ufr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    ufl = VectorMatrixFactory.newVector(x, ufr.get(Y), ufr.get(Z));
    cornerPoints[7] = ufl;

    return cornerPoints;

  }

  /**
   * prueft, welche OctreeNodes eines Octrees im View Frustum liegen, in
   * Kombination mit Scenegraph-Nodes: wenn sich sceneOctree und octreeMesh
   * schneiden, wird diese Funktion aufgerufen, um "genau zu gucken" welche
   * nodes des Meshes im Frustum liegen oder es schneidet
   * 
   * @param octreeNode
   *          der zu pruefende Octree
   * @param toDraw
   *          Liste, der die im Frustum liegenden OctreeNodes hinzugefuegt
   *          werden
   * @return Liste mit den im Frustum liegenden OctreeNodes
   */
  public ArrayList<OctreeNode<Integer>> checkOctree(OctreeNode<Integer> octreeNode,
      ArrayList<OctreeNode<Integer>> toDraw, CgNode node) {
    node.setVisible(false);
    if (isObjectInFrustum(octreeNode) == 0) {
      node.setVisible(true);
      return null;
    }
    if (isObjectInFrustum(octreeNode) == 2) {
      node.setVisible(false); // TODO
      // toDraw = traversalOctreeNodeIntersected(octreeNode, toDraw);
      // return toDraw;
    }
    return null;
  }

  /**
   * wird aufgerufen, wenn ein OctreeNode komplett innerhalb des Frustums liegt,
   * um an seine Leafnodes zu kommen
   * 
   * @param ocNode
   *          der OctreeNode, der komplett im Frustum liegt
   * @param toDraw
   *          ArrayList, der die leafnodes hinzugefuegt werden
   * @return
   */
  public ArrayList<OctreeNode<Integer>> traversalOctreeNode(OctreeNode<Integer> ocNode,
      ArrayList<OctreeNode<Integer>> toDraw, CgNode node) {
    if (ocNode.getNumberOfChildren() > 0) {
      for (int i = 0; i < ocNode.getNumberOfChildren(); i++) {
        traversalOctreeNode(ocNode.getChild(i), toDraw, node);
      }
    } else {
      toDraw.add(ocNode);
    }
    return toDraw;
  }

  /**
   * fuegt zu einem TriangleMesh alle Triangles hinzu, die der uebergebene
   * Octree indexiert
   * 
   * @param nodesInFrustum
   *          OctreeNodes im View Frustum
   * @param node
   *          der Content, von dem extrahiert werden soll
   * @return sichtbare Elemente
   */
  public ICgNodeContent extractNodeContent(OctreeNode<Integer> tree, CgNode node, OctreeNode<Integer> scene) {
    if (boxesIntersect(tree.getBoundingBox(), scene.getBoundingBox())) {
      ArrayList<OctreeNode<Integer>> toDraw = new ArrayList<OctreeNode<Integer>>();
      ICgNodeContent contentToDraw = null;
      checkOctree(tree, toDraw, node);

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
        contentToDraw.getMaterial().setTransparency(objectsTransparency);
        contentToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
        return contentToDraw;
      }
    } else {
      node.setVisible(false);
    }
    return null;
  }

  /**
   * ueberprueft, ob die Boundingboxes sich schneiden
   * 
   * @param one
   *          erste Boundingbox
   * @param two
   *          zweite Boundingbox
   * @return ob Schnitt stattfindet
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

  public ArrayList<OctreeNode<Integer>> traversalOctreeNodeIntersected(OctreeNode<Integer> oNode,
      ArrayList<OctreeNode<Integer>> toDraw) {
    if (oNode.getNumberOfChildren() > 0) {
      for (int i = 0; i < oNode.getNumberOfChildren(); i++) {
        traversalOctreeNodeIntersected(oNode.getChild(i), toDraw);
      }
    } else {
      if (isObjectInFrustum(oNode) == 0 || isObjectInFrustum(oNode) == 2) {
        toDraw.add(oNode);
      }
    }
    return null;
  }

  /**
   * Set the nodes in the view frustum visible and select visible triangles in
   * triangle meshes.
   */
  public void computeVisibleScenePart(CgRootNode rootNode) {
    if (this.objects.size() > 0) {
      for (int j = 0; j < objects.size(); j++) {
        for (int k = 0; k < visibleNodes.size(); k++) {
          addVisibleElementsToScene(rootNode, this, octrees.get(j), objects.get(j), visibleNodes.get(k));
        }
      }
    }
  }

  /**
   * wird aufgerufen, wenn ein OctreeNode komplett innerhalb des Frustums liegt,
   * um an seine Leafnodes zu kommen
   * 
   * @param node
   *          der OctreeNode, der komplett im Frustum liegt
   * @param objects
   *          ArrayList, der die leafnodes hinzugefuegt werden
   * @return
   */
  public ArrayList<CgNode> traversalOctreeNode(CgNode node, ArrayList<CgNode> objects) {
    if (node.getNumChildren() > 0) {
      for (int i = 0; i < node.getNumChildren(); i++) {
        traversalOctreeNode(node.getChildNode(i), objects);
      }
    } else {
      if (node.getContent() != null
          && (node.getContent().getClass() == TriangleMesh.class || node.getContent().getClass() == PointCloud.class)) {
        node.getContent().getMaterial().setTransparency(objectsTransparency);
        node.setVisible(true);
        objects.add(node);
      }
    }
    return objects;
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
   * erzeugt einen Octree fuer die Szene
   */
  public OctreeNode<Integer> createSceneOctree(ArrayList<CgNode> objects) {
    OctreeFactoryStrategyScene octreeFactoryStrategyScene = new OctreeFactoryStrategyScene(objects);
    OctreeFactory<Integer> octreeFactoryScene = new OctreeFactory<Integer>(octreeFactoryStrategyScene);
    OctreeNode<Integer> octreeSceneRoot = octreeFactoryScene.create(7, 5);
    return octreeSceneRoot;
  }

  /**
   * fuegt einer Liste die LeafNodes des SceneOctrees hinzu, die im Frustum
   * liegen oder es schneiden
   * 
   * @param vfc
   *          ViewFrustumCulling, fuer welches der SzeneOctree getestet wird
   * @param node
   *          SceneOctree
   * @param nodesInFrustum
   *          Liste, der die sichtbaren Nodes hinzugefuegt werden
   */
  public void extractNodesOfFrustum(ViewFrustumCulling vfc, OctreeNode<Integer> node,
      ArrayList<OctreeNode<Integer>> nodesInFrustum) {
    if (node.getNumberOfChildren() > 0) {
      for (int i = 0; i < node.getNumberOfChildren(); i++) {
        extractNodesOfFrustum(vfc, node.getChild(i), nodesInFrustum);
      }
    } else {
      if (vfc.isObjectInFrustum(node) == 0 || vfc.isObjectInFrustum(node) == 2) {
        nodesInFrustum.add(node);
      }
    }
  }

  public void addVisibleElementsToScene(CgNode rootNode, ViewFrustumCulling vfc, OctreeNode<Integer> tree, CgNode node,
      OctreeNode<Integer> scene) {
    ICgNodeContent partlyVisible = vfc.extractNodeContent(tree, node, scene);
    // System.out.println("8");
    if (vfc.isObjectInFrustum(node.getContent()) == 2) {
      // Objekte, die teilweise sichtbar sind, werden aktuell noch nicht
      // hinzugefuegt
      // CgNode newNode = new CgNode(partlyVisible, "partyVisible_object");
      // rootNode.addChild(newNode);
    }
    partlyVisible = null;
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    Logger.getInstance().message("Update");
    visibleNodes.clear();
    // Sichtbarkeit resetten
    for (int i = 0; i < objects.size(); i++) {
      objects.get(i).setVisible(true);
    }
    System.out.println(
        "Angle = " + Camera.getInstance().getOpeningAngle() + ", Far = " + Camera.getInstance().getFarClippingPlane()
            + ", Near = " + Camera.getInstance().getNearClippingPlane() + ", Eye =" + Camera.getInstance().getEye()
            + ", Ref =" + Camera.getInstance().getRef() + ", Up =" + Camera.getInstance().getUp());
    this.eye = Camera.getInstance().getEye();
    this.up = Camera.getInstance().getUp();
    calcPlanesOfFrustum(Camera.getInstance().getNearClippingPlane(), Camera.getInstance().getFarClippingPlane(), 1.0);
    extractNodesOfFrustum(this, octreeScene, visibleNodes);
    if (visibleNodes.size() > 0) {
      computeVisibleScenePart(rootNode);
    }
    // else{
    // for(int i = 0; i < objects.size(); i++){
    // objects.get(i).setVisible(false);
    // }
    // }
  }

  public void rebuildOctree() {
    // TODO

    objects = traversalOctreeNode(rootNode, objects);
    // for(int i = 0; i < objects.size(); i++){
    // objects.get(i).setVisible(true);
    // }
    this.octreeScene = createSceneOctree(objects);

    // Zeichne Ebenen
    // ITriangleMesh frustum = getFrustumMesh(vfc.getCorners());
    // frustum.getMaterial().setTransparency(objectsTransparency);

    // erzeuge Octrees fuer jedes Mesh

    for (int i = 0; i < objects.size(); i++) {
      ICgNodeContent content = objects.get(i).getContent();
      if (content instanceof ITriangleMesh) {
        ITriangleMesh mesh = (ITriangleMesh) content;
        TriangleMeshTools.cleanup(mesh);
        octrees.add(createMeshOctree((TriangleMesh) objects.get(i).getContent()));
        // objects.get(i).setVisible(false);
      }
    }
    computeVisibleScenePart(rootNode);

  }

}

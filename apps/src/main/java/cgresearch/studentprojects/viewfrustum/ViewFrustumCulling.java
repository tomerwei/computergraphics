package cgresearch.studentprojects.viewfrustum;

/**
 * Funktionalitaeten zum Berechnen des View Frustum Culling
 */

import java.util.ArrayList;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector3;
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
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.core.math.VectorMatrixFactory;

public class ViewFrustumCulling {

  // Kameraparameter
  private IVector3 eye;
  double angle;
  private IVector3 refPoint;
  private IVector3 up;
  private IVector3 cameraRight;
  private Plane[] frustum;

  // Eckpunkte des Frustums
  private IVector3[] cornerPoints;

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

  /**
   * Konstruktor
   * 
   * @param cam
   *          Kamera
   */
  public ViewFrustumCulling(Camera cam) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = VectorMatrixFactory.newIVector3(0.0, 0.0, -1.0);
    this.up = cam.getUp();
    this.cameraRight = (this.refPoint.cross(this.up));
    this.cameraRight.normalize();
    frustum = new Plane[6];
    cornerPoints = new Vector3[8];

    calcPlanesOfFrustum(-8.8, -4.9, 1.0); // is in view frustum
    // calc_planes_of_frustum(1, 3, 0.1); // is not in view frustum

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
    IVector3 fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl;
    nbr = VectorMatrixFactory.newIVector3(1.0, -1.0, -1.0);
    nbl = VectorMatrixFactory.newIVector3(-1.0, -1.0, -1.0);
    ntr = VectorMatrixFactory.newIVector3(1.0, 1.0, -1.0);
    ntl = VectorMatrixFactory.newIVector3(-1.0, 1.0, -1.0);
    fbr = VectorMatrixFactory.newIVector3(1.0, -1.0, 1.0);
    fbl = VectorMatrixFactory.newIVector3(-1.0, -1.0, 1.0);
    ftr = VectorMatrixFactory.newIVector3(1.0, 1.0, 1.0);
    ftl = VectorMatrixFactory.newIVector3(-1.0, 1.0, 1.0);
    IVector3[] corner_points = { fbr, fbl, ftr, ftl, nbr, nbl, ntr, ntl };
    vfc.setCornerPoints(corner_points);

    Plane[] planes = new Plane[6];
    planes[0] = vfc.calcPlane(VectorMatrixFactory.newIVector3(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newIVector3(-1.0, -1.0, 1.0), (VectorMatrixFactory.newIVector3(1.0, -1.0, 1.0))); // near
    planes[1] = vfc.calcPlane(VectorMatrixFactory.newIVector3(-1.0, 1.0, -1.0),
        VectorMatrixFactory.newIVector3(-1.0, -1.0, -1.0), (VectorMatrixFactory.newIVector3(1.0, -1.0, -1.0))); // far
    planes[1].setNormal(planes[1].getNormal().multiply(-1.0));
    planes[2] = vfc.calcPlane(VectorMatrixFactory.newIVector3(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newIVector3(-1.0, -1.0, 1.0), (VectorMatrixFactory.newIVector3(-1.0, -1.0, -1.0))); // left
    planes[2].setNormal(planes[2].getNormal().multiply(-1.0));
    planes[3] = vfc.calcPlane(VectorMatrixFactory.newIVector3(1.0, 1.0, 1.0),
        VectorMatrixFactory.newIVector3(1.0, -1.0, 1.0), (VectorMatrixFactory.newIVector3(1.0, -1.0, -1.0))); // right
    planes[4] = vfc.calcPlane(VectorMatrixFactory.newIVector3(-1.0, 1.0, 1.0),
        VectorMatrixFactory.newIVector3(1.0, 1.0, 1.0), (VectorMatrixFactory.newIVector3(1.0, 1.0, -1.0))); // top
    planes[5] = vfc.calcPlane(VectorMatrixFactory.newIVector3(-1.0, -1.0, 1.0),
        VectorMatrixFactory.newIVector3(1.0, -1.0, 1.0), (VectorMatrixFactory.newIVector3(1.0, -1.0, -1.0))); // bottom
    planes[5].setNormal(planes[5].getNormal().multiply(-1.0));
    vfc.setFrustum(planes);

    return vfc;
  }

  /**
   * Getter
   */
  public IVector3[] get_corners() {
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
  public void setEye(IVector3 eye) {
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
  public void setRefPoint(IVector3 refPoint) {
    this.refPoint = refPoint;
  }

  /**
   * Setter
   */
  public void setUp(IVector3 up) {
    this.up = up;
  }

  /**
   * Setter
   */
  public void setCameraRight(IVector3 cameraRight) {
    this.cameraRight = cameraRight;
  }

  /**
   * Setter
   */
  public void setCornerPoints(IVector3[] cornerPoints) {
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
    IVector3 nearCenter, farCenter; // distances
    double nearHeight, farHeight, nearWidth, farWidth; // sizes
    IVector3 farBottomRight, farBottomLeft, farTopRight, farTopLeft; // corners
    IVector3 nearBottomRight, nearBottomLeft, nearTopRight, nearTopleft; // corners
    Plane nearPlane, farPlane, bottomPlane, topPlane, leftPlane, rightPlane; // planes

    // Berechne CenterPunkte fuer nahe und ferne Ebene
    nearCenter = this.eye.subtract(this.refPoint.multiply(nearDistance));
    farCenter = this.eye.subtract(this.refPoint.multiply(farDistance));

    // Berechne Breite und Hoehe der nahen und fernen Ebene
    nearHeight = 2 * Math.tan(this.angle / 2) * nearDistance;
    farHeight = 2 * Math.tan(this.angle / 2) * farDistance;
    nearWidth = nearHeight * viewRatio;
    farWidth = farHeight * viewRatio;

    // Berechne Eckpunkte der Ebenen
    farBottomRight =
        farCenter.add(this.up.multiply(farHeight * 0.5).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbr] = farBottomRight;
    // System.out.println("farBottomRight = " + farBottomRight);
    farBottomLeft = farCenter.add(this.up.multiply(farHeight * 0.5).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbl] = farBottomLeft;
    // System.out.println("farBottomLeft = " + farBottomLeft);
    farTopRight = farCenter.subtract(this.up.multiply(farHeight * 0.5).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftr] = farTopRight;
    // System.out.println("farTopRight = " + farTopRight);
    farTopLeft =
        farCenter.subtract(this.up.multiply(farHeight * 0.5).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftl] = farTopLeft;
    // System.out.println("farTopLeft = " + farTopLeft);

    nearBottomRight =
        nearCenter.add(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbr] = nearBottomRight;
    // System.out.println("nearBottomRight = " + nearBottomRight);
    nearBottomLeft = nearCenter.add(this.up.multiply(nearHeight * 0.5).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbl] = nearBottomLeft;
    // System.out.println("nearBottomLeft = " + nearBottomLeft);
    nearTopRight =
        nearCenter.subtract(this.up.multiply(nearHeight * 0.5).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntr] = nearTopRight;
    // System.out.println("nearTopRight = " + nearTopRight);
    nearTopleft =
        nearCenter.subtract(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntl] = nearTopleft;
    // System.out.println("nearTopleft = " + nearTopleft);

    // Berechne Ebene
    // nah
    nearPlane = calcPlane(nearTopRight, nearBottomRight, nearTopleft);
    this.frustum[near] = nearPlane;
    // System.out.println("near_plane normal = "+ near_plane.getNormal() );

    // fern
    farPlane = calcPlane(farTopRight, farBottomRight, farTopLeft);
    farPlane.setNormal(farPlane.getNormal().multiply(-1.0)); // the normals must
                                                             // point inwards
                                                             // the frustum, so
                                                             // they have to be
                                                             // inverted
    this.frustum[far] = farPlane;
    // System.out.println("far_plane normal = "+ far_plane.getNormal() );

    // links
    leftPlane = calcPlane(nearTopRight, nearBottomRight, farTopRight);
    leftPlane.setNormal(leftPlane.getNormal().multiply(-1.0));
    this.frustum[left] = leftPlane;
    // System.out.println("left_plane normal = "+ left_plane.getNormal() );

    // rechts
    rightPlane = calcPlane(nearTopleft, nearBottomLeft, farTopLeft);
    // right_plane.setNormal(right_plane.getNormal().multiply(-1.0));
    this.frustum[right] = rightPlane;
    // System.out.println("right_plane normal = "+ right_plane.getNormal() );

    // oben
    topPlane = calcPlane(nearBottomRight, farBottomRight, nearBottomLeft);
    // top_plane.setNormal(top_plane.getNormal().multiply(-1.0));
    this.frustum[top] = topPlane;
    // System.out.println("top_plane normal = "+ top_plane.getNormal() );

    // unten
    bottomPlane = calcPlane(nearTopRight, farTopRight, nearTopleft);
    bottomPlane.setNormal(bottomPlane.getNormal().multiply(-1.0));
    this.frustum[bottom] = bottomPlane;
    // System.out.println("bottom_plane normal = "+ bottom_plane.getNormal() );

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
  public Plane calcPlane(IVector3 point_one, IVector3 point_two, IVector3 point_three) {
    IVector3 spanU = point_two.subtract(point_one);
    IVector3 spanV = point_three.subtract(point_one);
    IVector3 normal = (spanV.cross(spanU));
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
  public boolean isPointInFrustum(IVector3 point) {

    boolean result = true;
    for (int i = 0; i < this.frustum.length; i++) {
      if (frustum[i].computeSignedDistance(point) < 0)
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
      IVector3 ur =
          VectorMatrixFactory.newIVector3(((OctreeNode) obj).getLowerLeft().get(0) + ((OctreeNode) obj).getLength(),
              ((OctreeNode) obj).getLowerLeft().get(1) + ((OctreeNode) obj).getLength(),
              ((OctreeNode) obj).getLowerLeft().get(2) + ((OctreeNode) obj).getLength());
      bb = new BoundingBox(((OctreeNode) obj).getLowerLeft(), ur);
    } else {
      bb = obj.getBoundingBox();
    }
    IVector3[] corner_points = calcCornerPointsOfBoundingBox(bb);

    for (int i = 0; i < frustum.length; i++) {
      in = 0;
      out = 0;

      for (int j = 0; j < corner_points.length && (in == 0 || out == 0); j++) {
        // fuer jede Ecke der Bounding Box
        // pruefe, ob sie innerhalb oder au�erhalb liegt
        if (frustum[i].computeSignedDistance(corner_points[j]) < 0) {
          out++;
        } else {
          in++;
        }
      }

      if (in == 0) {
        return OUTSIDE;
      } else if (out > 0) { // in > 0 && out > 0 -> intersect
        result = INTERSECT;
      }
    }
    return result;

  }

  /**
   * erzeugt aus den Eckpunkten des View Frustums ein TriangleMesh
   * 
   * @param corner_points
   *          Eckpunkte des View Frustums
   * @return erzeugtes TriangleMesh, das das View Frustum eingrenzt
   */
  public TriangleMesh getFrustumMesh(IVector3[] corner_points) {
    TriangleMesh mesh = new TriangleMesh();
    // nahe Ebene
    mesh.addTriangle(new Triangle(4, 6, 7)); // near right //TODO vertauscht?!
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
      mesh.addVertex(new Vertex(corner_points[i], VectorMatrixFactory.newIVector3(1, 0, 0)));
    }
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
  private IVector3[] calcCornerPointsOfBoundingBox(BoundingBox b_box) {
    // nach
    // http://www.lighthouse3d.com/tutorials/view-frustum-culling/geometric-approach-testing-boxes/

    IVector3[] cornerPoints = new IVector3[8];
    for (int i = 0; i < cornerPoints.length; ++i) {
      cornerPoints[i] = VectorMatrixFactory.newIVector3();
    }

    IVector3 center = b_box.getCenter();
    IVector3 lnl, lfl, lfr, lnr, ufr, unr, unl, ufl; // corner points of
                                                     // bounding box

    // berechne Eckpunkte der Bonding Box
    // low near left
    lnl = b_box.getLowerLeft();
    cornerPoints[0] = lnl;

    // low far left
    double z = lnl.get(Z) + (center.get(Z) - lnl.get(Z)) * 2.0;
    lfl = VectorMatrixFactory.newIVector3(lnl.get(X), lnl.get(Y), z);
    cornerPoints[1] = lfl;

    // low far right
    double x = lfl.get(X) + (center.get(X) - lnl.get(X)) * 2.0;
    lfr = VectorMatrixFactory.newIVector3(x, lfl.get(Y), lfl.get(Z));
    cornerPoints[2] = lfr;

    // low near right
    x = b_box.getLowerLeft().get(X) + (center.get(X) - b_box.getLowerLeft().get(X)) * 2.0;
    lnr = VectorMatrixFactory.newIVector3(x, lnl.get(Y), lnl.get(Z));
    cornerPoints[3] = lnr;

    // up far right
    ufr = b_box.getUpperRight();
    cornerPoints[4] = ufr;

    // up near right
    z = ufr.get(Z) - (ufr.get(Z) - center.get(Z)) * 2.0;
    unr = VectorMatrixFactory.newIVector3(ufr.get(X), ufr.get(Y), z);
    cornerPoints[5] = unr;

    // up near left
    x = unr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    unl = VectorMatrixFactory.newIVector3(x, unr.get(Y), unr.get(Z));
    cornerPoints[6] = unl;

    // up far left
    x = ufr.get(X) - (ufr.get(X) - center.get(X)) * 2.0;
    ufl = VectorMatrixFactory.newIVector3(x, ufr.get(Y), ufr.get(Z));
    cornerPoints[7] = ufl;

    return cornerPoints;

  }

  /**
   * prueft, welche OctreeNodes eines Octrees im View Frustum liegen
   * 
   * @param octreeNode
   *          der zu pruefende Octree
   * @param toDraw
   *          Liste, der die im Frustum liegenden OctreeNodes hinzugefuegt
   *          werden
   * @return Liste mit den im Frustum liegenden OctreeNodes
   */
  public ArrayList<OctreeNode<Integer>> checkOctree(OctreeNode<Integer> octreeNode,
      ArrayList<OctreeNode<Integer>> toDraw) {
    if (isObjectInFrustum(octreeNode) == 1) {
      // drau�en
      return null;
    }
    if (isObjectInFrustum(octreeNode) == 0) {
      if (octreeNode.getNumberOfChildren() > 0) {
        // drinnen, ueberpruefe Kindknoten
        traversalOctreeNode(octreeNode, toDraw);
      }
      if (octreeNode.getNumberOfChildren() == 0) {
        // drinnen und keine Kindknoten, also fuege den Node hinzu
        toDraw.add(octreeNode);// (octree_node, to_draw);
      }
    }
    if (isObjectInFrustum(octreeNode) == 2 && octreeNode.getNumberOfChildren() > 0) {
      // geschnitten und noch Kindknoten, also ueberpruefe diese
      for (int i = 0; i < octreeNode.getNumberOfChildren(); i++) {
        checkOctree(octreeNode.getChild(i), toDraw);
      }
    }
    if (isObjectInFrustum(octreeNode) == 2 && octreeNode.getNumberOfChildren() == 0) {
      // geschnitten und keine Kinder, also fuege diesen Knoten nicht hinzu
      return null;
    }
    return toDraw;
  }

  /**
   * wird aufgerufen, wenn ein OctreeNode innerhalb des Frustums liegt, um an
   * seine Leaf Nodes zu kommen
   * 
   * @param node
   *          der OctreeNode, der komplett im Frustum liegt
   * @param toDraw
   *          ArrayList, der die leaf nodes hinzugefuegt werden
   * @return
   */
  public ArrayList<OctreeNode<Integer>> traversalOctreeNode(OctreeNode<Integer> node,
      ArrayList<OctreeNode<Integer>> toDraw) {
    if (node.getNumberOfChildren() > 0) {
      for (int i = 0; i < node.getNumberOfChildren(); i++) {
        traversalOctreeNode(node.getChild(i), toDraw);
      }
    } else {
      toDraw.add(node);
    }
    return toDraw;
  }

  /**
   * fuegt zu einem TriangleMesh alle Triangles hinzu, die der uebergebene
   * Octree indexiert
   * 
   * @param nodesInFrustum
   *          OctreeNodes im View Frustum
   * @param content
   *          der Content, von dem extrahiert werden soll
   * @return sichtbare Elemente
   */
  public ICgNodeContent extractMesh(ArrayList<OctreeNode<Integer>> nodesInFrustum, ICgNodeContent content) {
    if (nodesInFrustum == null) {
      System.out.println("BIN HIER");
      return null;
    }
    if (content.getClass() == TriangleMesh.class) {
      TriangleMesh meshToDraw = new TriangleMesh();
      for (int m = 0; m < ((TriangleMesh) content).getNumberOfVertices(); m++) {
        meshToDraw.addVertex(((TriangleMesh) content).getVertex(m));
      }
      for (int i = 0; i < nodesInFrustum.size(); i++) {
        for (int j = 0; j < nodesInFrustum.get(i).getNumberOfElements(); j++) {
          ITriangle t = ((TriangleMesh) content).getTriangle(nodesInFrustum.get(i).getElement(j));
          meshToDraw.addTriangle(t);
        }
      }
      meshToDraw.computeVertexNormals();
      meshToDraw.computeTriangleNormals();
      meshToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      return meshToDraw;
    }

    if (content.getClass() == PointCloud.class) {
      PointCloud pCloudToDraw = new PointCloud();
      for (int i = 0; i < nodesInFrustum.size(); i++) {
        if (nodesInFrustum.get(i).getNumberOfChildren() == 0) {
          for (int j = 0; j < nodesInFrustum.get(i).getNumberOfElements(); j++) {
            pCloudToDraw.addPoint(((PointCloud) content).getPoint(j));
          }
        }
      }
      return pCloudToDraw;
    }
    return null;
  }
}

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
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTools;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;
import cgresearch.core.math.VectorMatrixFactory;

public class ViewFrustumCulling {
    
    //merkt sich, welche OctreeNodes schon gecheckt wurden, um mehrere Aufrufe mit demselben OctreeNode zu vermeiden
    private ArrayList<OctreeNode<Integer>> checked = new ArrayList<OctreeNode<Integer>>();

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
   * @param nearDistance
   *          Distanz der nahen Ebene zur Kamera, ausgehend vom Augpunkt (0,0,5)
   * @param farDistance
   *          Distanz der fernen Ebene zur Kamera, ausgehend vom Augpunkt (0,0,5)
   *          merke: die nahe Ebene ist immer hinter und dichter als die ferne Ebene, nearDistance 
   *          muss also immer kleiner sein als farDistance
   */
  public ViewFrustumCulling(Camera cam, double nearDistance, double farDistance) {
    super();
    this.eye = cam.getEye();
    this.angle = cam.getOpeningAngle();
    this.refPoint = VectorMatrixFactory.newIVector3(0.0, 0.0, -1.0);
    this.up = cam.getUp();
    this.cameraRight = (this.refPoint.cross(this.up));
    this.cameraRight.normalize();
    frustum = new Plane[6];
    cornerPoints = new Vector3[8];

    //dritter Parameter beeinflusst die Breite des Frustums
//    calcPlanesOfFrustum(-8.0, -3.0, 1.0); //
//    calcPlanesOfFrustum(3.0, -1.0, 1.0); // 
    calcPlanesOfFrustum(nearDistance, farDistance, 1.0); //

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
  public IVector3[] getCorners() {
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
    // Sonderfall, vertausche die Hoehen
    if(Math.abs(nearDistance) > Math.abs(farDistance) && !(nearDistance >0 && farDistance>0 ||  nearDistance <0 && farDistance<0)){
        farHeight = 2 * Math.tan(this.angle / 2) * nearDistance;
        nearHeight = 2 * Math.tan(this.angle / 2) * farDistance;
    }
    else{
        nearHeight = 2 * Math.tan(this.angle / 2) * nearDistance;
        farHeight = 2 * Math.tan(this.angle / 2) * farDistance;
    }
    nearWidth = nearHeight * viewRatio;
    farWidth = farHeight * viewRatio;

    // Berechne Eckpunkte der Ebenen

    farBottomRight =
        farCenter.add(this.up.multiply(farHeight * 0.5).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbr] = farBottomRight;
    
    farBottomLeft = 
        farCenter.add(this.up.multiply(farHeight * 0.5).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[fbl] = farBottomLeft;
    
    farTopRight =
        farCenter.subtract(this.up.multiply(farHeight * 0.5).add(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftr] = farTopRight;
    
    farTopLeft =
        farCenter.subtract(this.up.multiply(farHeight * 0.5).subtract(this.cameraRight.multiply(farWidth * 0.5)));
    cornerPoints[ftl] = farTopLeft;

     
    nearBottomRight =
        nearCenter.add(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbr] = nearBottomRight;
    
    nearBottomLeft = 
        nearCenter.add(this.up.multiply(nearHeight * 0.5).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[nbl] = nearBottomLeft;
    
    nearTopRight =
        nearCenter.subtract(this.up.multiply(nearHeight * 0.5).add(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntr] = nearTopRight;
    
    nearTopleft =
        nearCenter.subtract(this.up.multiply(nearHeight * 0.5).subtract(this.cameraRight.multiply(nearWidth * 0.5)));
    cornerPoints[ntl] = nearTopleft;
     
     if((nearDistance > 0 && farDistance < 0) ||
             (nearDistance < 0 && farDistance > 0)){
         //bei negativem Vorzeichen werden top/bottom und links rechts vertauscht, rueckgaengig
         cornerPoints[fbr]  = cornerPoints[fbr].multiply(-1.0);
         cornerPoints[fbl]  = cornerPoints[fbl].multiply(-1.0);
         cornerPoints[ftr]  = cornerPoints[ftr].multiply(-1.0);
         cornerPoints[ftl]  = cornerPoints[ftl].multiply(-1.0);
         
         //near- und farDistance muessen aber bleiben
         for(int i = 0; i < 4; i++){
             double tmp  = cornerPoints[i].get(Z);
             cornerPoints[i].set(Z, tmp * -1);
             
         }
//         cornerPoints[nbr]  = cornerPoints[nbr].multiply(-1.0);
//         cornerPoints[nbl]  = cornerPoints[nbl].multiply(-1.0);
//         cornerPoints[ntr]  = cornerPoints[ntr].multiply(-1.0);
//         cornerPoints[ntl]  = cornerPoints[ntl].multiply(-1.0);
     }
//     System.out.println("farBottomRight = " + cornerPoints[fbr]);
//     System.out.println("farBottomLeft = " + cornerPoints[fbl]);
//     System.out.println("farTopRight = " + cornerPoints[ftr]);
//     System.out.println("farTopLeft = " + cornerPoints[ftl]);
//     System.out.println("nearBottomRight = " + cornerPoints[nbr]);
//     System.out.println("nearBottomLeft = " + cornerPoints[nbl]);
//     System.out.println("nearTopRight = " + cornerPoints[ntr]);
//     System.out.println("nearTopleft = " + cornerPoints[ntl]);

    // Berechne Ebene
    // nah
    nearPlane = calcPlane(cornerPoints[ntr], cornerPoints[nbr], cornerPoints[ntl]);
    nearPlane.setNormal(nearPlane.getNormal().multiply(-1.0)); // the normals must point inwards the frustum, so they have to be inverted
    this.frustum[near] = nearPlane;
//     System.out.println("near_plane normal = "+ nearPlane.getNormal() );

    // fern
    farPlane = calcPlane(cornerPoints[ftr], cornerPoints[fbr], cornerPoints[ftl]);
//    farPlane.setNormal(farPlane.getNormal().multiply(-1.0)); // the normals must point inwards the frustum, so they have to be inverted
    this.frustum[far] = farPlane;
//     System.out.println("far_plane normal = "+ farPlane.getNormal() );

    // links
    leftPlane = calcPlane(cornerPoints[ntr], cornerPoints[nbr], cornerPoints[ftr]);
//    leftPlane.setNormal(leftPlane.getNormal().multiply(-1.0));
    this.frustum[left] = leftPlane;
//     System.out.println("left_plane normal = "+ leftPlane.getNormal() );

    // rechts
    rightPlane = calcPlane(cornerPoints[ntl], cornerPoints[nbl], cornerPoints[ftl]);
    rightPlane.setNormal(rightPlane.getNormal().multiply(-1.0));
    this.frustum[right] = rightPlane;
//     System.out.println("right_plane normal = "+ rightPlane.getNormal() );

    // oben
    topPlane = calcPlane(cornerPoints[nbr], cornerPoints[fbr], cornerPoints[nbl]);
    topPlane.setNormal(topPlane.getNormal().multiply(-1.0));
    this.frustum[top] = topPlane;
//    System.out.println("top_plane normal = "+ topPlane.getNormal() );

    // unten
    bottomPlane = calcPlane(cornerPoints[ntr], cornerPoints[ftr], cornerPoints[ntl]);
//    bottomPlane.setNormal(bottomPlane.getNormal().multiply(-1.0));
    this.frustum[bottom] = bottomPlane;
//    System.out.println("bottom_plane normal = "+ bottomPlane.getNormal() );

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
        // pruefe, ob sie innerhalb oder auï¿½erhalb liegt
        if (frustum[i].computeSignedDistance(corner_points[j]) > 0) {
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
   * prueft, welche OctreeNodes eines Octrees im View Frustum liegen,
   * in Kombination mit Scenegraph-Nodes: wenn sich sceneOctree und octreeMesh schneiden, wird diese Funktion aufgerufen,
   * um "genau zu gucken" welche nodes des Meshes im Frustum liegen oder es schneidet
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
      if(checked.contains(octreeNode)){
          return null;
      }
      else{ 
          checked.add(octreeNode);
      }
   // diese Abfrage ist nun uberflussig, da octreeNode aufgrund der vorherigen Abfrage auf 
   // Intersections mit der sceneBoundingBox das Frustum sowieso schneidet und nicht mehr 
   // komplett außerhalb liegen kan
//        
//    if (isObjectInFrustum(octreeNode) == 1) { 
//      return null;
//    }
    if (isObjectInFrustum(octreeNode) == 0) {
      if (octreeNode.getNumberOfChildren() > 0) {
        // komplett drinnen, also hole die leafNodes, weitere Ueberpruefung auf Intersection ueberflussig
        traversalOctreeNode(octreeNode, toDraw);
      }
      if (octreeNode.getNumberOfChildren() == 0) {
        // drinnen und keine Kindknoten, also fuege den Node hinzu
        toDraw.add(octreeNode);// (octree_node, to_draw);
      }
    }
    if (isObjectInFrustum(octreeNode) == 2) {
        if(octreeNode.getNumberOfChildren() > 0){
      // geschnitten und noch Kindknoten, also ueberpruefe diese
      for (int i = 0; i < octreeNode.getNumberOfChildren(); i++) {
        checkOctree(octreeNode.getChild(i), toDraw);
      }
      }
        if (octreeNode.getNumberOfChildren() == 0) {
            // geschnitten und keine Kinder, also fuege diesen Knoten hinzu
            toDraw.add(octreeNode);// (octree_node, to_draw);
        }
    }
    return toDraw;
  }

  /**
   * wird aufgerufen, wenn ein OctreeNode komplett innerhalb des Frustums liegt, um an
   * seine Leafnodes zu kommen
   * 
   * @param node
   *          der OctreeNode, der komplett im Frustum liegt
   * @param toDraw
   *          ArrayList, der die leafnodes hinzugefuegt werden
   * @return
   */
  public ArrayList<OctreeNode<Integer>> traversalOctreeNode(OctreeNode<Integer> node,
      ArrayList<OctreeNode<Integer>> toDraw) {
      if (node.getNumberOfChildren() > 0) {
          for (int i = 0; i < node.getNumberOfChildren(); i++) {
              traversalOctreeNode(node.getChild(i), toDraw);
          }
      }
      else{
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
  public ICgNodeContent extractNodeContent(OctreeNode<Integer> tree, ICgNodeContent content, OctreeNode<Integer> scene) {
    if(boxesIntersect(tree.getBoundingBox(), scene.getBoundingBox())){
//        System.out.println("INTERSECT");
        ArrayList<OctreeNode<Integer>> toDraw = new ArrayList<OctreeNode<Integer>>();
        ICgNodeContent contentToDraw = null;
        checkOctree(tree,toDraw);
        if (toDraw.size() == 0){
            return null;
        }
        if (content.getClass() == TriangleMesh.class) {
            contentToDraw = new TriangleMesh();
            for (int m = 0; m < ((TriangleMesh) content).getNumberOfVertices(); m++) {
                ((ITriangleMesh) contentToDraw).addVertex(((TriangleMesh) content).getVertex(m));
            }
            for (int i = 0; i < toDraw.size(); i++) {
                for (int j = 0; j < toDraw.get(i).getNumberOfElements(); j++) {
                    ITriangle t = ((TriangleMesh) content).getTriangle(toDraw.get(i).getElement(j));
                    ((ITriangleMesh) contentToDraw).addTriangle(t);
                }
            }
            ((ITriangleMesh) contentToDraw).computeVertexNormals();
            ((ITriangleMesh) contentToDraw).computeTriangleNormals();
            contentToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
            TriangleMeshTools.cleanup((ITriangleMesh)contentToDraw);
        }

        if (content.getClass() == PointCloud.class) {
            contentToDraw = new PointCloud();
            for (int i = 0; i < toDraw.size(); i++) {
                 for (int j = 0; j < toDraw.get(i).getNumberOfElements(); j++) {
                     ((PointCloud) contentToDraw).addPoint(((PointCloud) content).getPoint(toDraw.get(i).getElement(j)));
               }
             }
        }
        contentToDraw.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
        return contentToDraw;
   }

   return null;
 }
  
  /**
   * ueberprueft, ob die Boundingboxes sich schneiden
   * @param one erste Boundingbox
   * @param two zweite Boundingbox
   * @return ob Schnitt stattfindet
   */
  public boolean boxesIntersect(BoundingBox one, BoundingBox two){

          if(two.getUpperRight().get(X) < one.getLowerLeft().get(X)){ 
              return false;
          }
          if(two.getLowerLeft().get(X) > one.getUpperRight().get(X)){
              return false;
          }
          if(two.getUpperRight().get(Y) < one.getLowerLeft().get(Y)){
              return false;
          }
          if(two.getLowerLeft().get(Y) > one.getUpperRight().get(Y)){
              return false;
          }
          if(two.getUpperRight().get(Z) < one.getLowerLeft().get(Z)){
              return false;
          }
          if(two.getLowerLeft().get(Z) > one.getUpperRight().get(Z)){
              return false;
          }
          return true;
  }
}


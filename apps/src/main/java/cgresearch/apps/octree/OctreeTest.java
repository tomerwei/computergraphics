package cgresearch.apps.octree;

import cgresearch.JoglAppLauncher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTools;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.tree.OctreeFactory;
import cgresearch.graphics.datastructures.tree.OctreeFactoryStrategyTriangleMesh;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;

/**
 * This application allows to test the octree creation by computing its
 * intersection with a plane.
 * 
 * @author Philipp Jenke
 *
 */
public class OctreeTest extends CgApplication {

  /**
   * Octree root node.
   */
  private OctreeNode<Integer> octreeRootNode;

  /**
   * Triangle mesh containing the visible elements.
   */
  private ITriangleMesh visibleElementsMesh;

  /**
   * Triangle mesh to be analyzed.
   */
  private ITriangleMesh mesh;

  /**
   * Clipping plane
   */
  private Plane plane = new Plane(VectorFactory.createVector3(0, 0, 0),
      VectorFactory.createVector3(0.6, 0.3, -0.2).getNormalized());

  public OctreeTest() {

    // Read mesh
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
    mesh = null;
    if (meshes.size() == 0) {
      return;
    }
    mesh = meshes.get(0);
    TriangleMeshTools.cleanup(mesh);
    mesh.fitToUnitBox();
    // TriangleMeshTransformation.scale(mesh, 2);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    CgNode meshNode = new CgNode(mesh, "mesh");
    meshNode.setVisible(true);
    getCgRootNode().addChild(meshNode);

    // Create octree
    OctreeFactory<Integer> octreeFactory = new OctreeFactory<>(new OctreeFactoryStrategyTriangleMesh(mesh));
    octreeRootNode = octreeFactory.create(5, 5);
    octreeRootNode.getMaterial().setShaderId(Material.SHADER_BLACK);
    CgNode octreeNode = new CgNode(octreeRootNode, "octree");
    octreeNode.setVisible(true);
    getCgRootNode().addChild(octreeNode);

    // Create plane
    plane.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR3);
    plane.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    plane.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    CgNode clippingPlaneNode = new CgNode(plane, "clipping plane");
    clippingPlaneNode.setVisible(true);
    getCgRootNode().addChild(clippingPlaneNode);

    // Triangle mesh for visible elements
    visibleElementsMesh = new TriangleMesh();
    visibleElementsMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    visibleElementsMesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR0);
    computeVisibleElements();
    CgNode visibleElementsNode = new CgNode(visibleElementsMesh, "visible elements");
    visibleElementsNode.setVisible(false);
    getCgRootNode().addChild(visibleElementsNode);

    // Coordinate system
    CgNode coordinateSystem = new CoordinateSystem(0.1);
    coordinateSystem.setVisible(true);
    getCgRootNode().addChild(coordinateSystem);
  }

  /**
   * Fill the elements triangle mesh with the elements in the visible octree
   * nodes (positive halfspace of the plane).
   */
  private void computeVisibleElements() {
    visibleElementsMesh.clear();

    // Copy all vertices from base mesh
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      visibleElementsMesh.addVertex(new Vertex(mesh.getVertex(i)));
    }

    // Recursively find all visible elements in octree
    Set<Integer> visibleTriangles = new HashSet<Integer>();
    computeVisibleElements(octreeRootNode, visibleTriangles);
    // debugComputeAllElemementsInOctree(octreeRootNode, visibleTriangles);

    // Add all visible triangles to visible triangle mesh
    for (int triangleIndex : visibleTriangles) {
      ITriangle triangle = mesh.getTriangle(triangleIndex);
      visibleElementsMesh.addTriangle(triangle.getA(), triangle.getB(), triangle.getC());
    }
  }

  /**
   * Create a list of all triangles in the octree (ignoring the plane).
   * 
   * @param node
   *          Current node to be checked (recursively)
   * @param visibleTriangles
   *          Created list of visible elements.
   */
  @SuppressWarnings("unused")
  private void debugComputeAllElemementsInOctree(OctreeNode<Integer> node, Set<Integer> visibleTriangles) {
    if (node == null) {
      return;
    }

    // Add all triangles to set of visible triangles.
    for (int elementIndex = 0; elementIndex < node.getNumberOfElements(); elementIndex++) {
      visibleTriangles.add(node.getElement(elementIndex));
    }

    // Recursive descent
    for (int childIndex = 0; childIndex < node.getNumberOfChildren(); childIndex++) {
      debugComputeAllElemementsInOctree(node.getChild(childIndex), visibleTriangles);
    }
  }

  /**
   * Recursive version for a node: Fill the elements triangle mesh with the
   * elements in the visible octree nodes (positive halfspace of the plane).
   */
  private void computeVisibleElements(OctreeNode<Integer> node, Set<Integer> visibleTriangles) {
    if (node == null) {
      return;
    }

    if (!isVisible(node)) {
      return;
    }

    // Add all triangles to set of visible triangles.
    for (int elementIndex = 0; elementIndex < node.getNumberOfElements(); elementIndex++) {
      visibleTriangles.add(node.getElement(elementIndex));
    }

    // Recursive descent
    for (int childIndex = 0; childIndex < node.getNumberOfChildren(); childIndex++) {
      computeVisibleElements(node.getChild(childIndex), visibleTriangles);
    }
  }

  /**
   * Checks if a given octree node is in the positibe side of a plane. At least
   * one corner point must be in this side.
   * 
   * @param node
   *          Octree node to be checked.
   * @return True if visible.
   */
  private boolean isVisible(OctreeNode<Integer> node) {

    if (node == null) {
      return false;
    }

    // Determine corner positions
    List<Vector> corners = node.computeCornerPoints();

    // Check if at least one corner point is on the positive plane side.
    for (Vector point : corners) {
      if (plane.computeSignedDistance(point) > 0) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new OctreeTest();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);

  }
}

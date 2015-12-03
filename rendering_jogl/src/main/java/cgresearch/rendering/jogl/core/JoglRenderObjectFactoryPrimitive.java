/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.primitives.Cuboid;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.datastructures.primitives.IPrimitive;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.primitives.Sphere;
import cgresearch.graphics.datastructures.primitives.Tetrahedron;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Factory for primitive nodes.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryPrimitive implements IRenderObjectsFactory<JoglRenderNode> {

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
   * .Object, edu.haw.cg.scenegraph.CgNode)
   */
  @Override
  public JoglRenderNode createRenderObject(JoglRenderNode parentNode, CgNode cgNode) {

    if (!(cgNode.getContent() instanceof IPrimitive)) {
      return null;
    }

    if (cgNode.getContent() instanceof Sphere) {
      return createSphere(parentNode, cgNode, (Sphere) cgNode.getContent());
    } else if (cgNode.getContent() instanceof Cylinder) {
      return createCylinder(parentNode, cgNode, (Cylinder) cgNode.getContent());
    } else if (cgNode.getContent() instanceof Cuboid) {
      return createCuboid(parentNode, cgNode, (Cuboid) cgNode.getContent());
    } else if (cgNode.getContent() instanceof Arrow) {
      return createArrow(parentNode, cgNode, (Arrow) cgNode.getContent());
    } else if (cgNode.getContent() instanceof Line3D) {
      return createLine3D(parentNode, cgNode, (Line3D) cgNode.getContent(), 10);
    } else if (cgNode.getContent() instanceof Tetrahedron) {
      return createTetrahedron(parentNode, cgNode, (Tetrahedron) cgNode.getContent());
    } else if (cgNode.getContent() instanceof Plane) {
      return createPlane(parentNode, cgNode, (Plane) cgNode.getContent());
    } else {
      Logger.getInstance().message("Render object factory cannot handle primitive");
      return null;
    }

  }

  private JoglRenderNode createPlane(JoglRenderNode parentNode, CgNode cgNode, Plane plane) {
    double SIZE = 0.5;
    ITriangleMesh mesh = TriangleMeshFactory.createPlane(plane.getPoint(), plane.getNormal(), SIZE);
    if (mesh == null) {
      Logger.getInstance().error("Failed to create cylinder mesh.");
      return null;
    }
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  private JoglRenderNode createCylinder(JoglRenderNode parentNode, CgNode cgNode, Cylinder cylinder) {
    ITriangleMesh mesh = TriangleMeshFactory.createCylinder(cylinder, 40);
    if (mesh == null) {
      Logger.getInstance().error("Failed to create cylinder mesh.");
      return null;
    }
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /**
   * Create a render node for a sphere.
   */
  private JoglRenderNode createSphere(JoglRenderNode parentNode, CgNode cgNode, Sphere sphere) {
    ITriangleMesh mesh = TriangleMeshFactory.createSphere(20);
    TriangleMeshTransformation.scale(mesh,
        VectorMatrixFactory.newIVector3(sphere.getRadius(), sphere.getRadius(), sphere.getRadius()));
    TriangleMeshTransformation.translate(mesh, sphere.getCenter());
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /**
   * Create a render node for a cuboid.
   */
  private JoglRenderNode createCuboid(JoglRenderNode parentNode, CgNode cgNode, Cuboid cuboid) {
    ITriangleMesh mesh = TriangleMeshFactory.createCube();
    mesh.getMaterial().setShowSophisticatesMesh(true);
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);
    TriangleMeshTransformation.scale(mesh, cuboid.getDimensions());
    TriangleMeshTransformation.translate(mesh, cuboid.getCenter());
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /**
   * Create a render node for a cuboid.
   */
  private JoglRenderNode createTetrahedron(JoglRenderNode parentNode, CgNode cgNode, Tetrahedron tetrahedron) {
    ITriangleMesh mesh = TriangleMeshFactory.createTetrahedron();
    mesh.getMaterial().setRenderMode(Material.Normals.PER_FACET);
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /**
   * Create a render node for an arrow.
   */
  public static JoglRenderNode createArrow(JoglRenderNode parentNode, CgNode cgNode, Arrow arrow) {
    ITriangleMesh mesh = TriangleMeshFactory.createArrow(arrow);
    mesh.computeTriangleNormals();
    mesh.invertFaceNormals();
    mesh.getMaterial().copyFrom(arrow.getMaterial());
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /**
   * Create a render node for an arrow.
   */
  private static JoglRenderNode createLine3D(JoglRenderNode parentNode, CgNode cgNode, Line3D line, int resolution) {
    ITriangleMesh mesh = TriangleMeshFactory.createLine3D(line, resolution, 0.015);
    JoglRenderNode renderNode = new JoglRenderNode(cgNode, new RenderContentTriangleMesh(mesh));
    return renderNode;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
   */
  @Override
  public Class<?> getType() {
    return IPrimitive.class;
  }

}

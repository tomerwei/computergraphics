/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;

/**
 * Representation of the coordinate system arrows in x-, y- and z-direction.
 * 
 * @author Philipp Jenke
 * 
 */
public class CoordinateSystem extends CgNode {

  /**
   * @param content
   * @param name
   */
  public CoordinateSystem() {
    this(1);
  }

  public CoordinateSystem(double scale) {
    super(null, "Coordinate system");

    // x-direction
    Arrow arrowX = new Arrow(VectorMatrixFactory.newVector(0, 0, 0), VectorMatrixFactory.newVector(scale, 0, 0));
    arrowX.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newVector(0.75, 0.25, 0.25));
    arrowX.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowX.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowXNode = new CgNode(arrowX, "x-direction");
    addChild(arrowXNode);

    // y-direction
    Arrow arrowY = new Arrow(VectorMatrixFactory.newVector(0, 0, 0), VectorMatrixFactory.newVector(0, scale, 0));
    arrowY.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newVector(0.25, 0.75, 0.25));
    arrowY.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowY.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowYNode = new CgNode(arrowY, "y-direction");
    addChild(arrowYNode);

    // z-direction
    Arrow arrowZ = new Arrow(VectorMatrixFactory.newVector(0, 0, 0), VectorMatrixFactory.newVector(0, 0, scale));
    arrowZ.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newVector(0.25, 0.25, 0.75));
    arrowZ.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowZ.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowZNode = new CgNode(arrowZ, "z-direction");
    addChild(arrowZNode);
  }

}

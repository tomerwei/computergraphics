/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.material.Material;

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
		super(null, "Coordinate system");

		// x-direction
		Arrow arrowX = new Arrow(VectorMatrixFactory.newIVector3(0, 0, 0),
				VectorMatrixFactory.newIVector3(1, 0, 0));
		arrowX.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25));
		arrowX.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		CgNode arrowXNode = new CgNode(arrowX, "x-direction");
		addChild(arrowXNode);

		// y-direction
		Arrow arrowY = new Arrow(VectorMatrixFactory.newIVector3(0, 0, 0),
				VectorMatrixFactory.newIVector3(0, 1, 0));
		arrowY.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.25, 0.75, 0.25));
		arrowY.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		CgNode arrowYNode = new CgNode(arrowY, "y-direction");
		addChild(arrowYNode);

		// z-direction
		Arrow arrowZ = new Arrow(VectorMatrixFactory.newIVector3(0, 0, 0),
				VectorMatrixFactory.newIVector3(0, 0, 1));
		arrowZ.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.25, 0.25, 0.75));
		arrowZ.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		CgNode arrowZNode = new CgNode(arrowZ, "z-direction");
		addChild(arrowZNode);
	}

}

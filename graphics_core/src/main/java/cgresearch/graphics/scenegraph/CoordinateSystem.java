/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
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

  public enum Dimension {
    DIMENSION_2D, DIMENSION_3D
  }

  public CoordinateSystem() {
    super(null, "Coordinate system");
    createCoordinateSystem3D(1);
  }

  public CoordinateSystem(Dimension dimension, double scale) {
    super(null, "Coordinate system");
    if (dimension == Dimension.DIMENSION_2D) {
      createCoordinateSystem2D(scale);
    } else {
      createCoordinateSystem3D(scale);
    }
  }

  /**
   * Create coordinate system with lines.
   */
  private void createCoordinateSystem2D(double scale) {

    LineSegments[] lineSegments = new LineSegments[3];

    lineSegments[0] = new LineSegments();
    lineSegments[0].addPoint(VectorFactory.createVector3(0, 0, 0));
    lineSegments[0].addPoint(VectorFactory.createVector3(1, 0, 0));
    lineSegments[0].addLine(0, 1);
    lineSegments[0].setLineColor(0, VectorFactory.createVector3(1, 0, 0));
    addChild(new CgNode(lineSegments[0], "x"));
    lineSegments[1] = new LineSegments();
    lineSegments[1].addPoint(VectorFactory.createVector3(0, 0, 0));
    lineSegments[1].addPoint(VectorFactory.createVector3(0, 1, 0));
    lineSegments[1].addLine(0, 1);
    lineSegments[1].setLineColor(0, VectorFactory.createVector3(0, 1, 0));
    addChild(new CgNode(lineSegments[1], "y"));
    lineSegments[2] = new LineSegments();
    lineSegments[2].addPoint(VectorFactory.createVector3(0, 0, 0));
    lineSegments[2].addPoint(VectorFactory.createVector3(0, 0, 1));
    lineSegments[2].addLine(0, 1);
    lineSegments[2].setLineColor(0, VectorFactory.createVector3(0, 0, 1));
    addChild(new CgNode(lineSegments[2], "z"));
    for (LineSegments lineSegment : lineSegments) {
      lineSegment.getMaterial().setShaderId(Material.SHADER_COLOR);
    }
  }

  private void createCoordinateSystem3D(double scale) {
    // x-direction
    Arrow arrowX = new Arrow(VectorFactory.createVector3(0, 0, 0), VectorFactory.createVector3(scale, 0, 0));
    arrowX.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.75, 0.25, 0.25));
    arrowX.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowX.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowXNode = new CgNode(arrowX, "x-direction");
    addChild(arrowXNode);

    // y-direction
    Arrow arrowY = new Arrow(VectorFactory.createVector3(0, 0, 0), VectorFactory.createVector3(0, scale, 0));
    arrowY.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.25, 0.75, 0.25));
    arrowY.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowY.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowYNode = new CgNode(arrowY, "y-direction");
    addChild(arrowYNode);

    // z-direction
    Arrow arrowZ = new Arrow(VectorFactory.createVector3(0, 0, 0), VectorFactory.createVector3(0, 0, scale));
    arrowZ.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.25, 0.25, 0.75));
    arrowZ.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    arrowZ.getMaterial().setRenderMode(Normals.PER_FACET);
    CgNode arrowZNode = new CgNode(arrowZ, "z-direction");
    addChild(arrowZNode);
  }

}

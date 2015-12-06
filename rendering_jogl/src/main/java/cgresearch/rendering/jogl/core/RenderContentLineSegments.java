/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.datastructures.linesegments.LineSegments;

/**
 * A render node for the line segments data structures.
 * 
 * @author Philipp Jenke
 * 
 */
public class RenderContentLineSegments implements IRenderContent {

  /**
   * Reference to the triangle mesh.
   */
  private final LineSegments lineSegments;

  /**
   * Constructor
   */
  public RenderContentLineSegments(LineSegments lineSegments) {
    this.lineSegments = lineSegments;
  }

  @Override
  public void draw3D(GL2 gl) {
    if (lineSegments == null) {
      return;
    }

    gl.glBegin(GL2.GL_LINES);
    for (int lineIndex = 0; lineIndex < lineSegments.getNumberOfLines(); lineIndex++) {
      gl.glVertex3fv(lineSegments.getLineStartPoint(lineIndex).floatData(), 0);
      gl.glVertex3fv(lineSegments.getLineEndPoint(lineIndex).floatData(), 0);
    }
    gl.glEnd();
  }

  @Override
  public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation, IVector3[] nearPlaneCorners, boolean cameraPositionChanged) {

  }

  @Override
  public void updateRenderStructures() {
  }

  @Override
  public void afterDraw(GL2 gl) {
  }
}

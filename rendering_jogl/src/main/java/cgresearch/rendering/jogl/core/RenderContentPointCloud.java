/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.material.Material;

/**
 * Render node for a point cloud.
 * 
 * @author Philipp Jenke
 * 
 */
public class RenderContentPointCloud extends
    JoglRenderContent {

  /**
   * Reference to the rendered point cloud.
   */
  private final PointCloud pointCloud;

  /**
   * Container for the required JOGL buffers.
   */
  List<JoglBuffers> buffers = new ArrayList<JoglBuffers>();

  /**
   * Constants.
   */
  public static final int MAX_POINTS_PER_BUFFER = 1000000;

  /**
   * Constructor.
   */
  public RenderContentPointCloud(PointCloud pointCloud) {
    this.pointCloud = pointCloud;
    updateRenderStructures();
  }

  /**
   * Init structures.
   */
  private void createRenderStructures() {
    if (pointCloud.getNumberOfPoints() == 0) {
      return;
    }

    int numberOfBuffers =
        (pointCloud.getNumberOfPoints() - 1)
            / MAX_POINTS_PER_BUFFER + 1;
    resetBuffers(numberOfBuffers);

    for (int buffersIndex = 0; buffersIndex < numberOfBuffers; buffersIndex++) {
      int bufferSize =
          (buffersIndex != numberOfBuffers - 1) ? MAX_POINTS_PER_BUFFER
              : pointCloud.getNumberOfPoints()
                  - MAX_POINTS_PER_BUFFER
                  * (numberOfBuffers - 1);
      buffers.get(buffersIndex).setSize(bufferSize);

      // Init temp arrays
      float vb[] =
          new float[bufferSize
              * JoglBuffers.NUMBER_FLOATS_IN_VERTEX];
      float nb[] =
          new float[bufferSize
              * JoglBuffers.NUMBER_FLOATS_IN_VERTEX];
      float cb[] =
          new float[bufferSize
              * JoglBuffers.NUMBER_FLOATS_IN_VERTEX];
      float tcb[] =
          new float[bufferSize
              * JoglBuffers.NUMBER_FLOATS_TEXCOORD];
      int ib[] = new int[bufferSize];

      // Fill arrays
      for (int bufferEntryIndex = 0; bufferEntryIndex < bufferSize; bufferEntryIndex++) {
        int pointIndex =
            buffersIndex * MAX_POINTS_PER_BUFFER
                + bufferEntryIndex;
        Point point = pointCloud.getPoint(pointIndex);
        // Vertex buffer
        vb[bufferEntryIndex * 3] =
            (float) point.getPosition().get(0);
        vb[bufferEntryIndex * 3 + 1] =
            (float) point.getPosition().get(1);
        vb[bufferEntryIndex * 3 + 2] =
            (float) point.getPosition().get(2);
        // Normal buffer
        nb[bufferEntryIndex * 3] =
            (float) (point.getNormal().get(0));
        nb[bufferEntryIndex * 3 + 1] =
            (float) (point.getNormal().get(1));
        nb[bufferEntryIndex * 3 + 2] =
            (float) (point.getNormal().get(2));
        // Color buffer
        cb[bufferEntryIndex * 3] =
            (float) (point.getColor().get(0));
        cb[bufferEntryIndex * 3 + 1] =
            (float) (point.getColor().get(1));
        cb[bufferEntryIndex * 3 + 2] =
            (float) (point.getColor().get(2));
        // Texture coordinate buffer
        tcb[bufferEntryIndex * 2] =
            (float) (point.getTexCoord().get(0));
        tcb[bufferEntryIndex * 2 + 1] =
            (float) (point.getTexCoord().get(1));
        // Index buffer
        ib[bufferEntryIndex] = bufferEntryIndex;
      }
      // Create buffers
      buffers.get(buffersIndex).createVertexBuffer(
          bufferSize, vb);
      buffers.get(buffersIndex).createNormalBuffer(
          bufferSize, nb);
      buffers.get(buffersIndex).createColorBuffer(
          bufferSize, cb);
      buffers.get(buffersIndex).createTexCoordBuffer(
          bufferSize, tcb);
      buffers.get(buffersIndex).createIndexBuffer(
          bufferSize, ib);
    }
  }

  /**
   * Adjust the buffers to the required size.
   */
  private void resetBuffers(int numberOfBuffers) {
    while (buffers.size() < numberOfBuffers) {
      buffers.add(new JoglBuffers());
    }
    while (buffers.size() > numberOfBuffers) {
      buffers.remove(0);
    }
    for (JoglBuffers buffer : buffers) {
      buffer.clear();
    }
    Logger
        .getInstance()
        .message(
            "Creating "
                + buffers.size()
                + " set(s) of buffers for point cloud rendering.");
  }

  @Override
  public void draw3D(GL2 gl) {

    if (pointCloud == null) {
      return;
    }

    if (pointCloud.needsUpdateRenderStructures()) {
      createRenderStructures();
    }

    for (int buffersIndex = 0; buffersIndex < buffers
        .size(); buffersIndex++) {
      JoglBuffers joglBuffers = buffers.get(buffersIndex);

      if (joglBuffers.getVertexBuffer() != null
          && joglBuffers.getColorBuffer() != null
          && joglBuffers.getNormalBuffer() != null
          && joglBuffers.getIndexBuffer() != null) {

        gl.glPointSize(2);

        // Call vertex list
        gl.glVertexPointer(
            JoglBuffers.NUMBER_FLOATS_IN_VERTEX,
            GL2.GL_FLOAT, 0, joglBuffers.getVertexBuffer());

        // Color
        gl.glColorPointer(
            JoglBuffers.NUMBER_FLOATS_IN_VERTEX,
            GL2.GL_FLOAT, 0, joglBuffers.getColorBuffer());

        // Texture coordinates
        gl.glColorPointer(
            JoglBuffers.NUMBER_FLOATS_TEXCOORD,
            GL2.GL_FLOAT, 0,
            joglBuffers.getTexCoordBuffer());

        // Normals
        gl.glNormalPointer(GL2.GL_FLOAT, 0,
            joglBuffers.getNormalBuffer());

        // Draw vertices via indices
        gl.glDrawElements(GL2.GL_POINTS,
            joglBuffers.getSize(), GL2.GL_UNSIGNED_INT,
            joglBuffers.getIndexBuffer());
      }
    }

    boolean showNormals = false;
    boolean showPointsAsDiscs = false;

    if (showNormals) {
      renderNormals(gl);
    }

    if (showPointsAsDiscs) {
      if (!hasSubContent()) {
        addSubContent(PointsAsDiscsMeshFactory
            .createPointsAsDiscsNode(pointCloud,
                Material.PALETTE0_COLOR0));
      }
      drawSubContent(gl);
    }
  }

  private void renderNormals(GL2 gl) {
    // Normals
    double scale = 0.005;
    gl.glBegin(GL2.GL_LINES);
    for (int pointIndex = 0; pointIndex < pointCloud
        .getNumberOfPoints(); pointIndex++) {
      IVector3 pos =
          pointCloud.getPoint(pointIndex).getPosition();
      IVector3 end =
          pos.add(pointCloud.getPoint(pointIndex)
              .getNormal().multiply(scale));
      gl.glVertex3fv(pos.floatData(), 0);
      gl.glVertex3fv(end.floatData(), 0);
    }
    gl.glEnd();

  }

  @Override
  public void updateRenderStructures() {
    pointCloud.updateRenderStructures();
  }

  @Override
  public void afterDraw(GL2 gl) {
  }
}

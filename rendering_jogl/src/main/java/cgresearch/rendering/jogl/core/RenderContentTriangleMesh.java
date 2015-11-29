/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.math.*;
import cgresearch.graphics.datastructures.trianglemesh.*;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.rendering.jogl.material.JoglTexture;
import com.jogamp.opengl.GL2GL3;

/**
 * A render node for a triangle mesh.
 * 
 * @author Philipp Jenke
 * 
 */
public class RenderContentTriangleMesh implements IRenderContent {

  /**
   * Reference to the triangle mesh.
   */
  private final ITriangleMesh triangleMesh;

  /**
   * All edges of the triangle mesh
   */
  private List<Edge> edges = null;

  /**
   * Back facing information for each Triangle in respect to the current light source
   */
  private Map<ITriangle, Boolean> backFacing = null;


  /**
   * Temporary data structures containing the vertex information.
   */
  private FloatBuffer vertexBuffer = null;

  /**
   * Temporary data structures containing the texture coordinates
   */
  private FloatBuffer texCoordBuffer = null;

  /**
   * Temporary data structures containing the normal information.
   */
  private FloatBuffer normalBuffer = null;

  /**
   * Temporary data structures containing the color information.
   */
  private FloatBuffer colorBuffer = null;

  /**
   * Temporary data structures containing the color information.
   */
  private FloatBuffer vertexAttributeBuffer = null;

  /**
   * Temporary data structures containing the index information.
   */
  private IntBuffer indexBuffer = null;

  private static final int NUMBER_OF_FLOATS_PER_VERTEX = 3;
  private static final int NUMBER_OF_TEX_COORDS_PER_VERTEX = 2;
  private static final int NUMBER_OF_VERTICES_PER_TRIANGLE = 3;
  private static final int NUMBER_FLOATS_IN_VERTEX = 3;
  private static final int SIZE_FLOAT = 4;
  private static final int NUMBER_FLOATS_TEXCOORD = 2;
  private static final int NUMBER_INDICES_IN_TRIANGLE = 3;
  private static final int SIZE_INT = 4;

  /**
   * Reference to a special node which contains the geometry for the
   * sophisticated mesh.
   */
  private JoglRenderNode sophisticatedMeshNode = null;

  /**
   * Constructor
   */
  public RenderContentTriangleMesh(ITriangleMesh triangleMesh) {
    this.triangleMesh = triangleMesh;
    createRenderStructures();
  }

  /**
   * Returns true if the material uses the wireframe shader.
   * 
   * @return
   */
  private boolean usesWireframe() {

    if (triangleMesh.getMaterial().getNumberOfShaders() > 0) {
      return triangleMesh.getMaterial().getShaderId(0).equals(Material.SHADER_WIREFRAME);
    }
    return false;
  }

  /**
   * Init structures.
   */
  private void createRenderStructures() {
    if (triangleMesh == null) {
      return;
    }

    int numberOfTriangles = triangleMesh.getNumberOfTriangles();
    if (numberOfTriangles == 0) {
      vertexBuffer = null;
      normalBuffer = null;
      colorBuffer = null;
      indexBuffer = null;
      texCoordBuffer = null;
      vertexAttributeBuffer = null;
      return;
    }
    float vb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX * NUMBER_OF_VERTICES_PER_TRIANGLE];
    float nb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX * NUMBER_OF_VERTICES_PER_TRIANGLE];
    float cb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX * NUMBER_OF_VERTICES_PER_TRIANGLE];
    float tcb[] = new float[numberOfTriangles * NUMBER_OF_TEX_COORDS_PER_VERTEX * NUMBER_OF_VERTICES_PER_TRIANGLE];
    float vab[] = null;
    if (usesWireframe()) {
      vab = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX * NUMBER_OF_VERTICES_PER_TRIANGLE];
    }

    createBufferArrays(numberOfTriangles, vb, nb, cb, tcb, vab);
    createBuffers(numberOfTriangles, vb, nb, cb, tcb, vab);
    createIndexBuffer(numberOfTriangles);
  }

  private void createBufferArrays(int numberOfTriangles, float vb[], float nb[], float[] cb, float tcb[], float vab[]) {
    for (int triangleIndex = 0; triangleIndex < numberOfTriangles; triangleIndex++) {

      final ITriangle triangle = triangleMesh.getTriangle(triangleIndex);

      for (int triangleVertexIndex = 0; triangleVertexIndex < NUMBER_OF_VERTICES_PER_TRIANGLE; triangleVertexIndex++) {
        IVertex vertex = TriangleMeshVertexProvider.getVertex(triangleVertexIndex, triangleMesh, triangle);

        // Select normal (per face or per vertex)
        IVector3 normal = (triangleMesh.getMaterial().getRenderMode() == Material.Normals.PER_FACET)
            ? triangle.getNormal() : vertex.getNormal();

        final int triangleBaseIndex = triangleIndex * NUMBER_OF_VERTICES_PER_TRIANGLE * NUMBER_OF_FLOATS_PER_VERTEX;
        final int vertexBaseIndex = triangleBaseIndex + NUMBER_OF_FLOATS_PER_VERTEX * triangleVertexIndex;
        final int texCoordBaseIndex = triangleIndex * NUMBER_OF_VERTICES_PER_TRIANGLE * NUMBER_OF_TEX_COORDS_PER_VERTEX
            + triangleVertexIndex * NUMBER_OF_TEX_COORDS_PER_VERTEX;
        // Set vertex buffer
        vb[vertexBaseIndex] = (float) vertex.getPosition().get(0);
        vb[vertexBaseIndex + 1] = (float) vertex.getPosition().get(1);
        vb[vertexBaseIndex + 2] = (float) vertex.getPosition().get(2);
        // Set normal buffer
        nb[vertexBaseIndex] = (float) normal.get(0);
        nb[vertexBaseIndex + 1] = (float) normal.get(1);
        nb[vertexBaseIndex + 2] = (float) normal.get(2);
        // Color buffer
        cb[vertexBaseIndex] = (float) (triangleMesh.getMaterial().getReflectionDiffuse().get(0));
        cb[vertexBaseIndex + 1] = (float) (triangleMesh.getMaterial().getReflectionDiffuse().get(1));
        cb[vertexBaseIndex + 2] = (float) (triangleMesh.getMaterial().getReflectionDiffuse().get(2));
        // Set texture coordinate buffer
        IVector3 texCoord = triangleMesh.getTextureCoordinate(triangle.getTextureCoordinate(triangleVertexIndex));
        tcb[texCoordBaseIndex] = (float) texCoord.get(0);
        tcb[texCoordBaseIndex + 1] = (float) texCoord.get(1);

        // Vertex attribute buffer
        if (usesWireframe()) {
          vab[vertexBaseIndex] = (triangleVertexIndex == 0) ? 1 : 0;
          vab[vertexBaseIndex + 1] = 1;
          vab[vertexBaseIndex + 2] = (triangleVertexIndex == 2) ? 1 : 0;
        }
      }
    }
  }

  private void createBuffers(int numberOfTriangles, float vb[], float nb[], float[] cb, float tcb[], float vab[]) {
    final int spacePerVertex = NUMBER_FLOATS_IN_VERTEX * SIZE_FLOAT;
    final int spacePerTexCoord = NUMBER_FLOATS_TEXCOORD * SIZE_FLOAT;

    // Vertices
    ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex * numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
    vbb.order(ByteOrder.nativeOrder());
    vertexBuffer = vbb.asFloatBuffer();
    vertexBuffer.put(vb);
    vertexBuffer.position(0);

    // Normals
    ByteBuffer nbb = ByteBuffer.allocateDirect(spacePerVertex * numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
    nbb.order(ByteOrder.nativeOrder());
    normalBuffer = nbb.asFloatBuffer();
    normalBuffer.put(nb);
    normalBuffer.position(0);

    // Color
    ByteBuffer cbb = ByteBuffer.allocateDirect(spacePerVertex * numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
    cbb.order(ByteOrder.nativeOrder());
    colorBuffer = cbb.asFloatBuffer();
    colorBuffer.put(cb);
    colorBuffer.position(0);

    // Texture coordinates
    ByteBuffer tcbb = ByteBuffer.allocateDirect(spacePerTexCoord * numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
    tcbb.order(ByteOrder.nativeOrder());
    texCoordBuffer = tcbb.asFloatBuffer();
    texCoordBuffer.put(tcb);
    texCoordBuffer.position(0);

    // Vertex attribute buffer
    if (usesWireframe()) {
      ByteBuffer vabb = ByteBuffer.allocateDirect(spacePerVertex * numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
      vabb.order(ByteOrder.nativeOrder());
      vertexAttributeBuffer = vabb.asFloatBuffer();
      vertexAttributeBuffer.put(vab);
      vertexAttributeBuffer.position(0);
    }
  }

  private void createIndexBuffer(int numberOfTriangles) {
    int ib[] = new int[numberOfTriangles * NUMBER_INDICES_IN_TRIANGLE];
    for (int i = 0; i < numberOfTriangles; i++) {
      ib[i * NUMBER_INDICES_IN_TRIANGLE] = i * NUMBER_INDICES_IN_TRIANGLE;
      ib[i * NUMBER_INDICES_IN_TRIANGLE + 1] = i * NUMBER_INDICES_IN_TRIANGLE + 1;
      ib[i * NUMBER_INDICES_IN_TRIANGLE + 2] = i * NUMBER_INDICES_IN_TRIANGLE + 2;
    }
    final int spacePerTriangle = SIZE_INT * NUMBER_INDICES_IN_TRIANGLE;
    ByteBuffer ibb = ByteBuffer.allocateDirect(triangleMesh.getNumberOfTriangles() * spacePerTriangle);
    ibb.order(ByteOrder.nativeOrder());
    indexBuffer = ibb.asIntBuffer();
    indexBuffer.put(ib);
    indexBuffer.position(0);
  }

  @Override
  public void draw3D(GL2 gl) {
    if (triangleMesh.needsUpdateRenderStructures()) {
      createRenderStructures();
    }

    if (triangleMesh.getMaterial().hasTexture()) {
      CgTexture texture =
          ResourceManager.getTextureManagerInstance().getResource(triangleMesh.getMaterial().getTextureId());
      JoglTexture.use(texture, gl);
    }

    if (vertexBuffer == null || normalBuffer == null || colorBuffer == null || texCoordBuffer == null
        || indexBuffer == null) {
      // Invalid data structures - call createRenderStructures() first
      return;
    }

    // Call vertex list
    gl.glVertexPointer(NUMBER_FLOATS_IN_VERTEX, GL2.GL_FLOAT, 0, vertexBuffer);

    // Normals
    gl.glNormalPointer(GL2.GL_FLOAT, 0, normalBuffer);

    // Color
    gl.glColorPointer(JoglBuffers.NUMBER_FLOATS_IN_VERTEX, GL2.GL_FLOAT, 0, colorBuffer);

    // Texture coordinates.
    gl.glTexCoordPointer(NUMBER_FLOATS_TEXCOORD, GL2.GL_FLOAT, 0, texCoordBuffer);

    if (usesWireframe()) {
      int vertexAttributeIndex = 1;
      int numberOfFloats = 3;
      int type = GL2.GL_FLOAT;
      boolean normalized = false;
      int stride = 0;
      gl.glEnableVertexAttribArray(vertexAttributeIndex);
      gl.glVertexAttribPointer(vertexAttributeIndex, numberOfFloats, type, normalized, stride, vertexAttributeBuffer);
    }

    // Draw vertices via indices
    gl.glDrawElements(GL2.GL_TRIANGLES, triangleMesh.getNumberOfTriangles() * NUMBER_INDICES_IN_TRIANGLE,
        GL2.GL_UNSIGNED_INT, indexBuffer);

    // Show the sophisticated mesh
    if (triangleMesh.getMaterial().isShowSophisticatesMesh()) {
      if (sophisticatedMeshNode == null) {
        sophisticatedMeshNode = SophisticatedMeshFactory.createSophisticatedMeshNode(triangleMesh);
      }
      sophisticatedMeshNode.draw3D(gl);
    }
  }

  @Override
  public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation) {
    if (triangleMesh.getMaterial().isThrowingShadow() && isInRange(lightSource, transformation)) {
      if (edges == null) {
        createEdgeList();
      }

      IVector3 lightPosition = lightSource.getPosition();
      updateBackFacingInformation(lightPosition, transformation);

      drawShadowPolygons(gl, lightPosition, lightSource.getType() == LightSource.Type.DIRECTIONAL, transformation);
    }
  }

  /**
   * Checks if object is in range of the light's radius
   */
  private boolean isInRange(LightSource lightSource, Transformation transformation) {
    // Light has no range defined, object is in range
    if (lightSource.getLightStrength() == -1) {
      return true;
    }

    // Get all corner points of the bounding box
    BoundingBox bb = triangleMesh.getBoundingBox();
    IVector3[] corners = getBoundingBoxCorners(bb);

    // Check if any of the corner points is in range of the light
    for (int i = 0; i < corners.length; i++) {
      // Get the distance of the transformed point
      IVector3 transformedPoint = transformation.getTransformedVector3(corners[i]);
      IVector3 distance = transformedPoint.subtract(lightSource.getPosition());

      // Is point in range?
      if (distance.getNorm() <= lightSource.getLightStrength()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Draws the shadow polygons
   */
  private void drawShadowPolygons(GL2 gl, IVector3 lightPosition, boolean isDirectional,
                                  Transformation transformation) {
    int lW = isDirectional ? 0 : 1;

    gl.glBegin(GL2GL3.GL_QUADS);
    for (Edge e : edges) {
      ITriangle t1 = e.getTriangle(0);
      ITriangle t2 = e.getTriangle(1);
      if (t2 == null || backFacing.get(t1) != backFacing.get(t2)) {
        // Possible silhouette edge found
        IVertex a, b;

        if (backFacing.get(t1)) {
          a = triangleMesh.getVertex(e.getB());
          b = triangleMesh.getVertex(e.getA());
        } else {
          a = triangleMesh.getVertex(e.getA());
          b = triangleMesh.getVertex(e.getB());
        }

        IVector4 vA = transformation.getTransformedVector4(a.getPosition());
        IVector4 vB = transformation.getTransformedVector4(b.getPosition());
        float[] aInf = {(float) (vA.get(0) * lW - lightPosition.get(0)), (float) (vA.get(1) * lW - lightPosition.get(1)),
                (float) (vA.get(2) * lW - lightPosition.get(2)), 0.0f};
        float[] bInf = {(float) (vB.get(0) * lW - lightPosition.get(0)), (float) (vB.get(1) * lW - lightPosition.get(1)),
                (float) (vB.get(2) * lW - lightPosition.get(2)), 0.0f};

        gl.glVertex4fv(vB.floatData(), 0);
        gl.glVertex4fv(vA.floatData(), 0);
        gl.glVertex4fv(aInf, 0);
        gl.glVertex4fv(bInf, 0);
      }
    }
    gl.glEnd();

    gl.glDepthFunc(GL.GL_NEVER);
    gl.glBegin(GL.GL_TRIANGLES);
    for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
      ITriangle t = triangleMesh.getTriangle(i);
      for (int j = 0; j < 3; j++) {
        int vIndex = t.get(j);
        IVertex v = triangleMesh.getVertex(vIndex);
        IVector4 vPos = transformation.getTransformedVector4(v.getPosition());
        if (backFacing.get(t)) {
          float[] vInf = {(float) (vPos.get(0) * lW - lightPosition.get(0)),
                  (float) (vPos.get(1) * lW - lightPosition.get(1)),
                  (float) (vPos.get(2) * lW - lightPosition.get(2)), 0.0f};
          gl.glVertex4fv(vInf, 0);
        } else {
          gl.glVertex4fv(vPos.floatData(), 0);
        }
      }
    }
    gl.glEnd();
    gl.glDepthFunc(GL.GL_LESS);
  }

  /**
   * Creates edge information for the triangle mesh
   */
  private void createEdgeList() {
    edges = new ArrayList<>();
    for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
      ITriangle t = triangleMesh.getTriangle(i);
      for (int j = 0; j < 3; j++) {
        Edge e = new Edge(t.get(j), t.get((j + 1) % 3));
        if (!edges.contains(e)) {
          e.addTriangle(t);
          edges.add(e);
        } else {
          int index = edges.indexOf(e);
          Edge existingEdge = edges.get(index);
          existingEdge.addTriangle(t);
        }
      }
    }
  }

  /**
   * Updates back-facing information in respect to the given light
   * @param lightPosition Current light's position
   */
  private void updateBackFacingInformation(IVector3 lightPosition, Transformation transformation) {
    boolean init = false;
    if (backFacing == null) {
      backFacing = new HashMap<>();
      init = true;
    }

    for (int i = 0; i < triangleMesh.getNumberOfTriangles(); i++) {
      int vertexCount = 3;
      ITriangle t = triangleMesh.getTriangle(i);
      IVertex[] triangleVertices = new Vertex[vertexCount];
      triangleVertices[0] = triangleMesh.getVertex(t.getA());
      triangleVertices[1] = triangleMesh.getVertex(t.getB());
      triangleVertices[2] = triangleMesh.getVertex(t.getC());

      // Transform vertices
      IVector3[] positions = new IVector3[vertexCount];
      for (int j = 0; j < vertexCount; j++) {
        positions[j] = transformation.getTransformedVector3(triangleVertices[j].getPosition());
      }

      // Get the middle of the triangle
      IVector3 tMiddle = positions[0].add(positions[1]);
      tMiddle = tMiddle.add(positions[2]);
      tMiddle.set(0, tMiddle.get(0) / 3.0);
      tMiddle.set(1, tMiddle.get(1) / 3.0);
      tMiddle.set(2, tMiddle.get(2) / 3.0);

      // Get the direction vector to L
      IVector3 l = lightPosition.subtract(tMiddle);
      // Get Transformed normal
      IVector3 n = getNormal(positions[0], positions[1], positions[2]);

      // Update back-face information
      if (init)
        backFacing.put(t, l.multiply(n) < 0);
      else
        backFacing.replace(t, l.multiply(n) < 0);
    }
  }

  /**
   * Computes the face normal for the given points
   */
  private IVector3 getNormal(IVector3 a, IVector3 b, IVector3 c) {
    IVector3 v1 = b.subtract(a);
    IVector3 v2 = c.subtract(a);
    IVector3 n = v1.cross(v2);
    n.normalize();
    return n;
  }

  /**
   * Returns all corners of the given Bounding box in the following schema:
   * 0 = Left-side lower left
   * 1 = Left-side upper left
   * 2 = Left-side upper right
   * 3 = Left-side lower right
   * 4 = Right-side lower left
   * 5 = Right-side upper left
   * 6 = Right-side upper right
   * 7 = Right-side lower right
   */
  private IVector3[] getBoundingBoxCorners(BoundingBox bb) {
    int pointCount = 8;
    IVector3[] corners = new IVector3[pointCount];
    IVector3 diag = bb.getUpperRight().subtract(bb.getLowerLeft());
    corners[0] = bb.getLowerLeft(); // Left-side lower left
    corners[1] = corners[0].add(VectorMatrixFactory.newIVector3(0, diag.get(1), 0)); // Left-side upper left
    corners[2] = corners[0].add(VectorMatrixFactory.newIVector3(0, diag.get(1), diag.get(2))); // Left-side upper right
    corners[3] = corners[0].add(VectorMatrixFactory.newIVector3(0, 0, diag.get(2))); // Left-side lower right
    corners[4] = corners[0].add(VectorMatrixFactory.newIVector3(diag.get(0), 0, 0)); // Right-side lower left
    corners[5] = corners[0].add(VectorMatrixFactory.newIVector3(diag.get(0), diag.get(1), 0)); // Right-side upper left
    corners[6] = bb.getUpperRight(); // Right-side upper right
    corners[7] = corners[0].add(VectorMatrixFactory.newIVector3(diag.get(0), 0, diag.get(2))); // Right-side lower right

    return corners;
  }

  @Override
  public void updateRenderStructures() {
    triangleMesh.updateRenderStructures();
  }

  @Override
  public void afterDraw(GL2 gl) {
  }
}

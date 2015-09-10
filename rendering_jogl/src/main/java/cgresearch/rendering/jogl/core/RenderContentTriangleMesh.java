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

import com.jogamp.opengl.GL2;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.rendering.jogl.material.JoglTexture;

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
			return triangleMesh.getMaterial().getShaderId(0)
					.equals(Material.SHADER_WIREFRAME);
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
		float vb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX
				* NUMBER_OF_VERTICES_PER_TRIANGLE];
		float nb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX
				* NUMBER_OF_VERTICES_PER_TRIANGLE];
		float cb[] = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX
				* NUMBER_OF_VERTICES_PER_TRIANGLE];
		float tcb[] = new float[numberOfTriangles
				* NUMBER_OF_TEX_COORDS_PER_VERTEX
				* NUMBER_OF_VERTICES_PER_TRIANGLE];
		float vab[] = null;
		if (usesWireframe()) {
			vab = new float[numberOfTriangles * NUMBER_OF_FLOATS_PER_VERTEX
					* NUMBER_OF_VERTICES_PER_TRIANGLE];
		}

		createBufferArrays(numberOfTriangles, vb, nb, cb, tcb, vab);
		createBuffers(numberOfTriangles, vb, nb, cb, tcb, vab);
		createIndexBuffer(numberOfTriangles);
	}

	private void createBufferArrays(int numberOfTriangles, float vb[],
			float nb[], float[] cb, float tcb[], float vab[]) {
		for (int triangleIndex = 0; triangleIndex < numberOfTriangles; triangleIndex++) {

			final Triangle triangle = triangleMesh.getTriangle(triangleIndex);

			for (int triangleVertexIndex = 0; triangleVertexIndex < NUMBER_OF_VERTICES_PER_TRIANGLE; triangleVertexIndex++) {
				final int vertexIndex = triangle.get(triangleVertexIndex);
				Vertex vertex = triangleMesh.getVertex(vertexIndex);

				// Select normal (per face or per vertex)
				IVector3 normal = (triangleMesh.getMaterial().getRenderMode() == Material.Normals.PER_FACET) ? triangle
						.getNormal() : vertex.getNormal();

				final int triangleBaseIndex = triangleIndex
						* NUMBER_OF_VERTICES_PER_TRIANGLE
						* NUMBER_OF_FLOATS_PER_VERTEX;
				final int vertexBaseIndex = triangleBaseIndex
						+ NUMBER_OF_FLOATS_PER_VERTEX * triangleVertexIndex;
				final int texCoordBaseIndex = triangleIndex
						* NUMBER_OF_VERTICES_PER_TRIANGLE
						* NUMBER_OF_TEX_COORDS_PER_VERTEX + triangleVertexIndex
						* NUMBER_OF_TEX_COORDS_PER_VERTEX;
				// Set vertex buffer
				vb[vertexBaseIndex] = (float) vertex.getPosition().get(0);
				vb[vertexBaseIndex + 1] = (float) vertex.getPosition().get(1);
				vb[vertexBaseIndex + 2] = (float) vertex.getPosition().get(2);
				// Set normal buffer
				nb[vertexBaseIndex] = (float) normal.get(0);
				nb[vertexBaseIndex + 1] = (float) normal.get(1);
				nb[vertexBaseIndex + 2] = (float) normal.get(2);
				// Color buffer
				cb[vertexBaseIndex] = (float) (triangleMesh.getMaterial()
						.getReflectionDiffuse().get(0));
				cb[vertexBaseIndex + 1] = (float) (triangleMesh.getMaterial()
						.getReflectionDiffuse().get(1));
				cb[vertexBaseIndex + 2] = (float) (triangleMesh.getMaterial()
						.getReflectionDiffuse().get(2));
				// Set texture coordinate buffer
				IVector3 texCoord = triangleMesh.getTextureCoordinate(triangle
						.getTextureCoordinate(triangleVertexIndex));
				tcb[texCoordBaseIndex] = (float) texCoord.get(0);
				tcb[texCoordBaseIndex + 1] = (float) texCoord.get(1);

				// Vertex attribute buffer
				if (usesWireframe()) {
					vab[vertexBaseIndex] = (triangleVertexIndex == 0) ? 1 : 0;
					vab[vertexBaseIndex + 1] = 1;
					vab[vertexBaseIndex + 2] = (triangleVertexIndex == 2) ? 1
							: 0;
				}
			}
		}
	}

	private void createBuffers(int numberOfTriangles, float vb[], float nb[],
			float[] cb, float tcb[], float vab[]) {
		final int spacePerVertex = NUMBER_FLOATS_IN_VERTEX * SIZE_FLOAT;
		final int spacePerTexCoord = NUMBER_FLOATS_TEXCOORD * SIZE_FLOAT;

		// Vertices
		ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex
				* numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vb);
		vertexBuffer.position(0);

		// Normals
		ByteBuffer nbb = ByteBuffer.allocateDirect(spacePerVertex
				* numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(nb);
		normalBuffer.position(0);

		// Color
		ByteBuffer cbb = ByteBuffer.allocateDirect(spacePerVertex
				* numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(cb);
		colorBuffer.position(0);

		// Texture coordinates
		ByteBuffer tcbb = ByteBuffer.allocateDirect(spacePerTexCoord
				* numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
		tcbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = tcbb.asFloatBuffer();
		texCoordBuffer.put(tcb);
		texCoordBuffer.position(0);

		// Vertex attribute buffer
		if (usesWireframe()) {
			ByteBuffer vabb = ByteBuffer.allocateDirect(spacePerVertex
					* numberOfTriangles * NUMBER_OF_VERTICES_PER_TRIANGLE);
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
			ib[i * NUMBER_INDICES_IN_TRIANGLE + 1] = i
					* NUMBER_INDICES_IN_TRIANGLE + 1;
			ib[i * NUMBER_INDICES_IN_TRIANGLE + 2] = i
					* NUMBER_INDICES_IN_TRIANGLE + 2;
		}
		final int spacePerTriangle = SIZE_INT * NUMBER_INDICES_IN_TRIANGLE;
		ByteBuffer ibb = ByteBuffer.allocateDirect(triangleMesh
				.getNumberOfTriangles() * spacePerTriangle);
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
			CgTexture texture = ResourceManager.getTextureManagerInstance()
					.getResource(triangleMesh.getMaterial().getTextureId());
			JoglTexture.use(texture, gl);
		}

		if (vertexBuffer == null || normalBuffer == null || colorBuffer == null
				|| texCoordBuffer == null || indexBuffer == null) {
			// Invalid data structures - call createRenderStructures() first
			return;
		}

		// Call vertex list
		gl.glVertexPointer(NUMBER_FLOATS_IN_VERTEX, GL2.GL_FLOAT, 0,
				vertexBuffer);

		// Normals
		gl.glNormalPointer(GL2.GL_FLOAT, 0, normalBuffer);

		// Color
		gl.glColorPointer(JoglBuffers.NUMBER_FLOATS_IN_VERTEX, GL2.GL_FLOAT, 0,
				colorBuffer);

		// Texture coordinates.
		gl.glTexCoordPointer(NUMBER_FLOATS_TEXCOORD, GL2.GL_FLOAT, 0,
				texCoordBuffer);

		if (usesWireframe()) {
			int vertexAttributeIndex = 1;
			int numberOfFloats = 3;
			int type = GL2.GL_FLOAT;
			boolean normalized = false;
			int stride = 0;
			gl.glEnableVertexAttribArray(vertexAttributeIndex);
			gl.glVertexAttribPointer(vertexAttributeIndex, numberOfFloats,
					type, normalized, stride, vertexAttributeBuffer);
		}

		// Draw vertices via indices
		gl.glDrawElements(GL2.GL_TRIANGLES, triangleMesh.getNumberOfTriangles()
				* NUMBER_INDICES_IN_TRIANGLE, GL2.GL_UNSIGNED_INT, indexBuffer);

		// Show the sophisticated mesh
		if (triangleMesh.getMaterial().isShowSophisticatesMesh()) {
			if (sophisticatedMeshNode == null) {
				sophisticatedMeshNode = SophisticatedMeshFactory
						.createSophisticatedMeshNode(triangleMesh);
			}
			sophisticatedMeshNode.draw3D(gl);
		}
	}

	@Override
	public void updateRenderStructures() {
		triangleMesh.updateRenderStructures();
	}

	@Override
	public void afterDraw(GL2 gl) {
	}
}

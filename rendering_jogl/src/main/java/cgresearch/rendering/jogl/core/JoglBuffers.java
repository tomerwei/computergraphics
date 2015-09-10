package cgresearch.rendering.jogl.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class JoglBuffers {
	/**
	 * Data structures containing the vertex information.
	 */
	private FloatBuffer vertexBuffer = null;

	/**
	 * Data structures containing the texture coordinate information
	 */
	private FloatBuffer texCoordBuffer = null;

	/**
	 * Data structures containing the normal information.
	 */
	private FloatBuffer normalBuffer = null;

	/**
	 * Data structures containing the colors.
	 */
	private FloatBuffer colorBuffer = null;

	/**
	 * Data structures containing the index information.
	 */
	private IntBuffer indexBuffer = null;

	/**
	 * Number of vertices in the buffer. TODO: Individual values for each
	 * buffer?
	 */
	private int size = -1;

	/**
	 * Constants
	 */
	public static final int SIZE_FLOAT = 4;
	public static final int SIZE_INT = 4;
	public static final int NUMBER_FLOATS_TEXCOORD = 2;
	public static final int NUMBER_FLOATS_IN_VERTEX = 3;
	public static final int SPACE_PER_VERTEX = 3 * SIZE_FLOAT;
	public static final int SPACE_PER_TEXCOORD = 2 * SIZE_FLOAT;

	/**
	 * Constructor
	 */
	public JoglBuffers() {

	}

	/**
	 * Clear all content
	 */
	public void clear() {
		vertexBuffer = null;
		colorBuffer = null;
		normalBuffer = null;
		texCoordBuffer = null;
		indexBuffer = null;
	}

	/**
	 * Create normal buffer.
	 */
	public void createNormalBuffer(int bufferSize, float[] data) {
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(JoglBuffers.SPACE_PER_VERTEX * bufferSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuffer.asFloatBuffer();
		normalBuffer.put(data);
		normalBuffer.position(0);
	}

	/**
	 * Create color buffer.
	 */
	public void createColorBuffer(int bufferSize, float[] data) {
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(JoglBuffers.SPACE_PER_VERTEX * bufferSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuffer.asFloatBuffer();
		colorBuffer.put(data);
		colorBuffer.position(0);
	}

	/**
	 * Create color buffer.
	 */
	public void createTexCoordBuffer(int bufferSize, float[] data) {
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(JoglBuffers.SPACE_PER_TEXCOORD * bufferSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		texCoordBuffer = byteBuffer.asFloatBuffer();
		texCoordBuffer.put(data);
		texCoordBuffer.position(0);
	}

	/**
	 * Create vertex buffer.
	 */
	public void createVertexBuffer(int bufferSize, float[] data) {
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(JoglBuffers.SPACE_PER_VERTEX * bufferSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(data);
		vertexBuffer.position(0);
	}

	/**
	 * Create vertex buffer.
	 */
	public void createIndexBuffer(int bufferSize, int[] data) {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(JoglBuffers.SIZE_INT
				* bufferSize);
		byteBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = byteBuffer.asIntBuffer();
		indexBuffer.put(data);
		indexBuffer.position(0);
	}

	/**
	 * Getter.
	 */
	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	/**
	 * Getter.
	 */
	public FloatBuffer getTexCoordBuffer() {
		return texCoordBuffer;
	}

	/**
	 * Getter.
	 */
	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}

	/**
	 * Getter.
	 */
	public FloatBuffer getColorBuffer() {
		return colorBuffer;
	}

	/**
	 * Getter.
	 */
	public IntBuffer getIndexBuffer() {
		return indexBuffer;
	}

	/**
	 * Setter.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Getter.
	 */
	public int getSize() {
		return size;
	}
}

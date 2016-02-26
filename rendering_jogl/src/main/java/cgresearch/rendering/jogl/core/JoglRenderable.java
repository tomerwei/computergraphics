package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

import cgresearch.core.math.BoundingBox;

/**
 * Interface for all renderables.
 * 
 * @author Philipp Jenke
 *
 */
public interface JoglRenderable {

	/**
	 * Draw the content in this method using OpenGL.
	 */
	public void draw3D(GL2 gl2);

	/**
	 * Return the bounding box of the renderable.
	 */
	public BoundingBox getBoundingBox();
}

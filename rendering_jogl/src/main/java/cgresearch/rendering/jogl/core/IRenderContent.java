/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

/**
 * @author Philipp Jenke
 * 
 */
public interface IRenderContent {

	/**
	 * All Gl drawing functionality is called here
	 */
	public void draw3D(GL2 gl);

	/**
	 * This method if called after all children are drawn.
	 */
	public void afterDraw(GL2 gl);

	/**
	 * Required an update of the cached render structures.
	 */
	public void updateRenderStructures();
}

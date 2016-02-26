/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.math.Vector;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
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
	 * All drawing functionality is called here
	 */
	public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation, Vector[] nearPlaneCorners,
					   boolean cameraPositionChanged);

	/**
	 * This method if called after all children are drawn.
	 */
	public void afterDraw(GL2 gl);

	/**
	 * Required an update of the cached render structures.
	 */
	public void updateRenderStructures();
}

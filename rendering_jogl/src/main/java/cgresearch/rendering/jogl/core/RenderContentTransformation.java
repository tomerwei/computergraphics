package cgresearch.rendering.jogl.core;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.scenegraph.LightSource;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.scenegraph.Transformation;

/**
 * Render content for transformation nodes.
 * 
 * @author Philipp Jenke
 *
 */
public class RenderContentTransformation extends JoglRenderContent {

	/**
	 * Transformation information
	 */
	private final Transformation transformation;

	/**
	 * Constructor.
	 */
	public RenderContentTransformation(Transformation transformation) {
		this.transformation = transformation;
	}

	@Override
	public void draw3D(GL2 gl) {
		draw3D(gl, null, null, null, false);
	}

	@Override
	public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation, IVector3[] nearPlaneCorners, boolean cameraPositionChanged) {
		gl.glPushMatrix();
		// If light source is null, we are rendering shadow volumes.
		// No transformation required.
		if (this.transformation != null && lightSource == null) {
			gl.glMultMatrixd(this.transformation.getTransposedTransformation()
					.data(), 0);
		}
	}

	@Override
	public void updateRenderStructures() {
	}

	@Override
	public void afterDraw(GL2 gl) {
		gl.glPopMatrix();
	}

}

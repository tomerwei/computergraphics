package cgresearch.rendering.jogl.core;

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
		draw3D(gl, null);
	}

	@Override
	public void draw3D(GL2 gl, LightSource lightSource) {
		gl.glPushMatrix();
		if (transformation != null) {
			gl.glMultMatrixd(transformation.getTransposedTransformation()
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

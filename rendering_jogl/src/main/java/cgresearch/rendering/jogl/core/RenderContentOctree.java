package cgresearch.rendering.jogl.core;

import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL2;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.tree.OctreeNode;

/**
 * Render an octree. First dummy implementation - should be improved for further
 * usage.
 * 
 * @author Philipp Jenke
 *
 */
public class RenderContentOctree implements IRenderContent {

	/**
	 * Reference to the octree to be displayed.
	 */
	private final OctreeNode<?> rootNode;

	/**
	 * Constructor.
	 */
	public RenderContentOctree(OctreeNode<?> rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Render the octree.
	 */
	public void draw3D(GL2 gl) {

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glColor3f(0.5f, 0.5f, 0.5f);
		renderOctreeNode(gl, rootNode);
	}

	@Override
	public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation) {

	}

	/**
	 * Render an octree node.
	 */
	private void renderOctreeNode(GL2 gl, OctreeNode<?> node) {
		if (node == null) {
			return;
		}

		IVector3 dx = VectorMatrixFactory.newIVector3(node.getLength(), 0, 0);
		IVector3 dy = VectorMatrixFactory.newIVector3(0, node.getLength(), 0);
		IVector3 dz = VectorMatrixFactory.newIVector3(0, 0, node.getLength());
		if (node.getNumberOfChildren() == 0) {
			if (node.getNumberOfElements() > 0) {
				drawCell(gl, node, dx, dy, dz);
			}
		} else {
			// Call render on children
			for (int i = 0; i < 8; i++) {
				renderOctreeNode(gl, node.getChild(i));
			}
		}
	}

	/**
	 * Draw the ouline of a cell. The cell is only drawn, if it contains
	 * elements.
	 */
	private void drawCell(GL2 gl, OctreeNode<?> node, IVector3 dx, IVector3 dy,
			IVector3 dz) {
		// Render node
		gl.glBegin(GL2.GL_LINES);
		// Bottom
		glVertex(gl, node.getLowerLeft());
		glVertex(gl, node.getLowerLeft().add(dx));
		glVertex(gl, node.getLowerLeft().add(dx));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz));
		glVertex(gl, node.getLowerLeft());
		glVertex(gl, node.getLowerLeft().add(dz));
		glVertex(gl, node.getLowerLeft().add(dz));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz));

		// Top
		glVertex(gl, node.getLowerLeft().add(dy));
		glVertex(gl, node.getLowerLeft().add(dx).add(dy));
		glVertex(gl, node.getLowerLeft().add(dx).add(dy));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz).add(dy));
		glVertex(gl, node.getLowerLeft().add(dy));
		glVertex(gl, node.getLowerLeft().add(dz).add(dy));
		glVertex(gl, node.getLowerLeft().add(dz).add(dy));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz).add(dy));

		// Sides
		glVertex(gl, node.getLowerLeft());
		glVertex(gl, node.getLowerLeft().add(dy));
		glVertex(gl, node.getLowerLeft().add(dx));
		glVertex(gl, node.getLowerLeft().add(dx).add(dy));
		glVertex(gl, node.getLowerLeft().add(dz));
		glVertex(gl, node.getLowerLeft().add(dz).add(dy));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz));
		glVertex(gl, node.getLowerLeft().add(dx).add(dz).add(dy));

		gl.glEnd();
	}

	/**
	 * Call the glVertex method on a IVector3.
	 */
	private void glVertex(GL2 gl, IVector3 v) {
		gl.glVertex3d(v.get(0), v.get(1), v.get(2));
	}

	@Override
	public void updateRenderStructures() {
	}

	@Override
	public void afterDraw(GL2 gl) {

	}

}

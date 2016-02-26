package cgresearch.rendering.jogl.core;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;


/**
 * Parent class for render content in JOGL nodes.
 * 
 * @author Philipp Jenke
 *
 */
public abstract class JoglRenderContent implements IRenderContent {
	/**
	 * Lst of subobjects.
	 */
	private List<JoglRenderNode> subContent = new ArrayList<JoglRenderNode>();

	/**
	 * Draw the sub content nodes.
	 */
	protected void drawSubContent(GL2 gl) {
		// Render child objects
		for (JoglRenderNode renderNode : subContent) {
			renderNode.draw3D(gl);
		}
	}

	/**
	 * Add additional subcontent
	 * 
	 * @param node
	 */
	protected void addSubContent(JoglRenderNode node) {
		subContent.add(node);
	}

	/**
	 * Return true if the content contains subcontent.
	 */
	protected boolean hasSubContent() {
		return subContent.size() > 0;
	}

}

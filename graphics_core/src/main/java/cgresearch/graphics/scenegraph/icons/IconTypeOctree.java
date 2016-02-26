/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Icon type for curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypeOctree extends IIconTypeStrategy {

	/**
	 * Constructor.
	 */
	public IconTypeOctree() {
		image = IconLoader.getIcon("icons/octree.png");
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * edu.haw.cg.scenegraph.icons.IIconTypeStrategy#fits(edu.haw.cg.scenegraph
	 * .CgNode)
	 */
	@Override
	public boolean fits(CgNode node) {
		return node.getContent() instanceof OctreeNode;
	}

}

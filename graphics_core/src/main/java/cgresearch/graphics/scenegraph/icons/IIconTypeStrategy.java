/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import javax.swing.ImageIcon;

import cgresearch.graphics.scenegraph.CgNode;

/**
 * Strategy pattern interface to register icon types.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class IIconTypeStrategy {

	/**
	 * Check if the node fits to the icon type strategy.
	 * 
	 * @param node
	 *            Node to be checked.
	 * @return True if the node fits.
	 */
	public abstract boolean fits(CgNode node);

	/**
	 * Image for the icon.
	 */
	protected ImageIcon image = null;

	/**
	 * Get the image icon.
	 */
	public ImageIcon getIconImage() {
		return image;
	}
}

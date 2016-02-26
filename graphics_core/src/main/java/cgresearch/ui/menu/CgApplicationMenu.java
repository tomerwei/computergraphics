/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui.menu;

import javax.swing.JMenu;

import cgresearch.graphics.scenegraph.CgNode;

/**
 * Parent class for menus to be registered in the CGApplication.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class CgApplicationMenu extends JMenu {

	/**
     * 
     */
	private static final long serialVersionUID = 4024253361927461423L;

	/**
	 * Reference to the scene graph root. This variable is set, when the menu is
	 * registered.
	 */
	protected CgNode rootNode = null;

	

	/**
	 * Constructor, setting the name (caption) of the menu.
	 */
	public CgApplicationMenu(String name) {
		super(name);
	}

	/**
	 * Setter.
	 */
	public void setRootNode(CgNode rootNode) {
		this.rootNode = rootNode;
	}

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui;

import javax.swing.JPanel;
import cgresearch.graphics.scenegraph.CgRootNode;

/**
 * Parent class for all user interfaces.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class IApplicationControllerGui extends JPanel {

	/**
     * 
     */
	private static final long serialVersionUID = 9174664937716235907L;

	/**
	 * Reference to the scene graph root. The reference is only valid after the
	 * registration of this GUI class at the main application.
	 */
	private CgRootNode rootNode;

	/**
	 * Get the name of the controller.
	 */
	public abstract String getName();

	/**
	 * This setter is used by the main application to set the root node. Do not
	 * used it yourself.
	 */
	protected void setRootNode(CgRootNode rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Getter.
	 */
	protected CgRootNode getRootNode() {
		return rootNode;
	}

	/**
	 * This method is called when the GUI is ready (scene graph root available).
	 * Override in subclasses.
	 */
	public void init() {
	}

}

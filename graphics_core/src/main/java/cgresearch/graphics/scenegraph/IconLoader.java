package cgresearch.graphics.scenegraph;

import javax.swing.ImageIcon;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;

/**
 * Helper class to load icons
 * 
 * @author Philipp Jenke
 *
 */
public class IconLoader {

	/**
	 * Try to find icon file on disc and create an ImageIcon with it. Return
	 * null, if icon cannot be found.
	 */
	public static ImageIcon getIcon(String filename) {
		String absolutePath = ResourcesLocator.getInstance().getPathToResource(
				filename);
		if (absolutePath == null) {
			Logger.getInstance().error("Cannot find icon file " + filename);
			return null;
		}
		return new ImageIcon(absolutePath);
	}

}

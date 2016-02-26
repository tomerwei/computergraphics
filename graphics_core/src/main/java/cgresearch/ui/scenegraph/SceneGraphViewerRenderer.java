/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.ui.scenegraph;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import cgresearch.graphics.scenegraph.icons.IIconTypeStrategy;
import cgresearch.graphics.scenegraph.icons.IconTypeAnimation;
import cgresearch.graphics.scenegraph.icons.IconTypeCurve;
import cgresearch.graphics.scenegraph.icons.IconTypeDefaultNode;
import cgresearch.graphics.scenegraph.icons.IconTypeMesh;
import cgresearch.graphics.scenegraph.icons.IconTypeMotionCaptureFrame;
import cgresearch.graphics.scenegraph.icons.IconTypeOctree;
import cgresearch.graphics.scenegraph.icons.IconTypePointCloud;
import cgresearch.graphics.scenegraph.icons.IconTypePrimitive;
import cgresearch.graphics.scenegraph.icons.IconTypeTransformation;

public class SceneGraphViewerRenderer extends JLabel implements
		TreeCellRenderer {

	private static final long serialVersionUID = 1L;
	private ImageIcon icon = new ImageIcon();

	/**
	 * Container for icon-type mapping.
	 */
	List<IIconTypeStrategy> iconTypes = new ArrayList<IIconTypeStrategy>();

	/**
	 * Constructor.
	 */
	public SceneGraphViewerRenderer() {

		// Register icon types
		registerIconType(new IconTypeDefaultNode());
		registerIconType(new IconTypeCurve());
		registerIconType(new IconTypeMesh());
		registerIconType(new IconTypePointCloud());
		registerIconType(new IconTypePrimitive());
		registerIconType(new IconTypeTransformation());
		registerIconType(new IconTypeOctree());
		registerIconType(new IconTypeAnimation());
		registerIconType(new IconTypeMotionCaptureFrame());

		setIcon(icon);
		setIconSafe(iconTypes.get(iconTypes.size() - 1).getIconImage());
		setText("Node");
	}

	private void setIconSafe(ImageIcon iconImage) {
		if (iconImage != null) {
			Image image = iconImage.getImage();
			if (image != null) {
				icon.setImage(image);
			}
		}
	}

	/**
	 * Register an additional icon type.
	 */
	public void registerIconType(IIconTypeStrategy iconType) {
		iconTypes.add(0, iconType);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tr, Object value,
			boolean sel, boolean expanded, boolean leaf, int row, boolean focus) {

		if (!(value instanceof SceneGraphViewerNode)) {
			return this;
		}
		SceneGraphViewerNode sgNode = (SceneGraphViewerNode) value;

		for (IIconTypeStrategy iconTypeStrategy : iconTypes) {
			if (iconTypeStrategy.fits(sgNode.getSceneGraphNode())) {
				setIconSafe(iconTypeStrategy.getIconImage());
				break;
			}
		}

		setText(sgNode.getName());

		if (sgNode.isVisible()) {
			setFont(getFont().deriveFont(Font.PLAIN + Font.BOLD));
		} else {
			setFont(getFont().deriveFont(Font.ITALIC));
		}
		return this;
	}
}

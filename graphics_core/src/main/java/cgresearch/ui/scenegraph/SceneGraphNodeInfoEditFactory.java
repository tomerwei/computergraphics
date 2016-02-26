/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.ui.scenegraph;

import javax.swing.JScrollPane;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;

public final class SceneGraphNodeInfoEditFactory {

	/**
	 * Constructor
	 */
	private SceneGraphNodeInfoEditFactory() {
	}

	/**
	 * Create a info edit widget for a scene graph node.
	 */
	public static InfoEditDefault createInfoEdit(JScrollPane parent,
			SceneGraphViewerNode node) {
		InfoEditDefault nodeInfoEdit = null;

		CgNode cgNode = node.getSceneGraphNode();
		if (cgNode != null && cgNode.getContent() instanceof ITriangleMesh) {
			nodeInfoEdit = new InfoEditTriangleMesh(parent, node);
		} else if (cgNode != null && cgNode.getContent() instanceof IPointCloud) {
			nodeInfoEdit = new InfoEditPointCloud(parent, node);
		} else {
			nodeInfoEdit = new InfoEditDefault(parent, node);
		}

		nodeInfoEdit.setup();
		return nodeInfoEdit;
	}

	/**
	 * Create a info edit widget for a scene graph node.
	 */
	public static InfoEditMaterial createInfoEditMaterial(JScrollPane parent,
			CgNode node) {
		InfoEditMaterial matInfo = new InfoEditMaterial(parent, node);
		return matInfo;
	}
};

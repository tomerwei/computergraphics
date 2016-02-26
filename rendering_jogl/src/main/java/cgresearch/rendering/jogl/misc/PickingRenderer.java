package cgresearch.rendering.jogl.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.picking.Picking;
import cgresearch.graphics.picking.PickingItem;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.Transformation;

public class PickingRenderer implements Observer {

	/**
	 * Render object for the picking item
	 */
	private String selectedPickingItemId = null;

	/**
	 * Transformation of the selected item
	 */
	private Transformation selectedItemTransformation = new Transformation();

	/**
	 * Scene graph node for all picking content.
	 * 
	 * TODO: Should this be visible in the scene graph?
	 */
	private CgNode pickingNode = new CgNode(null, "picking");

	/**
	 * Mapping between item id and corresponding CgNode.
	 */
	private Map<String, CgNode> mapPickItemID2CgNode = new HashMap<String, CgNode>();

	/**
	 * Color constants.
	 */
	private final Vector COLOR_NOT_SELECTED = Material.PALETTE1_COLOR2;
	private final Vector COLOR_SELECTED = Material.PALETTE1_COLOR4;

	/**
	 * Node which shows the coordinate system of the selected node.
	 */
	private final CgNode coordinateSystemNode;

	/**
	 * Constructor
	 * 
	 * @param rootNode
	 */
	public PickingRenderer(CgRootNode rootNode) {
		rootNode.addChild(pickingNode);

		Picking.getInstance().addObserver(this);

		coordinateSystemNode = createCoordinateSystemNode();
		coordinateSystemNode.setVisible(false);
		pickingNode.addChild(coordinateSystemNode);

		for (int i = 0; i < Picking.getInstance().getNumberOfPickingItems(); i++) {
			PickingItem pickingItem = Picking.getInstance().getPickingItem(i);
			Transformation transformation = new Transformation();
			// transformation.addScale(Picking.getInstance().getScaling());
			transformation.addTranslation(pickingItem.getPosition());
			CgNode transformationNode = new CgNode(transformation,
					"transformation");
			ITriangleMesh mesh = TriangleMeshFactory.createSphere(30);
			TriangleMeshTransformation.scale(mesh, 0.05);
			mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			mesh.getMaterial().setReflectionDiffuse(COLOR_NOT_SELECTED);
			CgNode node = new CgNode(mesh, "pickingItemMesh");
			transformationNode.addChild(node);
			pickingNode.addChild(transformationNode);

			mapPickItemID2CgNode.put(pickingItem.getId(), transformationNode);
		}

	}

	private CgNode createCoordinateSystemNode() {

		CgNode coordSystemNode = new CgNode(selectedItemTransformation,
				"picking coordinate system");

		// X
		Arrow arrowX = new Arrow(VectorFactory.createVector3(0, 0, 0),
				VectorFactory.createVector3(0.3, 0, 0));
		arrowX.getMaterial().setReflectionDiffuse(
				VectorFactory.createVector3(1, 0, 0));
		arrowX.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		coordSystemNode.addChild(new CgNode(arrowX, "x"));

		// Y
		Arrow arrowY = new Arrow(VectorFactory.createVector3(0, 0, 0),
				VectorFactory.createVector3(0, 0.3, 0));
		arrowY.getMaterial().setReflectionDiffuse(
				VectorFactory.createVector3(0, 1, 0));
		arrowY.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		coordSystemNode.addChild(new CgNode(arrowY, "y"));

		// Z
		Arrow arrowZ = new Arrow(VectorFactory.createVector3(0, 0, 0),
				VectorFactory.createVector3(0, 0, 0.3));
		arrowZ.getMaterial().setReflectionDiffuse(
				VectorFactory.createVector3(0, 0, 1));
		arrowZ.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		coordSystemNode.addChild(new CgNode(arrowZ, "z"));

		return coordSystemNode;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Picking) {

			coordinateSystemNode.setVisible(false);

			// Remove selection from old node
			CgNode currentlySelectedNode = mapPickItemID2CgNode
					.get(selectedPickingItemId);
			if (currentlySelectedNode != null) {
				currentlySelectedNode.getChildNode(0).getContent()
						.getMaterial().setReflectionDiffuse(COLOR_NOT_SELECTED);
			}

			selectedPickingItemId = Picking.getInstance().getSelectedItemId();
			// Add selection to new node
			CgNode newSelectedNode = mapPickItemID2CgNode.get(Picking
					.getInstance().getSelectedItemId());
			PickingItem item = Picking.getInstance().getSelectedItem();
			if (newSelectedNode != null) {
				newSelectedNode.getChildNode(0).getContent().getMaterial()
						.setReflectionDiffuse(COLOR_SELECTED);
				Transformation transformation = (Transformation) newSelectedNode
						.getContent();
				transformation.reset();
				//transformation.addScale(Picking.getInstance().getScaling());
				transformation.addTranslation(item.getPosition());
				selectedItemTransformation.reset();
				// selectedItemTransformation.addScale(0.3);
				selectedItemTransformation.addTranslation(item.getPosition());
				coordinateSystemNode.setVisible(true);
			}
		}
	}
}

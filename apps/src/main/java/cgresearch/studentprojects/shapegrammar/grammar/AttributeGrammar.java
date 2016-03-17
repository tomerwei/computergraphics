package cgresearch.studentprojects.shapegrammar.grammar;

import cgresearch.studentprojects.shapegrammar.datastructures.rules.GrammarRule;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.Node;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.VirtualFormTree;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.IVirtualForm;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualCuboid;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualPoint;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualShape;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;

/**
 * The Class AttributeGrammar set the Attributes from the virtual shapes in the
 * virtualformtree.
 * 
 * @author Thorben Watzl
 */
public class AttributeGrammar {

	private double maxNegativX;
	private double maxNegativZ;

	/**
	 * Instantiates a new attribute grammar.
	 */
	public AttributeGrammar() {
		maxNegativX = 0;
		maxNegativZ = 0;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param formTree
	 *            the tree with nodes in consenting to be set attribute.
	 */
	public void setAttributes(VirtualFormTree formTree, BuildingSettings buildingSettings) {
		Node<IVirtualForm> node = formTree.getRootNode();
		setPosition(node, buildingSettings);
		setAttributes(node, buildingSettings.getRuleTree());
		reposition(node);
	}

	private void reposition(Node<IVirtualForm> node) {
		IVirtualForm shape = node.getData();
		if (!(shape instanceof VirtualCuboid)) {
			VirtualPoint oldPosition = shape.getPosition();
			VirtualPoint position = new VirtualPoint(oldPosition.getX() - maxNegativX, oldPosition.getY(),
					oldPosition.getZ() - maxNegativZ);
			shape.setPosition(position);
			for (Node<IVirtualForm> childNode : node.getChildren()) {
				reposition(childNode);
			}
		}
	}

	/**
	 * Sets the attributes.
	 *
	 * @param node
	 *            the node in which you want to set attribute
	 */
	private void setAttributes(Node<IVirtualForm> node, RuleTree ruleTree) {
		GrammarRule nodeGrammar = ruleTree.getNodeByName(node.getData().getName()).getData();
		if (nodeGrammar.getTexture() != null) {
			((VirtualShape) node.getData()).setTexture(nodeGrammar.getTexture());
		}
		if (!node.getChildren().isEmpty()) {
			for (Node<IVirtualForm> child : node.getChildren()) {
				if (nodeGrammar.getTexture() != null) {
					((VirtualShape) child.getData()).setTexture(nodeGrammar.getTexture());
				}
				setAttributes(child, ruleTree);
			}
		}
	}

	/**
	 * Sets the position.
	 *
	 * @param node
	 *            the new position
	 */
	private void setPosition(Node<IVirtualForm> node, BuildingSettings buildingSettings) {
		IVirtualForm shape = node.getData();
		if (!(shape instanceof VirtualCuboid)) {
			for (Node<IVirtualForm> childNode : node.getChildren()) {
				IVirtualForm child = childNode.getData();
				VirtualPoint point = shape.getPosition();
				VirtualPoint childPoint = child.getPosition();
				VirtualPoint newChildPoint;
				switch (shape.getPosition().getDirection()) {
				case Top:
					newChildPoint = new VirtualPoint(point.getX() + childPoint.getX(), point.getY(),
							point.getZ() - childPoint.getY(), point.getDirection());
					break;
				case Left:
					newChildPoint = new VirtualPoint(point.getX(), point.getY() + childPoint.getY(),
							point.getZ() + childPoint.getX(), point.getDirection());
					break;
				case Right:
					newChildPoint = new VirtualPoint(point.getX(), point.getY() + childPoint.getY(),
							point.getZ() - childPoint.getX(), point.getDirection());
					break;
				case Front:
					newChildPoint = new VirtualPoint(point.getX() + childPoint.getX(), point.getY() + childPoint.getY(),
							point.getZ(), point.getDirection());
					break;
				case Back:
					newChildPoint = new VirtualPoint(point.getX() - childPoint.getX(), point.getY() + childPoint.getY(),
							point.getZ(), point.getDirection());
					break;
				case Bot:
					newChildPoint = new VirtualPoint(point.getX() + childPoint.getX(), point.getY(),
							point.getZ() + childPoint.getY(), point.getDirection());
					break;
				default:
					newChildPoint = new VirtualPoint(0, 0, 0);
					break;
				}
				child.setPosition(newChildPoint);
				if (newChildPoint.getX() < maxNegativX) {
					maxNegativX = newChildPoint.getX();
				}
				if (newChildPoint.getZ() > maxNegativZ) {
					maxNegativZ = newChildPoint.getZ();
				}
				setPosition(childNode, buildingSettings);
			}
		} else {
			VirtualCuboid cube = (VirtualCuboid) shape;
			VirtualPoint position = cube.getPosition();
			if (position.getDirection() != null) {
				VirtualPoint newCubePosition;
				double width = 0;
				double height = 0;
				double lendth = 0;
				switch (position.getDirection()) {
				case Top:
					newCubePosition = position;
					width = cube.getWidth();
					height = cube.getLendth();
					lendth = cube.getHeight();
					buildingSettings.setHeight(buildingSettings.getHeight() + height);
					break;
				case Left:
					newCubePosition = new VirtualPoint(position.getX() - cube.getLendth(), position.getY(),
							position.getZ(), position.getDirection());
					width = cube.getLendth();
					height = cube.getHeight();
					lendth = cube.getWidth();
					buildingSettings.setWidth(buildingSettings.getWidth() + width);
					break;
				case Right:
					newCubePosition = position;
					width = cube.getLendth();
					height = cube.getHeight();
					lendth = cube.getWidth();
					buildingSettings.setWidth(buildingSettings.getWidth() + width);
					break;
				case Front:
					newCubePosition = new VirtualPoint(position.getX(), position.getY(),
							position.getZ() + cube.getLendth(), position.getDirection());
					width = cube.getWidth();
					height = cube.getHeight();
					lendth = cube.getLendth();
					buildingSettings.setLength(buildingSettings.getLength() + lendth);
					break;
				case Back:
					newCubePosition = new VirtualPoint(position.getX() - cube.getLendth(), position.getY(),
							position.getZ(), position.getDirection());
					width = cube.getWidth();
					height = cube.getHeight();
					lendth = cube.getLendth();
					buildingSettings.setLength(buildingSettings.getLength() + lendth);
					break;
				case Bot:
					newCubePosition = new VirtualPoint(position.getX(), position.getY() - cube.getLendth(),
							position.getZ());
					width = cube.getWidth();
					height = cube.getLendth();
					lendth = cube.getHeight();
					buildingSettings.setHeight(buildingSettings.getHeight() + height);
					break;
				default:
					newCubePosition = new VirtualPoint(0, 0, 0);
					width = cube.getWidth();
					height = cube.getHeight();
					lendth = cube.getLendth();
					break;
				}
				cube.setSize(width, height, lendth);
				cube.setPosition(newCubePosition);
				SplitGrammar splitgrammar = new SplitGrammar();
				buildingSettings.setFormTree(new VirtualFormTree());
				node.setData(splitgrammar.split(cube, buildingSettings).getRootNode().getData());
				for (Node<IVirtualForm> childNode : node.getChildren()) {
					VirtualShape childShape = (VirtualShape) childNode.getData();
					VirtualShape side;
					switch (childShape.getPosition().getDirection()) {
					case Top:
						side = cube.getTop();
						break;
					case Left:
						side = cube.getLeft();
						break;
					case Right:
						side = cube.getRight();
						break;
					case Front:
						side = cube.getFront();
						break;
					case Back:
						side = cube.getBack();
						break;
					case Bot:
						side = cube.getBot();
						break;
					default:
						side = new VirtualShape("error", new VirtualPoint(0, 0, 0), 0, 0);
						break;
					}
					VirtualShape newChildShape = new VirtualShape(childShape.getName(), side.getPosition(),
							side.getWidth(), side.getHeight());
					childNode.setData(newChildShape);
				}
			}
			for (Node<IVirtualForm> childNode : node.getChildren()) {
				setPosition(childNode, buildingSettings);
			}
		}
	}
}

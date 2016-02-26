/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.scenegraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.BoundingBox;

/**
 * Representation of a generic node in the scene graph
 * 
 * @author Philipp Jenke
 * 
 */
public class CgNode extends Observable implements Observer {

	/**
	 * Content of the node.
	 */
	private final ICgNodeContent content;

	/**
	 * Reference to the parent node, only null for the root node.
	 */
	private CgNode parent = null;

	/**
	 * Static ID counter.
	 */
	private static int idCounter = 0;

	/**
	 * Unique id of the node.
	 */
	private final int id;

	/**
	 * Container for the child nodes.
	 */
	private List<CgNode> children = new ArrayList<CgNode>();

	/**
	 * Node name (not necessarily unique)
	 */
	private String name = "Scene Graph Node";

	/**
	 * Visibility of the node.
	 */
	private boolean visible = true;

	/**
	 * Default constructor
	 */
	public CgNode(ICgNodeContent content, String name) {
		this.content = content;
		this.name = name;
		if (content != null) {
			content.addObserver(this);
			content.getMaterial().addObserver(this);
		}
		id = idCounter;
		idCounter++;
		visible = true;
	}

	/**
	 * Default constructor
	 */
	public CgNode(ICgNodeContent content, String name, boolean visible) {
		this(content, name);
		this.visible = visible;
	}

	/**
	 * @param string
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * Add a new child.
	 * 
	 * @param child
	 */
	public void addChild(CgNode child) {
		children.add(child);
		child.parent = this;

		setChanged();
		notifyObservers(CgNodeStateChange.makeAddChild(this, child));
	}

	/**
	 * Initialize the node object.
	 */
	public void init() {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).init();
		}
	}

	/**
	 * Getter
	 */
	public int getNumChildren() {
		return children.size();
	}

	/**
	 * Getter.
	 */
	public CgNode getChildNode(int childIndex) {
		return children.get(childIndex);
	}

	/**
	 * Getter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter
	 */
	public void setVisible(boolean b) {
		if (visible == b) {
			return;
		}

		visible = b;
		for (int childIndex = 0; childIndex < children.size(); childIndex++) {
			children.get(childIndex).setVisible(b);
		}
		setChanged();
		notifyObservers(CgNodeStateChange.makeVisibilityChanged(this));
	}

	/**
	 * Getter
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Toggle the visibility flag.
	 */
	public void toggleVisibiliy() {
		setVisible(!isVisible());
	}

	/**
	 * Getter
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter.
	 */
	public ICgNodeContent getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "CGNode: " + getName();
	}

	/**
	 * Return the bounding box of the node.
	 */
	public BoundingBox getBoundingBox() {
		// Bounding box of the content
		BoundingBox bbox = new BoundingBox();
		if (isVisible() && (content instanceof ICgNodeContent)) {
			bbox = ((ICgNodeContent) content).getBoundingBox();
		}

		// Combine with bounding box of children
		for (CgNode childNode : children) {
			BoundingBox childBBox = childNode.getBoundingBox();
			if (childBBox != null && childBBox.isInitialized()) {
				if (bbox.isInitialized()) {
					bbox.unite(childBBox);
				} else {
					bbox = childBBox;
				}
			}
		}

		// If transform - transform bounding box of children
		if (content instanceof Transformation && bbox.isInitialized() ) {
			Transformation transformation = (Transformation) content;
			bbox.transform(transformation);
		}
		
		return bbox;

	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}

	/**
	 * Delete the node
	 */
	public void deleteNode() {
		if (parent != null) {
			parent.removeChild(this);
		}
	}

	/**
	 * Remove the specified child from the list of children.
	 */
	private void removeChild(CgNode cgNode) {
		// Recursive descent, required for the correct handling in the observers
		for (int i = 0; i < cgNode.getNumChildren(); i++) {
			cgNode.removeChild(cgNode.getChildNode(i));
			i--;
		}

		// Remove the node from the list of children
		children.remove(cgNode);

		// Notify observers
		setChanged();
		notifyObservers(CgNodeStateChange.makeRemoveChild(this, cgNode));
	}

	/**
	 * Remove all children.
	 */
	public void removeAllChildren() {
		while (getNumChildren() > 0) {
			getChildNode(0).deleteNode();
		}

	}

	/**
	 * Return true if the node is a child of the this-node.
	 */
	public boolean hasChild(CgNode node) {
		for (CgNode childNode : children) {
			if (node == childNode) {
				return true;
			}
		}
		return false;
	}
}

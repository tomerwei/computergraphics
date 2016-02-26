/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

/**
 * Representation of the event which occurs when the state of the scene graph
 * changes.
 * 
 * @author Philipp Jenke
 * 
 */
public class CgNodeStateChange {
	/**
	 * Indicate the reason for an update to the observers.
	 */
	public enum ChangeStates {
		ADD_CHILD, REMOVE_CHILD, VISIBILITY_CHANGED, CHANGED
	}

	/**
	 * Can be used to represent a node - not used by all state changes.
	 */
	private CgNode node1;

	/**
	 * Can be used to represent a node - not used by all state changes.
	 */
	private CgNode node2;

	/**
	 * State of this instance.
	 */
	private ChangeStates state;

	/**
	 * Private Constructor. Use Factory methods to create instances.
	 */
	private CgNodeStateChange() {
	}

	/**
	 * Create an instance to represent an add operation.
	 */
	public static CgNodeStateChange makeAddChild(CgNode parent, CgNode newChild) {
		CgNodeStateChange stateChange = new CgNodeStateChange();
		stateChange.node1 = parent;
		stateChange.node2 = newChild;
		stateChange.state = ChangeStates.ADD_CHILD;
		return stateChange;
	}

	/**
	 * Create an instance to represent an add operation.
	 */
	public static CgNodeStateChange makeRemoveChild(CgNode parent,
			CgNode removedChild) {
		CgNodeStateChange stateChange = new CgNodeStateChange();
		stateChange.node1 = parent;
		stateChange.node2 = removedChild;
		stateChange.state = ChangeStates.REMOVE_CHILD;
		return stateChange;
	}

	/**
	 * Create an instance to represent an add operation.
	 */
	public static CgNodeStateChange makeVisibilityChanged(CgNode node) {
		CgNodeStateChange stateChange = new CgNodeStateChange();
		stateChange.node1 = node;
		stateChange.node2 = null;
		stateChange.state = ChangeStates.VISIBILITY_CHANGED;
		return stateChange;
	}

	/**
	 * Create an instance to represent an add operation.
	 */
	public static CgNodeStateChange makeChanged(CgNode node) {
		CgNodeStateChange stateChange = new CgNodeStateChange();
		stateChange.node1 = node;
		stateChange.node2 = null;
		stateChange.state = ChangeStates.CHANGED;
		return stateChange;
	}

	/**
	 * Getter.
	 */
	public CgNode getNode1() {
		return node1;
	}

	/**
	 * Getter.
	 */
	public CgNode getNode2() {
		return node2;
	}

	/**
	 * Getter.
	 */
	public ChangeStates getState() {
		return state;
	}
}

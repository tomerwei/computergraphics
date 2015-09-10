package cgresearch.projects.portalculling;

/**
 * Representation of an edge in a portal scene. An edge has a start- and an
 * end-point. It can either be a wall or a portal.
 * 
 * @author Philipp Jenke
 *
 */
public class PortalEdge {

	/**
	 * Enum to represent the state of the walls
	 */
	public static enum State {
		WALL, PORTAL
	};

	/**
	 * Index of the start node.
	 */
	private final int startNodeIndex;

	/**
	 * Index of the end node.
	 */
	private final int endNodeIndex;

	/**
	 * Wall or portal.
	 */
	private final State state;

	/**
	 * Constructor
	 */
	public PortalEdge(State state, int startNodeIndex, int endNodeIndex) {
		this.startNodeIndex = startNodeIndex;
		this.endNodeIndex = endNodeIndex;
		this.state = state;
	}

	/**
	 * Getter.
	 */
	public int getStartNodeIndex() {
		return startNodeIndex;
	}

	/**
	 * Getter.
	 */
	public int getEndNodeIndex() {
		return endNodeIndex;
	}

	/**
	 * Getter.
	 */
	public boolean isWall() {
		return state == State.WALL;
	}

	/**
	 * Getter.
	 */
	public State getState() {
		return state;
	}

}

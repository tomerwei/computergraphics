package cgresearch.projects.portalculling;

/**
 * Representation of a triangle cell in a portal scene.
 * 
 * @author Philipp Jenke
 *
 */
public class PortalCell {

	/**
	 * A triangle consists of three edges.
	 */
	private int[] edgeIndices = new int[3];

	/**
	 * Constructor
	 */
	public PortalCell(int e0Index, int e1Index, int e2Index) {
		edgeIndices[0] = e0Index;
		edgeIndices[1] = e1Index;
		edgeIndices[2] = e2Index;
	}

	/**
	 * Getter.
	 */
	public int getEdgeIndex(int index) {
		return edgeIndices[index];
	}

	/**
	 * Check if the cell contains the specified edge. Return true of this is the
	 * case, false otherwise.
	 */
	public boolean contains(int edgeIndex) {
		return edgeIndices[0] == edgeIndex || edgeIndices[1] == edgeIndex
				|| edgeIndices[2] == edgeIndex;
	}

}

package cgresearch.projects.portalculling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Ray2D;
import cgresearch.graphics.misc.AnimationTimer;

/**
 * Representation of a portal scene. The cells of the scene are represented as
 * triangles. The y-coordinates are assumed to be 0 (ignored).
 * 
 * @author Philipp Jenke
 *
 */
public class PortalScene2D {

	/**
	 * Edges of the scene.
	 */
	private List<PortalEdge> edges = new ArrayList<PortalEdge>();

	/**
	 * Triangle-cells in the scene.
	 */
	private List<PortalCell> cells = new ArrayList<PortalCell>();

	/**
	 * List of nodes (end points of the edges).
	 */
	private List<Vector> nodes = new ArrayList<Vector>();

	/**
	 * List of already visited cell indices during the pvs computation.
	 */
	private Set<Integer> visitedCells = new HashSet<Integer>();

	/**
	 * Debugging variable to track the number if
	 */
	private int numberOfRecursiveCalls = 0;

	/**
	 * Constructor.
	 */
	public PortalScene2D() {
	}

	/**
	 * Compute the potentially visible (PVS) set for the scene. The PVS is
	 * represented as a set of cell indices.
	 */
	public Set<Integer> computePvs(ViewVolume2D viewVolume) {
		Set<Integer> pvs = new HashSet<Integer>();
		visitedCells.clear();

		numberOfRecursiveCalls = 0;

		pvs = traverse(getStartCellIndex(viewVolume.getOrigin()), viewVolume);
		return pvs;
	}

	/**
	 * Traverse a cell.
	 * 
	 * @param cellIndex
	 *            Index of the current cell.
	 * @param viewVolume
	 *            Viewvolume which entered the cell.
	 * @param recursionDepth
	 *            Debugging tracking of the number of recursion steps.
	 * @return
	 */
	private Set<Integer> traverse(int cellIndex, ViewVolume2D viewVolume) {
		Set<Integer> pvs = new HashSet<Integer>();

		// This statement is used for debugging. The algorithm can now
		// be controlled with the timer time line.
		numberOfRecursiveCalls++;
		if (numberOfRecursiveCalls > AnimationTimer.getInstance().getValue()) {
			return pvs;
		}

		// Invalid cell number (e.g. the view origin is outside of the scene.
		if (cellIndex < 0) {
			return pvs;
		}

		pvs.add(cellIndex);
		visitedCells.add(cellIndex);
		for (int i = 0; i < 3; i++) {
			int edgeIndex = getCell(cellIndex).getEdgeIndex(i);
			PortalEdge edge = getEdge(edgeIndex);
			if (!edge.isWall()) {
				int nextCellIndex = getOppositeCell(cellIndex, edgeIndex);
				if (!visitedCells.contains(nextCellIndex)) {
					ViewVolume2D intersectedVolume = intersect(edge, viewVolume);
					if (intersectedVolume != null
							&& intersectedVolume.getAngle() > 0) {
						pvs.addAll(traverse(nextCellIndex, intersectedVolume));
					}
				}
			}
		}

		return pvs;
	}

	/**
	 * Getter
	 */
	public int getNumberOfEdges() {
		return edges.size();
	}

	/**
	 * Getter.
	 */
	public PortalEdge getEdge(int index) {
		return edges.get(index);
	}

	/**
	 * Getter.
	 */
	public Vector getNode(int index) {
		return nodes.get(index);
	}

	/**
	 * Return the index of cell on the other side of the edge. Return -1 if the
	 * other cell cannot be found.
	 */
	public int getOppositeCell(int cellIndex, int edge) {
		for (int otherCellIndex = 0; otherCellIndex < cells.size(); otherCellIndex++) {
			if (cellIndex != otherCellIndex) {
				if (cells.get(otherCellIndex).contains(edge)) {
					return otherCellIndex;
				}
			}
		}
		return 0;
	}

	/**
	 * Getter.
	 */
	public PortalCell getCell(int index) {
		return cells.get(index);
	}

	/**
	 * Find the cell which contains the origin point and return its index.
	 * Return -1 if the cell cannot be found.
	 */
	public int getStartCellIndex(Vector origin) {
		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			if (cellContainsPoint(cells.get(cellIndex), origin)) {
				return cellIndex;
			}
		}
		return -1;
	}

	/**
	 * Return true if the specified cell contains the given point, false
	 * otherwise.
	 */
	public boolean cellContainsPoint(PortalCell cell, Vector p) {
		List<Vector> cellNodesList = getCellNodes(cell);
		Vector e01 = cellNodesList.get(1).subtract(cellNodesList.get(0));
		Vector e12 = cellNodesList.get(2).subtract(cellNodesList.get(1));
		Vector e20 = cellNodesList.get(0).subtract(cellNodesList.get(2));
		Vector e0p = p.subtract(cellNodesList.get(0));
		Vector e1p = p.subtract(cellNodesList.get(1));
		Vector e2p = p.subtract(cellNodesList.get(2));
		double A = 0.5 * Math.abs(e01.cross(e20).getNorm());
		double A1 = 0.5 * Math.abs(e01.cross(e0p).getNorm());
		double A2 = 0.5 * Math.abs(e12.cross(e1p).getNorm());
		double A3 = 0.5 * Math.abs(e20.cross(e2p).getNorm());
		double lambda1 = A1 / A;
		double lambda2 = A2 / A;
		double lambda3 = A3 / A;

		if (lambda1 >= 0 && lambda2 >= 0 && lambda3 >= 0 && lambda1 <= 1
				&& lambda2 <= 1 && lambda3 <= 1
				&& Math.abs(1 - lambda1 - lambda2 - lambda3) < 1e-5) {
			return true;
		}
		return false;
	}

	/**
	 * Get the three nodes of a cell.
	 */
	public List<Vector> getCellNodes(PortalCell cell) {
		Set<Vector> cellNodes = new HashSet<Vector>();
		for (int i = 0; i < 3; i++) {
			int edgeIndex = cell.getEdgeIndex(i);
			PortalEdge edge = edges.get(edgeIndex);
			cellNodes.add(nodes.get(edge.getStartNodeIndex()));
			cellNodes.add(nodes.get(edge.getEndNodeIndex()));
		}
		if (cellNodes.size() != 3) {
			Logger.getInstance().error("Cell does not have three nodes - invalid.");
		}
		List<Vector> cellNodesList = new ArrayList<Vector>(cellNodes);
		return cellNodesList;
	}

	/**
	 * Compute the resulting viewing volume after for the intersection between
	 * an edge and the given viewing volume.
	 * 
	 * @return viewing volume after the intersection. null if the viewing volume
	 *         is empty.
	 */
	public ViewVolume2D intersect(PortalEdge edge, ViewVolume2D viewVolume) {
		Ray2D rayLeft = new Ray2D(viewVolume.getOrigin(),
				viewVolume.getLeftBoundary());
		Ray2D rayRight = new Ray2D(viewVolume.getOrigin(),
				viewVolume.getRightBoundary());
		Ray2D rayEdge = new Ray2D(nodes.get(edge.getStartNodeIndex()), nodes
				.get(edge.getEndNodeIndex()).subtract(
						nodes.get(edge.getStartNodeIndex())));

		Ray2D.IntersectionResult resultLeft = rayLeft.intersect(rayEdge);
		Ray2D.IntersectionResult resultRight = rayRight.intersect(rayEdge);

		// Special case: back facing
		if (resultLeft != null && resultRight != null && resultLeft.lambda1 < 0
				&& resultRight.lambda1 < 0) {
			return null;
		}

		// Adjust boundary left if parallel
		double lambdaLeft;
		if (resultLeft == null) {
			lambdaLeft = (rayLeft.getDirection().multiply(
					rayEdge.getDirection()) > 0) ? 1 : 0;
		} else if (resultLeft.lambda1 < 0) {
			// Special case: ray left intersects with negative lambda
			lambdaLeft = (rayLeft.getDirection().multiply(
					rayEdge.getDirection()) > 0) ? Double.POSITIVE_INFINITY
					: Double.NEGATIVE_INFINITY;
		} else {
			lambdaLeft = resultLeft.lambda2;
		}

		// Adjust boundary right if parallel
		double lambdaRight;
		if (resultRight == null) {
			lambdaRight = (rayRight.getDirection().multiply(
					rayEdge.getDirection()) > 0) ? 1 : 0;
		} else if (resultRight.lambda1 < 0) {
			// Special case: ray right intersects with negative lambda
			lambdaRight = (rayRight.getDirection().multiply(
					rayEdge.getDirection()) > 0) ? Double.POSITIVE_INFINITY
					: Double.NEGATIVE_INFINITY;
		} else {
			lambdaRight = resultRight.lambda2;
		}

		// Clamp left and right boundary end point position.
		lambdaLeft = Math.min(Math.max(lambdaLeft, 0), 1);
		lambdaRight = Math.min(Math.max(lambdaRight, 0), 1);

		ViewVolume2D result = new ViewVolume2D(viewVolume.getOrigin(), rayEdge
				.eval(lambdaLeft).subtract(viewVolume.getOrigin()), rayEdge
				.eval(lambdaRight).subtract(viewVolume.getOrigin()));
		return result;
	}

	/**
	 * Clear all content.
	 */
	public void clear() {
		edges.clear();
		nodes.clear();
		cells.clear();
	}

	/**
	 * Add a node to the scene.
	 */
	public void addNode(Vector node) {
		nodes.add(node);
	}

	/**
	 * Add an edge to the scene. Return the index of the edge in the edge list.
	 */
	public int addEdge(PortalEdge edge) {
		edges.add(edge);
		return edges.size() - 1;
	}

	/**
	 * Add cell to scene
	 */
	public void addCell(PortalCell cell) {
		cells.add(cell);
	}

	/**
	 * Getter.
	 */
	public int getNumberOfNodes() {
		return nodes.size();
	}

	/**
	 * Getter.
	 */
	public int getNumberOfCells() {
		return cells.size();
	}
}

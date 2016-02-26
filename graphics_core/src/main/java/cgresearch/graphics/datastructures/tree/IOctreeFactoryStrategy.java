package cgresearch.graphics.datastructures.tree;

import cgresearch.core.math.BoundingBox;

/**
 * 
 * A strategy used to create octrees for different data types.
 *
 */
public interface IOctreeFactoryStrategy<T> {

	/**
	 * Numerical accuracy of the spacial data structure.
	 */
	public static final double NUMERICAL_ACCURACY = 1e-7;

	/**
	 * Get the Bounding box of the data.
	 */
	public BoundingBox getBoundingBox();

	/**
	 * Return the number of elements to be added to the octree.
	 */
	public int getNumberOfElements();

	/**
	 * Check if the element with the specified index fits into the given node
	 * (octree cell).
	 */
	public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node);
}

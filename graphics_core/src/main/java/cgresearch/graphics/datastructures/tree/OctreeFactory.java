package cgresearch.graphics.datastructures.tree;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Create a octree for a dataset (defined by a strategy pattern object).
 * 
 * @author Philipp Jenke
 *
 */
public class OctreeFactory<T> {

  /**
   * Strategy object for the actual octree content.
   */
  private final IOctreeFactoryStrategy<T> strategy;

  public OctreeFactory(IOctreeFactoryStrategy<T> strategy) {
    this.strategy = strategy;
  }

  /**
   * @param maxDepth
   *          Maximum depth of the created octree.
   * @param splitSize
   *          An octree node is split, if it contains more than this number of
   *          elements, except the maxDepth is reached.
   * @return Octree root node.
   */
  public OctreeNode<Integer> create(int maxDepth, int splitSize) {
    // Create root node
    BoundingBox bbox = strategy.getBoundingBox();
    IVector3 lowerLeft = bbox.getCenter().subtract(VectorMatrixFactory.newIVector3(bbox.getMaxExtend() / 2.0,
        bbox.getMaxExtend() / 2.0, bbox.getMaxExtend() / 2.0));
    double length = bbox.getMaxExtend();
    // Scale slightly up
    double offset = length * 0.02;
    lowerLeft = lowerLeft.subtract(VectorMatrixFactory.newIVector3(offset, offset, offset));
    length += offset * 2;
    OctreeNode<Integer> rootNode = new OctreeNode<Integer>(lowerLeft, length);

    // Add element, create octree
    for (int elementIndex = 0; elementIndex < strategy.getNumberOfElements(); elementIndex++) {
      if (!insertElement(rootNode, elementIndex, 0, maxDepth, splitSize)) {
        Logger.getInstance().error("Failed to insert element " + elementIndex);
      }
    }

    return rootNode;
  }

  /**
   * Insert an element into the octree, subdivide node if required.
   */
  private boolean insertElement(OctreeNode<Integer> node, int elementIndex, int level, int maxDepth, int splitSize) {
    if (node.getNumberOfChildren() == 0) {
      node.addElement(elementIndex);
      return checkNodeSplitRequired(node, level, maxDepth, splitSize);
    } else {
      boolean success = false;
      for (int childIndex = 0; childIndex < 8; childIndex++) {
        if (strategy.elementFitsInNode(elementIndex, node.getChild(childIndex))) {
          insertElement(node.getChild(childIndex), elementIndex, level + 1, maxDepth, splitSize);
          success = true;
        }
      }
      return success;
    }
  }

  /**
   * Check if the node requires a subdivision (into 8 subnodes).
   */
  private boolean checkNodeSplitRequired(OctreeNode<Integer> node, int level, int maxDepth, int splitSize) {
    boolean success = true;
    if (node.getNumberOfElements() > splitSize && level < maxDepth) {
      success = success && splitNode(node, level, maxDepth, splitSize);
    }
    return success;
  }

  /**
   * Split a node and move element to corresponding children.
   * 
   * @return True, if all elements are successfully sorted into nodes.
   */
  private boolean splitNode(OctreeNode<Integer> node, int level, int maxDepth, int splitSize) {
    node.subdivide();
    boolean allElementsSorted = true;
    for (int index = 0; index < node.getNumberOfElements(); index++) {
      int elementIndex = node.getElement(index);
      for (int childIndex = 0; childIndex < 8; childIndex++) {
        if (strategy.elementFitsInNode(elementIndex, node.getChild(childIndex))) {
          allElementsSorted = allElementsSorted
              && insertElement(node.getChild(childIndex), elementIndex, level + 1, maxDepth, splitSize);
        }
      }
    }
    node.clearElements();

    for (int childIndex = 0; childIndex < 8; childIndex++) {
      allElementsSorted =
          allElementsSorted && checkNodeSplitRequired(node.getChild(childIndex), level + 1, maxDepth, splitSize);
    }

    return allElementsSorted;
  }
}

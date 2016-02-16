package cgresearch.rendering.jogl.misc;

import java.util.ArrayList;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.tree.IOctreeFactoryStrategy;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.scenegraph.CgNode;

public class OctreeFactoryStrategyScene implements IOctreeFactoryStrategy<Integer> {

  /**
   * Elemente des Nodes
   */
  private ArrayList<CgNode> leafNodes = new ArrayList<CgNode>();

  /**
   * Constant fields.
   */
  private final static int X = 0;
  private final static int Y = 1;
  private final static int Z = 2;

  /**
   * Positionen der Objekte
   */
  public static final int INSIDE = 0;
  public static final int OUTSIDE = 1;
  public static final int INTERSECT = 2;

  public OctreeFactoryStrategyScene(ArrayList<CgNode> objects) {
    this.leafNodes = objects;
  }

  /**
   * gibt die BoundingBox der kompletten Szene zurueck
   */
  @Override
  public BoundingBox getBoundingBox() {
    IVector3 tmpLl, tmpUr, ll = null, ur = null;
    for (int i = 0; i < leafNodes.size(); i++) {
      tmpLl = leafNodes.get(i).getBoundingBox().getLowerLeft();
      tmpUr = leafNodes.get(i).getBoundingBox().getUpperRight();
      if (ll == null) {
        ll = tmpLl;
      }
      if (ur == null) {
        ur = tmpUr;
      }
      ll.set(0, tmpLl.get(0) < ll.get(0) ? tmpLl.get(0) : ll.get(0));
      ll.set(1, tmpLl.get(1) < ll.get(1) ? tmpLl.get(1) : ll.get(1));
      ll.set(2, tmpLl.get(2) < ll.get(2) ? tmpLl.get(2) : ll.get(2));

      ur.set(0, tmpUr.get(0) > ur.get(0) ? tmpUr.get(0) : ur.get(0));
      ur.set(1, tmpUr.get(1) > ur.get(1) ? tmpUr.get(1) : ur.get(1));
      ur.set(2, tmpUr.get(2) > ur.get(2) ? tmpUr.get(2) : ur.get(2));
    }
    // System.out.println("BB =" + new BoundingBox(ll, ur));
    if (ll != null && ur != null) {
      return new BoundingBox(ll, ur);
    } else {
      return new BoundingBox();
    }
  }

  @Override
  public int getNumberOfElements() {
    return leafNodes.size();
  }

  @Override
  public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node) {
    BoundingBox cur = leafNodes.get(elementIndex).getBoundingBox();
    IVector3 nodeUpperRight = node.getBoundingBox().getUpperRight();

    if (cur.getUpperRight().get(X) < node.getLowerLeft().get(X)) {
      return false;
    }
    if (cur.getLowerLeft().get(X) > nodeUpperRight.get(X)) {
      return false;
    }
    if (cur.getUpperRight().get(Y) < node.getLowerLeft().get(Y)) {
      return false;
    }
    if (cur.getLowerLeft().get(Y) > nodeUpperRight.get(Y)) {
      return false;
    }
    if (cur.getUpperRight().get(Z) < node.getLowerLeft().get(Z)) {
      return false;
    }
    if (cur.getLowerLeft().get(Z) > nodeUpperRight.get(Z)) {
      return false;
    }

    return true;

  }
}

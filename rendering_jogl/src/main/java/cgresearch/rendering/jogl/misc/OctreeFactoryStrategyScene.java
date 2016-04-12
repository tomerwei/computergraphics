package cgresearch.rendering.jogl.misc;

import java.util.ArrayList;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.tree.IOctreeFactoryStrategy;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.scenegraph.CgNode;

public class OctreeFactoryStrategyScene implements IOctreeFactoryStrategy<Integer> {

  /**
   * Elemente des Nodes
   */
  private ArrayList<CgNode> elements = new ArrayList<CgNode>();

  /**
   * Constant fields.
   */
  private final static int X = 0;
  private final static int Y = 1;
  private final static int Z = 2;
  
  // indices for corner points
  public static final int FBR = 5, FBL = 4, FTR = 6, FTL = 7, NBR = 1, NBL = 0, NTR = 2, NTL = 3;

  public OctreeFactoryStrategyScene(ArrayList<CgNode> objects) {
    this.elements = objects;
  }
  
  /**
   * gibt die BoundingBox der kompletten Szene zurueck
   */
  @Override
  public BoundingBox getBoundingBox() {
    Vector tmpLl, tmpUr, ll = null, ur = null;
    for (int i = 0; i < elements.size(); ++i) {
      tmpLl = new Vector(elements.get(i).getBoundingBox().getLowerLeft());
      tmpUr = new Vector( elements.get(i).getBoundingBox().getUpperRight());
      if (ll == null) {
        ll = tmpLl;
      }
      if (ur == null) {
        ur = tmpUr;
      }
      ll.set(X, tmpLl.get(X) < ll.get(X) ? tmpLl.get(X) : ll.get(X));
      ll.set(Y, tmpLl.get(Y) < ll.get(Y) ? tmpLl.get(Y) : ll.get(Y));
      ll.set(Z, tmpLl.get(Z) < ll.get(Z) ? tmpLl.get(Z) : ll.get(Z));

      ur.set(X, tmpUr.get(X) > ur.get(X) ? tmpUr.get(X) : ur.get(X));
      ur.set(Y, tmpUr.get(Y) > ur.get(Y) ? tmpUr.get(Y) : ur.get(Y));
      ur.set(Z, tmpUr.get(Z) > ur.get(Z) ? tmpUr.get(Z) : ur.get(Z));
    }
    if (ll != null && ur != null) {
      return new BoundingBox(ll, ur);
    } 
    return null;
  }

  @Override
  public int getNumberOfElements() {
    return elements.size();
  }

  @Override
  public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node) {
    BoundingBox cur = elements.get(elementIndex).getBoundingBox();
    Vector nodeUpperRight = new Vector(node.getLowerLeft().get(X) + node.getLength(), node.getLowerLeft().get(Y) + node.getLength(), node.getLowerLeft().get(Z) + node.getLength());

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

package cgresearch.studentprojects.viewfrustum;

import java.util.ArrayList;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.tree.IOctreeFactoryStrategy;
import cgresearch.graphics.datastructures.tree.OctreeNode;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;

public class OctreeFactoryStrategyScene implements IOctreeFactoryStrategy<Integer> {
    
    private ArrayList<CgNode> leafNodes = new ArrayList<CgNode>();
    
    /**
     * Constant fields.
     */
    private final static int X = 0;
    private final static int Y = 1;
    private final static int Z = 2;

    public OctreeFactoryStrategyScene(CgNode rootNode) {
        scenegraphTraversal(rootNode);
        System.out.println("IM KONSTRUKTOR: SIZE = " + leafNodes.size());
    }

    @Override
    public BoundingBox getBoundingBox() {
        IVector3 tmpLl, tmpUr, ll = null, ur = null;
        for(int i = 0; i < leafNodes.size(); i++){
            System.out.println("LEAF NODES BOUNDING BOX = " + leafNodes.get(i).getBoundingBox());
            tmpLl = leafNodes.get(i).getBoundingBox().getLowerLeft();
            tmpUr = leafNodes.get(i).getBoundingBox().getUpperRight();
            if(ll == null){
                ll = tmpLl;
            }
            if(ur == null){
                ur = tmpUr;
            }
            ll.set(0, tmpLl.get(0) < ll.get(0)? tmpLl.get(0) : ll.get(0));
            ll.set(1, tmpLl.get(1) < ll.get(1)? tmpLl.get(1) : ll.get(1));
            ll.set(2, tmpLl.get(2) < ll.get(2)? tmpLl.get(2) : ll.get(2));
            
            ur.set(0, tmpUr.get(0) > ur.get(0)? tmpUr.get(0) : ur.get(0));
            ur.set(1, tmpUr.get(1) > ur.get(1)? tmpUr.get(1) : ur.get(1));
            ur.set(2, tmpUr.get(2) > ur.get(2)? tmpUr.get(2) : ur.get(2));
            
        }
        System.out.println("Bounding Box Scene = " + new BoundingBox(ll, ur));
        return new BoundingBox(ll, ur);
    }

    @Override
    public int getNumberOfElements() {
        return leafNodes.size();
    }
    
    @Override
    public boolean elementFitsInNode(int elementIndex, OctreeNode<Integer> node) {
        BoundingBox cur = leafNodes.get(elementIndex).getContent().getBoundingBox();
        System.out.println("IN ELEMENTFITSINNODE");
        
        for (int i = 0; i < 3; i++) {
            if ((cur.getLowerLeft().get(i) < node.getBoundingBox().getLowerLeft().get(i)
                    - NUMERICAL_ACCURACY)
                    || (cur.getUpperRight().get(i) > node.getBoundingBox().getUpperRight().get(i)
                            + node.getLength() + NUMERICAL_ACCURACY)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * noch nicht genutzt, 
     * traversiert den scenegraph
     */
    private void scenegraphTraversal(CgNode node){
        if(node.getNumChildren() >0){
            for(int i = 0; i<node.getNumChildren(); i++){
                scenegraphTraversal(node.getChildNode(i));
            }
        }
        else{
            this.leafNodes.add(node);
        }
    }


}

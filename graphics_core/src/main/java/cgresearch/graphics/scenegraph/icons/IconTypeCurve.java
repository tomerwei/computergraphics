/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Icon type for curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypeCurve extends IIconTypeStrategy {

    /**
     * Constructor.
     */
    public IconTypeCurve() {
        image = IconLoader.getIcon("icons/curve.png");
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.scenegraph.icons.IIconTypeStrategy#fits(edu.haw.cg.scenegraph
     * .CgNode)
     */
    @Override
    public boolean fits(CgNode node) {
        return node.getContent() instanceof Curve;
    }

}

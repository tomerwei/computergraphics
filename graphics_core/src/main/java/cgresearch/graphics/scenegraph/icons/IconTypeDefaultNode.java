/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Icon type for default nodes.
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypeDefaultNode extends IIconTypeStrategy {

    /**
     * Constructor.
     */
    public IconTypeDefaultNode() {
        image = IconLoader.getIcon("icons/node.png");
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
        return true;
    }

}

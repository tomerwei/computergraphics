/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.datastructures.primitives.IPrimitive;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Icon type primitive
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypePrimitive extends IIconTypeStrategy {

    /**
     * Constructor.
     */
    public IconTypePrimitive() {
        image = IconLoader.getIcon("icons/cube.png");
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
        return node.getContent() instanceof IPrimitive;
    }

}

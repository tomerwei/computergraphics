/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;
import cgresearch.graphics.scenegraph.Transformation;

/**
 * Icon type for transformations.
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypeTransformation extends IIconTypeStrategy {

    /**
     * Constructor.
     */
    public IconTypeTransformation() {
        image = IconLoader.getIcon("icons/transformation.png");
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
        return node.getContent() instanceof Transformation;
    }

}

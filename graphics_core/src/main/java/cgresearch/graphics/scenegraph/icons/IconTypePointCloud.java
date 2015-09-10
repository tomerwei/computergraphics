/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph.icons;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Icon type for point clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class IconTypePointCloud extends IIconTypeStrategy {

    public IconTypePointCloud() {
        image = IconLoader.getIcon("icons/pointcloud.png");
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
        return node.getContent() instanceof IPointCloud;
    }

}

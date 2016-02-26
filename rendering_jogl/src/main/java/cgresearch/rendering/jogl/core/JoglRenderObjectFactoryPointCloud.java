/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for point clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryPointCloud implements
        IRenderObjectsFactory<JoglRenderNode> {

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public JoglRenderNode createRenderObject(JoglRenderNode parentNode,
            CgNode cgNode) {
        if (cgNode.getContent() instanceof PointCloud) {
            PointCloud pointCloud = (PointCloud) cgNode.getContent();
            JoglRenderNode node = new JoglRenderNode(cgNode,
                    new RenderContentPointCloud(pointCloud));

            // Observer pattern between scene graph node and JOGL render nodes
            cgNode.addObserver(node);
            pointCloud.getMaterial().addObserver(node);

            return node;
        } else {
            return null;
        }

    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
     */
    @Override
    public Class<?> getType() {
        return PointCloud.class;
    }

}

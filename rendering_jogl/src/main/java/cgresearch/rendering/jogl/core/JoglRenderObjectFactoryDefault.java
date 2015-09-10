/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Default render object factory.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryDefault implements
        IRenderObjectsFactory<JoglRenderNode> {

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
     * .Object)
     */
    @Override
    public JoglRenderNode createRenderObject(JoglRenderNode parentNode,
            CgNode cgNode) {
        JoglRenderNode node = new JoglRenderNode(null, null);
        return node;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
     */
    @Override
    public Class<?> getType() {
        return null;
    }
}

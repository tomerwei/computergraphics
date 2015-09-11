/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jmonkey;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

import com.jme3.scene.Node;

/**
 * Factory for jMonkey default render object.
 * 
 * @author Philipp Jenke
 * 
 */
public class JMonkeyRenderObjectFactoryDefault implements
        IRenderObjectsFactory<Node> {

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public Node createRenderObject(Node parentNode, CgNode cgNode) {
        Node node = new Node();
        parentNode.attachChild(node);
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

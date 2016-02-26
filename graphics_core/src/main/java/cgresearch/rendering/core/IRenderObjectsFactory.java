/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.core;

import cgresearch.graphics.scenegraph.CgNode;

/**
 * Creates a renderobject for a given scene graph node. Templatre parameter T is
 * the node type of the render system.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IRenderObjectsFactory<T> {

    /**
     * Create a render object for CG scene graph node.
     * 
     * @param parentNode
     *            Parent render node. Required to insert the new node into the
     *            render scene graph.
     * @param content
     *            Scene graph node content, e.g. a triangle mesh.
     */
    public T createRenderObject(T parentNode, CgNode cgNode);

    /**
     * Return the type of the content for which a render node can be created.
     * 
     * @return Content type.
     */
    public Class<?> getType();

}

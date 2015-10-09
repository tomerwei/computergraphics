/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render object factory for the line segments data structure.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryLineSegments implements IRenderObjectsFactory<JoglRenderNode> {

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
   * .Object, java.lang.Object)
   */
  @Override
  public JoglRenderNode createRenderObject(JoglRenderNode parentNode, CgNode cgNode) {
    if (cgNode == null) {
      Logger.getInstance().error("Error creating render object");
    }
    LineSegments lineSegments = (LineSegments) cgNode.getContent();
    JoglRenderNode node = new JoglRenderNode(cgNode, new RenderContentLineSegments(lineSegments));
    cgNode.addObserver(node);
    return node;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
   */
  @Override
  public Class<?> getType() {
    return LineSegments.class;
  }

}

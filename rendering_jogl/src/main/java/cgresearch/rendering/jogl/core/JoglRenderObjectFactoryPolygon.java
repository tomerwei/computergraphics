/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import cgresearch.graphics.datastructures.polygon.Polygon;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

/**
 * Render content factory for a polygon.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderObjectFactoryPolygon implements IRenderObjectsFactory<JoglRenderNode> {

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
   * .Object, edu.haw.cg.scenegraph.CgNode)
   */
  @Override
  public JoglRenderNode createRenderObject(JoglRenderNode parentNode, CgNode cgNode) {

    if (!(cgNode.getContent() instanceof Polygon)) {
      return null;
    }
    return createRenderNode(parentNode, cgNode, (Polygon) cgNode.getContent());
  }

  private JoglRenderNode createRenderNode(JoglRenderNode parentNode, CgNode cgNode, Polygon polygon) {
    if (polygon == null) {
      return null;
    }

    JoglRenderNode renderNodeLines = new JoglRenderNode(cgNode, new RenderContentPolygon(polygon, cgNode));

    return renderNodeLines;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
   */
  @Override
  public Class<?> getType() {
    return Polygon.class;
  }

}

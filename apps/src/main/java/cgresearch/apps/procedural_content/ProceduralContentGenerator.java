package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;

import cgresearch.graphics.scenegraph.CgNode;

/**
 * Shared interface for all content generators for DOM elements.
 * 
 * @author Philipp Jenke
 *
 */
public interface ProceduralContentGenerator {

  /**
   * Returns the element tag, the generator can be used for.
   */
  public String getElementTag();

  /**
   * Parse an element node and create a CGNode containing the represented
   * content.
   * 
   * @param element
   *          DOM element
   * @return CgNode with content.
   */
  public CgNode parseElement(Element element);
}

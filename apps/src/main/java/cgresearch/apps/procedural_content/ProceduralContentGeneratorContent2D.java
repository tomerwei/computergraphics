package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;

public class ProceduralContentGeneratorContent2D implements ProceduralContentGenerator {

  private static final String CONTENT2D = "Content2D";

  @Override
  public CgNode parseElement(Element element) {
    if (element != null && element.getNodeName().equals(CONTENT2D)) {
      CgNode node = new CgNode(null, CONTENT2D);
      return node;
    } else {
      Logger.getInstance().error("Error parsing " + CONTENT2D);
      return null;
    }
  }

  @Override
  public String getElementTag() {
    return CONTENT2D;
  }
}

package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;

public class ProceduralContentGeneratorPanel implements ProceduralContentGenerator {

  private static final String PANEL = "Panel";

  @Override
  public CgNode parseElement(Element element) {
    if (element != null && element.getNodeName().equals(PANEL)) {
      CgNode node = new CgNode(null, PANEL);
      return node;
    } else {
      Logger.getInstance().error("Error parsing " + PANEL);
      return null;
    }
  }

  @Override
  public String getElementTag() {
    return PANEL;
  }
}

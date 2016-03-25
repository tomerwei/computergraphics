package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;

public class ProceduralContentGeneratorStory implements ProceduralContentGenerator {

  private static final String STORY = "Story";

  @Override
  public CgNode parseElement(Element element) {
    if (element != null && element.getNodeName().equals(STORY)) {
      CgNode node = new CgNode(null, STORY);
      return node;
    } else {
      Logger.getInstance().error("Error parsing " + STORY);
      return null;
    }
  }

  @Override
  public String getElementTag() {
    return STORY;
  }
}

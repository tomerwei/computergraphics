package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;

public class ProceduralContentGeneratorContent3D implements ProceduralContentGenerator {

  private static final String CONTENT3D = "Content3D";

  @Override
  public CgNode parseElement(Element element) {
    if (element != null && element.getNodeName().equals(CONTENT3D)) {
      CgNode node = new CgNode(null, CONTENT3D);
      return node;
    } else {
      Logger.getInstance().error("Error parsing " + CONTENT3D);
      return null;
    }
  }

  @Override
  public String getElementTag() {
    return CONTENT3D;
  }
}

package cgresearch.apps.procedural_content;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.shapegrammar.generator.BuildingGenerator;

/**
 * Content generator for a building.
 * 
 * @author Philipp Jenke
 */
public class ProceduralContentGeneratorBuilding implements ProceduralContentGenerator {

  private static final String ATTR_TYPE = "type";
  private static final String ATTR_WIDTH = "width";
  private static final String ATTR_HEIGHT = "height";
  private static final String ATTR_LENGTH = "length";
  private static final String ATTR_POSITIONX = "positionX";
  private static final String ATTR_POSITIONY = "positionY";
  private static final String BUILDING = "Building";

  @Override
  public String getElementTag() {
    return BUILDING;
  }

  @Override
  public CgNode parseElement(Element element) {
    if (element != null && element.getNodeName().equals(BUILDING)) {
      try {
        NamedNodeMap attributeMap = element.getAttributes();
        Node typeNode = attributeMap.getNamedItem(ATTR_TYPE);
        Node widthNode = attributeMap.getNamedItem(ATTR_WIDTH);
        Node heightNode = attributeMap.getNamedItem(ATTR_HEIGHT);
        Node lengthNode = attributeMap.getNamedItem(ATTR_LENGTH);
        Node positionXNode = attributeMap.getNamedItem(ATTR_POSITIONX);
        Node positionYNode = attributeMap.getNamedItem(ATTR_POSITIONY);

        String widthString = widthNode.getNodeValue();
        String heightString = heightNode.getNodeValue();
        String lengthString = lengthNode.getNodeValue();
        String positionXString = positionXNode.getNodeValue();
        String positionYString = positionYNode.getNodeValue();
        double width = Double.parseDouble(widthString);
        double height = Double.parseDouble(heightString);
        double length = Double.parseDouble(lengthString);
        double positionX = Double.parseDouble(positionXString);
        double positionY = Double.parseDouble(positionYString);
        String type = typeNode.getNodeValue();

        Logger.getInstance().message("Generating building " + width + " x " + height);

        BuildingGenerator buildingGenerator = new BuildingGenerator();
        CgNode buildingNode =
            buildingGenerator.generateBuildingCgNode(type, width, height, length, positionX, positionY);
        return buildingNode;
      } catch (Exception e) {
        Logger.getInstance().error("Failed to generate " + BUILDING);
      }
    } else {
      Logger.getInstance().error("Error parsing " + BUILDING);
    }
    return null;
  }

}

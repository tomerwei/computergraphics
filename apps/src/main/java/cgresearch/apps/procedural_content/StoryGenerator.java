package cgresearch.apps.procedural_content;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.shapegrammar.generator.BuildingGenerator;

/**
 * Generates a story from a DSL description (as XML)
 * 
 * @author Philipp Jenke
 *
 */
public class StoryGenerator {

	private static final String STORY = "Story";
	private static final String PANEL = "Panel";
	private static final String CONTENT2D = "Content2D";
	private static final String CONTENT3D = "Content3D";
	private static final String BUILDING = "Building";
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_WIDTH = "width";
	private static final String ATTR_HEIGHT = "height";
	private static final String ATTR_LENGTH = "length";
	private static final String ATTR_POSITIONX = "positionX";
	private static final String ATTR_POSITIONY = "positionY";

	public StoryGenerator() {

	}

	/**
	 * Generate a story from an XML description.
	 */
	public CgNode generate(String xmlFilename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(ResourcesLocator.getInstance().getPathToResource(xmlFilename)));
			CgNode node = parseStory(document.getDocumentElement());
			return node;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Logger.getInstance().exception("Failed to parse XML " + xmlFilename, e);
		}
		return null;
	}

	/**
	 * Parse DOM for Story element.
	 */
	private CgNode parseStory(Element element) {
		CgNode node = new CgNode(null, "Story");
		if (element != null && element.getNodeName().equals(STORY)) {
			// A story consists of a List of panels
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node childNode = element.getChildNodes().item(i);
				if (childNode instanceof Element) {
					Element childElement = (Element) childNode;
					switch (childElement.getNodeName()) {
					case PANEL:
						CgNode panelNode = parsePanel(childElement);
						if (panelNode != null) {
							node.addChild(panelNode);
						}
						break;
					default:
						Logger.getInstance().message("Invalid stroy child: " + childElement.getNodeName());
					}
				}
			}
		} else {
			Logger.getInstance().error("Error parsing story.");
		}
		return node;
	}

	/**
	 * Parse DOM for Panel element.
	 */
	private CgNode parsePanel(Element element) {
		CgNode node = new CgNode(null, "Panel");
		if (element != null && element.getNodeName().equals(PANEL)) {
			// A story consists of a List of panels
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node childNode = element.getChildNodes().item(i);
				if (childNode instanceof Element) {
					Element childElement = (Element) childNode;
					switch (childElement.getNodeName()) {
					case CONTENT2D:
						CgNode content2DNode = parseContent2D(childElement);
						if (content2DNode != null) {
							node.addChild(content2DNode);
						}
						break;
					case CONTENT3D:
						CgNode content3DNode = parseContent3D(childElement);
						if (content3DNode != null) {
							node.addChild(content3DNode);
						}
						break;
					default:
						Logger.getInstance().message("Invalid stroy child: " + childElement.getNodeName());
					}
				}
			}
		} else {
			Logger.getInstance().error("Error parsing panel.");
		}
		return node;
	}

	/**
	 * Parse DOM for Content2D element.
	 */
	private CgNode parseContent2D(Element element) {
		CgNode node = new CgNode(null, "Content2D");
		if (element != null && element.getNodeName().equals(CONTENT2D)) {
			// A story consists of a List of panels
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node childNode = element.getChildNodes().item(i);
				if (childNode instanceof Element) {
					Element childElement = (Element) childNode;
					switch (childElement.getNodeName()) {
					default:
						Logger.getInstance().message("Invalid Content2D child: " + childElement.getNodeName());
					}
				}
			}
		} else {
			Logger.getInstance().error("Error parsing story.");
		}
		return node;
	}

	/**
	 * Parse DOM for Content3D element.
	 */
	private CgNode parseContent3D(Element element) {
		CgNode node = new CgNode(null, "Content3D");
		if (element != null && element.getNodeName().equals(CONTENT3D)) {
			// A story consists of a List of panels
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				Node childNode = element.getChildNodes().item(i);
				if (childNode instanceof Element) {
					Element childElement = (Element) childNode;
					switch (childElement.getNodeName()) {
					case BUILDING:
						CgNode buildingNode = parseBuilding(childElement);
						if (buildingNode != null) {
							node.addChild(buildingNode);
						}
						break;
					default:
						Logger.getInstance().message("Invalid Building child: " + childElement.getNodeName());
					}
				}
			}
		} else {
			Logger.getInstance().error("Error parsing story.");
		}
		return node;
	}

	/**
	 * Parse DOM for Building element.
	 */
	private CgNode parseBuilding(Element element) {
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
				CgNode buildingNode = buildingGenerator.generateBuildingCgNode(type, width, height, length, positionX,
						positionY);
				return buildingNode;
			} catch (Exception e) {
				Logger.getInstance().error("Failed to generate building.");
			}
		} else {
			Logger.getInstance().error("Error parsing story.");
		}
		return null;

	}

}

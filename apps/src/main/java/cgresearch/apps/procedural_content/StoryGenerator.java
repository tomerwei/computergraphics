package cgresearch.apps.procedural_content;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Generates a story from a DSL description (as XML)
 * 
 * @author Philipp Jenke
 *
 */
public class StoryGenerator {
  /**
   * Mapping between elements and generators
   */
  private Map<String, ProceduralContentGenerator> contentGenerators = new HashMap<String, ProceduralContentGenerator>();

  public StoryGenerator() {
    ProceduralContentGeneratorBuilding buldingGenerator = new ProceduralContentGeneratorBuilding();
    contentGenerators.put(buldingGenerator.getElementTag(), buldingGenerator);
    ProceduralContentGeneratorMesh meshGenerator = new ProceduralContentGeneratorMesh();
    contentGenerators.put(meshGenerator.getElementTag(), meshGenerator);
    ProceduralContentGeneratorContent2D content2DGenerator = new ProceduralContentGeneratorContent2D();
    contentGenerators.put(content2DGenerator.getElementTag(), content2DGenerator);
    ProceduralContentGeneratorContent3D content3DGenerator = new ProceduralContentGeneratorContent3D();
    contentGenerators.put(content3DGenerator.getElementTag(), content3DGenerator);
    ProceduralContentGeneratorPanel panelGenerator = new ProceduralContentGeneratorPanel();
    contentGenerators.put(panelGenerator.getElementTag(), panelGenerator);
    ProceduralContentGeneratorStory storyGenerator = new ProceduralContentGeneratorStory();
    contentGenerators.put(storyGenerator.getElementTag(), storyGenerator);
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
      CgNode node = parseElement(document.getDocumentElement());
      return node;
    } catch (ParserConfigurationException | SAXException | IOException e) {
      Logger.getInstance().exception("Failed to parse XML " + xmlFilename, e);
    }
    return null;
  }

  /**
   * Recursive parsing of an element, create CgNode for each element if
   * possible.
   */
  private CgNode parseElement(Element element) {
    ProceduralContentGenerator generator = contentGenerators.get(element.getNodeName());
    CgNode node = null;
    if (generator != null) {
      node = generator.parseElement(element);
      if (node == null) {
        return null;
      }
    }

    // Child-elements
    for (int i = 0; i < element.getChildNodes().getLength(); i++) {
      Node childNode = element.getChildNodes().item(i);
      if (childNode instanceof Element) {
        Element childElement = (Element) childNode;
        CgNode cgChildNode = parseElement(childElement);
        if (cgChildNode != null) {
          node.addChild(cgChildNode);
        }
      }
    }
    return node;
  }
}

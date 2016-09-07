package smarthomevis.groundplan.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;
import smarthomevis.groundplan.data.GPLine.LineType;

public class GPConfigXMLReader
{
	
	private GPConfig config = null;
	
	public GPConfigXMLReader()
	{
		this.config = new GPConfig();
	}
	
	public GPConfig readConfig(String URI)
	{
	Document xmlDoc = readXMLDocument(URI);
	
	NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();
	
	for (int i = 0; i < nodeList.getLength(); i++)
		{
		Node node = nodeList.item(i);
		
		// Kind-Knoten einer LayerNode des XMLs behandeln
		if (node.getNodeName().equals("layer") && node.hasAttributes())
			{
			// Namen und Typ fuer diese Ebene aus dem XML auslesen
			String layerName = node.getAttributes().getNamedItem("name")
				.getNodeValue();
			String lineTypeString = node.getAttributes().getNamedItem("type")
				.getNodeValue();
				
			if (layerName != null && lineTypeString != null)
				this.config.addLayer(layerName,
					defineLineTypeFromString(lineTypeString));
			}
		// Handelt es sich um eine config- statt um eine LayerNode, diese
		// Auslesen und dem GPDataType uebergeben
		else if (node.getNodeName().equals("config")
			&& node.getNodeType() == Node.ELEMENT_NODE)
			{
			extractConfig(node);
			}
		}
		
	return this.config;
	}
	
	private void extractConfig(Node node)
	{
	Element element = (Element) node;
	addValueToConfig(element, GPConfig.WALL_TOP_HEIGHT);
	addValueToConfig(element, GPConfig.WINDOW_BOTTOM_HEIGHT);
	addValueToConfig(element, GPConfig.WINDOW_TOP_HEIGHT);
	addValueToConfig(element, GPConfig.DOOR_TOP_HEIGHT);
	addValueToConfig(element, GPConfig.GROUNDPLAN_SCALING_FACTOR);
	addValueToConfig(element, GPConfig.ANGLE_TOLERANCE);
	addValueToConfig(element, GPConfig.POINT_TOLERANCE);
	addValueToConfig(element, GPConfig.DISTANCE_INTERVAL);
	addValueToConfig(element, GPConfig.LOWER_WALLTHICKNESS_LIMIT);
	addValueToConfig(element, GPConfig.UPPER_WALLTHICKNESS_LIMIT);
	addValueToConfig(element, GPConfig.WALL_COUNT_THRESHOLD);
	}
	
	private void addValueToConfig(Element element, String keyString)
	{
	this.config.addConfigValue(keyString, Double.valueOf(
		element.getElementsByTagName(keyString).item(0).getTextContent()));
	}
	
	private LineType defineLineTypeFromString(String lineTypeString)
	{
	if (lineTypeString.equalsIgnoreCase("WALL"))
		return LineType.WALL;
	if (lineTypeString.equalsIgnoreCase("WINDOW"))
		return LineType.WINDOW;
	if (lineTypeString.equalsIgnoreCase("DOOR"))
		return LineType.DOOR;
		
	return LineType.WALL;
	}
	
	/**
	 * Hilfsmethode fuer das Einlesen des XMLDokuments
	 * 
	 * @param filename
	 *          der Speicherort innerhalt des Asset-Ordners
	 * @return ein Document Object, das saemtliche XML-Knoten zur Definition der
	 *         (relevanten) Strukturen im DXF-Dokument enthaelt
	 */
	private Document readXMLDocument(String filename)
	{
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	Document xmlDoc = null;
	
	try
		{
		DocumentBuilder builder = factory.newDocumentBuilder();
		String absoluteFilename = ResourcesLocator.getInstance()
			.getPathToResource(filename);
		FileInputStream in = null;
		
		in = new FileInputStream(new File(absoluteFilename));
		xmlDoc = builder.parse(in);
		in.close();
		
		}
	catch (ParserConfigurationException e)
		{
		System.err.println("Could not create a DocumentBuilder");
		e.printStackTrace();
		}
	catch (FileNotFoundException e)
		{
		System.err.println("Could not load xml file");
		e.printStackTrace();
		}
	catch (IOException e)
		{
		System.err.println("Failed to parse xmlFile or close InputStream");
		e.printStackTrace();
		}
	catch (SAXException e)
		{
		System.err.println("Error occured while parsing xmlFile");
		e.printStackTrace();
		}
		
	// NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();
	
	return xmlDoc;
	}
	
}

package smarthomevis.groundplan.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.kabeja.dxf.DXFDocument;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;

/**
 * Class to convert DXF elements via XML structure definitions to a GPDataType
 * object and vice versa
 * 
 * @author Leonard.Opitz
 *
 */
public class Converter
	{

	private GPDataType resultData = null;
	private DXFDocument dxfDoc = null;

	public Converter()
		{
		this.resultData = new GPDataType();
		}

	/*
	 * 
	 * 
	 * 
	 * File Reading
	 */

	public GPDataType importData(String dxfDocURI, String xmlDocURI)
		{
		this.dxfDoc = readDXFDocument(dxfDocURI);
		Document xmlDoc = readXMLDocument(xmlDocURI);

		if (dxfDoc != null && xmlDoc != null)
			{
			extractWalls(dxfDoc, xmlDoc);
			}
		else
			System.err.println("Failed to import data. DXF or XML is missing!");

		return this.resultData;
		}

	/*
	 * 
	 */
	private void extractWalls(DXFDocument dxf, Document xmlDoc)
		{
		NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++)
			{
			Node layerNode = nodeList.item(i);

			String layerName = "";

			// handle children of each layer
			if (layerNode.getNodeName().equals("layer")
				&& layerNode.hasAttributes())
				{

				layerName = layerNode.getAttributes().getNamedItem("name")
					.getNodeValue();

				if (!layerName.isEmpty() && layerNode.hasChildNodes())
					System.out.println(
						"Extracting wall nodes of layer '" + layerName + "'");
				handleWallsOfLayer(layerName, layerNode);
				}
			}
		}

	/**
	 * iterate through the wall elements of a single layer node
	 * 
	 * @param layerName
	 *          the name of the current layer
	 * @param layerNode
	 *          the parent layer node containing wall nodes
	 */
	private void handleWallsOfLayer(String layerName, Node layerNode)
		{
		NodeList wallList = layerNode.getChildNodes();
		for (int i = 0; i < wallList.getLength(); i++)
			{
			Node wallNode = wallList.item(i);
			String wallId = "";

			if (wallNode.getNodeName().equals("wall")
				&& wallNode.hasAttributes())
				{
				wallId = wallNode.getAttributes().getNamedItem("id")
					.getNodeValue();
				}

			if (!wallId.isEmpty() && wallNode.hasChildNodes())
				{
				System.out.println(
					"Extracting surface nodes of wall '" + wallId + "'");
				handleSurfacesOfWall(layerName, wallId, wallNode);
				}
			}
		}

	private void handleSurfacesOfWall(String layerName, String wallId,
		Node wallNode)
		{
			
		}

	private DXFDocument readDXFDocument(String filename)
		{
		// get the File
		FileInputStream in = null;
		try
			{
			String absoluteFilename = ResourcesLocator.getInstance()
				.getPathToResource(filename);
			in = new FileInputStream(new File(absoluteFilename));
			}
		catch (FileNotFoundException e1)
			{
			e1.printStackTrace();
			}

		// get requested DXF Content
		Parser parser = ParserBuilder.createDefaultParser();
		DXFDocument doc = null;
		try
			{

			// parse
			parser.parse(in, DXFParser.DEFAULT_ENCODING);

			// get the document and the layer
			doc = parser.getDocument();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}

		return doc;
		}

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
		// FIXME replace syserr with exception logging
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

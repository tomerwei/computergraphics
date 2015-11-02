package smarthomevis.groundplan.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;

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
	private static int lineIndex;

	public Converter()
		{
		this.resultData = new GPDataType();
		this.lineIndex = 0;
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
				else
					System.out.println(
						"LayerName is missing or has no childs, please check xml");

				DXFLayer dxfLayer = dxf.getDXFLayer(layerName);

				if (dxfLayer != null)
					{
					Map<String, DXFLine> dxfEntityMap = extractDXFLinesFromLayer(
						dxfLayer);
					System.out.println(
						"DXFEntityMap has size " + dxfEntityMap.size());
					handleWallsOfLayer(dxfEntityMap, layerNode);
					}
				else
					System.out
						.println("Failed loading dxfLayer '" + layerName + "'");
				}
			}
		}

	private Map<String, DXFLine> extractDXFLinesFromLayer(DXFLayer dxfLayer)
		{
		Map<String, DXFLine> lineMap = new HashMap<>();

		@SuppressWarnings("unchecked")
		Iterator<DXFEntity> it = dxfLayer
			.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE).iterator();

		int i = 0;
		for (; it.hasNext();)
			{
			DXFEntity e = it.next();

			lineMap.put("Line" + getLineIndex(), (DXFLine) e);

			}
		return lineMap;
		}

	/**
	 * iterate through the wall elements of a single layer node
	 * 
	 * @param layerName
	 *          the name of the current layer
	 * @param layerNode
	 *          the parent layer node containing wall nodes
	 */
	private void handleWallsOfLayer(Map<String, DXFLine> dxfEntityMap,
		Node layerNode)
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
				handleSurfacesOfWall(dxfEntityMap, wallId, wallNode);
				}

			}
		}

	private void handleSurfacesOfWall(Map<String, DXFLine> dxfEntityMap,
		String wallId, Node wallNode)
		{
		NodeList surfaceList = wallNode.getChildNodes();
		List<String> surfaceIdsOfWall = new ArrayList<>();

		for (int i = 0; i < surfaceList.getLength(); i++)
			{
			Node surfaceNode = surfaceList.item(i);

			if (surfaceNode.getNodeName().equals("surface")
				&& surfaceNode.hasAttributes())
				{
				String surfaceId = surfaceNode.getAttributes()
					.getNamedItem("id").getNodeValue();

				if (!surfaceId.isEmpty())
					{
					System.out
						.println("Wall " + wallId + " | Surface " + surfaceId);

					DXFLine surfaceEntity = dxfEntityMap.get(surfaceId);

					if (surfaceEntity instanceof DXFLine)
						{
						surfaceIdsOfWall.add(surfaceId);
						Point startPoint = surfaceEntity.getStartPoint();
						Point endPoint = surfaceEntity.getEndPoint();

						this.resultData.addSurface(surfaceId, new GPSurface(
							surfaceId,
							VectorMatrixFactory.newIVector3(startPoint.getX(),
								startPoint.getY(), startPoint.getZ()),
							VectorMatrixFactory.newIVector3(endPoint.getX(),
								endPoint.getY(), endPoint.getZ())));
						}
					}
				}
			}

		this.resultData.addWall(wallId, surfaceIdsOfWall);
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

	private String getLineIndex()
		{
		if (lineIndex < 10)
			return "00" + lineIndex++;
		if (lineIndex < 100)
			return "0" + lineIndex++;

		return "" + lineIndex++;
		}

	}

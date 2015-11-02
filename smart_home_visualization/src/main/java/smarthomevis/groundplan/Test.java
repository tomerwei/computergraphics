package smarthomevis.groundplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.kabeja.dxf.DXFBlock;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

public class Test extends CgApplication
	{

	private DXFDocument readDocument(String filename)
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

	/*
	 * ########### First Test with Lines #############
	 */
	private void testOne()
		{
		// DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		DXFDocument doc = readDocument("dxf/Grundriss_Haus_02.dxf");
		getCgRootNode().addChild(renderLayer(doc, "Schnittkanten"));
		getCgRootNode().addChild(renderLayer(doc, "Ansichtskanten"));

		}

	private void testTwo()
		{
		// DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		DXFDocument doc = readDocument("dxf/4H-HORA Projekt1.dxf");
		getCgRootNode().addChild(renderLayer(doc, "VOM$SYSTEM$VORDEFINIERT"));
		// getCgRootNode().addChild(renderLayer(doc,
		// "M$BEL$1$100$$PCAE-PLOTVIEW$"));

		}

	private void testThree()
		{
		// DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		getCgRootNode().addChild(renderLayer(doc, "0"));

		}

	private void testFour()
		{
		Document doc = readXMLDocument("dxf/4H-HORA Projekt1.xml");
		System.out.println("NodeType: " + doc.getDocumentElement().getNodeType()
			+ ", NodeName: " + doc.getDocumentElement().getNodeName());
		NodeList nodeList = doc.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++)
			{
			Node node = nodeList.item(i);
			System.out.println("NodeType: " + node.getNodeType()
				+ ", NodeName: " + node.getNodeName());
			if (node.hasAttributes())
				{
				NamedNodeMap map = node.getAttributes();
				for (int j = 0; j < map.getLength(); j++)
					{
					Node n = map.item(j);
					System.out.println("NodeType: " + n.getNodeType()
						+ ", NodeName: " + n.getNodeName());
					System.out.println("\t" + n.toString());
					}
				}

			if (node.getNodeName().equals("layer") && node.hasChildNodes())
				{
				NodeList wallsOfLayer = node.getChildNodes();
				for (int k = 0; k < wallsOfLayer.getLength(); k++)
					{
					Node wall = wallsOfLayer.item(k);
					System.out.println("NodeType: " + wall.getNodeType()
						+ ", NodeName: " + wall.getNodeName());
					if (wall.hasAttributes())
						{
						NamedNodeMap map = wall.getAttributes();
						for (int j = 0; j < map.getLength(); j++)
							{
							Node n = map.item(j);
							System.out.println("NodeType: " + n.getNodeType()
								+ ", NodeName: " + n.getNodeName());
							System.out
								.println("\t== wall " + n.toString() + " ==");
							}
						}

					if (wall.getNodeName().equals("wall")
						&& wall.hasChildNodes())
						{
						NodeList surfaces = wall.getChildNodes();
						for (int l = 0; l < surfaces.getLength(); l++)
							{
							Node surface = surfaces.item(l);
							System.out
								.println("NodeType: " + surface.getNodeType()
									+ ", NodeName: " + surface.getNodeName());
							if (surface.hasAttributes())
								{
								NamedNodeMap attr = surface.getAttributes();
								for (int j = 0; j < attr.getLength(); j++)
									{
									Node n = attr.item(j);
									System.out
										.println("NodeType: " + n.getNodeType()
											+ ", NodeName: " + n.getNodeName());
									System.out.println(
										"\t\t surface " + n.toString());
									}
								}
							}
						}
					}
				}
			}
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

	private CgNode renderLayer(DXFDocument doc, String layerName)
		{
		DXFLayer layer = doc.getDXFLayer(layerName);
		@SuppressWarnings("unchecked")
		List<DXFLine> lineList = layer
			.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
		GPRenderer renderer = new GPRenderer();

		return new CgNode(renderer.renderWallFromDXFLineFormat(lineList),
			layerName);
		// return handleLines(layerName, lineList);
		}

	private CgNode handleLines(String name, List<DXFLine> lineList)
		{
		CgNode result = null;
		LineSegments lineSegments = new LineSegments();
		int i = 0;
		for (DXFLine line : lineList)
			{
			Point startPoint = line.getStartPoint();
			Point endPoint = line.getEndPoint();
			System.out.println("Line " + line.getID() + " | Start=["
				+ startPoint.getX() + ", " + startPoint.getY() + ", "
				+ startPoint.getZ() + "], End=[" + endPoint.getX() + ", "
				+ endPoint.getY() + ", " + endPoint.getZ() + "]");
			System.out.println("Type: " + line.getType() + "; Flags: "
				+ line.getFlags() + "; LayerName: " + line.getLayerName());

			lineSegments.addPoint(VectorMatrixFactory.newIVector3(
				startPoint.getX(), startPoint.getY(), startPoint.getZ()));
			lineSegments.addPoint(VectorMatrixFactory.newIVector3(
				endPoint.getX(), endPoint.getY(), endPoint.getZ()));

			lineSegments.addLine(i++, i++);
			}

		lineSegments.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
		lineSegments.getMaterial().setReflectionDiffuse(
			VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR0));
		result = new CgNode(lineSegments, name);
		return result;
		}

	/*
	 * ########### Test with Walls #############
	 */

	private void testWalls()
		{
		// ferienhaus();
		haus02();
		}

	private void ferienhaus()
		{
		DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		DXFLayer layer = doc.getDXFLayer("0");
		handleWalls(layer);
		}

	private void haus02()
		{
		DXFDocument doc = readDocument("dxf/Grundriss_Haus_02.dxf");
		DXFLayer layer = doc.getDXFLayer("Ansichtskanten");
		handleWalls(layer);
		DXFLayer layer2 = doc.getDXFLayer("Schnittkanten");
		handleWalls(layer2);

		}

	private void handleWalls(DXFLayer layer)
		{
		@SuppressWarnings("unchecked")
		List<DXFLine> lineList = layer
			.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
		for (DXFLine line : lineList)
			{
			ITriangleMesh mesh = new TriangleMesh();
			buildAWall(mesh, line);

			mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			mesh.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));

			getCgRootNode().addChild(new CgNode(mesh, "testmesh"));
			}
		}

	private void buildAWall(ITriangleMesh mesh, DXFLine line)
		{
		Point start = line.getStartPoint();
		Point end = line.getEndPoint();

		// creating the two bottom points
		int a = mesh.addVertex(new Vertex(VectorMatrixFactory
			.newIVector3(start.getX(), start.getY(), start.getZ())));
		int b = mesh.addVertex(new Vertex(VectorMatrixFactory
			.newIVector3(end.getX(), end.getY(), end.getZ())));

		// creating the two top points
		int c = mesh.addVertex(new Vertex(VectorMatrixFactory
			.newIVector3(start.getX(), start.getY(), start.getZ() + 1.5)));
		int d = mesh.addVertex(new Vertex(VectorMatrixFactory
			.newIVector3(end.getX(), end.getY(), end.getZ() + 1.5)));

		mesh.addTriangle(new Triangle(a, b, c));
		mesh.addTriangle(new Triangle(b, c, d));

		}

	/*
	 * ########### Printing Content #############
	 */

	private void printContent(String dxfURL)
		{
		DXFDocument doc = readDocument(dxfURL);

		@SuppressWarnings("unchecked")
		Iterator<DXFBlock> itBlock = doc.getDXFBlockIterator();

		System.out.println("#==========Blocks============#");

		for (; itBlock.hasNext();)
			{
			DXFBlock block = itBlock.next();
			System.out.println("| + Block: " + block.getName());
			System.out.println("| + Description: " + block.getDescription());
			System.out.println("| + Length: " + block.getLength());
			System.out.println("| + LayerID: " + block.getLayerID());

			@SuppressWarnings("unchecked")
			Iterator<DXFEntity> entIty = block.getDXFEntitiesIterator();
			for (; entIty.hasNext();)
				{
				DXFEntity e = entIty.next();
				System.out.println("| + + Entity ID: " + e.getID());
				System.out.println("| + + Layername: " + e.getLayerName());
				}
			System.out.println("#============================#");

			}

		@SuppressWarnings("unchecked")
		Iterator<DXFLayer> it = doc.getDXFLayerIterator();

		System.out.println("#==========Layers============#");

		for (; it.hasNext();)
			{
			DXFLayer layer = it.next();
			System.out.println("| - Layer: " + layer.getName());
			System.out.println("| - LineType: " + layer.getLineType());
			System.out.println("| - PlotStyle: " + layer.getPlotStyle());
			printLayerContents(layer);
			System.out.println("#============================#");
			}

		}

	private void printLayerContents(DXFLayer layer)
		{
		@SuppressWarnings("unchecked")
		Iterator<String> it = layer.getDXFEntityTypeIterator();
		for (; it.hasNext();)
			{
			System.out.println("| - - " + it.next());
			}

		@SuppressWarnings("unchecked")
		List<DXFLine> lineList = layer
			.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);


		}

	/*
	 *
	 * ==============================================================
	 */

	public Test()
		{
		// testOne();
		// testTwo();
		// testThree();
		// testFour();
		// testWalls();
		// printContent("dxf/Grundriss-Ferienhaus.dxf");
		// printContent("dxf/Grundriss_Haus_02.dxf");
		printContent("dxf/4H-HORA Projekt1.dxf");
		}

	public static void main(String[] args)
		{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		CgApplication app = new Test();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);
		}

	}

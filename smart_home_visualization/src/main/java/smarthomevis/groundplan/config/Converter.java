package smarthomevis.groundplan.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import smarthomevis.groundplan.config.GPLine.LineType;

/**
 * Aufgabe dieser Klasse ist es, die xml- und dxf-Informationen auszulesen und
 * diesen entsprechend ein GPDataType Object zu befuellen
 * 
 * @author Leonard.Opitz
 *
 */
public class Converter {

	private GPDataType resultData = null;
	private DXFDocument dxfDoc = null;
	private int lineIndex;

	public Converter() {
		this.resultData = new GPDataType();
		this.lineIndex = 0;
	}

	/**
	 * Erzeugt ein GPDataType-Objekt aus den Inhalten einer DXF-Datei mit Hilfe
	 * der Strukturangaben aus einer XML-Datei
	 * 
	 * @param dxfDocURI
	 *            Die Datei mit den Vektorinformationen fuer das Rendering des
	 *            Grundrisses
	 * @param xmlDocURI
	 *            Das XML-Dokument mit den Konfigurationsparamertern und den
	 *            Strukturdefinitionen der DXF-Datei
	 * @return ein GPDataType Obejct mit den extrahierten Vectorinformationen
	 *         zum Erzeugen eines Renderings
	 */
	public GPDataType importData(String dxfDocURI, String xmlDocURI) {
		this.dxfDoc = readDXFDocument(dxfDocURI);
		Document xmlDoc = readXMLDocument(xmlDocURI);

		if (dxfDoc != null && xmlDoc != null) {
			extractWalls(dxfDoc, xmlDoc);
		} else
			System.err.println("Failed to import data. DXF or XML is missing!");

		return this.resultData;
	}

	/*
	 * 
	 */
	private void extractWalls(DXFDocument dxf, Document xmlDoc) {
		NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			// Kind-Knoten einer LayerNode des XMLs behandeln
			if (node.getNodeName().equals("layer") && node.hasAttributes()) {
				// Namen und Typ fuer diese Ebene aus dem XML auslesen
				String layerName = node.getAttributes().getNamedItem("name").getNodeValue();
				String lineTypeString = node.getAttributes().getNamedItem("type").getNodeValue();

				if (layerName != null && lineTypeString != null && !layerName.isEmpty() && !lineTypeString.isEmpty()) {
					System.out.println("Extracting " + lineTypeString.toLowerCase() + "s of layer '" + layerName + "'");
					// das im xml definierte DXFLayer aus der dxf-Datei lesen
					DXFLayer dxfLayer = dxf.getDXFLayer(layerName);
					// den Typ-String in einen Enumerator umwandeln
					LineType lineType = defineLineTypeFromString(lineTypeString);

					if (dxfLayer != null) {
						List<DXFEntity> entityList = extractDXFLinesFromLayer(dxfLayer);
						System.out.println("DXFEntityMap has size " + entityList.size());
						// die DXFLines entsprechend dem Typ rendern
						handleLinesOfLayer(layerName, entityList, lineType);
					} else
						System.out.println("Failed loading dxfLayer '" + layerName + "'");
				} else
					System.out.println("LayerName is missing, please check xml");

			}
			// Handelt es sich um eine config- statt um eine LayerNode, diese
			// Auslesen und dem
			// GPDataType uebergeben
			else if (node.getNodeName().equals("config") && node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				this.resultData.setWallTopHeight(
						Double.valueOf(element.getElementsByTagName("wall_top_height").item(0).getTextContent()));
				this.resultData.setWindowBottomHeight(
						Double.valueOf(element.getElementsByTagName("window_bottom_height").item(0).getTextContent()));
				this.resultData.setWindowTopHeight(
						Double.valueOf(element.getElementsByTagName("window_top_height").item(0).getTextContent()));
				this.resultData.setDoorTopHeight(
						Double.valueOf(element.getElementsByTagName("door_top_height").item(0).getTextContent()));
			}
		}
	}

	private LineType defineLineTypeFromString(String lineTypeString) {
		if (lineTypeString.equalsIgnoreCase("WALL"))
			return LineType.WALL;
		if (lineTypeString.equalsIgnoreCase("WINDOW"))
			return LineType.WINDOW;
		if (lineTypeString.equalsIgnoreCase("DOOR"))
			return LineType.DOOR;

		return LineType.WALL;
	}

	private List<DXFEntity> extractDXFLinesFromLayer(DXFLayer dxfLayer) {
		List<DXFEntity> lineList = new ArrayList<>();

		@SuppressWarnings("unchecked")
		Iterator<DXFEntity> it = dxfLayer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE).iterator();

		for (; it.hasNext();) {
			DXFEntity e = it.next();

			lineList.add(e);

		}
		return lineList;
	}

	/**
	 * Liest die Vectordaten aus den DXFLine Objekten und erzeugt GPLine
	 * Objekte, die dann dem GPDataType uebergeben werden
	 * 
	 * @param layerName
	 *            der Name des Layers, dem die Linien angehoeren
	 * @param dxfLineList
	 *            die Liste der DXFLines, die umgewandelt werden sollen
	 * @param lineType
	 *            enthaelt den Typ der Linie (z.B. Wall, Window oder Door)
	 */
	private void handleLinesOfLayer(String layerName, List<DXFEntity> dxfLineList, LineType lineType) {
		List<String> surfaceList = new ArrayList<>();

		for (DXFEntity e : dxfLineList) {
			if (e instanceof DXFLine) {
				String currentLineId = "Line_" + getNextIndex();
				DXFLine l = (DXFLine) e;
				Point startPoint = l.getStartPoint();
				Point endPoint = l.getEndPoint();

				GPLine surface = new GPLine(currentLineId,
						VectorMatrixFactory.newIVector3(startPoint.getX(), startPoint.getY(), startPoint.getZ()),
						VectorMatrixFactory.newIVector3(endPoint.getX(), endPoint.getY(), endPoint.getZ()));

				surface.setLineType(lineType);

				this.resultData.addLine(currentLineId, surface);
				surfaceList.add(currentLineId);
			}
		}

		this.resultData.addWall(layerName, surfaceList);
	}

	/*
	 * 
	 * 
	 * 
	 * File Reading
	 */

	/**
	 * Hilfsmethode fuer das Einlesen des DXFDokuments
	 * 
	 * @param filename
	 *            der Speicherort innerhalb des Asset-Ordners
	 * @return ein DXFDocument Object, das saemtliche Vektorinformationen des
	 *         Grundrisses enthaelt
	 */
	private DXFDocument readDXFDocument(String filename) {
		// get the File
		FileInputStream in = null;
		try {
			String absoluteFilename = ResourcesLocator.getInstance().getPathToResource(filename);
			in = new FileInputStream(new File(absoluteFilename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// get requested DXF Content
		Parser parser = ParserBuilder.createDefaultParser();
		DXFDocument doc = null;
		try {

			// parse
			parser.parse(in, DXFParser.DEFAULT_ENCODING);

			// get the document and the layer
			doc = parser.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * Hilfsmethode fuer das Einlesen des XMLDokuments
	 * 
	 * @param filename
	 *            der Speicherort innerhalt des Asset-Ordners
	 * @return ein Document Object, das saemtliche XML-Knoten zur Definition der
	 *         (relevanten) Strukturen im DXF-Dokument enthaelt
	 */
	private Document readXMLDocument(String filename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		Document xmlDoc = null;

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			String absoluteFilename = ResourcesLocator.getInstance().getPathToResource(filename);
			FileInputStream in = null;

			in = new FileInputStream(new File(absoluteFilename));
			xmlDoc = builder.parse(in);
			in.close();

		}
		// FIXME replace syserr with exception logging
		catch (ParserConfigurationException e) {
			System.err.println("Could not create a DocumentBuilder");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("Could not load xml file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Failed to parse xmlFile or close InputStream");
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("Error occured while parsing xmlFile");
			e.printStackTrace();
		}

		// NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		return xmlDoc;
	}

	private int getNextIndex() {
		return this.lineIndex++;
	}
}

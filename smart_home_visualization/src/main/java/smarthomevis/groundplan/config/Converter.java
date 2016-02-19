package smarthomevis.groundplan.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

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
public class Converter
{
	
	private GPDataType resultData = null;
	private DXFDocument dxfDoc = null;
	private int lineIndex;
	
	public Converter()
	{
		this.resultData = new GPDataType();
		this.lineIndex = 0;
	}
	
	/**
	 * Erzeugt ein GPDataType-Objekt aus den Inhalten einer DXF-Datei mit Hilfe
	 * der Strukturangaben aus einer XML-Datei
	 * 
	 * @param dxfDocURI
	 *          Die Datei mit den Vektorinformationen fuer das Rendering des
	 *          Grundrisses
	 * @param xmlDocURI
	 *          Das XML-Dokument mit den Konfigurationsparamertern und den
	 *          Strukturdefinitionen der DXF-Datei
	 * @return ein GPDataType Object mit den extrahierten Vectorinformationen
	 *         zum Erzeugen eines Renderings
	 */
	public GPDataType importData(String dxfDocURI, GPConfig config)
	{
	this.dxfDoc = readDXFDocument(dxfDocURI);
	
	if (dxfDoc != null && config != null)
		{
		extractWalls(dxfDoc, config);
		}
	else
		System.err.println("Failed to import data. DXF or Config is missing!");
		
	return this.resultData;
	}
	
	/*
	 * 
	 */
	private void extractWalls(DXFDocument dxf, GPConfig config)
	{
	
	Map<String, LineType> layers = config.getLayers();
	if (!layers.isEmpty())
		{
		for (Entry<String, LineType> e : layers.entrySet())
			{
			String layerName = e.getKey();
			LineType lineType = e.getValue();
			System.out.println("Extracting " + lineType.toString().toLowerCase()
				+ "s of layer '" + layerName + "'");
			// das im xml definierte DXFLayer aus der dxf-Datei lesen
			DXFLayer dxfLayer = dxf.getDXFLayer(layerName);
			// den Typ-String in einen Enumerator umwandeln
			
			if (dxfLayer != null)
				{
				List<DXFEntity> entityList = extractDXFLinesFromLayer(dxfLayer);
				System.out
					.println("DXFEntityMap has size " + entityList.size());
				// die DXFLines entsprechend dem Typ rendern
				if (entityList.size() > 0)
					handleLinesOfLayer(layerName, entityList, lineType,
						config.getValue(GPConfig.GROUNDPLAN_SCALING_FACTOR));
				else
					System.out
						.println("Could not Load DXFEntities: List is empty");
				}
			else
				System.out
					.println("Failed loading dxfLayer '" + layerName + "'");
			}
		}
	else
		System.out.println("No Layers found, please check xml");
		
	}
	
	private List<DXFEntity> extractDXFLinesFromLayer(DXFLayer dxfLayer)
	{
	List<DXFEntity> lineList = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	List<DXFEntity> dxfEntities = dxfLayer
		.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
		
	if (dxfEntities != null && dxfEntities.size() > 0)
		{
		Iterator<DXFEntity> it = dxfEntities.iterator();
		
		for (; it.hasNext();)
			{
			DXFEntity e = it.next();
			
			lineList.add(e);
			
			}
		}
	return lineList;
	}
	
	/**
	 * Liest die Vectordaten aus den DXFLine Objekten und erzeugt GPLine
	 * Objekte, die dann dem GPDataType uebergeben werden
	 * 
	 * @param layerName
	 *          der Name des Layers, dem die Linien angehoeren
	 * @param dxfLineList
	 *          die Liste der DXFLines, die umgewandelt werden sollen
	 * @param lineType
	 *          enthaelt den Typ der Linie (z.B. Wall, Window oder Door)
	 * @param scale
	 *          der Skalierungsfaktor zur Umrechnung der Koordinaten
	 */
	private void handleLinesOfLayer(String layerName,
		List<DXFEntity> dxfLineList, LineType lineType, double scale)
	{
	List<String> surfaceList = new ArrayList<>();
	
	for (DXFEntity e : dxfLineList)
		{
		if (e instanceof DXFLine)
			{
			String currentLineId = "Line_" + getNextIndex();
			DXFLine l = (DXFLine) e;
			Point startPoint = l.getStartPoint();
			Point endPoint = l.getEndPoint();
			
			GPLine surface = new GPLine(currentLineId,
				VectorMatrixFactory.newVector(startPoint.getX() * scale,
					startPoint.getY() * scale, startPoint.getZ() * scale),
				VectorMatrixFactory.newVector(endPoint.getX() * scale,
					endPoint.getY() * scale, endPoint.getZ() * scale));
					
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
	 *          der Speicherort innerhalb des Asset-Ordners
	 * @return ein DXFDocument Object, das saemtliche Vektorinformationen des
	 *         Grundrisses enthaelt
	 */
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
	
	private int getNextIndex()
	{
	return this.lineIndex++;
	}
}

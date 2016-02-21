package smarthomevis.groundplan;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPDataType;
import smarthomevis.groundplan.config.GPLine;

/**
 * Diese Klasse ist fuer das Rendering verschiedener Darstellungen von
 * Grundrissen zustaendig. Das Rendering basiert hierbei auf dem internen
 * Datentyp GPDataType.
 * 
 * @author Leonard Opitz
 * 		
 */
public class GPRenderer
{
	private static Logger logger = Logger.getInstance();
	
	private final GPDataType data;
	
	public GPRenderer(GPDataType data)
	{
		this.data = data;
	}
	
	/*
	 * 
	 * 
	 * ============== 3D Mesh ===============
	 * 
	 */
	
	public CgNode render3DMeshViewFromGPDataType()
	{
	CgNode rootNode = new CgRootNode();
	Map<String, List<GPLine>> layersMap = this.data.getLayers();
	
	for (Entry<String, List<GPLine>> e : layersMap.entrySet())
		{
		String layerName = e.getKey();
		logger.debug("Rendering " + e.getValue().size() + " elements of layer "
			+ layerName);
		System.out.println("Rendering " + e.getValue().size()
			+ " elements of layer " + layerName);
			
		rootNode
			.addChild(renderScaled3DMeshViewOfLayer(layerName, e.getValue()));
		}
	// rootNode.addChild(new CoordinateSystem());
	
	return rootNode;
	}
	
	private CgNode renderScaled3DMeshViewOfLayer(String layerName,
		List<GPLine> lineList)
	{
	
	ITriangleMesh mesh = new TriangleMesh();
	
	for (GPLine l : lineList)
		{
		switch (l.getLineType())
			{
			case WALL:
			render3DMeshWall(mesh, l);
			break;
			case WINDOW:
			render3DMeshWindow(mesh, l);
			break;
			case DOOR:
			render3DMeshDoor(mesh, l);
			break;
			
			default:
			break;
			}
		}
		
	mesh.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
	// mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
	mesh.getMaterial().setReflectionDiffuse(
		VectorFactory.createVector(Material.PALETTE1_COLOR3));
	mesh.getMaterial().setRenderMode(Normals.PER_FACET);
	mesh.computeTriangleNormals();
	mesh.computeVertexNormals();
	
	return new CgNode(mesh, layerName);
	}
	
	private ITriangleMesh render3DMeshWall(ITriangleMesh mesh, GPLine l)
	{
	GPConfig gpConfig = this.data.getGPConfig();
	
	Vector start = l.getStart();
	System.out.println("StartPoint: " + start.toString(3));
	Vector end = l.getEnd();
	System.out.println("EndPoint: " + end.toString(3));
	
	Vector wallTopStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	Vector wallTopEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
	int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
	int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
	int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));
	
	mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex,
		wallTopStartIndex);
	// mesh.addTriangle(wallTopStartIndex, wallTopEndIndex,
	// wallBottomEndIndex);
	mesh.addTriangle(wallTopStartIndex, wallBottomEndIndex, wallTopEndIndex);
	
	return mesh;
	}
	
	private ITriangleMesh render3DMeshWindow(ITriangleMesh mesh, GPLine l)
	{
	GPConfig gpConfig = this.data.getGPConfig();
	
	// Die oberen und unteren Ortsvektoren der Wand erzeugen
	Vector start = l.getStart();
	Vector end = l.getEnd();
	
	Vector wallTopStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	Vector wallTopEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
	int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
	int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
	int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));
	
	// Die oberen und unteren Ortsvektoren des Fensters erzeugen
	Vector bottomWindowStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WINDOW_BOTTOM_HEIGHT));
	Vector bottomWindowEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WINDOW_BOTTOM_HEIGHT));
		
	Vector topWindowStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WINDOW_TOP_HEIGHT));
	Vector topWindowEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WINDOW_TOP_HEIGHT));
		
	int windowBottomStartIndex = mesh.addVertex(new Vertex(bottomWindowStart));
	int windowBottomEndIndex = mesh.addVertex(new Vertex(bottomWindowEnd));
	int windowTopStartIndex = mesh.addVertex(new Vertex(topWindowStart));
	int windowTopEndIndex = mesh.addVertex(new Vertex(topWindowEnd));
	
	// die Dreiecke zur Darstellung des unteren Wand-Elements
	mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex,
		windowBottomStartIndex);
	mesh.addTriangle(windowBottomStartIndex, windowBottomEndIndex,
		wallBottomEndIndex);
		// mesh.addTriangle(windowBottomStartIndex, wallBottomEndIndex,
		// windowBottomEndIndex);
		
	// die Dreiecke zur Darstellung des oberen Wand-Elements
	mesh.addTriangle(windowTopStartIndex, windowTopEndIndex, wallTopStartIndex);
	mesh.addTriangle(wallTopStartIndex, wallTopEndIndex, windowTopEndIndex);
	// mesh.addTriangle(wallTopStartIndex, windowTopEndIndex,
	// wallTopEndIndex);
	
	return mesh;
	}
	
	private ITriangleMesh render3DMeshDoor(ITriangleMesh mesh, GPLine l)
	{
	GPConfig gpConfig = this.data.getGPConfig();
	
	Vector start = l.getStart();
	Vector end = l.getEnd();
	
	// Die oberen Ortsvektoren der Wand erzeugen
	Vector wallTopStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
	Vector wallTopEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	// Die Ortsvektoren der Tuer erzeugen
	Vector doorStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.DOOR_TOP_HEIGHT));
	Vector doorEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.DOOR_TOP_HEIGHT));
		
	int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
	int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));
	int doorStartIndex = mesh.addVertex(new Vertex(doorStart));
	int doorEndIndex = mesh.addVertex(new Vertex(doorEnd));
	
	// Zwei Dreiecke zum Darstellen des Wandelements oberhalb der Tuer
	// erzeugen
	mesh.addTriangle(doorStartIndex, doorEndIndex, wallTopStartIndex);
	mesh.addTriangle(wallTopStartIndex, wallTopEndIndex, doorEndIndex);
	// mesh.addTriangle(wallTopStartIndex, doorEndIndex, wallTopEndIndex);
	
	return mesh;
	}
	
	/*
	 * 
	 * 
	 * ============== 3D Grid ===============
	 * 
	 */
	
	public CgNode render3DGridViewFromGPDataType()
	{
	CgNode rootNode = new CgRootNode();
	Map<String, List<GPLine>> layersMap = this.data.getLayers();
	
	for (Entry<String, List<GPLine>> e : layersMap.entrySet())
		{
		String layerName = e.getKey();
		logger.debug("Rendering " + e.getValue().size() + " elements of layer "
			+ layerName);
			
		rootNode.addChild(render3DGridViewOfLayer(layerName, e.getValue()));
		}
		
	return rootNode;
	}
	
	private CgNode render3DGridViewOfLayer(String layerName,
		List<GPLine> lineList)
	{
	LineSegments segment = new LineSegments();
	
	for (GPLine l : lineList)
		{
		switch (l.getLineType())
			{
			case WALL:
			segment = render3DGridWall(segment, l);
			break;
			case WINDOW:
			segment = render3DGridWindow(segment, l);
			break;
			case DOOR:
			segment = render3DGridDoor(segment, l);
			break;
			
			default:
			break;
			}
		}
		
	segment.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
	segment.getMaterial().setReflectionDiffuse(
		VectorFactory.createVector(Material.PALETTE1_COLOR0));
		
	return new CgNode(segment, layerName);
	}
	
	private LineSegments render3DGridWall(LineSegments segment, GPLine line)
	{
	logger.debug("\trendering wall " + line.getName());
	GPConfig gpConfig = this.data.getGPConfig();
	
	// zuerst die Bodenlinie erzeugen
	Vector start = line.getStart();
	int a = segment.addPoint(start);
	Vector end = line.getEnd();
	int b = segment.addPoint(end);
	
	segment.addLine(a, b);
	
	// die obere Wandlinie erzeugen
	
	Vector upperStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	Vector upperEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
		
	int c = segment.addPoint(upperStart);
	int d = segment.addPoint(upperEnd);
	
	segment.addLine(c, d);
	
	// vertikale Linien erzeugen
	// segment.addLine(a, c);
	// segment.addLine(b, d);
	
	return segment;
	}
	
	private LineSegments render3DGridWindow(LineSegments segment, GPLine line)
	{
	logger.debug("\trendering window " + line.getName());
	GPConfig gpConfig = this.data.getGPConfig();
	
	// zuerst die untere Wandlinie erzeugen
	Vector start = line.getStart();
	int bottomWallStartIndex = segment.addPoint(start);
	Vector end = line.getEnd();
	int bottomWallEndIndex = segment.addPoint(end);
	
	segment.addLine(bottomWallStartIndex, bottomWallEndIndex);
	
	// die untere Linie des Fensters erzeugen
	Vector bottomWindowStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WINDOW_BOTTOM_HEIGHT));
	Vector bottomWindowEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WINDOW_BOTTOM_HEIGHT));
	int bottomWindowStartIndex = segment.addPoint(bottomWindowStart);
	int bottomWindowEndIndex = segment.addPoint(bottomWindowEnd);
	
	segment.addLine(bottomWindowStartIndex, bottomWindowEndIndex);
	
	// die obere Linie des Fensters erzeugen
	Vector topWindowStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WINDOW_TOP_HEIGHT));
	Vector topWindowEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WINDOW_TOP_HEIGHT));
	int topWindowStartIndex = segment.addPoint(topWindowStart);
	int topWindowEndIndex = segment.addPoint(topWindowEnd);
	
	segment.addLine(topWindowStartIndex, topWindowEndIndex);
	
	// die obere Linie der Wand erzeugen
	Vector topWallStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
	int topWallStartIndex = segment.addPoint(topWallStart);
	Vector topWallEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
	int topWallEndIndex = segment.addPoint(topWallEnd);
	
	segment.addLine(topWallStartIndex, topWallEndIndex);
	
	// vertikale Linien des Fensters erzeugen
	segment.addLine(bottomWindowStartIndex, topWindowStartIndex);
	segment.addLine(bottomWindowEndIndex, topWindowEndIndex);
	
	return segment;
	}
	
	private LineSegments render3DGridDoor(LineSegments segment, GPLine line)
	{
	GPConfig gpConfig = this.data.getGPConfig();
	
	Vector start = line.getStart();
	int startIndex = segment.addPoint(start);
	Vector end = line.getEnd();
	int endIndex = segment.addPoint(end);
	
	// die obere Tuerlinie erzeugen
	Vector topDoorStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.DOOR_TOP_HEIGHT));
	Vector topDoorEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.DOOR_TOP_HEIGHT));
	int topDoorStartIndex = segment.addPoint(topDoorStart);
	int topDoorEndIndex = segment.addPoint(topDoorEnd);
	
	segment.addLine(topDoorStartIndex, topDoorEndIndex);
	
	// die obere Wandlinie erzeugen
	Vector topWallStart = new Vector(start.get(0), start.get(1),
		start.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
	Vector topWallEnd = new Vector(end.get(0), end.get(1),
		end.get(2) + gpConfig.getValue(GPConfig.WALL_TOP_HEIGHT));
	int topWallStartIndex = segment.addPoint(topWallStart);
	int topWallEndIndex = segment.addPoint(topWallEnd);
	
	segment.addLine(topWallStartIndex, topWallEndIndex);
	
	// vertikale Tuerlinien hinzufuegen
	segment.addLine(startIndex, topDoorStartIndex);
	segment.addLine(endIndex, topDoorEndIndex);
	
	return segment;
	}
	
	/*
	 * 
	 * 
	 * ============== 2D ===============
	 * 
	 */
	public CgNode render2DViewFromGPDataType()
	{
	CgNode rootNode = new CgRootNode();
	
	Map<String, List<GPLine>> wallsMap = this.data.getLayers();
	
	logger.debug("=== Rendering Elements of GPDataType ===");
	
	for (Entry<String, List<GPLine>> e : wallsMap.entrySet())
		{
		String wallId = e.getKey();
		logger.debug("\tRendering wall " + wallId);
		CgNode wallNode = new CgNode(
			render2DLineFromGPSurfaceList(e.getValue()), wallId);
		rootNode.addChild(wallNode);
		}
		
	return rootNode;
	}
	
	private LineSegments render2DLineFromGPSurfaceList(List<GPLine> surfaceList)
	{
	LineSegments wallSegment = new LineSegments();
	
	for (GPLine surface : surfaceList)
		{
		wallSegment = render2DLineFromGPSurface(wallSegment, surface);
		// increase the index to be correct for the next 2 points
		}
		
	wallSegment.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
	wallSegment.getMaterial().setReflectionDiffuse(
		VectorFactory.createVector(Material.PALETTE1_COLOR0));
		
	return wallSegment;
	}
	
	private LineSegments render2DLineFromGPSurface(LineSegments wallSegment,
		GPLine surface)
	{
	int a = wallSegment.addPoint(surface.getStart());
	int b = wallSegment.addPoint(surface.getEnd());
	
	wallSegment.addLine(a, b);
	return wallSegment;
	}
	
	/*
	 * 
	 * 
	 * ============== old methods ===============
	 * 
	 */
	
	/**
	 * takes a list of DXFLines and returns a LineSegments object containing
	 * representations of all these lines
	 * 
	 * @param unitElements
	 *          the List containing all the DXFLines to be represented
	 * @return a LineSegments object containing representations of all delivered
	 *         DXFLines
	 */
	public LineSegments renderWallFromDXFLineFormat(List<DXFLine> unitElements)
	{
	LineSegments unit = new LineSegments();
	
	/*
	 * points added to the LineSegments object need to be referenced by their
	 * index of addition (addPoint(x,y,z) could/should return that index..)
	 */
	int index = 0;
	
	for (DXFLine line : unitElements)
		{
		unit = renderLineFromDXFLine(unit, index, line);
		// increase the index to be correct for the next 2 points
		index = index + 2;
		}
		
	unit.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
	unit.getMaterial().setReflectionDiffuse(
		VectorFactory.createVector(Material.PALETTE1_COLOR0));
	return unit;
	}
	
	/**
	 * adds a representation of the given Line to the LineSegments object
	 * 
	 * @param segments
	 *          the LineSegments object containing the resultset
	 * @param index
	 *          the start index of this lines points in the whole unit
	 * @param line
	 *          the single DXFLine to render
	 */
	public LineSegments renderLineFromDXFLine(LineSegments segments, int index,
		DXFLine line)
	{
	Point startPoint = line.getStartPoint();
	Point endPoint = line.getEndPoint();
	
	segments.addPoint(VectorFactory.createVector3(startPoint.getX(),
		startPoint.getY(), startPoint.getZ()));
	segments.addPoint(VectorFactory.createVector3(endPoint.getX(),
		endPoint.getY(), endPoint.getZ()));
		
	segments.addLine(index, index + 1);
	return segments;
	}
}

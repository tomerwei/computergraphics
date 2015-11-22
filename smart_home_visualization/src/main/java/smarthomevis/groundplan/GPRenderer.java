package smarthomevis.groundplan;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
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
		CgRootNode rootNode = new CgRootNode();
		Map<String, List<GPLine>> layersMap = this.data.getLayers();

		for (Entry<String, List<GPLine>> e : layersMap.entrySet())
		{
			String layerName = e.getKey();
			logger.debug("Rendering " + e.getValue().size() + " elements of layer " + layerName);

			rootNode = render3DMeshViewOfLayer(rootNode, layerName, e.getValue());
		}
		// rootNode.addChild(new CoordinateSystem());

		return rootNode;
	}

	private CgRootNode render3DMeshViewOfLayer(CgRootNode rootNode, String layerName, List<GPLine> lineList)
	{

		ITriangleMesh mesh = new TriangleMesh();
		for (GPLine l : lineList)
		{
			switch (l.getLineType())
			{
			case WALL:
				render3DMeshWall(mesh, l);
				// render3DMeshWallDirectional(mesh, l);
				break;
			case WINDOW:
				render3DMeshWindow(mesh, l);
				// render3DMeshWindowDirectional(mesh, l);
				break;
			case DOOR:
				render3DMeshDoor(mesh, l);
				break;

			default:
				break;
			}
		}

		mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		// mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
		mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR3));
		mesh.getMaterial().setRenderMode(Normals.PER_FACET);
		mesh.computeTriangleNormals();
		mesh.computeVertexNormals();
		rootNode.addChild(new CgNode(mesh, layerName));

		return rootNode;
	}

	private ITriangleMesh render3DMeshWall(ITriangleMesh mesh, GPLine l)
	{
		IVector3 start = l.getStart();
		IVector3 end = l.getEnd();

		IVector3 wallTopStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());

		IVector3 wallTopEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

		int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
		int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
		int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
		int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));

		mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex, wallTopStartIndex);
		// mesh.addTriangle(wallTopStartIndex, wallTopEndIndex,
		// wallBottomEndIndex);
		mesh.addTriangle(wallTopStartIndex, wallBottomEndIndex, wallTopEndIndex);

		return mesh;
	}

	private ITriangleMesh render3DMeshWallDirectional(ITriangleMesh mesh, GPLine l)
	{
		IVector3 start = l.getStart();
		IVector3 end = l.getEnd();

		IVector3 wallTopStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());

		IVector3 wallTopEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

		int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
		int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
		int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
		int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));

		// berechnung des Richtungsvektors zwischen start und end
		IVector3 dirVec = new Vector3(end.get(0) - start.get(0), end.get(1) - start.get(1), end.get(2) - start.get(2));

		if (start.get(0) <= end.get(0) && start.get(1) <= end.get(1))
		{
			if (dirVec.get(0) == 0)
			{
				mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex, wallTopStartIndex);
				mesh.addTriangle(wallTopEndIndex, wallTopStartIndex, wallBottomEndIndex);
			}
			else if (dirVec.get(1) == 0)
			{
				// mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex,
				// wallTopStartIndex);
				// mesh.addTriangle(wallTopEndIndex, wallTopStartIndex,
				// wallBottomEndIndex);
			}
			else
			{
				// mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex,
				// wallTopStartIndex);
				// mesh.addTriangle(wallTopEndIndex, wallTopStartIndex,
				// wallBottomEndIndex);
			}
		}
		// identische x-Ordinate
		else if (start.get(0) == end.get(0))
		{
			if (start.get(1) > end.get(1))
			{
				mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex, wallTopStartIndex);
				mesh.addTriangle(wallTopEndIndex, wallTopStartIndex, wallBottomEndIndex);
				System.out.println("Drawing Line " + l.getName() + " of " + l.getLineType().toString().toLowerCase()
					+ " with start " + start.toString() + " and end " + end.toString());
			}
			else
			{

			}
		}
		// identische y-Ordinate (scheint korrekt!?)
		else if (start.get(1) == end.get(1))
		{
			mesh.addTriangle(wallBottomStartIndex, wallTopStartIndex, wallBottomEndIndex);
			mesh.addTriangle(wallTopStartIndex, wallTopEndIndex, wallBottomEndIndex);
		}
		else
		{
		}

		return mesh;
	}

	private ITriangleMesh render3DMeshWindow(ITriangleMesh mesh, GPLine l)
	{

		// Die oberen und unteren Ortsvektoren der Wand erzeugen
		IVector3 start = l.getStart();
		IVector3 end = l.getEnd();

		IVector3 wallTopStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());

		IVector3 wallTopEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

		int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
		int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
		int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
		int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));

		// Die oberen und unteren Ortsvektoren des Fensters erzeugen
		IVector3 bottomWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowBottomHeight());
		IVector3 bottomWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowBottomHeight());

		IVector3 topWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowTopHeight());
		IVector3 topWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowTopHeight());

		int windowBottomStartIndex = mesh.addVertex(new Vertex(bottomWindowStart));
		int windowBottomEndIndex = mesh.addVertex(new Vertex(bottomWindowEnd));
		int windowTopStartIndex = mesh.addVertex(new Vertex(topWindowStart));
		int windowTopEndIndex = mesh.addVertex(new Vertex(topWindowEnd));

		// die Dreiecke zur Darstellung des unteren Wand-Elements
		mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex, windowBottomStartIndex);
		mesh.addTriangle(windowBottomStartIndex, windowBottomEndIndex, wallBottomEndIndex);
		// mesh.addTriangle(windowBottomStartIndex, wallBottomEndIndex,
		// windowBottomEndIndex);

		// die Dreiecke zur Darstellung des oberen Wand-Elements
		mesh.addTriangle(windowTopStartIndex, windowTopEndIndex, wallTopStartIndex);
		mesh.addTriangle(wallTopStartIndex, wallTopEndIndex, windowTopEndIndex);
		// mesh.addTriangle(wallTopStartIndex, windowTopEndIndex,
		// wallTopEndIndex);

		return mesh;
	}

	private ITriangleMesh render3DMeshWindowDirectional(ITriangleMesh mesh, GPLine l)
	{

		// Die oberen und unteren Ortsvektoren der Wand erzeugen
		IVector3 start = l.getStart();
		IVector3 end = l.getEnd();

		IVector3 wallTopStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());

		IVector3 wallTopEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

		int wallBottomStartIndex = mesh.addVertex(new Vertex(start));
		int wallBottomEndIndex = mesh.addVertex(new Vertex(end));
		int wallTopStartIndex = mesh.addVertex(new Vertex(wallTopStart));
		int wallTopEndIndex = mesh.addVertex(new Vertex(wallTopEnd));

		// Die oberen und unteren Ortsvektoren des Fensters erzeugen
		IVector3 bottomWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowBottomHeight());
		IVector3 bottomWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowBottomHeight());

		IVector3 topWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowTopHeight());
		IVector3 topWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowTopHeight());

		int windowBottomStartIndex = mesh.addVertex(new Vertex(bottomWindowStart));
		int windowBottomEndIndex = mesh.addVertex(new Vertex(bottomWindowEnd));
		int windowTopStartIndex = mesh.addVertex(new Vertex(topWindowStart));
		int windowTopEndIndex = mesh.addVertex(new Vertex(topWindowEnd));

		// berechnung des Richtungsvektors zwischen start und end
		IVector3 dirVec = new Vector3(end.get(0) - start.get(0), end.get(1) - start.get(1), end.get(2) - start.get(2));
		if (dirVec.get(0) < 0 || dirVec.get(1) < 0)
		{

		}
		else
		{

			// die Dreiecke zur Darstellung des unteren Wand-Elements
			mesh.addTriangle(wallBottomStartIndex, wallBottomEndIndex, windowBottomStartIndex);
			mesh.addTriangle(windowBottomStartIndex, wallBottomEndIndex, windowBottomEndIndex);

			// die Dreiecke zur Darstellung des oberen Wand-Elements
			mesh.addTriangle(windowTopStartIndex, windowTopEndIndex, wallTopStartIndex);
			mesh.addTriangle(wallTopStartIndex, windowTopEndIndex, wallTopEndIndex);
		}

		return mesh;
	}

	private ITriangleMesh render3DMeshDoor(ITriangleMesh mesh, GPLine l)
	{
		IVector3 start = l.getStart();
		IVector3 end = l.getEnd();

		// Die oberen Ortsvektoren der Wand erzeugen
		IVector3 wallTopStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());
		IVector3 wallTopEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

		// Die Ortsvektoren der Tuer erzeugen
		IVector3 doorStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getDoorTopHeight());
		IVector3 doorEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getDoorTopHeight());

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
			logger.debug("Rendering " + e.getValue().size() + " elements of layer " + layerName);

			rootNode.addChild(render3DGridViewOfLayer(layerName, e.getValue()));
		}

		return rootNode;
	}

	private CgNode render3DGridViewOfLayer(String layerName, List<GPLine> lineList)
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
		segment.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR0));

		return new CgNode(segment, layerName);
	}

	private LineSegments render3DGridWall(LineSegments segment, GPLine line)
	{
		logger.debug("\trendering wall " + line.getName());

		// zuerst die Bodenlinie erzeugen
		IVector3 start = line.getStart();
		int a = segment.addPoint(start);
		IVector3 end = line.getEnd();
		int b = segment.addPoint(end);

		segment.addLine(a, b);

		// die obere Wandlinie erzeugen

		IVector3 upperStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());

		IVector3 upperEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());

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

		// zuerst die untere Wandlinie erzeugen
		IVector3 start = line.getStart();
		int bottomWallStartIndex = segment.addPoint(start);
		IVector3 end = line.getEnd();
		int bottomWallEndIndex = segment.addPoint(end);

		segment.addLine(bottomWallStartIndex, bottomWallEndIndex);

		// die untere Linie des Fensters erzeugen
		IVector3 bottomWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowBottomHeight());
		IVector3 bottomWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowBottomHeight());
		int bottomWindowStartIndex = segment.addPoint(bottomWindowStart);
		int bottomWindowEndIndex = segment.addPoint(bottomWindowEnd);

		segment.addLine(bottomWindowStartIndex, bottomWindowEndIndex);

		// die obere Linie des Fensters erzeugen
		IVector3 topWindowStart = new Vector3(start.get(0), start.get(1),
			start.get(2) + this.data.getWindowTopHeight());
		IVector3 topWindowEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWindowTopHeight());
		int topWindowStartIndex = segment.addPoint(topWindowStart);
		int topWindowEndIndex = segment.addPoint(topWindowEnd);

		segment.addLine(topWindowStartIndex, topWindowEndIndex);

		// die obere Linie der Wand erzeugen
		IVector3 topWallStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());
		int topWallStartIndex = segment.addPoint(topWallStart);
		IVector3 topWallEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());
		int topWallEndIndex = segment.addPoint(topWallEnd);

		segment.addLine(topWallStartIndex, topWallEndIndex);

		// vertikale Linien des Fensters erzeugen
		segment.addLine(bottomWindowStartIndex, topWindowStartIndex);
		segment.addLine(bottomWindowEndIndex, topWindowEndIndex);

		return segment;
	}

	private LineSegments render3DGridDoor(LineSegments segment, GPLine line)
	{
		IVector3 start = line.getStart();
		int startIndex = segment.addPoint(start);
		IVector3 end = line.getEnd();
		int endIndex = segment.addPoint(end);

		// die obere Tuerlinie erzeugen
		IVector3 topDoorStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getDoorTopHeight());
		IVector3 topDoorEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getDoorTopHeight());
		int topDoorStartIndex = segment.addPoint(topDoorStart);
		int topDoorEndIndex = segment.addPoint(topDoorEnd);

		segment.addLine(topDoorStartIndex, topDoorEndIndex);

		// die obere Wandlinie erzeugen
		IVector3 topWallStart = new Vector3(start.get(0), start.get(1), start.get(2) + this.data.getWallTopHeight());
		IVector3 topWallEnd = new Vector3(end.get(0), end.get(1), end.get(2) + this.data.getWallTopHeight());
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
			CgNode wallNode = new CgNode(render2DLineFromGPSurfaceList(e.getValue()), wallId);
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
		wallSegment.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR0));

		return wallSegment;
	}

	private LineSegments render2DLineFromGPSurface(LineSegments wallSegment, GPLine surface)
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
		 * points added to the LineSegments object need to be referenced by
		 * their index of addition (addPoint(x,y,z) could/should return that
		 * index..)
		 */
		int index = 0;

		for (DXFLine line : unitElements)
		{
			unit = renderLineFromDXFLine(unit, index, line);
			// increase the index to be correct for the next 2 points
			index = index + 2;
		}

		unit.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
		unit.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR0));
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
	public LineSegments renderLineFromDXFLine(LineSegments segments, int index, DXFLine line)
	{
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();

		segments.addPoint(VectorMatrixFactory.newIVector3(startPoint.getX(), startPoint.getY(), startPoint.getZ()));
		segments.addPoint(VectorMatrixFactory.newIVector3(endPoint.getX(), endPoint.getY(), endPoint.getZ()));

		segments.addLine(index, index + 1);
		return segments;
	}
}

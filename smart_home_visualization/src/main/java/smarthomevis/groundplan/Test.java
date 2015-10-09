package smarthomevis.groundplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.kabeja.dxf.DXFBlock;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.DXFPolyline;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
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
			String absoluteFilename = ResourcesLocator.getInstance().getPathToResource(filename);
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
		DXFDocument doc = readDocument("dxf/Grundriss-Ferienhaus.dxf");
		DXFLayer layer = doc.getDXFLayer("0");
		@SuppressWarnings("unchecked")
		List<DXFLine> lineList = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
		handleLines(lineList);

		// @SuppressWarnings("unchecked")
		// List<DXFPolyline> lineList =
		// layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
		// handlePolylines(lineList);

	}

	private void handleLines(List<DXFLine> lineList)
	{
		for (DXFLine line : lineList)
		{
			System.out.println(
				"Line " + line.getID() + " | Start=[" + line.getStartPoint().getX() + ", " + line.getStartPoint().getY()
					+ ", " + line.getStartPoint().getZ() + "], End=[" + line.getEndPoint().getX() + ", "
					+ line.getEndPoint().getY() + ", " + line.getEndPoint().getZ() + "]");
			System.out.println(
				"Type: " + line.getType() + "; Flags: " + line.getFlags() + "; LineType: " + line.getLineType());
			Line3D line3D = createLine3DFromDXFLine(line);
			ITriangleMesh mesh = createLine3D(line3D, 5, 0.015);
			mesh.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
			mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE1_COLOR0));
			getCgRootNode().addChild(new CgNode(mesh, "testmesh"));
		}
	}

	/**
	 * Create a mesh for a specific line.
	 */
	private ITriangleMesh createLine3D(Line3D line, int resolution, double radius)
	{
		ITriangleMesh mesh = TriangleMeshFactory.createLine3D(resolution, radius);
		mesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);

		// Scale to the required length
		double length = line.getEnd().subtract(line.getStart()).getNorm();
		TriangleMeshTransformation.scale(mesh, length);

		// Rotate
		IVector3 direction = line.getEnd().subtract(line.getStart());
		direction.normalize();
		IMatrix3 T = VectorMatrixFactory.createCoordinateFrameX(direction);
		TriangleMeshTransformation.transform(mesh, T);

		// Translate start
		TriangleMeshTransformation.translate(mesh, line.getStart());
		return mesh;
	}

	private Line3D createLine3DFromDXFLine(DXFLine line)
	{
		return new Line3D(
			VectorMatrixFactory.newIVector3(line.getStartPoint().getX(), line.getStartPoint().getY(),
				line.getStartPoint().getZ()),
			VectorMatrixFactory.newIVector3(line.getEndPoint().getX(), line.getEndPoint().getY(),
				line.getEndPoint().getZ()));
	}

	private void handlePolylines(List<DXFPolyline> lineList)
	{
		for (DXFPolyline pLine : lineList)
		{
			System.out.println("Polyline " + pLine.getID());
			System.out.println(
				"Type: " + pLine.getType() + "; Flags: " + pLine.getFlags() + "; LineType: " + pLine.getLineType());
		}
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
		List<DXFLine> lineList = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
		for (DXFLine line : lineList)
		{
			ITriangleMesh mesh = new TriangleMesh();
			buildAWall(mesh, line);

			mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));

			getCgRootNode().addChild(new CgNode(mesh, "testmesh"));
		}
	}

	private void buildAWall(ITriangleMesh mesh, DXFLine line)
	{
		Point start = line.getStartPoint();
		Point end = line.getEndPoint();

		// creating the two bottom points
		int a = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(start.getX(), start.getY(), start.getZ())));
		int b = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(end.getX(), end.getY(), end.getZ())));

		// creating the two top points
		int c = mesh
			.addVertex(new Vertex(VectorMatrixFactory.newIVector3(start.getX(), start.getY(), start.getZ() + 1.5)));
		int d = mesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(end.getX(), end.getY(), end.getZ() + 1.5)));

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
	}

	/*
	 * 
	 * ==============================================================
	 */

	public Test()
	{
		// testOne();
		testWalls();
		// printContent("dxf/Grundriss-Ferienhaus.dxf");
		// printContent("dxf/Grundriss_Haus_02.dxf");
		// printContent("dxf/4H-HORA Projekt1.dxf");
	}

	public static void main(String[] args)
	{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		CgApplication app = new Test();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);
	}

	// private void drawPolylines(List<DXFPolyline> dxfEntities)
	// {
	// System.out.println("Starting to draw");
	// for (DXFPolyline pLine : dxfEntities)
	// {
	// drawTriangle(pLine);
	// }
	// }
	//
	// private void drawTriangle(DXFPolyline pLine)
	// {
	// if (pLine.getVertexCount() == 4)
	// {
	// ITriangleMesh triangleMesh = new TriangleMesh();
	//
	// int a, b, c, d = 0;
	// // iterate over all vertex of the polyline
	// a = drawVertex(triangleMesh, pLine.getVertex(0).);
	// b = drawVertex(triangleMesh, pLine.getVertex(1));
	// c = drawVertex(triangleMesh, pLine.getVertex(2));
	// d = drawVertex(triangleMesh, pLine.getVertex(3));
	//
	// triangleMesh.addTriangle(new Triangle(a, b, c));
	// triangleMesh.addTriangle(new Triangle(a, c, d));
	//
	// triangleMesh.computeTriangleNormals();
	// triangleMesh.computeVertexNormals();
	//
	// triangleMesh.getMaterial().setShaderId(Material.SHADER_GOURAUD_SHADING);
	// triangleMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR0));
	// getCgRootNode().addChild(new CgNode(triangleMesh, "testmesh"));
	// }
	// else
	// {
	// System.out.println("!==! Vertex count is " + pLine.getVertexCount());
	// }
	// }
	//
	// private int drawVertex(ITriangleMesh triangleMesh, DXFVertex vertex)
	// {
	// return triangleMesh
	// .addVertex(new Vertex(VectorMatrixFactory.newIVector3(vertex.getX(),
	// vertex.getY(), vertex.getZ())));
	// }
}

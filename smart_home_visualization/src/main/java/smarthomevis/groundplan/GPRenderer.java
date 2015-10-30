package smarthomevis.groundplan;

import java.util.List;

import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.helpers.Point;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.material.Material;

public class GPRenderer
{
	/**
	 * takes a list of DXFLines and returns a LineSegments object containing
	 * representations of all these lines
	 * 
	 * @param unitElements
	 *          the List containing all the DXFLines to be represented
	 * @return a LineSegments object containing representations of all delivered
	 *         DXFLines
	 */
	protected LineSegments renderGPUnit(List<DXFLine> unitElements)
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
			unit = renderLine(unit, index, line);
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
	protected LineSegments renderLine(LineSegments segments, int index, DXFLine line)
	{
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();

		segments.addPoint(VectorMatrixFactory.newIVector3(startPoint.getX(), startPoint.getY(), startPoint.getZ()));
		segments.addPoint(VectorMatrixFactory.newIVector3(endPoint.getX(), endPoint.getY(), endPoint.getZ()));

		segments.addLine(index, index + 1);
		return segments;
	}
}

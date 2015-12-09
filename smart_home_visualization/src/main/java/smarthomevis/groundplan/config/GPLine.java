package smarthomevis.groundplan.config;

import java.io.Serializable;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector3;

/**
 * Zweidimensionale Definition einer Wand. Eine interne Representation der
 * Linien zur Darstellung eines Grundrisses
 * 
 * 
 * @author Leonard Opitz
 *		
 */
public class GPLine implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1039885046114959309L;
	
	private String name = null;
	private IVector3 start = null;
	private IVector3 end = null;
	private LineType lineType = null;
	
	public GPLine(String name, IVector3 startPoint, IVector3 endPoint)
	{
		this.name = name;
		this.start = startPoint;
		this.end = endPoint;
		this.lineType = LineType.WALL;
	}
	
	public String getName()
	{
	return name;
	}
	
	public LineType getLineType()
	{
	return lineType;
	}
	
	public void setLineType(LineType type)
	{
	this.lineType = type;
	}
	
	public IVector3 getStart()
	{
	return start;
	}
	
	public IVector3 getEnd()
	{
	return end;
	}
	
	public IVector3 getScaledStart(double scale)
	{
	return new Vector3(start.get(0) * scale, start.get(1) * scale,
		start.get(2) * scale);
	}
	
	public IVector3 getScaledEnd(double scale)
	{
		return new Vector3(end.get(0) * scale, end.get(1) * scale,
				end.get(2) * scale);
	}
	
	public String toString()
	{
	return this.name + " | Start<" + this.start.toString(2) + "> End<"
		+ this.end.toString(2) + ">";
	}
	
	public enum LineType
	{
		WALL, DOOR, WINDOW;
	}
	
}

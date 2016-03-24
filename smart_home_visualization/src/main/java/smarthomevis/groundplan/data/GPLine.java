package smarthomevis.groundplan.data;

import java.io.Serializable;
import java.text.DecimalFormat;

import cgresearch.core.math.Vector;

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
	private Vector start = null;
	private Vector end = null;
	private LineType lineType = null;
	
	public GPLine(String name, Vector startPoint, Vector endPoint)
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
	
	public Vector getStart()
	{
	return new Vector(start.get(0), start.get(1), start.get(2));
	}
	
	public Vector getEnd()
	{
	return new Vector(end.get(0), end.get(1), end.get(2));
	}
	
	public boolean equals(Object other)
	{
	if (!(other instanceof GPLine))
		return false;
		
	GPLine otherLine = (GPLine) other;
	if (!this.getName().equals(otherLine.getName()))
		return false;
		
	if (!this.getLineType().equals(otherLine.getLineType()))
		return false;
		
	if (!this.getStart().equals(otherLine.getStart()))
		return false;
		
	if (!this.getEnd().equals(otherLine.getEnd()))
		return false;
		
	return true;
	}
	
	public String toString()
	{
	return this.name + " | Start<" + getShortVectorString(this.start) + "> End<"
		+ getShortVectorString(this.end) + ">";
	}
	
	private String getShortVectorString(Vector v)
	{
	DecimalFormat format = new DecimalFormat("#.##");
	return format.format(v.get(0)) + ":" + format.format(v.get(1)) + ":"
		+ format.format(v.get(2));
	}
	
	public enum LineType
	{
		WALL, DOOR, WINDOW;
	}
	
	public GPLine clone()
	{
	GPLine gpLine = new GPLine(new String(name),
		new Vector(new Double(start.get(0)), new Double(start.get(1)),
			new Double(start.get(2))),
		new Vector(new Double(end.get(0)), new Double(end.get(1)),
			new Double(end.get(2))));
	gpLine.setLineType(lineType);
	return gpLine;
	}
}

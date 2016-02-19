package smarthomevis.groundplan.config;

import java.io.Serializable;

import cgresearch.core.math.Vector;
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
	return new Vector(start.get(0), start.get(1),
		start.get(2));
	}
	
	public Vector getEnd()
	{
	return new Vector(end.get(0), end.get(1),
		end.get(2));
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

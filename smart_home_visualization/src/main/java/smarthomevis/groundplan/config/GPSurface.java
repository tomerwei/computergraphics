package smarthomevis.groundplan.config;

import java.io.Serializable;

import cgresearch.core.math.IVector3;

/**
 * Twodimensional representation of a surface (one side/face of a wall)
 * consisting of two points building a line
 * 
 * 
 * @author Leonard.Opitz
 *
 */
public class GPSurface implements Serializable
	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1039885046114959309L;

	private String name = null;
	private IVector3 start = null;
	private IVector3 end = null;
	private Facing face = null;

	public GPSurface(String name, IVector3 startPoint, IVector3 endPoint)
		{
		this.name = name;
		this.start = startPoint;
		this.end = endPoint;
		this.face = Facing.INSIDE;
		}

	public String getName()
		{
		return name;
		}

	public Facing getFace()
		{
		return face;
		}

	public void setFacing(Facing face)
		{
		this.face = face;
		}

	public IVector3 getStart()
		{
		return start;
		}

	public IVector3 getEnd()
		{
		return end;
		}

	public String toString()
		{
		return this.name + " | Start<" + this.start.toString(2) + "> End<"
			+ this.end.toString(2) + ">";
		}

	public enum Facing
		{
		OUTSIDE, INSIDE;
		}

	}

package smarthomevis.groundplan.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cgresearch.core.math.IVector3;

public class GPDataType implements Serializable
{
	// holds all surfaces and maps each to a name string
	private Map<String, GPSurface> surfaces = null;
	// combines Surfaces to logical Walls
	private Map<String, List<String>> walls = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3152982897295763804L;

	public GPDataType()
	{
		this.surfaces = new HashMap<>();
		this.walls = new HashMap<>();
	}

	/*
	 * Getters
	 */

	public GPSurface getSurface(String name)
	{
		return this.surfaces.get(name);
	}

	public List<String> getWall(String id)
	{
		return this.walls.get(id);
	}

	public Map<String, List<GPSurface>> getWalls()
	{
		Map<String, List<GPSurface>> result = new HashMap<>();
		for (Entry<String, List<String>> e : this.walls.entrySet())
		{
			List<GPSurface> wall = new ArrayList<>();
			for (String name : e.getValue())
			{
				wall.add(this.surfaces.get(name));
			}
			result.put(e.getKey(), wall);
		}

		return result;
	}

	public List<List<GPSurface>> getWallsList()
	{
		List<List<GPSurface>> result = new ArrayList<>();
		for (Entry<String, List<String>> e : this.walls.entrySet())
		{
			List<GPSurface> wall = new ArrayList<>();
			for (String name : e.getValue())
			{
				wall.add(this.surfaces.get(name));
			}
			result.add(wall);
		}
		return result;
	}

	public void addSurface(String id, GPSurface surface)
	{
		this.surfaces.put(id, surface);
	}

	public void addWall(String id, List<String> surfaceList)
	{
		this.walls.put(id, surfaceList);
	}

	/*
	 * Setters
	 */
	/**
	 * Twodimensional representation of a surface (one side/face of a wall)
	 * consisting of two points building a line
	 * 
	 * 
	 * @author Leonard.Opitz
	 *
	 */
	public class GPSurface
	{
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

	}

	public enum Facing
	{
		OUTSIDE, INSIDE;
	}
}

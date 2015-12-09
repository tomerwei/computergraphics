package smarthomevis.groundplan.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Diese Klasse ist ein Transport- und Persistenzobjekt fuer die Elemente eines
 * zu rendernden Grundrisses.
 * 
 * @author Leonard Opitz
 *		
 */
public class GPDataType implements Serializable
{
	// Speicherung von GPLine-Objekten, addressiert ueber eindeutige Namen
	private Map<String, GPLine> lines = null;
	// Ordnet die GPLine-Objekte anhand ihres eindeutigen Namens einem Layer zu
	private Map<String, List<String>> layers = null;
	
	// die Konfigurationsparameter fuer das Rendern der einzelnen Elemente
	private double wall_top_height = 0.0;
	private double window_bottom_height = 0.0;
	private double window_top_height = 0.0;
	private double door_top_height = 0.0;
	
	private double scaling_scalar = 1.0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3152982897295763804L;
	
	public GPDataType()
	{
		this.lines = new HashMap<>();
		this.layers = new HashMap<>();
	}
	
	/*
	 * Getters
	 */
	
	public synchronized double getWallTopHeight()
	{
	return wall_top_height;
	}
	
	public synchronized double getWindowBottomHeight()
	{
	return window_bottom_height;
	}
	
	public synchronized double getWindowTopHeight()
	{
	return window_top_height;
	}
	
	public synchronized double getDoorTopHeight()
	{
	return door_top_height;
	}
	
	public synchronized double getScalingScalar()
	{
	return scaling_scalar;
	}
	
	public GPLine getLine(String name)
	{
	return this.lines.get(name);
	}
	
	public List<String> getLayer(String id)
	{
	return this.layers.get(id);
	}
	
	public Map<String, List<GPLine>> getLayers()
	{
	Map<String, List<GPLine>> result = new HashMap<>();
	for (Entry<String, List<String>> e : this.layers.entrySet())
		{
		List<GPLine> layerLineList = new ArrayList<>();
		for (String name : e.getValue())
			{
			layerLineList.add(this.lines.get(name));
			}
		result.put(e.getKey(), layerLineList);
		}
		
	return result;
	}
	
	public List<List<GPLine>> getLayerLists()
	{
	List<List<GPLine>> result = new ArrayList<>();
	for (Entry<String, List<String>> e : this.layers.entrySet())
		{
		List<GPLine> wall = new ArrayList<>();
		for (String name : e.getValue())
			{
			wall.add(this.lines.get(name));
			}
		result.add(wall);
		}
	return result;
	}
	
	/*
	 * Setters
	 */
	
	public synchronized void setWallTopHeight(double wall_top_height)
	{
	this.wall_top_height = wall_top_height;
	}
	
	public synchronized void setWindowBottomHeight(double window_bottom_height)
	{
	this.window_bottom_height = window_bottom_height;
	}
	
	public synchronized void setWindowTopHeight(double window_top_height)
	{
	this.window_top_height = window_top_height;
	}
	
	public synchronized void setDoorTopHeight(double door_top_height)
	{
	this.door_top_height = door_top_height;
	}
	
	public synchronized void setScalingScalar(double scaling_scalar)
	{
	this.scaling_scalar = scaling_scalar;
	}
	
	public void addLine(String id, GPLine line)
	{
	this.lines.put(id, line);
	}
	
	public void addWall(String id, List<String> lineList)
	{
	this.layers.put(id, lineList);
	}
	
	/*
	 * 
	 * Helpers
	 */
	
	public String toString()
	{
	StringBuffer buffy = new StringBuffer();
	
	for (Entry<String, List<GPLine>> e : getLayers().entrySet())
		{
		buffy.append("Layer " + e.getKey() + "\n");
		for (GPLine s : e.getValue())
			{
			buffy.append("\t" + s.toString() + "\n");
			}
		}
		
	return buffy.toString();
	}
	
}

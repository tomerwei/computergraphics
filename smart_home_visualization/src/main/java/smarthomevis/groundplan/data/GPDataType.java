package smarthomevis.groundplan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import smarthomevis.groundplan.config.GPConfig;

/**
 * Diese Klasse ist ein Transport- und Persistenzobjekt fuer die Elemente eines
 * zu rendernden Grundrisses.
 * 
 * @author Leonard Opitz
 * 
 */
public class GPDataType implements Serializable
{
	// die aus der XML Datei ausgelesenen Konfigurationswerte
	private GPConfig config = null;
	// Speicherung von GPLine-Objekten, addressiert ueber eindeutige Namen
	private Map<String, GPLine> lines = null;
	// Ordnet die GPLine-Objekte anhand ihres eindeutigen Namens einem Layer zu
	private Map<String, List<String>> layers = null;
	// Ordnet die gefundenen Linienpaaren wieder den entsprechenden Layers zu
	private Map<String, List<String[]>> linePairsPerLayer = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3152982897295763804L;

	public GPDataType()
	{
		this.lines = new HashMap<>();
		this.layers = new HashMap<>();
		this.linePairsPerLayer = new HashMap<>();
	}

	public GPDataType(GPConfig config)
	{
		this.config = config;
		this.lines = new HashMap<>();
		this.layers = new HashMap<>();
		this.linePairsPerLayer = new HashMap<>();
	}

	/*
	 * Getters
	 */
	public GPConfig getGPConfig()
	{
		return this.config;
	}

	public GPLine getLine(String name)
	{
		return this.lines.get(name);
	}

	public List<String> getLayer(String id)
	{
		return this.layers.get(id);
	}

	public String getLayerOfLine(String lineName)
	{
		for (String layerName : layers.keySet())
		{
			if (layers.get(layerName).contains(lineName))
			return layerName;
		}

		return "none";
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

	public Map<String, List<GPLine[]>> getGPLinePairsPerLayerMap()
	{
		Map<String, List<GPLine[]>> gplinePairsPerLayer = new HashMap<>();

		for (Entry<String, List<String[]>> e : linePairsPerLayer.entrySet())
		{
			String key = e.getKey();

			gplinePairsPerLayer.put(key, getGPLinePairsOfLayer(key));
		}

		return gplinePairsPerLayer;
	}

	public List<GPLine[]> getGPLinePairsOfLayer(String layerName)
	{
		List<GPLine[]> pairList = new ArrayList<>();
		for (String[] sArray : this.linePairsPerLayer.get(layerName))
		{
			GPLine[] lineArray =
			{ this.getLine(sArray[0]), this.getLine(sArray[1]) };
			pairList.add(lineArray);
		}

		return pairList;
	}

	/*
	 * Setters
	 */
	public void setGPConfig(GPConfig config)
	{
		this.config = config;
	}

	public void addLine(String id, GPLine line)
	{
		this.lines.put(id, line);
	}

	public void addWall(String id, List<String> lineList)
	{
		this.layers.put(id, lineList);
	}

	public void addPairToLayer(String layerName, String[] linePair)
	{
		if (!this.linePairsPerLayer.containsKey(layerName))
		this.linePairsPerLayer.put(layerName, new ArrayList<String[]>());

		this.linePairsPerLayer.get(layerName).add(linePair);
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

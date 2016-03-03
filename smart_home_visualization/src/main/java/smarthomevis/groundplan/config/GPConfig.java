package smarthomevis.groundplan.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import smarthomevis.groundplan.data.GPLine.LineType;

public class GPConfig implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3014243451469832259L;

	public static final String WALL_TOP_HEIGHT = "wall_top_height";
	public static final String DOOR_TOP_HEIGHT = "door_top_height";
	public static final String WINDOW_TOP_HEIGHT = "window_top_height";
	public static final String WINDOW_BOTTOM_HEIGHT = "window_bottom_height";
	public static final String GROUNDPLAN_SCALING_FACTOR = "groundplan_scaling_scalar";
	public static final String ANGLE_TOLERANCE = "angle_tolerance";
	public static final String DISTANCE_INTERVAL = "distance_interval";
	public static final String LOWER_WALLTHICKNESS_LIMIT = "lower_wallthickness_limit";
	public static final String UPPER_WALLTHICKNESS_LIMIT = "upper_wallthickness_limit";
	public static final String WALL_COUNT_THRESHOLD = "wall_count_threshold";

	private Map<String, LineType> layers = null;

	private Map<String, Double> configValues = null;

	public GPConfig()
	{
		this.layers = new HashMap<>();
		this.configValues = new HashMap<>();
	}

	public void addLayer(String name, LineType type)
	{
		this.layers.put(name, type);
	}

	public Map<String, LineType> getLayers()
	{
		return this.layers;
	}

	public void addConfigValue(String key, Double value)
	{
		this.configValues.put(key, value);
	}

	public List<String> getConfigKeys()
	{
		List<String> resultList = new ArrayList<>();
		for (Entry<String, Double> e : this.configValues.entrySet())
		{
			resultList.add(e.getKey());
		}

		return resultList;
	}

	public synchronized Double getValue(String key)
	{
		return this.configValues.get(key);
	}

	public Map<String, Double> getConfigMap()
	{
		return this.configValues;
	}

	public String toString()
	{
		StringBuffer buffy = new StringBuffer();
		buffy.append("=== ConfigData ===\n");
		buffy.append("## Layers ##\n");
		for (Entry<String, LineType> e : layers.entrySet())
		{
			buffy.append("Layer " + e.getKey() + " of type " + e.getValue().toString() + "\n");
		}

		buffy.append("\n## Values ##\n");
		for (Entry<String, Double> e : configValues.entrySet())
		{
			buffy.append(e.getKey() + ": " + String.valueOf(e.getValue()) + "\n");
		}
		buffy.append("\n=== ========== ===\n");
		return buffy.toString();
	}

}

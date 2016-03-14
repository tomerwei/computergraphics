package smarthomevis.groundplan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GPSolid implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033966425155890760L;

	private Map<String, GPLine> linesOfSolid;

	private GPLine[] basePair = null;

	public GPSolid()
	{
		this.linesOfSolid = new HashMap<>();
	}

	public void addLine(GPLine line)
	{
		this.linesOfSolid.put(line.getName(), line);
	}

	public void setBasePair(GPLine[] pair)
	{
		this.basePair = pair;
	}

	public GPLine getLine(String name)
	{
		return this.linesOfSolid.get(name);
	}

	public GPLine[] getBasePair()
	{
		return this.basePair;
	}

	public List<GPLine> getLineList()
	{
		List<GPLine> resultList = new ArrayList<>();
		for (Entry<String, GPLine> e : this.linesOfSolid.entrySet())
		{
			resultList.add(e.getValue());
		}
		return resultList;
	}

	public Map<String, GPLine> getLineMap()
	{
		return this.linesOfSolid;
	}

	public String toString()
	{
		StringBuffer buffy = new StringBuffer();
		buffy.append("Solid<");
		for (Entry<String, GPLine> e : this.linesOfSolid.entrySet())
		{
			buffy.append(e.getKey() + ", ");
		}

		String result = buffy.substring(0, buffy.length() - 2);

		return result + ">";
	}
}

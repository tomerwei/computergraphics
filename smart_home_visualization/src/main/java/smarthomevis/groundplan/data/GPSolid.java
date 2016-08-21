package smarthomevis.groundplan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cgresearch.core.math.Vector;

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

	public List<Vector> getBaseLinesVectorList()
	{
		List<Vector> vectorList = new ArrayList<>();

		for (int i = 0; i < this.basePair.length; i++)
		{
			GPLine l = this.basePair[i];

			vectorList.add(l.getStart());
			vectorList.add(l.getEnd());
		}

		return vectorList;
	}

	public int hashCodeVectorSum()
	{
		int hash = 1;
		
		double v_x_Sum = 1.0;
		double v_y_Sum = 1.0;
		double v_z_Sum = 1.0;

		for (Vector v : getBaseLinesVectorList())
			{
			v_x_Sum = v_x_Sum + new Double(v.get(0)).intValue();
			v_y_Sum = v_y_Sum + new Double(v.get(1)).intValue();
			v_z_Sum = v_z_Sum + new Double(v.get(2)).intValue();
			}

		hash = hash * 17 + Double.hashCode(v_x_Sum);
		hash = hash * 31 + Double.hashCode(v_y_Sum);
		hash = hash * 13 + Double.hashCode(v_z_Sum);

		return hash;
	}

	public boolean equalsInVectors(Object o)
	{
		if (!(o instanceof GPSolid))
			return false;

		GPSolid other = (GPSolid) o;

		if (this.hashCodeVectorSum() != other.hashCodeVectorSum())
			return false;

		return true;
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

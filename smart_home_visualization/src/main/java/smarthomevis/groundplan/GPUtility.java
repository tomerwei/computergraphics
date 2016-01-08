package smarthomevis.groundplan;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector3;
import smarthomevis.groundplan.config.GPLine;

public class GPUtility
{
	
	public static IVector3 kreuzproduktVon(IVector3 a, IVector3 b)
	{
	double r_x = (a.get(1) * b.get(2)) - (a.get(2) * b.get(1));
	double r_y = (a.get(2) * b.get(0)) - (a.get(0) * b.get(2));
	double r_z = (a.get(0) * b.get(1)) - (a.get(1) * b.get(0));
	
	return new Vector3(r_x, r_y, r_z);
	}
	
	public static List<GPLine> cloneList(List<GPLine> lineList)
	{
	List<GPLine> cloneList = new ArrayList<>();
	for (GPLine l : lineList)
		{
		cloneList.add(l);
		}
	return cloneList;
	}
	
	public static IVector3 normalizeVector(IVector3 vector)
	{
	double length = calcVectorLength(vector);
	return new Vector3(vector.get(0) / length, vector.get(1) / length,
		vector.get(2) / length);
	}
	
	public static double calcVectorLength(IVector3 vector)
	{
	return Math.sqrt(Math.pow(vector.get(0), 2.0) + Math.pow(vector.get(1), 2.0)
		+ Math.pow(vector.get(2), 2.0));
	}
	
	public static IVector3 substractOtherVector(IVector3 vector, IVector3 other)
	{
	return new Vector3(vector.get(0) - other.get(0),
		vector.get(1) - other.get(1), vector.get(2) - other.get(2));
	}
	
	// FIXME In Grundriss_Haus_02.dxf is scheinbar eine Line ungenau definiert.
	// Der normierte Richtungsvektor lautet (1.00000, 0.00050, 0.00000)
	// anstatt (1.00000, 0.00000, 0.00000), weshalb eine zusaetzliche Liste fuer
	// die Linie
	// angelegt wird
	public static double roundDown3(double d)
	{
	return (long) (d * 1e3) / 1e3;
	}
	
	public static void increaseDistanceCounter(Map<Double, Integer> distanceMap,
		double distance)
	{
	int currentCount = distanceMap.get(distance);
	distanceMap.put(distance, currentCount + 1);
	}
	
	public static void printDistanceMap(Map<Double, Integer> distanceMap)
	{
	System.out.println("=========== DISTANCES ===========");
	System.out.println("Distance\tCount");
	for (Entry<Double, Integer> e : distanceMap.entrySet())
		{
		System.out.println(e.getKey() + "\t\t" + e.getValue());
		}
	}
	
	public static void saveDistanceMapToCSVFile(
		Map<Double, Integer> distanceMap)
	{
	Charset utf8 = StandardCharsets.UTF_8;
	List<String> lines = new ArrayList<>();
	for (Entry<Double, Integer> e : distanceMap.entrySet())
		{
		lines.add(e.getKey() + "\t" + e.getValue() + "\t");
		}
		
	try
		{
		Files.write(Paths.get("distances.csv"), lines, utf8);
		}
	catch (IOException e)
		{
		e.printStackTrace();
		}
		
	}
}

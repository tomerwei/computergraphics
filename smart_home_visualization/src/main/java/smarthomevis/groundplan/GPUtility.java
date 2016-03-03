package smarthomevis.groundplan;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cgresearch.core.math.Vector;
import smarthomevis.groundplan.data.GPLine;
import cgresearch.core.math.Vector;

public class GPUtility
{
	
	public static Vector kreuzproduktVon(Vector a, Vector b)
	{
	BigDecimal aX = BigDecimal.valueOf(a.get(0));
	BigDecimal aY = BigDecimal.valueOf(a.get(1));
	BigDecimal aZ = BigDecimal.valueOf(a.get(2));
	
	BigDecimal bX = BigDecimal.valueOf(b.get(0));
	BigDecimal bY = BigDecimal.valueOf(b.get(1));
	BigDecimal bZ = BigDecimal.valueOf(b.get(2));
	
	double r_x = ((aY.multiply(bZ)).subtract(aZ.multiply(bY))).doubleValue();
	double r_y = ((aZ.multiply(bX)).subtract(aX.multiply(bZ))).doubleValue();
	double r_z = ((aX.multiply(bY)).subtract(aY.multiply(bX))).doubleValue();
	
	return new Vector(r_x, r_y, r_z);
	}
	
	public static double punktproduktVon(Vector a, Vector b)
	{
	BigDecimal aX = BigDecimal.valueOf(a.get(0));
	BigDecimal aY = BigDecimal.valueOf(a.get(1));
	BigDecimal aZ = BigDecimal.valueOf(a.get(2));
	
	BigDecimal bX = BigDecimal.valueOf(b.get(0));
	BigDecimal bY = BigDecimal.valueOf(b.get(1));
	BigDecimal bZ = BigDecimal.valueOf(b.get(2));
	
	BigDecimal x = aX.multiply(bX);
	BigDecimal y = aY.multiply(bY);
	BigDecimal z = aZ.multiply(bZ);
	BigDecimal result = x.add(y).add(z);
	
	return result.doubleValue();
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
	
	public static Vector normalizeVector(Vector vector)
	{
	BigDecimal length = BigDecimal.valueOf(calcVectorLength(vector));
	// System.out.println("Length: " + length);
	BigDecimal vX = BigDecimal.valueOf(vector.get(0));
	BigDecimal vY = BigDecimal.valueOf(vector.get(1));
	BigDecimal vZ = BigDecimal.valueOf(vector.get(2));
	
	// System.out.println("vX: " + vX + "; vY: " + vY + "; vZ: " + vZ);
	
	BigDecimal x = vX.divide(length, 5, RoundingMode.HALF_DOWN);
	BigDecimal y = vY.divide(length, 5, RoundingMode.HALF_DOWN);
	BigDecimal z = vZ.divide(length, 5, RoundingMode.HALF_DOWN);
	
	// System.out.println(vX + "/" + length + " = " + x + "; " + vY + "/" +
	// length
	// + " = " + y + "; " + vZ + "/" + length + " = " + z);
	return new Vector(x.doubleValue(), y.doubleValue(), z.doubleValue());
	}
	
	public static double calcVectorLength(Vector vector)
	{
	return Math.sqrt(Math.pow(vector.get(0), 2.0) + Math.pow(vector.get(1), 2.0)
		+ Math.pow(vector.get(2), 2.0));
	}
	
	public static Vector substractOtherVector(Vector vector, Vector other)
	{
	BigDecimal vX = BigDecimal.valueOf(vector.get(0));
	BigDecimal vY = BigDecimal.valueOf(vector.get(1));
	BigDecimal vZ = BigDecimal.valueOf(vector.get(2));
	BigDecimal oX = BigDecimal.valueOf(other.get(0));
	BigDecimal oY = BigDecimal.valueOf(other.get(1));
	BigDecimal oZ = BigDecimal.valueOf(other.get(2));
	
	return new Vector((vX.subtract(oX)).doubleValue(),
		(vY.subtract(oY)).doubleValue(), (vZ.subtract(oZ).doubleValue()));
	}
	
	public static double roundDown3(double d)
	{
	return (long) (d * 1e3) / 1e3;
	}
	
	public static double angleBetweenVectors(Vector vector, Vector other)
	{
	BigDecimal tmp_a = BigDecimal.valueOf(punktproduktVon(vector, other));
	
	// System.out.println("Punktprodukt von " + vector.toString(2) + " � "
	// + other.toString(2) + " = " + tmp_a);
	
	BigDecimal lengthOfVector = BigDecimal.valueOf(calcVectorLength(vector));
	BigDecimal lengthOfOther = BigDecimal.valueOf(calcVectorLength(other));
	
	// System.out
	// .println("Length of " + vector.toString(2) + " = " + lengthOfVector
	// + "\nLength of " + other.toString(2) + " = " + lengthOfOther);
	
	BigDecimal tmp_b = lengthOfVector.multiply(lengthOfOther);
	
	// System.out.println(lengthOfVector + " * " + lengthOfOther + " = " +
	// tmp_b);
	
	BigDecimal tmpResult = tmp_a.divide(tmp_b, 5, RoundingMode.HALF_DOWN);
	
	double winkelBogenmass = Math.acos(tmpResult.doubleValue());
	
	return bogenmassZuGrad(winkelBogenmass);
	}
	
	private static double bogenmassZuGrad(double value)
	{
	BigDecimal threesixty = BigDecimal.valueOf(360.0);
	
	BigDecimal twoTimesPi = BigDecimal.valueOf(2)
		.multiply(BigDecimal.valueOf(Math.PI));
		
	BigDecimal result = threesixty.divide(twoTimesPi, 5, RoundingMode.HALF_DOWN)
		.multiply(BigDecimal.valueOf(value));
		
	return result.doubleValue();
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
	lines.add("Abstand\tMenge\t");
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

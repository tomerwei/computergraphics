package smarthomevis.groundplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector3;
import cgresearch.graphics.bricks.CgApplication;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPDataType;
import smarthomevis.groundplan.config.GPLine;

public class Test extends CgApplication
{
	
	public void calculateDistancesInPlan(GPDataType type)
	{
	List<List<GPLine>> layerList = type.getLayerLists();
	
	Map<IVector3, List<GPLine>> directionMap = new HashMap<>();
	for (List<GPLine> linesOfLayerList : layerList)
		{
		for (GPLine line : linesOfLayerList)
			{
			IVector3 dirVector = GPUtility.substractOtherVector(line.getEnd(),
				line.getStart());
			IVector3 normDirVector = GPUtility.normalizeVector(dirVector);
			IVector3 contraNormDirVector = new Vector3(
				normDirVector.get(0) * (-1), normDirVector.get(1) * (-1),
				normDirVector.get(2) * (-1));
				
			if (directionMap.containsKey(normDirVector))
				{
				directionMap.get(normDirVector).add(line);
				System.out.print("+");
				}
			else if (directionMap.containsKey(contraNormDirVector))
				{
				directionMap.get(contraNormDirVector).add(line);
				System.out.print("-");
				}
			else
				{
				System.out.println(
					"\nAdding new Vector-Key: " + normDirVector.toString(5));
				directionMap.put(normDirVector, new ArrayList<GPLine>());
				directionMap.get(normDirVector).add(line);
				}
			}
		}
		
	for (Entry<IVector3, List<GPLine>> e : directionMap.entrySet())
		System.out.println("\nMap of " + e.getKey().toString(5) + " contains "
			+ e.getValue().size() + " lines");
			
	Map<Double, Integer> distanceMap = countAllDistances(directionMap);
	
	GPUtility.printDistanceMap(distanceMap);
	GPUtility.saveDistanceMapToCSVFile(distanceMap);
	
	}
	
	private Map<Double, Integer> countAllDistances(
		Map<IVector3, List<GPLine>> directionMap)
	{
	Map<Double, Integer> distanceMap = new HashMap<>();
	
	for (Entry<IVector3, List<GPLine>> e : directionMap.entrySet())
		{
		// zur Vermeidung von Problemen bei nebenläufigen Zugriffen (Löschen der
		// Einträge innerhalb der Iteration) eine Kopie der Liste verwenden
		List<GPLine> lineListCopy = GPUtility.cloneList(e.getValue());
		
		for (GPLine line : e.getValue())
			{
			lineListCopy.remove(line);
			
			for (GPLine other : lineListCopy)
				{
				double parallelOverlap = calculateParallelOverlapOf(line,
					other);
					
				double dist = 0.0;
				if (parallelOverlap > 0.0)
					dist = GPUtility.roundDown3(distanceBetween(line, other));
					
				if (dist > 0.0)
					{
					if (!distanceMap.containsKey(dist))
						distanceMap.put(dist, 0);
						
					GPUtility.increaseDistanceCounter(distanceMap, dist);
					
					// TODO speichern der gefundenen zueinander gehoerenden
					// Liniengruppen
					}
				}
				
			}
			
		}
		
	return distanceMap;
	}
	
	private double distanceBetween(GPLine line, GPLine other)
	{
	IVector3 dirVector = GPUtility.substractOtherVector(line.getEnd(),
		line.getStart());
	IVector3 tempVector = GPUtility.substractOtherVector(other.getStart(),
		line.getStart());
		
	IVector3 distanceVector = GPUtility.kreuzproduktVon(dirVector, tempVector);
	
	double temp = GPUtility.calcVectorLength(distanceVector);
	double temp2 = GPUtility.calcVectorLength(dirVector);
	
	return temp / temp2;
	}
	
	private double calculateParallelOverlapOf(GPLine line, GPLine other)
	{
	IVector3 dirVec_line = GPUtility.substractOtherVector(line.getEnd(),
		line.getStart());
		
	double beta = (dirVec_line.get(0) * dirVec_line.get(0))
		+ (dirVec_line.get(1) * dirVec_line.get(1))
		+ (dirVec_line.get(2) * dirVec_line.get(2));
		
	IVector3 pVec_0 = GPUtility.substractOtherVector(other.getStart(),
		line.getStart());
		
	double lambda_other_0 = (pVec_0.get(0) * dirVec_line.get(0))
		+ (pVec_0.get(1) * dirVec_line.get(1))
		+ (pVec_0.get(2) * dirVec_line.get(2));
		
	IVector3 pVec_1 = GPUtility.substractOtherVector(other.getEnd(),
		line.getStart());
		
	double lambda_other_1 = (pVec_1.get(0) * dirVec_line.get(0))
		+ (pVec_1.get(1) * dirVec_line.get(1))
		+ (pVec_1.get(2) * dirVec_line.get(2));
		
	// berechnetes Intervall untersuchen
	Double lowerLimit = null;
	Double upperLimit = null;
	boolean isEmptyInterval = false;
	
	// Reihenfolge der Intervallgrenzen pruefen
	if (lambda_other_1 < lambda_other_0)
		{
		// Werte tauschen
		double temp = lambda_other_1;
		lambda_other_1 = lambda_other_0;
		lambda_other_0 = temp;
		}
		
	// Untergrenze bestimmen
	if (lambda_other_0 <= 0.0)
		lowerLimit = 0.0;
	else
		{
		if (lambda_other_0 > beta)
			isEmptyInterval = true;
		else
			lowerLimit = lambda_other_0;
		}
		
	// Obergrenze bestimmen
	if (lambda_other_1 >= beta)
		upperLimit = beta;
	else
		{
		if (lambda_other_1 < 0.0)
			isEmptyInterval = true;
		else
			upperLimit = lambda_other_1;
		}
		
	if (isEmptyInterval || lowerLimit == null || upperLimit == null)
		return 0.0;
		
	return upperLimit - lowerLimit;
	}
	
	/*
	 *
	 * ==============================================================
	 */
	
	public Test()
	{
		// System.out.println("Math.pow(3, 2) = " + Math.pow(3.0, 2.0));
		// System.out.println("Math.pow(4, 2) = " + Math.pow(4.0, 2.0));
		//
		// System.out.println("5.5 * (-1) = " + 5.5 * (-1));
		
		// System.out.println("1.00050: " + roundDown3(1.00050));
		// System.out.println("-1.00050: " + roundDown3(-1.00050));
		// System.out.println("-1.00350: " + roundDown3(-1.00350));
		
		// IVector3 alpha_0 = new Vector3(0.0, 2.0, 0.0);
		// IVector3 alpha_1 = new Vector3(0.0, 5.0, 0.0);
		// IVector3 beta_0 = new Vector3(1.6, 2.0, 0.0);
		// IVector3 beta_1 = new Vector3(1.6, 5.0, 0.0);
		//
		// GPLine A = new GPLine("Ahhh", alpha_0, alpha_1);
		// GPLine B = new GPLine("Beeh", beta_0, beta_1);
		//
		// System.out.println("Distance between " + A.getName() + " and "
		// + B.getName() + " is " + roundDown3(distanceBetween(A, B)));
		
	}
	
	public static void main(String[] args)
	{
	ResourcesLocator.getInstance().parseIniFile("resources.ini");
	JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
	CgApplication app = new Test();
	appLauncher.create(app);
	appLauncher.setRenderSystem(RenderSystem.JOGL);
	appLauncher.setUiSystem(UI.JOGL_SWING);
	
	Converter converter = new Converter();
//	GPDataType renderData = converter.importData("dxf/TestRaum.dxf",
//		"dxf/TestRaum.xml");
	GPDataType renderData = converter.importData("dxf/Grundriss_Haus_02.dxf",
		"dxf/Grundriss_Haus_02.xml");
	// GPDataType renderData = converter.importData("dxf/4H-HORA Projekt1.dxf",
	// "dxf/4H-HORA Projekt1.xml");
	new Test().calculateDistancesInPlan(renderData);
	}
	
}

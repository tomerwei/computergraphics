package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cgresearch.core.math.IVector3;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPDataType;
import smarthomevis.groundplan.config.GPLine;

public class GPAnalyzer
{
	public GPDataType analyzeAndProcessData(GPDataType data)
	{
	
	calculateDistancesInPlan(data);
	// ergaenzende Linien zum Abschluss von Wandsegmenten sollten hier in den
	// GPDataType eingefuegt werden
	
	return data;
	}
	
	public void calculateDistancesInPlan(GPDataType type)
	{
	Map<String, List<GPLine>> layerMap = type.getLayers();
	
	Map<IVector3, List<GPLine>> directionMap = new HashMap<>();
	for (Entry<String, List<GPLine>> entry : layerMap.entrySet())
		{
		// if (entry.getKey().equals("Ansichtskanten"))
		for (GPLine line : entry.getValue())
			{
			IVector3 dirVector = GPUtility.substractOtherVector(line.getEnd(),
				line.getStart());
			IVector3 normDirVector = GPUtility.normalizeVector(dirVector);
			
			double angle_tolerance = type.getGPConfig()
				.getValue(GPConfig.ANGLE_TOLERANCE);
				
			IVector3 listDirVector = testVectorFitsAngleOfDirVector(
				normDirVector, directionMap.keySet(), angle_tolerance);
				
			if (listDirVector != null)
				{
				directionMap.get(listDirVector).add(line);
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
			
	Map<Double, Integer> distanceMap = countAllDistances(type, directionMap);
	
	// GPUtility.printDistanceMap(distanceMap);
	GPUtility.saveDistanceMapToCSVFile(distanceMap);
	
	}
	
	public IVector3 testVectorFitsAngleOfDirVector(IVector3 normDirVector,
		Set<IVector3> listDirVectors, double tolerance)
	{
	System.out.println("testing Vector " + normDirVector.toString(2));
	
	// durch alle bereits gefundenen Richtungsvektoren der directionMap
	// durchiterieren und den Winkel bestimmen
	for (IVector3 vector : listDirVectors)
		{
		double angleOfNormDirVector = GPUtility
			.angleBetweenVectors(normDirVector, vector);
		if ((angleOfNormDirVector < tolerance) || (180.0 <= angleOfNormDirVector
			&& angleOfNormDirVector <= 180.0 + tolerance))
			{
			System.out.println("angle of Normdirvetor (" + angleOfNormDirVector
				+ ") inside tolerance to " + vector.toString(2));
			return vector;
			}
		}
		
	return null;
	}
	
	private Map<Double, Integer> countAllDistances(GPDataType type,
		Map<IVector3, List<GPLine>> directionMap)
	{
	Map<Double, Integer> distanceMap = new HashMap<>();
	distanceMap.put(0.0, 0);
	
	double distanceInterval = type.getGPConfig()
		.getValue(GPConfig.DISTANCE_INTERVAL);
		
	for (Entry<IVector3, List<GPLine>> e : directionMap.entrySet())
		{
		List<GPLine> lineListCopy = GPUtility.cloneList(e.getValue());
		
		for (GPLine line : e.getValue())
			{
			// zur Vermeidung von Problemen bei nebenläufigen Zugriffen
			// (Löschen der Einträge innerhalb der Iteration) eine Kopie der
			// Liste
			// verwenden
			lineListCopy.remove(line);
			
			for (GPLine other : lineListCopy)
				{
				double parallelOverlap = calculateParallelOverlapOf(line,
					other);
					
				double dist = 0.0;
				if (parallelOverlap > 0.0)
					dist = distanceBetween(line, other);
					
				if (dist > 0.0)
					{
					// den passenden Intervallwert fuer die aktuelle Distanz
					// ermitteln
					double distanceKey = calculateDistanceKey(dist,
						distanceInterval);
					if (!distanceMap.containsKey(distanceKey))
						{
						expandDistanceMapToDistanceKey(distanceMap, distanceKey,
							distanceInterval);
						// System.out.println("# new amount of distances is " +
						// distanceMap.size());
						}
						
					System.out.println("++ increasing count of key "
						+ distanceKey + " for line distance " + dist);
					increaseDistanceCounter(distanceMap, distanceKey);
					
					// TODO speichern der gefundenen zueinander gehoerenden
					// Liniengruppen
					}
				}
				
			}
			
		}
		
	return distanceMap;
	}
	
	private double calculateDistanceKey(double dist, double distanceInterval)
	{
	BigDecimal distance = BigDecimal.valueOf(dist);
	BigDecimal interval = BigDecimal.valueOf(distanceInterval);
	
	// den remainder (rest) von der Distanz abziehen, um einen glatten
	// Quotient zu erhalten
	BigDecimal remainder = distance.remainder(interval);
	
	BigDecimal tmpDistance = distance.subtract(remainder);
	
	BigDecimal distanceMultiplicator = tmpDistance.divide(interval, 5,
		RoundingMode.HALF_DOWN);
	// System.out.println("=== distance " + distance + "; interval " + interval
	// + "; remainder " + remainder
	// + ";\ntmpdistance = distance - remainder = " + distance + " - " +
	// remainder + " = " + tmpDistance
	// + "; distanceMultiplikator: " + distanceMultiplicator);
	BigDecimal distanceKey = distanceMultiplicator.multiply(interval);
	// System.out.println("distanceKey = " + distanceKey);
	return distanceKey.doubleValue();
	}
	
	private void expandDistanceMapToDistanceKey(
		Map<Double, Integer> distanceMap, double distanceKey,
		double distanceInterval)
	{
	// zunaechst den bisher hoechsten Key ermitteln
	double highestCurrentKey = findHighestCurrentDistanceKey(distanceMap);
	
	BigDecimal current = BigDecimal.valueOf(highestCurrentKey);
	BigDecimal target = BigDecimal.valueOf(distanceKey);
	BigDecimal interval = BigDecimal.valueOf(distanceInterval);
	
	BigDecimal currentQuotient = current.divide(interval, 5,
		RoundingMode.HALF_DOWN);
	BigDecimal targetQuotient = target.divide(interval, 5,
		RoundingMode.HALF_DOWN);
		
	double numberOfSteps = (targetQuotient.subtract(currentQuotient))
		.doubleValue();
		
	for (int i = 1; i <= numberOfSteps; i++)
		{
		double additionalInterval = highestCurrentKey + (i * distanceInterval);
		distanceMap.put(additionalInterval, 0);
		System.out.println(
			"## added distanceKey " + additionalInterval + " to distanceMap");
		}
		
	}
	
	private double findHighestCurrentDistanceKey(
		Map<Double, Integer> distanceMap)
	{
	// geht alle Keys durch und findet den hoechsten
	double currentHighest = 0.0;
	for (Entry<Double, Integer> e : distanceMap.entrySet())
		{
		if (e.getKey() > currentHighest)
			currentHighest = e.getKey();
		}
	return currentHighest;
	}
	
	public double distanceBetween(GPLine line, GPLine other)
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
	
	private void increaseDistanceCounter(Map<Double, Integer> distanceMap,
		double distance)
	{
	int currentCount = distanceMap.get(distance);
	distanceMap.put(distance, currentCount + 1);
	}
	
	public double calculateParallelOverlapOf(GPLine line, GPLine other)
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
	
	// System.out.println("beta: " + beta + "; lambda_other_0: " +
	// lambda_other_0
	// + "; lambda_other_1: " + lambda_other_1);
	
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
		
	// System.out.println("[" + lowerLimit + ";" + upperLimit + "]");
	
	return upperLimit - lowerLimit;
	}
	
}

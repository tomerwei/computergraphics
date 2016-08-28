package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import cgresearch.core.math.Vector;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.data.GPDataType;
import smarthomevis.groundplan.data.GPLine;

public class GPAnalyzer
{

	private Map<Double, Integer> distanceMap = new TreeMap<>();

	public Map<Double, List<String[]>> analyzeData(GPDataType data)
	{

		Map<Double, List<String[]>> pairsToDistanceMap = calculateDistancesInPlan(data);

		data.setDistanceMap(distanceMap);

		return pairsToDistanceMap;
	}

	public Map<Double, List<String[]>> calculateDistancesInPlan(GPDataType type)
	{
		Map<String, List<GPLine>> layerMap = type.getLayers();

		// Toleranzwert fuer Winkelabweichung; wird bereits hier geladen, um
		// Zugriffshaeufigkeit zu reduzieren
		double angle_tolerance = type.getGPConfig().getValue(GPConfig.ANGLE_TOLERANCE);

		Map<Vector, List<GPLine>> directionMap = new HashMap<>();
		// Anmerkung: alle Linien verschiedenen Ebenen werden hier unter den
		// Distanzen zusammengefasst!

		// fuer alle Ebenen
		for (Entry<String, List<GPLine>> entry : layerMap.entrySet())
		{
			// fuer alle Linien einer Ebene
			for (GPLine line : entry.getValue())
			{
				Vector dirVector = GPUtility.substractOtherVector(line.getEnd(), line.getStart());
				Vector normDirVector = GPUtility.normalizeVector(dirVector);

				Vector listDirVector = testVectorFitsAngleOfDirVector(normDirVector, directionMap.keySet(),
					angle_tolerance);

				if (listDirVector != null)
				{
					directionMap.get(listDirVector).add(line);
				}
				else
				{
					System.out.println("\nAdding new Vector-Key: " + normDirVector.toString());
					directionMap.put(normDirVector, new ArrayList<GPLine>());
					directionMap.get(normDirVector).add(line);
				}
			}
		}

		for (Entry<Vector, List<GPLine>> e : directionMap.entrySet())
		System.out.println("\nMap of " + e.getKey().toString() + " contains " + e.getValue().size() + " lines");

		return countAllDistances(type, directionMap);
	}

	public Vector testVectorFitsAngleOfDirVector(Vector normDirVector, Set<Vector> listDirVectors, double tolerance)
	{
		System.out.println("testing Vector " + GPUtility.getShortVectorString(normDirVector));

		// durch alle bereits gefundenen Richtungsvektoren der directionMap
		// durchiterieren und den Winkel bestimmen
		for (Vector vector : listDirVectors)
		{
			double angleOfNormDirVector = GPUtility.angleBetweenVectors(normDirVector, vector);
			// FIXME 30-tolerance and 180-tolerance irrelevant?
			// TESTEN!
			if ((angleOfNormDirVector < tolerance)
				|| (180.0 <= angleOfNormDirVector && angleOfNormDirVector <= 180.0 + tolerance))
			{
				System.out.println("angle of Normdirvetor (" + angleOfNormDirVector + ") inside tolerance to "
					+ GPUtility.getShortVectorString(vector));
				return vector;
			}
		}

		return null;
	}

	private Map<Double, List<String[]>> countAllDistances(GPDataType type, Map<Vector, List<GPLine>> directionMap)
	{
		distanceMap.put(0.0, 0);

		// Map fuer die Speicherung der Linienpaare unter der Referenz des
		// jeweiligen Abstands
		Map<Double, List<String[]>> distanceAndLinesReferenceMap = new HashMap<>();

		double distanceInterval = type.getGPConfig().getValue(GPConfig.DISTANCE_INTERVAL);

		for (Entry<Vector, List<GPLine>> e : directionMap.entrySet())
		{
			List<GPLine> lineListCopy = GPUtility.cloneGPLineList(e.getValue());

			for (GPLine line : e.getValue())
			{
				// zur Vermeidung von Problemen bei nebenlaeufigen Zugriffen
				// (Loeschen der Eintraege innerhalb der Iteration) eine Kopie
				// der Liste verwenden
				lineListCopy.remove(line);

				for (GPLine other : lineListCopy)
				{
					double parallelOverlap = 0.0;

					// parallele Linien von verschiedenen Ebenen sollen nicht
					// beruecksichtigt werden
					if (line.getLineType() == other.getLineType())
					parallelOverlap = calculateParallelOverlapOf(line, other);

					double dist = 0.0;
					if (parallelOverlap > 0.0)
					dist = distanceBetween(line, other);

					if (dist > 0.0)
					{
						// den passenden Intervallwert fuer die aktuelle Distanz
						// ermitteln, um diesen zu zaehlen
						double distanceKey = calculateDistanceKey(dist, distanceInterval);
						// ist die Distanz ausserhalb des bisherigen Maximums,
						// Map erweitern
						if (!distanceMap.containsKey(distanceKey))
						{
							expandDistanceMapToDistanceKey(distanceKey, distanceInterval);
							// System.out.println("# new amount of distances is
							// " +
							// distanceMap.size());
						}

						System.out.println("++ increasing count of key " + distanceKey + " for line distance " + dist);
						increaseDistanceCounter(distanceKey);

						// die Namensreferenz des gefundenen Linienpaares unter
						// der Distanzreferenz ablegen
						saveLinePairWithDistance(distanceAndLinesReferenceMap, line.getName(), other.getName(),
							distanceKey);
					}
				}
			}
		}

		// GPUtility.printDistanceMap(distanceMap);
		GPUtility.saveDistanceMapToCSVFile(distanceMap);
		
		printRelevantDistancesToConsole();

		return distanceAndLinesReferenceMap;
	}

	private void printRelevantDistancesToConsole()
	{
		for(Entry<Double, Integer> e : distanceMap.entrySet())
			{
				if(e.getValue() > 5)
					System.out.println("Distance ["+e.getKey()+"] has <"+e.getValue()+"> entries");
			}
		
	}

	private void saveLinePairWithDistance(Map<Double, List<String[]>> distanceAndLinesReferenceMap, String lineName,
		String otherName, double distanceKey)
	{
		// Key ggf. vorinitialisieren
		if (!distanceAndLinesReferenceMap.containsKey(distanceKey))
		{
			distanceAndLinesReferenceMap.put(distanceKey, new ArrayList<>());
		}

		String[] pair =
		{ lineName, otherName };
		distanceAndLinesReferenceMap.get(distanceKey).add(pair);
	}

	private double calculateDistanceKey(double dist, double distanceInterval)
	{
		BigDecimal distance = BigDecimal.valueOf(dist);
		BigDecimal interval = BigDecimal.valueOf(distanceInterval);

		// den remainder (rest) von der Distanz abziehen, um einen glatten
		// Quotient zu erhalten
		BigDecimal remainder = distance.remainder(interval);

		BigDecimal tmpDistance = distance.subtract(remainder);

		BigDecimal distanceMultiplicator = tmpDistance.divide(interval, 5, RoundingMode.HALF_DOWN);
		// System.out.println("=== distance " + distance + "; interval " +
		// interval
		// + "; remainder " + remainder
		// + ";\ntmpdistance = distance - remainder = " + distance + " - " +
		// remainder + " = " + tmpDistance
		// + "; distanceMultiplikator: " + distanceMultiplicator);
		BigDecimal distanceKey = distanceMultiplicator.multiply(interval);
		// System.out.println("distanceKey = " + distanceKey);
		return distanceKey.doubleValue();
	}

	private void expandDistanceMapToDistanceKey(double distanceKey, double distanceInterval)
	{
		// zunaechst den bisher hoechsten Key ermitteln
		double highestCurrentKey = findHighestCurrentDistanceKey();

		BigDecimal current = BigDecimal.valueOf(highestCurrentKey);
		BigDecimal target = BigDecimal.valueOf(distanceKey);
		BigDecimal interval = BigDecimal.valueOf(distanceInterval);

		BigDecimal currentQuotient = current.divide(interval, 5, RoundingMode.HALF_DOWN);
		BigDecimal targetQuotient = target.divide(interval, 5, RoundingMode.HALF_DOWN);

		double numberOfSteps = (targetQuotient.subtract(currentQuotient)).doubleValue();

		for (int i = 1; i <= numberOfSteps; i++)
		{
			BigDecimal iTimesIntervall = interval.multiply(BigDecimal.valueOf(i));
			BigDecimal additionalInterval = current.add(iTimesIntervall);
			distanceMap.put(additionalInterval.doubleValue(), 0);
			System.out.println("## added distanceKey " + additionalInterval + " to distanceMap");
		}

	}

	private double findHighestCurrentDistanceKey()
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
		Vector dirVector = GPUtility.substractOtherVector(line.getEnd(), line.getStart());
		Vector tempVector = GPUtility.substractOtherVector(other.getStart(), line.getStart());

		Vector distanceVector = GPUtility.kreuzproduktVon(dirVector, tempVector);

		double temp = GPUtility.calcVectorLength(distanceVector);
		double temp2 = GPUtility.calcVectorLength(dirVector);

		return temp / temp2;
	}

	private void increaseDistanceCounter(double distance)
	{
		int currentCount = distanceMap.get(distance);
		distanceMap.put(distance, currentCount + 1);
	}

	public double calculateParallelOverlapOf(GPLine line, GPLine other)
	{
		Vector dirVec_line = GPUtility.substractOtherVector(line.getEnd(), line.getStart());

		double beta = (dirVec_line.get(0) * dirVec_line.get(0)) + (dirVec_line.get(1) * dirVec_line.get(1))
			+ (dirVec_line.get(2) * dirVec_line.get(2));

		Vector pVec_0 = GPUtility.substractOtherVector(other.getStart(), line.getStart());

		double lambda_other_0 = (pVec_0.get(0) * dirVec_line.get(0)) + (pVec_0.get(1) * dirVec_line.get(1))
			+ (pVec_0.get(2) * dirVec_line.get(2));

		Vector pVec_1 = GPUtility.substractOtherVector(other.getEnd(), line.getStart());

		double lambda_other_1 = (pVec_1.get(0) * dirVec_line.get(0)) + (pVec_1.get(1) * dirVec_line.get(1))
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

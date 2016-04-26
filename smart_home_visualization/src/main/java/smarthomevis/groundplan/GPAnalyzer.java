package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cgresearch.core.math.Vector;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.data.GPDataType;
import smarthomevis.groundplan.data.GPLine;
import smarthomevis.groundplan.data.GPSolid;

public class GPAnalyzer {

	private Map<Double, Integer> distanceMap = new HashMap<>();

	private Map<String, Integer> lineInPairsCount = new HashMap<>();

	public GPDataType analyzeAndProcessData(GPDataType data) {

		Map<Double, List<String[]>> pairsToDistanceMap = calculateDistancesInPlan(data);

		List<Double> relevantDistances = chooseCorrectDistances(pairsToDistanceMap.keySet(), data.getGPConfig());

		printFoundPairs(relevantDistances, pairsToDistanceMap, data);

		addFoundPairsToGPData(relevantDistances, pairsToDistanceMap, data);

		List<GPLine> unpairedLines = getUnpairedLines(data);

		// for (GPLine l : unpairedLines)
		// System.out.println("> " + l.getName());

		List<GPLine[]> singleLinePairs = getPairsWithLinesOnlyOnce(data);

		buildSolidsFromSingleLinePairs(data, unpairedLines, singleLinePairs);

		List<List<GPLine>> multipleLinePairs = getPairsWithLinesMoreThanOnce(data);

		buildSolidsFromMultiLinePairs(data, unpairedLines, multipleLinePairs);

		return data;
	}

	private void buildSolidsFromMultiLinePairs(GPDataType data, List<GPLine> unpairedLines,
			List<List<GPLine>> multipleLinePairs) {
		for (List<GPLine> list : multipleLinePairs) {
			System.out.println("#PAIR:");
			for (GPLine l : list) {
				System.out.println(l.toString());
			}

			GPLine multiLine = list.get(0);

			// die auf die multiLine projezierten Punkte behalten, um Luecken
			// fuellen zu koennen
			List<Vector> projectedPoints = new ArrayList<>();
			// Abstandsvektor der parallelen Linien
			Vector distanceVector = null;

			for (int i = 1; i < list.size(); i++) {
				GPLine currentLine = list.get(i);

				Vector normdirVec_multiline = GPUtility
						.normalizeVector(GPUtility.substractOtherVector(multiLine.getEnd(), multiLine.getStart()));

				Vector cStartOnMultiline = projectPointOnLine(currentLine.getStart(), multiLine.getStart(),
						normdirVec_multiline);
				Vector cEndOnMultiline = projectPointOnLine(currentLine.getEnd(), multiLine.getStart(),
						normdirVec_multiline);

				// Abstand zwischen den Linien bestimmen
				Vector distanceVectorStart = GPUtility.substractOtherVector(currentLine.getStart(), cStartOnMultiline);
				Vector distanceVectorEnd = GPUtility.substractOtherVector(currentLine.getEnd(), cEndOnMultiline);

				if (!distanceVectorStart.equals(distanceVectorEnd))
					System.out.println("ABWEICHUNG!\nStart: " + distanceVectorStart.toString(2) + "End:"
							+ distanceVectorEnd.toString(2));
				else
					System.out.println("Distanzvektoren identisch!");

				distanceVector = distanceVectorStart;

				projectedPoints.add(cStartOnMultiline);
				projectedPoints.add(cEndOnMultiline);

				GPLine projection = new GPLine("proj_" + currentLine.getName(), cStartOnMultiline, cEndOnMultiline);
				projection.setLineType(multiLine.getLineType());

				GPSolid solid = new GPSolid();

				solid.addLine(currentLine);
				solid.addLine(projection);

				GPLine[] lineArray = new GPLine[] { currentLine, projection };

				solid.setBasePair(lineArray);

				for (GPLine l : unpairedLines) {
					if (compareLineVectorsToLinePairVectors(l, lineArray)) {
						System.out.println(l.getName() + " added to " + solid.toString());
						solid.addLine(l);
					}
				}

				data.addSolid(solid);
			}

			/*
			 * Punkte auf der MultiLine analysieren und eventuelle Luecken
			 * ergaenzen
			 */
			analyseProjectedPointsAndMultiLineForGaps(data, multiLine, projectedPoints, distanceVector);
		}
	}

	private void analyseProjectedPointsAndMultiLineForGaps(GPDataType data, GPLine multiLine,
			List<Vector> projectedPoints, Vector distanceVector) {
		System.out.println("MULTILINE: " + multiLine.toString());

		// TODO export value to xml config
		double TOLERANCE = 0.05;

		Map<Double, Vector> sortedProjectedPoints = sortProjectedPointsOnMultiline(projectedPoints, multiLine);

		System.out.println("PROJECTED POINTS: " + projectedPoints.size());
		for (Vector v : projectedPoints)
			System.out.println(v.toString(2));

		// Start- und Endpunkte von MultiLine entfernen
		Vector multiLineStart = multiLine.getStart();

		Vector nearlyEqualStart = getEqualWithToleranceVector(multiLineStart, projectedPoints, TOLERANCE);
		if (nearlyEqualStart != null) {
			projectedPoints.remove(nearlyEqualStart);
			System.out.println("Multiline Startpoint removed: " + nearlyEqualStart.toString(2));
		}

		Vector multiLineEnd = multiLine.getEnd();

		Vector nearlyEqualEnd = getEqualWithToleranceVector(multiLineEnd, projectedPoints, TOLERANCE);
		if (nearlyEqualEnd != null) {
			projectedPoints.remove(nearlyEqualEnd);
			System.out.println("Multiline Endpoint removed: " + nearlyEqualEnd.toString(2));
		}

		List<Vector> relevantVectors = new ArrayList<>();
		// doppelte entfernen
		for (int i = 0; i < projectedPoints.size(); i++) {
			boolean existsOnlyOnce = true;
			Vector current = projectedPoints.get(i);
			for (int j = 0; j < projectedPoints.size(); j++) {
				if (i == j) {
					// skip
				} else {
					if (current.equals(projectedPoints.get(j))) {
						existsOnlyOnce = false;
						System.out.println(
								"EQUAL POINTS: " + current.toString(2) + " == " + projectedPoints.get(j).toString(2));
					}
				}
			}

			if (existsOnlyOnce) {
				relevantVectors.add(current);
				System.out.println("UNIQUE POINT: " + current.toString(2));
			}
		}

		if (relevantVectors.size() == 2) {
			Vector startPoint = relevantVectors.get(0);
			Vector endPoint = relevantVectors.get(1);
			System.out.println("Filling GAP between " + startPoint.toString(2) + " and " + endPoint.toString(2));

			GPLine gapLine = new GPLine(multiLine.getName() + "_gapLine", startPoint, endPoint);

			GPLine gapProjection = new GPLine(gapLine.getName() + "_projection", startPoint.add(distanceVector),
					endPoint.add(distanceVector));

			GPSolid solid = new GPSolid();
			solid.addLine(gapLine);
			solid.addLine(gapProjection);

			solid.setBasePair(new GPLine[] { gapLine, gapProjection });

			data.addSolid(solid);
		} else {
			// TODO bei mehreren Luecken muss sortiert werden
			System.out.println("TODO - More than one gap!");
			System.out.println("relevantVectors: " + relevantVectors.size());
		}
	}

	private Map<Double, Vector> sortProjectedPointsOnMultiline(List<Vector> projectedPoints, GPLine multiLine) {
		Map<Double, Vector> pointsWithSkalar = new HashMap<>();

		Vector dirVecMultiLine = GPUtility.substractOtherVector(multiLine.getEnd(), multiLine.getStart());
		
		

		for (Vector point : projectedPoints) {
			Double lambda_x = null;
			Double lambda_y = null;
			
			// alternativ ginge hier auch dirVecMultiLine.get(0) != 0 (Richtungsvektor ist p.D. 0 an der relevanten Ordinate)
			if(multiLine.getStart().get(0) != multiLine.getEnd().get(0))
			{
				lambda_x = calculateLambda(point.get(0), multiLine.getStart().get(0), dirVecMultiLine.get(0));
			}
			if(multiLine.getStart().get(1) != multiLine.getEnd().get(1))
			{
				lambda_y= calculateLambda(point.get(1), multiLine.getStart().get(1), dirVecMultiLine.get(1));
			}
			
			if(lambda_x != null)
			System.out.println("Lambda X: " + lambda_x);
			if(lambda_y != null)
			System.out.println("Lambda Y: "+lambda_y);
			
			if(lambda_x != null && lambda_y == null)
				pointsWithSkalar.put(lambda_x, point);
			else if (lambda_x == null && lambda_y != null)
				pointsWithSkalar.put(lambda_y, point);
			else if (lambda_x != null && lambda_y !=null)
			{
				if(lambda_x == lambda_y)
					pointsWithSkalar.put(lambda_x, point);
				else
					{
					System.out.println("Skalare Ergebnisse sind nicht eindeutig; Vector liegt nicht auf Multiline");
					}
			}
		}

		return pointsWithSkalar;
	}

	private double calculateLambda(double p, double s, double r) {
		System.out.println("(" + p + " - " + s + ") / " + r + ")");
		
		BigDecimal bd_P = BigDecimal.valueOf(p); 
		BigDecimal bd_S = BigDecimal.valueOf(s); 
		BigDecimal bd_R = BigDecimal.valueOf(r);
		
		BigDecimal bd_PminusS = bd_P.subtract(bd_S);
		return bd_PminusS.divide(bd_R, 5, RoundingMode.HALF_DOWN).doubleValue();
	}

	private Vector getEqualWithToleranceVector(Vector current, List<Vector> projectedPoints, double tolerance) {
		double[] currentCoords = current.data();

		for (Vector vector : projectedPoints) {
			double[] vectorCoords = vector.data();
			boolean nearlyEqualVector = true;

			// IndexOutOfBoundsException vermeiden
			if (currentCoords.length == vectorCoords.length) {
				// die Ordinaten vergleichen
				for (int i = 0; i < currentCoords.length; i++) {
					// Wenn Differenz auserhalb von -tolerance und tolerance,
					// dann nicht nahezu identisch
					double diff = currentCoords[i] - vectorCoords[i];
					if (diff > tolerance || diff < 0 - tolerance)
						nearlyEqualVector = false;

				}

				if (nearlyEqualVector)
					return vector;
			}
		}
		return null;
	}

	private Vector projectPointOnLine(Vector point, Vector posVecLine, Vector normDirVecLine) {
		// r + (((x-r)°u)/ u°u) * u

		// x-r ° u

		Vector xMinusR = GPUtility.substractOtherVector(point, posVecLine);

		BigDecimal numerator = BigDecimal.valueOf(GPUtility.punktproduktVon(xMinusR, normDirVecLine));

		BigDecimal denominator = BigDecimal.valueOf(GPUtility.punktproduktVon(normDirVecLine, normDirVecLine));

		BigDecimal fraction = numerator.divide(denominator);

		Vector p = normDirVecLine.multiply(fraction.doubleValue());

		return posVecLine.add(p);
	}

	private List<List<GPLine>> getPairsWithLinesMoreThanOnce(GPDataType data) {
		List<List<GPLine>> pairsList = new ArrayList<>();

		// Ebenen abarbeiten
		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet()) {
			// jedes Paar jeder Ebene
			for (GPLine[] array : e.getValue()) {

				System.out.println("#ARRAY:");
				for (GPLine l : array) {
					System.out.println(l.toString());
				}

				int countA = lineInPairsCount.get(array[0].getName());
				int countB = lineInPairsCount.get(array[1].getName());
				List<GPLine> multiplePairsLines = new ArrayList<>();

				if (countA > 1 && countB > 1) {
					// TODO split notwendig, beide haben mehrere parallele...
					// :-/
					System.out.println("!!! Linienpaar mit mehreren Ueberschneidungen !!!");
					System.out.println(array[0].getName() + ":" + countA + "; " + array[1].getName() + ":" + countB);
				} else if (countA > 1 || countB > 1) {
					GPLine multiLine = (countA > 1) ? array[0] : array[1];
					GPLine singleLine = (multiLine.equals(array[0])) ? array[1] : array[0];

					// pruefen, ob dieser Kandidat nicht schon abgearbeitet ist
					boolean alreadyFound = false;
					for (List<GPLine> list : pairsList) {
						// die mehrfach ueberschneidende Linie immer an index 0
						if (multiLine.equals(list.get(0))) {
							alreadyFound = true;
						}
					}

					if (!alreadyFound) {
						// die "multiLine" zuerst in die Liste (s.o.)
						multiplePairsLines.add(multiLine);
						multiplePairsLines.add(singleLine);

						findOtherParallelLines(e.getKey(), multiplePairsLines, data);

						System.out.println("-- PairArray:");
						for (GPLine l : multiplePairsLines) {
							System.out.println(l.toString());
						}

						pairsList.add(multiplePairsLines);
					}
				}
			}
		}

		return pairsList;
	}

	private void findOtherParallelLines(String layer, List<GPLine> multiplePairsLines, GPDataType data) {
		GPLine multiLine = multiplePairsLines.get(0);
		GPLine singleLine = multiplePairsLines.get(1);
		for (GPLine[] array : data.getGPLinePairsOfLayer(layer)) {
			List<GPLine> pairList = Arrays.asList(array);

			if (pairList.contains(multiLine) && !pairList.contains(singleLine)) {
				for (GPLine line : pairList) {
					if (!line.equals(multiLine))
						multiplePairsLines.add(line);
				}

			}
		}
	}

	private void buildSolidsFromSingleLinePairs(GPDataType data, List<GPLine> unpairedLines,
			List<GPLine[]> singleLinePairs) {
		for (GPLine[] lineArray : singleLinePairs) {
			GPSolid solid = new GPSolid();

			// FIXME: adding objectreferences twice is suboptimal
			solid.setBasePair(lineArray);

			for (GPLine l : lineArray) {
				solid.addLine(l);
			}

			for (GPLine l : unpairedLines) {
				if (compareLineVectorsToLinePairVectors(l, lineArray)) {
					System.out.println(l.getName() + " added to " + solid.toString());
					solid.addLine(l);
				}
			}
			data.addSolid(solid);
		}
	}

	private boolean compareLineVectorsToLinePairVectors(GPLine line, GPLine[] pair) {
		boolean result = false;
		boolean onefound = false;

		Vector start = line.getStart();
		Vector end = line.getEnd();

		for (GPLine l : pair) {
			if (l.getStart().equals(start)) {
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getStart().equals(end)) {
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getEnd().equals(start)) {
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getEnd().equals(end)) {
				if (onefound)
					result = true;
				else
					onefound = true;
			}
		}

		return result;
	}

	private List<GPLine> getUnpairedLines(GPDataType data) {
		List<GPLine> unpairedLines = new ArrayList<>();

		for (Entry<String, GPLine> e : data.getLines().entrySet()) {
			unpairedLines.add(e.getValue());
		}

		Set<GPLine> pairLines = new HashSet<>();

		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet()) {
			for (GPLine[] a : e.getValue()) {
				for (GPLine l : a) {
					pairLines.add(l);
				}
			}
		}
		unpairedLines.removeAll(pairLines);

		return unpairedLines;
	}

	private List<GPLine[]> getPairsWithLinesOnlyOnce(GPDataType data) {
		List<GPLine[]> pairsList = new ArrayList<>();

		// Ebenen abarbeiten
		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet()) {
			// jedes Paar jeder Ebene
			for (GPLine[] array : e.getValue()) {
				Integer countA = lineInPairsCount.get(array[0].getName());
				Integer countB = lineInPairsCount.get(array[1].getName());
				String pairString = array[0].getName() + "-" + array[1].getName();

				if (countA != null && countB != null) {
					if (countA == 1 && countB == 1) {
						pairsList.add(array);
						System.out.println(pairString + " added");
					}
				} else {
					System.err.println("Value missing in lineInPairsCount!");
				}
			}
		}

		return pairsList;
	}

	private void addFoundPairsToGPData(List<Double> relevantDistances, Map<Double, List<String[]>> pairsToDistanceMap,
			GPDataType data) {
		for (Double d : relevantDistances) {
			for (String[] sArray : pairsToDistanceMap.get(d)) {
				String line0 = sArray[0];
				String layerName0 = data.getLayerOfLine(line0);

				String line1 = sArray[1];
				String layerName1 = data.getLayerOfLine(line1);

				if (layerName0.equals(layerName1)) {
					data.addPairToLayer(layerName0, sArray);

					// Zaehler fuer die Linien erhoehen:
					increaseLineInPairCount(line0);
					increaseLineInPairCount(line1);
				} else
					System.err.println("Inconsistency found: Pair of Lines is not from same Layer " + line0 + "["
							+ layerName0 + "] - " + line1 + "[" + layerName1 + "]");

			}
		}
	}

	private void increaseLineInPairCount(String line) {
		if (!this.lineInPairsCount.containsKey(line)) {
			this.lineInPairsCount.put(line, 0);
		}

		int count = this.lineInPairsCount.get(line);
		this.lineInPairsCount.put(line, count + 1);
	}

	// Ausgabemethode fuers Verstaendnis..
	private void printFoundPairs(List<Double> relevantDistances, Map<Double, List<String[]>> pairsToDistanceMap,
			GPDataType data) {
		for (Double d : relevantDistances) {
			for (String[] s : pairsToDistanceMap.get(d)) {
				System.out.println(
						s[0] + "[" + data.getLayerOfLine(s[0]) + "] - " + s[1] + "[" + data.getLayerOfLine(s[1]) + "]");
			}
		}
	}

	private List<Double> chooseCorrectDistances(Set<Double> distanceSet, GPConfig config) {
		List<Double> relevantDistances = new ArrayList<>();
		for (Double distance : distanceSet) {
			if (distance >= config.getValue(GPConfig.LOWER_WALLTHICKNESS_LIMIT)
					&& distance <= config.getValue(GPConfig.UPPER_WALLTHICKNESS_LIMIT)) {
				System.out.println("Distance " + distance + " is in range");
				int count = distanceMap.get(distance);
				Double threshold = config.getValue(GPConfig.WALL_COUNT_THRESHOLD);
				if (count >= threshold) {
					System.out.println("number of pairs (" + count + ") is over threshold (" + threshold + ")");
					relevantDistances.add(distance);
				}
			}
		}

		return relevantDistances;
	}

	public Map<Double, List<String[]>> calculateDistancesInPlan(GPDataType type) {
		Map<String, List<GPLine>> layerMap = type.getLayers();

		// Toleranzwert fuer Winkelabweichung; wird bereits hier geladen, um
		// Zugriffshaeufigkeit zu reduzieren
		double angle_tolerance = type.getGPConfig().getValue(GPConfig.ANGLE_TOLERANCE);

		Map<Vector, List<GPLine>> directionMap = new HashMap<>();
		// Anmerkung: alle Linien verschiedenen Ebenen werden hier unter den
		// Distanzen zusammengefasst!

		// fuer alle Ebenen
		for (Entry<String, List<GPLine>> entry : layerMap.entrySet()) {
			// fuer alle Linien einer Ebene
			for (GPLine line : entry.getValue()) {
				Vector dirVector = GPUtility.substractOtherVector(line.getEnd(), line.getStart());
				Vector normDirVector = GPUtility.normalizeVector(dirVector);

				Vector listDirVector = testVectorFitsAngleOfDirVector(normDirVector, directionMap.keySet(),
						angle_tolerance);

				if (listDirVector != null) {
					directionMap.get(listDirVector).add(line);
				} else {
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

	public Vector testVectorFitsAngleOfDirVector(Vector normDirVector, Set<Vector> listDirVectors, double tolerance) {
		System.out.println("testing Vector " + normDirVector.toString());

		// durch alle bereits gefundenen Richtungsvektoren der directionMap
		// durchiterieren und den Winkel bestimmen
		for (Vector vector : listDirVectors) {
			double angleOfNormDirVector = GPUtility.angleBetweenVectors(normDirVector, vector);
			// FIXME 30-tolerance and 180-tolerance irrelevant?
			// TESTEN!
			if ((angleOfNormDirVector < tolerance)
					|| (180.0 <= angleOfNormDirVector && angleOfNormDirVector <= 180.0 + tolerance)) {
				System.out.println("angle of Normdirvetor (" + angleOfNormDirVector + ") inside tolerance to "
						+ vector.toString());
				return vector;
			}
		}

		return null;
	}

	private Map<Double, List<String[]>> countAllDistances(GPDataType type, Map<Vector, List<GPLine>> directionMap) {
		distanceMap.put(0.0, 0);

		// Map fuer die Speicherung der Linienpaare unter der Referenz des
		// jeweiligen Abstands
		Map<Double, List<String[]>> distanceAndLinesReferenceMap = new HashMap<>();

		double distanceInterval = type.getGPConfig().getValue(GPConfig.DISTANCE_INTERVAL);

		for (Entry<Vector, List<GPLine>> e : directionMap.entrySet()) {
			List<GPLine> lineListCopy = GPUtility.cloneList(e.getValue());

			for (GPLine line : e.getValue()) {
				// zur Vermeidung von Problemen bei nebenlaeufigen Zugriffen
				// (Loeschen der Eintraege innerhalb der Iteration) eine Kopie
				// der
				// Liste
				// verwenden
				lineListCopy.remove(line);

				for (GPLine other : lineListCopy) {
					double parallelOverlap = 0.0;

					// parallele Linien von verschiedenen Ebenen sollen nicht
					// beruecksichtigt werden
					if (line.getLineType() == other.getLineType())
						parallelOverlap = calculateParallelOverlapOf(line, other);

					double dist = 0.0;
					if (parallelOverlap > 0.0)
						dist = distanceBetween(line, other);

					if (dist > 0.0) {
						// den passenden Intervallwert fuer die aktuelle Distanz
						// ermitteln, um diesen zu zaehlen
						double distanceKey = calculateDistanceKey(dist, distanceInterval);
						// ist die Distanz ausserhalb des bisherigen Maximums,
						// Map erweitern
						if (!distanceMap.containsKey(distanceKey)) {
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

		return distanceAndLinesReferenceMap;
	}

	private void saveLinePairWithDistance(Map<Double, List<String[]>> distanceAndLinesReferenceMap, String lineName,
			String otherName, double distanceKey) {
		// Key ggf. vorinitialisieren
		if (!distanceAndLinesReferenceMap.containsKey(distanceKey)) {
			distanceAndLinesReferenceMap.put(distanceKey, new ArrayList<>());
		}

		String[] pair = { lineName, otherName };
		distanceAndLinesReferenceMap.get(distanceKey).add(pair);
	}

	private double calculateDistanceKey(double dist, double distanceInterval) {
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

	private void expandDistanceMapToDistanceKey(double distanceKey, double distanceInterval) {
		// zunaechst den bisher hoechsten Key ermitteln
		double highestCurrentKey = findHighestCurrentDistanceKey();

		BigDecimal current = BigDecimal.valueOf(highestCurrentKey);
		BigDecimal target = BigDecimal.valueOf(distanceKey);
		BigDecimal interval = BigDecimal.valueOf(distanceInterval);

		BigDecimal currentQuotient = current.divide(interval, 5, RoundingMode.HALF_DOWN);
		BigDecimal targetQuotient = target.divide(interval, 5, RoundingMode.HALF_DOWN);

		double numberOfSteps = (targetQuotient.subtract(currentQuotient)).doubleValue();

		for (int i = 1; i <= numberOfSteps; i++) {
			BigDecimal iTimesIntervall = interval.multiply(BigDecimal.valueOf(i));
			BigDecimal additionalInterval = current.add(iTimesIntervall);
			distanceMap.put(additionalInterval.doubleValue(), 0);
			System.out.println("## added distanceKey " + additionalInterval + " to distanceMap");
		}

	}

	private double findHighestCurrentDistanceKey() {
		// geht alle Keys durch und findet den hoechsten
		double currentHighest = 0.0;
		for (Entry<Double, Integer> e : distanceMap.entrySet()) {
			if (e.getKey() > currentHighest)
				currentHighest = e.getKey();
		}
		return currentHighest;
	}

	public double distanceBetween(GPLine line, GPLine other) {
		Vector dirVector = GPUtility.substractOtherVector(line.getEnd(), line.getStart());
		Vector tempVector = GPUtility.substractOtherVector(other.getStart(), line.getStart());

		Vector distanceVector = GPUtility.kreuzproduktVon(dirVector, tempVector);

		double temp = GPUtility.calcVectorLength(distanceVector);
		double temp2 = GPUtility.calcVectorLength(dirVector);

		return temp / temp2;
	}

	private void increaseDistanceCounter(double distance) {
		int currentCount = distanceMap.get(distance);
		distanceMap.put(distance, currentCount + 1);
	}

	public double calculateParallelOverlapOf(GPLine line, GPLine other) {
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
		if (lambda_other_1 < lambda_other_0) {
			// Werte tauschen
			double temp = lambda_other_1;
			lambda_other_1 = lambda_other_0;
			lambda_other_0 = temp;
		}

		// Untergrenze bestimmen
		if (lambda_other_0 <= 0.0)
			lowerLimit = 0.0;
		else {
			if (lambda_other_0 > beta)
				isEmptyInterval = true;
			else
				lowerLimit = lambda_other_0;
		}

		// Obergrenze bestimmen
		if (lambda_other_1 >= beta)
			upperLimit = beta;
		else {
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

package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import cgresearch.core.math.Vector;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.data.GPDataType;
import smarthomevis.groundplan.data.GPLine;
import smarthomevis.groundplan.data.GPSolid;

public class GPEvaluator
{

	private Map<String, Integer> lineInPairsCount = new HashMap<>();

	private GPDataType data = null;

	public GPDataType processData(GPDataType data, Map<Double, List<String[]>> pairsToDistanceMap)
	{
		this.data = data;

		List<Double> relevantDistances = chooseCorrectDistances(pairsToDistanceMap.keySet(), data.getGPConfig());

		printFoundPairs(relevantDistances, pairsToDistanceMap);

		addFoundPairsToGPData(relevantDistances, pairsToDistanceMap);

		List<GPLine> unpairedLines = getUnpairedLines(data);

		// for (GPLine l : unpairedLines)
		// System.out.println("> " + l.getName());

		/*
		 * SingleLine Pair Handling
		 */
		List<GPLine[]> singleLinePairs = getPairsWithLinesOnlyOnce(data);

		buildSolidsFromSingleLinePairs(data, unpairedLines, singleLinePairs);

		/*
		 * MultiLine Pairs Handling
		 */
		List<List<GPLine>> multipleLinePairs = getPairsWithLinesMoreThanOnce(data);

		buildSolidsFromMultiLinePairs(data, unpairedLines, multipleLinePairs);

		/*
		 * Komplex Gap Handling
		 */
		Map<Integer, Map<List<Vector>, GPSolid[]>> solidsSortedAfterCorners = sortSolidsAfterAlignedCorners();

		analyzeMatchedCornersAndBuildMissingSolids(solidsSortedAfterCorners);

		// for (GPSolid s : data.getAllSolids())
		// System.out.println(s.toString());

		// List<GPSolid> doppelgaenger = compareAllSolidsForDoubles();
		//
		// removeDoppelgaengerSolidsFromData(doppelgaenger);
		//
		// ersetzeSolidsInDataMitDoppelgaengern(doppelgaenger);

		return data;
	}

	@SuppressWarnings("unused")
	private void removeDoppelgaengerSolidsFromData(List<GPSolid> doppelgaenger)
	{
		data.getAllSolids().removeAll(doppelgaenger);
	}

	@SuppressWarnings("unused")
	private void ersetzeSolidsInDataMitDoppelgaengern(List<GPSolid> doppelgaenger)
	{
		data.getAllSolids().clear();
		for (GPSolid solid : doppelgaenger)
			data.addSolid(solid);
	}

	@SuppressWarnings("unused")
	private List<GPSolid> compareAllSolidsForDoubles()
	{
		List<GPSolid> solidList = data.getAllSolids();
		List<GPSolid> doppelgaenger = new ArrayList<>();

		for (int i = 0; i < solidList.size() - 1; i++)
		{
			GPSolid s = solidList.get(i);

			for (int j = i + 1; j < solidList.size(); j++)
			{
				GPSolid o = solidList.get(j);

				if (s.equalsInVectors(o))
				{
					System.out.println("=-=-=-=-> Identische Solids: " + s.toString() + " & " + o.toString());
					doppelgaenger.add(o);
				}
			}
		}
		return doppelgaenger;
	}

	private List<Double> chooseCorrectDistances(Set<Double> distanceSet, GPConfig config)
	{
		List<Double> relevantDistances = new ArrayList<>();
		for (Double distance : distanceSet)
		{
			if (distance >= config.getValue(GPConfig.LOWER_WALLTHICKNESS_LIMIT)
					&& distance <= config.getValue(GPConfig.UPPER_WALLTHICKNESS_LIMIT))
			{
				System.out.println("Distance " + distance + " is in range");
				int count = this.data.getDistanceMap().get(distance);
				Double threshold = config.getValue(GPConfig.WALL_COUNT_THRESHOLD);
				if (count >= threshold)
				{
					System.out.println("number of pairs (" + count + ") is over threshold (" + threshold + ")");
					relevantDistances.add(distance);
				}
			}
		}

		return relevantDistances;
	}

	// Ausgabemethode fuers Verstaendnis..
	private void printFoundPairs(List<Double> relevantDistances, Map<Double, List<String[]>> pairsToDistanceMap)
	{
		for (Double d : relevantDistances)
		{
			for (String[] s : pairsToDistanceMap.get(d))
			{
				System.out.println(s[0] + "[" + this.data.getLayerOfLine(s[0]) + "] - " + s[1] + "["
						+ this.data.getLayerOfLine(s[1]) + "]");
			}
		}
	}

	private void addFoundPairsToGPData(List<Double> relevantDistances, Map<Double, List<String[]>> pairsToDistanceMap)
	{
		for (Double d : relevantDistances)
		{
			for (String[] sArray : pairsToDistanceMap.get(d))
			{
				String line0 = sArray[0];
				String layerName0 = this.data.getLayerOfLine(line0);

				String line1 = sArray[1];
				String layerName1 = this.data.getLayerOfLine(line1);

				if (layerName0.equals(layerName1))
				{
					this.data.addPairToLayer(layerName0, sArray);

					// Zaehler fuer die Linien erhoehen:
					increaseLineInPairCount(line0);
					increaseLineInPairCount(line1);
				}
				else
					System.err.println("Inconsistency found: Pair of Lines is not from same Layer " + line0 + "["
							+ layerName0 + "] - " + line1 + "[" + layerName1 + "]");

			}
		}
	}

	private void increaseLineInPairCount(String line)
	{
		if (!this.lineInPairsCount.containsKey(line))
		{
			this.lineInPairsCount.put(line, 0);
		}

		int count = this.lineInPairsCount.get(line);
		this.lineInPairsCount.put(line, count + 1);
	}

	private List<GPLine> getUnpairedLines(GPDataType data)
	{
		List<GPLine> unpairedLines = new ArrayList<>();

		for (Entry<String, GPLine> e : data.getLines().entrySet())
		{
			unpairedLines.add(e.getValue());
		}

		Set<GPLine> pairLines = new HashSet<>();

		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet())
		{
			for (GPLine[] a : e.getValue())
			{
				for (GPLine l : a)
				{
					pairLines.add(l);
				}
			}
		}
		unpairedLines.removeAll(pairLines);

		return unpairedLines;
	}

	private List<GPLine[]> getPairsWithLinesOnlyOnce(GPDataType data)
	{
		List<GPLine[]> pairsList = new ArrayList<>();

		// Ebenen abarbeiten
		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet())
		{
			// jedes Paar jeder Ebene
			for (GPLine[] array : e.getValue())
			{
				Integer countA = lineInPairsCount.get(array[0].getName());
				Integer countB = lineInPairsCount.get(array[1].getName());
				String pairString = array[0].getName() + "-" + array[1].getName();

				if (countA != null && countB != null)
				{
					if (countA == 1 && countB == 1)
					{
						pairsList.add(array);
						System.out.println(pairString + " added");
					}
				}
				else
				{
					System.err.println("Value missing in lineInPairsCount!");
				}
			}
		}

		return pairsList;
	}

	private void buildSolidsFromSingleLinePairs(GPDataType data, List<GPLine> unpairedLines,
			List<GPLine[]> singleLinePairs)
	{
		for (GPLine[] lineArray : singleLinePairs)
		{
			GPSolid solid = new GPSolid();

			// FIXME: adding objectreferences twice is suboptimal
			solid.setBasePair(lineArray);

			for (GPLine l : lineArray)
			{
				solid.addLine(l);
			}

			for (GPLine l : unpairedLines)
			{
				if (compareLineVectorsToLinePairVectors(l, lineArray))
				{
					System.out.println(l.getName() + " added to " + solid.toString());
					solid.addLine(l);
				}
			}
			System.out.println("ADDING NORM SOLID " + solid.toString());
			data.addSolid(solid);
		}
	}

	private List<List<GPLine>> getPairsWithLinesMoreThanOnce(GPDataType data)
	{
		List<List<GPLine>> pairsList = new ArrayList<>();

		// Ebenen abarbeiten
		for (Entry<String, List<GPLine[]>> e : data.getGPLinePairsPerLayerMap().entrySet())
		{
			// jedes Paar jeder Ebene
			for (GPLine[] array : e.getValue())
			{

				System.out.println("#ARRAY:");
				for (GPLine l : array)
				{
					System.out.println(l.toString());
				}

				int countA = lineInPairsCount.get(array[0].getName());
				int countB = lineInPairsCount.get(array[1].getName());
				List<GPLine> multiplePairsLines = new ArrayList<>();

				if (countA > 1 && countB > 1)
				{
					// TODO split notwendig, beide haben mehrere
					// parallele...
					// :-/
					System.out.println("!!! Linienpaar mit mehreren Ueberschneidungen !!!");
					System.out.println(array[0].getName() + ":" + countA + "; " + array[1].getName() + ":" + countB);
				}
				else if (countA > 1 || countB > 1)
				{
					GPLine multiLine = (countA > 1) ? array[0] : array[1];
					GPLine singleLine = (multiLine.equals(array[0])) ? array[1] : array[0];

					// pruefen, ob dieser Kandidat nicht schon
					// abgearbeitet ist
					boolean alreadyFound = false;
					for (List<GPLine> list : pairsList)
					{
						// die mehrfach ueberschneidende
						// Linie immer an index 0
						if (multiLine.equals(list.get(0)))
						{
							alreadyFound = true;
						}
					}

					if (!alreadyFound)
					{
						// die "multiLine" zuerst in die
						// Liste (s.o.)
						multiplePairsLines.add(multiLine);
						multiplePairsLines.add(singleLine);

						findOtherParallelLines(e.getKey(), multiplePairsLines, data);

						System.out.println("-- PairArray:");
						for (GPLine l : multiplePairsLines)
						{
							System.out.println(l.toString());
						}

						pairsList.add(multiplePairsLines);
					}
				}
			}
		}

		return pairsList;
	}

	private void findOtherParallelLines(String layer, List<GPLine> multiplePairsLines, GPDataType data)
	{
		GPLine multiLine = multiplePairsLines.get(0);
		GPLine singleLine = multiplePairsLines.get(1);
		for (GPLine[] array : data.getGPLinePairsOfLayer(layer))
		{
			List<GPLine> pairList = Arrays.asList(array);

			if (pairList.contains(multiLine) && !pairList.contains(singleLine))
			{
				for (GPLine line : pairList)
				{
					if (!line.equals(multiLine))
						multiplePairsLines.add(line);
				}

			}
		}
	}

	private void buildSolidsFromMultiLinePairs(GPDataType data, List<GPLine> unpairedLines,
			List<List<GPLine>> multipleLinePairs)
	{
		for (List<GPLine> list : multipleLinePairs)
		{
			System.out.println("#PAIR:");
			for (GPLine l : list)
			{
				System.out.println(l.toString());
			}

			// die erste Linie ist die "Hauptlinie"
			GPLine multiLine = list.get(0);

			// die auf die multiLine projezierten Punkte behalten, um
			// Luecken fuellen zu koennen
			List<Vector> projectedPoints = new ArrayList<>();

			// die einzelnen Solids dieser Multiline einsammeln, um die
			// Luecken zu finden
			List<GPSolid> multiLineSolids = new ArrayList<>();

			// Abstandsvektor der parallelen Linien
			Vector distanceVector = null;

			Vector normdirVec_multiline = GPUtility
					.normalizeVector(GPUtility.substractOtherVector(multiLine.getEnd(), multiLine.getStart()));

			for (int i = 1; i < list.size(); i++)
			{
				GPLine currentLine = list.get(i);

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

				distanceVector = distanceVectorStart;

				projectedPoints.add(cStartOnMultiline);
				projectedPoints.add(cEndOnMultiline);

				System.out.println("++++\nAdding Vectors " + GPUtility.getShortVectorString(cStartOnMultiline) + " and "
						+ GPUtility.getShortVectorString(cEndOnMultiline) + " to PROJECTED POINTS\n+++");

				GPLine projection = new GPLine("proj_" + currentLine.getName(), cStartOnMultiline, cEndOnMultiline);
				projection.setLineType(multiLine.getLineType());

				GPSolid solid = new GPSolid();

				solid.addLine(currentLine);
				solid.addLine(projection);

				GPLine[] lineArray = new GPLine[] { currentLine, projection };

				solid.setBasePair(lineArray);

				for (GPLine l : unpairedLines)
				{
					if (compareLineVectorsToLinePairVectors(l, lineArray))
					{
						System.out.println(l.getName() + " added to " + solid.toString());
						solid.addLine(l);
					}
				}

				System.out.println("ADDING ML SOLID " + solid.toString());
				data.addSolid(solid);
				multiLineSolids.add(solid);
			}

			/*
			 * Punkte auf der MultiLine analysieren und eventuelle Luecken
			 * ergaenzen
			 */
			analyseProjectedPointsAndMultiLineForGaps(data, multiLine, projectedPoints, distanceVector,
					multiLineSolids);
		}
	}

	private Vector projectPointOnLine(Vector point, Vector posVecLine, Vector normDirVecLine)
	{
		// r + (((x-r)°u)/ u°u) * u

		// x-r ° u

		Vector xMinusR = GPUtility.substractOtherVector(point, posVecLine);

		BigDecimal numerator = BigDecimal.valueOf(GPUtility.punktproduktVon(xMinusR, normDirVecLine));

		BigDecimal denominator = BigDecimal.valueOf(GPUtility.punktproduktVon(normDirVecLine, normDirVecLine));

		BigDecimal fraction = numerator.divide(denominator, 5, RoundingMode.HALF_DOWN);

		Vector p = normDirVecLine.multiply(fraction.doubleValue());

		return posVecLine.add(p);
	}

	private boolean compareLineVectorsToLinePairVectors(GPLine line, GPLine[] pair)
	{
		boolean result = false;
		boolean onefound = false;

		Vector start = line.getStart();
		Vector end = line.getEnd();

		for (GPLine l : pair)
		{
			if (l.getStart().equals(start))
			{
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getStart().equals(end))
			{
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getEnd().equals(start))
			{
				if (onefound)
					result = true;
				else
					onefound = true;
			}
			if (l.getEnd().equals(end))
			{
				if (onefound)
					result = true;
				else
					onefound = true;
			}
		}

		return result;
	}

	private void analyseProjectedPointsAndMultiLineForGaps(GPDataType data, GPLine multiLine,
			List<Vector> projectedPoints, Vector distanceVector, List<GPSolid> multiLineSolids)
	{
		System.out.println("MULTILINE: " + multiLine.toString());

		double TOLERANCE = data.getGPConfig().getValue(GPConfig.POINT_TOLERANCE);

		System.out.println("PROJECTED POINTS: " + projectedPoints.size());
		for (Vector v : projectedPoints)
			System.out.println(v.toString(2));

		SortedMap<Double, Vector> sortedProjectedPoints = sortProjectedPointsOnMultiline(projectedPoints, multiLine);

		// Start- und Endpunkte von MultiLine entfernen
		Vector multiLineStart = multiLine.getStart();

		Vector nearlyEqualStart = getEqualWithToleranceVector(multiLineStart, projectedPoints, TOLERANCE);
		if (nearlyEqualStart != null)
		{
			projectedPoints.remove(nearlyEqualStart);
			System.out.println("X===> Multiline Startpoint removed: " + nearlyEqualStart.toString(2));
		}

		Vector multiLineEnd = multiLine.getEnd();

		Vector nearlyEqualEnd = getEqualWithToleranceVector(multiLineEnd, projectedPoints, TOLERANCE);
		if (nearlyEqualEnd != null)
		{
			projectedPoints.remove(nearlyEqualEnd);
			System.out.println("X===> Multiline Endpoint removed: " + nearlyEqualEnd.toString(2));
		}

		List<Vector> relevantVectors = new ArrayList<>();
		// doppelte entfernen
		for (int i = 0; i < projectedPoints.size(); i++)
		{
			boolean existsOnlyOnce = true;
			Vector current = projectedPoints.get(i);
			for (int j = 0; j < projectedPoints.size(); j++)
			{
				if (i == j)
				{
					// skip
				}
				else
				{
					if (current.equals(projectedPoints.get(j)))
					{
						existsOnlyOnce = false;
						System.out.println(
								"EQUAL POINTS: " + current.toString(2) + " == " + projectedPoints.get(j).toString(2));
					}
				}
			}

			if (existsOnlyOnce)
			{
				relevantVectors.add(current);
				System.out.println("UNIQUE POINT: " + current.toString(2));
			}
		}

		if (relevantVectors.size() == 2)
		{
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

			System.out.println("ADDING SINGLE GAP SOLID " + solid.toString());
			data.addSolid(solid);
		}
		else
		{
			List<Vector> sortedVectors = new ArrayList<>();

			// verwendete Lueckenvektoren einsammeln
			List<Vector> usedGapVectors = new ArrayList<>();
			for (Entry<Double, Vector> e : sortedProjectedPoints.entrySet())
			{
				if (relevantVectors.contains(e.getValue()))
					sortedVectors.add(e.getValue());
			}

			Map<Vector, Integer> indexedProjectedPoints = buildIndexedMapOfProjectedPoints(sortedProjectedPoints);
			System.out.println("==========\nrelevantVectors: " + relevantVectors.size());
			for (int i = 0; i < sortedVectors.size() - 1; i++)
			{

				Vector v = sortedVectors.get(i);
				int vIndex = indexedProjectedPoints.get(v);
				String vectorString = v.toString(2);
				System.out.println(
						"CURRENT (INDEX=" + vIndex + ") => " + vectorString.substring(2, vectorString.length() - 2));

				Vector w = sortedVectors.get(i + 1);
				int wIndex = indexedProjectedPoints.get(w);
				String wectorString = w.toString(2);
				System.out.println(
						"NEXT (INDEX=" + wIndex + ") => " + wectorString.substring(2, wectorString.length() - 2));

				if (wIndex - vIndex == 1)
				{
					System.out.println("## Neighbours found! ##");
					boolean solidExists = false;
					for (GPSolid s : multiLineSolids)
					{
						GPLine l = null;
						System.out.println("Comparing to BasePair:");
						System.out.println(s.getBasePair()[0].getName() + "\n" + s.getBasePair()[1].getName());
						// TODO unchecked access...
						// (NullPointerException)
						if (s.getBasePair()[0].getName().substring(0, 5).equals("proj_"))
							l = s.getBasePair()[0];
						else if (s.getBasePair()[1].getName().substring(0, 5).equals("proj_"))
							l = s.getBasePair()[1];

						if (l != null)
						{
							// System.out.println("COMPARING [" + v.toString(2)
							// + "|" + w.toString(2) + "] == ["
							// + l.getStart().toString(2) + "|" +
							// l.getEnd().toString(2) + "]");
							if ((v.equals(l.getStart()) || v.equals(l.getEnd()))
									&& (w.equals(l.getStart()) || w.equals(l.getEnd())))
							{
								solidExists = true;

								// Solid gefunden, keine weiteren Vergleiche
								// notwendig
								break;
							}
						}
					}

					if (!solidExists)
					{
						// Luecke gefunden!

						System.out.println("$$$ Filling GAP between " + v.toString(2) + " and " + w.toString(2));

						GPLine gapLine = new GPLine(multiLine.getName() + "_gapLine_" + vIndex + "-" + wIndex, v, w);

						GPLine gapProjection = new GPLine(gapLine.getName() + "_projection_" + vIndex + "-" + wIndex,
								v.add(distanceVector), w.add(distanceVector));

						GPSolid solid = new GPSolid();
						solid.addLine(gapLine);
						solid.addLine(gapProjection);

						solid.setBasePair(new GPLine[] { gapLine, gapProjection });

						System.out.println("ADDING MULTIGAP SOLID " + solid.toString());
						data.addSolid(solid);

						usedGapVectors.add(v);
						usedGapVectors.add(w);
					}

				}
			}

		}
	}

	private SortedMap<Double, Vector> sortProjectedPointsOnMultiline(List<Vector> projectedPoints, GPLine multiLine)
	{

		SortedMap<Double, Vector> pointsWithSkalar = new TreeMap<>();

		Vector dirVecMultiLine = GPUtility.substractOtherVector(multiLine.getEnd(), multiLine.getStart());

		for (Vector point : projectedPoints)
		{
			Double lambda_x = null;
			Double lambda_y = null;

			// alternativ ginge hier auch dirVecMultiLine.get(0) != 0
			// (Richtungsvektor ist p.D. 0 an der relevanten Ordinate)
			if (multiLine.getStart().get(0) != multiLine.getEnd().get(0))
			{
				lambda_x = calculateLambda(point.get(0), multiLine.getStart().get(0), dirVecMultiLine.get(0));
			}
			if (multiLine.getStart().get(1) != multiLine.getEnd().get(1))
			{
				lambda_y = calculateLambda(point.get(1), multiLine.getStart().get(1), dirVecMultiLine.get(1));
			}

			// if (lambda_x != null)
			// System.out.println("Lambda X: " + lambda_x);
			// if (lambda_y != null)
			// System.out.println("Lambda Y: " + lambda_y);
			//
			if (lambda_x != null && lambda_y == null)
				pointsWithSkalar.put(lambda_x, point);
			else if (lambda_x == null && lambda_y != null)
				pointsWithSkalar.put(lambda_y, point);
			else if (lambda_x != null && lambda_y != null)
			{
				if (lambda_x == lambda_y)
					pointsWithSkalar.put(lambda_x, point);
				// else
				// {
				// System.out.println("Skalare Ergebnisse sind nicht eindeutig;
				// Vector liegt nicht auf Multiline");
				// }
			}
		}

		// System.out.println("SKALARS: ");
		// for (Entry<Double, Vector> e : pointsWithSkalar.entrySet())
		// {
		// String vectorString = e.getValue().toString(2);
		// System.out.println(vectorString.substring(2, vectorString.length() -
		// 2) + ": " + e.getKey());
		// }

		return pointsWithSkalar;
	}

	private double calculateLambda(double p, double s, double r)
	{
		System.out.println("(" + p + " - " + s + ") / " + r + ")");

		BigDecimal bd_P = BigDecimal.valueOf(p);
		BigDecimal bd_S = BigDecimal.valueOf(s);
		BigDecimal bd_R = BigDecimal.valueOf(r);

		BigDecimal bd_PminusS = bd_P.subtract(bd_S);
		return bd_PminusS.divide(bd_R, 5, RoundingMode.HALF_DOWN).doubleValue();
	}

	private Vector getEqualWithToleranceVector(Vector current, List<Vector> projectedPoints, double tolerance)
	{
		for (Vector vector : projectedPoints)
		{
			if (compareVectorsWithTolerance(current, vector, tolerance))
				return vector;
		}
		return null;
	}

	private boolean compareVectorsWithTolerance(Vector a, Vector b, double tolerance)
	{
		double[] aCoords = a.data();
		double[] bCoords = b.data();

		// IndexOutOfBoundsException vermeiden
		if (aCoords.length == bCoords.length)
		{
			// die Ordinaten vergleichen
			for (int i = 0; i < aCoords.length; i++)
			{
				// Wenn Differenz auserhalb von -tolerance
				// und tolerance,
				// dann nicht nahezu identisch
				double diff = aCoords[i] - bCoords[i];
				if (diff > tolerance || diff < 0 - tolerance)
					return false;
			}
		}
		return true;
	}

	private Map<Vector, Integer> buildIndexedMapOfProjectedPoints(SortedMap<Double, Vector> pointsWithSkalar)
	{
		Map<Vector, Integer> indexedProjectedPoints = new HashMap<>();

		int i = 0;
		for (Entry<Double, Vector> e : pointsWithSkalar.entrySet())
		{
			indexedProjectedPoints.put(e.getValue(), i);
			i++;
		}

		return indexedProjectedPoints;
	}

	private Map<Integer, Map<List<Vector>, GPSolid[]>> sortSolidsAfterAlignedCorners()
	{
		// TODO import value from xml config
		double TOLERANCE = data.getGPConfig().getValue(GPConfig.POINT_TOLERANCE);

		Map<Integer, Map<List<Vector>, GPSolid[]>> sortedSolidsMap = new HashMap<>();
		sortedSolidsMap.put(1, new HashMap<>());
		sortedSolidsMap.put(2, new HashMap<>());
		sortedSolidsMap.put(3, new HashMap<>());
		sortedSolidsMap.put(4, new HashMap<>());

		System.out.println("#=======================#");
		System.out.println("#   Analysing Corners   #");
		System.out.println("#=======================#");

		List<GPSolid> allSolids = data.getAllSolids();
		List<GPSolid> copiedAllSolidsList = GPUtility.cloneGPSolidList(allSolids);
		// alle Solids untereinander vergleichen
		for (GPSolid s : allSolids)
		{
			for (GPSolid sCopy : copiedAllSolidsList)
			{
				// ausser identische
				if (!s.equals(sCopy))
				{
					int cornerHits = 0;
					List<Vector> matchedCorners = new ArrayList<>();

					List<Vector> solidCorners = s.getBaseLinesVectorList();
					List<Vector> solidCopyCorners = sCopy.getBaseLinesVectorList();
					for (Vector v : solidCorners)
					{
						for (Vector vCopy : solidCopyCorners)
						{
							if (compareVectorsWithTolerance(v, vCopy, TOLERANCE))
							{
								cornerHits++;
								System.out.println("Shared Corner #" + cornerHits + ": " + s.toString() + " + "
										+ sCopy.toString());
								matchedCorners.add(v);
							}
						}
					}
					if (cornerHits > 0)
					{
						System.out.println("--> ADDING SOLIDS TO LIST [" + cornerHits + "]: " + s.toString() + " + "
								+ sCopy.toString());
						GPSolid[] sArray = new GPSolid[2];
						sArray[0] = s;
						sArray[1] = sCopy;

						if (sortedSolidsMap.containsKey(cornerHits))
						{
							sortedSolidsMap.get(cornerHits).put(matchedCorners, sArray);
						}
						else
							System.out.println("!!!!!!!! TOO MANY SHARED CORNERS: " + cornerHits + "!!!!!!!!!");
					}

				}
			}
			// Mehrfachvergleiche unterbinden
			copiedAllSolidsList.remove(s);
		}

		System.out.println("#=======================#");
		System.out.println("#        RESULTS        #");
		System.out.println("#=======================#");
		for (Entry<Integer, Map<List<Vector>, GPSolid[]>> e : sortedSolidsMap.entrySet())
		{
			System.out.println("Solids with " + e.getKey() + " shared Corners: " + e.getValue().size());
		}
		return sortedSolidsMap;
	}

	private void analyzeMatchedCornersAndBuildMissingSolids(
			Map<Integer, Map<List<Vector>, GPSolid[]>> solidsSortedAfterCorners)
	{
		Map<List<Vector>, GPSolid[]> singleMatchedCornerSolidsMap = solidsSortedAfterCorners.get(1);
		Map<List<Vector>, GPSolid[]> doubleMatchedCornerSolidsMap = solidsSortedAfterCorners.get(2);

		Map<GPSolid, Integer> repetitionsOfSolids = new HashMap<>();
		Map<GPSolid, List<Vector>> matchedCornersOfSolids = new HashMap<>();
		for (Entry<List<Vector>, GPSolid[]> e : singleMatchedCornerSolidsMap.entrySet())
		{
			for (int i = 0; i < e.getValue().length; i++)
			{
				GPSolid solid = e.getValue()[i];
				if (!repetitionsOfSolids.containsKey(solid))
				{
					repetitionsOfSolids.put(solid, 0);
				}
				if (!matchedCornersOfSolids.containsKey(solid))
				{
					matchedCornersOfSolids.put(solid, new ArrayList<>());
				}

				int now = repetitionsOfSolids.get(solid);
				repetitionsOfSolids.put(solid, now + 1);
				for (Vector v : e.getKey())
					matchedCornersOfSolids.get(solid).add(v);
			}

			Vector v = e.getKey().get(0);
			int repetitionOfCorner = 0;
			for (Entry<List<Vector>, GPSolid[]> entry : doubleMatchedCornerSolidsMap.entrySet())
			{
				if (entry.getKey().contains(v))
					repetitionOfCorner++;
			}

			if (repetitionOfCorner == 0)
				System.out.println("HEUREKA!! ICH hab SIE GEFUNDEN!" + v.toString(2));

			// TODO relevante Vectoren in Solids umwandeln
		}

		for (Entry<GPSolid, Integer> e : repetitionsOfSolids.entrySet())
		{
			System.out.println(e.getKey().toString() + " repetitions: " + e.getValue());
		}
	}

}

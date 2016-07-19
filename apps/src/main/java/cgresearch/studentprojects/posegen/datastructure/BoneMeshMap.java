package cgresearch.studentprojects.posegen.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cgresearch.core.math.Ray3D;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;

/**
 * Mapping Bones > Mesh (Triangles)
 * 
 * @author Lars
 *
 */
public class BoneMeshMap {

	private ITriangleMesh mesh = null;
	private List<Bone> bones = null;
	private HashMap<Integer, List<ValuePair<IVertex, Double>>> boneVertexMap;// =
																				// new
																				// HashMap<>();

	public BoneMeshMap(ITriangleMesh mesh, List<Bone> bones) {
		this.mesh = mesh;
		this.bones = bones;

		// for (Bone bone : this.bones) {
		// updateBonesSelectedMesh(bone.getId()); // Init. falls nichts an dem
		// // bonehängt hat er sonst
		// // keine selected mesh map
		// }

		autoLinkTrianglesToBones();
	}

	// private void linkBoneToTriangles(Integer boneId, List<IVertex> vertices,
	// Double weight) {
	// if (null == boneId) {
	// return; // No bone selected
	// }
	// if (!boneVertexMap.containsKey(boneId)) { // New bone
	// boneVertexMap.put(boneId, new LinkedList<ValuePair<IVertex, Double>>());
	// }
	//
	// List<ValuePair<IVertex, Double>> list = boneVertexMap.get(boneId);
	// list.addAll(vertices);
	//
	// // Remove duplicates
	// Set<IVertex> listAsSet = new HashSet<>(list);
	// boneVertexMap.remove(boneId);
	// boneVertexMap.put(boneId, new LinkedList<>(listAsSet));
	//
	// updateBonesSelectedMesh(boneId);
	// }

	// private List<IVertex> entryList; // Temp var, to save time and not init

	/**
	 * Add a bone to a triangle. Without updating updateBonesSelectedMesh(id)!
	 * ATTENTION: You need to call finalizeAutoRigg(List<Bone> bones) after all
	 * bones were added
	 * 
	 * @param boneId
	 *            id of the bone to link to
	 * @param triangle
	 *            triangle to link to the bone
	 * @param factor
	 *            factor how strong is it connected. (0..1)
	 */
	private void linkOneBoneToVertex(Integer boneId, IVertex vertex, Double weight) {
		if (null == boneId) {
			return; // No bone selected
		}
		if (!boneVertexMap.containsKey(boneId)) { // New bone
			boneVertexMap.put(boneId, new LinkedList<ValuePair<IVertex, Double>>());
		}

		List<ValuePair<IVertex, Double>> entryList = boneVertexMap.get(boneId);
		entryList.add(new ValuePair<IVertex, Double>(vertex, weight));
	}

	/**
	 * Has to be called after linkOneBoneToTriangle(..) use
	 * 
	 * @param bones
	 *            bones. To get all ids
	 */
	private void finalizeAutoRigg(List<Bone> bones) {
		recalculateAllWeights();
		
		
		for (Bone bone : bones) {
			updateBonesSelectedMesh(bone.getId());
		}

		
	}

	private void recalculateAllWeights() {
		// private HashMap<Integer, List<ValuePair<IVertex, Double>>>
		// boneVertexMap;
		HashMap<IVertex, List<ValuePair<Integer, Double>>> vertexWeights = new HashMap<>();

		for (Integer boneId : boneVertexMap.keySet()) {
			for (ValuePair<IVertex, Double> valPair : boneVertexMap.get(boneId)) {
				IVertex vex = valPair.getValue1();
				Double weight = valPair.getValue2();
				if (!vertexWeights.containsKey(vex)) { // Noch kein wert
					vertexWeights.put(vex, new ArrayList<>());
				}
				vertexWeights.get(vex).add(new ValuePair<Integer, Double>(boneId, weight));
			}
		}

		for (IVertex vertex : vertexWeights.keySet()) {
			List<ValuePair<Integer, Double>> valuePair = vertexWeights.get(vertex);
			recalculateWeights(vertex, valuePair);
		}
		// System.out.println("Gesamtweight: " + vertexWeights.get(vertex));
	}

	private void recalculateWeights(IVertex vertex, List<ValuePair<Integer, Double>> listWeights) {
		Double weightSum = 0.0;

		// Get the total Weight sum
		for (ValuePair<Integer, Double> boneWeight : listWeights) {
			// Integer boneId = boneWeight.getValue1();
			Double weight = boneWeight.getValue2();
			weightSum += weight;
		}

		// Recalculate all weights to make them add up to 1.0;
		for (ValuePair<Integer, Double> boneWeight : listWeights) {
			Integer boneId = boneWeight.getValue1();
			Double weight = boneWeight.getValue2();
			Double newWeight = weight / weightSum * 1.0;
			setBoneWeightForVertex(boneId, vertex, newWeight);
		}
	}

	private void setBoneWeightForVertex(Integer BoneId, IVertex vertex, Double newWeight) {
		List<ValuePair<IVertex, Double>> valPairList = boneVertexMap.get(BoneId);
		for (ValuePair<IVertex, Double> valPair : valPairList) { // Iterate all
																	// vertices
																	// from this
																	// bone
			if (vertex.equals(valPair.getValue1())) { // If this is the vertex
														// to update
				valPair.setValue2(newWeight); // Update it.
			}
		}
	}

	private void updateBonesSelectedMesh(Integer boneId) {
		SelectedMesh selectedMesh = new SelectedMesh(mesh, boneVertexMap.get(boneId));
		Bone bone = Bone.getBoneById(boneId);
		bone.setSelectedMesh(selectedMesh);
	}

	public void autoLinkTrianglesToBones() {
		boneVertexMap = new HashMap<>();
		// https://groups.google.com/forum/#!topic/de.sci.mathematik/Wrl62qeQAiE

		IVertex vertex;
		// For each triangle, test all bones;
		Double distance;
		for (int i = mesh.getNumberOfVertices() - 1; i > 0; i--) {
			vertex = mesh.getVertex(i);
			for (Bone bone : bones) {
				distance = getDistanceToBone(bone, vertex);
				linkOneBoneToVertex(bone.getId(), vertex, gaussValue(distance));
			}

		}

		finalizeAutoRigg(bones);
	}

	private double sigma = 0.50;// 0.001;
	private double my = 0.0; // Verschiebung
	private double inputFaktor = 5.0; // Entfernung vervielfachen
	private double epsilon = 0.01; // Abschneiden minimaler ergebnisse ab x

	private double gaussValue(double entfernung) {
		double x = entfernung * inputFaktor;
		double res = 1.0 / (sigma * Math.sqrt(2 * Math.PI));
		double xMySigma = -Math.pow((x - my / sigma), 2.0) / 2.0;
		double resExpo = Math.exp(xMySigma);
		double result = res * resExpo;

		if (result < epsilon) {
			return 0.0;
		} else {
			return result;
		}
		// return 1.0 / (sigma * Math.sqrt(2.0 * Math.PI)) *
		// Math.exp(-Math.pow(x-my / sigma, 2.0) / 2.0);
	}

	/*
	 * Working, but disabled private Bone getClosestBone(IVertex vertex,
	 * List<Bone> bones) { Double currentMin = Double.MAX_VALUE; Bone
	 * currentClosestBone = null;
	 * 
	 * for (Bone bone : bones) { Vector vertexPosition = vertex.getPosition();
	 * double distance = distancePointToSegment(vertexPosition,
	 * bone.getStartPosition(), bone.getEndPosition());
	 * 
	 * if (distance < currentMin) { currentMin = distance; currentClosestBone =
	 * bone; } }
	 * 
	 * return currentClosestBone; }
	 */

	private Double getDistanceToBone(Bone bone, IVertex vertex) {
		Vector vertexPosition = vertex.getPosition();
		Double distance = distancePointToSegment(vertexPosition, bone.getStartPosition(), bone.getEndPosition());
		return distance;
	}

	private final Vector NULL_VECTOR_3DIM = new Vector(0, 0, 0);
	// Temporary Vars.
	private Vector line;
	private Vector lineToPoint;
	private Vector lineEnd;
	private Vector lineEndToPoint;

	/**
	 * Calculate closest distance from one point to a segment (2 points) If
	 * segments starts and ends in the same position.(Just a point). The
	 * distance to that point is returned
	 * 
	 * First calculates the distance to the line. Then calculates if the line
	 * has been hit outside the segment Thus one end of the segment ist closer
	 * and the distance to that point is returned.
	 * 
	 * @param point
	 * @param startSegment
	 * @param endSegment
	 * @return
	 */
	private double distancePointToSegment(Vector point, Vector startSegment, Vector endSegment) {
		// Segment. Starts and Ends in the same point -> Distance == distance
		if (startSegment.subtract(endSegment).equals(NULL_VECTOR_3DIM)) {
			return point.subtract(startSegment).getNorm(); // Length of a-b
		}
		// https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
		/*
		 * es reicht aber aus, wenn man zusätzlich noch die Länge AB der Strecke
		 * kennt. (Die Strecke sei AB, der Punkt P.) Dann kann man nämlich
		 * testen, ob der Winkel bei A (PB²>PA²+AB²) oder bei B (PA²>PB²+AB²)
		 * stumpf (>90 <180grad) ist. Im ersten Fall ist PA, im zweiten PB der
		 * kürzeste Abstand, ansonsten ist es der Abstand P-Gerade(AB).
		 */

		// TESTEN WO ES AM NÄCHSTEN AN DER STRECKE IST,NICHT NUR AN DER GERADEN
		line = startSegment.subtract(endSegment); // Vektor von start zum end.
		lineToPoint = startSegment.subtract(point); // vom start zum punkt.
		lineEnd = endSegment.subtract(startSegment); // vom ende zum start
		lineEndToPoint = endSegment.subtract(point); // vom start zum ende

		// Wenn größer 90Grad-> closest ist start
		double winkelStart = getWinkel(line, lineToPoint);
		// Wenn größer 90Grad -> closest ist end
		double winkelEnde = getWinkel(lineEnd, lineEndToPoint);
		if (winkelStart > 90.0 && winkelStart < 180.0) {
			// Start ist am nächsten. Der winkel ist über 90Grad somit wurde nur
			// der nächste nachbar auf der geraden ausserhalb der strecke
			// gefunden
			return startSegment.subtract(point).getNorm();
		}
		if (winkelEnde > 90.0 && winkelEnde < 180.0) {
			// Ende ist am nächsten
			return endSegment.subtract(point).getNorm();
		}

		// If not returned yet. The closest position ist on the line
		return closestDistanceFromPointToEndlessLine(point, startSegment, endSegment);
	}

	/**
	 * Calculate the closest distance from a position towards a line. Line is
	 * described as any two points on that line
	 * 
	 * @param point
	 *            The position from where the line should be reached.
	 * @param startSegment
	 *            Point one on the Line
	 * @param endSegment
	 *            A second point forming the line
	 * @return
	 */
	private double closestDistanceFromPointToEndlessLine(Vector point, Vector startSegment, Vector endSegment) {
		// Generate a line out of two points.
		Ray3D gerade = geradeAusPunkten(startSegment, endSegment);
		Vector direction = gerade.getDirection();
		double directionNorm = direction.getNorm();
		// d(PunktA, (line[B, directionU]) = (B-A) cross directionU durch norm u
		Vector b = gerade.getPoint();
		Vector ba = b.subtract(point);
		double factor = 1.0 / directionNorm; // Geteilt durch geraden länge
		Vector distance = ba.cross(direction).multiply(factor);
		double distanceToGerade = distance.getNorm();
		return distanceToGerade;
	}

	/**
	 * Der winkel zwischen zwei richtungsvektoren in Degrees
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private double getWinkel(Vector a, Vector b) {
		double zaehler = a.multiply(b);
		double nenner = a.getNorm() * b.getNorm();
		// nenner += 0.0001;
		double returnvalue = zaehler / nenner;
		returnvalue = Math.acos(returnvalue);
		returnvalue = Math.toDegrees(returnvalue);

		return returnvalue;
	}

	/**
	 * Ermittel die Gerade aus zwei Punkten. Returned punkt mit richtungsvektor
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private Ray3D geradeAusPunkten(Vector a, Vector b) {
		Vector verbindung = b.subtract(a);
		Ray3D ray = new Ray3D(a, verbindung);
		return ray;
	}

}

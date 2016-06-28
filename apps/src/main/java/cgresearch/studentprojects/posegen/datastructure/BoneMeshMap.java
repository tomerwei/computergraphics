package cgresearch.studentprojects.posegen.datastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cgresearch.core.math.Ray3D;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Mapping Bones > Mesh (Triangles)
 * 
 * @author Lars
 *
 */
public class BoneMeshMap {

	private ITriangleMesh mesh = null;
	private List<Bone> bones = null;
	private HashMap<Integer, List<ITriangle>> boneTriangleMap;// = new HashMap<>();

	public BoneMeshMap(ITriangleMesh mesh, List<Bone> bones) {
		this.mesh = mesh;
		this.bones = bones;
		
//		for (Bone bone : this.bones) {
//			updateBonesSelectedMesh(bone.getId()); // Init. falls nichts an dem
//													// bonehängt hat er sonst
//													// keine selected mesh map
//		}
		
		autoLinkTrianglesToBones();
	}

	public void linkBoneToTriangles(Integer boneId, List<ITriangle> triangles) {
		if (null == boneId) {
			return; // No bone selected
		}
		if (!boneTriangleMap.containsKey(boneId)) { // New bone
			boneTriangleMap.put(boneId, new LinkedList<ITriangle>());
		}

		List<ITriangle> list = boneTriangleMap.get(boneId);
		list.addAll(triangles);

		// Remove duplicates
		Set<ITriangle> listAsSet = new HashSet<>(list);
		boneTriangleMap.remove(boneId);
		boneTriangleMap.put(boneId, new LinkedList<>(listAsSet));

		updateBonesSelectedMesh(boneId);
	}

	private List<ITriangle> entryList; // Temp var, to save time and not init

	/**
	 * Add a bone to a triangle. Without updating updateBonesSelectedMesh(id)!
	 * ATTENTION: You need to call finalizeAutoRigg(List<Bone> bones) after all
	 * bones were added
	 * 
	 * @param boneId
	 *            id of the bone to link to
	 * @param triangle
	 *            triangle to link to the bone
	 */
	private void linkOneBoneToTriangle(Integer boneId, ITriangle triangle) {
		if (null == boneId) {
			return; // No bone selected
		}
		if (!boneTriangleMap.containsKey(boneId)) { // New bone
			boneTriangleMap.put(boneId, new LinkedList<ITriangle>());
		}

		entryList = boneTriangleMap.get(boneId);
		entryList.add(triangle);
	}

	/**
	 * Has to be called after linkOneBoneToTriangle(..) use
	 * 
	 * @param bones
	 *            bones. To get all ids
	 */
	private void finalizeAutoRigg(List<Bone> bones) {
		for (Bone bone : bones) {
			updateBonesSelectedMesh(bone.getId());
		}

	}

	private void updateBonesSelectedMesh(Integer boneId) {
		SelectedMesh selectedMesh = new SelectedMesh(mesh, boneTriangleMap.get(boneId));
		Bone bone = Bone.getBoneById(boneId);
		bone.setSelectedMesh(selectedMesh);
	}

	public void autoLinkTrianglesToBones() {
		boneTriangleMap = new HashMap<>();
		// https://groups.google.com/forum/#!topic/de.sci.mathematik/Wrl62qeQAiE

		ITriangle triangle;
		// For each triangle, test all bones;
		for (int i = mesh.getNumberOfTriangles() - 1; i > 0; i--) {
			triangle = mesh.getTriangle(i);
			Bone closestBone = getClosestBone(triangle, bones);
			linkOneBoneToTriangle(closestBone.getId(), triangle);
		}

		finalizeAutoRigg(bones);
	}

	private Bone getClosestBone(ITriangle triangle, List<Bone> bones) {
		Double currentMin = Double.MAX_VALUE;
		Bone currentClosestBone = null;

		for (Bone bone : bones) {
			int indexTrianglePosition = triangle.get(0);
			Vector facePosition = this.mesh.getVertex(indexTrianglePosition).getPosition();
			double distance = distancePointToSegment(facePosition, bone.getStartPosition(), bone.getEndPosition());

			if (distance < currentMin) {
				currentMin = distance;
				currentClosestBone = bone;
			}
		}

		return currentClosestBone;
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

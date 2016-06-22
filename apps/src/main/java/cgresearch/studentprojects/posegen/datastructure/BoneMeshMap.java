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

/**
 * Mapping Bones > Mesh (Triangles)
 * 
 * @author Lars
 *
 */
public class BoneMeshMap {

	private ITriangleMesh mesh = null;

	private HashMap<Integer, List<ITriangle>> boneTriangleMap = new HashMap<>();

	public BoneMeshMap(ITriangleMesh mesh, List<Bone> bones) {
		this.mesh = mesh;
		for (Bone bone : bones) {
			updateBonesSelectedMesh(bone.getId()); // Init. falls nichts an dem
													// bonehängt hat er sonst
													// keine selected mesh map
		}

		autoLinkTrianglesToBones(bones);
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

	private void updateBonesSelectedMesh(Integer boneId) {
		SelectedMesh selectedMesh = new SelectedMesh(mesh, boneTriangleMap.get(boneId));
		Bone bone = Bone.getBoneById(boneId);
		bone.setSelectedMesh(selectedMesh);
	}

	public void autoLinkTrianglesToBones(List<Bone> bones) {
		// https://groups.google.com/forum/#!topic/de.sci.mathematik/Wrl62qeQAiE

		// TODO derzeit gerade und nicht bone-strecke. Noch testen ob das an
		// einer seite näher ist.

		// Vector point = new Vector(0.0, 0.0, 0.0);
		// Vector start = new Vector(2.0, 2.0, 0.0);
		// Vector end = new Vector(2.0, 2.0, 0.0);
		// double distance = distancePointToSegment(point, start, end);
		// System.out.println("BoneMeshMap.autoLinkTrianglesToBones Distance =:
		// " + distance);

		// For each triangle, test all bones;
		for (int i = mesh.getNumberOfTriangles() - 1; i > 0; i--) {
			ITriangle triangle = mesh.getTriangle(i);
			List<ITriangle> trianglesList = new ArrayList<>(); // GENERATE LIST
																// overkill,
																// einfach nur
																// adder methode
																// für 1 elem
																// schreiben
			trianglesList.add(triangle);
			Bone closestBone = getClosestBone(triangle, bones);

			linkBoneToTriangles(closestBone.getId(), trianglesList);
		}

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

	/**
	 * Calculate closest distance from one point to a segment (2 points) If
	 * segments starts and ends in the same position.(Just a point). The
	 * distance to that point is returned
	 * 
	 * First calculates the distance to the line. Then calculates if the line has been hit outside the segment
	 * Thus one end of the segment ist closer and the distance to that point is returned.
	 * 
	 * @param point
	 * @param startSegment
	 * @param endSegment
	 * @return
	 */
	private double distancePointToSegment(Vector point, Vector startSegment, Vector endSegment) {
		// Segment. Starts and Ends in the same point -> Distance == distance
		// point and segment-point
		if (startSegment.subtract(endSegment).equals(NULL_VECTOR_3DIM)) {
			return point.subtract(startSegment).getNorm(); // Length of a-b
		}
		
		
		
		
		

		// https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
		/*
		 es reicht aber aus, wenn man zusätzlich noch die Länge AB der Strecke
		kennt. (Die Strecke sei AB, der Punkt P.) Dann kann man nämlich testen,
		ob der Winkel bei A (PB²>PA²+AB²) oder bei B (PA²>PB²+AB²) stumpf (>90 <180grad) ist.
		Im ersten Fall ist PA, im zweiten PB der kürzeste Abstand, ansonsten ist
		es der Abstand P-Gerade(AB).
		 */
		Ray3D gerade = geradeAusPunkten(startSegment, endSegment);

		Vector direction = gerade.getDirection();
		double directionNorm = direction.getNorm();
		// d(PunktA, (line[B, directionU]) = (B-A) cross directionU durch norm u
		Vector b = gerade.getPoint();
		Vector ba = b.subtract(point);
		double factor = 1.0 / directionNorm; // Geteilt durch geraden länge
		Vector distance = ba.cross(direction).multiply(factor);
		double distanceToGerade = distance.getNorm();
		
		//TESTEN WO ES AM NÄCHSTEN AN DER STRECKE IST,NICHT NUR AN DER GERADEN
		
		Vector line = startSegment.subtract(endSegment); //Vektor von start zum end.
		Vector lineToPoint = startSegment.subtract(point); //Vektor vom start zum punkt. 
		
		Vector lineEnd = endSegment.subtract(startSegment); //Vektor vom ende zum start
		Vector lineEndToPoint = endSegment.subtract(point); //Vektor vom start zum ende
		
		double winkelStart = getWinkel(line, lineToPoint); //Wenn größer 90Grad -> closest ist start
		double winkelEnde = getWinkel(lineEnd, lineEndToPoint);//Wenn größer 90Grad -> closest ist end
		winkelStart = Math.acos(winkelStart);
		winkelEnde = Math.acos(winkelEnde);
		winkelStart = Math.toDegrees(winkelStart);
		winkelEnde = Math.toDegrees(winkelEnde);
		if(winkelStart > 90.0 && winkelStart < 180.0){
			//Start ist am nächsten. Der winkel ist über 90Grad somit wurde nur der nächste nachbar auf der geraden ausserhalb der strecke gefunden
			double geradendistance = distance.getNorm();
			double startdistance = startSegment.subtract(point).getNorm();
			if(startdistance < geradendistance){
				System.out.println("This shouldnt happen " + startdistance + "  " + geradendistance);
			}
			return startSegment.subtract(point).getNorm();
		}
		if(winkelEnde > 90.0 && winkelEnde < 180.0){
			//Ende ist am nächsten
			double geradendistance = distance.getNorm();
			double enddistance = endSegment.subtract(point).getNorm();
			if(enddistance < geradendistance){
			}
			return endSegment.subtract(point).getNorm();
		}
		
		return distanceToGerade;
	}

	/**
	 * Der winkel zwischen zwei richtungsvektoren
	 * @param a
	 * @param b
	 * @return
	 */
	private double getWinkel(Vector a, Vector b){
		double zaehler = a.multiply(b);
		double nenner = a.getNorm() * b.getNorm();
		nenner += 0.0001;
		double returnvalue =zaehler/nenner;
//		System.out.println(returnvalue + " [Zaehler: " + zaehler + " Nenner: " + nenner + "][A:" + a + " B:" + b + "]");
		
		return returnvalue;
	}
	
	/**
	 * Ermittel die Gerade aus zwei Punkten. Returned punkt mit richtungsvektor
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

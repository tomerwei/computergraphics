package cgresearch.studentprojects.posegen.datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

public class Bone extends TriangleMesh {

	private static HashMap<Integer, Bone> boneIdMap = new HashMap<>();
	private static int currentMaxId = 0; // GlobalCounter to generate unique IDs
	private final int id; // Geht das beim laden/marshaling?!

	private Vector startBonePosition;
	private Vector endBonePosition;

	private Bone parentBone; // Startbone == null initen
	private List<Bone> childbonesAtEnd;

	private SelectedMesh selectedMesh = null;

	private BoneStartPositionPickup boneStartPositionPickup;
	private BoneEndPositionPickup boneEndPositionPickup = null;

	public BoneStartPositionPickup getStartPositionPickup() {
		return boneStartPositionPickup;
	}

	/**
	 * 
	 * @return null if no endPosition (leave) (childBones > 0)
	 */
	public BoneEndPositionPickup getEndPositionPickup() {
		if (childbonesAtEnd.size() == 0) {
			return boneEndPositionPickup;
		}
		return null;
	}

	public Bone(Bone parentBone, Vector endBonePositionOffset) { // Offset
																	// basiert
																	// auf
																	// basisstart
		this.id = currentMaxId;
		currentMaxId++;
		boneIdMap.put(this.id, this);

		childbonesAtEnd = new ArrayList<Bone>(); // Init empty list
		this.parentBone = parentBone;

		if (null == this.parentBone) {
			this.startBonePosition = new Vector(0.0, 0.0, 0.0);
		} else {
			this.startBonePosition = this.parentBone.getEndPosition();
			// Startposition = Endposition vom parentbone
			parentBone.registerABoneAsChild(this);
		}

		this.endBonePosition = startBonePosition.add(endBonePositionOffset);
		initBonePositionPickup();
		setColorNotSelected();
		updateAfterChange(); // Update Mesh, picking item etc.
	}

	private void initBonePositionPickup() {
		boneStartPositionPickup = new BoneStartPositionPickup(new Vector(startBonePosition), this);
		boneEndPositionPickup = new BoneEndPositionPickup(new Vector(endBonePosition), this);
	}

	public static Bone getBoneById(Integer boneId) {
		return boneIdMap.get(boneId);
	}

	public void setSelectedMesh(SelectedMesh selectedMesh) {
		this.selectedMesh = selectedMesh;
	}

	public void registerABoneAsChild(Bone bone) {
		// killEndBonePositionBickupMesh
		if (boneEndPositionPickup != null) {
			boneEndPositionPickup.clear();
			boneEndPositionPickup = null;
		}

		addChildBoneToList(bone);
	}

	public void updateAfterChange() {
		updateMesh();

		// Remove old picking item
		// Reset new picking item
	}

	private void setColor(Vector color) {
		this.material.setReflectionDiffuse(color); // Color
	}

	public void setColorSelected() {
		setColor(new Vector(1.0 / 255 * 60, 1.0 / 255 * 60, 1.0 / 255 * 155));
	}

	public void setColorNotSelected() {
		setColor(new Vector(1.0 / 255 * 240, 1.0 / 255 * 240, 1.0 / 255 * 120));
	}

	private void updateMesh() {
		this.clear();

		// At 0Deg
		Vector addV1 = new Vector(0.04, 0.04, -0.04); //Basis abstand vom start
		Vector addV2 = new Vector(0.04, -0.04, -0.04); //Basierend auf 0Degree rotation
		Vector addV3 = new Vector(0.04, 0.04, 0.04);
		Vector addV4 = new Vector(0.04, -0.04, 0.04);

		double rotation = getBoneRotation(startBonePosition, endBonePosition);
		rotation = Math.toDegrees(rotation);
		if (startBonePosition.get(1) > endBonePosition.get(1)) {
			rotation = rotation * -1.0;
		}

		Vector nullVector = new Vector(0, 0, 0);
		addV1 = rotatePositionByDegree(rotation, addV1, nullVector);
		addV2 = rotatePositionByDegree(rotation, addV2, nullVector);
		addV3 = rotatePositionByDegree(rotation, addV3, nullVector);
		addV4 = rotatePositionByDegree(rotation, addV4, nullVector);

		Vector v1 = startBonePosition.add(addV1);
		Vector v2 = startBonePosition.add(addV2);
		Vector v3 = startBonePosition.add(addV3);
		Vector v4 = startBonePosition.add(addV4);

		// Vector v2 = new Vector(this.startBonePosition.get(0) - 0.05,
		// this.startBonePosition.get(1) - 0.05, this.startBonePosition.get(2) -
		// 0.05);

		// Vector v3 = new Vector(this.startBonePosition.get(0) + 0.05,
		// this.startBonePosition.get(1) - 0.05, this.startBonePosition.get(2) +
		// 0.05);
		// Vector v4 = new Vector(this.startBonePosition.get(0) - 0.05,
		// this.startBonePosition.get(1) - 0.05, this.startBonePosition.get(2) +
		// 0.05);
		Vector vTop = new Vector(this.startBonePosition.get(0), this.startBonePosition.get(1),
				this.startBonePosition.get(2));
		Vector vEnd = new Vector(this.endBonePosition.get(0), this.endBonePosition.get(1), this.endBonePosition.get(2));

		IVertex vertex1 = new Vertex(VectorFactory.createVector3(v1.get(0), v1.get(1), v1.get(2)));
		IVertex vertex2 = new Vertex(VectorFactory.createVector3(v2.get(0), v2.get(1), v2.get(2)));
		IVertex vertex3 = new Vertex(VectorFactory.createVector3(v3.get(0), v3.get(1), v3.get(2)));
		IVertex vertex4 = new Vertex(VectorFactory.createVector3(v4.get(0), v4.get(1), v4.get(2)));
		IVertex vertexTop = new Vertex(VectorFactory.createVector3(vTop.get(0), vTop.get(1), vTop.get(2)));
		IVertex vertexEnd = new Vertex(VectorFactory.createVector3(vEnd.get(0), vEnd.get(1), vEnd.get(2)));

		this.addVertex(vertexTop);
		this.addVertex(vertex1);
		this.addVertex(vertex2);
		this.addVertex(vertex3);
		this.addVertex(vertex4);
		this.addVertex(vertexEnd);

		this.addTriangle(0, 1, 2); // Top, left, right : front
		this.addTriangle(0, 1, 3); // Top left left front/back
		this.addTriangle(0, 3, 4); // Top left right back
		this.addTriangle(0, 4, 1); // Top right back/front

		this.addTriangle(5, 1, 2);
		this.addTriangle(5, 1, 3);
		this.addTriangle(5, 3, 4);
		this.addTriangle(5, 4, 1);

		// setColorNotSelected();
		this.updateRenderStructures();

	}

	// private void updateMesh() {
	// this.clear();
	// Vector v1 = new Vector(this.startBonePosition.get(0) + 0.05,
	// this.startBonePosition.get(1) + 0.05,
	// this.startBonePosition.get(2) - 0.05);
	//
	// Vector v2 = new Vector(this.startBonePosition.get(0) - 0.05,
	// this.startBonePosition.get(1) - 0.05,
	// this.startBonePosition.get(2) - 0.05);
	//
	// Vector v3 = new Vector(this.startBonePosition.get(0) - 0.05,
	// this.startBonePosition.get(1),
	// this.startBonePosition.get(2) + 0.05);
	// Vector vtop = new Vector(this.endBonePosition.get(0),
	// this.endBonePosition.get(1), this.endBonePosition.get(2));
	//
	// IVertex vertex1 = new Vertex(VectorFactory.createVector3(v1.get(0),
	// v1.get(1), v1.get(2)));
	// IVertex vertex2 = new Vertex(VectorFactory.createVector3(v2.get(0),
	// v2.get(1), v2.get(2)));
	// IVertex vertex3 = new Vertex(VectorFactory.createVector3(v3.get(0),
	// v3.get(1), v3.get(2)));
	// IVertex vertexTop = new Vertex(VectorFactory.createVector3(vtop.get(0),
	// vtop.get(1), vtop.get(2)));
	//
	// this.addVertex(vertexTop);
	// this.addVertex(vertex1);
	// this.addVertex(vertex2);
	// this.addVertex(vertex3);
	//
	// this.addTriangle(0, 1, 2);
	// this.addTriangle(0, 2, 3);
	// this.addTriangle(0, 1, 3);
	// this.addTriangle(1, 2, 3);
	//
	// // setColorNotSelected();
	// this.updateRenderStructures();
	//
	// }

	private double getBoneRotation(Vector vectorStart, Vector vectorEnd) {
		vectorStart = new Vector(vectorStart.subtract(vectorStart));
		vectorEnd = new Vector(vectorEnd.subtract(vectorStart));

		Vector u = vectorEnd.subtract(vectorStart);
		Vector aufXAchse = vectorStart.add(new Vector(1.0, 0, 0));
		Vector v = aufXAchse.subtract(vectorStart);

		double skalarProd = u.multiply(v);
		double lengthU = u.getNorm();
		double lengthV = v.getNorm();

		double nenner = lengthU * lengthV;

		double value = skalarProd / nenner;

		return Math.acos(value);

	}

	public Vector getEndPosition() {
		// return endBonePosition;
		return new Vector(endBonePosition);
	}

	public int getId() {
		return id;
	}

	// public void stretchBone(double faktor) {
	// double xEnd = this.endBonePosition.get(0);
	// double yEnd = this.endBonePosition.get(1);
	// double zEnd = this.endBonePosition.get(2);
	//
	// double xStart = this.startBonePosition.get(0);
	// double yStart = this.startBonePosition.get(1);
	// double zStart = this.startBonePosition.get(2);
	//
	// //Calculate new coords with faktor
	// double xNew = (xEnd - xStart) * faktor;
	// double yNew = (yEnd - yStart);// * faktor;
	// double zNew = (zEnd - zStart);// * faktor;
	//
	// //Calculate new
	// Vector endBonePositionOffset = new Vector(xNew, yNew, zNew);
	// Vector offset = endBonePositionOffset.subtract(endBonePosition); //How
	// far did it move?
	//
	// this.endBonePosition = startBonePosition.add(endBonePositionOffset);
	//
	//
	//
	//
	// for(Bone childbone: childbonesAtEnd){
	// childbone.moveBoneByOffset(offset);
	// }
	// // new vec2 x,y * faktor
	// // child bones move um vector
	// }

	private void moveBoneByOffset(Vector offset) {
		this.startBonePosition = this.startBonePosition.add(offset);
		this.endBonePosition = this.endBonePosition.add(offset);

		for (Bone childbone : childbonesAtEnd) {
			childbone.moveBoneByOffset(offset);
		}

		updateAfterChange();
	}

	public void moveBoneStartToPosition(Vector newPosition) {
		this.startBonePosition.copy(newPosition);
		if (null != parentBone) {
			parentBone.moveBoneEndToPosition(newPosition);
		}
		updateAfterChange();
	}

	public void moveBoneEndToPosition(Vector newPosition) {
		if (!(newPosition.equals(this.endBonePosition))) { // No endless loop,
			// if a child recalls this one

			this.endBonePosition.copy(newPosition);
			for (Bone bone : childbonesAtEnd) {
				bone.moveBoneStartToPosition(newPosition);

			}
		} else {
			// System.out.println(this.endBonePosition);
			// System.out.println(newPosition);
		}

		updateAfterChange();
	}

	public Vector getStartPosition() {
		return startBonePosition;
	}

	private void moveBoneEndByOffset(Vector offset) {
		// this.startBonePosition = this.startBonePosition.add(offset);
		this.endBonePosition = this.endBonePosition.add(offset);

		for (Bone childbone : childbonesAtEnd) {
			childbone.moveBoneByOffset(offset);
		}

		updateAfterChange();
	}

	private void addChildBoneToList(Bone bone) {
		// TODO check if start pos v. child korrekt

		childbonesAtEnd.add(bone);
	}

	// public void rotate(int degree) {
	// // Was drehen?
	// // Basis bone oder position? oder start und ende?
	//
	// // Rotationsmatrix enthält verdrehung um die Achse des Startpunkts
	// Matrix rotationsMatrix = new Matrix(2, 2);
	//
	// // TODO generate rotationsMatrix
	//
	// for (Bone childBone : childbonesAtEnd) {
	// childBone.movedByParentBone(rotationsMatrix);
	// }
	// }

	public double degToRad(double deg) {
		return (deg * Math.PI / 180);
	}

	/*
	 * public void rotate(double winkelDeg) {
	 * 
	 * double winkelRad = degToRad(winkelDeg); // xStrich = double xEnd =
	 * this.endBonePosition.get(0); double yEnd = this.endBonePosition.get(1);
	 * double zEnd = this.endBonePosition.get(2);
	 * 
	 * double xStart = this.startBonePosition.get(0); double yStart =
	 * this.startBonePosition.get(1); double zStart =
	 * this.startBonePosition.get(2);
	 * 
	 * xEnd -= xStart; //Auf den startpunkt verschoben yEnd -= yStart; zEnd -=
	 * zStart;
	 * 
	 * 
	 * double xStrich = (xEnd * Math.cos(winkelRad)) - (yEnd *
	 * Math.sin(winkelRad)); double yStrich = (yEnd * Math.cos(winkelRad)) +
	 * (xEnd * Math.sin(winkelRad));
	 * 
	 * // (xEnd * Math.cos(winkel)) + yEnd * Math.sin(winkel); double zStrich =
	 * zEnd;
	 * 
	 * this.endBonePosition = new Vector(xStrich, yStrich, zStrich);
	 * updateMesh(); }
	 */

	private void rotateUmBoneStart(double winkelDeg) {

		double winkelRad = degToRad(winkelDeg);
		// xStrich =
		double xEnd = this.endBonePosition.get(0);
		double yEnd = this.endBonePosition.get(1);
		double zEnd = this.endBonePosition.get(2);

		double xStart = this.startBonePosition.get(0);
		double yStart = this.startBonePosition.get(1);
		double zStart = this.startBonePosition.get(2);

		double xStrich = xStart + Math.cos(winkelRad) * (xEnd - xStart) - Math.sin(winkelRad) * (yEnd - yStart);
		double yStrich = yStart + Math.sin(winkelRad) * (xEnd - xStart) + Math.cos(winkelRad) * (yEnd - yStart);
		double zStrich = zEnd;

		this.endBonePosition = new Vector(xStrich, yStrich, zStrich);
		updateAfterChange();

		// Alle Childs um den drehpunkt dieses bones drehen
		for (Bone childbone : childbonesAtEnd) {
			childbone.rotateUmDrehpunkt(winkelDeg, new Vector(xStart, yStart, zStart)); // Drehung
																						// der
																						// childs
																						// um
																						// die
																						// achse
																						// dieses
																						// Bones
		}
	}

	public Vector rotatePositionByDegree(double winkelDeg, Vector toTurn, Vector drehPunkt) {
		double winkelRad = degToRad(winkelDeg);
		double xEnd = toTurn.get(0);
		double yEnd = toTurn.get(1);
		double zEnd = toTurn.get(2);

		double xDrehpunkt = drehPunkt.get(0);
		double yDrehpunkt = drehPunkt.get(1);

		double xStrich = xDrehpunkt + Math.cos(winkelRad) * (xEnd - xDrehpunkt)
				- Math.sin(winkelRad) * (yEnd - yDrehpunkt);
		double yStrich = yDrehpunkt + Math.sin(winkelRad) * (xEnd - xDrehpunkt)
				+ Math.cos(winkelRad) * (yEnd - yDrehpunkt);
		double zStrich = zEnd;

		Vector returnValue = new Vector(xStrich, yStrich, zStrich);

		return returnValue;

	}

	public void rotateUmDrehpunkt(double winkelDeg, Vector drehPunkt) {
		this.selectedMesh.rotateTrianglePositions(winkelDeg, drehPunkt); // Hier
																			// ?
		double winkelRad = degToRad(winkelDeg);
		double xEnd = this.endBonePosition.get(0);
		double yEnd = this.endBonePosition.get(1);
		double zEnd = this.endBonePosition.get(2);

		double xDrehpunkt = drehPunkt.get(0);
		double yDrehpunkt = drehPunkt.get(1);

		double xStrich = xDrehpunkt + Math.cos(winkelRad) * (xEnd - xDrehpunkt)
				- Math.sin(winkelRad) * (yEnd - yDrehpunkt);
		double yStrich = yDrehpunkt + Math.sin(winkelRad) * (xEnd - xDrehpunkt)
				+ Math.cos(winkelRad) * (yEnd - yDrehpunkt);
		double zStrich = zEnd;

		this.endBonePosition = new Vector(xStrich, yStrich, zStrich);

		// Start des bones rotieren
		xEnd = this.startBonePosition.get(0);
		yEnd = this.startBonePosition.get(1);
		zEnd = this.startBonePosition.get(2);
		xStrich = xDrehpunkt + Math.cos(winkelRad) * (xEnd - xDrehpunkt) - Math.sin(winkelRad) * (yEnd - yDrehpunkt);
		yStrich = yDrehpunkt + Math.sin(winkelRad) * (xEnd - xDrehpunkt) + Math.cos(winkelRad) * (yEnd - yDrehpunkt);

		zStrich = zEnd;
		this.startBonePosition = new Vector(xStrich, yStrich, zStrich);

		if (childbonesAtEnd.size() != 0) {
			for (Bone childbone : childbonesAtEnd) {
				if (null == childbone) {
					Logger.getInstance().error("This bone has a 'null' child");
				} else {
					childbone.rotateUmDrehpunkt(winkelDeg, drehPunkt);
				}

			}
		}
		// TODO startBone wieder mit dem endbone des parents gleichsetzen
		updateAfterChange();
	}

	private void movedByParentBone(Matrix rotationsMatrix) {
		startBonePosition = parentBone.getEndPosition();
		endBonePosition = endBonePosition; // ROTATE MIT MATRIX
		for (Bone childBone : childbonesAtEnd) {
			childBone.movedByParentBone(rotationsMatrix);
		}

	}

	// public void setEndPosition(Vector position) {
	// this.endBonePosition.copy(position);
	// }

	// public void moveAbsolute(Vector movement) {
	// this.startBonePosition = this.startBonePosition.add(movement);
	// this.endBonePosition = this.endBonePosition.add(movement);
	//
	// for (Bone childBone : childbonesAtEnd) {
	// childBone.moveAbsolute(movement);
	// }
	//
	// }

}

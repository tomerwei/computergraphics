package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;

public class BoneEndPositionPickup extends IBoneMovePositionPickup {

	private Vector position;
	private double radius = 0.06;
	private int resolution = 10;
	private boolean isActive = false;
	private final Bone parentBone; // Link to parent bone

	public BoneEndPositionPickup(Vector position, Bone parentBone) {
		this.position = new Vector(3);
		this.position.copy(position);
		this.parentBone = parentBone;
		updateMesh();
	}

	/**
	 * Creates a mesh sphere at the current position
	 */
	private void updateMesh() {
		this.clear();
		ITriangleMesh sphere = TriangleMeshFactory.createSphere(position, radius, resolution);
		sphere.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		this.initWithMesh(sphere);
		updateColor();
		this.updateRenderStructures();
	}

	/**
	 * Returns the current color corresponding to the selection status
	 * 
	 * @return
	 */
	private void updateColor() {
		if (isActive) {
			this.getMaterial().setReflectionDiffuse(Material.PALETTE0_COLOR2);
		} else {
			this.getMaterial().setReflectionDiffuse(Material.PALETTE0_COLOR3);
		}
	}

	public void pickedUp() {
		isActive = true;
		updateColor();
	}

	public void dropped() {
		isActive = false;
		updateColor();
	}

	public void dragged(Vector newPositionAsScreenCoords) {
		if (isActive) {
			double x = newPositionAsScreenCoords.get(0);
			double y = newPositionAsScreenCoords.get(1);
			position.set(0, x);
			position.set(1, y);
			parentBone.moveBoneEndToPosition(new Vector(x, y, 0.0));
		}
		updateMesh();
	}

	private void initWithMesh(ITriangleMesh other) {
		super.copyFrom(other);
	}
}

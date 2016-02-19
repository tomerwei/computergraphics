package cgresearch.apps.hlsvis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;

/**
 * Container for all movable objects
 * 
 * @author Philipp Jenke
 *
 */
public class Movable extends CgNode {

	public static String DHL_TEXTURE_ID = "DHL_TEXTURE_ID";

	/**
	 * Path objects
	 */
	protected List<Vector> path = new ArrayList<Vector>();

	/**
	 * Current interpolation value along the path
	 */
	protected double alpha = 0;

	/**
	 * Increment at each alpha
	 */
	private static final double ALPHA_DELTA = 0.001;

	/**
	 * Height field
	 */
	protected HeightField heightField;

	/**
	 * Constructor
	 */
	public Movable() {
		this("Movable", null);
	}

	/**
	 * Constructor.
	 */
	public Movable(String name, HeightField heightField) {
		super(new Transformation(), name);
		this.heightField = heightField;
	}

	/**
	 * Simulation tick.
	 */
	public void tick(Date currentDate) {
		if (path.size() >= 2) {
			double segmentLength = path.get(1).subtract(path.get(0)).getNorm();
			alpha += ALPHA_DELTA / segmentLength;
			if (alpha >= 1) {
				alpha = 0;
				moveToNextPathPoint();
			}
		}

		updateTranslation();
	}

	/**
	 * Access to the transformation content (used for the translation).
	 */
	protected Transformation getTransformation() {
		return (Transformation) getContent();
	}

	/**
	 * Update the current transformation node.
	 */
	private void updateTranslation() {
		Transformation transformation = getTransformation();
		if (path.size() >= 2) {
			Vector pos = path.get(0).multiply(1 - alpha)
					.add(path.get(1).multiply(alpha));
			pos.set(1, heightField.getHeight(pos.get(0), pos.get(2)));
			transformation.reset();
			transformation.addTranslation(pos);
			Matrix rotation = getOrientation();
			transformation.addTransformation(rotation);
		}
	}

	/**
	 * Get the orientation matrix of the movable.
	 */
	protected Matrix getOrientation() {
		Vector x = getNormalizedOrientation();
		Vector y = VectorMatrixFactory.newVector(0, 1, 0);
		Vector z = x.cross(y);
		return VectorMatrixFactory.newMatrix(x, y, z);
	}

	/**
	 * Return the normalized orientation of the movable.
	 */
	private Vector getNormalizedOrientation() {
		if (path.size() < 2) {
			return VectorMatrixFactory.newVector(1, 0, 0);
		} else {
			Vector dir = path.get(1).subtract(path.get(0));
			dir.normalize();
			return dir;
		}
	}

	/**
	 * Move to the next path point.
	 */
	public void moveToNextPathPoint() {
		if (path.size() >= 2) {
			Vector first = path.get(0);
			path.remove(0);
			path.add(first);
		}
	}

	/**
	 * Liefert wahr, wenn das Objekt das Ziel erreicht hat.
	 */
	public boolean destinationReached() {
		return false;
	}
}

package cgresearch.projects.simulation;

import cgresearch.core.logging.Logger;

public class Spring {

	/**
	 * Mass points at the end of the spring.
	 */
	private int[] massPoints = new int[2];

	/**
	 * Reset length of the spring.
	 */
	private final double restLength;

	/**
	 * Spring material property
	 */
	private double materialProperty = 3;

	/**
	 * Constructor
	 */
	public Spring(int massPoint1, int massPoint2, double restLength,
			double materialProperty) {
		massPoints[0] = massPoint1;
		massPoints[1] = massPoint2;
		this.restLength = restLength;
		this.materialProperty = materialProperty;
	}

	public int getFirstMassPoints() {
		return massPoints[0];
	}

	public int getSecondMassPoints() {
		return massPoints[1];
	}

	public double getRestLength() {
		return restLength;
	}

	public double getMaterialProperty() {
		return materialProperty;
	}

	public void setMaterialProperty(double materialProperty) {
		this.materialProperty = materialProperty;
	}

	public int getOtherMassPointIndex(int index) {
		if (index == massPoints[0]) {
			return massPoints[1];
		} else if (index == massPoints[1]) {
			return massPoints[0];
		} else {
			Logger.getInstance().error(
					"Spring.getOtherMassPointIndex: invalid index");
			return -1;
		}
	}
}

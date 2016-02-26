package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.MathHelpers;

public class BasisFunctionBezier implements IBasisFunction {

	/**
	 * Degree of the function.
	 */
	private final int degree;

	/**
	 * Constructor.
	 */
	public BasisFunctionBezier(int degree) {
		this.degree = degree;
	}

	@Override
	public double eval(int index, double t) {
		return MathHelpers.over(getDegree(), index) * Math.pow(t, index)
				* Math.pow(1 - t, getDegree() - index);
	}

	/**
	 * Getter.
	 */
	private int getDegree() {
		return degree;
	}
}

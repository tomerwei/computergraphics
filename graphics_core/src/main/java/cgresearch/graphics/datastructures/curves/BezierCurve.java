/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Implementation of a Bezier curve.
 * 
 * @author Philipp Jenke
 * 
 */
public class BezierCurve extends ICurve {

	private final IBasisFunction basisFunction;

	/**
	 * Constructor.
	 */
	public BezierCurve(int degree) {
		controlPoints = new IVector3[degree + 1];
		basisFunction = new BasisFunctionBezier(degree);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.datastructures.curves.ICurve#eval(double)
	 */
	@Override
	public IVector3 eval(double t) {
		IVector3 p = VectorMatrixFactory.newIVector3(0, 0, 0);
		for (int i = 0; i <= getDegree(); i++) {
			p = p.add(controlPoints[i].multiply(basisFunction.eval(i, t)));
		}
		return p;
	}

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.curves;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

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
		controlPoints = new Vector[degree + 1];
		basisFunction = new BasisFunctionBezier(degree);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.datastructures.curves.ICurve#eval(double)
	 */
	@Override
	public Vector eval(double t) {
		Vector p = VectorFactory.createVector3(0, 0, 0);
		for (int i = 0; i <= getDegree(); i++) {
			p = p.add(controlPoints[i].multiply(basisFunction.eval(i, t)));
		}
		return p;
	}

}

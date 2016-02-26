package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.functions.Function;
import cgresearch.core.math.functions.GradientDescent;
import cgresearch.core.math.functions.TerminatingConditionError;
import cgresearch.core.math.functions.TerminatingConditionNumberIterations;

/**
 * Test class for the abstract class Function
 * 
 * @author Philipp Jenke
 *
 */
public class FunctionTest {

	@Test
	public void testFunction() {

		// f(x,y) = (x-2)^2 + (y+0.5)^2 (Squared distance from point (2,-0.5))
		Function function = new Function() {
			@Override
			public int getDimension() {
				return 2;
			}

			@Override
			public double eval(double[] x) {
				return MathHelpers.sqr(x[0] - 2.0)
						+ MathHelpers.sqr(x[1] + 0.5);
			}

			@Override
			public double[] getGradient(double[] x) {
				// Use cached array - initialize once
				if (gradient == null) {
					gradient = new double[2];
				}
				gradient[0] = 2 * (x[0] - 2.0);
				gradient[1] = 2 * (x[1] + 0.5);
				return gradient;
			}
		};

		assertEquals(4.25, function.eval(new double[] { 0, 0 }), 1e-4);
		assertEquals(3.25, function.eval(new double[] { 1, 1 }), 1e-4);

		// Termination condition: 20 iterations
		GradientDescent gradientDescent = new GradientDescent(1e-1,
				new TerminatingConditionNumberIterations(20));
		double[] x = new double[] { 1, 1 };
		x = gradientDescent.optimize(function, x);
		double fx = function.eval(x);
		System.out.format("f(%.3f, %.3f) = %.5f\n", x[0], x[1], fx);
		assertEquals(0, fx, 1e-3);
		System.out.println();

		// Termination condition: error < 1-e5
		gradientDescent = new GradientDescent(1e-3,
				new TerminatingConditionError(1e-5));
		x = new double[] { 1, 1 };
		x = gradientDescent.optimize(function, x);
		fx = function.eval(x);
		System.out.format("f(%.3f, %.3f) = %.5f\n", x[0], x[1], fx);
		assertEquals(0, fx, 1e-3);

	}
}

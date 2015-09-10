package cgresearch.core.math.functions;

/**
 * Implementation of the Gradient descent approach to find minima of energy
 * functions.
 * 
 * @author Philipp Jenke
 *
 */
public class GradientDescent {

	public enum StepSizeUpdateMethod {
		CONSTANT, ADAPTIVE
	};

	private StepSizeUpdateMethod stepSizeUpdateMethod = StepSizeUpdateMethod.ADAPTIVE;

	/**
	 * Step size.
	 */
	double h = 1e-5;

	/**
	 * Cached arrays to adaptively adjust the step size.
	 */
	private double[] xhDouble = null;
	private double[] xhHalf = null;

	/**
	 * Termination condition
	 */
	private final TerminatingCondition terminationCondition;

	/**
	 * Constructor which initialized the step size value.
	 */
	public GradientDescent(double initialStepSize,
			TerminatingCondition terminationCondition) {
		this.terminationCondition = terminationCondition;
		h = initialStepSize;
	}

	/**
	 * Apply one step of the gradient descent approach. The step size is
	 * adaptively adjusted. Attention, the new values for x are saved in the
	 * same array!
	 */
	public double[] step(Function f, double[] x) {

		if (stepSizeUpdateMethod == StepSizeUpdateMethod.CONSTANT) {
			double[] df = f.getGradient(x);
			for (int i = 0; i < f.getDimension(); i++) {
				x[i] -= df[i] * h;
			}
			return x;
		} else if (stepSizeUpdateMethod == StepSizeUpdateMethod.ADAPTIVE) {
			double[] df = f.getGradient(x);
			if (xhHalf == null) {
				xhHalf = x.clone();
				xhDouble = x.clone();
			} else {
				System.arraycopy(x, 0, xhHalf, 0, x.length);
				System.arraycopy(x, 0, xhDouble, 0, x.length);
			}

			// Clone to check if doubling the step size h improves the result
			for (int i = 0; i < f.getDimension(); i++) {
				xhDouble[i] -= df[i] * h * 2.0;
				xhHalf[i] -= df[i] * h / 2.0;
				x[i] -= df[i] * h;
			}

			// Check if doubling the step size improves the result
			double fx = f.eval(x);
			double fxDouble = f.eval(xhDouble);
			double fxHalf = f.eval(xhHalf);

			// Choose the optimal step size
			if (fxDouble <= fx && fxDouble <= fxHalf) {
				h *= 2;
				// System.out.println("Doubled step size to " + h);
				return xhDouble;
			} else if (fx <= fxHalf) {
				return x;
			} else {
				h /= 2;
				// System.out.println("Halfed step size to " + h);
				return xhHalf;
			}
		} else {
			return x;
		}
	}

	/**
	 * Optimize the function parameters until the termination condition is
	 * reached. Returns the optimized parameter vector. Attention, the new
	 * values for x are saved in the same array!
	 */
	public double[] optimize(Function f, double[] x) {
		int numberOfIterations = 0;
		do {
			x = step(f, x);
			numberOfIterations++;
		} while (!terminationCondition.terminated(f.eval(x), x));

		System.out.println("#iterations: " + numberOfIterations);

		return x;
	}

	public void setStepSizeUpdateMethod(StepSizeUpdateMethod stepSizeUpdateMethod) {
		this.stepSizeUpdateMethod = stepSizeUpdateMethod;
	}

}

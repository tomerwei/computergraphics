package cgresearch.core.math.functions;

/**
 * Terminates if the function value changes less than a predefined epsilon
 * 
 * @author Philipp Jenke
 *
 */
public class TerminatingConditionError implements TerminatingCondition {

	/**
	 * Terminates if the function value changes less than this epsilon value.
	 */
	private final double epsilon;

	/**
	 * Remember last function value;
	 */
	private double lastFx = Double.POSITIVE_INFINITY;

	/**
	 * Constructor.
	 */
	public TerminatingConditionError(double epsilon) {
		this.epsilon = epsilon;
	}

	@Override
	public boolean terminated(double fx, double[] x) {
		boolean terminated = Math.abs(fx - lastFx) < epsilon;
		lastFx = fx;
		return terminated;
	}

}

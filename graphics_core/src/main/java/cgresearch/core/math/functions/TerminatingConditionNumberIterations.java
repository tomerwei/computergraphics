package cgresearch.core.math.functions;

/**
 * Terminates when a given number of iterations is reached.
 * 
 * @author Philipp Jenke
 *
 */
public class TerminatingConditionNumberIterations implements
		TerminatingCondition {

	private final int maxNumberOfIterations;

	private int numberOfIterations = 0;

	/**
	 * Constructor.
	 */
	public TerminatingConditionNumberIterations(int maxNumberOfIterations) {
		this.maxNumberOfIterations = maxNumberOfIterations;
	}

	@Override
	public boolean terminated(double fx, double[] x) {
		numberOfIterations++;
		return numberOfIterations >= maxNumberOfIterations;
	}

}

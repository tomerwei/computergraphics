package cgresearch.core.math.functions;

/**
 * Interface to represent a terminating condiction of an optimization approach
 * 
 * @author Philipp Jenke
 *
 */
public interface TerminatingCondition {

	/**
	 * This method returns true when the approach is done.
	 */
	public boolean terminated(double fx, double[] x);

}

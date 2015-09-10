/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

/**
 * Interface for every child brick of the root one.
 * 
 * @author Chris Michael Marquardt
 */
public interface IChildBrick extends IBrick {
	/**
	 * Get the root brick.
	 * @return
	 */
	public IBrick getRootBrick();
}

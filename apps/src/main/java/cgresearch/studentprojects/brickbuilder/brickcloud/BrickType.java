/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

/**
 * Enumeration for every brick type.
 * 
 * @author Chris Michael Marquardt
 */
public enum BrickType {
	/**
	 * Root brick
	 */
	ROOT(),
	/**
	 * Composed brick (only multiples of the root in each axis)
	 */
	COMPOSED(),
	/**
	 * Special brick (for example with a layout like a T)
	 */
	SPECIAL(),
	/**
	 * Special brick for a rotation around the x axiz
	 */
	SPECIAL_ROTATION_X,
	/**
	 * Special brick for a rotation around the y axiz
	 */
	SPECIAL_ROTATION_Y,
	/**
	 * Special brick for a rotation around the z axiz
	 */
	SPECIAL_ROTATION_Z;
}

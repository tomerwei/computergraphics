/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.math;

/**
 * Interface of a super simple uneditable integer 3d-vector.
 * 
 * @author Chris Michael Marquardt
 */
public interface IVectorInt3 {

	/**
	 * Get the x dimension value.
	 * @return
	 */
	public int getX();
	
	/**
	 * Get the y dimension value.
	 * @return
	 */
	public int getY();
	
	/**
	 * Get the z dimension value.
	 * @return
	 */
	public int getZ();
	
	/**
	 * Get all dimensions as int array.
	 * @return
	 */
	public int[] get();
	
	/**
	 * Get the product of the vector.
	 * @return
	 */
	public int getProduct();
	
	/**
	 * Add another vector and returns the result.
	 * @param other
	 * return
	 */
	public IVectorInt3 add(IVectorInt3 other);
	
	/**
	 * Subtracts another vector and returns the result.
	 * @param other
	 * return
	 */
	public IVectorInt3 sub(IVectorInt3 other);
}

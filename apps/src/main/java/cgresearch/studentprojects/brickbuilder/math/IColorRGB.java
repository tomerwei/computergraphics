/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.math;

import java.util.List;

import cgresearch.core.math.IVector3;

/**
 * Interface of a super simple uneditable color.
 * 
 * @author Chris Michael Marquardt
 */
public interface IColorRGB {
	/**
	 * Get the red value.
	 * @return
	 */
	public int getRed();
	
	/**
	 * Get the green value.
	 * @return
	 */
	public int getGreen();
	
	/**
	 * Get the blue value.
	 * @return
	 */
	public int getBlue();
	
	/**
	 * Get the color as integer.
	 * @return
	 */
	public int getAsInteger();
	
	/**
	 * Get the color as vector.
	 * @return
	 */
	public IVector3 getAsVector();
	
	/**
	 * Get the color as byte array.
	 * @return
	 */
	public byte[] getAsByteArray();
	
	/**
	 * Get the nearest color out of the list.
	 * @param list
	 * @return
	 */
	public IColorRGB getNearestColor(List<IColorRGB> list);
}

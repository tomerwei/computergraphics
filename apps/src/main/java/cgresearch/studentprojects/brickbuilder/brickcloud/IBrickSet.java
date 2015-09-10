/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.List;

import cgresearch.studentprojects.brickbuilder.math.IColorRGB;

/**
 * Brick set containing all available bricks and their colors.
 * 
 * @author Chris Michael Marquardt
 */
public interface IBrickSet {
	/**
	 * Get the root brick.
	 * @return
	 */
	public IBrick getRootBrick();
	
	/**
	 * Get a list with all child bricks.
	 * @return
	 */
	public List<IChildBrick> getChildBricks();
	
	/**
	 * Get a list with all brick colors.
	 * @return
	 */
	public List<IColorRGB> getBrickColors();
	
	/**
	 * Add a child brick.
	 * @param childBrick
	 */
	public void addChildBrick(IChildBrick childBrick);
	public void addChildBrick(IChildBrick childBrick, IBrick[] precessors);
	
	/**
	 * Add a brick color.
	 * @param color
	 */
	public void addBrickColor(IColorRGB color);
	
	/**
	 * Get the next smallest brick.
	 * @param brick
	 * @return
	 */
	public List<IChildBrick> getNextBricks(IBrick brick);
}

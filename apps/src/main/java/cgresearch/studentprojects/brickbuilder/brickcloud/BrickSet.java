/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.studentprojects.brickbuilder.math.IColorRGB;

/**
 * Implementation of the IBrickSet interface.
 * 
 * @author Chris Michael Marquardt
 */
public class BrickSet implements IBrickSet {
	/**
	 * The root brick.
	 */
	private IBrick rootBrick;
	/**
	 * Root successors.
	 */
	private List<IChildBrick> rootSuccs;
	/**
	 * All child bricks to remain order.
	 */
	private List<IChildBrick> childBrickOrdered;
	/**
	 * All child bricks with successors.
	 */
	private Map<IChildBrick, List<IChildBrick>> childBricks;
	/**
	 * All brick colors.
	 */
	private List<IColorRGB> brickColors;
	
	/**
	 * Constructor
	 * @param rootBrick
	 */
	public BrickSet(RootBrick rootBrick) {
		this.rootBrick = rootBrick;
		this.childBrickOrdered = new ArrayList<IChildBrick>();
		this.childBricks = new HashMap<IChildBrick, List<IChildBrick>>();
		this.brickColors = new ArrayList<IColorRGB>();
		this.rootSuccs = new ArrayList<IChildBrick>();
	}
	
	@Override
	public IBrick getRootBrick() {
		return rootBrick;
	}
	
	@Override
	public List<IChildBrick> getChildBricks() {
		return new ArrayList<IChildBrick>(childBrickOrdered);
	}
	
	@Override
	public List<IColorRGB> getBrickColors() {
		return new ArrayList<IColorRGB>(brickColors);
	}
	
	@Override
	public void addChildBrick(IChildBrick childBrick) {
		// generate precessors
		List<IBrick> precessors = new ArrayList<IBrick>();
		precessors.add(rootBrick);
		for (IChildBrick child : childBrickOrdered) {
			if (childBrick.getResolution().getX() >= child.getResolution().getX() && 
					childBrick.getResolution().getY() >= child.getResolution().getY() &&
					childBrick.getResolution().getZ() >= child.getResolution().getZ())
				precessors.add(child);
		}
		addChildBrick(childBrick, precessors.toArray(new IBrick[] {}));
	}
	
	@Override
	public void addChildBrick(IChildBrick childBrick, IBrick[] precessors) {
		childBrickOrdered.add(childBrick);
		childBricks.put(childBrick, new ArrayList<IChildBrick>());
		for (IBrick brick : precessors) {
			if (brick == rootBrick) this.rootSuccs.add(childBrick);
			else childBricks.get(brick).add(childBrick);
		}
	}
	
	@Override
	public void addBrickColor(IColorRGB color) {
		brickColors.add(color);
	}

	@Override
	public List<IChildBrick> getNextBricks(IBrick brick) {
		return (brick == rootBrick ? rootSuccs : childBricks.get(brick));
	}
}

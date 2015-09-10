package cgresearch.studentprojects.brickbuilder.brickcloud;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;


public interface IBrickBuilderMergeFunction {
	/**
	 * Value of a brick. Higher = better.
	 * @param brickCloud	brick cloud
	 * @param brick			brick type to test	
	 * @param pos			position of the brick
	 * @param rot			rotation of the brick
	 * @return				connections to other bricks above and under it
	 */
	public int valueFunction(IBrickCloud brickCloud, IBrick brick,
			IVectorInt3 pos, BrickRotation rot);
}

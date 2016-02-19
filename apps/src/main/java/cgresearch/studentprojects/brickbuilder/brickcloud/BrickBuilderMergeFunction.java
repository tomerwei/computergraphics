package cgresearch.studentprojects.brickbuilder.brickcloud;

import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

public class BrickBuilderMergeFunction implements IBrickBuilderMergeFunction {

	@Override
	public int valueFunction(IBrickCloud brickCloud, IBrick brick,
			VectorInt3 pos, BrickRotation rot) {
		// costs = connections / more = better
		return brickCloud.getBrickConnections(brick, pos, rot);
	}

}

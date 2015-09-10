package cgresearch.studentprojects.brickbuilder.brickcloud;

import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;

public class BrickBuilderMergeFunctionRandom implements IBrickBuilderMergeFunction {

	@Override
	public int valueFunction(IBrickCloud brickCloud, IBrick brick,
			IVectorInt3 pos, BrickRotation rot) {
		// 1 for everybody => random
		return 1;
	}

}

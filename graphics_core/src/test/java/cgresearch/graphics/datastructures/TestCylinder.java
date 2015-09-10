package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Cylinder;

public class TestCylinder {

	@Test
	public void testCylinder() {
		Cylinder cylinder = new Cylinder(VectorMatrixFactory.newIVector3(1, 0,
				1), VectorMatrixFactory.newIVector3(0, 1, 0), 0.5);
		IVector3 p = VectorMatrixFactory.newIVector3(2, 1, 1);
		double distance = cylinder.getDistance(p);
		assertEquals(0.5, distance, 1E-4);
	}
}

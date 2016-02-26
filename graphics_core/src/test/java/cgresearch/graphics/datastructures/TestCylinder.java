package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Cylinder;

public class TestCylinder {

	@Test
	public void testCylinder() {
		Cylinder cylinder = new Cylinder(VectorFactory.createVector3(1, 0,
				1), VectorFactory.createVector3(0, 1, 0), 0.5);
		Vector p = VectorFactory.createVector3(2, 1, 1);
		double distance = cylinder.getDistance(p);
		assertEquals(0.5, distance, 1E-4);
	}
}

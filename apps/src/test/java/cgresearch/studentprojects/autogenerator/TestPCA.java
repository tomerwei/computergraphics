package cgresearch.studentprojects.autogenerator;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Plane;

/**
 * Testing class for PCA
 * 
 * @author Philipp Jenke
 *
 */
public class TestPCA {

	@Test
	public void testPCA3D() {
		// Idea: Generate a plane. sample points from the noisy plane. In the
		// result
		// there should be two large and a small eigenvalue. The eigenvector
		// corresponding to the smallest eigenvalue should point into the normal
		// direction.

		double px = Math.random();
		double py = Math.random();
		double pz = Math.random();
		Vector p = VectorFactory.createVector3(px, py, pz);
		Vector normal = VectorFactory.createVector3(Math.random(), Math.random(), Math.random()).getNormalized();
    Plane plane = new Plane(p, normal);

		PCA pca = new PCA();
		// Create some random points near to the plane
		for (int i = 0; i < 30; i++) {
			Vector planeSample = plane.getTangentU().multiply(Math.random())
					.add(plane.getTangentV().multiply(Math.random())).add(normal.multiply(Math.random() * 0.02));
			Vector x = new Vector(3);
			x.set(0, planeSample.get(0));
			x.set(1, planeSample.get(1));
			x.set(2, planeSample.get(2));
			pca.add(x);
			System.out.println(x);
		}
		pca.applyPCA();
		Vector n = new Vector(3);
		n.set(0, normal.get(0));
		n.set(1, normal.get(1));
		n.set(2, normal.get(2));
		// Test successful the the normal is (nearly) equals to the eigenvector
		// corresponding to the smallest eigenvalue.
		assertTrue(Math.abs(n.multiply(pca.getEigenVector(0))) > 0.95);

		System.out.println("Debuggin Output:");
		System.out.println("Normal: " + normal);
		System.out.println("EigenValue 0: " + pca.getEigenValue(0));
		System.out.println("EigenValue 1: " + pca.getEigenValue(1));
		System.out.println("EigenValue 2: " + pca.getEigenValue(2));
		System.out.print("EigenVector 0: " + pca.getEigenVector(0));
		System.out.print("EigenVector 1: " + pca.getEigenVector(1));
		System.out.print("EigenVector 2: " + pca.getEigenVector(2));
	}

}

/**
 * 
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.HermiteCurve;

/**
 * Testing class for @see HermiteCurve
 * 
 * @author Philipp Jenke
 * 
 */
public class TestHermiteCurve {

    /**
     * Test the functionality for a 2D curve.
     */
    @Test
    public void test2D() {
        Vector p0 = VectorFactory.createVector3(-0.5, -0.5, 0);
        Vector p1 = VectorFactory.createVector3(0.5, 0.5, 0);
        Vector m0 = VectorFactory.createVector3(0, 1, 0);
        Vector m1 = VectorFactory.createVector3(-1, 0, 0);
        HermiteCurve hermiteCurve = new HermiteCurve(p0, m0, m1, p1);
        assertEquals(hermiteCurve.eval(0), p0);
       // assertEquals(hermiteCurve.eval(0.5),
        //        VectorMatrixFactory.newVector(0, 0, 0));
        assertEquals(hermiteCurve.eval(1), p1);
        assertEquals(hermiteCurve.derivative(0), m0);
        //assertEquals(hermiteCurve.derivative(0.5),
        //        VectorMatrixFactory.newVector(0, 0, 0));
        assertEquals(hermiteCurve.derivative(1), m1);
    }
}

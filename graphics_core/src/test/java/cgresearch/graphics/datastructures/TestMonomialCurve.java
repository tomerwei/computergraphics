/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.MonomialCurve;

/**
 * Testing class for Polynomials
 * 
 * @author Philipp Jenke
 * 
 */
public class TestMonomialCurve {

    /**
     * Test monomials of degree 1.
     */
    @Test
    public void testDegree1() {
        MonomialCurve poly = new MonomialCurve(1);
        poly.setControlPoint(0, VectorMatrixFactory.newVector(-0.5, -0.5, 0));
        poly.setControlPoint(1, VectorMatrixFactory.newVector(1, 1, 0));
        assertEquals(poly.eval(0),
                VectorMatrixFactory.newVector(-0.5, -0.5, 0));
        assertEquals(poly.eval(0.5), VectorMatrixFactory.newVector(0, 0, 0));
        assertEquals(poly.eval(1), VectorMatrixFactory.newVector(0.5, 0.5, 0));
        assertEquals(poly.derivative(0),
                VectorMatrixFactory.newVector(1, 1, 0));
        assertEquals(poly.derivative(0.5),
                VectorMatrixFactory.newVector(1, 1, 0));
        assertEquals(poly.derivative(1),
                VectorMatrixFactory.newVector(1, 1, 0));
    }

    /**
     * Test monomials of degree 2.
     */
    @Test
    public void testDegree2() {
        MonomialCurve poly = new MonomialCurve(2);
        poly.setControlPoint(0, VectorMatrixFactory.newVector(-0.5, -0.5, 0));
        poly.setControlPoint(1, VectorMatrixFactory.newVector(1, 1, 0));
        poly.setControlPoint(2, VectorMatrixFactory.newVector(-1, 1, 0));
        assertEquals(poly.eval(0),
                VectorMatrixFactory.newVector(-0.5, -0.5, 0));
        assertEquals(poly.eval(0.5),
                VectorMatrixFactory.newVector(-0.25, 0.25, 0));
        assertEquals(poly.eval(1),
                VectorMatrixFactory.newVector(-0.5, 1.5, 0));
        assertEquals(poly.derivative(0),
                VectorMatrixFactory.newVector(1, 1, 0));
        assertEquals(poly.derivative(0.5),
                VectorMatrixFactory.newVector(0, 2, 0));
        assertEquals(poly.derivative(1),
                VectorMatrixFactory.newVector(-1, 3, 0));
    }

    @Test
    public void testInterpolate2Points() {
        Vector p0 = VectorMatrixFactory.newVector(-0.5, -0.5, -0.5);
        Vector p1 = VectorMatrixFactory.newVector(0.5, -0.25, 0.75);
        MonomialCurve curve = MonomialCurve.interpolate(p0, p1);
        assertEquals(p0, curve.eval(0));
        assertEquals(p1, curve.eval(1));
    }

    @Test
    public void testInterpolate3Points() {
        Vector p0 = VectorMatrixFactory.newVector(-0.5, -0.5, -0.5);
        Vector p1 = VectorMatrixFactory.newVector(0.0, -0.25, 0.75);
        Vector p2 = VectorMatrixFactory.newVector(0.5, 0.5, 0.5);
        MonomialCurve curve = MonomialCurve.interpolate(p0, p1, p2);
        assertEquals(p0, curve.eval(0.0));
        assertEquals(p1, curve.eval(0.5));
        assertEquals(p2, curve.eval(1.0));
    }

}

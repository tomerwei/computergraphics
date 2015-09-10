/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Testing class for the class MyVector.
 * 
 * @author Philipp Jenke
 * 
 */
public class MyVectorTest {

    @Test
    /**
     * Test multiply with scalar.
     */
    public void testMulitplyScalar() {
        IVector3 vector = VectorMatrixFactory.newIVector3(1, 2, 3);
        IVector3 result = vector.multiply(2);
        IVector3 expected = VectorMatrixFactory.newIVector3(2, 4, 6);
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test scalar product.
     */
    public void testMultiplyVector() {
        IVector3 vector = VectorMatrixFactory.newIVector3(1, 2, 3);
        double result = vector.multiply(VectorMatrixFactory.newIVector3(-3, -1,
                2));
        double expected = 1;
        assertEquals(result, expected, MathHelpers.EPSILON);
    }

    @Test
    /**
     * Test scalar product.
     */
    public void testGetSqrNorm() {
        IVector3 vector = VectorMatrixFactory.newIVector3(1, 2, 2);
        double result = vector.getSqrNorm();
        double expected = 9;
        assertEquals(result, expected, MathHelpers.EPSILON);
    }

    @Test
    /**
     * Test scalar product.
     */
    public void testCopyFrom() {
        IVector3 vector = VectorMatrixFactory.newIVector3(1, 2, 2);
        double result = vector.getNorm();
        double expected = 3;
        assertEquals(result, expected, MathHelpers.EPSILON);
    }

    @Test
    /**
     * Test cross product.
     */
    public void testCross() {
        IVector3 result = VectorMatrixFactory.newIVector3(1, 0, 0).cross(
                VectorMatrixFactory.newIVector3(0, 1, 0));
        IVector3 expected = VectorMatrixFactory.newIVector3(0, 0, 1);
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test subtraction.
     */
    public void testSubtraction() {
        IVector3 result = VectorMatrixFactory.newIVector3(1, 2, 3).subtract(
                VectorMatrixFactory.newIVector3(-3, 2, 1));
        IVector3 expected = VectorMatrixFactory.newIVector3(4, 0, 2);
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test addition.
     */
    public void testAdd() {
        IVector3 result = VectorMatrixFactory.newIVector3(1, 2, 3).add(
                VectorMatrixFactory.newIVector3(-3, 2, 1));
        IVector3 expected = VectorMatrixFactory.newIVector3(-2, 4, 4);
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test getNormalized.
     */
    public void testGetNormalized() {
        IVector3 result = VectorMatrixFactory.newIVector3(2, 2, 2)
                .getNormalized();
        double expected = 1;
        assertEquals(result.getNorm(), expected, MathHelpers.EPSILON);

        result = VectorMatrixFactory.newIVector3(0, 3, 0).getNormalized();
        assertEquals(result, VectorMatrixFactory.newIVector3(0, 1, 0));
    }

}

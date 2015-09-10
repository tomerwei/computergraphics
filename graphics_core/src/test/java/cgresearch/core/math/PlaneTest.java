/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.Plane;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Test the Plane class.
 * 
 * @author Philipp Jenke
 * 
 */
public class PlaneTest {
    @Test
    /**
     * Test isInPositiveHalfSpace.
     */
    public void testIsOnPositiveHalfspace() {
        Plane plane = new Plane(VectorMatrixFactory.newIVector3(0, 0, 0),
                VectorMatrixFactory.newIVector3(0, 1, 0));

        IVector3 p = VectorMatrixFactory.newIVector3(0, 2, 1);
        boolean result = plane.isInPositiveHalfSpace(p);
        boolean expected = true;
        assertEquals(result, expected);

        p = VectorMatrixFactory.newIVector3(3, -3, 3);
        result = plane.isInPositiveHalfSpace(p);
        expected = false;
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test project.
     */
    public void testProject() {
        Plane plane = new Plane(VectorMatrixFactory.newIVector3(0, 0, 0),
                VectorMatrixFactory.newIVector3(0, 1, 0));

        IVector3 p = VectorMatrixFactory.newIVector3(0, 2, 1);
        IVector3 result = plane.project(p);
        IVector3 expected = VectorMatrixFactory.newIVector3(0, 0, 1);
        assertEquals(result, expected);
    }
}

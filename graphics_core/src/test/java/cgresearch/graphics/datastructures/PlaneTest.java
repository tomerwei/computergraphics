/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.primitives.Plane;

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
        Plane plane = new Plane(VectorFactory.createVector3(0, 0, 0),
                VectorFactory.createVector3(0, 1, 0));

        Vector p = VectorFactory.createVector3(0, 2, 1);
        boolean result = plane.isInPositiveHalfSpace(p);
        boolean expected = true;
        assertEquals(result, expected);

        p = VectorFactory.createVector3(3, -3, 3);
        result = plane.isInPositiveHalfSpace(p);
        expected = false;
        assertEquals(result, expected);
    }

    @Test
    /**
     * Test project.
     */
    public void testProject() {
        Plane plane = new Plane(VectorFactory.createVector3(0, 0, 0),
                VectorFactory.createVector3(0, 1, 0));

        Vector p = VectorFactory.createVector3(0, 2, 1);
        Vector result = plane.project(p);
        Vector expected = VectorFactory.createVector3(0, 0, 1);
        assertEquals(result, expected);
    }
}

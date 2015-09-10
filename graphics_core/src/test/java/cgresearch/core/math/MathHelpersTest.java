/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.MathHelpers;

/**
 * Testing class for class MathHelpers.
 * 
 * @author Philipp Jenke
 * 
 */
public class MathHelpersTest {

	@Test
	/**
	 * Test radiens2Degrees
	 */
	public void testRadiens2Degrees() {
		double radiens = 23 * Math.PI / 180.0;
		double result = MathHelpers.radiens2degree(radiens);
		double expected = 23;
		assertEquals(result, expected, MathHelpers.EPSILON);
	}

	@Test
	/**
	 * Test degrees2Radiens
	 */
	public void testDegrees2Radiens() {
		double degrees = 23;
		double expected = 23 * Math.PI / 180.0;
		double result = MathHelpers.degree2radiens(degrees);
		assertEquals(result, expected, MathHelpers.EPSILON);
	}
}

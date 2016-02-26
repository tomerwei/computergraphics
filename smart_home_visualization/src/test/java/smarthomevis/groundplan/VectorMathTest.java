package smarthomevis.groundplan;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;

public class VectorMathTest
{
	@Test
	public void vectorSubstractionTest1()
	{
	Vector vector1 = new Vector(-0.5, 3.4, 12);
	Vector vector2 = new Vector(1.0, 5.6, 6.2);
	
	Vector resultVector = GPUtility.substractOtherVector(vector1, vector2);
	
	double x = resultVector.get(0);
	double y = resultVector.get(1);
	double z = resultVector.get(2);
	
	assertEquals("vectorSubstractionTest1: test of x failed", -1.5, x, 0.0);
	assertEquals("vectorSubstractionTest1: test of y failed", -2.2, y, 0.0);
	assertEquals("vectorSubstractionTest1: test of z failed", 5.8, z, 0.0);
	}
	
	@Test
	public void kreuzProduktTest1()
	{
	Vector a = new Vector(1.5, 2.0, -3.0);
	Vector b = new Vector(2.0, -1.0, 5.0);
	
	Vector result = GPUtility.kreuzproduktVon(a, b);
	
	double x = result.get(0);
	double y = result.get(1);
	double z = result.get(2);
	
	assertEquals("kreuzProduktTest1: test of x failed", 7.0, x, 0.0);
	assertEquals("kreuzProduktTest1: test of y failed", -13.5, y, 0.0);
	assertEquals("kreuzProduktTest1: test of z failed", -5.5, z, 0.0);
	}
	
	@Test
	public void kreuzProduktTest2()
	{
	Vector a = new Vector(1.0, 2.0, 3.0);
	Vector b = new Vector(3.0, 2.0, 1.0);
	
	Vector result = GPUtility.kreuzproduktVon(a, b);
	
	double x = result.get(0);
	double y = result.get(1);
	double z = result.get(2);
	
	assertEquals("kreuzProduktTest2: test of x failed", -4.0, x, 0.0);
	assertEquals("kreuzProduktTest2: test of y failed", 8.0, y, 0.0);
	assertEquals("kreuzProduktTest2: test of z failed", -4.0, z, 0.0);
	}
	
	@Test
	public void punktProduktTest1()
	{
	Vector a = new Vector(-4.0, 2.2, -0.2);
	Vector b = new Vector(0.5, 0.2, -9.0);
	
	double result = GPUtility.punktproduktVon(a, b);
	
	assertEquals("punktProduktTest1 failed", 0.24, result, 0.0);
	}
	
	@Test
	public void vectorLengthTest1()
	{
	Vector a = new Vector(3.0, 4.0, 7.0);
	
	double result = GPUtility.calcVectorLength(a);
	
	assertEquals("VectorLengthTest1 failed", 8.60, result, 0.01);
	}
	
	@Test
	public void vectorLengthTest2()
	{
	Vector a = new Vector(-3.0, -4.0, 2.0);
	
	double result = GPUtility.calcVectorLength(a);
	
	assertEquals("VectorLengthTest2 failed", 5.38, result, 0.01);
	}
	
	@Test
	public void normalizeVectorTest1()
	{
	Vector a = new Vector(3.0, 4.0, 7.0);
	
	Vector result = GPUtility.normalizeVector(a);
	// Vector result = a.getNormalized();
	
	double x = result.get(0);
	double y = result.get(1);
	double z = result.get(2);
	
	assertEquals("normalizeVectorTest1: x failed", 0.348, x, 0.001);
	assertEquals("normalizeVectorTest1: y failed", 0.465, y, 0.001);
	assertEquals("normalizeVectorTest1: z failed", 0.813, z, 0.001);
	}
	
	@Test
	public void angleBetweenVectorsTest1()
	{
		Vector a = new Vector(1.0, 3.0, -2.0);
		Vector b = new Vector(-1.0, 4.0, 3.0);
		
		double angle = GPUtility.angleBetweenVectors(a, b);
		
		assertEquals("angleBetweenVectorsTest1 failed", 74.8, angle, 0.1);
	}
	
	@Test
	public void angleBetweenVectorsTest2()
	{
		Vector a = new Vector(1.0, 4.0, -2.0);
		Vector b = new Vector(-3.0, 3.0, 1.0);
		
		double angle = GPUtility.angleBetweenVectors(a, b);
		
		assertEquals("angleBetweenVectorsTest2 failed", 69.5, angle, 0.1);
	}
	
}

package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVector {

  @Test
  public void testConstructor() {
    Vector vector = new Vector(3);
    vector.set(0, 1);
    vector.set(1, -2);
    vector.set(2, 3);
    assertEquals(1, vector.get(0), 1e-5);
    assertEquals(-2, vector.get(1), 1e-5);
    assertEquals(3, vector.get(2), 1e-5);
  }

  @Test
  public void testAdd() {
    Vector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    Vector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    Vector r = a.add(b);
    assertEquals(-1, r.get(0), 1e-5);
    assertEquals(2.5, r.get(1), 1e-5);
    assertEquals(4, r.get(2), 1e-5);
  }

  @Test
  public void testSubtract() {
    Vector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    Vector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    Vector r = a.subtract(b);
    assertEquals(3, r.get(0), 1e-5);
    assertEquals(1.5, r.get(1), 1e-5);
    assertEquals(2, r.get(2), 1e-5);
  }

  @Test
  public void testInnerProduct() {
    Vector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    Vector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    double s = a.multiply(b);
    assertEquals(2, s, 1e-5);
  }

  @Test
  public void testMultiplyScalar() {
    Vector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    Vector r = a.multiply(-0.5);
    assertEquals(-0.5, r.get(0), 1e-5);
    assertEquals(-1, r.get(1), 1e-5);
    assertEquals(-1.5, r.get(2), 1e-5);
  }

  @Test
  public void testNorm() {
    Vector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getNorm();
    assertEquals(5, norm, 1e-5);
  }

  @Test
  public void testSqrNorm() {
    Vector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getSqrNorm();
    assertEquals(25, norm, 1e-5);
  }

  @Test
  /**
   * Test multiply with scalar.
   */
  public void testMulitplyScalar() {
    Vector vector = VectorMatrixFactory.newVector(1, 2, 3);
    Vector result = vector.multiply(2);
    Vector expected = VectorMatrixFactory.newVector(2, 4, 6);
    assertEquals(result, expected);
  }

  @Test
  /**
   * Test scalar product.
   */
  public void testMultiplyVector() {
    Vector vector = VectorMatrixFactory.newVector(1, 2, 3);
    double result = vector.multiply(VectorMatrixFactory.newVector(-3, -1, 2));
    double expected = 1;
    assertEquals(result, expected, MathHelpers.EPSILON);
  }

  @Test
  /**
   * Test scalar product.
   */
  public void testGetSqrNorm() {
    Vector vector = VectorMatrixFactory.newVector(1, 2, 2);
    double result = vector.getSqrNorm();
    double expected = 9;
    assertEquals(result, expected, MathHelpers.EPSILON);
  }

  @Test
  /**
   * Test scalar product.
   */
  public void testCopyFrom() {
    Vector vector = VectorMatrixFactory.newVector(1, 2, 2);
    double result = vector.getNorm();
    double expected = 3;
    assertEquals(result, expected, MathHelpers.EPSILON);
  }

  @Test
  /**
   * Test cross product.
   */
  public void testCross() {
    Vector result = VectorMatrixFactory.newVector(1, 0, 0).cross(VectorMatrixFactory.newVector(0, 1, 0));
    Vector expected = VectorMatrixFactory.newVector(0, 0, 1);
    assertEquals(result, expected);
  }

  @Test
  /**
   * Test subtraction.
   */
  public void testSubtraction() {
    Vector result = VectorMatrixFactory.newVector(1, 2, 3).subtract(VectorMatrixFactory.newVector(-3, 2, 1));
    Vector expected = VectorMatrixFactory.newVector(4, 0, 2);
    assertEquals(result, expected);
  }

  @Test
  /**
   * Test getNormalized.
   */
  public void testGetNormalized() {
    Vector result = VectorMatrixFactory.newVector(2, 2, 2).getNormalized();
    double expected = 1;
    assertEquals(result.getNorm(), expected, MathHelpers.EPSILON);

    result = VectorMatrixFactory.newVector(0, 3, 0).getNormalized();
    assertEquals(result, VectorMatrixFactory.newVector(0, 1, 0));
  }
}

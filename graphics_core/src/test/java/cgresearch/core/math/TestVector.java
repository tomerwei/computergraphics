package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVector {

  @Test
  public void testConstructorDimension() {
    Vector vector = new Vector(3);
    vector.set(0, 1);
    vector.set(1, -2);
    vector.set(2, 3);
    assertEquals(1, vector.get(0), MathHelpers.EPSILON);
    assertEquals(-2, vector.get(1), MathHelpers.EPSILON);
    assertEquals(3, vector.get(2), MathHelpers.EPSILON);
  }

  @Test
  public void testCopyConstructor() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    Vector copy = new Vector(vector);
    assertEquals(copy.getDimension(), vector.getDimension());
    for (int i = 0; i < dimension; i++) {
      assertEquals(vector.get(i), copy.get(i), MathHelpers.EPSILON);
    }
  }

  @Test
  public void testConstructor3() {
    Vector vector = new Vector(1, -2, 3);
    assertEquals(1, vector.get(0), MathHelpers.EPSILON);
    assertEquals(-2, vector.get(1), MathHelpers.EPSILON);
    assertEquals(3, vector.get(2), MathHelpers.EPSILON);
    assertEquals(3, vector.getDimension());
  }

  @Test
  public void testConstructor4() {
    Vector vector = new Vector(1, -2, 3, -3);
    assertEquals(1, vector.get(0), MathHelpers.EPSILON);
    assertEquals(-2, vector.get(1), MathHelpers.EPSILON);
    assertEquals(3, vector.get(2), MathHelpers.EPSILON);
    assertEquals(-3, vector.get(3), MathHelpers.EPSILON);
    assertEquals(4, vector.getDimension());
  }

  @Test
  public void testCopy() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    Vector copy = new Vector(dimension);
    copy.copy(vector);
    assertEquals(copy.getDimension(), vector.getDimension());
    for (int i = 0; i < dimension; i++) {
      assertEquals(vector.get(i), copy.get(i), MathHelpers.EPSILON);
    }
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
    assertEquals(3, r.get(0), MathHelpers.EPSILON);
    assertEquals(1.5, r.get(1), MathHelpers.EPSILON);
    assertEquals(2, r.get(2), MathHelpers.EPSILON);
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
    assertEquals(-1, r.get(0), MathHelpers.EPSILON);
    assertEquals(2.5, r.get(1), MathHelpers.EPSILON);
    assertEquals(4, r.get(2), MathHelpers.EPSILON);
  }

  @Test
  public void testNorm() {
    Vector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getNorm();
    assertEquals(5, norm, MathHelpers.EPSILON);
  }

  @Test
  public void testSqrNorm() {
    Vector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getSqrNorm();
    assertEquals(25, norm, MathHelpers.EPSILON);
  }

  @Test
  public void testMultiplyVector() {
    Vector vector = VectorMatrixFactory.newVector(1, 2, 3);
    double result = vector.multiply(VectorMatrixFactory.newVector(-3, -1, 2));
    double expected = 1;
    assertEquals(result, expected, MathHelpers.EPSILON);
  }

  @Test
  public void testMultiplyScalar() {
    Vector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    Vector r = a.multiply(-0.5);
    assertEquals(-0.5, r.get(0), MathHelpers.EPSILON);
    assertEquals(-1, r.get(1), MathHelpers.EPSILON);
    assertEquals(-1.5, r.get(2), MathHelpers.EPSILON);
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
    assertEquals(2, s, MathHelpers.EPSILON);
  }

  @Test
  public void testGetNormalized() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    Vector normalized = vector.getNormalized();
    assertEquals(1, normalized.getSqrNorm(), MathHelpers.EPSILON);

  }

  @Test
  public void testNormalize() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    vector.normalize();
    assertEquals(1, vector.getSqrNorm(), MathHelpers.EPSILON);
  }

  @Test
  public void testCross() {
    Vector result = VectorMatrixFactory.newVector(1, 0, 0).cross(VectorMatrixFactory.newVector(0, 1, 0));
    Vector expected = VectorMatrixFactory.newVector(0, 0, 1);
    assertEquals(result, expected);
  }

  @Test
  public void testAddSelf() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector1 = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector1.set(i, Math.random());
    }
    Vector vector2 = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector2.set(i, Math.random());
    }
    Vector oldVector1 = new Vector(vector1);
    vector1.addSelf(vector2);
    assertEquals(vector1.getDimension(), vector2.getDimension());
    for (int i = 0; i < dimension; i++) {
      assertEquals(oldVector1.get(i) + vector2.get(i), vector1.get(i), MathHelpers.EPSILON);
    }
  }

  @Test
  public void testSubtractSelf() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector1 = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector1.set(i, Math.random());
    }
    Vector vector2 = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector2.set(i, Math.random());
    }
    Vector oldVector1 = new Vector(vector1);
    vector1.subtractSelf(vector2);
    assertEquals(vector1.getDimension(), vector2.getDimension());
    for (int i = 0; i < dimension; i++) {
      assertEquals(oldVector1.get(i) - vector2.get(i), vector1.get(i), MathHelpers.EPSILON);
    }
  }

  @Test
  public void testMultiplySelf() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector1 = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector1.set(i, Math.random());
    }
    double factor = Math.random();
    Vector oldVector1 = new Vector(vector1);
    vector1.multiplySelf(factor);
    assertEquals(oldVector1.getDimension(), vector1.getDimension());
    for (int i = 0; i < dimension; i++) {
      assertEquals(oldVector1.get(i) * factor, vector1.get(i), MathHelpers.EPSILON);
    }
  }

  @Test
  public void testFloatData() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    float[] data = vector.floatData();
    assertEquals(vector.getDimension(), data.length);
    for (int i = 0; i < dimension; i++) {
      assertEquals(vector.get(i), data[i], MathHelpers.EPSILON);
    }
  }

  @Test
  public void testDoubleData() {
    int dimension = (int) (Math.random() * 9) + 1;
    Vector vector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      vector.set(i, Math.random());
    }
    double[] data = vector.data();
    assertEquals(vector.getDimension(), data.length);
    for (int i = 0; i < dimension; i++) {
      assertEquals(vector.get(i), data[i], MathHelpers.EPSILON);
    }
  }

}

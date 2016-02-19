package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMatrix {

  @Test
  public void testInverse() {
    Matrix A = VectorMatrixFactory.newMatrix(-1, 2, 0.5, 3, -4, 2, 1, 1, 7, -4, -3, 2, 4, -2, 1, -5);
    Matrix invA = A.getInverse();
    Matrix result = A.multiply(invA);
    for (int rowIndex = 0; rowIndex < A.getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < A.getNumberOfColumns(); columnIndex++) {
        assertEquals((rowIndex == columnIndex) ? 1 : 0, result.get(rowIndex, columnIndex), 1e-5);
      }
    }

    Matrix matrix = VectorMatrixFactory.newMatrix(2, -4, 1, 3, -5, -3, 2, 1, -4);
    Matrix inverse = matrix.getInverse();
    Matrix result2 = matrix.multiply(inverse);
    for (int row = 0; row < matrix.getNumberOfRows(); row++) {
      for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
        assertEquals((row == column) ? 1 : 0, result2.get(row, column), 1e-5);
      }
    }
  }

  @Test
  public void testConstructor() {
    Matrix A = new Matrix(2, 3);
    A.set(0, 0, 1);
    A.set(0, 1, -2);
    A.set(0, 2, 3);
    A.set(1, 0, -3);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    assertEquals(1, A.get(0, 0), 1e-5);
    assertEquals(-2, A.get(0, 1), 1e-5);
    assertEquals(3, A.get(0, 2), 1e-5);
    assertEquals(-3, A.get(1, 0), 1e-5);
    assertEquals(2, A.get(1, 1), 1e-5);
    assertEquals(-1, A.get(1, 2), 1e-5);

    Matrix B = VectorMatrixFactory.newMatrix(1, 2, 3, 4, 5, 6, 7, 8, 9);
    Vector v = VectorMatrixFactory.newVector(0.5, 1.5, 2.5);
    Vector result = B.multiply(v);
    // System.out.println("B:\n" + B);
    // System.out.println("v:\n" + v);
    // System.out.println("B*v:\n" + result);
    assertEquals(result, VectorMatrixFactory.newVector(11, 24.5, 38));
  }

  @Test
  public void testMatrixVectorMultiplication() {
    // r = A * v ( 2 x 3 * 3 x 1 = 2 x 1)
    //
    // A
    // ( 1 -2 3 )
    // ( -3 2 -1 )
    // v
    // ( 1 )
    // ( -2 )
    // ( 3 )
    Matrix A = new Matrix(2, 3);
    A.set(0, 0, 1);
    A.set(0, 1, -2);
    A.set(0, 2, 3);
    A.set(1, 0, -3);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    Vector v = new Vector(3);
    v.set(0, 1);
    v.set(1, -2);
    v.set(2, 3);
    Vector r = A.multiply(v);
    assertEquals(2, r.getDimension());
    assertEquals(14, r.get(0), 1e-5);
    assertEquals(-10, r.get(1), 1e-5);
  }

  @Test
  public void testMatrixMatrixMultiplication() {
    // R = A * B = ( 2 x 3 * 3 x 2 = 2 x 2 )
    //
    // A
    // ( 1 -2 3 )
    // ( -3 2 -1 )
    // B
    // ( 1, -3 )
    // ( -2, 2 )
    // ( 3, -1 )
    Matrix A = new Matrix(2, 3);
    A.set(0, 0, 1);
    A.set(0, 1, -2);
    A.set(0, 2, 3);
    A.set(1, 0, -3);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    Matrix B = new Matrix(3, 2);
    B.set(0, 0, 1);
    B.set(0, 1, -3);
    B.set(1, 0, -2);
    B.set(1, 1, 2);
    B.set(2, 0, 3);
    B.set(2, 1, -1);
    Matrix R = A.multiply(B);
    assertEquals(2, R.getNumberOfRows());
    assertEquals(2, R.getNumberOfColumns());
    assertEquals(14, R.get(0, 0), 1e-5);
    assertEquals(-10, R.get(0, 1), 1e-5);
    assertEquals(-10, R.get(1, 0), 1e-5);
    assertEquals(14, R.get(1, 1), 1e-5);
  }

  @Test
  /**
   * Test divide.
   */
  public void testMultiply() {
    Matrix matrixA = VectorMatrixFactory.newMatrix(VectorMatrixFactory.newVector(1, 2, 1),
        VectorMatrixFactory.newVector(3, 4, 5), VectorMatrixFactory.newVector(-4, 4, 2));
    Vector vector = VectorMatrixFactory.newVector(1, -1, 2);
    Vector expected = VectorMatrixFactory.newVector(1, 9, -4);
    Vector result = matrixA.multiply(vector);
    assertEquals(result, expected);
  }

  @Test
  /**
   * Test getTranspose()
   */
  public void testTranspose() {
    Matrix A = VectorMatrixFactory.newMatrix(VectorMatrixFactory.newVector(1, 2, 3),
        VectorMatrixFactory.newVector(4, 5, 6), VectorMatrixFactory.newVector(7, 8, 9));
    Matrix B = VectorMatrixFactory.newMatrix(VectorMatrixFactory.newVector(1, 4, 7),
        VectorMatrixFactory.newVector(2, 5, 8), VectorMatrixFactory.newVector(3, 6, 9));
    Matrix transposedA = A.getTransposed();
    Matrix transposedB = B.getTransposed();

    // System.out.println("A:\n" + A);
    // System.out.println("A':\n" + transposedA);
    // System.out.println("B:\n" + B);
    // System.out.println("B:\n" + transposedB);
    assertEquals(A, transposedB);
    assertEquals(transposedA, B);
  }

  @Test
  public void testMultiplyMatrix() {
    Matrix A = VectorMatrixFactory.newMatrix(1, 2, 3, 4, 5, 6, 7, 8, 9);
    Matrix B = VectorMatrixFactory.newMatrix(0.5, 0.5, 0.5, 2, 2, 2, 1, 1, 1);
    Matrix result = A.multiply(B);
    // System.out.println("A:\n" + A);
    // System.out.println("(B:\n" + (B);
    // System.out.println("A*(B:\n" + result);
    Matrix expectedResult = VectorMatrixFactory.newMatrix(7.5, 7.5, 7.5, 18, 18, 18, 28.5, 28.5, 28.5);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testRotation() {
    Vector axis = VectorMatrixFactory.newVector(1, 0, 0);
    double angle = 45.0 * Math.PI / 180.0;
    Matrix rotationMatrix = VectorMatrixFactory.getRotationMatrix(axis, angle);
    Vector x = VectorMatrixFactory.newVector(0, 1, 0);
    System.out.println(rotationMatrix.multiply(x));
  }
}

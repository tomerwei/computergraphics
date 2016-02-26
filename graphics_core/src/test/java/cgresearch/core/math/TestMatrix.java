package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMatrix {

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
    Matrix B = MatrixFactory.createMatrix3(1, 2, 3, 4, 5, 6, 7, 8, 9);
    Vector v = VectorFactory.createVector3(0.5, 1.5, 2.5);
    Vector result = B.multiply(v);
    assertEquals(result, VectorFactory.createVector3(11, 24.5, 38));
  }

  @Test
  public void testConstructor3() {
    Matrix A = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(3, A.getNumberOfRows());
    assertEquals(3, A.getNumberOfColumns());
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        assertEquals(row * 3 + col + 1, A.get(row, col), MathHelpers.EPSILON);
      }
    }
  }

  @Test
  public void testConstructor4() {
    Matrix A = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    assertEquals(4, A.getNumberOfRows());
    assertEquals(4, A.getNumberOfColumns());
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        assertEquals(row * 4 + col + 1, A.get(row, col), MathHelpers.EPSILON);
      }
    }
  }

  @Test
  public void testCopyConstructor() {
    Matrix A = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
    Matrix B = new Matrix(A);
    assertEquals(B.getNumberOfRows(), A.getNumberOfRows());
    assertEquals(B.getNumberOfColumns(), A.getNumberOfColumns());
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        assertEquals(B.get(row, col), A.get(row, col), MathHelpers.EPSILON);
      }
    }
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
  public void testScale() {
    int numberOfRows = (int) (Math.random() * 9) + 1;
    int numberOfCols = (int) (Math.random() * 9) + 1;
    Matrix A = new Matrix(numberOfRows, numberOfCols);
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        A.set(row, col, Math.random());
      }
    }
    double factor = Math.random();
    Matrix C = A.multiply(factor);
    assertEquals(numberOfRows, C.getNumberOfRows());
    assertEquals(numberOfCols, C.getNumberOfColumns());
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        assertEquals(A.get(row, col) * factor, C.get(row, col), MathHelpers.EPSILON);
      }
    }
  }

  @Test
  public void testAdd() {
    int numberOfRows = (int) (Math.random() * 9) + 1;
    int numberOfCols = (int) (Math.random() * 9) + 1;
    Matrix A = new Matrix(numberOfRows, numberOfCols);
    Matrix B = new Matrix(numberOfRows, numberOfCols);
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        A.set(row, col, Math.random());
        B.set(row, col, Math.random());
      }
    }
    Matrix C = A.add(B);
    assertEquals(numberOfRows, C.getNumberOfRows());
    assertEquals(numberOfCols, C.getNumberOfColumns());
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        assertEquals(A.get(row, col) + B.get(row, col), C.get(row, col), MathHelpers.EPSILON);
      }
    }
  }

  @Test
  public void testSubtract() {
    int numberOfRows = (int) (Math.random() * 9) + 1;
    int numberOfCols = (int) (Math.random() * 9) + 1;
    Matrix A = new Matrix(numberOfRows, numberOfCols);
    Matrix B = new Matrix(numberOfRows, numberOfCols);
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        A.set(row, col, Math.random());
        B.set(row, col, Math.random());
      }
    }
    Matrix C = A.subtract(B);
    assertEquals(numberOfRows, C.getNumberOfRows());
    assertEquals(numberOfCols, C.getNumberOfColumns());
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        assertEquals(A.get(row, col) - B.get(row, col), C.get(row, col), MathHelpers.EPSILON);
        ;
      }
    }
  }

  @Test
  public void testGetDeterminant() {
    Matrix A = new Matrix(0, 1, 2, 3, 2, 1, 1, 1, 0);
    assertEquals(3, A.getDeterminant(), MathHelpers.EPSILON);
  }

  @Test
  public void testInverse() {
    Matrix A = MatrixFactory.createMatrix4(-1, 2, 0.5, 3, -4, 2, 1, 1, 7, -4, -3, 2, 4, -2, 1, -5);
    Matrix invA = A.getInverse();
    Matrix result = A.multiply(invA);
    for (int rowIndex = 0; rowIndex < A.getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < A.getNumberOfColumns(); columnIndex++) {
        assertEquals((rowIndex == columnIndex) ? 1 : 0, result.get(rowIndex, columnIndex), 1e-5);
      }
    }
    Matrix matrix = MatrixFactory.createMatrix3(2, -4, 1, 3, -5, -3, 2, 1, -4);
    Matrix inverse = matrix.getInverse();
    Matrix result2 = matrix.multiply(inverse);
    for (int row = 0; row < matrix.getNumberOfRows(); row++) {
      for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
        assertEquals((row == column) ? 1 : 0, result2.get(row, column), 1e-5);
      }
    }
  }

  @Test
  /**
   * Test getTranspose()
   */
  public void testTranspose() {
    Matrix A = MatrixFactory.createMatrix3(VectorFactory.createVector3(1, 2, 3),
        VectorFactory.createVector3(4, 5, 6), VectorFactory.createVector3(7, 8, 9));
    Matrix B = MatrixFactory.createMatrix3(VectorFactory.createVector3(1, 4, 7),
        VectorFactory.createVector3(2, 5, 8), VectorFactory.createVector3(3, 6, 9));
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
  public void testData() {
    int numberOfRows = (int) (Math.random() * 9) + 1;
    int numberOfCols = (int) (Math.random() * 9) + 1;
    Matrix A = new Matrix(numberOfRows, numberOfCols);
    for (int row = 0; row < numberOfRows; row++) {
      for (int col = 0; col < numberOfCols; col++) {
        A.set(row, col, numberOfCols * row + col);
      }
    }
    double[] data = A.data();
    assertEquals(numberOfRows * numberOfCols, data.length);
    for (int i = 0; i < data.length; i++) {
      assertEquals(i, data[i], MathHelpers.EPSILON);
    }
  }

  @Test
  public void testRotation() {
    Vector axis = VectorFactory.createVector3(1, 0, 0);
    double angle = 45.0 * Math.PI / 180.0;
    Matrix rotationMatrix = MatrixFactory.createRotationMatrix(axis, angle);
    Vector x = VectorFactory.createVector3(0, 1, 0);
    System.out.println(rotationMatrix.multiply(x));
  }
}

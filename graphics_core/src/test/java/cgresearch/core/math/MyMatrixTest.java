/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Testing class for the class MyMatrix.
 * 
 * @author Philipp Jenke
 * 
 */
public class MyMatrixTest {

  @Test
  /**
   * Test divide.
   */
  public void testMultiply() {
    IMatrix3 matrixA = VectorMatrixFactory.newIMatrix3(VectorMatrixFactory.newIVector3(1, 2, 1),
        VectorMatrixFactory.newIVector3(3, 4, 5), VectorMatrixFactory.newIVector3(-4, 4, 2));
    IVector3 vector = VectorMatrixFactory.newIVector3(1, -1, 2);
    IVector3 expected = VectorMatrixFactory.newIVector3(1, 9, -4);
    assertEquals(matrixA.multiply(vector), expected);
  }

  @Test
  /**
   * Test getTranspose()
   */
  public void testTranspose() {
    IMatrix3 A = VectorMatrixFactory.newIMatrix3(VectorMatrixFactory.newIVector3(1, 2, 3),
        VectorMatrixFactory.newIVector3(4, 5, 6), VectorMatrixFactory.newIVector3(7, 8, 9));
    IMatrix3 B = VectorMatrixFactory.newIMatrix3(VectorMatrixFactory.newIVector3(1, 4, 7),
        VectorMatrixFactory.newIVector3(2, 5, 8), VectorMatrixFactory.newIVector3(3, 6, 9));
    IMatrix3 transposedA = A.getTransposed();
    IMatrix3 transposedB = B.getTransposed();

    // System.out.println("A:\n" + A);
    // System.out.println("A':\n" + transposedA);
    // System.out.println("B:\n" + B);
    // System.out.println("B:\n" + transposedB);
    assertEquals(A, transposedB);
    assertEquals(transposedA, B);
  }

  @Test
  public void testConstructor() {
    IMatrix3 A = VectorMatrixFactory.newIMatrix3(1, 2, 3, 4, 5, 6, 7, 8, 9);
    IVector3 v = VectorMatrixFactory.newIVector3(0.5, 1.5, 2.5);
    IVector3 result = A.multiply(v);
    // System.out.println("A:\n" + A);
    // System.out.println("v:\n" + v);
    // System.out.println("A*v:\n" + result);
    assertEquals(result, VectorMatrixFactory.newIVector3(11, 24.5, 38));
  }

  @Test
  public void testGetColumn() {
    IMatrix3 A = VectorMatrixFactory.newIMatrix3(1, 2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(VectorMatrixFactory.newIVector3(1, 4, 7), A.getColumn(0));
    assertEquals(VectorMatrixFactory.newIVector3(2, 5, 8), A.getColumn(1));
    assertEquals(VectorMatrixFactory.newIVector3(3, 6, 9), A.getColumn(2));
  }

  @Test
  public void testMultiplyMatrix() {
    IMatrix3 A = VectorMatrixFactory.newIMatrix3(1, 2, 3, 4, 5, 6, 7, 8, 9);
    IMatrix3 B = VectorMatrixFactory.newIMatrix3(0.5, 0.5, 0.5, 2, 2, 2, 1, 1, 1);
    IMatrix3 result = A.multiply(B);
    // System.out.println("A:\n" + A);
    // System.out.println("(B:\n" + (B);
    // System.out.println("A*(B:\n" + result);
    IMatrix3 expectedResult = VectorMatrixFactory.newIMatrix3(7.5, 7.5, 7.5, 18, 18, 18, 28.5, 28.5, 28.5);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testRotation() {
    IVector3 axis = VectorMatrixFactory.newIVector3(1, 0, 0);
    double angle = 45.0 * Math.PI / 180.0;
    IMatrix3 rotationMatrix = VectorMatrixFactory.getRotationMatrix(axis, angle);
    IVector3 x = VectorMatrixFactory.newIVector3(0, 1, 0);
    System.out.println(rotationMatrix.multiply(x));
  }

  @Test
  public void testInverse() {
    IMatrix3 matrix = VectorMatrixFactory.newIMatrix3(2, -4, 1, 3, -5, -3, 2, 1, -4);
    IMatrix3 inverse = matrix.getInverse();
    IMatrix3 result = matrix.multiply(inverse);
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 3; column++) {
        assertEquals((row == column) ? 1 : 0, result.get(row, column), 1e-5);
      }
    }
  }
}

package cgresearch.core.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixTest {
  @Test
  public void testConstructor() {
    IMatrix A = new Matrix(2, 3);
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
  }

  @Test
  public void testMatrixVectorMultiplication() {
    // r = A * v ( 2 x 3 * 3 x 1  = 2 x 1)
    //
    // A
    // ( 1 -2 3 )
    // ( -3 2 -1 )
    // v
    // ( 1 )
    // ( -2 )
    // ( 3 )
    IMatrix A = new Matrix(2, 3);
    A.set(0, 0, 1);
    A.set(0, 1, -2);
    A.set(0, 2, 3);
    A.set(1, 0, -3);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    IVector v = new Vector(3);
    v.set(0, 1);
    v.set(1, -2);
    v.set(2, 3);
    IVector r = A.multiply(v);
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
    IMatrix A = new Matrix(2, 3);
    A.set(0, 0, 1);
    A.set(0, 1, -2);
    A.set(0, 2, 3);
    A.set(1, 0, -3);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    IMatrix B = new Matrix(3, 2);
    B.set(0, 0, 1);
    B.set(0, 1, -3);
    B.set(1, 0, -2);
    B.set(1, 1, 2);
    B.set(2, 0, 3);
    B.set(2, 1, -1);
    IMatrix R = A.multiply(B);
    assertEquals(2, R.getNumberOfRows());
    assertEquals(2, R.getNumberOfColumns());
    assertEquals(14, R.get(0, 0), 1e-5);
    assertEquals(-10, R.get(0, 1), 1e-5);
    assertEquals(-10, R.get(1, 0), 1e-5);
    assertEquals(14, R.get(1, 1), 1e-5);
  }
}

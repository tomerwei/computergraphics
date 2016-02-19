package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestMatrix4 {

  @Test
  public void testInverse() {
    IMatrix4 A = VectorMatrixFactory.newIMatrix4(-1, 2, 0.5, 3, -4, 2, 1, 1, 7, -4, -3, 2, 4, -2, 1, -5);
    IMatrix4 invA = A.getInverse();
    IMatrix4 result = A.multiply(invA);
    for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
      for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
        assertEquals((rowIndex == columnIndex) ? 1 : 0, result.get(rowIndex, columnIndex), 1e-5);
      }
    }
  }
}

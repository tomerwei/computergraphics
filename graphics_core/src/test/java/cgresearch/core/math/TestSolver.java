package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testclass for Solver.
 * 
 * @author Philipp Jenke
 *
 */
public class TestSolver {

  @Test
  public void testLUDecomposition() {
    IMatrix A = new Matrix(3, 3);
    A.set(0, 0, 1);
    A.set(0, 1, 2);
    A.set(0, 2, 3);
    A.set(1, 0, 1);
    A.set(1, 1, 1);
    A.set(1, 2, 1);
    A.set(2, 0, 3);
    A.set(2, 1, 3);
    A.set(2, 2, 1);
    IVector b = new Vector(3);
    b.set(0, 1);
    b.set(1, 2);
    b.set(2, 3);
    IVector x = Solver.solveLUDecomposition(A, b);
    assertEquals(0, A.multiply(x).subtract(b).getNorm(), 1e-5);
  }

  @Test
  public void testConjugateGradient() {
    IMatrix A = new Matrix(3, 3);
    A.set(0, 0, 2);
    A.set(0, 1, -1);
    A.set(0, 2, 0);
    A.set(1, 0, -1);
    A.set(1, 1, 2);
    A.set(1, 2, -1);
    A.set(2, 0, 0);
    A.set(2, 1, -1);
    A.set(2, 2, 2);
    IVector b = new Vector(3);
    b.set(0, 1);
    b.set(1, 2);
    b.set(2, 3);
    IVector x = new Vector(b.getDimension());
    x = Solver.solveConjugateGradient(A, b, x, 10, 1e-5);
    assertEquals(0, A.multiply(x).subtract(b).getNorm(), 1e-5);
  }

}

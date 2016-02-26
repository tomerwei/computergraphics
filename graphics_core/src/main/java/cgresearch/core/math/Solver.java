package cgresearch.core.math;

/**
 * Provides solvers for linear systems.
 * 
 * @author Philipp Jenke
 *
 */
public class Solver {

  /**
   * Solves the linear system by computing the LU decomposition.
   * 
   * @param A
   *          Linear system.
   * @param b
   *          Right-hand-side.
   * @return Solution vector x (if it exists), null (otherwise)
   */
  public static Vector solveLUDecomposition(Matrix A, Vector b) {
    if (A == null || A.getNumberOfRows() != A.getNumberOfColumns() || A.getNumberOfColumns() != b.getDimension()) {
      throw new IllegalArgumentException();
    }

    // Compute LU
    // L = Triangular matrix below main diagonal + I
    // U = Triangular matrix above (including) main diagonal
    int n = A.getNumberOfColumns();
    Matrix LU = new Matrix(A);
    for (int k = 0; k < n - 1; k++) {
      for (int i = k + 1; i < n; i++) {
        LU.set(i, k, LU.get(i, k) / LU.get(k, k));
        for (int j = k + 1; j < n; j++) {
          LU.set(i, j, LU.get(i, j) - LU.get(i, k) * LU.get(k, j));
        }
      }
    }

    // Ly = b
    Vector y = new Vector(n);
    for (int rowIndex = 0; rowIndex < n; rowIndex++) {
      double offset = 0;
      for (int columnIndex = 0; columnIndex < rowIndex; columnIndex++) {
        offset += LU.get(rowIndex, columnIndex) * y.get(columnIndex);
      }
      y.set(rowIndex, b.get(rowIndex) - offset);
    }

    // Ux = y
    Vector x = new Vector(n);
    for (int rowIndex = n - 1; rowIndex >= 0; rowIndex--) {
      double offset = 0;
      for (int columnIndex = rowIndex + 1; columnIndex < n; columnIndex++) {
        offset += LU.get(rowIndex, columnIndex) * x.get(columnIndex);
      }
      x.set(rowIndex, (y.get(rowIndex) - offset) / (LU.get(rowIndex, rowIndex)));
    }

    return x;
  }

  /**
   * Solves the linear system using the conjugate gradient method.
   * 
   * @param A
   *          Linear system.
   * @param b
   *          Right-hand-side.
   * @param x
   *          X contains the initial guess. If no guess is available, use 0. x
   *          must have the correct dimensions. The solutiuon will be written to
   *          this vector.
   * @param maxNumberOfIterations
   *          The iterative method is aborted when this number of iterations is
   *          reached.
   * @param convergenceEpsilon
   *          The solution is considered found when the error is smaller than
   *          this threshold.
   * @return Solution vector x (if it exists), null (otherwise)
   */
  public static Vector solveConjugateGradient(Matrix A, Vector b, Vector x, int maxNumberOfIterations,
      double convergenceEpsilon) {
    if (A == null || x == null || b == null || A.getNumberOfRows() != A.getNumberOfColumns()
        || A.getNumberOfColumns() != b.getDimension() || x.getDimension() != b.getDimension()) {
      throw new IllegalArgumentException();
    }

    Vector r = b.subtract(A.multiply(x));
    Vector p = new Vector(r);
    int iterationCounter = 0;
    while (iterationCounter < maxNumberOfIterations && r.getSqrNorm() > convergenceEpsilon * convergenceEpsilon) {
      Vector s = A.multiply(p);
      double alpha = r.multiply(r) / p.multiply(s);
      x = x.add(p.multiply(alpha));
      double betaLower = r.multiply(r);
      r = r.subtract(s.multiply(alpha));
      double beta = r.multiply(r) / betaLower;
      p = r.add(p.multiply(beta));
      iterationCounter++;

      //System.out.println(r.getNorm());
      // System.out.println(x);
    }

    if (r.getSqrNorm() < convergenceEpsilon) {
      System.out.println("CG solver converged.");
    } else {
      System.out.println("CG solver could not find a solution.");
    }

    return x;
  }
}

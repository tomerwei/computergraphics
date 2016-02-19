package cgresearch.core.math;

public class Matrix {

  private double[][] values;

  public Matrix(int numberOfRows, int numberOfColumns) {
    if (numberOfColumns < 1 || numberOfColumns < 1) {
      throw new IllegalArgumentException();
    }
    values = new double[numberOfRows][numberOfColumns];
  }

  /**
   * Constructor for 3x3 matrix
   */
  public Matrix(double v00, double v01, double v02, double v10, double v11, double v12, double v20, double v21,
      double v22) {
    this(3, 3);
    set(0, 0, v00);
    set(0, 1, v01);
    set(0, 2, v02);
    set(1, 0, v10);
    set(1, 1, v11);
    set(1, 2, v12);
    set(2, 0, v20);
    set(2, 1, v21);
    set(2, 2, v22);
  }

  /**
   * Constructor for 4x4 matrix
   */
  public Matrix(double v00, double v01, double v02, double v03, double v10, double v11, double v12, double v13,
      double v20, double v21, double v22, double v23, double v30, double v31, double v32, double v33) {
    this(4, 4);
    set(0, 0, v00);
    set(0, 1, v01);
    set(0, 2, v02);
    set(0, 3, v03);
    set(1, 0, v10);
    set(1, 1, v11);
    set(1, 2, v12);
    set(1, 3, v13);
    set(2, 0, v20);
    set(2, 1, v21);
    set(2, 2, v22);
    set(2, 3, v23);
    set(3, 0, v30);
    set(3, 1, v31);
    set(3, 2, v32);
    set(3, 3, v33);
  }

  /**
   * Copy constructor.
   * 
   * @param other
   *          Matrix to be cloned.
   */
  public Matrix(Matrix other) {
    this(other.getNumberOfRows(), other.getNumberOfColumns());
    for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
        set(rowIndex, columnIndex, other.get(rowIndex, columnIndex));
      }
    }
  }

  public int getNumberOfRows() {
    return values.length;
  }

  public int getNumberOfColumns() {
    return values[0].length;
  }

  public double get(int rowIndex, int columnIndex) {
    return values[rowIndex][columnIndex];
  }

  public void set(int rowIndex, int columnIndex, double value) {
    values[rowIndex][columnIndex] = value;
  }

  public Vector multiply(Vector other) {
    if (getNumberOfColumns() != other.getDimension()) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(getNumberOfRows());
    for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
      double value = 0;
      for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
        value += get(rowIndex, columnIndex) * other.get(columnIndex);
      }
      result.set(rowIndex, value);
    }
    return result;
  }

  public Matrix multiply(Matrix other) {
    if (getNumberOfColumns() != other.getNumberOfRows()) {
      throw new IllegalArgumentException();
    }
    Matrix result = new Matrix(getNumberOfRows(), other.getNumberOfColumns());
    for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < other.getNumberOfColumns(); columnIndex++) {
        double value = 0;
        for (int i = 0; i < getNumberOfColumns(); i++) {
          value += get(rowIndex, i) * other.get(i, columnIndex);
        }
        result.set(rowIndex, columnIndex, value);
      }
    }
    return result;
  }

  public Matrix add(Matrix other) {
    Matrix result = null;
    if (this.getNumberOfColumns() == other.getNumberOfColumns() && this.getNumberOfRows() == other.getNumberOfRows()) {
      result = new Matrix(getNumberOfRows(), getNumberOfColumns());
      for (int i = 0; i < result.getNumberOfRows(); i++) {
        for (int j = 0; j < result.getNumberOfColumns(); j++) {
          result.set(i, j, this.get(i, j) + other.get(i, j));
        }
      }
    } else {
      System.err.println("Dimensions Conflict!");
    }
    return result;
  }

  public double getDeterminant() {
    if (getNumberOfRows() != getNumberOfColumns()) {
      throw new IllegalArgumentException();
    }
    if (getNumberOfColumns() == 3) {
      return get(0, 0) * get(1, 1) * get(2, 2) + get(0, 1) * get(1, 2) * get(2, 0) + get(0, 2) * get(1, 0) * get(2, 1)
          - get(0, 2) * get(1, 1) * get(2, 0) - get(0, 1) * get(1, 0) * get(2, 2) - get(0, 0) * get(1, 2) * get(2, 1);
    } else {
      Jama.Matrix M = new Jama.Matrix(values);
      return M.det();
    }
  }

  public Matrix getInverse() {
    if (getNumberOfRows() != getNumberOfColumns()) {
      throw new IllegalArgumentException();
    }
    if (getNumberOfRows() == 3) {
      double det = getDeterminant();
      double a = get(0, 0);
      double b = get(0, 1);
      double c = get(0, 2);
      double d = get(1, 0);
      double e = get(1, 1);
      double f = get(1, 2);
      double g = get(2, 0);
      double h = get(2, 1);
      double i = get(2, 2);
      Matrix inverse = VectorMatrixFactory.newMatrix(e * i - f * h, c * h - b * i, b * f - c * e, f * g - d * i,
          a * i - c * g, c * d - a * f, d * h - e * g, b * g - a * h, a * e - b * d).multiply(1.0 / det);
      return inverse;
    } else {
      Jama.Matrix M = new Jama.Matrix(values);
      Jama.Matrix invM = M.inverse();
      Matrix result = VectorMatrixFactory.newMatrix(getNumberOfRows(), getNumberOfColumns());
      for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
        for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
          result.set(rowIndex, columnIndex, invM.getArray()[rowIndex][columnIndex]);
        }
      }
      return result;
    }
  }

  public Matrix getTransposed() {
    Matrix result = new Matrix(getNumberOfColumns(), getNumberOfRows());
    for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
        result.set(columnIndex, rowIndex, get(rowIndex, columnIndex));
      }
    }
    return result;
  }

  public double[] data() {
    double[] data = new double[getNumberOfRows() * getNumberOfColumns()];
    for (int row = 0; row < getNumberOfRows(); row++) {
      for (int col = 0; col < getNumberOfColumns(); col++) {
        data[row * getNumberOfColumns() + col] = get(row, col);
      }
    }
    return data;
  }

  public Matrix multiply(double d) {
    Matrix result = new Matrix(getNumberOfRows(), getNumberOfColumns());
    for (int row = 0; row < getNumberOfRows(); row++) {
      for (int column = 0; column < getNumberOfColumns(); column++) {
        result.set(row, column, get(row, column) * d);
      }
    }
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Matrix)) {
      return false;
    }
    Matrix otherMatrix = (Matrix) other;
    if (getNumberOfRows() != otherMatrix.getNumberOfRows()) {
      return false;
    }
    if (getNumberOfColumns() != otherMatrix.getNumberOfColumns()) {
      return false;
    }
    for (int row = 0; row < getNumberOfRows(); row++) {
      for (int col = 0; col < getNumberOfColumns(); col++) {
        if (!MathHelpers.equals(get(row, col), otherMatrix.get(row, col))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    String content = "";
    for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
        content += String.format("%4.3f ", get(rowIndex, columnIndex));
      }
      content += "\n";
    }
    return content;
  }
}

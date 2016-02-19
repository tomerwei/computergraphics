package cgresearch.core.math;

import java.io.Serializable;

public class Vector implements IVector, Serializable {

  private static final long serialVersionUID = 1L;

  private double[] values;

  public Vector(int dimension) {
    values = new double[dimension];
  }

  /**
   * Copy constructor.
   * 
   * @param vector
   *          Vector to be cloned.
   */
  public Vector(IVector other) {
    this(other.getDimension());
    for (int index = 0; index < getDimension(); index++) {
      set(index, other.get(index));
    }
  }

  @Override
  public int getDimension() {
    return values.length;
  }

  @Override
  public double get(int index) {
    return values[index];
  }

  @Override
  public void set(int index, double value) {
    values[index] = value;
  }

  @Override
  public String toString() {
    String content = "( ";
    for (int index = 0; index < getDimension(); index++) {
      content += String.format("%4.3f ", get(index));
    }
    content += ")\n";
    return content;
  }

  @Override
  public IVector subtract(IVector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }

    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) - other.get(index));
    }
    return result;
  }

  @Override
  public IVector add(IVector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }

    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) + other.get(index));
    }
    return result;
  }

  @Override
  public double getNorm() {
    return Math.sqrt(getSqrNorm());
  }

  @Override
  public double getSqrNorm() {
    double norm = 0;
    for (int index = 0; index < getDimension(); index++) {
      norm += get(index) * get(index);
    }
    return norm;
  }

  @Override
  public double multiply(IVector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }

    double result = 0;
    for (int index = 0; index < getDimension(); index++) {
      result += get(index) * other.get(index);
    }
    return result;
  }

  @Override
  public IVector multiply(double factor) {
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) * factor);
    }
    return result;
  }

  // Erweitert von Vitalij Kagaidj
  @Override
  public IMatrix innerProduct(IVector d) {
    IMatrix M = new Matrix(d.getDimension(), d.getDimension());
    for (int rowIndex = 0; rowIndex < d.getDimension(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < d.getDimension(); columnIndex++) {
        M.set(rowIndex, columnIndex, get(rowIndex) * d.get(columnIndex));
      }
    }
    return M;
  }

}

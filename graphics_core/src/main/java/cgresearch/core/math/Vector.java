package cgresearch.core.math;

import java.io.Serializable;

/**
 * Representation of a vector with arbitraty dimension.
 * 
 * @author Philipp Jenke
 */
public class Vector implements Serializable {

  private static final long serialVersionUID = 1L;

  private double[] values;

  public Vector(int dimension) {
    values = new double[dimension];
  }

  public Vector(Vector other) {
    this(other.getDimension());
    for (int index = 0; index < getDimension(); index++) {
      set(index, other.get(index));
    }
  }

  public Vector(double x, double y, double z) {
    this(3);
    set(0, x);
    set(1, y);
    set(2, z);
  }

  public Vector(double x, double y, double z, double w) {
    this(4);
    set(0, x);
    set(1, y);
    set(2, z);
    set(3, w);
  }

  public int getDimension() {
    return values.length;
  }

  public double get(int index) {
    return values[index];
  }

  public void set(int index, double value) {
    values[index] = value;
  }

  public Vector subtract(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) - other.get(index));
    }
    return result;
  }

  public Vector add(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) + other.get(index));
    }
    return result;
  }

  public double getNorm() {
    return Math.sqrt(getSqrNorm());
  }

  public double getSqrNorm() {
    double norm = 0;
    for (int index = 0; index < getDimension(); index++) {
      norm += get(index) * get(index);
    }
    return norm;
  }

  public double multiply(Vector other) {
    if (other == null || other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }

    double result = 0;
    for (int index = 0; index < getDimension(); index++) {
      result += get(index) * other.get(index);
    }
    return result;
  }

  public Vector multiply(double factor) {
    Vector result = new Vector(getDimension());
    for (int index = 0; index < getDimension(); index++) {
      result.set(index, get(index) * factor);
    }
    return result;
  }

  public Matrix innerProduct(Vector d) {
    Matrix M = new Matrix(d.getDimension(), d.getDimension());
    for (int rowIndex = 0; rowIndex < d.getDimension(); rowIndex++) {
      for (int columnIndex = 0; columnIndex < d.getDimension(); columnIndex++) {
        M.set(rowIndex, columnIndex, get(rowIndex) * d.get(columnIndex));
      }
    }
    return M;
  }

  public void copy(Vector other) {
    if (other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getDimension(); i++) {
      set(i, other.get(i));
    }
  }

  public Vector getNormalized() {
    final double d = getNorm();
    if (Math.abs(d) < MathHelpers.EPSILON) {
      throw new IllegalArgumentException();
    }
    return this.multiply(1.0 / d);
  }

  public void normalize() {
    double norm = getNorm();
    for (int i = 0; i < getDimension(); i++) {
      values[i] /= norm;
    }
  }

  public Vector cross(final Vector other) {
    if (getDimension() != 3 || other.getDimension() != 3) {
      throw new IllegalArgumentException();
    }
    Vector result = new Vector(3);
    result.set(MathHelpers.INDEX_0, get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_2)
        - get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_1));
    result.set(MathHelpers.INDEX_1, get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_0)
        - get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_2));
    result.set(MathHelpers.INDEX_2, get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_1)
        - get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_0));
    return result;
  }

  public void addSelf(Vector other) {
    if (other.getDimension() != getDimension()) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < getDimension(); i++) {
      set(i, get(i) + other.get(i));
    }
  }

  public void multiplySelf(double d) {
    for (int i = 0; i < getDimension(); i++) {
      set(i, get(i) * d);
    }
  }

  public float[] floatData() {
    float[] floatData = new float[values.length];
    for (int i = 0; i < values.length; i++) {
      floatData[i] = (float) values[i];
    }
    return floatData;
  }

  public double[] data() {
    double[] data = new double[values.length];
    for (int i = 0; i < values.length; i++) {
      data[i] = values[i];
    }
    return data;
  }

  public String toString(int precision) {
    String result = "(";
    for (int i = 0; i < getDimension(); i++) {
      result += String.format("%." + precision, values[i]);
    }
    return result;
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
  public boolean equals(Object other) {
    if (other == null || !(other instanceof Vector)) {
      return false;
    }
    Vector otherVec = (Vector) other;
    if (getDimension() != otherVec.getDimension()) {
      return false;
    }
    for (int i = 0; i < getDimension(); i++) {
      if (!MathHelpers.equals(get(i), otherVec.get(i))) {
        return false;
      }
    }
    return true;
  }
}

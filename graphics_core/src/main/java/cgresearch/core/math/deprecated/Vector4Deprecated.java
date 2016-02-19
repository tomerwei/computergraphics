///**
// * Prof. Philipp Jenke
// * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
// * Lecture demo program.
// */
//package cgresearch.core.math.deprecated;
//
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.MathHelpers;
//import cgresearch.core.math.Matrix;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.VectorMatrixFactory;
//
///**
// * Implementation of a vector class in 4-space.
// * 
// * @author Philipp Jenke
// * 
// */
//public class VectorDeprecated implements Vector {
//  private static final long serialVersionUID = -4829778222701077184L;
//
//  /**
//   * Array containing the vector values.
//   */
//  private final double[] values = { 0, 0, 0, 0 };
//
//  /**
//   * Constructor
//   */
//  public Vector() {
//  }
//
//  /**
//   * Constructor with initialization.
//   */
//  public Vector(double x, double y, double z, double w) {
//    values[MathHelpers.INDEX_0] = x;
//    values[MathHelpers.INDEX_1] = y;
//    values[MathHelpers.INDEX_2] = z;
//    values[MathHelpers.INDEX_3] = w;
//  }
//
//  /**
//   * Copy constructor.
//   */
//  public Vector(Vector other) {
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      values[i] = other.get(i);
//
//    }
//  }
//
//  @Override
//  public int hashCode() {
//    return new Double(values[0]).hashCode() + new Double(values[1]).hashCode() + new Double(values[2]).hashCode()
//        + new Double(values[3]).hashCode();
//  }
//
//  @Override
//  public boolean equals(Object other) {
//    if (!(other instanceof Vector)) {
//      return false;
//    }
//    Vector otherVector = (Vector) other;
//
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      if (Math.abs(values[i] - otherVector.get(i)) > MathHelpers.EPSILON) {
//        return false;
//      }
//    }
//
//    return true;
//  }
//
//  @Override
//  public Vector multiply(final double s) {
//    Vector result = new Vector();
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      result.set(i, values[i] * s);
//    }
//    return result;
//  }
//
//  @Override
//  public double multiply(Vector other) {
//    if (other.getDimension() != 4) {
//      throw new IllegalArgumentException();
//    }
//    double result = 0;
//    for (int i = 0; i < 4; i++) {
//      result += get(i) * other.get(i);
//    }
//    return result;
//  }
//
//  @Override
//  public double getSqrNorm() {
//    double sn = 0;
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      sn += values[i] * values[i];
//    }
//    return sn;
//  }
//
//  @Override
//  public double getNorm() {
//    return Math.sqrt(getSqrNorm());
//  }
//
//  @Override
//  public void set(final int index, final double value) {
//    values[index] = value;
//  }
//
//  @Override
//  public double get(final int index) {
//    return values[index];
//  }
//
//  @Override
//  public Vector subtract(Vector other) {
//    if (other.getDimension() != 4) {
//      throw new IllegalArgumentException();
//    }
//    Vector result = new Vector();
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      result.set(i, get(i) - other.get(i));
//    }
//    return result;
//  }
//
//  @Override
//  public Vector add(Vector other) {
//    if (other.getDimension() != 4) {
//      throw new IllegalArgumentException();
//    }
//    Vector result = new Vector();
//    for (int i = 0; i < MathHelpers.DIMENSION_4; i++) {
//      result.set(i, get(i) + other.get(i));
//    }
//    return result;
//  }
//
//  @Override
//  public Vector getNormalized() {
//    final double d = getNorm();
//    if (Math.abs(d) < MathHelpers.EPSILON) {
//      return new Vector();
//    }
//    Vector normalizedVector = new Vector(this);
//    normalizedVector.multiply(1.0 / d);
//    return normalizedVector;
//  }
//
//  @Override
//  public double[] data() {
//    return values;
//  }
//
//  @Override
//  public float[] floatData() {
//    float[] floatData = new float[values.length];
//    for (int i = 0; i < values.length; i++) {
//      floatData[i] = (float) values[i];
//    }
//    return floatData;
//  }
//
//  @Override
//  public String toString() {
//    return toString(5);
//  }
//
//  public String toString(int precision) {
//    return String.format("( %." + precision + "f, %." + precision + "f, %." + precision + "f, %." + precision + "f )",
//        values[0], values[1], values[2], values[3]);
//  }
//
//  @Override
//  public Vector toVector() {
//    return VectorMatrixFactory.newVector(get(0), get(1), get(2));
//  }
//
//  @Override
//  public int getDimension() {
//    return 4;
//  }
//
//  @Override
//  public Matrix innerProduct(Vector other) {
//    if (other.getDimension() != 4) {
//      throw new IllegalArgumentException();
//    }
//    Matrix matrix = VectorMatrixFactory.newMatrix(4, 4);
//    for (int row = 0; row < 4; row++) {
//      for (int column = 0; column < 4; column++) {
//        matrix.set(row, column, get(row) * other.get(column));
//      }
//    }
//    return matrix;
//  }
//
//  @Override
//  public void copy(Vector other) {
//    if (other.getDimension() != getDimension()) {
//      throw new IllegalArgumentException();
//    }
//    for (int i = 0; i < getDimension(); i++) {
//      set(i, other.get(i));
//    }
//  }
//}

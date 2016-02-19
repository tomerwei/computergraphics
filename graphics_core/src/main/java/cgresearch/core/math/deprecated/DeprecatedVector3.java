///**
// * Prof. Philipp Jenke
// * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
// * Lecture demo program.
// */
//package cgresearch.core.math.deprecated;
//
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.MathHelpers;
//import cgresearch.core.math.Matrix;
//import cgresearch.core.math.Vector;
//import cgresearch.core.math.VectorMatrixFactory;
//
///**
// * 
// * @author Philipp Jenke
// * 
// */
//public class DeprecatedVector3 {
//  /**
//   * Version 1: Initial commit
//   */
//  private static final long serialVersionUID = 1;
//
//  /**
//   * Array containing the vector values.
//   */
//  private final double[] values = { 0, 0, 0 };
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
//  public Vector(double x, double y, double z) {
//    values[MathHelpers.INDEX_0] = x;
//    values[MathHelpers.INDEX_1] = y;
//    values[MathHelpers.INDEX_2] = z;
//  }
//
//  /**
//   * Copy constructor.
//   */
//  public Vector(Vector other) {
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
//      values[i] = other.get(i);
//    }
//  }
//
//  @Override
//  public int hashCode() {
//    return new Double(values[0]).hashCode() + new Double(values[1]).hashCode() + new Double(values[2]).hashCode();
//  }
//
//  @Override
//  public boolean equals(Object other) {
//    if (!(other instanceof Vector)) {
//      return false;
//    }
//    Vector otherVector = (Vector) other;
//
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
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
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
//      result.set(i, values[i] * s);
//    }
//    return result;
//  }
//
//  @Override
//  public double getSqrNorm() {
//    double sn = 0;
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
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
//  public Vector cross(final Vector other) {
//    if (other.getDimension() != 3) {
//      throw new IllegalArgumentException();
//    }
//    Vector result = new Vector();
//    result.set(MathHelpers.INDEX_0, get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_2)
//        - get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_1));
//    result.set(MathHelpers.INDEX_1, get(MathHelpers.INDEX_2) * other.get(MathHelpers.INDEX_0)
//        - get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_2));
//    result.set(MathHelpers.INDEX_2, get(MathHelpers.INDEX_0) * other.get(MathHelpers.INDEX_1)
//        - get(MathHelpers.INDEX_1) * other.get(MathHelpers.INDEX_0));
//    return result;
//  }
//
//  @Override
//  public Vector subtract(Vector other) {
//    Vector result = new Vector();
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
//      result.set(i, get(i) - other.get(i));
//    }
//    return result;
//  }
//
//  @Override
//  public Vector add(Vector other) {
//    Vector result = new Vector();
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
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
//    Vector normalizedVector = new Vector(this.multiply(1.0 / d));
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
//  public double multiply(Vector other) {
//    double result = get(0) * other.get(0) + get(1) * other.get(1) + get(2) * other.get(2);
//    return result;
//  }
//
//  /*
//   * (nicht-Javadoc)
//   * 
//   * @see edu.haw.cg.math.Vector#copy(edu.haw.cg.math.Vector)
//   */
//  @Override
//  public void copy(Vector other) {
//    if (other == null) {
//      throw new IllegalArgumentException("Invalid copy vector.");
//    }
//    values[MathHelpers.INDEX_0] = other.get(MathHelpers.INDEX_0);
//    values[MathHelpers.INDEX_1] = other.get(MathHelpers.INDEX_1);
//    values[MathHelpers.INDEX_2] = other.get(MathHelpers.INDEX_2);
//  }
//
//  /*
//   * (nicht-Javadoc)
//   * 
//   * @see edu.haw.cg.math.Vector#normalize()
//   */
//  @Override
//  public void normalize() {
//    double norm = getNorm();
//    values[MathHelpers.INDEX_0] /= norm;
//    values[MathHelpers.INDEX_1] /= norm;
//    values[MathHelpers.INDEX_2] /= norm;
//  }
//
//  /*
//   * (nicht-Javadoc)
//   * 
//   * @see edu.haw.cg.math.Vector#makeHomogenious()
//   */
//  @Override
//  public Vector makeHomogenious() {
//    return VectorMatrixFactory.newVector(values[0], values[1], values[2], 1);
//  }
//
//  @Override
//  public Matrix innerProduct(Vector other) {
//    if (other.getDimension() != 3) {
//      throw new IllegalArgumentException();
//    }
//    Matrix matrix = VectorMatrixFactory.newMatrix(0, 0, 0, 0, 0, 0, 0, 0, 0);
//    for (int row = 0; row < 3; row++) {
//      for (int column = 0; column < 3; column++) {
//        matrix.set(row, column, get(row) * other.get(column));
//      }
//    }
//    return matrix;
//  }
//
//  @Override
//  public void multiplySelf(double s) {
//    values[0] *= s;
//    values[1] *= s;
//    values[2] *= s;
//  }
//
//  @Override
//  public String toString() {
//    return toString(5);
//  }
//
//  @Override
//  public String toString(int precision) {
//    return String.format("( %." + precision + "f, %." + precision + "f, %." + precision + "f )", values[0], values[1],
//        values[2]);
//  }
//
//  @Override
//  public void addSelf(Vector other) {
//    if (other.getDimension() != 3) {
//      throw new IllegalArgumentException();
//    }
//    for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
//      set(i, get(i) + other.get(i));
//    }
//  }
//
//  @Override
//  public Vector getHomogenious() {
//    return VectorMatrixFactory.newVector(get(0), get(1), get(2), 1);
//  }
//
//  @Override
//  public void set(double x, double y, double z) {
//    set(0, x);
//    set(1, y);
//    set(2, z);
//  }
//
//  @Override
//  public int getDimension() {
//    return 3;
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
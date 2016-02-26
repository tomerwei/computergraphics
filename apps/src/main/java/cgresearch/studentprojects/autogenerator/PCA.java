package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import Jama.EigenvalueDecomposition;
import cgresearch.core.math.Matrix;

public class PCA {

  /**
   * Centroid
   */
  private Vector centroid = null;

  /**
   * Container for the points.
   */
  List<Vector> points = new ArrayList<Vector>();

  /**
   * This matix holds the eigenvalues of the analyzed point set.
   */
  private Jama.Matrix V = null;

  /**
   * This diagonal matrix holds the eigenvectors of the analyzed point set.
   */
  private Jama.Matrix D = null;

  /**
   * Constructor
   */
  public PCA() {
  }

  /**
   * Add an additional point.
   */
  public void add(Vector point) {
    points.add(point);
  }

  /**
   * Apply the PCA, compute tangentU, tangentV and normal.
   */
  public void applyPCA() {

    if (points.size() < 3) {
      Logger.getInstance().error("Need a least 3 points for PCA");
      return;
    }

    int dimension = points.get(0).getDimension();

    // Compute centroid
    centroid = new Vector(dimension);
    for (Vector p : points) {
      centroid = centroid.add(p);
    }
    centroid = centroid.multiply(1.0 / points.size());

    Matrix M = new Matrix(dimension, dimension);
    for (Vector p : points) {
      Vector d = p.subtract(centroid);
      M = M.add(d.innerProduct(d));
    }

    // Singular value decomposition
    Jama.Matrix jamaM = new Jama.Matrix(dimension, dimension);
    for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
      for (int colIndex = 0; colIndex < dimension; colIndex++) {
        jamaM.set(colIndex, rowIndex, M.get(colIndex, rowIndex));
      }
    }
    EigenvalueDecomposition e = jamaM.eig();
    V = e.getV();
    D = e.getD();
  }

  /**
   * Getter.
   */
  public double getEigenValue(int index) {
    return D.get(index, index);
  }

  /**
   * Getter.
   */
  public Vector getEigenVector(int index) {
    int dimension = D.getColumnDimension();
    Vector eigenVector = new Vector(dimension);
    for (int i = 0; i < dimension; i++) {
      eigenVector.set(i, V.get(i, index));
    }
    return eigenVector;
  }

  /**
   * Clear list of points.
   */
  public void clear() {
    points.clear();
    D = null;
    V = null;
  }

  public Matrix getVDV() {
    int dimension = points.get(0).getDimension();
    Matrix result = new Matrix(dimension, dimension);
    Matrix v = new Matrix(V.getRowDimension(), V.getColumnDimension());
    Matrix d = new Matrix(D.getRowDimension(), D.getColumnDimension());
    Jama.Matrix VV = V.transpose();
    Matrix vv = new Matrix(V.getRowDimension(), V.getColumnDimension());

    for (int rowIndex = 0; rowIndex < dimension; rowIndex++) {
      for (int colIndex = 0; colIndex < dimension; colIndex++) {
        v.set(colIndex, rowIndex, V.get(colIndex, rowIndex));
        d.set(colIndex, rowIndex, D.get(colIndex, rowIndex));
        vv.set(colIndex, rowIndex, VV.get(colIndex, rowIndex));
      }
    }

    result = v.multiply(d);
    result = result.multiply(vv);

    return result;
  }

  public void testPCA() {
    int dimension = 3;
    for (int i = 0; i < 10; i++) {
      System.out.println("Vector " + (i + 1) + ":");
      Vector v = new Vector(dimension);
      v.set(0, Math.random() * 2);
      // System.out.println("Wert 0 = " + v.get(0));
      for (int j = 1; j < dimension; j++) {
        v.set(j, Math.random());
        // System.out.println("Wert " + j + " = " + v.get(j));
      }
      this.add(v);
      System.out.println(v);
    }

    this.applyPCA();

  }

  public Vector getCentroid() {
    return centroid;
  }

  public void setCentroid(Vector centroid) {
    this.centroid = centroid;
  }

  public Jama.Matrix getV() {
    return V;
  }

  public void setV(Jama.Matrix v) {
    V = v;
  }

  public Jama.Matrix getD() {
    return D;
  }

  public void setD(Jama.Matrix d) {
    D = d;
  }

}

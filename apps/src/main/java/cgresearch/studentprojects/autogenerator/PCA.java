package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix;
import cgresearch.core.math.IVector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.jama.EigenvalueDecomposition;
import cgresearch.core.math.Matrix;

public class PCA {

  /**
   * Centroid
   */
  private IVector centroid = null;

  /**
   * Container for the points.
   */
  List<IVector> points = new ArrayList<IVector>();

  /**
   * This matix holds the eigenvalues of the analyzed point set.
   */
  private cgresearch.core.math.jama.Matrix V = null;

  /**
   * This diagonal matrix holds the eigenvectors of the analyzed point set.
   */
  private cgresearch.core.math.jama.Matrix D = null;

  /**
   * Constructor
   */
  public PCA() {
  }

  /**
   * Add an additional point.
   */
  public void add(IVector point) {
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
    for (IVector p : points) {
      centroid = centroid.add(p);
    }
    centroid = centroid.multiply(1.0 / points.size());

    IMatrix M = new Matrix(dimension, dimension);
    for (IVector p : points) {
      IVector d = p.subtract(centroid);
      M = M.add(d.innerProduct(d));
    }

    // Singular value decomposition
    cgresearch.core.math.jama.Matrix jamaM = new cgresearch.core.math.jama.Matrix(dimension, dimension);
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
  public IVector getEigenVector(int index) {
    int dimension = D.getColumnDimension();
    IVector eigenVector = new Vector(dimension);
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

  public IMatrix getVDV() {
    int dimension = points.get(0).getDimension();
    IMatrix result = new Matrix(dimension, dimension);
    IMatrix v = new Matrix(V.getRowDimension(), V.getColumnDimension());
    IMatrix d = new Matrix(D.getRowDimension(), D.getColumnDimension());
    cgresearch.core.math.jama.Matrix VV = V.transpose();
    IMatrix vv = new Matrix(V.getRowDimension(), V.getColumnDimension());

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
      IVector v = new Vector(dimension);
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

  public IVector getCentroid() {
    return centroid;
  }

  public void setCentroid(IVector centroid) {
    this.centroid = centroid;
  }

  public cgresearch.core.math.jama.Matrix getV() {
    return V;
  }

  public void setV(cgresearch.core.math.jama.Matrix v) {
    V = v;
  }

  public cgresearch.core.math.jama.Matrix getD() {
    return D;
  }

  public void setD(cgresearch.core.math.jama.Matrix d) {
    D = d;
  }

}

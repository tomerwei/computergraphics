package cgresearch.studentprojects.registration;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import Jama.EigenvalueDecomposition;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;



public class IcpDistanceFunction {

  int x = 0, y = 1, z = 2;
  double dk = 0.0;

  public IcpDistanceFunction() {

  }

  public IPointCloud startAlgorithm(IPointCloud Base, IPointCloud Register, int iteration) {
    Point ux, up;

    NearestPointWithKdTree nearest = new NearestPointWithKdTree();

    int[] nearestPoints = nearest.nearestPoints(Base, Register);

    up = centerOfMassyBase(Base);
    ux = centerOfMassyRegister(Register, nearestPoints);
//    System.out.println("up: " + up.getPosition());
//    System.out.println("ux: " + ux.getPosition());

    Matrix covarianceMatrix = this.getCrossCovarianceMatrix(Base, Register, up, ux, nearestPoints);
    cgresearch.core.math.Matrix q = this.getQ(covarianceMatrix);
    Vector eigenVector = getMaxEigenVector(q);
    Matrix rotationMatrix = getRotationMatrix(eigenVector);
    Vector translationVector = translationVector(rotationMatrix, up, ux);
    computeNewPointCloud(Register, rotationMatrix, translationVector);
    dk = computeDk(rotationMatrix, translationVector, Base, Register, nearestPoints);
    Logger.getInstance()
        .message("| " + iteration + " \t| " + eigenVector.toString() + "" + translationVector + " \t| " + dk + " \t |");

    return Register;

  }

  

  /**
   * calculate up = sum of nearest Points of Base PointCloud
   * 
   * @param ipc
   * @return
   */

  public Point centerOfMassyBase(IPointCloud ipc) {

    Vector newPoint = VectorFactory.createVector3(0, 0, 0);

    for (int i = 0; i < ipc.getNumberOfPoints(); i++) {
      newPoint.set(x, (newPoint.get(x) + ipc.getPoint(i).getPosition().get(x)));
      newPoint.set(y, (newPoint.get(y) + ipc.getPoint(i).getPosition().get(y)));
      newPoint.set(z, (newPoint.get(z) + ipc.getPoint(i).getPosition().get(z)));
      // System.out.println("i centerofmass "+ i);
    }

    newPoint.set(x, (newPoint.get(x) / ipc.getNumberOfPoints()));
    newPoint.set(y, (newPoint.get(y) / ipc.getNumberOfPoints()));
    newPoint.set(z, (newPoint.get(z) / ipc.getNumberOfPoints()));
    return new Point(newPoint);

  }

  /**
   * calculate ux = sum of nearest Points of Register PointCloud
   * 
   * @param ipc
   * @param next
   * @return
   */
  public Point centerOfMassyRegister(IPointCloud ipc, int[] next) {

    Vector newPoint = VectorFactory.createVector3(0, 0, 0);
    for (int i = 0; i < ipc.getNumberOfPoints(); i++) {
      newPoint.set(x, (newPoint.get(x) + ipc.getPoint(next[i]).getPosition().get(x)));
      newPoint.set(y, (newPoint.get(y) + ipc.getPoint(next[i]).getPosition().get(y)));
      newPoint.set(z, (newPoint.get(z) + ipc.getPoint(next[i]).getPosition().get(z)));
      // System.out.println("i centerofmass "+ i);
    }

    newPoint.set(x, (newPoint.get(x) / ipc.getNumberOfPoints()));
    newPoint.set(y, (newPoint.get(y) / ipc.getNumberOfPoints()));
    newPoint.set(z, (newPoint.get(z) / ipc.getNumberOfPoints()));
    return new Point(newPoint);

  }

  /**
   * Calculate the cross Covariance Matrix
   */

  private Matrix getCrossCovarianceMatrix(IPointCloud Base, IPointCloud Register, Point up, Point ux, int[] next) {

    // if(Base.getNumberOfPoints()!= Register.getNumberOfPoints())
    // return null;

    Matrix covariance = MatrixFactory.createMatrix(3, 3);

    for (int i = 0; i < Base.getNumberOfPoints(); i++) {

      for (int k = 0; k < 3; k++) {
        for (int j = 0; j < 3; j++) {

          covariance.set(k, j, (covariance.get(k, j)
              + (Base.getPoint(i).getPosition().get(k) * Register.getPoint(next[i]).getPosition().get(j))));
          // System.out.println("Rechnung covariance:i = "+i+" k = "+k+"j =
          // "+j+" covarinace.get: " + (covariance.get(k, j) +" + (Base:
          // "+Base.getPoint(i).getPosition().get(k) +" * Register next
          // "+Register.getPoint(next[i]).getPosition().get(j)+" = "+
          // (covariance.get(k, j) + Base.getPoint(i).getPosition().get(k) *
          // Register.getPoint(next[i]).getPosition().get(j))));
          // if(j==2){
          // System.out.println("\n");
          //
          // }
        }

      }
    }

    // System.out.println("Convariance Matrix erster SChritt:\n " +
    // covariance.toString());

    for (int k = 0; k < 3; k++) {
      for (int j = 0; j < 3; j++) {

        covariance.set(k, j, (covariance.get(k, j) / Base.getNumberOfPoints()));

      }
    }
    // System.out.println("Convariance Matrix zweiter schritt SChritt:\n " +
    // covariance.toString());
    for (int u = 0; u < 3; u++) {
      for (int j = 0; j < 3; j++) {

        covariance.set(u, j, (covariance.get(u, j) - (up.getPosition().get(u) * ux.getPosition().get(j))));
      }
    }
//    System.out.println("Convariance Matrix SChritt:\n " + covariance.toString());
    return covariance;

  }

  /**
   * 4x4 Matrix Q
   * 
   * @param covariance
   * @return
   */

  private Matrix getQ(Matrix covariance) {

    Matrix q = new cgresearch.core.math.Matrix(4, 4);
    q.set(0, 0, getTrace(covariance));

    // Coveriance Matrix - Covariance Matrix transponiert, ergibt den Vector
    // Delta, um Q zu bilden.

    q.set(1, 0, (covariance.get(1, 2) - covariance.get(2, 1)));
    q.set(2, 0, (covariance.get(2, 0) - covariance.get(0, 2)));
    q.set(3, 0, (covariance.get(0, 1) - covariance.get(1, 0)));

    // Transponiert Delta
    q.set(0, 1, (q.get(1, 0)));
    q.set(0, 2, (q.get(2, 0)));
    q.set(0, 3, (q.get(3, 0)));

    // Rest von Q auffï¿½llen

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        q.set((1 + i), (1 + j), (covariance.get(i, j) + covariance.get(j, i) - (i == j ? q.get(0, 0) : 0)));
      }
    }
//    System.out.println("Matrix Q:\n " + q.toString());
    return q;
  }

  /**
   * used to calculate the Q Matrix
   * 
   * @param covariance
   * @return
   */

  private double getTrace(Matrix covariance) {
    double trace = 0;
    for (int i = 0; i < 3; i++) {
      trace += covariance.get(i, i);
    }

    return trace;
  }

  /**
   * Max EigenVector. Itï¿½s needed to calculate the RotationMatrix
   * 
   * @param q
   * @return
   */

  private Vector getMaxEigenVector(Matrix q) {

    double[][] temp = new double[4][4];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        temp[i][j] = q.get(i, j);
      }
    }

    Jama.Matrix m = new Jama.Matrix(temp);
    EigenvalueDecomposition evd = new EigenvalueDecomposition(m);

    double[] eigenValues = evd.getRealEigenvalues();
    double max = Double.NEGATIVE_INFINITY;
    int index = 0;
    Vector eigenVector = new Vector(4);

    for (int i = 0; i < eigenValues.length; i++) {

      if (eigenValues[i] > max) {
        max = eigenValues[i];
        index = i;
      }
    }

    double[] test = evd.getV().transpose().getArray()[index]; //Länge von 4

    for (int i = 0; i < test.length; i++) {
      eigenVector.set(i, test[i]); //passen nur 3 rein
//      System.out.println("count i for eigenVector: "+eigenVector.get(i));
    }
   
    return eigenVector;

  }

  /**
   * Return optimal RotationMatrix
   * 
   * @param eigenVector
   * @return
   */

  private Matrix getRotationMatrix(Vector eigenVector) {

    Matrix rotationMatrix = new Matrix(3, 3);
    int q0 = 0, q1 = 1, q2 = 2, q3 = 3;

    rotationMatrix.set(0, 0, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q1) * eigenVector.get(q1))
        - (eigenVector.get(q2) * eigenVector.get(q2)) - (eigenVector.get(q3) * eigenVector.get(q3))));

    rotationMatrix.set(0, 1,
        (2 * ((eigenVector.get(q1) * eigenVector.get(q2)) - (eigenVector.get(q0) * eigenVector.get(q3)))));
    rotationMatrix.set(0, 2,
        (2 * ((eigenVector.get(q1) * eigenVector.get(q3)) + (eigenVector.get(q0) * eigenVector.get(q2)))));

    rotationMatrix.set(1, 0,
        (2 * ((eigenVector.get(q1) * eigenVector.get(q2)) + (eigenVector.get(q0) * eigenVector.get(q3)))));
    rotationMatrix.set(1, 1, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q2) * eigenVector.get(q2))
        - (eigenVector.get(q1) * eigenVector.get(q1)) - (eigenVector.get(q3) * eigenVector.get(q3))));

    rotationMatrix.set(1, 2,
        (2 * ((eigenVector.get(q2) * eigenVector.get(q3)) - (eigenVector.get(q0) * eigenVector.get(q1)))));

    rotationMatrix.set(2, 0,
        (2 * ((eigenVector.get(q1) * eigenVector.get(q3)) - (eigenVector.get(q0) * eigenVector.get(q2)))));
    rotationMatrix.set(2, 1,
        (2 * ((eigenVector.get(q2) * eigenVector.get(q3)) + (eigenVector.get(q0) * eigenVector.get(q1)))));
    rotationMatrix.set(2, 2, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q3) * eigenVector.get(q3))
        - (eigenVector.get(q1) * eigenVector.get(q1)) - (eigenVector.get(q2) * eigenVector.get(q2))));

//    System.out.println("RotationsMatrix: \n" + rotationMatrix.toString());

    return rotationMatrix;

  }

  /**
   * Optimal translation vector
   * 
   * @param rotation
   * @param upCloudRegister
   * @param uxCloudClosest
   * @return
   */

  private Vector translationVector(Matrix rotation, Point upCloudRegister, Point uxCloudClosest) {

    Vector translation = new Vector(3);

    translation.set(x,
        (uxCloudClosest.getPosition().get(x) - (rotation.get(0, 0) * upCloudRegister.getPosition().get(x)
            + rotation.get(0, 1) * upCloudRegister.getPosition().get(y)
            + rotation.get(0, 2) * upCloudRegister.getPosition().get(z))));

    translation.set(y,
        (uxCloudClosest.getPosition().get(y) - (rotation.get(1, 0) * upCloudRegister.getPosition().get(x)
            + rotation.get(1, 1) * upCloudRegister.getPosition().get(y)
            + rotation.get(1, 2) * upCloudRegister.getPosition().get(z))));

    translation.set(z,
        (uxCloudClosest.getPosition().get(z) - (rotation.get(2, 0) * upCloudRegister.getPosition().get(x)
            + rotation.get(2, 1) * upCloudRegister.getPosition().get(y)
            + rotation.get(2, 2) * upCloudRegister.getPosition().get(z))));

//    System.out.println("Translation: \n" + translation.toString());

    return translation;
  }

  /**
   * Calculate Error of ICP
   * 
   * @param rotation
   * @param translation
   * @param Base
   * @param Register
   * @param next
   * @return
   */

  private double computeDk(Matrix rotation, Vector translation, IPointCloud Base, IPointCloud Register, int[] next) {
    // Vector rotationBase = new Vector();
    // Point bla = new Point();
    double dk = 0;
    double error = 0;
    IPointCloud temp = new PointCloud();
    IPointCloud temp2 = new PointCloud();
    double x1, y1, z1, x2, y2, z2;
    // Rotation *pi

    // temp anstatt Register
    for (int i = 0; i < Base.getNumberOfPoints(); i++) {

      x1 = 0;
      y1 = 0;
      z1 = 0;

      x1 = ((rotation.get(0, 0) * Base.getPoint(i).getPosition().get(x))
          + (rotation.get(0, 1) * Base.getPoint(i).getPosition().get(y)
              + (rotation.get(0, 2) * Base.getPoint(i).getPosition().get(z))));
      y1 = ((rotation.get(1, 0) * Base.getPoint(i).getPosition().get(x))
          + (rotation.get(1, 1) * Base.getPoint(i).getPosition().get(y)
              + (rotation.get(1, 2) * Base.getPoint(i).getPosition().get(z))));
      z1 = ((rotation.get(2, 0) * Base.getPoint(i).getPosition().get(x))
          + (rotation.get(2, 1) * Base.getPoint(i).getPosition().get(y)
              + (rotation.get(2, 2) * Base.getPoint(i).getPosition().get(z))));

      Vector position = VectorFactory.createVector3(x1, y1, z1);
      temp.addPoint(new Point(position));

      // (R*pi)-t temp anstatt Register
      // rotationBase.add(translation);

      temp.getPoint(i).getPosition().set(x, (temp.getPoint(i).getPosition().get(x) - translation.get(x)));
      temp.getPoint(i).getPosition().set(y, (temp.getPoint(i).getPosition().get(y) - translation.get(y)));
      temp.getPoint(i).getPosition().set(z, (temp.getPoint(i).getPosition().get(z) - translation.get(z)));

      // (R*pi)+T-pj
      // temp2 anstatt Register

      x2 = 0;
      y2 = 0;
      z2 = 0;

      x2 = (Register.getPoint(next[i]).getPosition().get(x) - temp.getPoint(i).getPosition().get(x));
      y2 = (Register.getPoint(next[i]).getPosition().get(y) - temp.getPoint(i).getPosition().get(y));
      z2 = (Register.getPoint(next[i]).getPosition().get(z) - temp.getPoint(i).getPosition().get(z));

      Vector position2 = VectorFactory.createVector3(x2, y2, z2);
      temp2.addPoint(new Point(position2));

      error = error + (temp2.getPoint(i).getPosition().get(x) * temp2.getPoint(i).getPosition().get(x)
          + temp2.getPoint(i).getPosition().get(y) * temp2.getPoint(i).getPosition().get(y)
          + temp2.getPoint(i).getPosition().get(z) * temp2.getPoint(i).getPosition().get(z));

    }

    dk = error / Register.getNumberOfPoints();

    return dk;

  }

  /**
   * Calculate new Rgister PointCloud
   * 
   * @param rotationMatrix
   * @param translationVector
   */

  private void computeNewPointCloud(IPointCloud Register, Matrix rotationMatrix, Vector translationVector) {
    IPointCloud newPoints = new PointCloud();
    double x1, y1, z1;

    Point p = new Point();
    // Pk+1 = Pk * R
    for (int i = 0; i < Register.getNumberOfPoints(); i++) {
      x1 = 0;
      y1 = 0;
      z1 = 0;

      x1 = ((rotationMatrix.getTransposed().get(0, 0) * Register.getPoint(i).getPosition().get(x))
          + (rotationMatrix.getTransposed().get(0, 1) * Register.getPoint(i).getPosition().get(y)
              + (rotationMatrix.getTransposed().get(0, 2) * Register.getPoint(i).getPosition().get(z))));
      y1 = ((rotationMatrix.getTransposed().get(1, 0) * Register.getPoint(i).getPosition().get(x))
          + (rotationMatrix.getTransposed().get(1, 1) * Register.getPoint(i).getPosition().get(y)
              + (rotationMatrix.getTransposed().get(1, 2) * Register.getPoint(i).getPosition().get(z))));
      z1 = ((rotationMatrix.getTransposed().get(2, 0) * Register.getPoint(i).getPosition().get(x))
          + (rotationMatrix.getTransposed().get(2, 1) * Register.getPoint(i).getPosition().get(y)
              + (rotationMatrix.getTransposed().get(2, 2) * Register.getPoint(i).getPosition().get(z))));
      Vector position = VectorFactory.createVector3(x1, y1, z1);
      newPoints.addPoint(new Point(position));

      // Pk+1 = Pk * R + t

      newPoints.getPoint(i).getPosition().set(x,
          (newPoints.getPoint(i).getPosition().get(x) - translationVector.get(x)));
      newPoints.getPoint(i).getPosition().set(y,
          (newPoints.getPoint(i).getPosition().get(y) - translationVector.get(y)));
      newPoints.getPoint(i).getPosition().set(z,
          (newPoints.getPoint(i).getPosition().get(z) - translationVector.get(z)));
      Register.getPoint(i).getPosition().set(x, newPoints.getPoint(i).getPosition().get(x));
      Register.getPoint(i).getPosition().set(y, newPoints.getPoint(i).getPosition().get(y));
      Register.getPoint(i).getPosition().set(z, newPoints.getPoint(i).getPosition().get(z));

      // x_neu = R^t * ( x_alt - t)
//      System.out.println("Transponierte Rotations MAtrix :\n" + rotationMatrix.getTransposed().toString());

    }
  }

}

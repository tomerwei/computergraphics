package cgresearch.studentprojects.registration;

import java.util.Arrays;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.Matrix3;
import cgresearch.core.math.Matrix4;
import cgresearch.core.math.Vector3;
import cgresearch.core.math.Vector4;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.core.math.functions.Function;
import cgresearch.core.math.jama.EigenvalueDecomposition;
import cgresearch.core.math.jama.Matrix;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

public class IcpDistanceFunction  {
	
	private IPointCloud pointCloudBase;
	
	private IPointCloud pointCloudRegister;
	
	
	
	int x = 0,y = 1,z = 2;
	
	
	public IcpDistanceFunction(IPointCloud pointCloudBase) {
		this.pointCloudBase = pointCloudBase;


	}
	
	
	public IPointCloud startAlgorithm(IPointCloud newPointCloud){
		
		pointCloudRegister = getClosestPoints(newPointCloud);
		
		Point upCloudBase = centerOfMass(pointCloudBase);
		Point uxCloudRegister = centerOfMass(pointCloudRegister);
		
		Matrix3 covarianceMatrix = this.getCrossCovarianceMatrix(pointCloudBase, pointCloudRegister, upCloudBase, uxCloudRegister);
		Matrix4 q = this.getQ(covarianceMatrix);
		Vector4 eigenVector = getMaxEigenVector(q);
		Matrix3 rotationMatrix = getRotationMatrix(eigenVector);
		Vector3 translationVector = translationVector(rotationMatrix, upCloudBase, uxCloudRegister);
		
		System.out.println("\n");
		System.out.println("eigenVector: "+ eigenVector.toString());
		System.out.println("\n");
		System.out.println("RotationsMatrix: \n"+ rotationMatrix.toString());
		System.out.println("\n");
		System.out.println("TranslationVector: \n"+ translationVector.toString());
		pointCloudRegister = computeRegistration(rotationMatrix, translationVector, pointCloudRegister);
		
		
		return pointCloudRegister;
		
	}
	
	/*
	 * Get the closestPoints between two Pointclouds
	 */
	
	private IPointCloud getClosestPoints(IPointCloud pointCloudRegister){
		
		Point[] tmp = new Point[pointCloudBase.getNumberOfPoints()];
		IPointCloud ClosestPoints = new PointCloud();
		
		for(int i =0 ; i < pointCloudBase.getNumberOfPoints();){
			Point closest = null;
			double dist = Double.POSITIVE_INFINITY;

			for(int k = 0; k < pointCloudRegister.getNumberOfPoints(); k++)
			{
				System.out.println("i: "+i+" "+pointCloudBase.getPoint(i).getPosition());
				System.out.println("k: "+i+" "+pointCloudRegister.getPoint(k).getPosition());
				double distance = getDistance(pointCloudBase.getPoint(i), pointCloudRegister.getPoint(k));
				System.out.println("Distance: "+distance);
				System.out.println("Dist: "+dist);
				if(distance < dist)
				{
					closest = pointCloudRegister.getPoint(k);
					
					dist = distance;
				}
			}
			ClosestPoints.addPoint(closest);
			System.out.println("i ClosestPoints: "+i+" "+ClosestPoints.getPoint(i).getPosition());
			
			
			i++;
		}
		for(int j = 0; j <3; j++){
			System.out.println("ClosestsPoint: " + ClosestPoints.getPoint(j).getPosition());
		}
		
		return ClosestPoints;
	}
	
	
	private double getDistance(Point a, Point b){
		
		return Math.sqrt(Math.pow(b.getPosition().get(0) - a.getPosition().get(0),2) + Math.pow(b.getPosition().get(1) - a.getPosition().get(1),2) + Math.pow(b.getPosition().get(2) - a.getPosition().get(2),2));
		
		
	}
	
	/*
	 * Center of Mass for the PointCLoud
	 */
	
	private Point centerOfMass(IPointCloud ipc){
		
		
		IVector3 newPoint = VectorMatrixFactory.newIVector3(0,0,0);
				
		for(int i = 0; i < ipc.getNumberOfPoints(); i++)
		{
			newPoint.set(x, (newPoint.get(x) + ipc.getPoint(i).getPosition().get(x)));
			newPoint.set(y, (newPoint.get(y) + ipc.getPoint(i).getPosition().get(y)));
			newPoint.set(z, (newPoint.get(z) + ipc.getPoint(i).getPosition().get(z)));			
		}
		newPoint.set(x, (newPoint.get(x) / ipc.getNumberOfPoints()));
		newPoint.set(y, (newPoint.get(y) / ipc.getNumberOfPoints()));
		newPoint.set(z, (newPoint.get(z) / ipc.getNumberOfPoints()));
		return new Point(newPoint);
		
	}
	
	/*
	 * Calculate the cross Covariance Matrix
	 */
	
	private Matrix3 getCrossCovarianceMatrix(IPointCloud pointCloudBase, IPointCloud ClosestPoints, Point upCloudBase, Point uxCloudRegister){
		
		if(pointCloudBase.getNumberOfPoints()!=ClosestPoints.getNumberOfPoints())
			return null;

		Matrix3 covariance = new Matrix3();
		
		for(int i = 0; i < pointCloudBase.getNumberOfPoints(); i++){
			
			for(int k =0 ; k < 3 ; k++){
				for (int j = 0; j < 3; j++){
					
					covariance.set(k, j, (covariance.get(k, j) + (pointCloudBase.getPoint(i).getPosition().get(k) * ClosestPoints.getPoint(i).getPosition().get(j))));
					
				}
				
			}	
		}
		
		for(int k =0 ; k < 3 ; k++){
			for (int j = 0; j < 3; j++){
					
				covariance.set(k, j, (covariance.get(k, j) / pointCloudBase.getNumberOfPoints()));
					
			}
		}		
		for(int u =0 ; u < 3 ; u++){
			for (int j = 0; j < 3; j++){
					
				covariance.set(u, j, (covariance.get(u, j) - (upCloudBase.getPosition().get(u) * uxCloudRegister.getPosition().get(j))));
			}
		}
	return covariance;
		
	}
	
	/*
	 * Calculate the 4x4 Matrix Q 
	 */
	
	private Matrix4 getQ(Matrix3 covariance){
		
		System.out.println("Covariance: \n"+ covariance.toString());
		System.out.println("\n");
		
		
		Matrix4 q = new Matrix4();
		
		q.set(0, 0, getTrace(covariance));
		
		//Coveriance Matrix - Covariance Matrix transponiert, ergibt den Vector Delta, um Q zu bilden.
		
		q.set(1, 0, (covariance.get(1, 2) - covariance.get(2, 1)));
		q.set(2, 0, (covariance.get(2, 0) - covariance.get(0, 2)));
		q.set(3, 0, (covariance.get(0, 1) - covariance.get(1, 0)));
		
		//Transponiert Delta
		q.set(0, 1, (q.get(1, 0)));
		q.set(0, 2, (q.get(2, 0)));
		q.set(0, 3, (q.get(3, 0)));
		
		//Rest von Q auffüllen
		
		for(int i =0; i < 3; i++)
		{
			for(int j =0; j < 3; j++)
			{
				q.set((1+i), (1+j), (covariance.get(i, j) + covariance.get(j, i) - (i==j?q.get(0, 0):0)));
			}
		}
			
		return q;
	}
	
	/*
	 * calculate the Trace of the Matrix
	 */
	
	private double getTrace(Matrix3 covariance)
	{
		double trace = 0;
		for(int i =0; i < 3; i++)
		{
			trace += covariance.get(i, i);
		}

		return trace;
	}
	
	private Vector4 getMaxEigenVector(Matrix4 q){
		
		System.out.println("Q: \n"+ q.toString());
		System.out.println("\n");
		
		double[][] temp = new double[4][4];
		
		for(int i =0; i < 4; i++)
		{
			for(int j =0; j < 4; j++)
			{
				temp[i][j] = q.get(i, j);
			}
		}
		
		Matrix m = new Matrix(temp);
		EigenvalueDecomposition evd = new EigenvalueDecomposition(m);

		double[] eigenValues = evd.getRealEigenvalues();
		double max = Double.NEGATIVE_INFINITY;
		int index = 0;
		Vector4 eigenVector = new Vector4();

		for(int i =0; i < eigenValues.length; i++)
		{
			System.out.println("EV: "+eigenValues[i]);
			System.out.println(Arrays.toString(evd.getV().transpose().getArray()[i]));
			if(eigenValues[i]>max)
			{
				max = eigenValues[i];
				index = i;
			}
		}
		
		double[] test = evd.getV().transpose().getArray()[index];
		
		for(int i = 0; i < test.length; i++){
			eigenVector.set(i, test[i]);
		}

		return eigenVector;
		
		
	}
	
	private Matrix3 getRotationMatrix(Vector4 eigenVector){
		
		Matrix3 rotationMatrix = new Matrix3();
		int q0 = 0, q1 = 1, q2 = 2 , q3 = 3;
		
		rotationMatrix.set(0, 0, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q1) * eigenVector.get(q1))
				- (eigenVector.get(q2) * eigenVector.get(q2)) - (eigenVector.get(q3) * eigenVector.get(q3))));
		
		rotationMatrix.set(0, 1, (2 * ((eigenVector.get(q1) * eigenVector.get(q2)) - (eigenVector.get(q0) * eigenVector.get(q3)))));
		rotationMatrix.set(0, 2, (2 * ((eigenVector.get(q1) * eigenVector.get(q3)) + (eigenVector.get(q0) * eigenVector.get(q2)))));
		
		rotationMatrix.set(1, 0, (2 * ((eigenVector.get(q1) * eigenVector.get(q2)) + (eigenVector.get(q0) * eigenVector.get(q3)))));
		rotationMatrix.set(1, 1, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q2) * eigenVector.get(q2))
				- (eigenVector.get(q1) * eigenVector.get(q1)) - (eigenVector.get(q3) * eigenVector.get(q3))));
		
		rotationMatrix.set(1, 2, (2 * ((eigenVector.get(q2) * eigenVector.get(q3)) - (eigenVector.get(q0) * eigenVector.get(q1)))));
		
		rotationMatrix.set(2, 0, (2 * ((eigenVector.get(q1) * eigenVector.get(q3)) - (eigenVector.get(q0) * eigenVector.get(q2)))));
		rotationMatrix.set(2, 1, (2 * ((eigenVector.get(q2) * eigenVector.get(q3)) + (eigenVector.get(q0) * eigenVector.get(q1)))));
		rotationMatrix.set(2, 2, ((eigenVector.get(q0) * eigenVector.get(q0)) + (eigenVector.get(q3) * eigenVector.get(q3))
				- (eigenVector.get(q1) * eigenVector.get(q1)) - (eigenVector.get(q2) * eigenVector.get(q2))));
		
		return rotationMatrix;
		
	}
	
	private Vector3 translationVector(Matrix3 rotation, Point upCloudBase, Point uxCloudRegister){
		
		Vector3 translation = new Vector3();
		
		translation.set(x, (uxCloudRegister.getPosition().get(x) - (rotation.get(0, 0) * upCloudBase.getPosition().get(x) + rotation.get(0, 1) 
				* upCloudBase.getPosition().get(y) + rotation.get(0, 2) * upCloudBase.getPosition().get(z))));
		
		translation.set(y, (uxCloudRegister.getPosition().get(y) - (rotation.get(1, 0) * upCloudBase.getPosition().get(x) + rotation.get(1, 1) 
				* upCloudBase.getPosition().get(y) + rotation.get(1, 2) * upCloudBase.getPosition().get(z))));
		
		translation.set(z, (uxCloudRegister.getPosition().get(z) - (rotation.get(2, 0) * upCloudBase.getPosition().get(x) + rotation.get(2, 1) 
				* upCloudBase.getPosition().get(y) + rotation.get(2, 2) * upCloudBase.getPosition().get(z))));
		
		
		return translation;
	}
	
	private IPointCloud computeRegistration(Matrix3 rotation, Vector3 translation, IPointCloud pointCloudRegister){
		Vector3 rotationBase = new Vector3();
		Point bla = new Point();
		double error;
		IPointCloud temp = new PointCloud();
		//Rotation *pi
		
		for(int i =0; i < pointCloudRegister.getNumberOfPoints(); i++){
		rotationBase.set(x, (rotation.get(0, 0) * pointCloudRegister.getPoint(i).getPosition().get(x) + 
				rotation.get(0, 1) * pointCloudRegister.getPoint(0).getPosition().get(y) + rotation.get(0, 2) * pointCloudRegister.getPoint(0).getPosition().get(z)));
		rotationBase.set(y, (rotation.get(1, 0) * pointCloudRegister.getPoint(i).getPosition().get(x) + 
				rotation.get(1, 1) * pointCloudRegister.getPoint(0).getPosition().get(y) + rotation.get(1, 2) * pointCloudRegister.getPoint(0).getPosition().get(z)));
		rotationBase.set(z, (rotation.get(2, 0) * pointCloudRegister.getPoint(i).getPosition().get(x) + 
				rotation.get(2, 1) * pointCloudRegister.getPoint(0).getPosition().get(y) + rotation.get(2, 2) * pointCloudRegister.getPoint(0).getPosition().get(z)));
		
		//(R*pi)+t
//		rotationBase.add(translation);
		rotationBase.set(x, (rotationBase.get(x) + translation.get(x)));
		rotationBase.set(y, (rotationBase.get(y) + translation.get(y)));
		rotationBase.set(z, (rotationBase.get(z) + translation.get(z)));
		//(R*pi)+T-pj
		rotationBase.set(x, (rotationBase.get(x) - pointCloudBase.getPoint(i).getPosition().get(x)));
		rotationBase.set(y, (rotationBase.get(y) - pointCloudBase.getPoint(i).getPosition().get(y)));
		rotationBase.set(z, (rotationBase.get(z) - pointCloudBase.getPoint(i).getPosition().get(z)));
		
		//((R*pi)+T-pj)²
//		rotationBase.multiply(rotationBase);
		rotationBase.set(x, (rotationBase.get(x) * rotationBase.get(x)));
		rotationBase.set(y, (rotationBase.get(y) * rotationBase.get(y)));
		rotationBase.set(z, (rotationBase.get(z) * rotationBase.get(z)));
		System.out.println("I: "+i);
		bla.getPosition().set(x, rotationBase.get(x));
		bla.getPosition().set(y, rotationBase.get(y));
		bla.getPosition().set(z, rotationBase.get(z));
		error = rotationBase.get(x) + rotationBase.get(y) + rotationBase.get(z);
		//return error;
		System.out.println("error: \n"+ error);
		temp.addPoint(bla);
		}
		
		return temp;
	}
	
	
}




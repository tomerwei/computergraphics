package cgresearch.studentprojects.registration;

import java.util.Arrays;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Matrix3;
import cgresearch.core.math.Matrix4;
import cgresearch.core.math.Vector3;
import cgresearch.core.math.Vector4;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.core.math.jama.EigenvalueDecomposition;
import cgresearch.core.math.jama.Matrix;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

public class Minimization {
	
	int x = 0,y = 1,z = 2;
	
	public Minimization(){
		
	}
	
	public IPointCloud calculate(IPointCloud Register, IPointCloud closestPoints, int k){
		//umbauen hier nur methoden, die aus der REstriationButton aufgerufen werdedn. REgistrationButton wird die Hauptklasse.
		
		
//		Point upCloudRegister = centerOfMassyBase(Register);
//		Point uxCloudClosest = centerOfMassyRegister(closestPoints);
		
//		Matrix3 covarianceMatrix = this.getCrossCovarianceMatrix(Register, closestPoints, upCloudRegister, uxCloudClosest);
//		Matrix4 q = this.getQ(covarianceMatrix);
//		Vector4 eigenVector = getMaxEigenVector(q);
//		Matrix3 rotationMatrix = getRotationMatrix(eigenVector);
//		Vector3 translationVector = translationVector(rotationMatrix, upCloudRegister, uxCloudClosest);
//		
//		Register = computeNewPointCloud(Register, rotationMatrix, translationVector);
//		double dk = computeDk(rotationMatrix, translationVector, Register, closestPoints);
//		this.print(dk, eigenVector, translationVector, k);
//		
		
		
		
//		System.out.println("\n");
//		System.out.println("eigenVector: "+ eigenVector.toString());
//		System.out.println("\n");
//		System.out.println("RotationsMatrix: \n"+ rotationMatrix.toString());
//		System.out.println("\n");
//		System.out.println("TranslationVector: \n"+ translationVector.toString());
//		Register = computeRegistration(rotationMatrix, translationVector, Register);
		
		return Register;
		
	}
	
	
public Point centerOfMassyBase(IPointCloud ipc){
		
		
	
		IVector3 newPoint = VectorMatrixFactory.newIVector3(0,0,0);
		
				
		for(int i = 0; i < ipc.getNumberOfPoints(); i++)
		{
			newPoint.set(x, (newPoint.get(x) + ipc.getPoint(i).getPosition().get(x)));
			newPoint.set(y, (newPoint.get(y) + ipc.getPoint(i).getPosition().get(y)));
			newPoint.set(z, (newPoint.get(z) + ipc.getPoint(i).getPosition().get(z)));
//			System.out.println("i centerofmass "+ i);
		}
		
		
		newPoint.set(x, (newPoint.get(x) / ipc.getNumberOfPoints()));
		newPoint.set(y, (newPoint.get(y) / ipc.getNumberOfPoints()));
		newPoint.set(z, (newPoint.get(z) / ipc.getNumberOfPoints()));
		return new Point(newPoint);
		
	}

public Point centerOfMassyRegister(IPointCloud ipc, int[] next){
	
	
	
	IVector3 newPoint = VectorMatrixFactory.newIVector3(0,0,0);
	
			
	for(int i = 0; i < ipc.getNumberOfPoints(); i++)
	{
		newPoint.set(x, (newPoint.get(x) + ipc.getPoint(i).getPosition().get(x)));
		newPoint.set(y, (newPoint.get(y) + ipc.getPoint(i).getPosition().get(y)));
		newPoint.set(z, (newPoint.get(z) + ipc.getPoint(i).getPosition().get(z)));
//		System.out.println("i centerofmass "+ i);
	}
	
	
	newPoint.set(x, (newPoint.get(x) / ipc.getNumberOfPoints()));
	newPoint.set(y, (newPoint.get(y) / ipc.getNumberOfPoints()));
	newPoint.set(z, (newPoint.get(z) / ipc.getNumberOfPoints()));
	return new Point(newPoint);
	
}

	
	
	/**
	 * Calculate the cross Covariance Matrix
	 */
	
	private Matrix3 getCrossCovarianceMatrix(IPointCloud Register, IPointCloud ClosestPoints, Point upCloudRegister, Point uxCloudClosest){
		
		if(Register.getNumberOfPoints()!=ClosestPoints.getNumberOfPoints())
			return null;

		Matrix3 covariance = new Matrix3();
		
		for(int i = 0; i < Register.getNumberOfPoints(); i++){
			
			for(int k =0 ; k < 3 ; k++){
				for (int j = 0; j < 3; j++){
					
					covariance.set(k, j, (covariance.get(k, j) + (Register.getPoint(i).getPosition().get(k) * ClosestPoints.getPoint(i).getPosition().get(j))));
					
				}
				
			}	
		}
		
		for(int k =0 ; k < 3 ; k++){
			for (int j = 0; j < 3; j++){
					
				covariance.set(k, j, (covariance.get(k, j) / Register.getNumberOfPoints()));
					
			}
		}		
		for(int u =0 ; u < 3 ; u++){
			for (int j = 0; j < 3; j++){
					
				covariance.set(u, j, (covariance.get(u, j) - (upCloudRegister.getPosition().get(u) * uxCloudClosest.getPosition().get(j))));
			}
		}
	return covariance;
		
	}
	
	/*
	 * Calculate the 4x4 Matrix Q 
	 */
	
	private Matrix4 getQ(Matrix3 covariance){
		
//		System.out.println("Covariance: \n"+ covariance.toString());
//		System.out.println("\n");
		
		
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
		
//		System.out.println("Q: \n"+ q.toString());
//		System.out.println("\n");
		
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
//			System.out.println("EV: "+eigenValues[i]);
//			System.out.println(Arrays.toString(evd.getV().transpose().getArray()[i]));
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
	
	private Vector3 translationVector(Matrix3 rotation, Point upCloudRegister, Point uxCloudClosest){
		
		Vector3 translation = new Vector3();
		
		translation.set(x, (uxCloudClosest.getPosition().get(x) - (rotation.get(0, 0) * upCloudRegister.getPosition().get(x) + rotation.get(0, 1) 
				* upCloudRegister.getPosition().get(y) + rotation.get(0, 2) * upCloudRegister.getPosition().get(z))));
		
		translation.set(y, (uxCloudClosest.getPosition().get(y) - (rotation.get(1, 0) * upCloudRegister.getPosition().get(x) + rotation.get(1, 1) 
				* upCloudRegister.getPosition().get(y) + rotation.get(1, 2) * upCloudRegister.getPosition().get(z))));
		
		translation.set(z, (uxCloudClosest.getPosition().get(z) - (rotation.get(2, 0) * upCloudRegister.getPosition().get(x) + rotation.get(2, 1) 
				* upCloudRegister.getPosition().get(y) + rotation.get(2, 2) * upCloudRegister.getPosition().get(z))));
		
		
		return translation;
	}
	
	private IPointCloud computeNewPointCloud(IPointCloud Register, Matrix3 rotationMatrix, Vector3 translationVector){
		IPointCloud newPoints = new PointCloud();
		double x1,y1,z1;
		
		Point p = new Point();
		// Pk+1 = Pk * R
			for(int i =0; i < Register.getNumberOfPoints(); i++){
			x1 = 0;
			y1 = 0;
			z1 = 0;

			x1 = ((rotationMatrix.get(0, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(0, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotationMatrix.get(0, 2) * Register.getPoint(i).getPosition().get(z))));
			y1 = ((rotationMatrix.get(1, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(1, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotationMatrix.get(1, 2) * Register.getPoint(i).getPosition().get(z))));
			z1 = ((rotationMatrix.get(2, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(2, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotationMatrix.get(2, 2) * Register.getPoint(i).getPosition().get(z))));
			IVector3 position = VectorMatrixFactory.newIVector3(x1,y1,z1);
			newPoints.addPoint(new Point(position));
			
			
//				newPoints..getPoint(i).getPosition().set(x, ((rotationMatrix.get(0, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(0, 1) * Register.getPoint(i).getPosition().get(y) + 
//						(rotationMatrix.get(0, 2) * Register.getPoint(i).getPosition().get(z)))));
//				
//				newPoints.getPoint(i).getPosition().set(y, ((rotationMatrix.get(1, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(1, 1) * Register.getPoint(i).getPosition().get(y) + 
//						(rotationMatrix.get(1, 2) * Register.getPoint(i).getPosition().get(z)))));
//				
//				newPoints.getPoint(i).getPosition().set(x, ((rotationMatrix.get(2, 0) * Register.getPoint(i).getPosition().get(x)) + (rotationMatrix.get(2, 1) * Register.getPoint(i).getPosition().get(y) + 
//						(rotationMatrix.get(2, 2) * Register.getPoint(i).getPosition().get(z)))));
				
				newPoints.getPoint(i).getPosition().set(x, (newPoints.getPoint(i).getPosition().get(x) + translationVector.get(x)) );
				newPoints.getPoint(i).getPosition().set(y, (newPoints.getPoint(i).getPosition().get(y) + translationVector.get(y)) );
				newPoints.getPoint(i).getPosition().set(z, (newPoints.getPoint(i).getPosition().get(z) + translationVector.get(z)) );
			
		}
		//Pk+1 = Pk * R + t
				
			
		
		
		return newPoints;
	}
	
	private double computeDk(Matrix3 rotation, Vector3 translation, IPointCloud Register, IPointCloud closestPoints){
//		Vector3 rotationBase = new Vector3();
//		Point bla = new Point();
		double dk = 0;
		double error = 0;
		IPointCloud temp = new PointCloud();
		IPointCloud temp2 = new PointCloud();
		double x1,y1,z1,x2,y2,z2;
		//Rotation *pi
		
		
		//temp anstatt Register
		for(int i =0; i < Register.getNumberOfPoints(); i++){
			
			x1 = 0;
			y1 = 0;
			z1 = 0;
			
			x1 = ((rotation.get(0, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(0, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotation.get(0, 2) * Register.getPoint(i).getPosition().get(z))));
			y1 = ((rotation.get(1, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(1, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotation.get(1, 2) * Register.getPoint(i).getPosition().get(z))));
			z1 = ((rotation.get(2, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(2, 1) * Register.getPoint(i).getPosition().get(y) + 
					(rotation.get(2, 2) * Register.getPoint(i).getPosition().get(z))));
			
			IVector3 position = VectorMatrixFactory.newIVector3(x1,y1,z1);
			temp.addPoint(new Point(position));
			
//			Register.getPoint(i).getPosition().set(x, ((rotation.get(0, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(0, 1) * Register.getPoint(i).getPosition().get(y) + 
//					(rotation.get(0, 2) * Register.getPoint(i).getPosition().get(z)))));
//			
//			Register.getPoint(i).getPosition().set(y, ((rotation.get(1, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(1, 1) * Register.getPoint(i).getPosition().get(y) + 
//					(rotation.get(1, 2) * Register.getPoint(i).getPosition().get(z)))));
//			
//			Register.getPoint(i).getPosition().set(z, ((rotation.get(2, 0) * Register.getPoint(i).getPosition().get(x)) + (rotation.get(2, 1) * Register.getPoint(i).getPosition().get(y) + 
//					(rotation.get(2, 2) * Register.getPoint(i).getPosition().get(z)))));
			
		//(R*pi)-t temp anstatt Register
//		rotationBase.add(translation);
		
		temp.getPoint(i).getPosition().set(x, (temp.getPoint(i).getPosition().get(x) - translation.get(x)) );
		temp.getPoint(i).getPosition().set(y, (temp.getPoint(i).getPosition().get(y) - translation.get(y)) );
		temp.getPoint(i).getPosition().set(z, (temp.getPoint(i).getPosition().get(z) - translation.get(z)) );
		
//		rotationBase.set(x, (rotationBase.get(x) + translation.get(x)));
//		rotationBase.set(y, (rotationBase.get(y) + translation.get(y)));
//		rotationBase.set(z, (rotationBase.get(z) + translation.get(z)));
		//(R*pi)+T-pj
		//temp2 anstatt Register
		
		x2 = 0;
		y2 = 0;
		z2 = 0;

		x2 = (closestPoints.getPoint(i).getPosition().get(x) - temp.getPoint(i).getPosition().get(x));
		y2 = (closestPoints.getPoint(i).getPosition().get(y) - temp.getPoint(i).getPosition().get(y));
		z2 = (closestPoints.getPoint(i).getPosition().get(z) - temp.getPoint(i).getPosition().get(z));
		
		IVector3 position2 = VectorMatrixFactory.newIVector3(x2,y2,z2);
		temp2.addPoint(new Point(position2));
		

//		temp2.getPoint(i).getPosition().set(x, (closestPoints.getPoint(i).getPosition().get(x) - temp.getPoint(i).getPosition().get(x)));
//		temp2.getPoint(i).getPosition().set(y, (closestPoints.getPoint(i).getPosition().get(y) - temp.getPoint(i).getPosition().get(y)));
//		temp2.getPoint(i).getPosition().set(z, (closestPoints.getPoint(i).getPosition().get(z) - temp.getPoint(i).getPosition().get(z)));
		
		error = error + (temp2.getPoint(i).getPosition().get(x) * temp2.getPoint(i).getPosition().get(x) + temp2.getPoint(i).getPosition().get(y) * temp2.getPoint(i).getPosition().get(y)
				+ temp2.getPoint(i).getPosition().get(z) * temp2.getPoint(i).getPosition().get(z));
		
		}
		
		dk = error / Register.getNumberOfPoints();
		
		return dk;
		
		
		
//		pointCloudRegister.getPoint(i).getPosition().set(x, (pointCloudRegister.getPoint(i).getPosition().get(x) - pointCloudBase.getPoint(i).getPosition().get(x)) );
//		pointCloudRegister.getPoint(i).getPosition().set(y, (pointCloudRegister.getPoint(i).getPosition().get(y) - pointCloudBase.getPoint(i).getPosition().get(y)) );
//		pointCloudRegister.getPoint(i).getPosition().set(z, (pointCloudRegister.getPoint(i).getPosition().get(z) - pointCloudBase.getPoint(i).getPosition().get(z)) );
		
//		rotationBase.set(x, (rotationBase.get(x) - pointCloudBase.getPoint(i).getPosition().get(x)));
//		rotationBase.set(y, (rotationBase.get(y) - pointCloudBase.getPoint(i).getPosition().get(y)));
//		rotationBase.set(z, (rotationBase.get(z) - pointCloudBase.getPoint(i).getPosition().get(z)));
		
		//((R*pi)+T-pj)²
//		rotationBase.multiply(rotationBase);
		
//		pointCloudRegister.getPoint(i).getPosition().set(x, (pointCloudRegister.getPoint(i).getPosition().get(x) - pointCloudBase.getPoint(i).getPosition().get(x)) );
//		pointCloudRegister.getPoint(i).getPosition().set(y, (pointCloudRegister.getPoint(i).getPosition().get(y) - pointCloudBase.getPoint(i).getPosition().get(y)) );
//		pointCloudRegister.getPoint(i).getPosition().set(z, (pointCloudRegister.getPoint(i).getPosition().get(z) - pointCloudBase.getPoint(i).getPosition().get(z)) );
//		
//		pointCloudRegister.getPoint(i).getPosition().cross(pointCloudRegister.getPoint(i).getPosition());
		
//		rotationBase.set(x, (rotationBase.get(x) * rotationBase.get(x)));
//		rotationBase.set(y, (rotationBase.get(y) * rotationBase.get(y)));
//		rotationBase.set(z, (rotationBase.get(z) * rotationBase.get(z)));
		
//		System.out.println("I: "+i);
//		bla.getPosition().set(x, rotationBase.get(x));
//		bla.getPosition().set(y, rotationBase.get(y));
//		bla.getPosition().set(z, rotationBase.get(z));
//		pointCloudRegister.getPoint(0).getPosition().set(0, 1);
//		pointCloudRegister.getPoint(0).getPosition().set(1, 4);
//		pointCloudRegister.getPoint(0).getPosition().set(2, 1);
//		
//		pointCloudRegister.getPoint(1).getPosition().set(0, 2);
//		pointCloudRegister.getPoint(1).getPosition().set(1, 5);
//		pointCloudRegister.getPoint(1).getPosition().set(2, 2);
//		
//		pointCloudRegister.getPoint(2).getPosition().set(0, 3);
//		pointCloudRegister.getPoint(2).getPosition().set(1, 6);
//		pointCloudRegister.getPoint(2).getPosition().set(2, 3);
//		System.out.println("Drauf geschissen!!!!!!!!!!!!!!!!!!!");
//		for(int i=0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
//			System.out.println("newPiontCloud mittendrinnen: "+pointCloudRegister.getPoint(i).getPosition());
//		}
//		error = pointCloudRegister.getPoint(i).getPosition().get(x) + pointCloudRegister.getPoint(i).getPosition().get(y) +pointCloudRegister.getPoint(i).getPosition().get(z);
//		error = rotationBase.get(x) + rotationBase.get(y) + rotationBase.get(z);
		//return error;
//		System.out.println("error: \n"+ error);
//		System.out.println("NewPointCloudRegister: "+i+" "+pointCloudRegister.getPoint(i).getPosition());
		
//		}
//		pointCloudRegister.updateRenderStructures();
		
//		return pointCloudRegister;
	}
	
	private void print(double dk, Vector4 eigenVector, Vector3 translation, int k){
		
		Logger.getInstance().message("\n"+k+"\t"+eigenVector+""+translation+"\t"+dk);
		
		
	}
	

}

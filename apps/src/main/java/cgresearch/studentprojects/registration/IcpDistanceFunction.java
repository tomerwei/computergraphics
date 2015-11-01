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
	
	private IPointCloud closestPoints;
	
	
	
	
	
	
	public IcpDistanceFunction(IPointCloud pointCloudBase) {
		this.pointCloudBase = pointCloudBase;


	}
	
	
	public IPointCloud startAlgorithm(IPointCloud Register, int iterationSteps){
		
		Minimization min = new Minimization();
		
		Logger.getInstance().message("\nRegistration output:");
		Logger.getInstance().message("\nk\tq\tdk");
		
		for(int k = 0; k < iterationSteps; k++){
			closestPoints = NormalDistance.getClosestPoints(pointCloudBase, Register);
			Register = min.calculate(Register, closestPoints, k);
			Register.updateRenderStructures();
		
		}
		
		return Register;
		
	}
	
	/*
	 * Get the closestPoints between two Pointclouds
	 */
	
	
	
	/*
	 * Center of Mass for the PointCLoud
	 */
	
	
	
	
}




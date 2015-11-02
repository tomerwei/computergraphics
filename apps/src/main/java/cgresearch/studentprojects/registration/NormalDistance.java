package cgresearch.studentprojects.registration;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

public class NormalDistance {
	
	public NormalDistance(){
		
	}
	
public static IPointCloud getClosestPoints(IPointCloud Model, IPointCloud Register ){
		
		Point[] tmp = new Point[Model.getNumberOfPoints()];
		IPointCloud ClosestPoints = new PointCloud();
		
		for(int i =0 ; i < Register.getNumberOfPoints();){
			Point closest = null;
			double dist = Double.POSITIVE_INFINITY;

			for(int k = 0; k < Model.getNumberOfPoints(); k++)
			{
//				System.out.println("i Model: "+i+" "+Model.getPoint(i).getPosition());
//				System.out.println("k Register: "+i+" "+Register.getPoint(k).getPosition());
				double distance = getDistance(Model.getPoint(i), Register.getPoint(k));
//				System.out.println("Distance: "+distance);
//				System.out.println("Dist: "+dist);
				if(distance < dist)
				{
					closest = Register.getPoint(k);
					
					dist = distance;
				}
			}
			ClosestPoints.addPoint(closest);
//			System.out.println("i ClosestPoints: "+i+" "+ClosestPoints.getPoint(i).getPosition());
			
			
			i++;
		}
		for(int j = 0; j <3; j++){
//			System.out.println("ClosestsPoint: " + ClosestPoints.getPoint(j).getPosition());
		}
		
		return ClosestPoints;
	}
	
	
	private static double getDistance(Point a, Point b){
		
		return Math.sqrt(Math.pow(b.getPosition().get(0) - a.getPosition().get(0),2) + Math.pow(b.getPosition().get(1) - a.getPosition().get(1),2) + Math.pow(b.getPosition().get(2) - a.getPosition().get(2),2));
		
		
	}

}

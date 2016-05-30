package cgresearch.studentprojects.registration;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;

public class NearestPointWithKdTree {
	
	public NearestPointWithKdTree(){
		
	}
	
	public int[] nearestPoints(IPointCloud base, IPointCloud register) {

	    int[] nearestPoints = new int[base.getNumberOfPoints()];
	    PointNeighborsQuery nearest = new PointNeighborsQuery(register);

	    for (int k = 0; k < base.getNumberOfPoints(); k++) {

	      nearest.queryKnn(base.getPoint(k).getPosition(), 1);
	      

	      nearestPoints[k] = nearest.getNeigbor(0);
	   
//	      System.out.println("Platz in der RegisterCloud:" + nearestPoints[k]);

//	      System.out.println("Punkt: " + Register.getPoint(nearestPoints[k]).getPosition());

	    }
	    return nearestPoints;
	  }

}

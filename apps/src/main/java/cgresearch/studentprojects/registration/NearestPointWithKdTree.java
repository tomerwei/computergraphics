package cgresearch.studentprojects.registration;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;

public class NearestPointWithKdTree {
	
	PointNeighborsQuery nearest;
	
	public NearestPointWithKdTree(IPointCloud base){
		this.nearest = new PointNeighborsQuery(base);
	}
	
	public int nearestPoints(Point register) {

	    int nearestPoints = 0;// = new int[register.getNumberOfPoints()];

//	    for (int k = 0; k < register.getNumberOfPoints(); k++) {

	      nearest.queryKnn(register.getPosition(), 1);
//	      System.out.println("dichtester gefundener Punkt und in nearest gepackt: "+nearest.getNeigbor(0));
	      
	      nearestPoints = nearest.getNeigbor(0);
	      
	   
//	      System.out.println("Platz in der RegisterCloud:" + nearestPoints[k]);

//	      System.out.println("Punkt: " + Register.getPoint(nearestPoints[k]).getPosition());

//	    }
	    return nearestPoints;
	  }

}

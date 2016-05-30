package cgresearch.studentprojects.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloud;


public class trimmedICP {
	
	ArrayList<TrimmedDistance> list;
	double sLTS = 0;
	double npo = 0;
	double alpha = 0.4;
	double beta = 1;
	private IPointCloud register = new PointCloud();
	double xi = alpha;
	

	
	public trimmedICP(IPointCloud register, double slts){
		this.sLTS = slts;
		this.register = register;
	}
	
	public IPointCloud startAlgorithm(IPointCloud base, boolean findPercent, int percent){
		
		double tolerance = 0.01;
		
		
		//for/while und if anweisung für Stoppkonditionen und Slts = s´lts setzen
		//Stoppkonditionen: max Niter, e ist klein genug  die änderung von e ist klein genug
		
		NearestPointWithKdTree nearest = new NearestPointWithKdTree();
	    int[] nearestPoints = nearest.nearestPoints(base, register);
//	    for(int i =0; i < nearestPoints.length; i++){
////	    System.out.println("nearestPoints: "+nearestPoints[i]);
//	    }
	   
	   if(findPercent == true){//automatic percentage overlap
		   while((beta - alpha) > tolerance){
			   
			   npo = xi * register.getNumberOfPoints();
			   System.out.println("NPO: "+npo);
			   double psi = goldenSectionSearch();
			   //psi noch in prozent umrechen damit xi prozent zahl bekommt. Alpha oder Beta muss das sein
			   xi = psi;
			   
			   System.out.println("xi = psi : "+xi);
			   this.computeIndividualDistance(nearestPoints, base, register);
			   sLTS = this.sortListInAscendingOrder();
			   
		   }
		    
	   }else{//manually percentage overlap
		   xi = percent;
		   System.out.println(" falsche SChleife");
	   }
	    
	    
	    //muss nach der goldensearch kommen
	    
	   
	    
		
		return register;
	}
	
	private void computeIndividualDistance(int[] nearestPoints, IPointCloud base, IPointCloud register){
		
		list  = new ArrayList<TrimmedDistance>(nearestPoints.length);
		
		for(int i=0; i < base.getNumberOfPoints(); i++){
			
			double x,y,z = 0;
			
			x = (base.getPoint(i).getPosition().get(0) - register.getPoint(nearestPoints[i]).getPosition().get(0));
			y = (base.getPoint(i).getPosition().get(1) - register.getPoint(nearestPoints[i]).getPosition().get(1));
			z = (base.getPoint(i).getPosition().get(2) - register.getPoint(nearestPoints[i]).getPosition().get(2));
			double temp ;
			temp = x*x + y*y + z*z;
//			System.out.println(" berechnung: "+Math.sqrt(temp));
			list.add(new TrimmedDistance(Math.sqrt(temp),nearestPoints[i]));
//			System.out.println("liste : "+list.get(i));
			
		}
	}
	
	private double sortListInAscendingOrder(){
		
		double sltsNew = 0;
		
		
		 Collections.sort(list, new Comparator<TrimmedDistance>() {
             @Override
             public int compare(TrimmedDistance p1, TrimmedDistance p2) {
                 return  Double.compare(p1.getDistance(), p2.getDistance());
             }
		  });
		 
		 for(int i = 0; i < npo; i++){
//			 System.out.println("einzelene Distancen: "+list.size());
			 sltsNew = sltsNew + list.get(i).getDistance() * list.get(i).getDistance(); 
			 
		 }
		 
		 
		  return sltsNew;
		  
		
	}
	
	//prozentuale Punkte von register auswählen und ihre Summer der LTS berechnen
	
	private double computePsi(double xi){
		double psi = 0;
		int lamda = 3;
		double MSE = 0;
		
		System.out.println("slts: "+sLTS);
		MSE = sLTS / npo;
	
		psi = MSE / Math.pow(xi, lamda);

		
//		System.out.println("psi in computePSI: " +psi);
		return psi;
		
		
	}
	
	private double goldenSectionSearch(){
		
		double xi1 = 0;
		double xi2 = 0;
		double w = 0;
		double psiAlpha = 0;
		double psiX1 = 0;
		double psiX2 = 0;
		double psiBeta =0;
		
		w = (3 - Math.sqrt(5))/2;
		System.out.println("alpha: "+alpha);
		System.out.println("beta: "+beta);
		xi1 = alpha + w * (beta - alpha);
		xi2 = beta - w * (beta - alpha);
		System.out.println("xi1 :"+xi1);
		System.out.println("xi2 :"+xi2);
		psiX1 = computePsi(xi1);
		psiX2 = computePsi(xi2);
		System.out.println("psiX1 :"+psiX1);
		System.out.println("psiX2 :"+psiX2);
		
//		if( psiX1 < psiX2){
//			
//			beta = xi2;
//		}else
//			alpha = xi1;
//		System.out.println("alpha: "+alpha);
//		System.out.println("beta: "+beta);
//		
		psiAlpha = computePsi(alpha);
		psiX1 = computePsi(xi1);
		
		System.out.println("psiAlpha :"+psiAlpha);
		System.out.println("psiX1 :"+psiX1);
		
		
//		
		return alpha;
	}
	
	//berechnung der rotation und translation aus der Klasse ICPDistanceFunction und die anwedung ebenfalls in der ICP klasse.
	
	//berechnung von e überlappung

}

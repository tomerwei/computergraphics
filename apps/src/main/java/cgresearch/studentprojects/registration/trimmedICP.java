package cgresearch.studentprojects.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloud;


public class trimmedICP {

	ArrayList<TrimmedDistance> list;

	double sLTS = 0;

	double alpha = 0.4;
	double beta = 1;
	private IPointCloud register = new PointCloud();
	//	double xi = 0;
	double npo = 0;
	double MSE = 0;
	double dk = 0;
	double sLTSTest = 0;
	double percent = 0;

	public trimmedICP(IPointCloud register, double slts){
		this.sLTS = slts;
		this.register = register;
	}

	public IPointCloud startAlgorithm(IPointCloud base, boolean findPercent, double percent){

		double tolerance = 0.01;
		double psi[][] = new double[60][4];
		this.percent = percent;

		//for/while und if anweisung für Stoppkonditionen und Slts = s´lts setzen
		//Stoppkonditionen: max Niter, e ist klein genug  die änderung von e ist klein genug

		
//		System.out.println("Arraygröße dichteste Punkte: "+nearestPoints.length);
		//	    for(int i =0; i < nearestPoints.length; i++){
		////	    System.out.println("nearestPoints: "+nearestPoints[i]);
		//	    }

		if(findPercent == true){//automatic percentage overlap
			this.computeIndividualDistance(base, register);
			System.out.println("Größe der Liste mit den dichtesten Punkten: "+list.size());
			this.sortListInAscendingOrder();
//			for(int i = 0; i < list.size(); i++){
//				System.out.println("Liste geordnet Distance: "+list.get(i).getDistance()+", Point of Base: "+ base.getPoint(list.get(i).indexForBase).getPosition()+ ", Points register: "+ register.getPoint(list.get(i).indexForRegister).getPosition());
//			}

			while((beta - alpha) > tolerance){

				//			   npo = xi * register.getNumberOfPoints();
				//			   System.out.println("NPO: "+npo);
				//			   double psi = goldenSectionSearch();
				//			   xi = alpha;
				goldenSectionSearch();
			}

			//			   while(xi <= 1 ){

			psi = testSearch();
			for(int i =0 ; i < 60; i++){
				//					   
				System.out.println("xi: "+psi[i][0]+ " psi: "+psi[i][1]+ " MSE: "+psi[i][2]+ " slts: "+psi[i][3]+ " Npo: "+psi[i][4]);
				//				   xi = xi + 0.05;
			}
			//			   register = this.computeTranslation(base, register);

			//psi noch in prozent umrechen damit xi prozent zahl bekommt. Alpha oder Beta muss das sein
			//			   xi = psi;

			//			   System.out.println("xi = psi : "+xi);
//			register = computeTranslation(base, register);
			computeTranslation(base, register);

			//		   }

		}else{//manually percentage overlap
			//		   alpha = percent;
					   this.computeIndividualDistance(base, register);
//					   this.sortListInAscendingOrder();
//					   register = computeTranslation(base, register);
			//		   System.out.println(" falsche SChleife");
		}


		//muss nach der goldensearch kommen




		return register;
	}
	
	/**
	 * calculate the distance for the nearest Points and save the result in a list
	 * 
	 * @param nearestPoints
	 * @param base
	 * @param register
	 */

	private void computeIndividualDistance(IPointCloud base, IPointCloud register){
		NearestPointWithKdTree nearest = new NearestPointWithKdTree(base);
		int nearestPoints = 0;
		list  = new ArrayList<TrimmedDistance>();
		double x,y,z = 0;
		double temp ;
		boolean exist = false;
		
		for(int i =0 ; i < register.getNumberOfPoints(); i++){
			nearestPoints = nearest.nearestPoints(register.getPoint(i));
			

			for(int k=0; k < list.size(); k++){
				if((nearestPoints) == list.get(k).getIndexForBase()){
					//					System.out.println(" Punkt aus der Liste entfernen: "+list.get(k));
					exist = true;
					break;

				}
			}
			if(exist == false){
				
				x = ((base.getPoint(nearestPoints).getPosition().get(0)) - register.getPoint(i).getPosition().get(0));
				y = ((base.getPoint(nearestPoints).getPosition().get(1)) - register.getPoint(i).getPosition().get(1));
				z = ((base.getPoint(nearestPoints).getPosition().get(2)) - register.getPoint(i).getPosition().get(2));

				temp = x*x + y*y + z*z;
				//				System.out.println(" Adde Punkte mit dem Index: "+nearestPoints[i]);

				list.add(new TrimmedDistance((Math.sqrt(temp)),nearestPoints, i));
				

			}else{
				exist = false;
			}
			
		}
		
		
//		boolean exist = false;
//		
//		for(int k = 0; k <base.getNumberOfPoints(); k++){
//			System.out.println("Punkte in base: "+ base.getPoint(k).getPosition());
//		}
//		for(int p = 0;p <base.getNumberOfPoints(); p++){
//			System.out.println("Punkte in register: "+ register.getPoint(p).getPosition());
//		}
//		for(int o = 0; o <base.getNumberOfPoints(); o++){
//			System.out.println("Punkte in nearest Points: "+ nearestPoints[o]);
//
//		}
//		
//		
//		for(int i=0; i < base.getNumberOfPoints(); i++){
//						
//						
//			for(int k=0; k < list.size(); k++){
//				if((nearestPoints[i]) == list.get(k).getIndex()){
//					//					System.out.println(" Punkt aus der Liste entfernen: "+list.get(k));
//					exist = true;
//					break;
//
//				}
//			}
//
//			if(exist == false){
//				x = ((base.getPoint(i).getPosition().get(0)) - register.getPoint(nearestPoints[i]).getPosition().get(0));
//				y = ((base.getPoint(i).getPosition().get(1)) - register.getPoint(nearestPoints[i]).getPosition().get(1));
//				z = ((base.getPoint(i).getPosition().get(2)) - register.getPoint(nearestPoints[i]).getPosition().get(2));
//
//				temp = x*x + y*y + z*z;
//				//				System.out.println(" Adde Punkte mit dem Index: "+nearestPoints[i]);
//
//				list.add(new TrimmedDistance(Math.sqrt(temp),nearestPoints[i]));
//
//			}else{
//				exist = false;
//			}
//		}
	}
	
	/**
	 * sorting the list with the individual distance in ascending order
	 * 
	 */

	private void sortListInAscendingOrder(){

		//		for(int k = 0; k < list.size(); k++){
		//			
		//			
		//		}

		Collections.sort(list, new Comparator<TrimmedDistance>() {
			@Override
			public int compare(TrimmedDistance p1, TrimmedDistance p2) {
				return  Double.compare(p1.getDistance(), p2.getDistance());
			}
		});

//		System.out.println("Göße Liste: "+list.size());
//				 for(int i = 0; i < list.size(); i++){
//					 
//					 
//				 }



	}
	
	/**
	 * compute new Sts (overall distance)
	 * 
	 * @param npo
	 * @return sltsNew
	 * 
	 */

	private double computeNewSts(double npo){
		double sltsNew = 0;
		int k = 0;
		k = (int) npo;
		//		System.out.println("k: "+k);
		for(int i = 0; i < k; i++){
			sltsNew = sltsNew + list.get(i).getDistance();//(list.get(i).getDistance() * list.get(i).getDistance());
		}
		return sltsNew;
	}

	//prozentuale Punkte von register auswählen und ihre Summer der LTS berechnen
	
	/**
	 * compute the psi value
	 * 
	 * @param xi
	 * @param npo
	 * @return psi value
	 * 
	 */
	
	private double computePsi(double xi, double npo){
		double psi = 0;
		int lamda = 3;

		//		double testNpo;
		//		testNpo =  xi * list.size();
		//		testNpo = Math.round(testNpo*1)/1.0;
		//		System.out.println("testNpo: "+testNpo);
		//		sLTS = this.computeNewSts(testNpo);
		//		System.out.println("slts: "+sLTS);
//		computeMse(npo);
		sLTS = this.computeNewSts(npo);
		MSE = (sLTS / npo);
		//		System.out.println("MSE in der ComputePsi: "+MSE);

		psi = MSE / Math.pow(xi, lamda);


		//		System.out.println("psi in computePSI: " +psi);
		return psi;


	}
	
	/**
	 * compute the MSE
	 * 
	 * @param npo
	 * 
	 */

	private void computeMse(double npo){
		MSE = (sLTS / npo);
	}
	
	/**
	 * test method for searching the psi value in a range from 0.4 to 1
	 * 
	 * @return psiAll[][]
	 * 
	 */

	private double[][] testSearch(){
		double[][] psiAll = new double[60][5];
		int i = 0;
		double xi = 0.4;
		while(xi < 1){

			npo =  xi * list.size();
			npo = Math.round(npo*1)/1.0;
			//			System.out.println("npo: "+npo);

//			sLTS = this.computeNewSts(npo);

			psiAll[i][0] = xi;
			psiAll[i][1] = computePsi(xi, npo);
			psiAll[i][2] = MSE;
			psiAll[i][3] = sLTS; 
			psiAll[i][4] = npo;

			xi = xi + 0.01;
			xi = Math.round(xi*100)/100.0;

			i++;
		}

		return psiAll;

	}
	
	/**
	 * call of the ICP algorithm to calculate the best translation with the best psi value
	 * 
	 * @param base
	 * @param register
	 * @return new Pointcloud with the calculated translation
	 * 
	 */

	private IPointCloud computeTranslation(IPointCloud base, IPointCloud register){

		ArrayList<TrimmedDistance> newSortedPoints = new ArrayList();
//		double percent = 0.9 * list.size();
		percent = Math.round(percent*1)/1.0;
		
		for(int i = 0; i < percent; i++){
			newSortedPoints.add(list.get(i));
		}
		

//		IPointCloud newRegister = new PointCloud();
//		double pointsForTranslationCalculate = 0;
//		System.out.println("alpha : "+alpha);
//		pointsForTranslationCalculate = calculateNpo(alpha);
//		for(int i = 0; i < pointsForTranslationCalculate; i++){
//			//			System.out.println("newRegister: "+list.get(i).getIndex());
//			newRegister.addPoint(register.getPoint(list.get(i).getIndex()));
//		}
		
		

		IcpDistanceFunction icp = new IcpDistanceFunction();
		register = icp.startAlgorithm(base, register, newSortedPoints, 1);
		dk = icp.dk;
		return register;

	}
	
	/**
	 * calculate the new numbers of points which are included to calculate the psi value
	 * 
	 * @param xi
	 * @return npo
	 * 
	 */

	private double calculateNpo(double xi){
		double npoTest = 0;
		npoTest =  xi * list.size();
		//		System.out.println("npo ohne runden: "+npo);
		npoTest = Math.round(npoTest*1)/1.0;
		//		System.out.println("npo: "+npo);
		sLTSTest = this.computeNewSts(npoTest);
		computeMse(npoTest);
		return npoTest;
	}

	/**
	 * calculate the best psi value with the golden section serach
	 * 
	 */
	
	private void goldenSectionSearch(){

		double xi1 = 0;
		double xi2 = 0;
		double w = 0;
		double psiAlpha = 0;
		double psiX1 = 0;
		double psiX2 = 0;
		double psiBeta =0;


		//		System.out.println("npo: "+npo);

		//		
		//		
		w = (3 - Math.sqrt(5))/2;
		//		System.out.println("alpha: "+alpha);
		//		System.out.println("beta: "+beta);
		xi1 = alpha + w * (beta - alpha);
		xi2 = beta - w * (beta - alpha);
		System.out.println("xi1 :"+xi1);
		System.out.println("xi2 :"+xi2);

		psiAlpha = computePsi(alpha, calculateNpo(alpha)); 
		psiX1 = computePsi(xi1, calculateNpo(xi1));

		System.out.println("psiAlpha :"+psiAlpha);
		System.out.println("psiX1 :"+psiX1);

		//		System.out.println("alpha :"+computePsi(alpha, calculateNpo(alpha)));
		//		System.out.println("45 :"+computePsi(0.45, calculateNpo(0.45)));
		//		System.out.println("50 :"+computePsi(0.50, calculateNpo(0.50)));
		//		System.out.println("55 :"+computePsi(0.55,calculateNpo(0.55)));
		//		System.out.println("x1 :"+computePsi(0.629179606750063, calculateNpo(0.629179606750063)));
		//		System.out.println("65 :"+computePsi(0.65, calculateNpo(0.65)));
		//		System.out.println("70 :"+computePsi(0.70, calculateNpo(0.70)));
		//		System.out.println("x2 :"+computePsi(0.7708203932499369, calculateNpo(0.7708203932499369)));
		//		System.out.println("80 :"+computePsi(0.80, calculateNpo(0.80)));
		//		System.out.println("85 :"+computePsi(0.85, calculateNpo(0.85)));
		//		System.out.println("90 :"+computePsi(0.90, calculateNpo(0.90)));
		//		System.out.println("95 :"+computePsi(0.95, calculateNpo(0.95)));
		//		System.out.println("beta :"+computePsi(beta, calculateNpo(beta)));

		if( psiAlpha < psiX1){

			beta = xi1;
		}else{
			psiX2 = computePsi(xi2, calculateNpo(xi2));
			psiBeta = computePsi(beta, calculateNpo(beta)); 
			System.out.println("psiX2 :"+psiX2);
			System.out.println("beta :"+psiBeta);
			if(psiX1 < psiX2){
				alpha = xi1;
				//				beta = xi2;
			}else{
				alpha = xi2;
			}

		}

		System.out.println("alpha: "+alpha);
		System.out.println("beta: "+beta);

		//		psiAlpha = computePsi(alpha);
		//		psiX1 = computePsi(xi1);

		//		System.out.println("psiAlpha :"+psiAlpha);
		//		System.out.println("psiX1 :"+psiX1);
		//		System.out.println("alpha :"+ alpha);
		//		System.out.println("beta : "+beta);
		//		
		//		
		//		return alpha;
	}

	//berechnung der rotation und translation aus der Klasse ICPDistanceFunction und die anwedung ebenfalls in der ICP klasse.

	//berechnung von e überlappung

}

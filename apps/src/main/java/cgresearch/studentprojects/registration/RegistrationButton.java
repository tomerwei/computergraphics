package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Noise;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.ui.IApplicationControllerGui;
import javax.swing.JTextField;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;


public class RegistrationButton extends IApplicationControllerGui {

	/**
	 * ToDO: Fehlerhafte Eingabe abfangen
	 * 
	 * checkbox, ob ICP oder TRimmed ICP verwendet werden soll
	 */
	private static final long serialVersionUID = 1L;
	private String ModelPath = null;
	private String DataPath = null;
	ITriangleMesh cubeMeshModel;
	ITriangleMesh cubeMeshData;

	JTextField iterationSteps;
	private IPointCloud basePointCloud = new PointCloud();
	private IPointCloud registerPointCloud = new PointCloud();
	Surface surfaceBase;
	Surface surfaceData;
	boolean trimmed = true;


	int numberOfPointsModel = 3000;
	int numberOfPointsData = 3000;

	public RegistrationButton(CgRootNode rootNode) {

		this.surfaceBase = new Surface(rootNode);
		this.surfaceData = new Surface(rootNode);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel MainPanel = new JPanel();
		MainPanel.setPreferredSize(new Dimension(200, 100));
		MainPanel.setBounds(0, 0, 200, 264);// 264
		add(MainPanel);
		MainPanel.setLayout(null);

		JLabel lbModel = new JLabel("Model:");
		lbModel.setBounds(10, 8, 100, 14);
		MainPanel.add(lbModel);

		JLabel lbModelName = new JLabel("Model: none");
		lbModelName.setBounds(10, 60, 200, 14);
		MainPanel.add(lbModelName);

		JLabel lbNumberPoints = new JLabel("Number of Points: none");
		lbNumberPoints.setBounds(10, 76, 200, 14);
		MainPanel.add(lbNumberPoints);

		JLabel lbChangeNumber = new JLabel("Change Number of Points: ");
		lbChangeNumber.setBounds(160, 76, 200, 14);
		MainPanel.add(lbChangeNumber);

		JTextField tfChangeNumber = new JTextField();
		tfChangeNumber.setColumns(10);
		tfChangeNumber.setBounds(310, 76, 60, 20);
		MainPanel.add(tfChangeNumber);
		
		/**
		 * Change Number of Points at the Model Surface
		 */
		
		JButton btChangeGo = new JButton("Go");
		btChangeGo.setBounds(375, 76, 110, 20);
		btChangeGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numberOfPointsModel = Integer.parseInt(tfChangeNumber.getText());
				basePointCloud = TriangleMeshSampler.sample(cubeMeshModel, numberOfPointsModel);
				surfaceBase.deleteNode(basePointCloud);
				surfaceBase.create(basePointCloud);
//				basePointCloud.updateRenderStructures();
				lbNumberPoints.setText("Number of Points: " + numberOfPointsModel);
			}

		});
		MainPanel.add(btChangeGo);

		/**
		 * Load Model Surface
		 */
		
		JButton btModel = new JButton("Load Model");
		btModel.setBounds(10, 30, 110, 20);
		btModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				OpenFile open = new OpenFile();
				ModelPath = open.Open();

				ObjFileReader reader = new ObjFileReader();
				cubeMeshModel = reader.readFile(ModelPath).get(0);
				basePointCloud = TriangleMeshSampler.sample(cubeMeshModel, numberOfPointsModel);
				basePointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
				for (int i = 0; i < basePointCloud.getNumberOfPoints(); i++) {
					basePointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
				}
				surfaceBase.create(basePointCloud);
//				RegistrationFrame regFrame = new RegistrationFrame();

				lbModelName.setText("Model: " + ModelPath.toString());
				lbNumberPoints.setText("Number of Points: " + numberOfPointsModel);
			}

		});
		MainPanel.add(btModel);

		JSeparator sep = new JSeparator();
		sep.setBounds(10, 95, 1000, 1);
		MainPanel.add(sep);

		JLabel lbData = new JLabel("Data:");
		lbData.setBounds(10, 100, 100, 14);
		MainPanel.add(lbData);

		JLabel lbDataName = new JLabel("Data: none");
		lbDataName.setBounds(10, 152, 200, 14);
		MainPanel.add(lbDataName);

		JLabel lbNumberPointsData = new JLabel("Number of Points: none");
		lbNumberPointsData.setBounds(10, 168, 200, 14);
		MainPanel.add(lbNumberPointsData);

		JLabel lbChangeNumberData = new JLabel("Change Number of Points: ");
		lbChangeNumberData.setBounds(160, 168, 200, 14);
		MainPanel.add(lbChangeNumberData);

		JTextField tfChangeNumberData = new JTextField();
		tfChangeNumberData.setColumns(10);
		tfChangeNumberData.setBounds(310, 168, 60, 20);
		MainPanel.add(tfChangeNumberData);
		
		/**
		 * Change Number of Points at the Model Surface
		 */
		
		JButton btChangeGoData = new JButton("Go");
		btChangeGoData.setBounds(375, 168, 110, 20);
		btChangeGoData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numberOfPointsData = Integer.parseInt(tfChangeNumberData.getText());
				registerPointCloud = TriangleMeshSampler.sample(cubeMeshData, numberOfPointsData);
				surfaceData.deleteNode(registerPointCloud);
				surfaceData.create(registerPointCloud);
//				registerPointCloud.updateRenderStructures();
				lbNumberPointsData.setText("Number of Points: " + numberOfPointsData);
			}

		});
		MainPanel.add(btChangeGoData);

		/**
		 * Load Data Surface
		 */
		
		JButton btData = new JButton("Load Data");
		btData.setBounds(10, 122, 110, 20);
		btData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				OpenFile open = new OpenFile();
				DataPath = open.Open();
				ObjFileReader reader = new ObjFileReader();
				cubeMeshData = reader.readFile(DataPath).get(0);
				registerPointCloud = TriangleMeshSampler.sample(cubeMeshData, numberOfPointsData);
				registerPointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
				for (int i = 0; i < registerPointCloud.getNumberOfPoints(); i++) {
					registerPointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR0);
				}
				surfaceData.create(registerPointCloud);
				lbDataName.setText("Data: " + DataPath.toString());
				lbNumberPointsData.setText("Number of Points: " + numberOfPointsData);
			}
		});
		MainPanel.add(btData);
		
		/**
		 * Change the translation and rotation of the Data surface
		 */
		
		JButton btRandomPos = new JButton("random Pose");
		btRandomPos.setBounds(150, 122, 110, 20);
		btRandomPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double transRnd[] = new double[3];
				
				for(int i = 0; i< 3; i++){
					transRnd[i] = (double) (Math.random()*(0-(-1))+(-1)); // Wert muss getestet werden, ob wirklich 2 geht oder shcon zu weit ist
					System.out.println("Translation Random: "+transRnd[i]);
					
				}
				//normal transRnd[0],transRnd[1], transRnd[2] in create vector klammer
				Vector translation = VectorFactory.createVector3(0.3,0.3,0.3);
				int rndRotate = 20;
				double rotationAngle = rndRotate * Math.PI / 180;
				System.out.println("random rotate: "+rndRotate);
				System.out.println("Rotation Angel: "+rotationAngle);
				
			    TriangleMeshTransformation.transform(cubeMeshData, MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1).getNormalized(), rotationAngle));
			    TriangleMeshTransformation.translate(cubeMeshData, translation);
			    registerPointCloud = TriangleMeshSampler.sample(cubeMeshData, numberOfPointsData);
//				surfaceData.update(registerPointCloud);
			    surfaceData.deleteNode(registerPointCloud);
				surfaceData.create(registerPointCloud);
//			    registerPointCloud.updateRenderStructures();
				lbNumberPointsData.setText("Number of Points: " + numberOfPointsData);
			}

		});
		MainPanel.add(btRandomPos);

		JLabel lbModifyData = new JLabel("Modify Data:");
		lbModifyData.setBounds(10, 184, 200, 14);
		MainPanel.add(lbModifyData);

		JTextField tfNoisyValue = new JTextField();
		tfNoisyValue.setColumns(10);
		tfNoisyValue.setBounds(125, 206, 60, 20);
		MainPanel.add(tfNoisyValue);
		
		/**
		 * noisy the Data surface
		 */
		
		JButton btNoise = new JButton("Noise");
		btNoise.setBounds(10, 206, 110, 20);
		btNoise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double degree = 0.01;
				Noise noise = new Noise();
				noise.addNoise(registerPointCloud, degree);
//				surfaceData.deleteNode(registerPointCloud);
//				surfaceData.create(registerPointCloud);
//				registerPointCloud.updateRenderStructures();
				surfaceData.update();
				
			}
		});
		MainPanel.add(btNoise);
		
		/**
		 * TODO Klasse Surface überarbeiten. Mit Update und Create usw.
		 */

		JTextField tfNumberOfOutliers = new JTextField();
		tfNumberOfOutliers.setColumns(10);
		tfNumberOfOutliers.setBounds(125, 231, 60, 20);
		MainPanel.add(tfNumberOfOutliers);
		
		/**
		 * generate outliers at the Data surface
		 */
		
		JButton btOutliers = new JButton("Outliers");
		btOutliers.setBounds(10, 231, 110, 20);
		btOutliers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x=0,y=1,z = 2;
				double[] rndPoint = new double[3];
				int outliers = Integer.parseInt(tfNumberOfOutliers.getText());
				for(int k = 0; k < outliers; k++){
					for(int i = 0; i< 3; i++){
						rndPoint[i] = (double) (Math.random()*(2-(-1))+(-1)); // Wert muss getestet werden, ob wirklich 2 geht oder shcon zu weit ist		
						
					}
					Vector vec = new Vector(rndPoint[x], rndPoint[y], rndPoint[z]);			
					Point point = new Point(vec);
					System.out.println("outliers: "+point.getPosition());
					registerPointCloud.addPoint(point);
					
				}
//				surfaceData.deleteNode(registerPointCloud);
//				surfaceData.create(registerPointCloud);
				registerPointCloud.updateRenderStructures();
				

			}
		});
		MainPanel.add(btOutliers);

		JTextField tfpercentOfOverlapping = new JTextField();
		tfpercentOfOverlapping.setColumns(10);
		tfpercentOfOverlapping.setBounds(125, 256, 60, 20);
		MainPanel.add(tfpercentOfOverlapping);
		
		/**
		 * Generate a overlapping in % of the Data surface to the Model surface
		 * works only with the same obj file
		 */
		
		JButton btPartialOverlapping = new JButton("Overlapping");
		btPartialOverlapping.setBounds(10, 256, 110, 20);
		btPartialOverlapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int percent = Integer.parseInt(tfpercentOfOverlapping.getText());
				IPointCloud newPointCloudRegister = new PointCloud();
				IPointCloud newPointCloud = new PointCloud();
				IPointCloud newPointCloudBase = new PointCloud();
				
				
				double baseMaxX = basePointCloud.getPoint(0).getPosition().get(0);
				double baseMinX = basePointCloud.getPoint(0).getPosition().get(0);;
				double baseDistance = 0;
				double registerMaxX = basePointCloud.getPoint(0).getPosition().get(0);
				double registerMinX = basePointCloud.getPoint(0).getPosition().get(0);;
				double resgisterDistance = 0;
				int numberOfPoints = 0;
				double nearestPointAtTheCenter = 0;
				Point nearestPoint = null;
				
				for(int i =0; i < basePointCloud.getNumberOfPoints(); i++){
					
					if(basePointCloud.getPoint(i).getPosition().get(0)> baseMaxX){
						baseMaxX = basePointCloud.getPoint(i).getPosition().get(0);
					}else if(basePointCloud.getPoint(i).getPosition().get(0)< baseMinX){
						baseMinX = basePointCloud.getPoint(i).getPosition().get(0);
					}
					
				}
				
				baseDistance =  (baseMaxX - baseMinX)/2;
				double newMax = baseMaxX - baseDistance;
				System.out.println("Distance: " +baseDistance);
				System.out.println("new Distance: " +newMax);
				
				for(int k =0; k < basePointCloud.getNumberOfPoints(); k++){
//					System.out.println("aktueller Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
					if(basePointCloud.getPoint(k).getPosition().get(0) > newMax){
						newPointCloudBase.addPoint(basePointCloud.getPoint(k));
//						System.out.println("Neuer Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
						
					}
					
				}
				
				
				
				basePointCloud.clear();
				
				for(int i= 0; i < newPointCloudBase.getNumberOfPoints(); i++){
					basePointCloud.addPoint(newPointCloudBase.getPoint(i));
					
				}
				basePointCloud.updateRenderStructures();
				System.out.println("Anzahl der Punkte im Modell "+basePointCloud.getNumberOfPoints());
				
				/*
				 * REigster Teil
				 * 
				 */
				
				numberOfPoints = (basePointCloud.getNumberOfPoints() * percent) / 100;
				
				System.out.println("Anzahl der Prozentpunkte: "+numberOfPoints);
				
				for(int i =0; i < registerPointCloud.getNumberOfPoints(); i++){
					
					if(registerPointCloud.getPoint(i).getPosition().get(0)> registerMaxX){
						registerMaxX = registerPointCloud.getPoint(i).getPosition().get(0);
					}else if(registerPointCloud.getPoint(i).getPosition().get(0)< registerMinX){
						registerMinX = registerPointCloud.getPoint(i).getPosition().get(0);
						
					}
					
				}
				
				resgisterDistance =  (registerMaxX - registerMinX)/2;
				double newMaxRegister = registerMaxX - resgisterDistance;
				System.out.println("Distance: " +resgisterDistance);
				System.out.println("new Distance: " +newMaxRegister);
				nearestPointAtTheCenter = registerMinX;
				for(int k =0; k < registerPointCloud.getNumberOfPoints(); k++){
//					System.out.println("aktueller Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
					if(registerPointCloud.getPoint(k).getPosition().get(0) < newMaxRegister){
						newPointCloudRegister.addPoint(registerPointCloud.getPoint(k));
						if(registerPointCloud.getPoint(k).getPosition().get(0) > nearestPointAtTheCenter){
							nearestPointAtTheCenter = registerPointCloud.getPoint(k).getPosition().get(0);
							nearestPoint = registerPointCloud.getPoint(k);
							
						}
							
						
					//						System.out.println("Neuer Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
						
					}else{
						newPointCloud.addPoint(registerPointCloud.getPoint(k));
					}
					
				}
				System.out.println("dichtester Punkt an der Mitte: "+nearestPoint.getPosition());
				int[] nearestPoints = nearestPoints(nearestPoint, newPointCloud, numberOfPoints);

				
				
				registerPointCloud.clear();
				
				for(int l = 0; l < nearestPoints.length; l++ ){
//					System.out.println("Punkte die von Modell ind Register übergeben werden: "+basePointCloud.getPoint(nearestPoints[l]).getPosition());
					registerPointCloud.addPoint(newPointCloud.getPoint(nearestPoints[l]));
				}
				
				
				for(int i= 0; i < newPointCloudRegister.getNumberOfPoints(); i++){
					registerPointCloud.addPoint(newPointCloudRegister.getPoint(i));
					
				}
				
				registerPointCloud.updateRenderStructures();
				System.out.println("Anzahl der Punkte im Register mit überlapp "+registerPointCloud.getNumberOfPoints());
				
				
//				int numberOfPoints = (registerPointCloud.getNumberOfPoints() * percent) / 100;
//				int numberOfPointsBase = (basePointCloud.getNumberOfPoints() * percent) / 100;
//				
//				
//				int[] nearestPoints = nearestPoints(registerPointCloud,numberOfPoints);
//				int[] nearestPointsBase = nearestPoints(basePointCloud,numberOfPointsBase);
//				
//				System.out.println("Register position 0: "+registerPointCloud.getPoint(0).getPosition());
//				System.out.println("base position 0: "+basePointCloud.getPoint(0).getPosition());
//				
//
//				
////				System.out.println("Anzahl Punkte nach nereast Berechnung:"+nearestPoints.length );
//				
//				for(int i= 0; i < numberOfPoints; i++){
////					System.out.println("Number of Points: "+i);
//					newPointCloud.addPoint(registerPointCloud.getPoint(nearestPoints[i]));
//					
//				}
//				int k = nearestPointsBase.length;
//				for(int i= basePointCloud.getNumberOfPoints(); i > numberOfPointsBase; i--){
////					System.out.println("Number of Points: "+i);
//					newPointCloudBase.addPoint(basePointCloud.getPoint(nearestPointsBase[k]));
//					k--;
//				}
//				
//				registerPointCloud.clear();
//				basePointCloud.clear();
//				for(int i= 0; i < newPointCloud.getNumberOfPoints(); i++){
//					registerPointCloud.addPoint(newPointCloud.getPoint(i));
//					
//				}
//				registerPointCloud.updateRenderStructures();
//				
//				for(int i= 0; i < newPointCloudBase.getNumberOfPoints(); i++){
//					basePointCloud.addPoint(newPointCloudBase.getPoint(i));
//					
//				}
//				basePointCloud.updateRenderStructures();
				

			}
		});
		MainPanel.add(btPartialOverlapping);
		
		JSeparator sep2 = new JSeparator();
		sep2.setBounds(10, 281, 1000, 1);
		MainPanel.add(sep2);

		JLabel lbParameter = new JLabel("Parameters:");
		lbParameter.setBounds(10, 286, 100, 14);
		MainPanel.add(lbParameter);

		JLabel lbIterationSteps = new JLabel("Iteration Steps:");
		lbIterationSteps.setBounds(10, 305, 100, 14);
		MainPanel.add(lbIterationSteps);

		JTextField tfIterationsSteps = new JTextField();
		tfIterationsSteps.setColumns(10);
		tfIterationsSteps.setBounds(125, 305, 60, 20);
		MainPanel.add(tfIterationsSteps);

		JLabel lbSlts = new JLabel("S`LTS:");
		lbSlts.setBounds(10, 330, 100, 14);
		MainPanel.add(lbSlts);

		JTextField tfSlts = new JTextField();
		tfSlts.setColumns(10);
		tfSlts.setBounds(125, 330, 60, 20);
		MainPanel.add(tfSlts);

		JSeparator sep3 = new JSeparator();
		sep3.setBounds(10, 355, 1000, 1);
		MainPanel.add(sep3);

		JButton btRun = new JButton("Run");
		btRun.setBounds(10, 361, 250, 20);
		btRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int iterationsteps = Integer.parseInt(tfIterationsSteps.getText());
				startRegistration(basePointCloud, registerPointCloud, iterationsteps);
			}
		});
		MainPanel.add(btRun);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}
	
	public int[] nearestPoints(Point p, IPointCloud PointCloud, int NumerOfPoints){
		int NumberOfNeighbours = NumerOfPoints;
		int[] nearestPoints = new int[NumberOfNeighbours];
	    PointNeighborsQuery nearest = new PointNeighborsQuery(PointCloud);

	    for (int k = 0; k < PointCloud.getNumberOfPoints(); k++) {

	      nearest.queryKnn(p.getPosition(), NumberOfNeighbours);

	     

	      
		}
	   System.out.println("Anzahl an Nachbarn:"+nearest.getNumberOfNeighbors()); 
	   
	   
	    for(int i = 0; i < nearest.getNumberOfNeighbors(); i++){
	    nearestPoints[i] = nearest.getNeigbor(i);
//	      System.out.println("Platz in der RegisterCloud:" + nearestPoints[i]);
//	      System.out.println("Anazhl Punkte die übergeben werden sollen: " + nearestPoints.length);
	}
	    return nearestPoints;
		
	}

	private void startRegistration(IPointCloud base, IPointCloud register, int iteration) {

		Logger.getInstance().message("| Iteration Steps \t| q \t| dk \t |");
		IcpDistanceFunction icp = new IcpDistanceFunction();
		
		double dkOld = 2;
		double dkNew = 0.0;
		double dk = 0.0;
		double slts = 10000;
		for (int i = 1; i <= iteration; i++) {
			if(dk < 0)
				break;		
			if(trimmed == true){
				trimmedICP trIcp = new trimmedICP(register, slts);
				register = trIcp.startAlgorithm(base,true , 0);
			}else{
			register = icp.startAlgorithm(base, register, i);
			dkNew = icp.dk;
			dk = dkOld - dkNew;
			System.out.println("dk: "+dk+ " = "+dkOld+ " + "+dkNew );
			dkOld = dkNew;
			}
			
			
			// for(int k = 0 ;k < Register.getNumberOfPoints(); k++ ){
			// System.out.println(" verï¿½nderte Register: "+
			// Register.getPoint(k).getPosition());
			// }
			
			registerPointCloud.updateRenderStructures();

//			surfaceData.deleteNode(registerPointCloud);
//			surfaceData.create(registerPointCloud);

		}
	}



}

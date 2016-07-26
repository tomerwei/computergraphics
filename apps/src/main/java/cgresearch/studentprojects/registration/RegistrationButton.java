package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
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
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.ObjFileWriter;
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
	private JTextField tfNoiseValue;
	private JTextField tfOutliers;
	private JTextField tfRotation;
	private JTextField tfx;
	private JTextField tfy;
	private JTextField tfz;
	private JTextField tfIterationsSteps;
	private JTextField tfpercent;	
	private IPointCloud basePointCloud = new PointCloud();
	private IPointCloud registerPointCloud = new PointCloud();
	ModifieSurface surfaceBase;
	ModifieSurface surfaceData;
	private boolean trimmed = true;
	private double[] input = new double[8];
	private int numberOfPointsModel = 3000;
	private int numberOfPointsData = 3000;

	public RegistrationButton(CgRootNode rootNode) {

		this.surfaceBase = new ModifieSurface(rootNode);
		this.surfaceData = new ModifieSurface(rootNode);

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
				surfaceBase.changeColor("green");
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
				System.out.println("Modelpath: "+ModelPath);
				ObjFileReader reader = new ObjFileReader();
				cubeMeshModel = reader.readFile(ModelPath).get(0);
				basePointCloud = TriangleMeshSampler.sample(cubeMeshModel, numberOfPointsModel);
				surfaceBase.create(basePointCloud);
				surfaceBase.changeColor("green");
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
		 * Change Number of Points at the Data Surface
		 */

		JButton btChangeGoData = new JButton("Go");
		btChangeGoData.setBounds(375, 168, 110, 20);
		btChangeGoData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				numberOfPointsData = Integer.parseInt(tfChangeNumberData.getText());
				registerPointCloud = TriangleMeshSampler.sample(cubeMeshData, numberOfPointsData);
				surfaceData.deleteNode(registerPointCloud);
				surfaceData.create(registerPointCloud);
				surfaceBase.changeColor("red");
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
				surfaceData.create(registerPointCloud);
				surfaceData.changeColor("red");
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
				Vector translation = VectorFactory.createVector3(-0.05,-0.05,-0.05);
//				int rndRotate = 0;
//				double rotationAngle = rndRotate * Math.PI / 180;
//				System.out.println("random rotate: "+rndRotate);
//				System.out.println("Rotation Angel: "+rotationAngle);

//				TriangleMeshTransformation.transform(cubeMeshData, MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1).getNormalized(), rotationAngle));
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
		
		JSeparator sep1 = new JSeparator();
		sep.setBounds(10, 194, 1000, 1);
		MainPanel.add(sep1);

		JLabel lbModifyData = new JLabel("Modify Data: (Hold mouse over text for more Information)");
		lbModifyData.setBounds(10, 204, 350, 20);
		MainPanel.add(lbModifyData);
		
		JLabel lbNoise = new JLabel("Noise: ");
		lbNoise.setBounds(10, 229, 200, 20);
		
		MainPanel.add(lbNoise);

		tfNoiseValue = new JTextField();
		tfNoiseValue.setColumns(10);
		tfNoiseValue.setBounds(105, 230, 60, 20);
		tfNoiseValue.setText("0");
		tfNoiseValue.setToolTipText("a value between 0.01 and 0.05");
		MainPanel.add(tfNoiseValue);
		
		JLabel lbOutliers = new JLabel("Outliers: ");
		lbOutliers.setBounds(10, 255, 200, 20);
		MainPanel.add(lbOutliers);

		tfOutliers = new JTextField();
		tfOutliers.setColumns(10);
		tfOutliers.setBounds(105, 256, 60, 20);
		tfOutliers.setText("0");
		tfOutliers.setToolTipText("use only integer");
		MainPanel.add(tfOutliers);
		
		JLabel lbRotaion = new JLabel("Rotation: ");
		lbRotaion.setBounds(10, 281, 200, 20);
		MainPanel.add(lbRotaion);

		tfRotation = new JTextField();
		tfRotation.setColumns(10);
		tfRotation.setBounds(105, 282, 60, 20);
		tfRotation.setText("0");
		tfRotation.setToolTipText("in degrees");
		MainPanel.add(tfRotation);
		
		JLabel lbTransformation = new JLabel("Transformation: ");
		lbTransformation.setBounds(10, 307, 200, 20);
		MainPanel.add(lbTransformation);


		JLabel lbx = new JLabel("x: ");
		lbx.setBounds(105, 307, 200, 20);
		MainPanel.add(lbx);

		tfx = new JTextField();
		tfx.setColumns(10);
		tfx.setBounds(118, 308, 35, 20);
		tfx.setText("-0.05");
		tfx.setToolTipText("small values for x,y,z direction");
		MainPanel.add(tfx);
		
		JLabel lby = new JLabel("y: ");
		lby.setBounds(158, 307, 200, 20);
		MainPanel.add(lby);

		tfy = new JTextField();
		tfy.setColumns(10);
		tfy.setBounds(171, 308, 35, 20);
		tfy.setText("-0.05");
		tfy.setToolTipText("small values for x,y,z direction");
		MainPanel.add(tfy);
		
		JLabel lbz = new JLabel("z: ");
		lbz.setBounds(211, 307, 200, 20);
		MainPanel.add(lbz);

		tfz = new JTextField();
		tfz.setColumns(10);
		tfz.setBounds(224, 308, 35, 20);
		tfz.setText("-0.05");
		tfz.setToolTipText("small values for x,y,z direction");
		MainPanel.add(tfz);
		
		JButton btModifie = new JButton("Modifie");
		btModifie.setBounds(105, 333, 110, 20);
		btModifie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					modifie(arg0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		MainPanel.add(btModifie);

		/**
		 * Generate a overlapping in % of the Data surface to the Model surface
		 * works only with the same obj file
		 */

//		JButton btPartialOverlapping = new JButton("Overlapping");
//		btPartialOverlapping.setBounds(10, 256, 110, 20);
//		btPartialOverlapping.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				percent = Integer.parseInt(tfpercentOfOverlapping.getText());
//				IPointCloud newPointCloudRegister = new PointCloud();
//				IPointCloud newPointCloud = new PointCloud();
//				IPointCloud newPointCloudBase = new PointCloud();
//
//
//				double baseMaxX = basePointCloud.getPoint(0).getPosition().get(0);
//				double baseMinX = basePointCloud.getPoint(0).getPosition().get(0);;
//				double baseDistance = 0;
//				//				double registerMaxX = basePointCloud.getPoint(0).getPosition().get(0);
//				//				double registerMinX = basePointCloud.getPoint(0).getPosition().get(0);
//				double registerMaxX = registerPointCloud.getPoint(0).getPosition().get(0);
//				double registerMinX = registerPointCloud.getPoint(0).getPosition().get(0);
//				double resgisterDistance = 0;
//				double borderForPoints = 0;
//				double singleStep = 0;
//				int numberOfPoints = 0;
//				double nearestPointAtTheCenter = 0;
//				Point nearestPoint = null;
//				double registerMiddle = 0;
//
//								for(int i =0; i < basePointCloud.getNumberOfPoints(); i++){
//									
//									if(basePointCloud.getPoint(i).getPosition().get(0)> baseMaxX){
//										baseMaxX = basePointCloud.getPoint(i).getPosition().get(0);
//									}else if(basePointCloud.getPoint(i).getPosition().get(0)< baseMinX){
//										baseMinX = basePointCloud.getPoint(i).getPosition().get(0);
//									}
//									
//								}
//								
//								baseDistance =  (baseMaxX - baseMinX)/2;
//								double newMax = baseMaxX - baseDistance;
//								System.out.println("Distance: " +baseDistance);
//								System.out.println("new Distance: " +newMax);
//								
//								for(int k =0; k < basePointCloud.getNumberOfPoints(); k++){
//				//					System.out.println("aktueller Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
//									if(basePointCloud.getPoint(k).getPosition().get(0) > newMax){
//										newPointCloudBase.addPoint(basePointCloud.getPoint(k));
//				//						System.out.println("Neuer Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
//										
//									}
//									
//								}
//								
//								
//								
//								basePointCloud.clear();
//								
//								for(int i= 0; i < newPointCloudBase.getNumberOfPoints(); i++){
//									basePointCloud.addPoint(newPointCloudBase.getPoint(i));
//									
//								}
//								basePointCloud.updateRenderStructures();
//								System.out.println("Anzahl der Punkte im Modell "+basePointCloud.getNumberOfPoints());
//
//				/*
//				 * REigster Teil
//				 * 
//				 */
//
//				//find x max und x min
//				for(int i =0; i < registerPointCloud.getNumberOfPoints(); i++){
//
//					if(registerPointCloud.getPoint(i).getPosition().get(0)> registerMaxX){
//						registerMaxX = registerPointCloud.getPoint(i).getPosition().get(0);
//					}else if(registerPointCloud.getPoint(i).getPosition().get(0)< registerMinX){
//						registerMinX = registerPointCloud.getPoint(i).getPosition().get(0);
//
//					}
//
//				}
//
//
//				//find max Distance and the middle
//				resgisterDistance =  (registerMaxX - registerMinX);
//				System.out.println("resgisterDistance: "+resgisterDistance);
//				if(registerMinX < 0){
//					registerMiddle = registerMaxX + registerMinX;
//				}
//				else{
//					registerMiddle = registerMaxX - registerMinX;
//				}
//				System.out.println("registerMaxX: "+registerMaxX);
//				System.out.println("registerMinX: "+registerMinX);
//				
//				double middleToMax = registerMaxX - (resgisterDistance/2);
//				double temp = (middleToMax * percent)/100;
//				borderForPoints = (resgisterDistance/2) + temp;
//				System.out.println("middle: "+resgisterDistance/2);
//				singleStep = (resgisterDistance/2) /100;
////				System.out.println("singleStep: "+singleStep);
//				System.out.println("registerMiddle: "+registerMiddle);
////				borderForPoints = (resgisterDistance/2);
////				for(int p =0; p < percent; p++){
////					borderForPoints = borderForPoints + singleStep;
////				}
//				System.out.println("register border for points: "+borderForPoints);
//				for(int k = 0; k < registerPointCloud.getNumberOfPoints(); k++){
//					if(registerPointCloud.getPoint(k).getPosition().get(0) < borderForPoints){
//						newPointCloudRegister.addPoint(registerPointCloud.getPoint(k));
//
//					}
//				}
//				System.out.println("Anzahl Punkte in der neuen RegiserPointCloud: "+registerPointCloud.getNumberOfPoints());
//				registerPointCloud.clear();
//				for(int i= 0; i < newPointCloudRegister.getNumberOfPoints(); i++){
//					registerPointCloud.addPoint(newPointCloudRegister.getPoint(i));
//
//				}
//				System.out.println("base Point Cloud Größe: "+basePointCloud.getNumberOfPoints());
//				System.out.println("Register Point Cloud Größe: "+registerPointCloud.getNumberOfPoints());
//
//				registerPointCloud.updateRenderStructures();
//
//
//
//				//				numberOfPoints = (basePointCloud.getNumberOfPoints() * percent) / 100;
//				//				
//				//				System.out.println("Anzahl der Prozentpunkte: "+numberOfPoints);
//				//				
//				//				for(int i =0; i < registerPointCloud.getNumberOfPoints(); i++){
//				//					
//				//					if(registerPointCloud.getPoint(i).getPosition().get(0)> registerMaxX){
//				//						registerMaxX = registerPointCloud.getPoint(i).getPosition().get(0);
//				//					}else if(registerPointCloud.getPoint(i).getPosition().get(0)< registerMinX){
//				//						registerMinX = registerPointCloud.getPoint(i).getPosition().get(0);
//				//						
//				//					}
//				//					
//				//				}
//				//				
//				//				resgisterDistance =  (registerMaxX - registerMinX)/2;
//				//				double newMaxRegister = registerMaxX - resgisterDistance;
//				//				System.out.println("Distance: " +resgisterDistance);
//				//				System.out.println("new Distance: " +newMaxRegister);
//				//				nearestPointAtTheCenter = registerMinX;
//				//				for(int k =0; k < registerPointCloud.getNumberOfPoints(); k++){
//				////					System.out.println("aktueller Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
//				//					if(registerPointCloud.getPoint(k).getPosition().get(0) < newMaxRegister){
//				//						newPointCloudRegister.addPoint(registerPointCloud.getPoint(k));
//				//						if(registerPointCloud.getPoint(k).getPosition().get(0) > nearestPointAtTheCenter){
//				//							nearestPointAtTheCenter = registerPointCloud.getPoint(k).getPosition().get(0);
//				//							nearestPoint = registerPointCloud.getPoint(k);
//				//							
//				//						}
//				//							
//				//						
//				//					//						System.out.println("Neuer Punkt: "+basePointCloud.getPoint(k).getPosition().get(0));
//				//						
//				//					}else{
//				//						newPointCloud.addPoint(registerPointCloud.getPoint(k));
//				//					}
//				//					
//				//				}
//				//				
//				//				System.out.println("dichtester Punkt an der Mitte: "+nearestPoint.getPosition());
//				//				int[] nearestPoints = nearestPoints(nearestPoint, newPointCloud, numberOfPoints);
//				//
//				//				
//				//				
//				//				registerPointCloud.clear();
//				//				
//				//				for(int l = 0; l < nearestPoints.length; l++ ){
//				////					System.out.println("Punkte die von Modell ind Register übergeben werden: "+basePointCloud.getPoint(nearestPoints[l]).getPosition());
//				//					registerPointCloud.addPoint(newPointCloud.getPoint(nearestPoints[l]));
//				//				}
//				//				
//				//				System.out.println("Anzahl der Punkte im RegisterModell "+newPointCloudRegister.getNumberOfPoints());
//				//				for(int i= 0; i < newPointCloudRegister.getNumberOfPoints(); i++){
//				//					registerPointCloud.addPoint(newPointCloudRegister.getPoint(i));
//				//					
//				//				}
//				//				
//				//				registerPointCloud.updateRenderStructures();
//				//				System.out.println("Anzahl der Punkte im Register mit überlapp "+registerPointCloud.getNumberOfPoints());
//				//				
//
//
//
//			}
//		});
//		MainPanel.add(btPartialOverlapping);

//		JSeparator sep2 = new JSeparator();
//		sep2.setBounds(10, 281, 1000, 1);
//		MainPanel.add(sep2);

//		JLabel lbParameter = new JLabel("Parameters:");
//		lbParameter.setBounds(10, 286, 100, 14);
//		MainPanel.add(lbParameter);
		
		JSeparator sep3 = new JSeparator();
		sep3.setBounds(10, 358, 1000, 1);
		MainPanel.add(sep3);

		JLabel lbIterationSteps = new JLabel("Iteration Steps:");
		lbIterationSteps.setBounds(10, 363, 100, 14);
		MainPanel.add(lbIterationSteps);

		tfIterationsSteps = new JTextField();
		tfIterationsSteps.setColumns(10);
		tfIterationsSteps.setBounds(105, 364, 60, 20);
		tfIterationsSteps.setText("100");
		tfIterationsSteps.setToolTipText("use only integer");
		MainPanel.add(tfIterationsSteps);

		JLabel lbSlts = new JLabel("Percent:");
		lbSlts.setBounds(10, 389, 100, 14);
		MainPanel.add(lbSlts);

		tfpercent = new JTextField();
		tfpercent.setColumns(10);
		tfpercent.setBounds(105, 390, 60, 20);
		tfpercent.setText("-1");
		tfpercent.setToolTipText("-1: automaticlly search for overlap. Otherwise use only integer degrees and between 40 and 100 ");
		MainPanel.add(tfpercent);

		

		JButton btRun = new JButton("Run");
		btRun.setBounds(10, 415, 250, 20);
		btRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
					try {
						run(arg0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				

				
				
				
				
				
				
				
				
				
//				int percent = -1;
//				int iterationsteps = Integer.parseInt(tfIterationsSteps.getText());
//				if(! tfpercent.getText().equals("")){
//					
//					
//					percent = Integer.parseInt(tfpercent.getText());
//				}
////					System.out.println("percent erste: "+percent);
//				
			}
		});
		MainPanel.add(btRun);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}
	
	public void modifie(ActionEvent evt) throws IOException{
		
		int test = Integer.parseInt(tfOutliers.getText());
		System.out.println("test: "+test);
		try{
			if (Double.parseDouble(tfNoiseValue.getText()) < 0 || Double.parseDouble(tfNoiseValue.getText()) > 0.05) throw new NumberFormatException("Noise: number not in the range of 0.01 to 0.05");
			input[0] = (Double.parseDouble(tfNoiseValue.getText()));
			

			if (test < 0) throw new NumberFormatException("Outliers: no negative value");
			input[1] = (Double.parseDouble(tfOutliers.getText()));
			
			if (Double.parseDouble(tfRotation.getText()) < 0 || Double.parseDouble(tfRotation.getText()) > 360) throw new NumberFormatException("Rotation: no negative value and/or no value greater than 360");
			input[2] = (Double.parseDouble(tfRotation.getText()));
			
			
			input[3] = (Double.parseDouble(tfx.getText()));
			input[4] = (Double.parseDouble(tfy.getText()));
			input[5] = (Double.parseDouble(tfz.getText()));
			
			coordinateInput();
			
			//			

		}catch(NumberFormatException e){
			Logger.getInstance().message(e.getMessage());
		}
		
	}
	
public void run(ActionEvent evt) throws IOException{
	boolean searchOverlap = false;

		try{
						
			if (Double.parseDouble(tfIterationsSteps.getText()) < 0) throw new NumberFormatException("Iteration steps: must be a value over 0");
			input[6] = (Double.parseDouble(tfIterationsSteps.getText()));

			if (Double.parseDouble(tfpercent.getText()) < - 1 || Double.parseDouble(tfpercent.getText()) > 0 && Double.parseDouble(tfpercent.getText()) < 40 || Double.parseDouble(tfpercent.getText()) > 100) throw new NumberFormatException("Percent: no value less than 40 and no value greater than 100");
			input[7] = (Double.parseDouble(tfpercent.getText()));
			
			
			//			

		}catch(NumberFormatException e){
			Logger.getInstance().message(e.getMessage());
		}
		
		if(input[7] == -1)
			searchOverlap = true;
		
		startRegistration(basePointCloud, registerPointCloud, input[6], input[7], searchOverlap);
		
	}
	
	/**
	 * Methode to create the right register pointcloud with overlap, noise etc   
	 * 
	 * Array[0] = noise
	 * Array[1] = outliers
	 * Array[2] = rotation
	 * Array[3] = translation X direction
	 * Array[4] = translation Y direction
	 * Array[5] = translation Z direction
	 * Array[6] = iteration steps
	 * Array[7] = manually percent input or automatic search
	 * 
	 */
	
	private void coordinateInput(){
		
		
		if(input[2] != 0)
			surfaceData.addRotation(input[2], cubeMeshData);
		if(input[3] != 0 || input[4] != 0 || input[5] != 0)
			surfaceData.addTranslation(input[3], input[4], input[5], cubeMeshData);
		if(input[0] != 0)
			surfaceData.addNoise(input[0]);
		if(input[1] != 0)
			surfaceData.addOutliers(input[1]);
	
		
			
	}
	


	private void startRegistration(IPointCloud base, IPointCloud register, double iteration, double percent, boolean overlap) {

		Logger.getInstance().message("| Iteration Steps \t| q \t| dk \t |");
//		System.out.println("percent zweite: "+percent);
		double slts = 10000;
		double mseNew = 0.0;
		double mse = 5;
		double mseOld = 0.0;
		double tolerance = 9.E-6;
		double usedIterations = 0;
		boolean searchOverlap;
		double percentDouble = ((double)percent)/100;
		
		searchOverlap = overlap;
		System.out.println("searchOverlap = "+searchOverlap);
		
		if(searchOverlap){
			System.out.println("iteration: "+iteration);
			for (int i = 1; i <= iteration; i++) {
				if(mse < tolerance)
					break;		
				trimmedICP trIcp = new trimmedICP(register, slts);
				register = trIcp.startAlgorithm(base,searchOverlap , percentDouble);
				System.out.println("Änderung Point 0 : "+register.getPoint(0).getPosition());
				
				mseNew = trIcp.MSE;
				mse = Math.abs((mseOld - mseNew));
				mseOld = mseNew;
				System.out.println("mse: "+mse+ " = "+mseOld+ " - "+mseNew );
				usedIterations++;
				System.out.println("used Iterations: "+usedIterations);
//				registerPointCloud.updateRenderStructures();
				surfaceData.update();
				
			}
		}else{
			System.out.println("searchOverlap: flase");
			for (int i = 1; i <= iteration; i++) {
				if(mse < tolerance)
					break;		
				trimmedICP trIcp = new trimmedICP(register, slts);
				register = trIcp.startAlgorithm(base,searchOverlap , percentDouble);
				mseNew = trIcp.MSE;
				mse = Math.abs((mseOld - mseNew));
				mseOld = mseNew;
				System.out.println("mse: "+mse+ " = "+mseOld+ " - "+mseNew );
				usedIterations++;
				System.out.println("used Iterations: "+usedIterations);
				surfaceData.update();
			}
		}
//		surfaceData.update();
	}
}
	


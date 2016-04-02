package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;

import cgresearch.ui.IApplicationControllerGui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.util.List;

import javax.swing.border.LineBorder;
import javax.swing.DefaultListModel;

import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.PointNeighborsQuery;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import Jama.Matrix;

public class RegistrationButton extends IApplicationControllerGui {

	/**
	 * 
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

	private final CgRootNode rootNode;
	int numberOfPointsModel = 3000;
	int numberOfPointsData = 3000;

	public RegistrationButton(CgRootNode rootNode) {
		this.rootNode = rootNode;
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
				RegistrationFrame regFrame = new RegistrationFrame();

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
				
//				for(int i = 0; i< 3; i++){
//					transRnd[i] = (double) (Math.random()*(2-(-1))+(-1)); // Wert muss getestet werden, ob wirklich 2 geht oder shcon zu weit ist
//					System.out.println("Translation Random: "+transRnd[i]);
//					
//				}
//				
//				Vector translation = VectorFactory.createVector3(transRnd[0],transRnd[1], transRnd[2]);
				int rndRotate = (int) (Math.random()*100-360);
				double rotationAngle = rndRotate * Math.PI / 180;
			    TriangleMeshTransformation.transform(cubeMeshData, MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1), rotationAngle));
//			    TriangleMeshTransformation.translate(cubeMeshData, translation);
			    registerPointCloud = TriangleMeshSampler.sample(cubeMeshData, numberOfPointsData);
//				surfaceData.update(registerPointCloud);
			    surfaceData.deleteNode(registerPointCloud);
				surfaceData.create(registerPointCloud);
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

				// verrauschen
				// update der Pointclouds
			}
		});
		MainPanel.add(btNoise);

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

				// outliers
				// update der Pointclouds
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

				// überlappung
				// update der Pointclouds
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

		// JButton startButton = new JButton("Start");
		// startButton.setBounds(10, 130, 80, 20);
		// startButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		//
		// int iteration = Integer.parseInt(iterationSteps.getText());
		//
		// startRegistration(RegistrationFrame.basePointCloud,
		// RegistrationFrame.Register, iteration);
		//
		// }
		//
		// });
		// MainPanel.add(startButton);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}

	private void startRegistration(IPointCloud Base, IPointCloud Register, int iteration) {

		Logger.getInstance().message("| Iteration Steps \t| q \t| dk \t |");
		IcpDistanceFunction icp = new IcpDistanceFunction();
		for (int i = 1; i <= iteration; i++) {

			Register = icp.startAlgorithm(Base, Register, i);
			// for(int k = 0 ;k < Register.getNumberOfPoints(); k++ ){
			// System.out.println(" verï¿½nderte Register: "+
			// Register.getPoint(k).getPosition());
			// }

			surfaceData.deleteNode(registerPointCloud);
			surfaceData.create(registerPointCloud);

		}
	}

	// private void loadTestData() {
	// // Load cube from file
	// ObjFileReader reader = new ObjFileReader();
	// ITriangleMesh cubeMeshBase = reader.readFile(basePatch).get(0);
	// ITriangleMesh cubeMeshRegister = reader.readFile(registerPath).get(0);
	// // Created point cloud from cube
	// basePointCloud = TriangleMeshSampler.sample(cubeMeshBase, 500);
	// basePointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
	// // Set point color
	// for (int i = 0; i < basePointCloud.getNumberOfPoints(); i++) {
	// basePointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR0);
	// }
	//
	// // Transform mesh for second cube
	// // Rotation of the second point cloud: 10 degrees in degrees -
	// transformed
	// // to radiens. Rotation axis: (1,1,1)
	// double rotationAngle = 10 * Math.PI / 180;
	// TriangleMeshTransformation.transform(cubeMeshRegister,
	// VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newVector(1, 1,
	// 1), rotationAngle));
	// // Optional: translation
	// TriangleMeshTransformation.translate(cubeMeshRegister,
	// VectorMatrixFactory.newVector(0.2, 0.2, 0.2));
	// registerPointCloud = TriangleMeshSampler.sample(cubeMeshRegister, 500);
	// registerPointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
	// // Set point color
	// for (int i = 0; i < registerPointCloud.getNumberOfPoints(); i++) {
	// registerPointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
	// }
	//
	// CgNode basePointCloudNode = new CgNode(basePointCloud, "pointCloud");
	// getCgRootNode().addChild(basePointCloudNode);
	// CgNode registerPointCloudNode = new CgNode(registerPointCloud,
	// "pointCloud2");
	// getCgRootNode().addChild(registerPointCloudNode);
	//
	// }

}

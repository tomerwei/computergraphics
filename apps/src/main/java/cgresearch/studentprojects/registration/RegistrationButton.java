package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;




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


import cgresearch.graphics.datastructures.points.PointNeighborsQuery;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.core.math.jama.Matrix;
import cgresearch.graphics.bricks.CgApplication;



public class RegistrationButton extends IApplicationControllerGui {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String basePatch;
	private String registerPath;
	JTextField iterationSteps;
	private IPointCloud basePointCloud = new PointCloud();
	private IPointCloud registerPointCloud = new PointCloud();

	public RegistrationButton(){
	
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
	
	JPanel MainPanel = new JPanel();
	MainPanel.setPreferredSize(new Dimension(200, 300));
	MainPanel.setBounds(0, 0, 200, 264);
	add(MainPanel);
	MainPanel.setLayout(null);
	
	JLabel lblBuildingCounter = new JLabel("Load PointClouds:");
	lblBuildingCounter.setBounds(10, 8, 150, 14);
	MainPanel.add(lblBuildingCounter);
	
	JButton openBase = new JButton("Load");
	openBase.setBounds(100, 28, 80, 20);
	openBase.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			OpenFile open = new OpenFile();
			basePatch = open.Open();
			System.out.println("Test PAth:"+ basePatch.toString());
		}

	});
	MainPanel.add(openBase);
	
	
	JLabel loadBase = new JLabel("Load Base:");
	loadBase.setBounds(10, 28, 100, 14);
	MainPanel.add(loadBase);
	
	JLabel loadRegister = new JLabel("Load Register:");
	loadRegister.setBounds(10, 56, 100, 14);
	MainPanel.add(loadRegister);
	
	JButton openRegister = new JButton("Load");
	openRegister.setBounds(100, 56, 80, 20);
	openRegister.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			OpenFile open = new OpenFile();
			registerPath = open.Open();
			System.out.println("Test PAth:"+ registerPath.toString());
		}

	});
	MainPanel.add(openRegister);
	
	JLabel iteration = new JLabel("Iteration Steps:");
	iteration.setBounds(10, 100, 100, 14);
	MainPanel.add(iteration);
	
	iterationSteps = new JTextField();
	iterationSteps.setColumns(10);
	iterationSteps.setBounds(100, 100, 42, 20);
	MainPanel.add(iterationSteps);
	
	
	
	
	
	JButton startButton = new JButton("Start");
	startButton.setBounds(10, 130, 80, 20);
	startButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			int iteration = Integer.parseInt(iterationSteps.getText());
			
			startRegistration(RegistrationFrame.basePointCloud,  RegistrationFrame.Register, iteration);
			
		}

	});
	MainPanel.add(startButton);
	

	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}
	

	
	private void startRegistration(IPointCloud Base, IPointCloud Register, int iteration){
		
		Logger.getInstance().message("| Iteration Steps \t| q \t| dk \t |");
		IcpDistanceFunction icp = new IcpDistanceFunction();
		for(int i = 1; i <= iteration; i++){
			
			
			Register = icp.startAlgorithm(Base, Register, i);
//			for(int k = 0 ;k < Register.getNumberOfPoints(); k++ ){
//				System.out.println(" veränderte Register: "+ Register.getPoint(k).getPosition());
//			}
			
			Register.updateRenderStructures();
			
		
		}
	}
	
//	private void loadTestData() {
//	    // Load cube from file
//	    ObjFileReader reader = new ObjFileReader();
//	    ITriangleMesh cubeMeshBase = reader.readFile(basePatch).get(0);
//	    ITriangleMesh cubeMeshRegister = reader.readFile(registerPath).get(0);
//	    // Created point cloud from cube
//	    basePointCloud = TriangleMeshSampler.sample(cubeMeshBase, 500);
//	    basePointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
//	    // Set point color
//	    for (int i = 0; i < basePointCloud.getNumberOfPoints(); i++) {
//	      basePointCloud.getPoint(i).getColor().copy(Material.PALETTE2_COLOR0);
//	    }
//
//	    // Transform mesh for second cube
//	    // Rotation of the second point cloud: 10 degrees in degrees - transformed
//	    // to radiens. Rotation axis: (1,1,1)
//	    double rotationAngle = 10 * Math.PI / 180;
//	    TriangleMeshTransformation.transform(cubeMeshRegister,
//	        VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newIVector3(1, 1, 1), rotationAngle));
//	    // Optional: translation
//	    TriangleMeshTransformation.translate(cubeMeshRegister, VectorMatrixFactory.newIVector3(0.2, 0.2, 0.2));
//	    registerPointCloud = TriangleMeshSampler.sample(cubeMeshRegister, 500);
//	    registerPointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
//	    // Set point color
//	    for (int i = 0; i < registerPointCloud.getNumberOfPoints(); i++) {
//	      registerPointCloud.getPoint(i).getColor().copy(Material.PALETTE1_COLOR3);
//	    }
//
//	    CgNode basePointCloudNode = new CgNode(basePointCloud, "pointCloud");
//	    getCgRootNode().addChild(basePointCloudNode);
//	    CgNode registerPointCloudNode = new CgNode(registerPointCloud, "pointCloud2");
//	    getCgRootNode().addChild(registerPointCloudNode);
//
//	  }
		
				
			
	
	

	

	

	
	
	
	
	

	

	

	

	

	
	

}

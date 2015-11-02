package cgresearch.studentprojects.registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.ui.IApplicationControllerGui;

public class RegistrationButton extends IApplicationControllerGui {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistrationButton(){
	
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
	JButton startButton = new JButton("Start");
	startButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			startRegistration();
			
		}

	});
	add(startButton);
	
//	JButton updateButton = new JButton("Update");
//	startButton.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent arg0) {
//			updateRegistration();
//		}
//
//	});
//	add(updateButton);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}
	
	private void startRegistration(){
		
		
		
		
		for(int i=0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
			System.out.println("newPiontCloud vorher: "+RegistrationFrame.pointCloud2.getPoint(i).getPosition());
		}
		
		
		
		IcpDistanceFunction icp =  new IcpDistanceFunction(RegistrationFrame.pointCloud);
		RegistrationFrame.pointCloud2 = icp.startAlgorithm(RegistrationFrame.pointCloud2, 10);
		RegistrationFrame.pointCloud2.updateRenderStructures();
		
		
		
		for(int i = 0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
			System.out.println("PointCloud2 "+RegistrationFrame.pointCloud2.getPoint(i).getPosition());
		}
		
		
//		icp.startAlgorithm(RegistrationFrame.pointCloud2, 50);
		
//		RegistrationFrame.pointCloud2.updateRenderStructures();
//		for(int i=0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
//			System.out.println("newPintCloud: "+RegistrationFrame.pointCloud2.getPoint(i).getPosition());
//		}
		
		
	}
	private void updateRegistration(){
		//RegistrationFrame.pointCloud2.updateRenderStructures();
//		IVector3 position7 = VectorMatrixFactory.newIVector3(1,4,1);
//		IVector3 position8 = VectorMatrixFactory.newIVector3(2,5,2);
//		IVector3 position9 = VectorMatrixFactory.newIVector3(3,6,3);
//		IVector3 color = VectorMatrixFactory.newIVector3(Math.random(),
//				Math.random(), Math.random());
//		IVector3 normal = VectorMatrixFactory.newIVector3(Math.random(),
//				Math.random(), Math.random());
		
//		for(int i = 0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
//			System.out.println("PointCloud2 first "+RegistrationFrame.pointCloud2.getPoint(i).getPosition());
//		}
		RegistrationFrame.pointCloud2.getPoint(0).getPosition().set(0, 1);
		RegistrationFrame.pointCloud2.getPoint(0).getPosition().set(1, 4);
		RegistrationFrame.pointCloud2.getPoint(0).getPosition().set(2, 1);
		
		RegistrationFrame.pointCloud2.getPoint(1).getPosition().set(0, 2);
		RegistrationFrame.pointCloud2.getPoint(1).getPosition().set(1, 5);
		RegistrationFrame.pointCloud2.getPoint(1).getPosition().set(2, 2);
		
		RegistrationFrame.pointCloud2.getPoint(2).getPosition().set(0, 3);
		RegistrationFrame.pointCloud2.getPoint(2).getPosition().set(1, 6);
		RegistrationFrame.pointCloud2.getPoint(2).getPosition().set(2, 3);
		RegistrationFrame.pointCloud2.updateRenderStructures();
		
//		for(int i = 0; i < RegistrationFrame.pointCloud2.getNumberOfPoints(); i++){
//			System.out.println("PointCloud2 "+RegistrationFrame.pointCloud2.getPoint(i).getPosition());
//		}
	}
	

	
	

}

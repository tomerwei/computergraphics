package cgresearch.studentprojects.registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

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
	
	JButton updateButton = new JButton("Update");
	startButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			updateRegistration();
		}

	});
	add(updateButton);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Registration";
	}
	
	private void startRegistration(){
		IcpDistanceFunction icp =  new IcpDistanceFunction(RegistrationFrame.pointCloud);
		RegistrationFrame.pointCloud2 = icp.startAlgorithm(RegistrationFrame.pointCloud2);
		
		
	}
	private void updateRegistration(){
		//RegistrationFrame.pointCloud2.updateRenderStructures();
		
	}
	

	
	

}

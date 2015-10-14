package cgresearch.studentprojects.registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import cgresearch.studentprojects.shapegrammar.gui.frame.NewBuildingFrame;
import cgresearch.studentprojects.shapegrammar.gui.frame.NewCityFrame;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;
import cgresearch.ui.menu.CgApplicationMenu;

public class RegistrationMenu extends CgApplicationMenu implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistrationMenu() {
		super("Registration");
		JMenuItem full = new JMenuItem("Full");
		full.addActionListener(this);
		full.setActionCommand("Full");
		JMenuItem step_by_step = new JMenuItem("Step by Step");
		step_by_step.addActionListener(this);
		step_by_step.setActionCommand("step_by_step");
		add(full);
		add(step_by_step);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Full".equals(e.getActionCommand())){
			NewBuildingFrame newFrame = new NewBuildingFrame();
			newFrame.setVisible(true);
		}else if("step_by_step".equals(e.getActionCommand())){
			NewCityFrame cityFrame = new NewCityFrame();
			cityFrame.setVisible(true);
		}
		
	}
	
	

}

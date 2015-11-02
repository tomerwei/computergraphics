package cgresearch.studentprojects.registration;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.studentprojects.shapegrammar.gui.frame.NewBuildingFrame;
import cgresearch.studentprojects.shapegrammar.gui.frame.NewCityFrame;
import cgresearch.studentprojects.shapegrammar.gui.menu.BuilderMenu;
import cgresearch.ui.menu.CgApplicationMenu;

public class RegistrationMenu extends CgApplicationMenu implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private JTextField iteration;

	public RegistrationMenu() {
		super("Registration");
//		JPanel jp = new JPanel();
//		jp.setLayout(new GridLayout(7,2));
//		jp.setPreferredSize(new Dimension(300,130));
//		
//		
//		
//		jp.add(new JLabel("Iteration"));
//		iteration = new JTextField("200");
//		jp.add(iteration);
		
		
		
		
		
		
		JMenuItem start = new JMenuItem("Start");
		start.addActionListener(this);
		start.setActionCommand("start");
		
//		JMenuItem step_by_step = new JMenuItem("Options");
//		step_by_step.addActionListener(this);
//		step_by_step.setActionCommand("options");
//		
		add(start);
//		add(step_by_step);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("start".equals(e.getActionCommand())){
			StartMenu newFrame = new StartMenu();
			newFrame.setVisible(true);
//		}else if("options".equals(e.getActionCommand())){
//			Options options = new Options();
//			cityFrame.setVisible(true);
		}
		
	}
	
	

}

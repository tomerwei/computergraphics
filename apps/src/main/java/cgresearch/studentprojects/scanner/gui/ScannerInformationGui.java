package cgresearch.studentprojects.scanner.gui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cgresearch.ui.IApplicationControllerGui;

public class ScannerInformationGui extends IApplicationControllerGui {

	/**
   * 
   */
  private static final long serialVersionUID = -3568819010033739694L;

  public ScannerInformationGui() {
		
		setLayout(new GridLayout(0, 1));
		
		JPanel motor1Panel = new JPanel();
		motor1Panel.setLayout(new BoxLayout(motor1Panel, BoxLayout.Y_AXIS));
		motor1Panel.setBorder(BorderFactory.createTitledBorder("Information"));
		add(motor1Panel);
		
	}
	
	@Override
	public String getName() {
		return "Scanner Info";	
	}
	
	

}

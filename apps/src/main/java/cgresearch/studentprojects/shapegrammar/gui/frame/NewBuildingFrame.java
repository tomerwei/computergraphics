package cgresearch.studentprojects.shapegrammar.gui.frame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import cgresearch.core.logging.Logger;
import cgresearch.studentprojects.shapegrammar.fileio.BuildingReader;
import cgresearch.studentprojects.shapegrammar.generator.BuildingGenerator;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettingManager;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;


/**
 * The Class NewBuildingFrame is the new Building window.
 * @author Thorben Watzl
 */
public class NewBuildingFrame extends JFrame implements ActionListener{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7661336459265178067L;
	
	/**
	 * Action Commands.
	 */
	public static final String ACTION_COMMAND_CREATE = "ACTION_COMMAND_CREATE";
	
	/** The buildings selection. */
	private JComboBox<String> buildingsSelection;
	
	/** The height input. */
	private JTextField heightInput;
	
	/** The width input. */
	private JTextField widthInput;
	
	/** The lendth input. */
	private JTextField lengthInput;
	
	/** The x input. */
	private JTextField xInput;
	
	/** The z input. */
	private JTextField zInput;
	
	/**
	 * Instantiates a new new building frame.
	 */
	public NewBuildingFrame(){
		super("New Building");
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(7,2));
		jp.setPreferredSize(new Dimension(300,130));

		jp.add(new JLabel("Select Building"));
		buildingsSelection = new JComboBox<String>(getBuildingsArray());
		jp.add(buildingsSelection);
		
		jp.add(new JLabel("Height"));
		heightInput = new JTextField("4");
		jp.add(heightInput);
		
		jp.add(new JLabel("Width"));
		widthInput = new JTextField("12");
		jp.add(widthInput);
		
		jp.add(new JLabel("Lendth"));
		lengthInput = new JTextField("6");
		jp.add(lengthInput);
		
		jp.add(new JLabel("Position X"));
		xInput = new JTextField("0");
		jp.add(xInput);
		
		jp.add(new JLabel("Position Z"));
		zInput = new JTextField("0");
		jp.add(zInput);
		
		JButton createButton = new JButton("Create");
		createButton.addActionListener(this);
		createButton.setActionCommand(ACTION_COMMAND_CREATE);
		jp.add(createButton);
		
		getContentPane().add(jp);
		pack();
	}
	
	/**
	 * Gets the buildings array.
	 *
	 * @return the buildings array
	 */
	private String[] getBuildingsArray(){
		BuildingReader buildingReader = new BuildingReader();
		List<String> buildingList = buildingReader.readBuildings();
		return buildingList.toArray(new String[buildingList.size()]);
	}

	/* (nicht-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(NewBuildingFrame.ACTION_COMMAND_CREATE)){
			try{
				BuildingGenerator buildingGenerator = new BuildingGenerator();
				String buildingDir = (String)buildingsSelection.getSelectedItem();
				
				String height = (String)heightInput.getText();
				double doubleHeight = Double.parseDouble(height);
				
				String width = (String)widthInput.getText();
				double doubleWidth = Double.parseDouble(width);
				
				String length = (String)lengthInput.getText();
				double doubleLength = Double.parseDouble(length);
				
				String x = (String)xInput.getText();
				double doubleX = Double.parseDouble(x);
				
				String z = (String)zInput.getText();
				double doubleZ = -(Double.parseDouble(z));
				
				buildingGenerator.generateBuilding(buildingDir, doubleWidth, doubleHeight, doubleLength, doubleX, doubleZ);
				printBuildingSettings();
				setVisible(false);
				dispose();
			}catch(NumberFormatException exception){
				Logger.getInstance().message("Input is wrong: " + exception);
			}
		}
	}

	/**
	 * Creates the rule tree.
	 */
	private void printBuildingSettings() {
		BuildingSettings buildingSettings = BuildingSettingManager.getInstance().getActualSettings();
		Logger.getInstance().message("\nSetup Buildingsettings");
		Logger.getInstance().message("----------------------");
		Logger.getInstance().message("Width: " + buildingSettings.getWidth());
		Logger.getInstance().message("Height: " + buildingSettings.getHeight());
		Logger.getInstance().message("Lendth: " + buildingSettings.getLength());
		Logger.getInstance().message("Position: (" + buildingSettings.getX() + "," + (-1*buildingSettings.getZ())+")");
		Logger.getInstance().message("BuildingDir: " + buildingSettings.getBuildingDir());
		Logger.getInstance().message("----------------------");
	}
}

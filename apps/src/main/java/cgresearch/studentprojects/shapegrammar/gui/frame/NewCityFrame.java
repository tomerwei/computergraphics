package cgresearch.studentprojects.shapegrammar.gui.frame;

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
import javax.swing.JPanel;
import javax.swing.JButton;

import cgresearch.studentprojects.shapegrammar.fileio.BuildingReader;
import cgresearch.studentprojects.shapegrammar.settings.CitySettingManager;
import cgresearch.studentprojects.shapegrammar.settings.CitySettings;

import javax.swing.JList;

import cgresearch.core.logging.Logger;

public class NewCityFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5328290784436897651L;
	
	/**
	 * Action Commands.
	 */
	public static final String ACTION_COMMAND_CREATE = "ACTION_COMMAND_CREATE";
	
	private JTextField numberBuildingsXInput;
	private JTextField numberBuildingsZInput;
	private JTextField minWidth;
	private JTextField maxWidth;
	private JTextField minHeight;
	private JTextField maxHeight;
	private JTextField minLength;
	private JTextField maxLength;
	private JList<String> list;
	private CitySettings citySettings;
	
	public NewCityFrame() {
		super("New City");
		CitySettingManager.getInstance().createNewCitySettings();
		citySettings = CitySettingManager.getInstance().getActualSettings();
		JPanel MainPanel = new JPanel();
		MainPanel.setPreferredSize(new Dimension(305, 355));
		MainPanel.setBounds(0, 0, 305, 329);
		getContentPane().add(MainPanel);
		MainPanel.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_4.setBounds(10, 8, 285, 53);
		MainPanel.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblBuildingCounter = new JLabel("Numbers of Buildings");
		lblBuildingCounter.setBounds(10, 8, 99, 14);
		panel_4.add(lblBuildingCounter);
		
		numberBuildingsXInput = new JTextField();
		numberBuildingsXInput.setBounds(32, 25, 42, 20);
		panel_4.add(numberBuildingsXInput);
		numberBuildingsXInput.setColumns(10);
		
		JLabel lblX = new JLabel("X:");
		lblX.setBounds(10, 28, 23, 14);
		panel_4.add(lblX);
		
		JLabel lblY = new JLabel("Z:");
		lblY.setBounds(88, 28, 23, 14);
		panel_4.add(lblY);
		
		numberBuildingsZInput = new JTextField();
		numberBuildingsZInput.setColumns(10);
		numberBuildingsZInput.setBounds(110, 25, 42, 20);
		panel_4.add(numberBuildingsZInput);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 66, 89, 85);
		MainPanel.add(panel);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setLayout(null);
		
		JLabel lblWidth = new JLabel("House Width");
		lblWidth.setBounds(10, 11, 79, 14);
		panel.add(lblWidth);
		
		JLabel lblMin = new JLabel("Min:");
		lblMin.setBounds(10, 31, 46, 14);
		panel.add(lblMin);
		
		minWidth = new JTextField();
		minWidth.setBounds(35, 28, 36, 20);
		panel.add(minWidth);
		minWidth.setColumns(10);
		
		JLabel lblMax = new JLabel("Max:");
		lblMax.setBounds(10, 59, 46, 14);
		panel.add(lblMax);
		
		maxWidth = new JTextField();
		maxWidth.setBounds(35, 56, 36, 20);
		panel.add(maxWidth);
		maxWidth.setColumns(10);
		
		JLabel lblBuildingRules = new JLabel("Building Rules (Multiple Selection)");
		lblBuildingRules.setBounds(10, 163, 209, 14);
		MainPanel.add(lblBuildingRules);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(109, 66, 89, 85);
		MainPanel.add(panel_1);
		
		JLabel lblHouseHeight = new JLabel("House Height");
		lblHouseHeight.setBounds(10, 11, 79, 14);
		panel_1.add(lblHouseHeight);
		
		JLabel label_1 = new JLabel("Min:");
		label_1.setBounds(10, 31, 46, 14);
		panel_1.add(label_1);
		
		minHeight = new JTextField();
		minHeight.setColumns(10);
		minHeight.setBounds(35, 28, 36, 20);
		panel_1.add(minHeight);
		
		JLabel label_2 = new JLabel("Max:");
		label_2.setBounds(10, 59, 46, 14);
		panel_1.add(label_2);
		
		maxHeight = new JTextField();
		maxHeight.setColumns(10);
		maxHeight.setBounds(35, 56, 36, 20);
		panel_1.add(maxHeight);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBounds(208, 66, 89, 85);
		MainPanel.add(panel_3);
		
		JLabel lblHouseLendth = new JLabel("House Lendth");
		lblHouseLendth.setBounds(10, 11, 79, 14);
		panel_3.add(lblHouseLendth);
		
		JLabel label_4 = new JLabel("Min:");
		label_4.setBounds(10, 31, 46, 14);
		panel_3.add(label_4);
		
		minLength = new JTextField();
		minLength.setColumns(10);
		minLength.setBounds(35, 28, 36, 20);
		panel_3.add(minLength);
		
		JLabel label_5 = new JLabel("Max:");
		label_5.setBounds(10, 59, 46, 14);
		panel_3.add(label_5);
		
		maxLength = new JTextField();
		maxLength.setColumns(10);
		maxLength.setBounds(35, 56, 36, 20);
		panel_3.add(maxLength);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(10, 321, 89, 23);
		btnCreate.addActionListener(this);
		btnCreate.setActionCommand(ACTION_COMMAND_CREATE);
		MainPanel.add(btnCreate);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(10, 177, 285, 139);
		MainPanel.add(panel_2);
		panel_2.setLayout(null);
		
		list = new JList<String>();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		list.setBounds(0, 0, 285, 139);
		BuildingReader buildingReader = new BuildingReader();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String building : buildingReader.readBuildings()) {
			listModel.addElement(building);
		}
		list.setModel(listModel);
		panel_2.add(list);
		
		pack();
	}
	/* (nicht-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_CREATE)){
			try{
				List<String> selectedBuildings = list.getSelectedValuesList();
				if(selectedBuildings.size() > 0){
					Logger.getInstance().message("Start city generation");
					CitySettingManager.getInstance().startChanges();
					citySettings.setNumberBuildingsXInput(Double.parseDouble(numberBuildingsXInput.getText()));
					citySettings.setNumberBuildingsZInput(Double.parseDouble(numberBuildingsZInput.getText()));
					citySettings.setMinWidth(Double.parseDouble(minWidth.getText()));
					citySettings.setMaxWidth(Double.parseDouble(maxWidth.getText()));
					citySettings.setMinHeight(Double.parseDouble(minHeight.getText()));
					citySettings.setMaxHeight(Double.parseDouble(maxHeight.getText()));
					citySettings.setMinLength(Double.parseDouble(minLength.getText()));
					citySettings.setMaxLength(Double.parseDouble(maxLength.getText()));
					citySettings.setBuildingRulesDir(selectedBuildings);
					CitySettingManager.getInstance().notifyChanges();
					setVisible(false);
					dispose();
				}else{
					Logger.getInstance().message("Input is wrong: Please select building rules!");
				}
			}catch(NumberFormatException exception){
				Logger.getInstance().message("Input is wrong: " + exception);
			}
		}
	}
}

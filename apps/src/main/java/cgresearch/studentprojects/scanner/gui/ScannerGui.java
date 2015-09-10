package cgresearch.studentprojects.scanner.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import cgresearch.ui.IApplicationControllerGui;
import jssc.SerialPortList;

public class ScannerGui extends IApplicationControllerGui {

  /**
	 * 
	 */
  private static final long serialVersionUID = 2409120488870904297L;
  /**
   * Action Commands.
   */
  public static final String ACTION_COMMAND_INIT = "ACTION_COMMAND_INIT";
  public static final String ACTION_COMMAND_TEST = "ACTION_COMMAND_TEST";
  public static final String ACTION_COMMAND_SCAN = "ACTION_COMMAND_SCAN";
  public static final String ACTION_COMMAND_LASER_ON =
      "ACTION_COMMAND_LASER_ON";
  public static final String ACTION_COMMAND_LASER_OFF =
      "ACTION_COMMAND_LASER_OFF";
  public static final String ACTION_COMMAND_SAVE_FILE =
      "ACTION_COMMAND_SAVE_FILE";
  public static final String ACTION_COMMAND_LOAD_FILE =
      "ACTION_COMMAND_LOAD_FILE";
  public static final String ACTION_COMMAND_DEBUG_ANGLE =
      "ACTION_COMMAND_DEBUG_ANGLE";
  public static final String ACTION_COMMAND_DEBUG_HEIGHT =
      "ACTION_COMMAND_DEBUG_HEIGHT";

  private String[] ports;
  private JComboBox<String> portsComboBox;

  private JTextField textFieldPointsInLine;
  private JTextField textFieldAngle;

  private JTextField textFieldDebugAngle;
  private JTextField textFieldDebugHeight;
  private JTextField textFieldConfigLoad;
  private JTextField textFieldConfigSave;

  public ScannerGui(ActionListener scannerApplication) {
    ports = SerialPortList.getPortNames();

    setLayout(new GridLayout(0, 1));

    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
    configPanel.setPreferredSize(new Dimension(10, 50));
    configPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
    add(configPanel);

    JPanel scanPanel = new JPanel();
    scanPanel.setLayout(new BoxLayout(scanPanel, BoxLayout.Y_AXIS));
    scanPanel.setBorder(BorderFactory.createTitledBorder("Scanning"));
    add(scanPanel);

    JPanel debugPanel = new JPanel();
    debugPanel.setLayout(new BoxLayout(debugPanel, BoxLayout.Y_AXIS));
    debugPanel.setBorder(BorderFactory.createTitledBorder("Debugging"));
    add(debugPanel);

    // Debug - move 5000 steps
    JButton buttonTest = new JButton("Move 5000 Steps up");
    buttonTest.addActionListener(scannerApplication);
    buttonTest.setActionCommand(ACTION_COMMAND_TEST);
    debugPanel.add(buttonTest);

    // Debug - Laser on
    JButton buttonLaserOn = new JButton("Laser On");
    buttonLaserOn.addActionListener(scannerApplication);
    buttonLaserOn.setActionCommand(ACTION_COMMAND_LASER_ON);
    debugPanel.add(buttonLaserOn);

    // Debug - Laser off
    JButton buttonLaserOff = new JButton("Laser Off");
    buttonLaserOff.addActionListener(scannerApplication);
    buttonLaserOff.setActionCommand(ACTION_COMMAND_LASER_OFF);
    debugPanel.add(buttonLaserOff);

    // Debug - Height
    JPanel panelDebugHeight = new JPanel();
    textFieldDebugHeight = new JTextField(3);
    textFieldDebugHeight.setText("10");
    JLabel labelDebugHeight = new JLabel("Height in mm: ");
    labelDebugHeight.setPreferredSize(new Dimension(120, 25));
    panelDebugHeight.add(labelDebugHeight);
    panelDebugHeight.add(textFieldDebugHeight);
    debugPanel.add(panelDebugHeight);

    JButton buttonDebugHeight = new JButton("Move Vertical");
    buttonDebugHeight.addActionListener(scannerApplication);
    buttonDebugHeight.setActionCommand(ACTION_COMMAND_DEBUG_HEIGHT);
    panelDebugHeight.add(buttonDebugHeight);

    // Debug - Rotation/angle
    JPanel panelDebugAngle = new JPanel();
    textFieldDebugAngle = new JTextField(3);
    textFieldDebugAngle.setText("90");
    JLabel labelDebugAngle = new JLabel("Angle in deg: ");
    labelDebugAngle.setPreferredSize(new Dimension(120, 25));
    panelDebugAngle.add(labelDebugAngle);
    panelDebugAngle.add(textFieldDebugAngle);
    debugPanel.add(panelDebugAngle);

    JButton buttonDebugAngle = new JButton("Rotate deg");
    buttonDebugAngle.addActionListener(scannerApplication);
    buttonDebugAngle.setActionCommand(ACTION_COMMAND_DEBUG_ANGLE);
    panelDebugAngle.add(buttonDebugAngle);

    // Config
    JButton buttonInitScanner = new JButton("Initalize Scanner");
    buttonInitScanner.addActionListener(scannerApplication);
    buttonInitScanner.setActionCommand(ACTION_COMMAND_INIT);
    configPanel.add(buttonInitScanner);

    JButton buttonScan = new JButton("Scan");
    buttonScan.addActionListener(scannerApplication);
    buttonScan.setActionCommand(ACTION_COMMAND_SCAN);
    scanPanel.add(buttonScan);

    JPanel panelPointsInLine = new JPanel();
    textFieldPointsInLine = new JTextField(3);
    textFieldPointsInLine.setText("50");
    JLabel labelPointsInLine = new JLabel("Height in mm: ");
    labelPointsInLine.setPreferredSize(new Dimension(120, 25));
    panelPointsInLine.add(labelPointsInLine);
    panelPointsInLine.add(textFieldPointsInLine);
    scanPanel.add(panelPointsInLine);

    JPanel panelAngle = new JPanel();
    textFieldAngle = new JTextField(3);
    textFieldAngle.setText("8");
    JLabel labelAngle = new JLabel("Angle: ");
    labelAngle.setPreferredSize(new Dimension(120, 25));
    panelAngle.add(labelAngle);
    panelAngle.add(textFieldAngle);
    scanPanel.add(panelAngle);

    // Config - Load
    JPanel panelConfigSave = new JPanel();
    textFieldConfigSave = new JTextField(20);
    textFieldConfigSave.setText("point_cloud_scan.ascii");
    JLabel labelConfigSave = new JLabel("Save file: ");
    labelConfigSave.setPreferredSize(new Dimension(120, 25));
    panelConfigSave.add(labelConfigSave);
    panelConfigSave.add(textFieldConfigSave);
    scanPanel.add(panelConfigSave);

    JButton buttonConfigSave = new JButton("Save");
    buttonConfigSave.addActionListener(scannerApplication);
    buttonConfigSave.setActionCommand(ACTION_COMMAND_SAVE_FILE);
    panelConfigSave.add(buttonConfigSave);

    // Config - Load
    JPanel panelConfigLoad = new JPanel();
    textFieldConfigLoad = new JTextField(20);
    textFieldConfigLoad.setText("point_cloud_scan.ascii");
    JLabel labelConfigLoad = new JLabel("Load file: ");
    labelConfigLoad.setPreferredSize(new Dimension(120, 25));
    panelConfigLoad.add(labelConfigLoad);
    panelConfigLoad.add(textFieldConfigLoad);
    scanPanel.add(panelConfigLoad);

    JButton buttonConfigLoad = new JButton("Load");
    buttonConfigLoad.addActionListener(scannerApplication);
    buttonConfigLoad.setActionCommand(ACTION_COMMAND_LOAD_FILE);
    panelConfigLoad.add(buttonConfigLoad);

    portsComboBox = new JComboBox<String>(ports);
    portsComboBox.setMaximumSize(new Dimension(300, 25));
    portsComboBox.setPreferredSize(new Dimension(100, 25));
    configPanel.add(portsComboBox);
  }

  public int getPointsPerLine() {
    try {
      return Integer.parseInt(textFieldPointsInLine.getText());
    } catch (NumberFormatException e) {
      return 1;
    }
  }

  public int getAngle() {
    try {
      return Integer.parseInt(textFieldAngle.getText()) * 2;
    } catch (NumberFormatException e) {
      return 8;
    }
  }

  public int getDebugAngle() {
    try {
      return Integer.parseInt(textFieldDebugAngle.getText());
    } catch (NumberFormatException e) {
      return 8;
    }
  }

  public float getDebugHeight() {
    try {
      return Float.parseFloat(textFieldDebugHeight.getText());
    } catch (NumberFormatException e) {
      return 10;
    }
  }

  public String getLoadFilename() {
    return textFieldConfigLoad.getText();
  }

  public String getSaveFilename() {
    return textFieldConfigSave.getText();
  }

  public String getPort() {
    return (String) portsComboBox.getSelectedItem();
  }

  @Override
  public String getName() {
    return "3D Scanner";
  }

}

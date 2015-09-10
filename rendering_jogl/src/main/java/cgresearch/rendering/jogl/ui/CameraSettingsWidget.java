package cgresearch.rendering.jogl.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.camera.Camera.ControllerType;

/**
 * Settings for the camera controller.
 * 
 * @author Philipp Jenke
 *
 */
public class CameraSettingsWidget extends JFrame implements ActionListener {

	/**
	 * Constants
	 */
	private static final long serialVersionUID = 1L;
	private static String ACTION_COMMAND_CAMERA_CONTROLLER = "ACTION_COMMAND_CAMERA_CONTROLLER";
	private static String ACTION_COMMAND_CLEAR_KEYPOINTS = "ACTION_COMMAND_CLEAR_KEYPOINTS";
	private static String ACTION_COMMAND_ADD_KEYPOINT = "ACTION_COMMAND_ADD_KEYPOINT";
	private static String ACTION_COMMAND_SHOW_CAMERA_PATH = "ACTION_COMMAND_SHOW_CAMERA_PATH";
	private static String ACTION_COMMAND_LOAD_CAMERA_PATH = "ACTION_COMMAND_LOAD_CAMERA_PATH";
	private static String ACTION_COMMAND_SAVE_CAMERA_PATH = "ACTION_COMMAND_SAVE_CAMERA_PATH";

	/**
	 * Reference to GUI components.
	 */
	private JComboBox<ControllerType> comboBox = null;
	private JCheckBox checkBoxShowCameraPath = null;

	/**
	 * Constructor.
	 */
	public CameraSettingsWidget() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		add(panel);

		// Contoller selection
		JLabel labelController = new JLabel("Controller:");
		panel.add(labelController);
		comboBox = new JComboBox<Camera.ControllerType>();
		for (ControllerType ct : ControllerType.values())
			comboBox.addItem(ct);
		comboBox.setActionCommand(ACTION_COMMAND_CAMERA_CONTROLLER);
		comboBox.addActionListener(this);
		comboBox.setSelectedItem(Camera.getInstance()
				.getCurrentControllerType());
		panel.add(comboBox);

		JButton buttonLoadCameraPath = new JButton("Load camera path");
		buttonLoadCameraPath.addActionListener(this);
		buttonLoadCameraPath.setActionCommand(ACTION_COMMAND_LOAD_CAMERA_PATH);
		panel.add(buttonLoadCameraPath);

		JButton buttonSaveCameraPath = new JButton("Save camera path");
		buttonSaveCameraPath.addActionListener(this);
		buttonSaveCameraPath.setActionCommand(ACTION_COMMAND_SAVE_CAMERA_PATH);
		panel.add(buttonSaveCameraPath);

		JButton buttonClearKeyPoints = new JButton("Clear key points");
		buttonClearKeyPoints.addActionListener(this);
		buttonClearKeyPoints.setActionCommand(ACTION_COMMAND_CLEAR_KEYPOINTS);
		panel.add(buttonClearKeyPoints);

		JButton buttonAddKeyPoints = new JButton("Add key point");
		buttonAddKeyPoints.addActionListener(this);
		buttonAddKeyPoints.setActionCommand(ACTION_COMMAND_ADD_KEYPOINT);
		panel.add(buttonAddKeyPoints);

		JLabel labelShowCameraPath = new JLabel("Show camera path");
		panel.add(labelShowCameraPath);

		checkBoxShowCameraPath = new JCheckBox();
		checkBoxShowCameraPath.setSelected(Camera.getInstance()
				.showCameraPath());
		checkBoxShowCameraPath.addActionListener(this);
		checkBoxShowCameraPath
				.setActionCommand(ACTION_COMMAND_SHOW_CAMERA_PATH);
		panel.add(checkBoxShowCameraPath);

		setSize(300, 160);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_CAMERA_CONTROLLER)) {
			Camera.ControllerType type = (ControllerType) comboBox
					.getSelectedItem();
			Camera.getInstance().setController(type);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_CLEAR_KEYPOINTS)) {
			Camera.getInstance().clearKeyPoints();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_ADD_KEYPOINT)) {
			Camera.getInstance()
					.appendKeyPoint(Camera.getInstance().getEye(),
							Camera.getInstance().getUp(),
							Camera.getInstance().getRef());
		} else if (e.getActionCommand().endsWith(
				ACTION_COMMAND_SHOW_CAMERA_PATH)) {
			Camera.getInstance().setShowCameraPath(
					checkBoxShowCameraPath.isSelected());
		} else if (e.getActionCommand().equals(ACTION_COMMAND_LOAD_CAMERA_PATH)) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			Camera.getInstance().loadCameraPathFromFile(filename);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SAVE_CAMERA_PATH)) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			Camera.getInstance().saveCameraPathToFile(filename);
		}
	}
}

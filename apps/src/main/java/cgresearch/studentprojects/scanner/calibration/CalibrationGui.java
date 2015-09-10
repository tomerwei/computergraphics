package cgresearch.studentprojects.scanner.calibration;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.primitives.IPrimitive;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;
import cgresearch.ui.IApplicationControllerGui;

public class CalibrationGui extends IApplicationControllerGui implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action commands
	 */
	private final String ACTION_COMMAND_LOAD = "ACTION_COMMAND_LOAD";
	private final String ACTION_COMMAND_CREATE_POINT_CLOUD = "ACTION_COMMANDCREATE_POINT_CLOUD";
	private final String ACTION_COMMAND_DEBUG_PRINT_ENERGY_FUNCTION = "ACTION_COMMAND_DEBUG_PRINT_ENERGY_FUNCTION";
	private final String ACTION_COMMAND_X = "ACTION_COMMAND_X";
	private final String ACTION_COMMAND_Y = "ACTION_COMMAND_Y";
	private final String ACTION_COMMAND_Z = "ACTION_COMMAND_Z";
	private final String ACTION_COMMAND_STEP = "ACTION_COMMAND_STEP";

	/**
	 * Calibration object.
	 */
	private Calibration calibration = new Calibration();
	private CgNode cylinderTransformtionNode = null;
	private JTextField textFieldX, textFieldY, textFieldZ;

	/**
	 * Constructor
	 */
	public CalibrationGui() {
		setLayout(new GridLayout(0, 1));

		// X
		textFieldX = new JTextField(calibration.registrationData.dS.get(0) + "");
		textFieldX.setActionCommand(ACTION_COMMAND_X);
		textFieldX.addActionListener(this);
		add(textFieldX);

		// Y
		textFieldY = new JTextField(calibration.registrationData.dS.get(1) + "");
		textFieldY.setActionCommand(ACTION_COMMAND_Y);
		textFieldY.addActionListener(this);
		add(textFieldY);

		// Z
		textFieldZ = new JTextField(calibration.registrationData.dS.get(2) + "");
		textFieldZ.setActionCommand(ACTION_COMMAND_Z);
		textFieldZ.addActionListener(this);
		add(textFieldZ);

		// Load data
		JButton buttonLoad = new JButton("Load file.");
		buttonLoad.setActionCommand(ACTION_COMMAND_LOAD);
		buttonLoad.addActionListener(this);
		add(buttonLoad);

		// Create point cloud
		JButton buttonCreatePointCloud = new JButton("Create point cloud.");
		buttonCreatePointCloud
				.setActionCommand(ACTION_COMMAND_CREATE_POINT_CLOUD);
		buttonCreatePointCloud.addActionListener(this);
		add(buttonCreatePointCloud);

		// [DEBUG] Print energy function
		JButton buttonPrintEnergyFunction = new JButton(
				"[DEBUG] Print energy function.");
		buttonPrintEnergyFunction
				.setActionCommand(ACTION_COMMAND_DEBUG_PRINT_ENERGY_FUNCTION);
		buttonPrintEnergyFunction.addActionListener(this);
		add(buttonPrintEnergyFunction);

		// Optimization step
		JButton buttonOptimizationStep = new JButton("Optimization Step.");
		buttonOptimizationStep.setActionCommand(ACTION_COMMAND_STEP);
		buttonOptimizationStep.addActionListener(this);
		add(buttonOptimizationStep);

		calibration.loadDataset("scanner/duck_raw2.txt");
		// calibration.loadDataset("scanner/measuring_can_center.txt");
		calibration.createRegisteredPointCloud();

	}

	@Override
	public void init() {
		// Create a node for the calibration point cloud
		CgNode calibrationNode = new CgNode(null, "Calibration");
		getRootNode().addChild(calibrationNode);
		calibrationNode.addChild(new CgNode(calibration.getPointCloud(),
				"Scanner data"));
		calibration.getPointCloud().getMaterial()
				.setShaderId(Material.SHADER_COLOR);

		IPrimitive cylinder = calibration.getCalibrationObject();
		cylinder.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR1);
		cylinder.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);

		Transformation transformation = new Transformation();
		cylinderTransformtionNode = new CgNode(transformation, "transformation");
		CgNode cylinderNode = new CgNode(cylinder, "Registration cylinder");
		cylinderNode.setVisible(false);
		cylinderTransformtionNode.addChild(cylinderNode);
		calibrationNode.addChild(cylinderTransformtionNode);
	}

	@Override
	public String getName() {
		return "Calibration";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_LOAD)) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			calibration.loadDataset(filename);
		} else if (e.getActionCommand().equals(
				ACTION_COMMAND_CREATE_POINT_CLOUD)) {
			calibration.createRegisteredPointCloud();
		} else if (e.getActionCommand().equals(
				ACTION_COMMAND_DEBUG_PRINT_ENERGY_FUNCTION)) {
			double[] gradient = calibration.computeGradient(calibration.h);
			Logger.getInstance().message(
					"Enery: " + calibration.evaluateEnergyFunction());
			Logger.getInstance().message(
					"Gradient: " + gradient[0] + ", " + gradient[1] + ", "
							+ gradient[2] + ", " + gradient[3]);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_X)) {
			calibration.registrationData.dS.set(0,
					Double.parseDouble(textFieldX.getText()));
			calibration.createRegisteredPointCloud();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_Y)) {
			calibration.registrationData.dS.set(1,
					Double.parseDouble(textFieldY.getText()));
			calibration.createRegisteredPointCloud();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_Z)) {
			calibration.registrationData.dS.set(2,
					Double.parseDouble(textFieldZ.getText()));
			calibration.createRegisteredPointCloud();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_STEP)) {
			calibration.optimzationStep();
			Transformation transformation = (Transformation) cylinderTransformtionNode
					.getContent();
			transformation.reset();
			transformation.addTranslation(calibration.calibrationObject
					.getPoint());
			calibration.createRegisteredPointCloud();
		}
	}
}

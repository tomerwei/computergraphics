package cgresearch.apps.simulation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cgresearch.projects.simulation.Simulation;
import cgresearch.projects.simulation.solver.Solver;
import cgresearch.ui.IApplicationControllerGui;

public class SimulationGui extends IApplicationControllerGui implements
		ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action Commands.
	 */
	private final String ACTION_COMMAND_SIMULATION_STEP = "ACTION_COMMAND_SIMULATION_STEP";
	private final String ACTION_COMMAND_SIMULATION_RESET = "ACTION_COMMAND_SIMULATION_RESET";
	private final String ACTION_COMMAND_SIMULATION_RUN = "ACTION_COMMAND_SIMULATION_RUN";
	private final String ACTION_COMMAND_SIMULATION_STOP = "ACTION_COMMAND_SIMULATION_STOP";
	private final String ACTION_COMMAND_SIMULATION_STEP_SIZE = "ACTION_COMMAND_SIMULATION_STEP_SIZE";
	private final String ACTION_COMMAND_SIMULATION_SOLVER = "ACTION_COMMAND_SIMULATION_SOLVER";

	/**
	 * Swing components.
	 */
	private JTextField textFieldStepSize;
	private JComboBox<String> comboBoxSolver;

	/**
	 * Reference to the simulation object.
	 */
	private final Simulation simulator;

	/**
	 * Constructor.
	 */
	public SimulationGui(Simulation simulator) {
		this.simulator = simulator;

		setLayout(new GridLayout(0, 2));

		JButton buttonReset = new JButton("Reset");
		buttonReset.addActionListener(this);
		buttonReset.setActionCommand(ACTION_COMMAND_SIMULATION_RESET);
		add(buttonReset);

		JButton buttonSimulationStep = new JButton("Step");
		buttonSimulationStep.addActionListener(this);
		buttonSimulationStep.setActionCommand(ACTION_COMMAND_SIMULATION_STEP);
		add(buttonSimulationStep);

		JButton buttonRun = new JButton("Run");
		buttonRun.addActionListener(this);
		buttonRun.setActionCommand(ACTION_COMMAND_SIMULATION_RUN);
		add(buttonRun);

		JButton buttonStop = new JButton("Stop");
		buttonStop.addActionListener(this);
		buttonStop.setActionCommand(ACTION_COMMAND_SIMULATION_STOP);
		add(buttonStop);

		JLabel labelStepSize = new JLabel("Step size:");
		add(labelStepSize);

		textFieldStepSize = new JTextField("" + simulator.getStepSize());
		textFieldStepSize.addKeyListener(this);
		textFieldStepSize.setActionCommand(ACTION_COMMAND_SIMULATION_STEP_SIZE);
		add(textFieldStepSize);

		JLabel labelSolver = new JLabel("Solver:");
		add(labelSolver);

		comboBoxSolver = new JComboBox<String>();
		comboBoxSolver.addItem(Solver.SolverType.EULER.toString());
		comboBoxSolver.addItem(Solver.SolverType.HEUN.toString());
		comboBoxSolver.setSelectedItem(simulator.getSolver().getType()
				.toString());
		comboBoxSolver.setActionCommand(ACTION_COMMAND_SIMULATION_SOLVER);
		comboBoxSolver.addActionListener(this);
		add(comboBoxSolver);
	}

	@Override
	public String getName() {
		return "Cloth simulation";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_SIMULATION_STEP)) {
			simulator.simulationStep();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SIMULATION_RESET)) {
			simulator.reset();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SIMULATION_RUN)) {
			simulator.run();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SIMULATION_STOP)) {
			simulator.stop();
		} else if (e.getActionCommand()
				.equals(ACTION_COMMAND_SIMULATION_SOLVER)) {
			simulator.setSolver(Solver.SolverType
					.valueOf((String) (comboBoxSolver.getSelectedItem())));
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		simulator.setStepSize(Double.parseDouble(textFieldStepSize.getText()));
	}
}

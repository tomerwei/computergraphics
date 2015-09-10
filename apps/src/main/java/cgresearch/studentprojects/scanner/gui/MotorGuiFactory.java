/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.studentprojects.scanner.motors.StepperMotor;


/**
 * Factory class for a motor GUI.
 * 
 * @author Philipp Jenke
 * 
 */
public class MotorGuiFactory implements Observer {

    /**
     * Reference to the represented motor.
     */
    private final StepperMotor motor;

    /**
     * Enabled checkbox.
     */
    private ConnectionLabel checkBoxIsEnabled = null;

    /**
     * Represents the number of steps to be moved.
     */
    private JTextField textFieldMove = null;

    /**
     * Change the max velocity;
     */
    private JButton buttonMaxVelocity;

    /**
     * Change the acceleration;
     */
    private JButton buttonAcceleration;

    /**
     * Constructor.
     */
    public MotorGuiFactory(StepperMotor motor) {
        this.motor = motor;
        motor.addObserver(this);
    }

    /**
     * Create the GUI for the motor.
     */
    public JPanel createMotorGui(String text) {

        // Create action listener for the motor
        MotorGuiListener motorGuiListener = new MotorGuiListener(motor, this);

        // Create the panel for motor GUI
        JPanel panelMotor = new JPanel();
        BoxLayout layout = new BoxLayout(panelMotor, BoxLayout.Y_AXIS);
        panelMotor.setLayout(layout);

        panelMotor.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black), text));

        // Enable the motor
        JButton buttonEnable = new JButton("Enable motor");
        buttonEnable.setActionCommand(MotorGuiListener.ENABLE);
        buttonEnable.addActionListener(motorGuiListener);
        buttonEnable.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(buttonEnable);

        // Disable the motor
        JButton buttonDisable = new JButton("Disable motor");
        buttonDisable.setActionCommand(MotorGuiListener.DISABLE);
        buttonDisable.addActionListener(motorGuiListener);
        buttonDisable.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(buttonDisable);

        // Is connected
        checkBoxIsEnabled = new ConnectionLabel("enabled");
        checkBoxIsEnabled.setSelected(motor.isEnabled());
        checkBoxIsEnabled.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(checkBoxIsEnabled);

        // Move forward
        JButton buttonForward = new JButton("Forward");
        buttonForward.setActionCommand(MotorGuiListener.FORWARD);
        buttonForward.addActionListener(motorGuiListener);
        buttonForward.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(buttonForward);

        // Move backward
        JButton buttonBackward = new JButton("Backward");
        buttonBackward.setActionCommand(MotorGuiListener.BACKWARD);
        buttonBackward.addActionListener(motorGuiListener);
        buttonBackward.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(buttonBackward);

        // Move stop
        JButton buttonStop = new JButton("Stop");
        buttonStop.setActionCommand(MotorGuiListener.STOP);
        buttonStop.addActionListener(motorGuiListener);
        buttonStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMotor.add(buttonStop);

        // Move steps
        JPanel panelMoveSteps = new JPanel();
        panelMoveSteps
                .setLayout(new BoxLayout(panelMoveSteps, BoxLayout.Y_AXIS));
        panelMoveSteps.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Stepping"));
        JPanel panelMoveStepsInput = new JPanel();
        panelMoveStepsInput.setLayout(new BoxLayout(panelMoveStepsInput,
                BoxLayout.X_AXIS));
        JLabel labelMoveSteps = new JLabel("#steps:");
        panelMoveStepsInput.add(labelMoveSteps);
        textFieldMove = new JTextField("10");
        textFieldMove.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMoveStepsInput.add(textFieldMove);
        panelMotor.add(panelMoveStepsInput);
        JButton buttonMoveSteps = new JButton("Move steps");
        buttonMoveSteps.setActionCommand(MotorGuiListener.MOVE_STEPS);
        buttonMoveSteps.addActionListener(motorGuiListener);
        buttonMoveSteps.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMoveSteps.add(panelMoveStepsInput);
        panelMoveSteps.add(buttonMoveSteps);
        panelMotor.add(panelMoveSteps);

        // Motor info
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Info"));
        panelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel labelInfo = new JLabel(motor.toString());
        buttonAcceleration = new JButton();
        buttonAcceleration.addActionListener(motorGuiListener);
        buttonAcceleration.setActionCommand(MotorGuiListener.ACCELERATION);
        buttonMaxVelocity = new JButton();
        buttonMaxVelocity.addActionListener(motorGuiListener);
        buttonMaxVelocity.setActionCommand(MotorGuiListener.MAX_VELOCITY);
        setButtonTexts();
        panelInfo.add(buttonAcceleration);
        panelInfo.add(buttonMaxVelocity);
        panelInfo.add(labelInfo);
        panelMotor.add(panelInfo);

        return panelMotor;
    }

    /**
     * Set the captions on the buttons.
     */
    private void setButtonTexts() {
        buttonAcceleration.setText("acceleration: " + motor.getAcceleration()
                + " steps/sec^2");
        buttonMaxVelocity.setText("max velocity: " + motor.getMaxVelocity()
                + " steps/sec");
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable observable, Object event) {
        if (observable == motor) {
            checkBoxIsEnabled.setSelected(motor.isEnabled());
            setButtonTexts();
        }
    }

    /**
     * Return the number of steps to move set in the GUI.
     * 
     * @return
     */
    public int getNumberOfSteps() {
        return Integer.parseInt(textFieldMove.getText());
    }
}

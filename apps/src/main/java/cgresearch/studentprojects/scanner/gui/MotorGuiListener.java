/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import cgresearch.studentprojects.scanner.motors.StepperMotor;

/**
 * Action listener for the GUI of a stepper motor.
 * 
 * @author Philipp Jenke
 * 
 */
public class MotorGuiListener implements ActionListener {

    /**
     * Reference to the controlled stepper motor.
     */
    private final StepperMotor motor;

    /**
     * Action commands.
     */
    public static final String FORWARD = "FORWARD";
    public static final String BACKWARD = "BACKWARD";
    public static final String STOP = "STOP";
    public static final String ENABLE = "ENABLE";
    public static final String DISABLE = "DISABLE";
    public static final String MOVE_STEPS = "MOVE_STEPS";
    public static final String ACCELERATION = "ACCELERATION";
    public static final String MAX_VELOCITY = "MAX_VELOCITY";

    /**
     * Reference to the factory required to access the number of moves - nasty
     * TODO: design better!
     */
    private final MotorGuiFactory factory;

    /**
     * Constructor
     */
    public MotorGuiListener(StepperMotor motor, MotorGuiFactory factory) {
        this.motor = motor;
        this.factory = factory;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(ENABLE)) {
            motor.enable();
        } else if (event.getActionCommand().equals(DISABLE)) {
            motor.disable();
        } else if (event.getActionCommand().equals(FORWARD)) {
            motor.moveForward();
        } else if (event.getActionCommand().equals(BACKWARD)) {
            motor.moveBackward();
        } else if (event.getActionCommand().equals(STOP)) {
            motor.stop();
        } else if (event.getActionCommand().equals(MOVE_STEPS)) {
            motor.moveStepsForward(factory.getNumberOfSteps());
        } else if (event.getActionCommand().equals(ACCELERATION)) {
            String result = JOptionPane.showInputDialog(
                    "Acceleration in steps/sec^2: ", motor.getAcceleration());
            if (result != null) {
                int acceleration = Integer.parseInt(result);
                motor.setAcceleration(acceleration);
            }
        } else if (event.getActionCommand().equals(MAX_VELOCITY)) {
            String result = JOptionPane.showInputDialog(
                    "Max velocity in steps/sec: ", motor.getMaxVelocity());
            if (result != null) {
                int maxVelocity = Integer.parseInt(result);
                motor.setMaxVelocity(maxVelocity);
            }
        }
    }

}

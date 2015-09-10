/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.motors;

import java.util.Observable;

import cgresearch.core.logging.Logger;

import com.tinkerforge.BrickStepper;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * Container for all information about a motor and the contact to the physical
 * tinkerforge hardware.
 * 
 * @author Philipp Jenke
 * 
 */
public class StepperMotor extends Observable implements IStepperMotor {

	public static final int STEPS_FOR_CM = 1600;
	
    /**
     * UID of the hardware.
     */
    private final String uid;

    /**
     * Tinkerforge stepper motor object.
     */
    private BrickStepper stepper = null;

    /**
     * Step mode, use 1 for full steps.
     */
    private short stepMode = (short) 1;

    /**
     * Current used to "feed" the motor.
     */
    private int current = 800;

    /**
     * Maximum velocity of the motor. (500 default)
     */
    private int maxVelocity = 500; 

    /**
     * Acceleration of the motor. (1000 default)
     */
    private int acceleration = 1000;

    /**
     * Deceleration of the motor. (500 default)
     */
    private int deceleration = 500;

    /**
     * Flag to indicate that the motor is enabled.
     */
    private boolean isEnabled = false;

    /**
     * Constructor.
     * 
     * @param uid
     */
    public StepperMotor(String uid, IPConnection ipCon) {
        this.uid = uid;
        stepper = new BrickStepper(uid, ipCon);
    }

    /**
     * Setup the properties of the motor.
     */
    public void setup() {
        try {
            stepper.setMotorCurrent(current);
            stepper.setStepMode(stepMode);
            stepper.setMaxVelocity(maxVelocity);
            stepper.setSpeedRamping(acceleration, deceleration);
            // Adjust decay (do not change these values)
            // stepper.setSyncRect(false);
            // stepper.setDecay(65535);
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to setup stepper motor.");
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to setup stepper motor.");
            e.printStackTrace();
        }

    }

    /**
     * Getter.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Stop the motor.
     */
    public void stop() {
        try {
            stepper.stop();
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to stop stepper motor.");
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to stop stepper motor.");
            e.printStackTrace();
        }

        stateHasChanged();
    }

    /**
     * State of the motor has changed - inform observers.
     */
    private void stateHasChanged() {
        // Set update info to observers
        setChanged();
        notifyObservers();
    }

    /**
     * Get the current step counter
     */
    public int getStepCounter() {
    	try {
			return stepper.getCurrentPosition();
		} catch (TimeoutException e) {
			e.printStackTrace();
			return 0;
		} catch (NotConnectedException e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    /**
     * Set this position to Step zero
     */
    public void setStartPosition() {
    	try {
			stepper.setCurrentPosition(0);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Enable the motor - now the motor is ready for interaction.
     */
    public void enable() {
        try {
            stepper.enable();
            isEnabled = true;
            stateHasChanged();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to enable stepper motor.");
            e.printStackTrace();
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to enable stepper motor.");
            e.printStackTrace();
        }
    }

    /**
     * Disable the motor - motor cannot be controlled any more.
     */
    public void disable() {
        try {
            stepper.stop();
            stepper.disable();
            isEnabled = false;
            stateHasChanged();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to disable stepper motor.");
            e.printStackTrace();
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to diable stepper motor.");
            e.printStackTrace();
        }
    }

    /**
     * Run motor in forward direction.
     */
    public void moveForward() {
        try {
            stepper.driveForward();
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to set stepper motor forward.");
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to set stepper motor forward.");
            e.printStackTrace();
        }
    }

    /**
     * Run motor in backward direction.
     */
    public void moveBackward() {
        try {
            stepper.driveBackward();
        } catch (TimeoutException e) {
            Logger.getInstance().error("Failed to set stepper motor backward.");
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Logger.getInstance().error("Failed to set stepper motor backward.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String s = "<html>";
        s += "current: " + current / 1000.0 + " A" + "<br>";
        s += "step mode: " + stepMode + "<br>";
        s += " </html>";
        return s;
    }

    /**
     * Getter
     * 
     * @return True if the motor is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    public void moveCm(float cm) {
    	moveStepsForward((int)(cm*STEPS_FOR_CM));
    }
    
    /**
     * Move a given number of steps.
     * 
     * @param numberOfSteps
     *            Number of steps to be moved. Positive values move forward,
     *            negative values move backwards.
     */
    public void moveStepsForward(int numberOfSteps) {
        try {
            stepper.setSteps(numberOfSteps);
        } catch (TimeoutException e) {
            Logger.getInstance().error(
                    "Failed to set stepper motor forward steps.");
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Logger.getInstance().error(
                    "Failed to set stepper motor forward steps.");
            e.printStackTrace();
        }
    }
    
    public void waitForStepper() {
    	try {
			while(stepper.getRemainingSteps() < 0){}
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
    }

    /**
     * Setter
     * 
     * @param acceleration
     */
    public void setAcceleration(int newValue) {
        this.acceleration = newValue;
        this.deceleration = newValue;

        try {
            stepper.setSpeedRamping(acceleration, deceleration);
        } catch (TimeoutException e) {
        } catch (NotConnectedException e) {
        }
        stateHasChanged();
    }

    /**
     * Setter
     * 
     * @param velocity
     */
    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
        try {
            stepper.setMaxVelocity(maxVelocity);
        } catch (TimeoutException e) {
        } catch (NotConnectedException e) {
        }
        stateHasChanged();
    }

    /**
     * Getter
     */
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * Getter
     */
    public int getMaxVelocity() {
        return maxVelocity;
    }
}

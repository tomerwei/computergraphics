package homeautomation.actors;

import com.tinkerforge.BrickStepper;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import cgresearch.core.logging.Logger;

/**
 * Access to a Tinkerforge brick with a stepper motor.
 * 
 * @author Philipp Jenke
 *
 */
public class TinkerforgeStepperMotor {

  /**
   * Tinkerforge object.
   */
  private final BrickStepper stepperBrick;

  public TinkerforgeStepperMotor(String uid, IPConnection ipConnection) {
    this(uid, ipConnection, 2500, 2291, 30000, 30000, 30000);
  }

  public TinkerforgeStepperMotor(String uid, IPConnection ipConnection, int voltage, int current, int maxVelocity,
      int accelleration, int decelleration) {
    stepperBrick = new BrickStepper(uid, ipConnection);
    try {
      stepperBrick.setMinimumVoltage(voltage);
      stepperBrick.setMotorCurrent(current);
      stepperBrick.setMaxVelocity(maxVelocity);
      stepperBrick.setSpeedRamping(accelleration, decelleration);
      stepperBrick.setStepMode((short) 1);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error initializing stepper");
    }
  }

  /**
   * Move stepper motor for the given number of steps.
   */
  public void moveSteps(int steps) {
    try {
      stepperBrick.setSteps(steps);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error moving stepper");
    }
  }

  /**
   * Stop motor.
   */
  public void stop() {
    try {
      stepperBrick.fullBrake();
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error stopping stepper");
    }
  }

  /**
   * Enable motor.
   */
  public void enable() {
    try {
      stepperBrick.enable();
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error enabling stepper");
    }
  }

  /**
   * Disable motor.
   */
  public void disable() {
    try {
      stepperBrick.disable();
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error disabling stepper");
    }
  }

  public enum Axis {
    FORWARDS, BACKWARDS
  };

  public void move(Axis axis) {
    try {
      if (axis == Axis.FORWARDS) {
        stepperBrick.driveForward();
      } else {
        stepperBrick.driveBackward();
      }
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().message("Failed to move stepper motor.");
    }
  }
}

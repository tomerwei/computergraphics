package homeautomation.actors;

import com.tinkerforge.BrickStepper;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import cgresearch.core.logging.Logger;

public class TinkerforgeStepperMotor {

  /**
   * Tinkerforge object.
   */
  private final BrickStepper stepperBrick;

  public TinkerforgeStepperMotor(String uid, IPConnection ipConnection) {
    stepperBrick = new BrickStepper(uid, ipConnection);
    try {
      stepperBrick.setMinimumVoltage(2500);
      stepperBrick.setMotorCurrent(2291);
      stepperBrick.setMaxVelocity(30000);
      stepperBrick.setSpeedRamping(30000, 30000);
      stepperBrick.setStepMode((short) 1);
    } catch (TimeoutException | NotConnectedException e) {
      Logger.getInstance().error("Error initializing stepper");
    }
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
      Logger.getInstance().message("Successfully enabled stepper motor.");
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
}

package cnc;

import java.io.IOException;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;

import cgresearch.core.logging.Logger;
import homeautomation.actors.TinkerforgeStepperMotor;

/**
 * Reduced controller for a CNC mill.
 * 
 * @author Philipp Jenke
 *
 */
public class CncMill {

  /**
   * Connection to Tinkerforge stack.
   */
  private IPConnection ipConnection;

  private TinkerforgeStepperMotor motorX;
  private TinkerforgeStepperMotor motorY;
  private TinkerforgeStepperMotor motorZ;

  /**
   * Stepper steps required to move 1mm in x-direction.
   */
  private final double STEPS_PER_MM_X = 10;
  /**
   * Stepper steps required to move 1mm in y-direction.
   */
  private final double STEPS_PER_MM_Y = 10;

  /**
   * Stepper steps required to move 1mm in z-direction.
   */
  private final double STEPS_PER_MM_Z = 10;

  public CncMill() {
    ipConnection = new IPConnection();
    try {
      ipConnection.connect("localhost", 4223);
      Logger.getInstance().message("Successfully connected to Tinkerforge.");
    } catch (AlreadyConnectedException | IOException e) {
      Logger.getInstance().error("Failed to connect to Tinkerforge.");
    }
    motorX = new TinkerforgeStepperMotor("6DAWPT", ipConnection, 2500, 2291, 20000, 20000, 20000); // wrong
                                                                                                   // UID!
    motorY = new TinkerforgeStepperMotor("", ipConnection, 2500, 2291, 20000, 20000, 20000);
    motorZ = new TinkerforgeStepperMotor("6DAWPT", ipConnection, 2500, 2291, 20000, 20000, 20000);
  }

  /**
   * Positive distance = left
   * 
   * @param distance
   *          in mm
   */
  public void moveX(double distance) {
    motorX.moveSteps((int) (distance * STEPS_PER_MM_X));
  }

  /**
   * Positive distance = up
   * 
   * @param distance
   *          in mm
   */
  public void moveY(double distance) {
    motorY.moveSteps((int) (distance * STEPS_PER_MM_Y));
  }

  /**
   * Positive distance = down
   * 
   * @param distance
   *          in mm
   */
  public void moveZ(double distance) {
    motorZ.moveSteps((int) (distance * STEPS_PER_MM_Z));
  }

  /**
   * Stop all motors.
   */
  public void stop() {
    motorX.stop();
    motorY.stop();
    motorZ.stop();
  }

  /**
   * Enable all motors.
   */
  public void enable() {
    motorX.enable();
    motorY.enable();
    motorZ.enable();
  }

  /**
   * Disable all motors
   */
  public void disable() {
    motorX.disable();
    motorY.disable();
    motorZ.disable();
  }
}

package cnc;

import java.io.IOException;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;

import cgresearch.core.logging.Logger;
import homeautomation.actors.TinkerforgeStepperMotor;
import homeautomation.actors.TinkerforgeStepperMotor.Axis;
import homeautomation.sensors.TinkerforgeJoystick;
import homeautomation.sensors.TinkerforgeLinearPoti;

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
   * 
   * (2.5mm * 16 Zähne) / 200 Schritt pro Umdrehung / 2 wegen Flaschenzug
   */
  private final double MM_PER_STEP_X = 20.0 / 200.0;
  /**
   * Stepper steps required to move 1mm in y-direction.
   * 
   * (2.5mm * 16 Zähne) / 200 Schritt pro Umdrehung
   */
  private final double MM_PER_STEP_Y = 40.0 / 200.0;

  /**
   * Stepper steps required to move 1mm in z-direction.
   */
  private final double STEPS_PER_MM_Z = 10;

  private final int SPEED = 500; // 500 steps per second
  private final int ACCELLERATION = 5000;

  public CncMill() {
    ipConnection = new IPConnection();
    try {
      ipConnection.connect("localhost", 4223);
      Logger.getInstance().message("Successfully connected to Tinkerforge.");
    } catch (AlreadyConnectedException | IOException e) {
      Logger.getInstance().error("Failed to connect to Tinkerforge.");
    }
    motorX = new TinkerforgeStepperMotor("6DAWPT", ipConnection, 2500, 2291, SPEED, ACCELLERATION, ACCELLERATION);
    motorY = new TinkerforgeStepperMotor("6eStmv", ipConnection, 2500, 2291, SPEED, ACCELLERATION, ACCELLERATION);
    motorZ = new TinkerforgeStepperMotor("67PFAJ", ipConnection, 2500, 2291, SPEED / 2, ACCELLERATION, ACCELLERATION);
    new TinkerforgeLinearPoti("sQU", ipConnection, (position) -> {
      handleControllerZ(position);
    });
    new TinkerforgeJoystick("wao", ipConnection, (position) -> {
      handleControllerXy(position.x, position.y);
    });
  }

  /**
   * Handle controller input for the x and y axis.
   */
  private void handleControllerXy(int x, int y) {
    // X-axis
    if (x < -33) {
      motorY.move(Axis.FORWARDS);
    } else if (x > 33) {
      motorY.move(Axis.BACKWARDS);
    } else {
      motorY.stop();
    }

    // Y-axis
    if (y < -33) {
      motorX.move(Axis.FORWARDS);
    } else if (y > 33) {
      motorX.move(Axis.BACKWARDS);
    } else {
      motorX.stop();
    }
  }

  /**
   * Handle controller input for the z axis.
   */
  private void handleControllerZ(int z) {
    // YZaxis
    if (z < 33) {
      motorZ.move(Axis.BACKWARDS);
    } else if (z > 66) {
      motorZ.move(Axis.FORWARDS);
    } else {
      motorZ.stop();
    }
  }

  /**
   * Positive distance = left
   * 
   * @param distance
   *          in mm
   */
  public void moveX(double distance) {
    motorX.moveSteps((int) (distance / MM_PER_STEP_X));
  }

  /**
   * Positive distance = up
   * 
   * @param distance
   *          in mm
   */
  public void moveY(double distance) {
    motorY.moveSteps((int) (distance * MM_PER_STEP_Y));
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

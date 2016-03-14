package electronics.gyro;

import java.util.List;

import javax.swing.SwingUtilities;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import homeautomation.actors.ArduinoServo;
import homeautomation.platform.ArduinoConnection;
import homeautomation.sensors.ArduinoMpu6050;

/**
 * Model (logic) for the Gyro test application.
 * 
 * @author Philipp Jenke
 *
 */
public class GyroApplicationModel {

  /**
   * Connection to the Arduino
   */
  private ArduinoConnection arduinoConnection;

  /**
   * Servo for testing the custom commands
   */
  private ArduinoServo servo;

  private ArduinoMpu6050 gyro;

  /**
   * Angle to test the object to show current orientation
   */
  private double dummyAngle = 0;

  public GyroApplicationModel() {
    System.load("/Users/abo781/abo781/code/computergraphics/libs/native/osx/librxtxSerial.jnilib");
    arduinoConnection = new ArduinoConnection();
    servo = new ArduinoServo(arduinoConnection, 8);
    gyro = new ArduinoMpu6050(arduinoConnection);
  }

  public void connect(String port) {
    arduinoConnection.connect(port);
    servo.attach();
  }

  public void disconnect() {
    servo.detach();
    arduinoConnection.disconnect();
  }

  /**
   * Returns the orientation (and translation) of the sensor in 3-space
   */
  public Matrix getOrientation() {

    // Nasty hack, otherwise the Arduino does not react
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        gyro.requestValues();
      }
    });

    dummyAngle += 0.1;
    Matrix sixDof = MatrixFactory.createHomogeniousFor3spaceMatrix(
        MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1).getNormalized(), dummyAngle));
    return sixDof;

    // return MatrixFactory.createIdentityMatrix4();
  }

  public List<String> getPorts() {
    return arduinoConnection.getPortList();
  }

  public void setServoAngle(int angle) {
    servo.setDegree(angle);
  }

  public ArduinoMpu6050 getGyro() {
    return gyro;
  }
}

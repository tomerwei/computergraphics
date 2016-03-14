package electronics.gyro;

import java.util.List;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.VectorFactory;
import homeautomation.platform.ArduinoConnection;

/**
 * Model (logic) for the Gyro test application.
 * 
 * @author Philipp Jenke
 *
 */
public class GyroApplicationModel {

  private ArduinoConnection arduinoConnection;

  private double dummyAngle = 0;

  public GyroApplicationModel() {
    System.load("/Users/abo781/abo781/code/computergraphics/libs/native/osx/librxtxSerial.jnilib");
    arduinoConnection = new ArduinoConnection();
    // arduinoConnection.test();
  }

  public void connect(String port) {
    arduinoConnection.connect(port);
  }

  public void disconnect() {
    arduinoConnection.disconnect();
  }

  /**
   * Returns the orientation (and translation) of the sensor in 3-space
   */
  public Matrix getOrientation() {
    //dummyAngle += 0.1;
    Matrix sixDof = MatrixFactory.createHomogeniousFor3spaceMatrix(
        MatrixFactory.createRotationMatrix(VectorFactory.createVector3(1, 1, 1).getNormalized(), dummyAngle));
    return sixDof;

    //    return MatrixFactory.createIdentityMatrix4();
  }

  public List<String> getPorts() {
    return arduinoConnection.getPortList();
  }

}

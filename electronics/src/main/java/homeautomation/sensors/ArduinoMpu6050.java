package homeautomation.sensors;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import homeautomation.platform.ArduinoConnection;

public class ArduinoMpu6050 implements ISensor {

  private Vector accelleration = VectorFactory.createVector3(0, 0, 0);
  private Vector position = VectorFactory.createVector3(0, 0, 0);
  private final ArduinoConnection connection;

  public ArduinoMpu6050(ArduinoConnection connection) {
    this.connection = connection;
    connection.addMessageCallback(message -> {
      parseValues(message);
    });
    Logger.getInstance().message("Create MPU6050 sensor");
  }

  @Override
  public String getModel() {
    return "MPU6050";
  }

  @Override
  public String getIdentifier() {
    return null;
  }

  @Override
  public String getLocation() {
    return null;
  }

  @Override
  public double getValue() {
    return 0;
  }

  @Override
  public SensorType getType() {
    return SensorType.ACCELLEROMETER;
  }

  @Override
  public String toJson() {
    return null;
  }

  private void parseValues(String message) {
    String[] tokens = message.trim().split(" ");
    if (tokens.length < 7 || !tokens[0].contains("gyro")) {
      return;
    }
    for (int i = 0; i < 3; i++) {
      accelleration.set(i, Double.parseDouble(tokens[i + 1]));
      position.set(i, Double.parseDouble(tokens[i + 4]));
    }
    // Logger.getInstance().message("Accelleration: " + accelleration);
  }

  public void requestValues() {
    connection.sendCustomCommand("gyro");
  }
}

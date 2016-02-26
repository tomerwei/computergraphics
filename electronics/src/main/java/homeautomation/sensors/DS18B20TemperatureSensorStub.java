package homeautomation.sensors;

public class DS18B20TemperatureSensorStub extends DS18B20TemperatureSensor {

  /**
   * Constructor.
   */
  public DS18B20TemperatureSensorStub() {
    super("Sensor Stub", "Dummy location");
  }

  @Override
  public double getValue() {
    return Math.random();
  }

  @Override
  public String toString() {
    return getModel() + " (" + getIdentifier() + "): " + getType();
  }
}

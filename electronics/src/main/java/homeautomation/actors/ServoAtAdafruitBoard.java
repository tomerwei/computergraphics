package homeautomation.actors;

import homeautomation.foundation.AdafruitPCA9685;
import homeautomation.foundation.Logger;

/**
 * Steuerung eines Servos über das Adafruit board.
 */
public class ServoAtAdafruitBoard implements IServo {

  /**
   * Slot-Index auf dem Board [0...15].
   */
  private final int slotIndex;

  /**
   * Min Wert für PWM (-90 Grad).
   */
  private int servoMin = 150;

  /**
   * Max Wert für PWM (90 Grad).
   */
  private int servoMax = 600;

  /**
   * Maximum rotation (neg. and pos.) of the servo.
   */
  private int maxDegree = 45;

  /**
   * Adafruit-Board Schnittstelle.
   *
   */
  private AdafruitPCA9685 servoBoard;

  /**
   * Konstruktor.
   *
   * @param freq
   *          Frequenz zur Kommunikation mit dem Servo.
   * @param slotIndex
   *          Index des Servos auf dem Board.
   * @param servoMin
   *          Minimaler PWM.
   * @param servoMax
   *          Maximaler PWM.
   */
  public ServoAtAdafruitBoard(int slotIndex, int maxDegree, int servoMin, int servoMax, AdafruitPCA9685 board) {
    this.maxDegree = maxDegree;
    this.slotIndex = slotIndex;
    this.servoMin = servoMin;
    this.servoMax = servoMax;
    this.servoBoard = board;
  }

  @Override
  public ActorType getType() {
    return ActorType.SERVO;
  }

  @Override
  public void sendCommand(String command) {
    Logger.getInstance().error("sendCommand() not implemented for " + getClass().getName());
  }

  @Override
  public String getLocation() {
    Logger.getInstance().error("getLocation() not implemented for " + getClass().getName());
    return null;
  }

  @Override
  public String getHumanRedableType() {
    return "Servo at AdafruitBoard";
  }

  @Override
  public String toJson() {
    Logger.getInstance().error("toJson() not implemented for " + getClass().getName());
    return null;
  }

  @Override
  public void setDegree(int degree) {

    if (degree == 0) {
      servoBoard.setPWM(slotIndex, 0, 0);
      return;
    }

    double scale = (degree + maxDegree) / (2.0 * maxDegree);
    double servoPwm = servoMin + (servoMax - servoMin) * scale;
    // System.out.println("Setting PWM to " + servoPwm);
    servoBoard.setPWM(slotIndex, 0, (int) servoPwm);
  }

  @Override
  public void shutdown() {
  }

  @Override
  public String toString() {
    return getClass().getName();
  }

  /**
   * Stop the servo (at least a continuous one).
   */
  public void stop() {
    servoBoard.setPWM(slotIndex, 0, 0);
  }

  /**
   * Test-Anwendung.
   *
   * @param args
   *          Kommandozeilenparameter
   */
  public static void main(String[] args) {

    int TEST_INDEX = 1;
    int MIN = HomeAutomationConstants.MG_995_01_MIN_PULSE;
    int MAX = HomeAutomationConstants.MG_995_01_MAX_PULSE;

    int maxDegree = 45;
    AdafruitPCA9685 board = new AdafruitPCA9685();
    board.setPWMFreq(HomeAutomationConstants.MG_995_FREQUENCY);
    ServoAtAdafruitBoard servo = new ServoAtAdafruitBoard(TEST_INDEX, maxDegree, MIN, MAX, board);
    for (int i = -5; i < 5; i += 1) {
      System.out.println(i);
      servo.setDegree(i * 2);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
      }
    }

    servo.setDegree(0);

    // TODO: Test me
    servo.stop();
  }
}

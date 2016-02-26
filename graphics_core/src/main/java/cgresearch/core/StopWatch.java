package cgresearch.core;

/**
 * Stopwatch used to measure timing.
 */
public class StopWatch {

  /**
   * Remember start- and stop time.
   */
  private long startTime, stopTime;

  /**
   * Start measurement.
   */
  public void start() {
    startTime = System.nanoTime();
    stopTime = 0;
  }

  /**
   * Stop measurement.
   */
  public void stop() {
    stopTime = System.nanoTime();
  }

  /**
   * Return the measured interval in seconds.
   */
  public double getSeconds() {
    if (startTime > stopTime) {
      return 0;
    }
    return (stopTime - startTime) / 1000000000.0;
  }
}

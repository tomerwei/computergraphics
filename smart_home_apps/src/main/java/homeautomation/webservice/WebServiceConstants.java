package homeautomation.webservice;

/**
 * Container for the constants in the web service.
 * 
 * @author Philipp Jenke
 *
 */
public interface WebServiceConstants {
  /**
   * Enum for the measurement interval.
   */
  public enum MeasurementInterval {
    DAY, WEEK, MONTH, YEAR
  }

  public static final String PARAMETER_INDEX = "index";
  public static final String PARAMETER_COMMAND = "command";
  public static final Object OPERATION = "op";
  public static final String GET_LIST = "getList";
}

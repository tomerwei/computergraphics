package homeautomation.webservice.dataaccess;

import homeautomation.database.DatabaseAccess;
import homeautomation.foundation.Logger;
import homeautomation.webservice.ChartHelper;
import homeautomation.webservice.WebServiceConstants.MeasurementInterval;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides access to measurements from the database.
 * 
 * @author Philipp Jenke
 *
 */
public class DatabaseInformationProvider {

  /**
   * Helper instance to create charts from the data.
   */
  private final ChartHelper chartHelper;

  /**
   * Constructor.
   * 
   * @param databaseAccess
   *          Database interface.
   */
  public DatabaseInformationProvider(DatabaseAccess databaseAccess) {
    chartHelper = new ChartHelper(databaseAccess);
  }

  /**
   * Create a buffered image for a sensor given a certain time interval.
   * 
   * @param measurementInterval
   *          Interval to be used (e.g. month).
   * @param sensorKey
   *          Database key of the sensor.
   * @return Image containing the stats chart.
   */
  public BufferedImage createStatsImage(
      MeasurementInterval measurementInterval, int sensorKey) {

    if (measurementInterval == null) {
      Logger.getInstance().error("Invalid stats: " + measurementInterval);
      return null;
    }

    Calendar cal = GregorianCalendar.getInstance();
    cal.getTime();
    long offset = 0;
    switch (measurementInterval) {
      case DAY:
        offset = 24L * 60L * 60L * 1000L;
        break;
      case WEEK:
        offset = 7L * 24L * 60L * 60L * 1000L;
        break;
      case MONTH:
        offset = 31L * 24L * 60L * 60L * 1000L;
        break;
      case YEAR:
        offset = 365L * 24L * 60L * 60L * 1000L;
        break;
      default:

    }
    Date startTimestamp = new Date(cal.getTime().getTime() - offset);
    return chartHelper.createImage("Sensor " + sensorKey, 640, 480, sensorKey,
        startTimestamp, cal.getTime());
  }
}

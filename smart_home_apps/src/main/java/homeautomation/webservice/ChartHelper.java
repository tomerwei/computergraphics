package homeautomation.webservice;

import homeautomation.database.DatabaseAccess;
import homeautomation.database.Measurement;
import homeautomation.foundation.Logger;

import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

public class ChartHelper {

  /**
   * Reference to the database access.
   */
  private DatabaseAccess databaseAccess;

  /**
   * Constructor.
   * 
   * @param databaseAccess
   *          Database interface.
   */
  public ChartHelper(DatabaseAccess databaseAccess) {
    this.databaseAccess = databaseAccess;
  }

  /**
   * Create a buffered image for the sensor within the given time boundaries.
   * 
   * @param title
   *          Title of the image.
   * @param width
   *          Image width.
   * @param height
   *          Image height.
   * @param sensorKey
   *          Sensor database key.
   * @param startTimestamp
   *          Starting timestamp.
   * @param endTimestamp
   *          Ending timestamp.
   * 
   * @return Created image.
   */
  public BufferedImage createImage(String title, int width, int height,
      int sensorKey, Date startTimestamp, Date endTimestamp) {
    JFreeChart chart = createChart(sensorKey, startTimestamp, endTimestamp);
    return chart.createBufferedImage(width, height);
  }

  /**
   * Create a chart for the given sensor within the given time boundaries.
   * 
   * @param sensorKey
   *          Sensor database key.
   * @param startTimestamp
   *          Starting timestamp.
   * @param endTimestamp
   *          Ending timestamp.
   * 
   * @return Created chart.
   */
  private JFreeChart createChart(int sensorKey, Date startTimestamp,
      Date endTimestamp) {
    TimeSeriesCollection timeSerieCollection = new TimeSeriesCollection();
    databaseAccess.connect();

    Logger.getInstance().message("Looking for key " + sensorKey);

    TimeSeries timeSeriesDataset =
        createTimeSeries("Messwert", sensorKey, startTimestamp, endTimestamp);
    if (timeSeriesDataset != null) {
      timeSerieCollection.addSeries(timeSeriesDataset);

      // Create smoothed time series
      TimeSeries smoothedTimeSeries =
          createSmoothedTimeSeries(timeSeriesDataset, startTimestamp,
              endTimestamp);
      timeSerieCollection.addSeries(smoothedTimeSeries);
    }

    databaseAccess.disconnect();
    JFreeChart chart =
        ChartFactory.createTimeSeriesChart("Sensor " + sensorKey, "Zeit",
            "Wert", timeSerieCollection);
    return chart;
  }

  /**
   * Create a time series object.
   * 
   * @param sensorData
   *          Contained data.
   * @param startTimestamp
   *          Startin timestamp.
   * @param endTimestamp
   *          Ending timestamp.
   * @return Time series object with the data between start and end.
   */
  private TimeSeries createSmoothedTimeSeries(TimeSeries sensorData,
      Date startTimestamp, Date endTimestamp) {
    TimeSeries timeSeries = new TimeSeries("Mittelwert");

    int numberOfSmoothedValues = 100;
    long timestamp = startTimestamp.getTime();
    long delta =
        (endTimestamp.getTime() - startTimestamp.getTime())
            / (long) (numberOfSmoothedValues - 1);
    double sigma = (endTimestamp.getTime() - startTimestamp.getTime()) / 30L;
    for (int i = 0; i < numberOfSmoothedValues; i++) {
      double value = getSmoothedValue(timestamp, sensorData, sigma);
      if (value > -1000) {
        timeSeries.add(new Second(new Timestamp(timestamp)), value);
      }
      timestamp += delta;
    }
    return timeSeries;
  }

  /**
   * Compute the smoothed value using a Gaussian.
   * 
   * @param timestamp
   *          Date of the timestamp.
   * @param sensorData
   *          Data to be smoothed.
   * @param sigma
   *          Standard deviation of the Gaussian.
   * @return Smoothed value.
   */
  private double getSmoothedValue(long timestamp, TimeSeries sensorData,
      double sigma) {
    double summedWeight = 0;
    double value = 0;
    for (int i = 0; i < sensorData.getItemCount(); i++) {
      TimeSeriesDataItem item = sensorData.getDataItem(i);
      long ts = item.getPeriod().getLastMillisecond();

      double delta = ts - timestamp;
      double weight = Math.exp((-0.5 * delta * delta) / (2 * sigma * sigma));
      double localValue = (double) (item.getValue());
      value += localValue * weight;
      summedWeight += weight;
    }
    if (summedWeight > 1e-5) {
      value = value / summedWeight;
    } else {
      value = Double.NEGATIVE_INFINITY;
    }
    return value;
  }

  /**
   * Create a time series in the specified time interval for the given sensorID
   * (key in database).
   * 
   * @param title
   *          Image title.
   * @param sensorId
   *          Sensor database key.
   * @param startTimestamp
   *          Starting timestamp.
   * @param endTimestamp
   *          Ending timestamp.
   * @return Time series object with the data.
   */
  private TimeSeries createTimeSeries(String title, int sensorId,
      Date startTimestamp, Date endTimestamp) {
    TimeSeries timeSeriesDataset = new TimeSeries(title);
    List<Measurement> databaseData =
        databaseAccess.getValuesForSensorByKey(sensorId, startTimestamp,
            endTimestamp);

    if (databaseData == null) {
      System.out.println("Dataset is null.");
      return null;
    }
    if (databaseData.size() == 0) {
      System.out.println("Dataset has no content.");
      return null;
    }
    Logger.getInstance().debug(
        "Succesfully grabbed " + databaseData.size()
            + " datasets from database.");
    for (int index = 0; index < databaseData.size(); index++) {
      timeSeriesDataset.add(new Second(databaseData.get(index).timestamp),
          databaseData.get(index).value);
    }
    return timeSeriesDataset;
  }
}

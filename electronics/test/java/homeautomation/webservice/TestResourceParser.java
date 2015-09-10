package homeautomation.webservice;

import static org.junit.Assert.*;
import homeautomation.webservice.WebServiceConstants.MeasurementInterval;
import homeautomation.webservice.resources.ResourceSensorStatsImage;

import org.junit.Test;

/**
 * Testing class for the resource URI parsers.
 * 
 * @author Philipp Jenke
 *
 */
public class TestResourceParser {

  @Test
  public void testParserSensorIndex() {
    String resourceUri = "/raspiserver/stats/sensorKey=2,interval=WEEK";
    assertEquals(ResourceSensorStatsImage.getSensorKey(resourceUri), 2);
  }

  @Test
  public void testParserStats() {
    String resourceUri = "/raspiserver/stats/sensorKey=2,interval=WEEK";
    assertEquals(ResourceSensorStatsImage.getStats(resourceUri),
        MeasurementInterval.WEEK);
  }
}

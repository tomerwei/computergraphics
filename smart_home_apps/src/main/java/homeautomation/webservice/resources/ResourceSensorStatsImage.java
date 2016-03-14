package homeautomation.webservice.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import homeautomation.apps.RasPiServer;
import cgresearch.core.logging.Logger;
import homeautomation.webservice.WebServiceConstants.MeasurementInterval;
import homeautomation.webservice.dataaccess.DatabaseAccessSingleton;
import homeautomation.webservice.dataaccess.DatabaseInformationProvider;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;

import org.restlet.data.MediaType;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Returns the requested stats for a sensor as PNG image.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceSensorStatsImage extends ServerResource {

  @Override
  public void doInit() {
  }

  /**
   * Parse the sensor index from the resource URI.
   * 
   * @param resourceUri
   *          URI of the requested image.
   * 
   * @return Index of the corresponding sensor.
   */
  public static int getSensorKey(String resourceUri) {
    Matcher matcher = createMatcher(resourceUri);
    int sensorIndex = -1;
    try {
      sensorIndex = Integer.parseInt(matcher.group(1));
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to parse for sensor index", e);
    }
    return sensorIndex;
  }

  /**
   * Parse the stats type from the resource URI.
   * 
   * @param resourceUri
   *          URI of the requested stat.
   * @return Interval of the requeste stat.
   */
  public static MeasurementInterval getStats(String resourceUri) {
    Matcher matcher = createMatcher(resourceUri);
    MeasurementInterval stats = null;
    try {
      stats = MeasurementInterval.valueOf(matcher.group(2));
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to parse for stats", e);
    }
    return stats;
  }

  /**
   * Create a matcher to parse the URI.
   * 
   * @param resourceUri
   *          URI to be parsed.
   * @return Matcher object.
   */
  private static Matcher createMatcher(String resourceUri) {
    String regex = "/raspiserver/stats/sensorKey=(.*),interval=(.*)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(resourceUri);
    matcher.find();
    return matcher;
  }

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {
    String resourceUri = getRequest().getResourceRef().getPath();
    int sensorKey = getSensorKey(resourceUri);
    MeasurementInterval stats = getStats(resourceUri);
    BufferedImage image = createStatsImage(stats, sensorKey);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", os);
    } catch (IOException e) {
      Logger.getInstance().exception(
          "Failed to create image for sensor stats.", e);
      e.printStackTrace();
    }
    InputStream is = new ByteArrayInputStream(os.toByteArray());
    return new InputRepresentation(is, MediaType.IMAGE_PNG);

  }

  /**
   * Create an image with the day stats.
   * 
   * @param statsImage
   *          Measurement interval (e.g. month)
   * @param sensorKey
   *          Database key of the sensor.
   * 
   * @return Filename of the image.
   */
  private BufferedImage createStatsImage(MeasurementInterval statsImage,
      int sensorKey) {
    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();
    BufferedImage image = null;
    if (raspiServer != null) {
      DatabaseInformationProvider dbsp =
          new DatabaseInformationProvider(DatabaseAccessSingleton.getInstance()
              .getDatabaseAccess());
      image = dbsp.createStatsImage(statsImage, sensorKey);
    } else {
      Logger.getInstance().message(
          "Created dummy image for sensor with key " + sensorKey + ".");
      int width = 640;
      int height = 480;
      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          image.setRGB(i, j,
              new Color((float) Math.random(), (float) Math.random(),
                  (float) Math.random()).getRGB());
        }
      }
      Font font = new Font("Default", Font.BOLD, 50);
      image.getGraphics().setFont(font);
      image.getGraphics().drawString(statsImage.toString(), 10, 10);
    }

    return image;

    // // Write image to file
    // String filename = createUniqueImageIndifier(statsImage, sensor);
    // try {
    // ImageIO.write(image, "png", new FileOutputStream(new File(
    // "raspiserver/" + filename)));
    // } catch (IOException e) {
    // Logger.getInstance().exception(
    // "Failed to save image to " + filename, e);
    // return null;
    // }
    // return filename;

  }
}

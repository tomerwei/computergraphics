package homeautomation.webservice.resources;

import homeautomation.apps.RasPiServer;
import homeautomation.foundation.Logger;
import homeautomation.sensors.SensorInformation;
import homeautomation.webservice.WebServiceConstants;
import homeautomation.webservice.dataaccess.ISensorListProvider;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;
import homeautomation.webservice.dataaccess.SensorListProviderSingleton;

import java.util.Dictionary;
import java.util.List;

import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

/**
 * Handle REST requests for sensors.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceSensors extends RestResource {

  /**
   * Possible data sources.
   */
  private enum DataSource {
    CURRENT_RASPI_SERVER, DATABASE
  }

  /**
   * Selected data source for the request.
   */
  private DataSource dataSource = DataSource.DATABASE;

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {
    // Handle getList
    String uri = getRequest().getResourceRef().getPath();
    Dictionary<String, String> dictionary = createParameterDictionary(uri);

    if (!checkDictionaryGetList(dictionary)) {
      Representation result = new EmptyRepresentation();
      Logger.getInstance().error("Invalid parameter for GET .");
      return result;
    }
    String jsonListOfSensors = getJsonListOfSensors();
    return new StringRepresentation(jsonListOfSensors);
  }

  /**
   * Check the params for a request for a sensors list.
   * 
   * @param dictionary
   *          Dictionary of the parameters.
   * 
   * @return True if the params are valid, false otherwise.
   */
  private boolean checkDictionaryGetList(Dictionary<String, String> dictionary) {
    if (dictionary.get(WebServiceConstants.OPERATION) == null) {
      return false;
    }
    if (dictionary.get(WebServiceConstants.OPERATION).compareTo(
        WebServiceConstants.GET_LIST) != 0) {
      return false;
    }
    return true;
  }

  /**
   * Create a JSON array list of sensors.
   * 
   * @return JSON-formated representation of the sensor list.
   */
  private String getJsonListOfSensors() {
    RasPiServer raspiServer =
        RaspiServerAccessSingleton.getInstance().getRaspiServer();
    if (raspiServer == null) {
      Logger.getInstance().error("Failed to access raspi server.");
      return "[]";
    }

    if (dataSource == DataSource.CURRENT_RASPI_SERVER) {
      return raspiServer.getJsonSensorsList();
    } else if (dataSource == DataSource.DATABASE) {
      return getJsonListOfSensorsDatabase();
    } else {
      Logger.getInstance().error("Invalid data source.");
      return "";
    }
  }

  /**
   * Create JSON array of sensors.
   * 
   * @return JSON-formatted list of sensors from the database.
   */
  private String getJsonListOfSensorsDatabase() {
    ISensorListProvider sensorListProvider =
        SensorListProviderSingleton.getInstance();
    List<SensorInformation> sensorList = sensorListProvider.getSensorList();
    String json = "[";
    boolean isFirst = true;
    for (SensorInformation sensor : sensorList) {
      if (isFirst) {
        isFirst = false;
      } else {
        json += ",\n";
      }
      json += sensor.toJson();
    }
    json += "]";
    return json;
  }
}

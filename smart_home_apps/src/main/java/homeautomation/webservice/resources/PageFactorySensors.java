package homeautomation.webservice.resources;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import homeautomation.database.Measurement;
import homeautomation.sensors.ISensor.SensorType;
import homeautomation.sensors.SensorInformation;
import homeautomation.webservice.WebServiceConstants.MeasurementInterval;
import homeautomation.webservice.dataaccess.ISensorListProvider;
import homeautomation.webservice.dataaccess.SensorListProviderSingleton;

/**
 * Factory for an HTML page with the sensors.
 * 
 * @author Philipp Jenke
 *
 */
public class PageFactorySensors extends PageFactoryAbstract {

  /**
   * IDs of different sections in the table.
   */
  private enum BlockId {
    TEMPERATURE_CURRENT, HUMIDITY_CURRENT, OLD_MEASUREMENTS
  }

  @Override
  public String getHtmlCode() {
    int width = 650;
    String html = getHtmlPageTop("Sensoren");
    html += "<center>";
    html += getNavigationBar(width);
    html += getHtmlSensorValuesTable(width);
    html += getHtmlCodeBanner(width);
    html += "</center>";
    html += getHtmlPageBottom();
    return html;
  }

  /**
   * Return the HTML code for a table view of sensors.
   * 
   * @param width
   *          Width of the central table.
   * 
   * @return HTML code for the list of sensors.
   */
  private String getHtmlSensorValuesTable(int width) {
    String htmlCode = "";
    htmlCode +=
        "<table cellpadding='0' cellspacing='0' border='0' width='" + width
            + "'>";
    // htmlCode += "<tr>" + "<td><p class='text-bold'>Ort</p></td>"
    // + "<td><p class='text-bold'>Typ</p></td>"
    // + "<td><p class='text-bold'>Wert</p></td>"
    // + "<td><p class='text-bold'>Zeitpunkt</p></td>"
    // + "<td><p class='text-bold'>Tag</p></td>"
    // + "<td><p class='text-bold'>Woche</p></td>"
    // + "<td><p class='text-bold'>Monat</p></td>"
    // + "<td><p class='text-bold'>Jahr</p></td>"
    // + "<td><p class='text-bold'>Sensor</p></td>" + "</tr>\n";
    htmlCode += getHtmlSensorValues();
    htmlCode += "</table>";
    return htmlCode;
  }

  /**
   * Return the HTML code for the sensors.
   * 
   * @return HTML code for the sensors.
   */
  private String getHtmlSensorValues() {
    ISensorListProvider sensorListProvider =
        SensorListProviderSingleton.getInstance();
    String htmlCode = "";
    List<SensorInformation> sensorList = sensorListProvider.getSensorList();

    // Sort list
    Collections.sort(sensorList, new SensorListComparatorCurrentAndType());

    htmlCode +=
        "<tr><td colspan='9'><p class='text-bold-italics-left'>Temperatur<td></tr>";

    BlockId blockId = BlockId.TEMPERATURE_CURRENT;
    for (int i = 0; i < sensorList.size(); i++) {
      SensorInformation sensorInfo = sensorList.get(i);

      if (blockId == BlockId.TEMPERATURE_CURRENT
          && sensorInfo.getType() == SensorType.HUMIDITY) {
        blockId = BlockId.HUMIDITY_CURRENT;
        htmlCode +=
            "<tr><td colspan='9'><p class='text-bold-italics-left'>"
                + "Luftfeuchtigkeit<td></tr>";
      } else if (blockId == BlockId.HUMIDITY_CURRENT && !sensorInfo.isCurrent()) {
        blockId = BlockId.OLD_MEASUREMENTS;
        htmlCode +=
            "<tr><td colspan='9'><p class='text-bold-italics-left'>"
                + "Nicht aktuelle Werte<td></tr>";
      }

      htmlCode += getHtmlCodeSensor(sensorInfo);
    }
    return htmlCode;
  }

  /**
   * Getter.
   * 
   * @param sensorInfo
   *          Information object for a sensor.
   * 
   * @return Return the HTML code table row for a single sensor.
   */
  private String getHtmlCodeSensor(SensorInformation sensorInfo) {
    String imageNameDay =
        "stats/sensorKey=" + sensorInfo.databaseKey + ",interval="
            + MeasurementInterval.DAY.toString();
    String imageNameWeek =
        "stats/sensorKey=" + sensorInfo.databaseKey + ",interval="
            + MeasurementInterval.WEEK.toString();
    String imageNameMonth =
        "stats/sensorKey=" + sensorInfo.databaseKey + ",interval="
            + MeasurementInterval.MONTH.toString();
    String imageNameYear =
        "stats/sensorKey=" + sensorInfo.databaseKey + ",interval="
            + MeasurementInterval.YEAR.toString();
    String chartIconCode =
        "<img src='images/charticon.png' width='30' height='20' alt='Chart'>";

    String imgTemperature =
        "<img src='images/temperature.png' width='20' height='20' alt='Temperature'>";
    String imgHumidity =
        "<img src='images/humidity.png' width='20' height='20' alt='Humidity'>";
    Measurement valueTimestamp = sensorInfo.lastMeasurement;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    return "<tr>" + "<td><p class='text'>"
        + sensorInfo.getLocation()
        + "</p></td>"
        + "<td><p class='text'>"
        + ((sensorInfo.getType() == SensorType.TEMPERATURE) ? imgTemperature
            : imgHumidity) + "</p></td>" + "<td width='50'><p class='text'>"
        + String.format("%.2f", valueTimestamp.value) + "</p></td>"
        + "<td width='100'><p class='text'>"
        + formatter.format(new Date(valueTimestamp.timestamp.getTime()))
        + "</p></td>" + "<td><p class='text'>" + "<a href='" + imageNameDay
        + "'>" + chartIconCode + "</a>" + "</p></td>" + "<td><p class='text'>"
        + "<a href='" + imageNameWeek + "'>" + chartIconCode + "</a>"
        + "</p></td>" + "<td><p class='text'>" + "<a href='" + imageNameMonth
        + "'>" + chartIconCode + "</a>" + "</p></td>" + "<td><p class='text'>"
        + "<a href='" + imageNameYear + "'>" + chartIconCode + "</a>"
        + "</p></td>" + "<td><p class='text'>" + sensorInfo.getModel() + " ("
        + sensorInfo.getIdentifier() + ")" + "</p></td>" + "</tr>\n";
  }
}

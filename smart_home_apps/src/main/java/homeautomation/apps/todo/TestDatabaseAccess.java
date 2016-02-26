package homeautomation.apps.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import homeautomation.database.DatabaseAccess;

/**
 * Test class for access to the database.
 * 
 * @author Philipp Jenke
 *
 */
public class TestDatabaseAccess {
  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   * 
   * @throws Exception
   *           Database access failure.
   */
  public static void main(String[] args) throws Exception {
    DatabaseAccess databaseAccess =
        new DatabaseAccess("127.0.0.1", "homeautomation",
            "SENSOR_MEASUREMENTS", "pj", "mysql");

    Date date = Calendar.getInstance().getTime();

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    System.out.println(dateFormat.format(cal.getTime()));

    databaseAccess.insertSensorMeasurement(date.getTime(), 53.122, 1);
    databaseAccess.testReadAll();
  }
}

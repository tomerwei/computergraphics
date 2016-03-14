package homeautomation.database;

import cgresearch.core.logging.*;
import homeautomation.sensors.ISensor;
import homeautomation.sensors.SensorInformation;
import homeautomation.sensors.ISensor.SensorType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DatabaseAccess {

  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;
  private Session session = null;

  private String databaseName = "homeautomation";
  private String tableName = "SENSOR_MEASUREMENTS";
  private String userName = "pj";
  private String password = "mysql";
  private String hostName = "localhost";
  private String sshHost = null;
  private String sshLogin = null;
  private String sshPasswort = null;
  private int sshLocalPort = -1;
  private int sshRemotePort = -1;

  private boolean isConnected = false;

  /**
   * Constructor.
   * 
   * @param hostname
   *          Database host name.
   * @param databaseName
   *          Name of the database.
   * @param tableName
   *          Name of the table.
   * @param username
   *          Database user name.
   * @param password
   *          Database passoword.
   */
  public DatabaseAccess(String hostname, String databaseName, String tableName,
      String username, String password) {
    this.hostName = hostname;
    this.databaseName = databaseName;
    this.tableName = tableName;
    this.userName = username;
    this.password = password;
    isConnected = false;
  }

  /**
   * Constructor.
   * 
   * @param hostname
   *          Database host name.
   * @param databaseName
   *          Name of the database.
   * @param tableName
   *          Name of the table.
   * @param username
   *          Database user name.
   * @param password
   *          Database password.
   * @param sshHost
   *          SSH host name.
   * @param sshLogin
   *          SSH login name.
   * @param sshPassword
   *          SSH login password.
   * @param localPort
   *          SSH host port
   * @param remotePort
   *          SSH host remote port.
   */
  public DatabaseAccess(String hostname, String databaseName, String tableName,
      String username, String password, String sshHost, String sshLogin,
      String sshPassword, int localPort, int remotePort) {
    this(hostname, databaseName, tableName, username, password);
    this.sshHost = sshHost;
    this.sshLogin = sshLogin;
    this.sshPasswort = sshPassword;
    this.sshLocalPort = localPort;
    this.sshRemotePort = remotePort;
  }

  /**
   * Create SSH tunnel.
   * 
   * @return True if the tunnel was created successfully.
   */
  private boolean doSshTunnel() {
    final JSch jsch = new JSch();
    try {
      Logger.getInstance().debug(
          "Creating ssh session to " + sshLogin + "@" + sshHost);
      session = jsch.getSession(sshLogin, sshHost, 22);
    } catch (JSchException e) {
      Logger.getInstance().error(
          "Failed to create SSH session: " + e.getMessage());
      return false;
    }
    session.setPassword(sshPasswort);

    final Properties config = new Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);

    try {
      Logger.getInstance().debug("Connecting to session.");
      session.connect();
    } catch (JSchException e) {
      Logger.getInstance().error(
          "Failed to connect to SSH remote host: " + e.getMessage());
      return false;
    }
    try {
      Logger.getInstance().debug(
          "Port-Forwarding: " + hostName + ":" + sshLocalPort + " -> "
              + sshRemotePort);
      session.setPortForwardingL(sshLocalPort, hostName, sshRemotePort);
    } catch (JSchException e) {
      Logger.getInstance().error(
          "Failed to set port forwarding: " + e.getMessage());
      return false;
    }

    return true;
  }

  /**
   * Connect to the database.
   * 
   * @return True, if the connection was established successfully.
   */
  public boolean connect() {
    if (isConnected) {
      Logger.getInstance().error(
          "Database is currently connected; please disconnect first.");
      return false;
    }

    isConnected = false;

    if (sshHost != null) {
      if (!doSshTunnel()) {
        return false;
      }
      if (!connectToDatabase(hostName + ":" + sshLocalPort)) {
        return false;
      }
    } else {
      if (!connectToDatabase(hostName)) {
        return false;
      }
    }

    isConnected = true;
    return true;
  }

  /**
   * Connect to a database.
   * 
   * @param hostName
   *          Host name of the database.
   * @return True if the connection was established successfully.
   */
  private boolean connectToDatabase(String hostName) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      String connectionString =
          "jdbc:mysql://" + hostName + "/" + databaseName + "?" + "user="
              + userName + "&password=" + password;
      Logger.getInstance().debug("Connecting to database: " + connectionString);
      connect = DriverManager.getConnection(connectionString);
      return true;
    } catch (ClassNotFoundException | SQLException e) {
      Logger.getInstance().error(
          "Failed to connect to database: " + e.getMessage());
      return false;
    }
  }

  /**
   * Disconnect from database.
   */
  public void disconnect() {

    if (!isConnected) {
      Logger.getInstance().error(
          "Currently not connected, cannot disconnect from database.");
      return;
    }

    Logger.getInstance().debug("Disconnecting from database.");

    close(resultSet);
    close(statement);
    close(connect);
    if (session != null) {
      session.disconnect();
      session = null;
    }
    isConnected = false;
  }

  /**
   * Test an insert statement.
   * 
   * @param date
   *          Date of the inserted information.
   * @param value
   *          Value of the inserted information.
   * @param sensorKey
   *          Database sensor key.
   */
  public void insertSensorMeasurement(long date, double value, int sensorKey) {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return;
    }

    try {
      String statement =
          "insert into  " + databaseName + "." + tableName
              + " values (default, ?, ?, ?)";
      preparedStatement = connect.prepareStatement(statement);
      preparedStatement.setTimestamp(1, new java.sql.Timestamp(date));
      preparedStatement.setString(2, value + "");
      preparedStatement.setInt(3, sensorKey);

      preparedStatement.executeUpdate();

      Logger.getInstance().debug("Inserted measurement (" + statement + ")");

    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to insert into database: " + e.getMessage());
    }
  }

  /**
   * Statements allow to issue SQL queries to the database.
   */
  public void testReadAll() {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return;
    }

    try {
      statement = connect.createStatement();
      // resultSet gets the result of the SQL query
      resultSet =
          statement.executeQuery("select * from " + databaseName.toUpperCase()
              + "." + tableName + "");
      writeResultSet(resultSet);
    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to read table column names: " + e.getMessage());
    }
  }

  /**
   * ??.
   * 
   * @param resultSet
   *          Contains the information to be written.
   * @throws SQLException
   *           Invalid SQL statement.
   */
  private void writeResultSet(ResultSet resultSet) throws SQLException {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return;
    }

    // resultSet is initialised before the first data set
    while (resultSet.next()) {
      // it is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g., resultSet.getSTring(2);
      String sensorId = resultSet.getString("SENSOR_ID");
      java.sql.Timestamp timestamp = resultSet.getTimestamp("TIMESTAMP");
      String value = resultSet.getString("VALUE");
      String location = resultSet.getString("LOCATION");
      String type = resultSet.getString("TYPE");
      DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
      String timeStampString = dateFormat.format(timestamp.getTime());

      Logger.getInstance().debug(
          sensorId + ", " + timeStampString + ", " + value + ", " + location
              + ",  " + type);
    }
  }

  /**
   * Close an instance of an AutoClosable(). Used by disconnect().
   * 
   * @param closable
   *          Object to be closed.
   */
  private void close(AutoCloseable closable) {
    try {
      if (closable != null) {
        closable.close();
      }
    } catch (Exception e) {
      // don't throw now as it might leave following closables in
      // undefined state
    }
  }

  /**
   * Return all values for a given type/location/sensorId from the database.
   * 
   * @param sensorKey
   *          Sensor database key.
   * @param startTimestamp
   *          Starting timestamp.
   * @param endTimestamp
   *          Ending timestamp.
   * 
   * @return List of measurements.
   */
  public List<Measurement> getValuesForSensorByKey(int sensorKey,
      java.util.Date startTimestamp, java.util.Date endTimestamp) {

    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return null;
    }

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String statement =
        "SELECT " + DatabaseConstants.COLUMN_NAME_TIMESTAMP + ", "
            + DatabaseConstants.COLUMN_NAME_VALUE + " from " + databaseName
            + "." + tableName + " WHERE "
            + DatabaseConstants.COLUMN_NAME_SENSOR_KEY + "='" + sensorKey + "'";

    if (startTimestamp != null && endTimestamp != null) {
      statement +=
          " AND " + DatabaseConstants.COLUMN_NAME_TIMESTAMP + " >= '"
              + dateFormat.format(startTimestamp) + "' AND "
              + DatabaseConstants.COLUMN_NAME_TIMESTAMP + " <= '"
              + dateFormat.format(endTimestamp.getTime()) + "'";

    }
    statement += ";";

    List<Measurement> dataset = new ArrayList<Measurement>();

    try {
      Logger.getInstance().debug(statement);

      preparedStatement = connect.prepareStatement(statement);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        dataset.add(new Measurement(resultSet.getTimestamp("TIMESTAMP"),
            resultSet.getDouble("VALUE")));
      }
    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to select from database: " + e.getMessage());
    }
    return dataset;
  }

  /**
   * Return the list of sensors in the database.
   * 
   * @return List of sensor information objects.
   */
  public List<SensorInformation> getSensors() {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return null;
    }

    List<SensorInformation> sensors = new ArrayList<SensorInformation>();
    try {
      String statement =
          "SELECT " + DatabaseConstants.COLUMN_NAME_SENSOR_ID + ", "
              + DatabaseConstants.COLUMN_NAME_LOCATION + ", "
              + DatabaseConstants.COLUMN_NAME_TYPE + ", "
              + DatabaseConstants.COLUMN_NAME_MODEL + ", "
              + DatabaseConstants.COLUMN_NAME_ID + " from " + databaseName
              + ".SENSORS;";
      Logger.getInstance().debug(statement);
      preparedStatement = connect.prepareStatement(statement);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String identifier =
            resultSet.getString(DatabaseConstants.COLUMN_NAME_SENSOR_ID);
        String location =
            resultSet.getString(DatabaseConstants.COLUMN_NAME_LOCATION);
        SensorType type =
            SensorType.valueOf(resultSet
                .getString(DatabaseConstants.COLUMN_NAME_TYPE));
        String model = resultSet.getString(DatabaseConstants.COLUMN_NAME_MODEL);
        int key = resultSet.getInt(DatabaseConstants.COLUMN_NAME_ID);
        sensors.add(new SensorInformation(identifier, location, type, model,
            key));
      }
    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to select from database: " + e.getMessage());
    }
    return sensors;
  }

  @Override
  public String toString() {
    String returnValue = "*** DatabaseAccess ***\n";
    returnValue += "   hostName: " + hostName + "\n";
    returnValue += "   userName: " + userName + "\n";
    returnValue += "   password: " + password + "\n";
    returnValue += "   databaseName: " + databaseName + "\n";
    returnValue += "   tableName: " + tableName + "\n";
    returnValue += "   sshHost: " + sshHost + "\n";
    returnValue += "   sshLogin: " + sshLogin + "\n";
    returnValue += "   sshPasswort: " + sshPasswort + "\n";
    returnValue += "   sshLocalPort: " + sshLocalPort + "\n";
    returnValue += "   sshRemotePort: " + sshRemotePort + "\n";
    return returnValue;
  }

  /**
   * Getter.
   * 
   * @return True of the connection to the database is established.
   */
  public boolean isConnected() {
    return isConnected;
  }

  /**
   * Return the sensor key in the database.
   * 
   * @param sensor
   *          Sensor instance.
   * 
   * @return Database key of the sensor.
   */
  public int getSensorKey(ISensor sensor) {
    return getSensorKey(sensor.getModel(), sensor.getIdentifier(),
        sensor.getType(), sensor.getLocation());
  }

  /**
   * Return the sensor key in the database.
   * 
   * @param model
   *          Sensor model.
   * @param identifier
   *          Sensor GPIO port.
   * @param sensorType
   *          Type of the sensor.
   * @param location
   *          Location of the sensor.
   * 
   * @return Sensor database key.
   */
  private int getSensorKey(String model, String identifier,
      SensorType sensorType, String location) {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return -1;
    }

    try {
      String statement =
          "SELECT " + DatabaseConstants.COLUMN_NAME_ID + " FROM "
              + databaseName + ".SENSORS WHERE "
              + DatabaseConstants.COLUMN_NAME_SENSOR_ID + "='" + identifier
              + "' AND " + DatabaseConstants.COLUMN_NAME_LOCATION + "='"
              + location + "' AND " + DatabaseConstants.COLUMN_NAME_TYPE + "='"
              + sensorType.toString() + "' AND "
              + DatabaseConstants.COLUMN_NAME_MODEL + "='" + model + "';";
      Logger.getInstance().debug(statement);
      preparedStatement = connect.prepareStatement(statement);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        return resultSet.getInt(DatabaseConstants.COLUMN_NAME_ID);
      }
    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to select from database: " + e.getMessage());
    }

    Logger.getInstance().error("Could not find sensor id.");

    return -1;
  }

  /**
   * Create a copy of the object.
   * 
   * @return Cloned object.
   */
  public DatabaseAccess clone() {
    DatabaseAccess dbAccess =
        new DatabaseAccess(hostName, databaseName, tableName, userName,
            password, sshHost, sshLogin, sshPasswort, sshLocalPort,
            sshRemotePort);
    return dbAccess;
  }

  /**
   * Return the latest measurement for the sensor with the given key in the
   * database.
   * 
   * @param databaseKey
   *          Sensor database key.
   * 
   * @return Latest measurement of the sensor.
   */
  public Measurement getLastMeasurement(int databaseKey) {
    if (!isConnected) {
      Logger.getInstance().error("Connect to database first!");
      return null;
    }

    try {
      String statement =
          "SELECT "
              + DatabaseConstants.COLUMN_NAME_VALUE
              + ","
              + DatabaseConstants.COLUMN_NAME_TIMESTAMP
              + " FROM "
              + databaseName
              + "."
              + DatabaseConstants.DATABASE_HOMEAUTOMATION_TABLE_SENSOR_MEASUREMENTS
              + " WHERE " + DatabaseConstants.COLUMN_NAME_SENSOR_KEY + "='"
              + databaseKey + "'" + " ORDER BY 2 DESC" + " LIMIT 1" + ";";
      Logger.getInstance().debug(statement);
      preparedStatement = connect.prepareStatement(statement);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        return new Measurement(
            resultSet.getTimestamp(DatabaseConstants.COLUMN_NAME_TIMESTAMP),
            resultSet.getDouble(DatabaseConstants.COLUMN_NAME_VALUE));

      }
    } catch (SQLException e) {
      Logger.getInstance().error(
          "Failed to select from database: " + e.getMessage());
    }

    Logger.getInstance().error("Could not find sensor id.");

    // Dummy
    return new Measurement(new Timestamp(0), -23);
  }
}

package homeautomation;

public interface JsonConstants {

  // Apps
  public static final String APPNAME_RASPI_SERVER = "raspiserver";
  public static final String APPNAME_VISCLIENT = "visclient";
  public static final String APPNAME_SENSORTEST = "sensortest";
  public static final String APPNAME_SENSOR_CONTROLS_LED = "sensorcontrolsled";
  public static final String APPNAME_ELECTRICAL_SWITCH_TEST = "electrical_switch_test";
  public static final String APPNAME_RCBOAT = "rcboat";

  public static final String SENSORS = "sensors";
  public static final String ACTORS = "actors";
  public static final String LIBRARIES = "libraries";
  public static final String DATABASE = "database";
  public static final String WEBSERVICE = "webservice";

  public static final String MOTION_DETECTION_SENSOR = "motionDetectionSensor";
  public static final String LED_ACTOR = "ledActor";

  // RasPi Server
  public static final String TIMER_INTERVAL = "timerInterval";
  public static final String EXPORT_TARGET = "exportTarget";

  // Sensors
  public static final String TYPE = "type";
  public static final String LOCATION = "location";
  public static final String MODEL = "model";
  public static final String IDENTIFIER = "identifier";
  public static final String TINKERFORGE_UID = IDENTIFIER;
  public static final String PIN_NUMBER = "pinNumber";
  public static final String SYSTEM_CODE = "systemCode";
  public static final String UNIT_CODE = "unitCode";
  public static final String MODE = "mode";
  public static final String VALUE = "value";
  public static final String DATE = "date";
  public static final String TIME = "time";
  public static final String IS_CURRENT = "isCurrent";
  public static final String DATABASE_KEY = "databaseKey";
  public static final String TINKERFORGE_SERVO_BRICK = "tinkerforgeServoBrick";
  public static final String MAX_DEGREE = "maxDegree";
  public static final String PULSE_WIDTH_MIN = "pulseWidthMin";
  public static final String PULSE_WIDTH_MAX = "pulseWidthMax";
  public static final String VELOCITY = "velocity";
  public static final String ACCELERATION = "acceleration";
  public static final String PERIOD = "period";
  public static final String OUTPUT_VOLTAGE = "outputVoltage";
  public static final String MAX_VELOCITY = "maxVelocity";
  public static final String PWM_FREQUENCY = "pwmFrequency";

  // Database
  public static final String DB_DATABASE_NAME = "databaseName";
  public static final String DB_HOSTNAME = "hostname";
  public static final String DB_USERNAME = "username";
  public static final String DB_PASSWORD = "password";
  public static final String DB_TABLENAME = "tableName";
  public static final String DB_SSH_HOST = "sshHost";
  public static final String DB_SSH_USERNAME = "sshUsername";
  public static final String DB_SSH_PASSWORD = "sshPassword";
  public static final String DB_LOCAL_PORT = "localPort";
  public static final String DB_REMOTE_PORT = "remotePort";

  // WebService
  public static final String PORT = "port";
  public static final String ROOT_DIR = "rootDir";

}

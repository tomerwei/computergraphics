package homeautomation.factories;

import homeautomation.actors.IActor;
import homeautomation.actors.RaspPiLed;
import homeautomation.apps.ElectricalSwitchTest;
import homeautomation.apps.SensorControlsLed;
import homeautomation.apps.SensorTest;
import homeautomation.apps.IHomeautomationApp;
import homeautomation.apps.RasPiServer;
import homeautomation.apps.RasPiServer.ExportTarget;
import homeautomation.apps.VisClient;
import homeautomation.database.DatabaseAccess;
import homeautomation.foundation.ConsoleLogger;
import homeautomation.foundation.Logger;
import homeautomation.foundation.Logger.VerboseMode;
import homeautomation.JsonConstants;
import homeautomation.jsonparser.JsonParserActors;
import homeautomation.jsonparser.JsonParserDatabase;
import homeautomation.jsonparser.JsonParserSensor;
import homeautomation.sensors.ISensor;
import homeautomation.sensors.RaspPiMotionDetectionSensor;
import homeautomation.webservice.WebService;
import homeautomation.webservice.dataaccess.DatabaseAccessSingleton;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class HomeAutomationAppFactory {

  /**
   * Create an application from a config file.
   * 
   * @param configJsonFilename
   *          This file contains the JSON config.
   * 
   * @return Created application.
   */
  public IHomeautomationApp createApplication(String configJsonFilename) {

    if (!new File(configJsonFilename).exists()) {
      Logger.getInstance().error("Could not find JSON config file " + configJsonFilename);
      return null;
    }

    // Create a logger for all the debug output.
    new ConsoleLogger(VerboseMode.NORMAL);

    // Look for the object JsonConstants.APPNAME_RASPPI_SERVER, which
    // indicates the application config.
    try (FileInputStream is = new FileInputStream(new File(configJsonFilename));
        JsonParser parser = Json.createParser(is)) {
      while (parser.hasNext()) {
        Event event = parser.next();
        if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.APPNAME_RASPI_SERVER)) {
          RasPiServer app = createRasPiServer(parser);
          if (app != null) {
            Logger.getInstance().message("Successfully created app " + app.toString());
          }
          return app;
        } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.APPNAME_VISCLIENT)) {
          VisClient app = createVisClient(parser);
          if (app != null) {
            Logger.getInstance().message("Successfully created app " + app.toString());
          }
          return app;
        } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.APPNAME_SENSORTEST)) {
          SensorTest app = createSensorTest(parser);
          if (app != null) {
            Logger.getInstance().message("Successfully created app " + app.toString());
          }
          return app;
        } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.APPNAME_SENSOR_CONTROLS_LED)) {
          SensorControlsLed app = createSensorControlsLed(parser);
          if (app != null) {
            Logger.getInstance().message("Successfully created app " + app.toString());
          }
          return app;
        } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.APPNAME_ELECTRICAL_SWITCH_TEST)) {
          ElectricalSwitchTest app = createElectricalSwitchTest(parser);
          if (app != null) {
            Logger.getInstance().message("Successfully created app " + app.toString());
          }
          return app;
          // } else if (event == Event.KEY_NAME &&
          // parser.getString().equals(JsonConstants.APPNAME_RCBOAT)) {
          // RCApp app = createRCBoat(parser);
          // if (app != null) {
          // Logger.getInstance().message("Successfully created app " +
          // app.toString());
          // }
          // return app;
        } else if (event == Event.END_OBJECT) {
          return null;
        }
      }
    } catch (IOException e) {
      Logger.getInstance().error("Failed to parse JSON file " + configJsonFilename);
      e.printStackTrace();
    } catch (JsonException e) {
      Logger.getInstance().error("Failed to parse JSON file " + configJsonFilename);
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Create an RC boat application
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  // private RCApp createRCBoat(JsonParser parser) {
  // return new RCApp();
  // }

  /**
   * Create an app to test the electrical switches.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  private ElectricalSwitchTest createElectricalSwitchTest(JsonParser parser) {
    ElectricalSwitchTest app = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.LIBRARIES)) {
        List<String> libraries = parseLibraries(parser);
        for (String lib : libraries) {
          System.load(lib);
        }
      } else if (event == Event.END_OBJECT) {
        app = new ElectricalSwitchTest();
        return app;
      }
    }
    return app;
  }

  /**
   * Create a SensorControlsLed instance.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  private SensorControlsLed createSensorControlsLed(JsonParser parser) {
    SensorControlsLed app = null;
    RaspPiMotionDetectionSensor motionDetectionSensor = null;
    RaspPiLed led = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.MOTION_DETECTION_SENSOR)) {
        motionDetectionSensor = JsonParserSensor.parseMotionDetectionSensor(parser);
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.LED_ACTOR)) {
        led = JsonParserActors.parseLed(parser);
      } else if (event == Event.END_OBJECT) {
        app = new SensorControlsLed(motionDetectionSensor, led);
        return app;
      }
    }
    return app;
  }

  /**
   * Create a SensorTest instance.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  private SensorTest createSensorTest(JsonParser parser) {
    List<ISensor> sensors = new ArrayList<ISensor>();
    List<String> libraries = new ArrayList<String>();
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.SENSORS)) {
        sensors = JsonParserSensor.parseSensors(parser);
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.LIBRARIES)) {
        libraries = parseLibraries(parser);
        for (String lib : libraries) {
          System.load(lib);
        }
      } else if (event == Event.END_OBJECT) {
        SensorTest app = new SensorTest();
        for (ISensor sensor : sensors) {
          app.addSensor(sensor);
        }
        for (String library : libraries) {
          app.addLibrary(library);
        }
        return app;
      }
    }
    return null;
  }

  /**
   * Create a visualization client application.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  private VisClient createVisClient(JsonParser parser) {
    DatabaseAccess database = null;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.DATABASE)) {
        database = JsonParserDatabase.parseDatabase(parser);
      } else if (event == Event.END_OBJECT) {
        if (database != null) {
          VisClient app = new VisClient("Visualization Client", database);
          return app;
        }
        return null;
      }
    }
    return null;
  }

  /**
   * Create a RaspPiServer application.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return Created application.
   */
  private RasPiServer createRasPiServer(JsonParser parser) {
    List<ISensor> sensors = new ArrayList<ISensor>();
    List<IActor> actors = new ArrayList<IActor>();
    List<String> libraries = new ArrayList<String>();
    DatabaseAccess database = null;
    int timerInterval = 10 * 60;
    RasPiServer.ExportTarget exportTarget = RasPiServer.ExportTarget.DATABASE;
    WebService webService = null;

    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.SENSORS)) {
        sensors = JsonParserSensor.parseSensors(parser);
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.ACTORS)) {
        actors = JsonParserActors.parseActors(parser);
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.LIBRARIES)) {
        libraries = parseLibraries(parser);
        for (String lib : libraries) {
          System.load(lib);
        }
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.DATABASE)) {
        database = JsonParserDatabase.parseDatabase(parser);
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.WEBSERVICE)) {
        webService = JsonParserDatabase.parseWebService(parser);
        if (webService != null) {
          Logger.getInstance().message(webService.toString());
        } else {
          Logger.getInstance().error("Failed to create web service.");
        }
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.TIMER_INTERVAL)) {
        event = parser.next();
        timerInterval = parser.getInt();
      } else if (event == Event.KEY_NAME && parser.getString().equals(JsonConstants.EXPORT_TARGET)) {
        event = parser.next();
        exportTarget = ExportTarget.valueOf(parser.getString());
      } else if (event == Event.END_OBJECT) {
        if (database != null) {
          RasPiServer app = new RasPiServer(timerInterval, database, exportTarget);
          for (String library : libraries) {
            // Libraries must unfortunately be loaded before.
            app.addLibrary(library);
          }
          for (ISensor sensor : sensors) {
            app.addSensor(sensor);
          }
          for (IActor actor : actors) {
            app.addActor(actor);
          }
          // Inform the singletons
          RaspiServerAccessSingleton.getInstance().setRaspiServer(app);
          DatabaseAccessSingleton.getInstance().setDatabaseAccess(database.clone());
          Logger.getInstance().message("Starting web and file server ... ");
          return app;
        }
        return null;
      }
    }
    return null;
  }

  /**
   * Parse the list of libraries.
   * 
   * @param parser
   *          JSON parser at the current position.
   * @return List of library names.
   */
  private List<String> parseLibraries(JsonParser parser) {
    List<String> libraries = new ArrayList<String>();
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.END_ARRAY) {
        return libraries;
      } else if (event == Event.VALUE_STRING) {
        libraries.add(parser.getString());
      }
    }
    return libraries;
  }
}

package homeautomation.jsonparser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import homeautomation.JsonConstants;
import homeautomation.database.DatabaseAccess;
import cgresearch.core.logging.Logger;
import homeautomation.webservice.WebService;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * Parsing of JSON files for database information.
 * 
 * @author Philipp Jenke
 *
 */
public class JsonParserDatabase {

  /**
   * Parse for a @Webservice object.
   * 
   * @param parser
   *          Parser at the correct position.
   * @return Created WebService object.
   */
  public static WebService parseWebService(JsonParser parser) {
    int port = -1;
    String rootPath = "";

    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.PORT)) {
        parser.next();
        try {
          port = Integer.parseInt(parser.getString());
        } catch (Exception ex) {
          Logger.getInstance().exception("Failed to parse webservice port", ex);
        }
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.ROOT_DIR)) {
        parser.next();
        rootPath = parser.getString();
      } else if (event == Event.END_OBJECT) {
        WebService webservice = new WebService(rootPath, port);
        return webservice;
      }
    }
    return null;
  }

  /**
   * Parse a database config.
   * 
   * @param parser
   *          Parser at the correct position.
   * @return Created DatabaseAccess instance.
   */
  public static DatabaseAccess parseDatabase(JsonParser parser) {
    String hostname = null;
    String databaseName = null;
    String tableName = null;
    String username = null;
    String password = null;
    String sshHost = null;
    String sshUsername = null;
    String sshPassword = null;
    int localPort = -1;
    int remotePort = -1;
    while (parser.hasNext()) {
      Event event = parser.next();
      if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_HOSTNAME)) {
        parser.next();
        hostname = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_DATABASE_NAME)) {
        parser.next();
        databaseName = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_TABLENAME)) {
        parser.next();
        tableName = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_USERNAME)) {
        parser.next();
        username = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_PASSWORD)) {
        parser.next();
        password = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_SSH_HOST)) {
        parser.next();
        sshHost = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_SSH_USERNAME)) {
        parser.next();
        sshUsername = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_SSH_PASSWORD)) {
        parser.next();
        sshPassword = parser.getString();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_LOCAL_PORT)) {
        parser.next();
        localPort = parser.getInt();
      } else if (event == Event.KEY_NAME
          && parser.getString().equals(JsonConstants.DB_REMOTE_PORT)) {
        parser.next();
        remotePort = parser.getInt();
      } else if (event == Event.END_OBJECT) {
        DatabaseAccess databaseAccess =
            new DatabaseAccess(hostname, databaseName, tableName, username,
                password, sshHost, sshUsername, sshPassword, localPort,
                remotePort);
        return databaseAccess;
      }
    }
    return null;
  }

  /**
   * Create a database for a string json doc.
   * 
   * @param jsonCode
   *          JSON code.
   * @return Created DatabaseAccess instance.
   */
  public static DatabaseAccess createDatabase(String jsonCode) {
    InputStream is =
        new ByteArrayInputStream(jsonCode.getBytes(Charset.defaultCharset()));
    return createDatabaseAccess(is);
  }

  /**
   * Create a database for an input stream.
   * 
   * @param is
   *          Stream to read the JSON code from.
   * @return Created DatabaseAccess instance.
   */
  private static DatabaseAccess createDatabaseAccess(InputStream is) {
    JsonParser parser = Json.createParser(is);
    return parseDatabase(parser);
  }
}

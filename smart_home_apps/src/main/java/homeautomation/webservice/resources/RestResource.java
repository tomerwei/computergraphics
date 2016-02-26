package homeautomation.webservice.resources;

import java.util.Dictionary;
import java.util.Hashtable;

import org.restlet.resource.ServerResource;

/**
 * Parent class for all rest resources.
 * 
 * @author Philipp Jenke
 */
public class RestResource extends ServerResource {

  /**
   * Parse the request URI and create a dictionary of the parameters (key-value
   * pairs).
   * 
   * @param uri
   *          URI to be used.
   * @return Dictionary for the parameters.
   */
  protected Dictionary<String, String> createParameterDictionary(String uri) {
    Dictionary<String, String> dictionary = new Hashtable<String, String>();
    String parameterString = uri.substring(uri.lastIndexOf("/") + 1);
    String separator = ",";
    String[] tokens = parameterString.split(separator);
    for (String token : tokens) {
      String[] keyValues = token.split("=");
      if (keyValues.length == 2) {
        dictionary.put(keyValues[0], keyValues[1]);
      }
    }
    return dictionary;
  }
}

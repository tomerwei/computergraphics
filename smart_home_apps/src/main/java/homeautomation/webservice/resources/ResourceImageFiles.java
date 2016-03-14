package homeautomation.webservice.resources;

import cgresearch.core.logging.Logger;
import homeautomation.webservice.dataaccess.RaspiServerAccessSingleton;

import java.io.File;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Provides the image files.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceImageFiles extends ServerResource {

  /**
   * Handle GET command.
   * 
   * @return HTML answer.
   */
  @Get
  public Representation get() {
    String filePath = getRequest().getResourceRef().getPath();
    String fileBaseDirectory =
        RaspiServerAccessSingleton.getInstance().getRootPath();
    String absoluteFilename = fileBaseDirectory + filePath;
    if (!new File(absoluteFilename).exists()) {
      Logger.getInstance().error(
          "Cannot find requeste resource " + absoluteFilename);
      return null;
    }
    MediaType mediaType = getMediaType(absoluteFilename);
    FileRepresentation fr =
        new FileRepresentation(new File(absoluteFilename), mediaType);
    return fr;
  }

  /**
   * Determine the media type for the image from the filename.
   * 
   * @param absoluteFilename
   *          Filename of the file to be checked.
   * 
   * @return Media type of the file. Return PNG as default.
   */
  private MediaType getMediaType(String absoluteFilename) {
    MediaType mediaType = MediaType.IMAGE_PNG;
    if (absoluteFilename.toUpperCase().endsWith("JPG")) {
      mediaType = MediaType.IMAGE_JPEG;
    } else if (absoluteFilename.toUpperCase().endsWith("JPEG")) {
      mediaType = MediaType.IMAGE_JPEG;
    } else if (absoluteFilename.toUpperCase().endsWith("PNG")) {
      mediaType = MediaType.IMAGE_PNG;
    } else if (absoluteFilename.toUpperCase().endsWith("GIF")) {
      mediaType = MediaType.IMAGE_GIF;
    }
    return mediaType;
  }
}

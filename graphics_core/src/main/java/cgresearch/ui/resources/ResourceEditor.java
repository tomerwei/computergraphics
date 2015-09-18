package cgresearch.ui.resources;

/**
 * Generic Handler for arbitrary resources; creates corresponding handler.
 * 
 * @author Philipp Jenke
 *
 */
public interface ResourceEditor {
  /**
   * This method is the handler for the resource. It creates an editor.
   * 
   * @param resource
   *          Resource to be handled.
   */
  public void apply(Object resource);
}

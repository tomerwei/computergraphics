package cgresearch.graphics.scenegraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Special node in the scene graph which contains scene information like the
 * light sources.
 * 
 * @author Philipp Jenke
 *
 */
public class CgRootNode extends CgNode {

  /**
   * List of lights
   */
  private List<LightSource> lights = new ArrayList<LightSource>();

  /**
   * Defines whether shadows are allowed in this scene
   */
  private boolean allowShadows = false;

  /**
   * Use blending in the scene.
   */
  private boolean useBlending = false;

  /**
   * Constructor.
   */
  public CgRootNode() {
    super(null, "root");
  }

  /**
   * Setter
   */
  public void clearLights() {
    lights.clear();
    lightingChanged();
  }

  /**
   * Setter.
   */
  public void addLight(LightSource light) {
    lights.add(light);
    lightingChanged();
  }

  /**
   * Getter.
   */
  public int getNumberOfLights() {
    return lights.size();
  }

  /**
   * Getter.
   */
  public LightSource getLight(int index) {
    return lights.get(index);
  }

  /**
   * Lighting situation changed.
   */
  public void lightingChanged() {
    setChanged();
    notifyObservers();
  }

  /**
   * Getter
   */
  public boolean areShadowsAllowed() {
    return allowShadows;
  }

  /**
   * Setter
   */
  public void setAllowShadows(boolean allowShadows) {
    this.allowShadows = allowShadows;
  }

  public boolean getUseBlending() {
    return useBlending;
  }

  public void setUseBlending(boolean useBlending) {
    this.useBlending = useBlending;
  }
}

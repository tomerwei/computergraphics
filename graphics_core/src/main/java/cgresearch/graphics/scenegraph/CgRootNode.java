package cgresearch.graphics.scenegraph;

import cgresearch.rendering.core.RendererOptions;

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
   * Indicates that view frustum culling should be used.
   */
  private boolean useViewFrustumCulling = false;

  /**
   * Show frames per second
   */
  private boolean showFps = false;

  /**
   * Renderer options for the node.
   */
  private RendererOptions rendererOptions;

  /**
   * Constructor.
   */
  public CgRootNode() {
    super(null, "root");

    //Set the default renderer options.
    rendererOptions = RendererOptions.defaultRendererOptions();
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

  public boolean useViewFrustumCulling() {
    return useViewFrustumCulling;
  }

  public void setUseViewFrustumCulling(boolean value) {
    useViewFrustumCulling = value;
  }

  public boolean isShowFps() {
    return showFps;
  }

  public void setShowFps(boolean showFps) {
    this.showFps = showFps;
  }

  /**
   * Getter.
   * @return the renderer options object associated with this node
   */
  public RendererOptions getRendererOptions() {
    return rendererOptions;
  }
}

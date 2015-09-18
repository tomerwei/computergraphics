/**
 * Pro
 * v. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jmonkey;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.bricks.IRenderFrame;
import cgresearch.graphics.material.IGlslShaderCompiler;
import cgresearch.graphics.scenegraph.CgNodeStateChange;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.rendering.core.IRenderObjectsFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * Default frame for jMonkey applications.
 * 
 * @author Philipp Jenke
 * 
 */
public class JMonkeyFrame implements IRenderFrame<Node>, Observer {

  /**
   * JMonkey frame.
   */
  private SimpleApplication app;

  /**
   * Render object manager.
   */
  private final JMonkeyRenderObjectManager renderObjectManager;

  /**
   * Reference to the base application
   */
  private final CgApplication cgApplication;

  /**
   * Constructor
   */
  public JMonkeyFrame(CgApplication application) {
    this.cgApplication = application;
    // application.getCgRootNode().addObserver(this);
    app = new SimpleApplication() {
      @Override
      public void simpleInitApp() {
        registerRenderObjectsFactory(new JMonkeyRenderObjectFactoryPointCloud(assetManager));
        registerRenderObjectsFactory(new JMonkeyRenderObjectFactoryTriangleMesh(assetManager));

        // Set lights
        updateLights();

        // FilterPostProcessor fpp = new
        // FilterPostProcessor(assetManager);
        // SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f,
        // 0.61f);
        // fpp.addFilter(ssaoFilter);
        // viewPort.addProcessor(fpp);

        // Create render nodes
        Logger.getInstance().debug("Initially create JOGL render nodes for scene graph.");
        renderObjectManager.update(cgApplication.getCgRootNode(),
            CgNodeStateChange.makeAddChild(null, cgApplication.getCgRootNode()));
      }

    };

    renderObjectManager = new JMonkeyRenderObjectManager(application.getCgRootNode(), app.getRootNode());
    app.setShowSettings(false);

    setFullscreen();

    app.start();
  }

  private void setFullscreen() {

    AppSettings settings = new AppSettings(true);
    settings.setResolution(640, 480);
    GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    DisplayMode[] modes = device.getDisplayModes();
    int i = 0; // note: there are usually several, let's pick the first
    settings.setResolution(modes[i].getWidth(), modes[i].getHeight());
    settings.setFrequency(modes[i].getRefreshRate());
    settings.setBitsPerPixel(modes[i].getBitDepth());
    settings.setFullscreen(device.isFullScreenSupported());
    app.setSettings(settings);
  }

  /**
   * Set all the lights.
   */
  private void updateLights() {
    Logger.getInstance().debug("Updated lights.");

    // Remove all lights
    app.getRootNode().getLocalLightList().clear();

    // Add lights from scene graph
    CgRootNode cgRootNode = cgApplication.getCgRootNode();
    for (int i = 0; i < cgRootNode.getNumberOfLights(); i++) {
      LightSource lightSource = cgRootNode.getLight(i);
      switch (lightSource.getType()) {
        case DIRECTIONAL:
          DirectionalLight directionalLight = new DirectionalLight();
          directionalLight.setDirection(new Vector3f((float) lightSource.getDirection().get(0),
              (float) lightSource.getDirection().get(1), (float) lightSource.getDirection().get(2)));
          directionalLight.setColor(new ColorRGBA((float) lightSource.getDiffuseColor().get(0),
              (float) lightSource.getDiffuseColor().get(1), (float) lightSource.getDiffuseColor().get(2), 1));
          app.getRootNode().addLight(directionalLight);
          break;
        case POINT:
          PointLight pointLight = new PointLight();
          pointLight.setPosition(new Vector3f((float) lightSource.getPosition().get(0),
              (float) lightSource.getPosition().get(1), (float) lightSource.getPosition().get(2)));
          pointLight.setColor(new ColorRGBA((float) lightSource.getDiffuseColor().get(0),
              (float) lightSource.getDiffuseColor().get(1), (float) lightSource.getDiffuseColor().get(2), 1));
          app.getRootNode().addLight(pointLight);
          break;
        default:
          Logger.getInstance().error("Unsupported light type: " + lightSource.getType());
          break;
      }
    }

    // AmbientLight al = new AmbientLight();
    // al.setColor(new ColorRGBA(1.8f, 1.8f, 1.8f, 1.0f));
    // app.getRootNode().addLight(al);

  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.rendering.IRenderFrame#registerRenderObjectsFactory(edu.haw
   * .cg.rendering.IRenderObjectsFactory)
   */
  @Override
  public void registerRenderObjectsFactory(IRenderObjectsFactory<Node> factory) {
    renderObjectManager.registerRenderObjectsFactory(factory);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof CgRootNode) {
      updateLights();
    }
  }

  @Override
  public IGlslShaderCompiler getShaderCompiler() {
    return null;
  }
}

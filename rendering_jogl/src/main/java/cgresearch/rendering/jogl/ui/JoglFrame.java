/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.rendering.jogl.ui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.bricks.IRenderFrame;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.camera.Camera.ControllerType;
import cgresearch.graphics.material.IGlslShaderCompiler;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.misc.MovieExport;
import cgresearch.rendering.core.IRenderObjectsFactory;
import cgresearch.rendering.jogl.camera.CameraPathController;
import cgresearch.rendering.jogl.camera.FirstPersonCameraController;
import cgresearch.rendering.jogl.camera.InspectionCameraController;
import cgresearch.rendering.jogl.camera.MoveableInspectionCameraController;
import cgresearch.rendering.jogl.core.JoglRenderNode;
import cgresearch.rendering.jogl.core.JoglRenderObjectManager;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.rendering.jogl.material.JoglShaderCompiler;

public class JoglFrame extends JFrame implements IRenderFrame<JoglRenderNode>, Observer {

  /**
   * 
   */
  private static final long serialVersionUID = -4152553034524319804L;

  /**
   * Action command.
   */
  public static enum ActionCommands {
    ACTION_COMMAND_TAKE_SCREENSHOT
  };

  /**
   * Render object manager.
   */
  private final JoglRenderObjectManager renderObjectManager;

  /**
   * Reference to the view
   */
  private JoglCanvas view;

  /**
   * Compiler for JOGL shaders (used by the shader editor).
   */
  private JoglShaderCompiler shaderCompiler = new JoglShaderCompiler();

  /**
   * Default constructor.
   */
  public JoglFrame(CgApplication application) {
    // Setup a default scene graph
    renderObjectManager = new JoglRenderObjectManager(application.getCgRootNode());
    GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
    capabilities.setStencilBits(8);
    view = new JoglCanvas(application.getCgRootNode(), renderObjectManager, capabilities);
    Logger.getInstance().debug("Stencil bits: " + capabilities.getStencilBits());
    getContentPane().add(view);

    AnimationTimer.getInstance().addObserver(this);

    // Register controller
    Camera.getInstance().registerController(ControllerType.INSPECTION, new InspectionCameraController());
    Camera.getInstance().registerController(ControllerType.MOVEABLE_INSPECTION,
        new MoveableInspectionCameraController());
    Camera.getInstance().registerController(ControllerType.CAMERA_PATH,
        new CameraPathController(Camera.getInstance().getCameraPathInterpolator()));
    Camera.getInstance().registerController(ControllerType.FIRST_PERSON, new FirstPersonCameraController());

    view.addRenderable(shaderCompiler);

    add(new QuickOptionToolBar(), BorderLayout.WEST);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    requestFocus();
    setSize(640, 480);
    setVisible(true);
    setLocation(400, 0);
  }

  /**
   * Register an additional render objects factory.
   * 
   * @param factory
   *          Corresponding factory.
   */
  public void registerRenderObjectsFactory(IRenderObjectsFactory<JoglRenderNode> factory) {
    renderObjectManager.registerRenderObjectsFactory(factory);
  }

  /**
   * Save a screenshot at the next redraw to the specified file.
   */
  protected void takeScreenshot(String filename) {
    view.takeScreenshot(filename);
  }

  @Override
  public void update(Observable o, Object arg) {
    // Animation timer tick.
    if (o instanceof AnimationTimer) {
      // Check if a screenshot is required for the movie export.
      if (MovieExport.getInstance().isRunning()) {
        takeScreenshot(MovieExport.getInstance().getCurrentFrameFilename());
      }
    }
  }

  /**
   * Add additional renderable.
   * 
   * @param renderable
   */
  public void addRenderable(JoglRenderable renderable) {
    view.addRenderable(renderable);
  }

  @Override
  public IGlslShaderCompiler getShaderCompiler() {
    return shaderCompiler;
  }
}

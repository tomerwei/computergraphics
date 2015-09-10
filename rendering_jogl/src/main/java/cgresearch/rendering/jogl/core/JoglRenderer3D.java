/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.rendering.jogl.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.Animation;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgNodeStateChange;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.rendering.jogl.misc.PickingRenderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLReadBufferUtil;

/**
 * Implements the 3D rendering functionality.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderer3D implements Observer {

  /**
   * Constants
   */
  private static final double NEAR_CLIPPING_PLANE_DEFAULT =
      0.1;
  private static final double FAR_CLIPPING_PLANE_DEFAULT =
      10.0;
  private static final double NEAR_CLIPPING_FACTOR = 0.1;
  private static final double FAR_CLIPPING_FACTOR = 3.0;
  private static final int SCREEN_WIDTH = 640;
  private static final int SCREEN_HEIGHT = 480;
  private static final float OPAQUE = 1.0f;
  private static final IVector3 CLEAR_COLOR =
      VectorMatrixFactory.newIVector3(1, 1, 1);

  /**
   * Screen aspect ration
   */
  private double aspectRatio = 1;

  /**
   * Near clipping plane.
   */
  private double nearClippingPlane =
      NEAR_CLIPPING_PLANE_DEFAULT;

  /**
   * Far clipping plane.
   */
  private double farClippingPlane =
      FAR_CLIPPING_PLANE_DEFAULT;

  /**
   * Screenshot filename. A screenshot is saved, when this variable is not the
   * empty string.
   */
  private String screenshotFilename = "";

  private int JOGL_NUMBER_OF_LIGHTS = 8;

  /**
   * Reference to the scene graph root node.
   */
  private final CgRootNode rootNode;

  /**
   * Reference to the root node.
   */
  private final JoglRenderObjectManager renderObjectMananger;

  /**
   * GLU instance
   */
  private GLU glu = null;

  /**
   * Set this flag to true, if intrinsic camera parameters need to be updated.
   */
  private boolean updateIntrinsicCameraParametersRequired =
      true;

  /**
   * Set this flag to true, if extrinsic camera parameters need to be updated.
   */
  private boolean updateExtrinsicCameraParametersRequired =
      true;

  /**
   * Reference to the frame which contains the view.
   */
  private final List<JoglRenderable> renderables =
      new ArrayList<JoglRenderable>();

  /**
   * This flag indicated that the lights need to be updated.
   */
  private boolean updateLightsRequired = true;

  /**
   * Constructor.
   */
  public JoglRenderer3D(
      JoglRenderObjectManager renderObjectManager,
      CgRootNode rootNode) {
    this.renderObjectMananger = renderObjectManager;
    this.rootNode = rootNode;
    new PickingRenderer(rootNode);
    rootNode.addObserver(this);
    Camera.getInstance().addObserver(this);
    glu = new GLU();
  }

  public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();

    // Perspective
    final int width = SCREEN_WIDTH;
    final int height = SCREEN_HEIGHT;
    aspectRatio = (float) width / (float) height;
    glu.gluPerspective(Camera.getInstance()
        .getOpeningAngle(), aspectRatio, nearClippingPlane,
        farClippingPlane);

    // Viewport
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

    // Depth
    gl.glEnable(GL2.GL_DEPTH_TEST);

    // which is the front? the one which is drawn counter clockwise
    gl.glFrontFace(GL2.GL_CCW);
    // which one should NOT be drawn
    gl.glCullFace(GL2.GL_FRONT);

    // Enable buffers
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
    gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
    gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);

    // define the color we want to be displayed as the "clipping wall"
    gl.glClearColor((float) (CLEAR_COLOR.get(0)),
        (float) (CLEAR_COLOR.get(1)),
        (float) (CLEAR_COLOR.get(2)), OPAQUE);

    // Enable lighting
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glLightModelf(GL2.GL_LIGHT_MODEL_TWO_SIDE,
        GL2.GL_TRUE);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

    // Set some lights
    updateLights(gl);

    // Texturing
    gl.glTexParameterf(GL2.GL_TEXTURE_2D,
        GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
    gl.glTexParameterf(GL2.GL_TEXTURE_2D,
        GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

    // Shading
    gl.glEnable(GLLightingFunc.GL_NORMALIZE);

    updateExtrinsicCameraParameters(drawable);

    // Create render nodes
    Logger
        .getInstance()
        .debug(
            "Initially create JOGL render nodes for scene graph.");
    renderObjectMananger.update(rootNode,
        CgNodeStateChange.makeAddChild(null, rootNode));
  }

  /**
   * Set the current lights
   */
  private void updateLights(GL2 gl) {
    if (updateLightsRequired) {
      Logger.getInstance().debug("Updated lights.");

      // Disable inactive lights
      for (int i = 0; i < JOGL_NUMBER_OF_LIGHTS; i++) {
        gl.glDisable(getLightIndex(i));
        int lightIndex = getLightIndex(i);
        gl.glLightfv(lightIndex, GL2.GL_DIFFUSE,
            new float[] { -1, -1, -1, -1 }, 0);
      }

      // Set the active lights
      for (int i = 0; i < rootNode.getNumberOfLights(); i++) {
        int lightIndex = getLightIndex(i);
        LightSource light = rootNode.getLight(i);
        float lightAmbient[] = { 1, 1, 1, 1 };
        float lightSpecular[] = { 1, 1, 1, 1 };
        float lightPosition[] = { 1, 1, 1, 1 };
        float lightDiffuse[] = { 1, 1, 1, 1 };
        gl.glEnable(lightIndex);
        gl.glLightfv(lightIndex, GL2.GL_AMBIENT,
            lightAmbient, 0);
        gl.glLightfv(lightIndex, GL2.GL_SPECULAR,
            lightSpecular, 0);
        switch (light.getType()) {
          case DIRECTIONAL:
            lightPosition[0] =
                (float) light.getPosition().get(0);
            lightPosition[1] =
                (float) light.getPosition().get(1);
            lightPosition[2] =
                (float) light.getPosition().get(2);
            lightPosition[3] = 0;
            lightDiffuse[0] =
                (float) light.getDiffuseColor().get(0);
            lightDiffuse[1] =
                (float) light.getDiffuseColor().get(1);
            lightDiffuse[2] =
                (float) light.getDiffuseColor().get(2);
            lightDiffuse[3] = 1;
            gl.glEnable(lightIndex);
            gl.glLightfv(lightIndex, GL2.GL_POSITION,
                lightPosition, 0);
            gl.glLightfv(lightIndex, GL2.GL_DIFFUSE,
                lightDiffuse, 0);

            break;
          case POINT:
            lightPosition[0] =
                (float) light.getPosition().get(0);
            lightPosition[1] =
                (float) light.getPosition().get(1);
            lightPosition[2] =
                (float) light.getPosition().get(2);
            lightPosition[3] = 1;
            lightDiffuse[0] =
                (float) light.getDiffuseColor().get(0);
            lightDiffuse[1] =
                (float) light.getDiffuseColor().get(1);
            lightDiffuse[2] =
                (float) light.getDiffuseColor().get(2);
            lightDiffuse[3] = 1;
            gl.glEnable(lightIndex);
            gl.glLightfv(lightIndex, GL2.GL_POSITION,
                lightPosition, 0);
            gl.glLightfv(lightIndex, GL2.GL_DIFFUSE,
                lightDiffuse, 0);
            break;
          default:
            Logger.getInstance().error(
                "Unsupported light type: "
                    + light.getType());
            break;
        }
      }
      updateLightsRequired = false;
    }
  }

  public void resize(GLAutoDrawable drawable, int w, int h) {
    GL2 gl = drawable.getGL().getGL2();
    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();
    aspectRatio = (float) w / (float) h;
    glu.gluPerspective(Camera.getInstance()
        .getOpeningAngle(), aspectRatio, nearClippingPlane,
        farClippingPlane);
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    updateExtrinsicCameraParameters(drawable);
  }

  /**
   * Draw content
   */
  public void draw(GLAutoDrawable drawable) {
    // clear the color buffer and the depth buffer
    GL2 gl = drawable.getGL().getGL2();

    gl.glClear(GL.GL_COLOR_BUFFER_BIT
        | GL.GL_DEPTH_BUFFER_BIT);

    // Update intrinsic camera settings
    updateIntrinsicCameraParameters(gl);

    // Update extrinsic camera
    updateExtrinsicCameraParameters(drawable);

    // Set some lights
    updateLights(gl);

    // Render all registered renderables.
    for (JoglRenderable renderable : renderables) {
      renderable.draw3D(gl);
    }

    // Render the scene graph
    renderNode(rootNode, gl);

    // Render the current camera path
    renderCameraPath(gl);

    // Take screenshot
    checkTakeScreenshot(drawable);

    JoglHelper.hasGLError(gl, "GL rendering");
  }

  /**
   * Update the intrinsic camera parameters.
   */
  private void updateIntrinsicCameraParameters(GL2 gl) {
    if (updateIntrinsicCameraParametersRequired) {
      if (gl != null) {
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(Camera.getInstance()
            .getOpeningAngle(), aspectRatio,
            nearClippingPlane, farClippingPlane);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      }
      updateIntrinsicCameraParametersRequired = false;
    }
  }

  /**
   * Update the GL matrix stack based on the camera.
   */
  private void updateExtrinsicCameraParameters(
      GLAutoDrawable drawable) {
    if (updateExtrinsicCameraParametersRequired) {
      GL2 gl = drawable.getGL().getGL2();
      gl.glLoadIdentity();
      final float eyeX =
          (float) Camera.getInstance().getEye().get(0);
      final float eyeY =
          (float) Camera.getInstance().getEye().get(1);
      final float eyeZ =
          (float) Camera.getInstance().getEye().get(2);
      final float refX =
          (float) Camera.getInstance().getRef().get(0);
      final float refY =
          (float) Camera.getInstance().getRef().get(1);
      final float refZ =
          (float) Camera.getInstance().getRef().get(2);
      final float upX =
          (float) Camera.getInstance().getUp().get(0);
      final float upY =
          (float) Camera.getInstance().getUp().get(1);
      final float upZ =
          (float) Camera.getInstance().getUp().get(2);
      glu.gluLookAt(eyeX, eyeY, eyeZ, refX, refY, refZ,
          upX, upY, upZ);
      updateExtrinsicCameraParametersRequired = false;
    }
  }

  /**
   * Check if a screenshot needs to be taken, do so if yes.
   */
  private void checkTakeScreenshot(GLAutoDrawable drawable) {
    if (screenshotFilename.length() > 0) {
      GLReadBufferUtil screenshot =
          new GLReadBufferUtil(true, false);
      if (screenshot.readPixels(drawable.getGL(), false)) {
        screenshot.write(new File(screenshotFilename));
        Logger.getInstance().message(
            "Saved screenshot to file "
                + screenshotFilename);
      } else {
        Logger.getInstance().error(
            "Failed to write screenshot file.");
      }
      screenshotFilename = "";
    }
  }

  /**
   * Render the current camera path.
   */
  private void renderCameraPath(GL2 gl) {
    // Render camera path
    if (Camera.getInstance().showCameraPath()) {
      int RESOLUTION = 500;
      gl.glDisable(GL2.GL_LIGHTING);
      gl.glBegin(GL2.GL_LINE_STRIP);
      gl.glPointSize(2);
      gl.glColor3f(0.15f, 0.15f, 0.15f);
      for (int i = 0; i < RESOLUTION; i++) {
        float t = (float) i / (float) (RESOLUTION + 1);
        IVector3 p =
            Camera.getInstance()
                .getCameraPathInterpolation(t);
        gl.glVertex3d(p.get(0), p.get(1), p.get(2));
      }
      gl.glEnd();
    }
  }

  /**
   * Recursive method to render nodes.
   */
  private void renderNode(CgNode node, GL2 gl) {
    if (node == null) {
      return;
    }
    if (!node.isVisible()) {
      return;
    }

    if (Camera.getInstance().getCenterViewRequired()) {
      setViewFromBoundingBox();
    }

    // Draw node
    JoglRenderNode renderNode =
        renderObjectMananger.getRenderNode(node);
    if (renderNode != null) {
      renderNode.draw3D(gl);
    }

    // Draw children
    for (int childIndex = 0; childIndex < node
        .getNumChildren(); childIndex++) {
      if (node.getContent() instanceof Animation) {
        // Special case: animation node, only render current time step.
        if (AnimationTimer.getInstance().getValue() == childIndex) {
          renderNode(node.getChildNode(childIndex), gl);
        }
      } else {
        // Non-animation node
        renderNode(node.getChildNode(childIndex), gl);
      }
    }

    if (renderNode != null) {
      renderNode.afterDraw(gl);
    }

  }

  public void setClipplingPlanes(double near, double far) {
    nearClippingPlane = near;
    farClippingPlane = far;
    updateIntrinsicCameraParametersRequired = true;
  }

  /**
   * Get the bounding box of all visible scene content.
   */
  public BoundingBox getCurrentBoundingBox() {
    BoundingBox boundingBox = rootNode.getBoundingBox();
    for (JoglRenderable renderable : renderables) {
      if (renderable.getBoundingBox() != null
          && renderable.getBoundingBox().isInitialized()) {
        boundingBox.unite(renderable.getBoundingBox());
      }
    }
    return boundingBox;
  }

  /**
   * Setter.
   */
  public void setScreenshotFilename(String filename) {
    screenshotFilename = filename;
  }

  /**
   * Setter.
   */
  public void addRenderable(JoglRenderable renderable) {
    renderables.add(renderable);
  }

  /**
   * Returns the light index constant for an index
   */
  private int getLightIndex(int i) {
    switch (i) {
      case 0:
        return GLLightingFunc.GL_LIGHT0;
      case 1:
        return GLLightingFunc.GL_LIGHT1;
      case 2:
        return GLLightingFunc.GL_LIGHT2;
      case 3:
        return GLLightingFunc.GL_LIGHT3;
      case 4:
        return GLLightingFunc.GL_LIGHT4;
      case 5:
        return GLLightingFunc.GL_LIGHT5;
      case 6:
        return GLLightingFunc.GL_LIGHT6;
      case 7:
        return GLLightingFunc.GL_LIGHT7;
      default:
        return GLLightingFunc.GL_LIGHT0;
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof Camera) {
      updateExtrinsicCameraParametersRequired = true;
    } else if (o instanceof CgRootNode) {
      updateLightsRequired = true;
    }
  }

  /**
   * Setup the camera based to 'see' the accumulated bounding box of the scene.
   */
  public void setViewFromBoundingBox() {
    BoundingBox boundingBox = getCurrentBoundingBox();
    if (boundingBox.isInitialized()) {
      final double diameter = boundingBox.getDiameter();
      setClipplingPlanes(diameter * NEAR_CLIPPING_FACTOR,
          diameter * FAR_CLIPPING_FACTOR);
      Camera.getInstance().getCurrentController()
          .fitToBoundingBox(boundingBox);
    } else {
      Logger
          .getInstance()
          .message(
              "Cannot center view - no visible nodes with valid bounding boxes.");
    }
  }
}

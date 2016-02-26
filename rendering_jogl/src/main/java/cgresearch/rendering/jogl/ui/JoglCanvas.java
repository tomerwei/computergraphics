/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.rendering.jogl.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.picking.Picking;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.rendering.jogl.core.JoglRenderObjectManager;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.rendering.jogl.core.JoglRenderer3D;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * This class represents a view for 3D content.
 * 
 * @author Philipp Jenke
 */
public class JoglCanvas extends GLJPanel
    implements GLEventListener, MouseListener, MouseWheelListener, MouseMotionListener, KeyListener, Observer {

  /**
   * Constants.
   */
  private static final long serialVersionUID = 1L;
  private static final int FPS = 60;
  private static final int KEY_IS_PRESSED_UPDATE_INTERVAL = 50;
  private static final char KEY_PRESSED_NONE = ' ';

  /**
   * This thread is used to inform the camera controller that a key is currently
   * pressed.
   */
  private Thread keyIsPressed = null;

  /**
   * Currently pressed key.
   */
  private char currentlyPressedKey = KEY_PRESSED_NONE;

  /**
   * Renderer object
   */
  private final JoglRenderer3D renderer3d;

  /**
   * Last coordinates of the mouse
   */
  private Vector lastMouseCoordinates = VectorFactory.createVector3(-1, -1, 0);

  /**
   * Remember last clicked button.
   */
  private int currentButton = -1;

  /**
   * Constructor
   */
  public JoglCanvas(CgRootNode rootNode, JoglRenderObjectManager renderObjectManager, GLCapabilities capabilities) {
    super(capabilities);
    renderer3d = new JoglRenderer3D(renderObjectManager, rootNode);
    addGLEventListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    FPSAnimator animator = new FPSAnimator(this, FPS, true);
    animator.start();
    AnimationTimer.getInstance().addObserver(this);
  }

  @Override
  public void display(GLAutoDrawable drawable) {
    renderer3d.draw(drawable);
  }

  @Override
  public void dispose(GLAutoDrawable arg0) {
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    renderer3d.init(drawable);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    renderer3d.resize(drawable, w, h);
  }

  @Override
  public void mouseClicked(MouseEvent event) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent event) {
    requestFocus();
    currentButton = event.getButton();
    if (event.getButton() == MouseEvent.BUTTON1) {
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    }
  }

  @Override
  public void mouseReleased(MouseEvent event) {
    currentButton = -1;
    lastMouseCoordinates = VectorFactory.createVector3(event.getX(), event.getY(), 0);

    if (Picking.getInstance().isActive()) {
      Picking.getInstance().handleSelectionClick(event.getX(), event.getY(), getWidth(), getHeight(),
          Camera.getInstance().getOpeningAngle());
    }
  }

  @Override
  public void mouseDragged(MouseEvent event) {
    if (currentButton == MouseEvent.BUTTON1) {
      if ((lastMouseCoordinates.get(0) > 0) && (lastMouseCoordinates.get(1) > 0)) {
        float deltaX = (float) (event.getX() - lastMouseCoordinates.get(0));
        float deltaY = (float) (event.getY() - lastMouseCoordinates.get(1));

        if (Picking.getInstance().isActive()) {
          if (currentlyPressedKey == 'x') {
            Picking.getInstance().moveX(deltaX);
          } else if (currentlyPressedKey == 'y') {
            Picking.getInstance().moveY(deltaX);
          } else if (currentlyPressedKey == 'z') {
            Picking.getInstance().moveZ(deltaX);
          }
        } else {
          // Regular camera controller
          Camera.getInstance().getCurrentController().mouseDeltaXLeftButton(deltaX);
          Camera.getInstance().getCurrentController().mouseDeltaYLeftButton(deltaY);
        }

      }
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    } else if (currentButton == MouseEvent.BUTTON3) {
      if ((lastMouseCoordinates.get(0) > 0) && (lastMouseCoordinates.get(1) > 0)) {
        Camera.getInstance().getCurrentController()
            .mouseDeltaXRightButton((float) (event.getX() - lastMouseCoordinates.get(0)));
        Camera.getInstance().getCurrentController()
            .mouseDeltaYRightButton((float) (event.getY() - lastMouseCoordinates.get(1)));
      }
      lastMouseCoordinates.set(0, event.getX());
      lastMouseCoordinates.set(1, event.getY());
    }
  }

  @Override
  public void mouseMoved(MouseEvent event) {
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent event) {
    Camera.getInstance().getCurrentController().mouseWheelMoved(event.getWheelRotation());
  }

  @Override
  public void keyPressed(KeyEvent e) {
    final int keyCode = e.getKeyCode();
    currentlyPressedKey = e.getKeyChar();
    if (keyIsPressed == null) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          while (!Thread.currentThread().isInterrupted()) {
            Camera.getInstance().getCurrentController().keyIsPressed(keyCode);
            try {
              Thread.sleep(KEY_IS_PRESSED_UPDATE_INTERVAL);
            } catch (InterruptedException e) {
              break;
            }
          }
        }
      };
      keyIsPressed = new Thread(runnable);
      keyIsPressed.start();
    }

    Camera.getInstance().getCurrentController().keyDown(e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {

    currentlyPressedKey = KEY_PRESSED_NONE;

    if (keyIsPressed != null) {
      keyIsPressed.interrupt();
      keyIsPressed = null;
    }

    if (e.getKeyChar() == '+') {
      Camera.getInstance().getCurrentController().mouseDeltaYRightButton(-1);
    } else if (e.getKeyChar() == '-') {
      Camera.getInstance().getCurrentController().mouseDeltaYRightButton(1);
    } else if (e.getKeyChar() == 't') {
      takeScreenshot();
    } else if (e.getKeyChar() == 'c') {
      renderer3d.setViewFromBoundingBox();
    } else if (e.getKeyChar() == '1') {
      AnimationTimer.getInstance().startTimer(100);
    } else if (e.getKeyChar() == '2') {
      AnimationTimer.getInstance().stop();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  /**
   * Take a screenshot of the GL canvas. If a file gets selected, the filename
   * is saved in the member variable screenshotFilename. The next time, the
   * scene is redrawn, the snapshot is taken and saved to that filename.
   */
  void takeScreenshot() {
    // Select file an save
    final JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setMultiSelectionEnabled(false);
    fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "PNG, JPG"));
    int returnVal = fc.showSaveDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      final File screenshotFile = fc.getSelectedFile();
      if (screenshotFile != null) {
        takeScreenshot(screenshotFile.getAbsolutePath());
      }
    }
  }

  /**
   * Take a screenshot and save it to the specified file at the next redraw.
   * 
   * @param filename
   */
  void takeScreenshot(String filename) {
    renderer3d.setScreenshotFilename(filename);
  }

  /**
   * Setter.
   */
  public void addRenderable(JoglRenderable renderable) {
    renderer3d.addRenderable(renderable);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof AnimationTimer) {
      Camera.getInstance().getCurrentController().animationTimerTick();
    }
  }

}
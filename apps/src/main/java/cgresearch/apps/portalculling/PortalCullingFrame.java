/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.portalculling;

import java.util.List;
import java.util.Observer;
import java.util.Set;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.projects.portalculling.PortalCell;
import cgresearch.projects.portalculling.PortalEdge;
import cgresearch.projects.portalculling.PortalScene2D;
import cgresearch.projects.portalculling.PortalSceneImporter;
import cgresearch.projects.portalculling.ViewVolume2D;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.rendering.jogl.material.JoglTexture;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Camera path in a virtual scene.
 * 
 * @author Philipp Jenke
 * 
 */
public class PortalCullingFrame extends CgApplication implements Observer, JoglRenderable {

  /**
   * The scene with the portals.
   */
  private PortalScene2D scene;

  /**
   * View volume to be analyzed.
   */
  private ViewVolume2D viewVolume;

  /**
   * The texture is used to show the walls of the cells.
   */
  private Texture texture = null;

  /**
   * Render settings for the portal culling.
   */
  private PortalCullingSettings settings = new PortalCullingSettings();

  /**
   * Constructor.
   */
  public PortalCullingFrame() {
    scene = new PortalScene2D();
    PortalSceneImporter importer = new PortalSceneImporter();
    importer.importScene(scene, "portal/scene.scene");

    viewVolume = new ViewVolume2D(VectorMatrixFactory.newIVector3(2, 0, 6), VectorMatrixFactory.newIVector3(6, 0, 6),
        VectorMatrixFactory.newIVector3(7, 0, -1));

    AnimationTimer.getInstance().setMaxValue(50);
  }

  @Override
  public void draw3D(GL2 gl) {
    Set<Integer> pvs = scene.computePvs(viewVolume);

    gl.glDisable(GL2.GL_LIGHTING);

    // Render scene
    if (settings.showCells) {
      gl.glColor3f(0.1f, 0.1f, 0.1f);
      for (int edgeIndex = 0; edgeIndex < scene.getNumberOfEdges(); edgeIndex++) {
        PortalEdge edge = scene.getEdge(edgeIndex);
        IVector3 startNode = scene.getNode(edge.getStartNodeIndex());
        IVector3 endNode = scene.getNode(edge.getEndNodeIndex());
        if (edge.getState() == PortalEdge.State.WALL) {
          gl.glLineWidth(4);
        } else {
          gl.glLineWidth(1);
        }
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3fv(startNode.floatData(), 0);
        gl.glVertex3fv(endNode.floatData(), 0);
        gl.glEnd();
      }
    }

    // Render view volume
    if (settings.showViewVolume) {
      gl.glBegin(GL2.GL_LINES);
      gl.glColor3f(0.25f, 0.25f, 0.75f);
      gl.glVertex3fv(viewVolume.getOrigin().floatData(), 0);
      gl.glVertex3fv(viewVolume.getOrigin().add(viewVolume.getLeftBoundary()).floatData(), 0);
      gl.glVertex3fv(viewVolume.getOrigin().floatData(), 0);
      gl.glVertex3fv(viewVolume.getOrigin().add(viewVolume.getRightBoundary()).floatData(), 0);
      gl.glEnd();
    }

    // Show pvs
    if (settings.showPvs) {
      gl.glBegin(GL2.GL_TRIANGLES);
      gl.glColor3f(0.3f, 0.6f, 0.3f);
      for (Integer cellIndex : pvs) {
        PortalCell cell = scene.getCell(cellIndex);
        List<IVector3> cellNodes = scene.getCellNodes(cell);
        for (int i = 0; i < cellNodes.size(); i++) {
          IVector3 p = cellNodes.get(i);
          gl.glVertex3d(p.get(0), 0.01, p.get(2));
        }
      }
      gl.glEnd();
    }

    // Show wall walls in the scene.
    if (settings.showAllWalls) {
      if (texture == null) {
        texture = JoglTexture.createTexture("textures/bricks.jpg");
      }
      if (texture != null) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject(gl));
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
      }
      for (int cellIndex = 0; cellIndex < scene.getNumberOfCells(); cellIndex++) {
        PortalCell cell = scene.getCell(cellIndex);
        for (int i = 0; i < 3; i++) {
          int edgeIndex = cell.getEdgeIndex(i);
          PortalEdge edge = scene.getEdge(edgeIndex);
          IVector3 v0 = scene.getNode(edge.getStartNodeIndex());
          IVector3 v1 = scene.getNode(edge.getEndNodeIndex());
          if (edge.getState() == PortalEdge.State.WALL) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1, 1, 1);
            gl.glTexCoord2f(0, 0);
            gl.glVertex3d(v0.get(0), v0.get(1), v0.get(2));
            gl.glTexCoord2f(1, 0);
            gl.glVertex3d(v1.get(0), v1.get(1), v1.get(2));
            gl.glTexCoord2f(1, 1);
            gl.glVertex3d(v1.get(0), -2, v1.get(2));
            gl.glTexCoord2f(0, 1);
            gl.glVertex3d(v0.get(0), -2, v0.get(2));
            gl.glEnd();
          }
        }
      }
      gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    // Show the walls of the PVS
    if (settings.showPvsWalls) {
      if (texture == null) {
        texture = JoglTexture.createTexture("textures/bricks.jpg");
      }
      if (texture != null) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject(gl));
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
      }
      for (Integer cellIndex : pvs) {
        PortalCell cell = scene.getCell(cellIndex);
        for (int i = 0; i < 3; i++) {
          int edgeIndex = cell.getEdgeIndex(i);
          PortalEdge edge = scene.getEdge(edgeIndex);
          IVector3 v0 = scene.getNode(edge.getStartNodeIndex());
          IVector3 v1 = scene.getNode(edge.getEndNodeIndex());
          if (edge.getState() == PortalEdge.State.WALL) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1, 1, 1);
            gl.glTexCoord2f(0, 0);
            gl.glVertex3d(v0.get(0), v0.get(1), v0.get(2));
            gl.glTexCoord2f(1, 0);
            gl.glVertex3d(v1.get(0), v1.get(1), v1.get(2));
            gl.glTexCoord2f(1, 1);
            gl.glVertex3d(v1.get(0), -2, v1.get(2));
            gl.glTexCoord2f(0, 1);
            gl.glVertex3d(v0.get(0), -2, v0.get(2));
            gl.glEnd();
          }
        }
      }
      gl.glDisable(GL2.GL_TEXTURE_2D);
    }
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox box = new BoundingBox();
    for (int nodeIndex = 0; nodeIndex < scene.getNumberOfNodes(); nodeIndex++) {
      box.add(scene.getNode(nodeIndex));
    }
    return box;
  }

  /**
   * Getter.
   */
  private PortalCullingSettings getSettings() {
    return settings;
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    PortalCullingFrame app = new PortalCullingFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addRenderable(app);
    appLauncher.addCustomUi(new PortalCullingGui(app.getSettings()));
  }
}

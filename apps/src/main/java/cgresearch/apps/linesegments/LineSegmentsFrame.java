/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.linesegments;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.linesegments.LineSegments;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Test application for the line segments datastructure.
 * 
 * @author Philipp Jenke
 * 
 */
public class LineSegmentsFrame extends CgApplication {

  public LineSegmentsFrame() {
    LineSegments lineSegments = new LineSegments();
    lineSegments.addPoint(VectorMatrixFactory.newIVector3(0, 0, 0));
    lineSegments.addPoint(VectorMatrixFactory.newIVector3(0, 1, 0));
    lineSegments.addPoint(VectorMatrixFactory.newIVector3(1, 1, 0));
    lineSegments.addPoint(VectorMatrixFactory.newIVector3(1, 0, 0));
    lineSegments.addLine(0, 1);
    lineSegments.addLine(1, 2);
    lineSegments.addLine(2, 3);
    lineSegments.addLine(3, 0);
    lineSegments.addLine(0, 2);
    CgNode cgNode = new CgNode(lineSegments, "line segments");
    lineSegments.getMaterial().setReflectionDiffuse(Material.COLOR_BLACK);
    lineSegments.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    getCgRootNode().addChild(cgNode);

  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    LineSegmentsFrame app = new LineSegmentsFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}
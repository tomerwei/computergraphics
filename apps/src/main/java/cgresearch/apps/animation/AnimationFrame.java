package cgresearch.apps.animation;

import java.util.Observable;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Testframe for animation - rotating arrow.
 * 
 * @author Philipp Jenke
 */
public class AnimationFrame extends CgApplication {

  private Arrow arrow;
  private Vector endPoint = VectorFactory.createVector3(1, 0, 0);
  private Matrix R = MatrixFactory.createRotationMatrix(VectorFactory.createVector3(0, 1, 0), Math.PI / 180.0);

  public AnimationFrame() {
    arrow = new Arrow(VectorFactory.createVector3(0, 0, 0), endPoint);
    arrow.getMaterial().setReflectionDiffuse(Material.PALETTE0_COLOR3);
    arrow.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);

    AnimationTimer.getInstance().startTimer(100);

    CgNode node = new CgNode(arrow, "rotating arrow");
    getCgRootNode().addChild(node);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new AnimationFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }

  @Override
  public void update(Observable o, Object arg) {
    super.update(o, arg);
    arrow.getEnd().copy(R.multiply(arrow.getEnd()));
    arrow.updateRenderStructures();
  }
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.bricks;

import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.LightSource;

/**
 * General widget for a CG application.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class CgApplication implements Observer {

  /**
   * Root node of the scene
   */
  protected final CgRootNode rootNode;

  /**
   * Constructor.
   */
  public CgApplication() {
    rootNode = new CgRootNode();
    addLight();
    AnimationTimer.getInstance().addObserver(this);
  }

  /**
   * Add default lights.
   */
  private void addLight() {
    // Light source 1
    LightSource light1 =
        new LightSource(LightSource.Type.POINT).setPosition(VectorMatrixFactory.newVector(5, 5, 5))
            // .setColor(VectorMatrixFactory.newVector(0.75, 0.25, 0.25));
            .setColor(VectorMatrixFactory.newVector(1, 1, 1));
    rootNode.addLight(light1);
    // Light source 2
    LightSource light2 = new LightSource(LightSource.Type.POINT).setPosition(VectorMatrixFactory.newVector(-5, -5, -5))
        // .setColor(VectorMatrixFactory.newVector(0.25, 0.75, 0.25));
        .setColor(VectorMatrixFactory.newVector(1, 1, 1));
    rootNode.addLight(light2);
    // Light source 3
    // LightSource light3 = new
    // LightSource(LightSource.Type.POINT).setPosition(VectorMatrixFactory.newVector(0,
    // 0, 2))
    // // .setColor(VectorMatrixFactory.newVector(0.25, 0.25, 0.75));
    // .setColor(VectorMatrixFactory.newVector(1, 1, 1));
    // rootNode.addLight(light3);
  }

  /**
   * Getter.
   */
  public CgRootNode getCgRootNode() {
    return rootNode;
  }

  @Override
  public void update(Observable o, Object arg) {
  }
}

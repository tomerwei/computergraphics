/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.scenegraph;

import java.util.Observable;

import cgresearch.core.math.BoundingBox;
import cgresearch.graphics.material.Material;

/**
 * Interface for all data structures which have a bounding box.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class ICgNodeContent extends Observable {

  private boolean needsUpdateRenderStructures = false;

  /**
   * Update bounding box?
   */
  protected boolean updateBoundingBox = true;

  /**
   * Material for the mesh.
   */
  protected Material material = new Material();

  /**
   * Return the bounding box of the content.
   */
  public BoundingBox getBoundingBox() {
    return new BoundingBox();
  }

  /**
   * Getter.
   */
  public Material getMaterial() {
    return material;
  }

  /**
   * Indicate that the render content needs to be updated
   */
  public void updateRenderStructures() {
    needsUpdateRenderStructures = true;
  }

  /**
   * Returns true if the render structured need to be updated. This call resets
   * the flag to false.
   */
  public boolean needsUpdateRenderStructures() {
    if (needsUpdateRenderStructures) {
      needsUpdateRenderStructures = false;
      return true;
    } else {
      return false;
    }
  }

  /**
   * The bounding box needs to be recalculated.
   */
  public void setUpdateBoundingBox() {
    updateBoundingBox = true;
  }
}

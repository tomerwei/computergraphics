package cgresearch.graphics.material;

/**
 * Specialized material for curves
 * 
 * @author Philipp Jenke.
 *
 */
public class CurveMaterial extends Material {

  private boolean showControlPolyon = false;

  private boolean showCurrentPoint = false;

  public boolean isShowCurrentPoint() {
    return showCurrentPoint;
  }

  public void setShowCurrentPoint(boolean showCurrentPoint) {
    this.showCurrentPoint = showCurrentPoint;
  }

  public boolean isShowControlPolyon() {
    return showControlPolyon;
  }

  public void setShowControlPolyon(boolean showControlPolyon) {
    this.showControlPolyon = showControlPolyon;
  }

}

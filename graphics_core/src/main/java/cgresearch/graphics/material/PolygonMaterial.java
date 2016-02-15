package cgresearch.graphics.material;

/**
 * Special material for polygons.
 * 
 * @author Philipp Jenke
 */
public class PolygonMaterial extends Material {

  /**
   * Size of the spheres showing the point locations.
   */
  private double pointSphereSize = 0.02;

  /**
   * This flag indicates that the points should be rendered
   */
  private boolean showPointSpheres = true;

  public double getPointSphereSize() {
    return pointSphereSize;
  }

  public void setPointSphereSize(double pointSphereSize) {
    this.pointSphereSize = pointSphereSize;
  }

  public boolean isShowPointSpheres() {
    return showPointSpheres;
  }

  public void setShowPointSpheres(boolean showPointSpheres) {
    this.showPointSpheres = showPointSpheres;
  }

  public boolean getShowPointSpheres() {
    return showPointSpheres;
  }
}

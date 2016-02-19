package cgresearch.graphics.scenegraph;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a light source in the scene.
 * 
 * @author Philipp Jenke
 *
 */
public class LightSource {

  public enum Type {
    POINT, DIRECTIONAL, SPOT
  }

  public enum ShadowType {
    HARD, PLANE_X, PLANE_Y, PLANE_Z, SPHERE
  }

  /**
   * Position in 3-space.
   */
  private Vector position = VectorMatrixFactory.newVector(3);

  /**
   * Color of the light source
   */
  private Vector color = VectorMatrixFactory.newVector(1, 1, 1);

  /**
   * Direction of the light (if required)
   */
  private Vector direction = VectorMatrixFactory.newVector(0, 0, 0);

  /**
   * Type of the light source.
   */
  private Type type = Type.POINT;

  /**
   * Type of the light's shadow
   */
  private ShadowType shadow = ShadowType.HARD;

  /**
   * Strength of the light in units. -1 equals infinite
   */
  private double lightStrength = -1.0;

  /**
   * Opening angle of a slot light: Angle in degrees between the direction and
   * the light cone outer shell.
   */
  private double spotLightAngle = 20;

  /**
   * Constructor.
   */
  public LightSource(Type type) {
    this.type = type;
    this.shadow = ShadowType.HARD;
  }

  /**
   * Constructor
   */
  public LightSource(Type type, ShadowType shadow, double lightStrength) {
    this.type = type;
    this.shadow = shadow;
    this.lightStrength = lightStrength;
  }

  public Vector getPosition() {
    return position;
  }

  public Vector getDirection() {
    return direction;
  }

  public Vector getDiffuseColor() {
    return color;
  }

  public Vector getSpecularColor() {
    return color;
  }

  public Type getType() {
    return type;
  }

  public ShadowType getShadowType() {
    return shadow;
  }

  public double getLightStrength() {
    return lightStrength;
  }

  public LightSource setPosition(Vector position) {
    this.position = position;
    return this;
  }

  public LightSource setColor(Vector color) {
    this.color = color;
    return this;
  }

  public LightSource setDirection(Vector direction) {
    this.direction = direction;
    return this;
  }

  public void setLightStrength(double lightStrength) {
    this.lightStrength = lightStrength;
  }

  public LightSource copy() {
    LightSource copy = new LightSource(type, shadow, lightStrength);
    copy.setColor(color);
    copy.setDirection(direction);
    copy.setPosition(position);
    return copy;
  }

  /**
   * Set the opening angle of a slot light.
   * 
   * @param angle
   *          Angle in degrees between the direction and the light cone outer
   *          shell.
   */
  public void setSpotOpeningAngle(double angle) {
    this.spotLightAngle = angle;
  }

  public double getSpotLightAngle() {
    return spotLightAngle;
  }
}

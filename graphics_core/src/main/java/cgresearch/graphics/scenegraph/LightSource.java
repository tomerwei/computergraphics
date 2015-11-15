package cgresearch.graphics.scenegraph;

import cgresearch.core.math.IVector3;
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
	private IVector3 position = VectorMatrixFactory.newIVector3();

	/**
	 * Color of the light source
	 */
	private IVector3 color = VectorMatrixFactory.newIVector3(1, 1, 1);

	/**
	 * Direction of the light (if required)
	 */
	private IVector3 direction = VectorMatrixFactory.newIVector3(0, 0, 0);

	/**
	 * Type of the light source.
	 */
	private Type type = Type.POINT;

	/**
	 * Type of the light's shadow
	 */
	private ShadowType shadow = ShadowType.HARD;

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
	public LightSource(Type type, ShadowType shadow) {
		this.type = type;
		this.shadow = shadow;
	}

	public IVector3 getPosition() {
		return position;
	}

	public IVector3 getDirection() {
		return direction;
	}

	public IVector3 getDiffuseColor() {
		return color;
	}

	public Type getType() {
		return type;
	}

	public ShadowType getShadowType() {
		return shadow;
	}

	public LightSource setPosition(IVector3 position) {
		this.position = position;
		return this;
	}

	public LightSource setColor(IVector3 color) {
		this.color = color;
		return this;
	}

	public LightSource setDirection(IVector3 direction) {
		this.direction = direction;
		return this;
	}

	public LightSource copy() {
		LightSource copy = new LightSource(type, shadow);
		copy.setColor(color);
		copy.setDirection(direction);
		copy.setPosition(position);
		return copy;
	}
}

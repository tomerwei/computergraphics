package cgresearch.projects.simulation;

import java.util.Iterator;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;

public class Fabric extends Cloth {

	/**
	 * Resolution of the fabric (#mass points = resolution^2)
	 */
	private final int resolution;

	/**
	 * Scaling of the fabric (required for reset call)
	 */
	private final double scale;

	/**
	 * Translation of the fabric (required for reset call)
	 */
	private final Vector translation = VectorFactory.createVector(3);

	/**
	 * Combined mass for all mass points in the fabric
	 */
	private final double mass;

	/**
	 * This type describes the initial geometry of the fabric
	 *
	 */
	public enum Type {
		XZ, XY
	};

	/**
	 * Type of the current fabric (required for reset)
	 */
	private Type type = Type.XY;

	/**
	 * Material constants
	 */
	private final double SPRING_PROPERTY_STRUCTURE = 40;
	private final double SPRING_PROPERTY_SHEAR = 20;
	private final double SPRING_PROPERTY_BEND = 10;

	/**
	 * Physics constants
	 */
	private final double GRAVITATION = 9.81;
	private final double WIND_ACCELERATION = 5;
	private final Vector WIND_DIRECTION = VectorFactory.createVector3(0,
			0, -1);

	/**
	 * Create a squared cloth with a specified number of mass points
	 * (resolution) in each dimension in [-1,1]^2. The mass points are further
	 * translated as specified.
	 * 
	 * @param type
	 *            Type of the initial geometry (e.g. XZ)
	 * @param resolution
	 *            number of mass points in each dimension (#mass points =
	 *            resolution^2)
	 * @param scale
	 *            Scaling of the fabric in each dimension.
	 * @param translation
	 *            Translation offset of all mass points.
	 * @param mass
	 *            Mass of the fabric in [kg].
	 */
	public Fabric(Type type, int resolution, double scale,
			Vector translation, double mass) {
		this.type = type;
		this.resolution = resolution;
		this.scale = scale;
		this.translation.copy(translation);
		this.mass = mass;
		init();

	}

	/**
	 * Init the cloth mass points and springs.
	 */
	private void init() {
		switch (type) {
		case XZ:
			initXZ();
			break;
		case XY:
			initXY();
			break;
		}
	}

	/**
	 * Init cloth in XZ-plane
	 */
	private void initXZ() {
		clear();
		double deltaStructure = 2.0 / (double) (resolution - 1);
		// Create mass points
		double massPointMass = mass / (resolution * resolution);
		for (int i = 0; i < resolution; i++) {
			double x = -1 + deltaStructure * i;
			for (int j = 0; j < resolution; j++) {
				double z = -1 + deltaStructure * j;
				Vector pos = VectorFactory.createVector3(x, 0, z)
						.multiply(scale).add(translation);
				addMassPoint(new MassPoint(pos, massPointMass));
			}
		}

		addSpringsStructure(deltaStructure);
		addSpringsShear(deltaStructure);
		addSpringsBend(deltaStructure);
	}

	/**
	 * Init cloth in XY-plane
	 */
	private void initXY() {
		clear();
		double deltaStructure = 2.0 / (double) (resolution - 1);
		// Create mass points
		double massPointMass = mass / (resolution * resolution);
		for (int i = 0; i < resolution; i++) {
			double x = -1 + deltaStructure * i;
			for (int j = 0; j < resolution; j++) {
				double y = -1 + deltaStructure * j;
				Vector pos = VectorFactory.createVector3(x, y, 0)
						.multiply(scale).add(translation);
				addMassPoint(new MassPoint(pos, massPointMass));
			}
		}

		addSpringsStructure(deltaStructure);
		addSpringsShear(deltaStructure);
		addSpringsBend(deltaStructure);
	}

	/**
	 * Add the springs to control the bending.
	 */
	private void addSpringsBend(double deltaStructure) {
		// Bending springs
		double deltaBending = 2.0 * deltaStructure;
		for (int j = 0; j < resolution - 2; j++) {
			for (int i = 0; i < resolution - 2; i++) {
				int firstMassPointIndex = i * resolution + j;
				int secondMassPointIndex = (i + 2) * resolution + j;
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaBending, SPRING_PROPERTY_BEND));
				firstMassPointIndex = i * resolution + j;
				secondMassPointIndex = i * resolution + (j + 2);
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaBending, SPRING_PROPERTY_BEND));

			}
		}
	}

	/**
	 * Add the springs to control the shearing.
	 */
	private void addSpringsShear(double deltaStructure) {
		// Shearing springs
		double deltaShearing = Math.sqrt(2.0) * deltaStructure;
		for (int j = 1; j < resolution; j++) {
			for (int i = 1; i < resolution; i++) {
				int firstMassPointIndex = (i - 1) * resolution + j;
				int secondMassPointIndex = i * resolution + (j - 1);
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaShearing, SPRING_PROPERTY_SHEAR));
				firstMassPointIndex = i * resolution + j;
				secondMassPointIndex = (i - 1) * resolution + (j - 1);
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaShearing, SPRING_PROPERTY_SHEAR));
			}
		}
	}

	/**
	 * Add the springs to control the strcuture.
	 */
	private void addSpringsStructure(double deltaStructure) {
		// Structural springs
		for (int i = 1; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				int firstMassPointIndex = (i - 1) * resolution + j;
				int secondMassPointIndex = i * resolution + j;
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaStructure, SPRING_PROPERTY_STRUCTURE));
			}
		}
		for (int j = 1; j < resolution; j++) {
			for (int i = 0; i < resolution; i++) {
				int firstMassPointIndex = i * resolution + (j - 1);
				int secondMassPointIndex = i * resolution + j;
				addSpring(new Spring(firstMassPointIndex, secondMassPointIndex,
						deltaStructure, SPRING_PROPERTY_STRUCTURE));
			}
		}
	}

	@Override
	public void reset() {
		init();
	}

	/**
	 * Return the current length of the spring
	 */
	public double getSpringLength(Spring spring) {
		MassPoint massPoint1 = getMassPoint(spring.getFirstMassPoints());
		MassPoint massPoint2 = getMassPoint(spring.getSecondMassPoints());
		return massPoint1.getX().subtract(massPoint2.getX()).getNorm();
	}

	public Vector getForceDirection(Vector x1, Vector x2) {
		Vector direction = x1.subtract(x2);
		direction.normalize();
		return direction;
	}

	private Vector computeSpringForce(int index, Vector pos, Spring spring) {
		MassPoint otherMassPoint = getMassPoint(spring
				.getOtherMassPointIndex(index));
		Vector forceDirection = getForceDirection(pos, otherMassPoint.getX());
		return forceDirection.multiply(spring.getMaterialProperty()
				* (spring.getRestLength() - getSpringLength(spring)));
	}

	/**
	 * Compute the combined spring force at a mass node.
	 */
	private Vector computeSpringForce(int index, Vector x) {
		Iterator<Spring> iteratorSpring = getMassPoint(index)
				.getSpringsIterator();
		Vector force = VectorFactory.createVector3(0, 0, 0);
		while (iteratorSpring.hasNext()) {
			Spring spring = iteratorSpring.next();
			force = force.add(computeSpringForce(index, x, spring));
		}
		return force;
	}

	@Override
	public Vector eval(int index) {
		MassPoint massPoint = getMassPoint(index);

		// Gravitational force
		Vector gravitationalForce = VectorFactory.createVector3(0, -1, 0)
				.multiply(GRAVITATION).multiply(massPoint.getMass());

		// Wind
		Vector windForce = WIND_DIRECTION.multiply(WIND_ACCELERATION)
				.multiply(massPoint.getMass());

		// Spring force
		Vector springForce = computeSpringForce(index, massPoint.getX());

		// Assemble force
		Vector force = gravitationalForce.add(springForce).add(windForce);

		// Compute acceleration
		Vector accelleration = force.multiply(1.0 / massPoint.getMass());
		return accelleration;
	}

	@Override
	public Vector eval(int index, Vector pos) {
		MassPoint massPoint = getMassPoint(index);
		Vector gravitationalForce = VectorFactory.createVector3(0, -1, 0)
				.multiply(GRAVITATION).multiply(massPoint.getMass());
		Vector springForce = computeSpringForce(index, pos);
		Vector force = gravitationalForce.add(springForce);
		Vector accelleration = force.multiply(1.0 / massPoint.getMass());
		return accelleration;
	}
}

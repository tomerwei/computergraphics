package cgresearch.projects.simulation;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;
import cgresearch.projects.simulation.collision.Collidable;
import cgresearch.projects.simulation.collision.CollidablePlane;
import cgresearch.projects.simulation.collision.CollidableSphere;
import cgresearch.projects.simulation.solver.Accelleration;
import cgresearch.projects.simulation.solver.EulerSolver;
import cgresearch.projects.simulation.solver.HeunSolver;
import cgresearch.projects.simulation.solver.Solver;

/**
 * Parent class for all simulations
 * 
 * @author Philipp Jenke
 *
 */
public abstract class Simulation {

	/**
	 * Settings
	 */
	private double TOTAL_SIMULATION_TIME = 2; // [s]
	private double TOLERANCE = 1e-7; // [m]
	private double VIS_TIME_INTERVAL = 1.0 / 60.0; // [s]

	private Solver solver;

	private double currentSimulationTime = 0;

	private double nextVisTime = 0;

	private Thread simulationThread = null;

	private List<IVector3> x = new ArrayList<IVector3>();
	private List<IVector3> v = new ArrayList<IVector3>();
	private List<IVector3> newX = new ArrayList<IVector3>();
	private List<IVector3> newV = new ArrayList<IVector3>();

	/**
	 * These mass points are fixed
	 */
	private List<Integer> boundaryPoints = new ArrayList<Integer>();

	/**
	 * This counts the number of simulation steps.
	 */
	private int currentSimulationStep = 0;

	/**
	 * List of objects in the scene (potentially collision candidates.
	 */
	private List<Collidable> sceneObjects = new ArrayList<Collidable>();

	/**
	 * This is the parent node of the simulated frames
	 */
	private final CgNode animationNode;

	/**
	 * This is the parent node of all supplementary content
	 */
	private final CgNode supplementNode;

	/**
	 * Constructor.
	 */
	public Simulation(CgNode animationNode, CgNode supplementNode) {
		this.animationNode = animationNode;
		this.supplementNode = supplementNode;
		solver = new HeunSolver(TOTAL_SIMULATION_TIME);
	}

	public void addSceneObject(Collidable object) {
		sceneObjects.add(object);
	}

	public int getNumberOfSceneObjects() {
		return sceneObjects.size();
	}

	public Collidable getSceneObject(int index) {
		return sceneObjects.get(index);
	}

	/**
	 * Start the simulation
	 */
	public void run() {
		if (simulationThread != null) {
			stop();
		}
		simulationThread = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted()) {
					simulationStep();
				}
			}
		};
		simulationThread.start();
	}

	/**
	 * Stop the simulation
	 */
	public void stop() {
		if (simulationThread != null) {
			simulationThread.interrupt();
			simulationThread = null;
		}
	}

	/**
	 * Setup the required fields for a simulation step. Implement in subclasses.
	 * 
	 * @param x
	 *            Positions.
	 * @param v
	 *            Velocities.
	 */
	protected abstract void preSimulationStep(List<IVector3> x, List<IVector3> v);

	/**
	 * Provides the updated values for x an v.
	 * 
	 * @param x
	 *            Positions.
	 * @param v
	 *            Velocities.
	 */
	protected abstract void postSimulationStep(List<IVector3> x,
			List<IVector3> v);

	/**
	 * Return an object which computes the acceleration values.
	 */
	protected abstract Accelleration getAcceleration();

	/**
	 * Apply a single simulation step.
	 */
	public void simulationStep() {

		while (getCurrentTime() < nextVisTime) {

			// Make sure, the array have the correct size
			preSimulationStep(x, v);
			while (newX.size() < x.size()) {
				newX.add(VectorMatrixFactory.newIVector3());
			}
			while (newV.size() < v.size()) {
				newV.add(VectorMatrixFactory.newIVector3());
			}

			// Compute integration step
			currentSimulationTime += solver.getH();
			solver.solve(x, v, newX, newV, TOLERANCE, getAcceleration());

			// Handle fixed mass points
			for (int massPointIndex : boundaryPoints) {
				newX.set(massPointIndex, x.get(massPointIndex));
				newV.set(massPointIndex, v.get(massPointIndex));
			}

			// Handle collisions
			for (int massIndex = 0; massIndex < x.size(); massIndex++) {
				for (Collidable collidable : sceneObjects) {
					if (collidable.collides(x.get(massIndex))) {
						// TODO: better x, better v
						newX.set(massIndex, collidable.projectToSurface(newX
								.get(massIndex)));
						newV.set(massIndex,
								VectorMatrixFactory.newIVector3(0, 0, 0));
					}
				}
			}

			postSimulationStep(newX, newV);
		}
		nextVisTime = nextVisTime + VIS_TIME_INTERVAL;
		currentSimulationStep++;

		createOutput();
		AnimationTimer.getInstance().setMaxValue(getCurrentSimulationStep());
		AnimationTimer.getInstance().setValue(getCurrentSimulationStep());
		System.out.printf("Simulated frame %d at time t=%.5f.\n",
				getCurrentSimulationStep(), getCurrentTime());
	}

	/**
	 * Create output (e.g.) visualization of the result.
	 */
	protected abstract void createOutput();

	/**
	 * Reset the simulation state.
	 */
	protected abstract void resetSimulation();

	/**
	 * Reset the simulation
	 */
	public void reset() {
		currentSimulationTime = 0;
		currentSimulationStep = 0;
		animationNode.removeAllChildren();
		supplementNode.removeAllChildren();
		currentSimulationStep = 0;
		currentSimulationTime = 0;
		nextVisTime = 0;

		resetSimulation();
		AnimationTimer.getInstance().setValue(0);

		createColliderNodes();
		createBoundaryPointNodes();
	}

	private void createBoundaryPointNodes() {
		for (int i = 0; i < getNumberOfBoundaryPoints(); i++) {
			Integer boundaryPointIndex = getBoundaryPoint(i);
			IVector3 pos = getMassPointPosition(boundaryPointIndex);
			ITriangleMesh mesh = TriangleMeshFactory
					.createSphere(pos, 0.05, 10);
			mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			mesh.getMaterial().setReflectionDiffuse(
					VectorMatrixFactory.newIVector3(246.0 / 255.0,
							157.0 / 255.0, 0));
			CgNode node = new CgNode(mesh, "boundary point");
			supplementNode.addChild(node);
		}
	}

	private void createColliderNodes() {
		for (int i = 0; i < getNumberOfSceneObjects(); i++) {
			Collidable collidable = getSceneObject(i);
			if (collidable instanceof CollidableSphere) {
				createColliderNode((CollidableSphere) collidable);
			} else if (collidable instanceof CollidablePlane) {
				createColliderNode((CollidablePlane) collidable);
			}
		}
	}

	private void createColliderNode(CollidablePlane plane) {
		ITriangleMesh meshPlane = TriangleMeshFactory
				.createQuadWithTextureCoordinates();
		meshPlane.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
		meshPlane.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25));

		Transformation transformation = new Transformation();
		transformation.addTranslation(plane.getPoint());
		CgNode planeTranslationNode = new CgNode(transformation,
				"transformation");
		CgNode planeNode = new CgNode(meshPlane, "Plane");
		planeNode.setVisible(false);
		planeTranslationNode.addChild(planeNode);
		supplementNode.addChild(planeTranslationNode);
	}

	private void createColliderNode(CollidableSphere sphere) {
		// Sphere
		ITriangleMesh meshSphere = TriangleMeshFactory.createSphere(
				sphere.getCenter(), sphere.getRadius(), 10);
		meshSphere.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
		meshSphere.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25));
		CgNode sphereNode = new CgNode(meshSphere, "Sphere");
		sphereNode.setVisible(false);
		supplementNode.addChild(sphereNode);

	}

	/**
	 * Mark an additional mass point as fixed.
	 */
	public void addFixedMassPoint(int index) {
		boundaryPoints.add(index);
	}

	/**
	 * Getter
	 */
	public double getCurrentTime() {
		return currentSimulationTime;
	}

	/**
	 * Getter.
	 */
	public int getCurrentSimulationStep() {
		return currentSimulationStep;
	}

	/**
	 * Getter.
	 */
	protected CgNode getAnimationNode() {
		return animationNode;
	}

	/**
	 * Getter.
	 */
	public int getNumberOfBoundaryPoints() {
		return boundaryPoints.size();
	}

	/**
	 * Getter.
	 */
	public Integer getBoundaryPoint(int i) {
		return boundaryPoints.get(i);
	}

	/**
	 * Return the position of the mass point with the specified index.
	 */
	public abstract IVector3 getMassPointPosition(int index);

	/**
	 * Getter.
	 */
	public double getStepSize() {
		return solver.getH();
	}

	/**
	 * Setter.
	 */
	public void setStepSize(double stepSize) {
		solver.setH(stepSize);
	}

	public void setSolver(Solver.SolverType type) {
		switch (type) {
		case EULER:
			solver = new EulerSolver();
			break;
		case HEUN:
			solver = new HeunSolver(TOTAL_SIMULATION_TIME);
			break;
		}
	}

	/**
	 * Getter.
	 */
	public Solver getSolver() {
		return solver;
	}
}

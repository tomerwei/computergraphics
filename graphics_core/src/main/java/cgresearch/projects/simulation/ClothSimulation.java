package cgresearch.projects.simulation;

import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.projects.simulation.collision.CollidablePlane;
import cgresearch.projects.simulation.collision.CollidableSphere;
import cgresearch.projects.simulation.solver.Accelleration;
import cgresearch.projects.simulation.solver.Solver.SolverType;

/**
 * Simulator for a cloth scene.
 * 
 * @author Philipp Jenke
 *
 */
public class ClothSimulation extends Simulation {
	/**
	 * Cloth object
	 */
	private Cloth cloth = null;

	/**
	 * "Master" mesh for the cloth.
	 */
	private ITriangleMesh clothRestMesh;

	/**
	 * Different presets.
	 *
	 */
	private enum SimulationType {
		CLOTH_SPHERE_PLANE, CLOTH_WIND
	};

	/**
	 * Current simulation type.
	 */
	private SimulationType type;

	/**
	 * Constructor.
	 */
	public ClothSimulation(CgNode animationNode, CgNode supplementNode) {
		super(animationNode, supplementNode);
		setSolver(SolverType.HEUN);
		type = SimulationType.CLOTH_WIND;
		reset();
	}

	private void initSceneClothWind() {
		int clothResolution = 20;
		Cloth cloth = new Fabric(Fabric.Type.XY, clothResolution, 1.0,
				VectorMatrixFactory.newIVector3(0, 0, 0), 1);
		setCloth(cloth);
		addFixedMassPoint(clothResolution - 1);
		addFixedMassPoint(clothResolution * clothResolution - 1);
	}

	private void initSceneClothSpherePlane() {
		// Fill scene
		double sphereRadius = 0.6;
		double sphereHeight = 0.75;
		double clothHeight = 1.5;
		double planeHeight = -0.5;
		int clothResolution = 20;
		Cloth cloth = new Fabric(Fabric.Type.XZ, clothResolution, 1.0,
				VectorMatrixFactory.newIVector3(0, clothHeight, 0), 1);

		setCloth(cloth);
		addSceneObject(new CollidableSphere(VectorMatrixFactory.newIVector3(0,
				sphereHeight, 0), sphereRadius));
		addSceneObject(new CollidablePlane(VectorMatrixFactory.newIVector3(0,
				planeHeight, 0), VectorMatrixFactory.newIVector3(0, 1, 0)));
	}

	public Cloth getCloth() {
		return cloth;
	}

	@Override
	protected void preSimulationStep(List<IVector3> x, List<IVector3> v) {
		// Make sure, the array have the correct size
		while (x.size() < cloth.getNumberOfMassPoints()) {
			x.add(VectorMatrixFactory.newIVector3());
		}
		while (v.size() < cloth.getNumberOfMassPoints()) {
			v.add(VectorMatrixFactory.newIVector3());
		}
		for (int massIndex = 0; massIndex < cloth.getNumberOfMassPoints(); massIndex++) {
			MassPoint massPoint = cloth.getMassPoint(massIndex);
			x.set(massIndex, massPoint.getX());
			v.set(massIndex, massPoint.getVelocity());
		}
	}

	@Override
	protected void postSimulationStep(List<IVector3> x, List<IVector3> v) {
		// Update position and velocity
		for (int massIndex = 0; massIndex < cloth.getNumberOfMassPoints(); massIndex++) {
			MassPoint massPoint = cloth.getMassPoint(massIndex);
			massPoint.setX(x.get(massIndex));
			massPoint.setVelocity(v.get(massIndex));
		}
	}

	@Override
	protected Accelleration getAcceleration() {
		return cloth;
	}

	@Override
	public void createOutput() {
		// Update cloth mesh
		for (int nodeIndex = 0; nodeIndex < getCloth().getNumberOfMassPoints(); nodeIndex++) {
			clothRestMesh.getVertex(nodeIndex).getPosition()
					.copy(getCloth().getMassPoint(nodeIndex).getX());
		}
		clothRestMesh.computeTriangleNormals();
		clothRestMesh.computeVertexNormals();
		clothRestMesh.updateRenderStructures();

		// Handle output
		ITriangleMesh frameMesh = new TriangleMesh(clothRestMesh);
		CgNode frameNode = new CgNode(frameMesh, "frame"
				+ getCurrentSimulationStep());
		getAnimationNode().addChild(frameNode);
	}

	@Override
	public void resetSimulation() {
		switch (type) {
		case CLOTH_SPHERE_PLANE:
			initSceneClothSpherePlane();
			break;
		case CLOTH_WIND:
			initSceneClothWind();
			break;
		}
		createOutput();
	}

	public void setCloth(Cloth cloth) {
		this.cloth = cloth;
		createClothMesh();
	}

	private void createClothMesh() {
		// Render objects for cloth
		clothRestMesh = new TriangleMesh();
		int clothResolution = (int) Math.sqrt(cloth.getNumberOfMassPoints());
		for (int i = 0; i < clothResolution; i++) {
			for (int j = 0; j < clothResolution; j++) {
				int index = i * clothResolution + j;
				Vertex vertex = new Vertex(getCloth().getMassPoint(index)
						.getX());
				double u = (double) i / (double) (clothResolution - 1);
				double v = (double) j / (double) (clothResolution - 1);
				clothRestMesh.addTextureCoordinate(VectorMatrixFactory
						.newIVector3(u, v, 0));
				clothRestMesh.addVertex(vertex);
			}
		}
		for (int i = 1; i < clothResolution; i++) {
			for (int j = 1; j < clothResolution; j++) {
				int v00 = (i - 1) * clothResolution + (j - 1);
				int v01 = (i - 1) * clothResolution + j;
				int v11 = i * clothResolution + j;
				int v10 = i * clothResolution + (j - 1);
				clothRestMesh.addTriangle(new Triangle(v00, v01, v11, v00, v01,
						v11));
				clothRestMesh.addTriangle(new Triangle(v00, v11, v10, v00, v11,
						v10));
			}
		}
		clothRestMesh.computeTriangleNormals();
		clothRestMesh.computeVertexNormals();
		clothRestMesh.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newIVector3(0.25, 0.25, 0.75));
		clothRestMesh.getMaterial().setRenderMode(Material.Normals.PER_VERTEX);
		String TEXTURE_CLOTH_ID = "TEXTURE_CLOTH_ID";
		ResourceManager.getTextureManagerInstance().addResource(
				TEXTURE_CLOTH_ID, new CgTexture("textures/cloth.png"));
		clothRestMesh.getMaterial().setTextureId(TEXTURE_CLOTH_ID);
		clothRestMesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
	}

	@Override
	public IVector3 getMassPointPosition(int index) {
		return cloth.getMassPoint(index).getX();
	}
}

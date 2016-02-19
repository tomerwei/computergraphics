package cgresearch.projects.simulation.ball;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.projects.simulation.Simulation;
import cgresearch.projects.simulation.solver.Accelleration;
import cgresearch.projects.simulation.solver.Solver.SolverType;

/**
 * Simulation of a ball flight.
 * 
 * @author Philipp Jenke
 *
 */
public class BallSimulation extends Simulation implements Accelleration {

	private Vector x = VectorMatrixFactory.newVector(3);
	private Vector v = VectorMatrixFactory.newVector(3);
	private double mass = 1;

	/**
	 * Constructor.
	 */
	public BallSimulation(CgNode animationNode, CgNode supplementNode) {
		super(animationNode, supplementNode);
		setSolver(SolverType.EULER);
		reset();
	}

	@Override
	protected void preSimulationStep(List<Vector> x, List<Vector> v) {
		if (x.size() > 1) {
			x.clear();
		}
		if (v.size() > 1) {
			v.clear();
		}
		while (x.size() < 1) {
			x.add(VectorMatrixFactory.newVector(3));
		}
		while (v.size() < 1) {
			v.add(VectorMatrixFactory.newVector(3));
		}

		x.get(0).copy(this.x);
		v.get(0).copy(this.v);

	}

	@Override
	protected void postSimulationStep(List<Vector> x, List<Vector> v) {
		this.x.copy(x.get(0));
		this.v.copy(v.get(0));

		// Stop simulation when ball hits ground
		if (this.x.get(1) < 0) {
			stop();
		}
	}

	@Override
	protected Accelleration getAcceleration() {
		return this;
	}

	@Override
	protected void createOutput() {
		ITriangleMesh mesh = TriangleMeshFactory.createSphere(x, 0.1, 20);
		mesh.getMaterial().setReflectionDiffuse(
				VectorMatrixFactory.newVector(1, 0, 0));
		mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		String name = String.format("ball_%d", getCurrentSimulationStep());
		CgNode node = new CgNode(mesh, name);
		getAnimationNode().addChild(node);
	}

	@Override
	protected void resetSimulation() {
		x = VectorMatrixFactory.newVector(0, 0, 0);
		v = VectorMatrixFactory.newVector(2, 4, 2);
	}

	@Override
	public Vector eval(int index) {
		return VectorMatrixFactory.newVector(0, -1, 0).multiply(9.81 / mass);
	}

	@Override
	public Vector eval(int index, Vector pos) {
		return VectorMatrixFactory.newVector(0, -1, 0).multiply(9.81 / mass);
	}

	@Override
	public Vector getMassPointPosition(int index) {
		if (index == 0) {
			return x;
		} else {
			Logger.getInstance().error("Invalid inddex");
			return null;
		}
	}
}

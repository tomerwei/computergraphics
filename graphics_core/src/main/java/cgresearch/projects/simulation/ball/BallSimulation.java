package cgresearch.projects.simulation.ball;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
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

	private IVector3 x = VectorMatrixFactory.newIVector3();
	private IVector3 v = VectorMatrixFactory.newIVector3();
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
	protected void preSimulationStep(List<IVector3> x, List<IVector3> v) {
		if (x.size() > 1) {
			x.clear();
		}
		if (v.size() > 1) {
			v.clear();
		}
		while (x.size() < 1) {
			x.add(VectorMatrixFactory.newIVector3());
		}
		while (v.size() < 1) {
			v.add(VectorMatrixFactory.newIVector3());
		}

		x.get(0).copy(this.x);
		v.get(0).copy(this.v);

	}

	@Override
	protected void postSimulationStep(List<IVector3> x, List<IVector3> v) {
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
				VectorMatrixFactory.newIVector3(1, 0, 0));
		mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
		String name = String.format("ball_%d", getCurrentSimulationStep());
		CgNode node = new CgNode(mesh, name);
		getAnimationNode().addChild(node);
	}

	@Override
	protected void resetSimulation() {
		x = VectorMatrixFactory.newIVector3(0, 0, 0);
		v = VectorMatrixFactory.newIVector3(2, 4, 2);
	}

	@Override
	public IVector3 eval(int index) {
		return VectorMatrixFactory.newIVector3(0, -1, 0).multiply(9.81 / mass);
	}

	@Override
	public IVector3 eval(int index, IVector3 pos) {
		return VectorMatrixFactory.newIVector3(0, -1, 0).multiply(9.81 / mass);
	}

	@Override
	public IVector3 getMassPointPosition(int index) {
		if (index == 0) {
			return x;
		} else {
			Logger.getInstance().error("Invalid inddex");
			return null;
		}
	}
}

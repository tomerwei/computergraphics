package cgresearch.projects.simulation.solver;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;

public class HeunSolver extends Solver {

	private double DAMPING = 0.3;

	private final double totalSimulationTime;

	/**
	 * Constructor
	 */
	public HeunSolver(double totalSimulationTime) {
		this.totalSimulationTime = totalSimulationTime;
	}

	@Override
	public void solve(List<Vector> x, List<Vector> v, List<Vector> newX,
			List<Vector> newV, double tolerance, Accelleration acc) {
		if (x == null || v == null || newX == null || newV == null) {
			Logger.getInstance().error(
					"EulerSolver.solve: Invalid input arrays");
			return;
		}

		if (x.size() != v.size() || x.size() != newX.size()
				|| x.size() != newV.size()) {
			Logger.getInstance().error(
					"EulerSolver.solve: Invalid input array size");
			return;
		}

		for (int i = 0; i < x.size(); i++) {
			Vector acceleration = acc.eval(i);
			Vector xE = x.get(i).add(v.get(i).multiply(h));
			Vector vE = v.get(i).add(acceleration.multiply(h));

			Vector accelerationE = acc.eval(i, xE);
			newX.set(i, x.get(i).add(v.get(i).add(vE).multiply(h / 2.0)));
			newV.set(
					i,
					v.get(i).add(
							acceleration.add(accelerationE).multiply(h / 2.0)
									.multiply(DAMPING)));
		}

		// Adjust step size
		double lte = computeLte(x, v, newX, newV, totalSimulationTime);
		h = totalSimulationTime * Math.sqrt(tolerance / lte);
	}

	@Override
	public SolverType getType() {
		return SolverType.HEUN;
	}
}

package cgresearch.projects.simulation.solver;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;

public class EulerSolver extends Solver {

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
			newX.set(i, x.get(i).add(v.get(i).multiply(h)));
			newV.set(i, v.get(i).add(acceleration.multiply(h)));
		}
	}

	@Override
	public SolverType getType() {
		return SolverType.EULER;
	}
}

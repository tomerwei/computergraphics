package cgresearch.projects.simulation.solver;

import java.util.List;

import cgresearch.core.math.IVector3;

public abstract class Solver {

	/**
	 * Step size
	 */
	protected double h = 0.0001;

	/**
	 * Available solvers
	 * 
	 * @author Philipp Jenke
	 *
	 */
	public enum SolverType {
		EULER, HEUN
	};

	/**
	 * Solve the system.
	 * 
	 * @param x
	 *            Input mass points.
	 * @param v
	 *            Input velocities.
	 * @param newX
	 *            Output mass points.
	 * @param newV
	 *            Output velocities.
	 * @param h
	 *            Step size
	 */
	public abstract void solve(List<IVector3> x, List<IVector3> v,
			List<IVector3> newX, List<IVector3> newV, double tolerance,
			Accelleration acc);

	/**
	 * Compute the local t... error.
	 */
	protected double computeLte(List<IVector3> x, List<IVector3> v,
			List<IVector3> newX, List<IVector3> newV, double totalSimulationTime) {
		double normXnewX = 0;
		double normVnewV = 0;
		for (int i = 0; i < x.size(); i++) {
			IVector3 dx = x.get(i).subtract(newX.get(i));
			IVector3 dv = v.get(i).subtract(newV.get(i));
			normXnewX += dx.multiply(dx);
			normVnewV += dv.multiply(dv);
		}
		normXnewX = Math.sqrt(normXnewX);
		normVnewV = Math.sqrt(normVnewV);
		return normXnewX + totalSimulationTime * normVnewV;
	}

	/**
	 * Return the step size
	 */
	public double getH() {
		return h;
	}

	/**
	 * Set the step size
	 */
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * Getter.
	 */
	public abstract SolverType getType();

}

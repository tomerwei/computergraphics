package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector;

public class ButData32 {

	private final int carVektor = 32;

	private List<IVector> x = new ArrayList<IVector>();
	private List<IVector> y = new ArrayList<IVector>();
	private List<IVector> z = new ArrayList<IVector>();

	public List<IVector> getX() {
		return x;
	}

	public void setX(List<IVector> x) {
		this.x = x;
	}

	public List<IVector> getY() {
		return y;
	}

	public void setY(List<IVector> y) {
		this.y = y;
	}

	public List<IVector> getZ() {
		return z;
	}

	public void setZ(List<IVector> z) {
		this.z = z;
	}
}

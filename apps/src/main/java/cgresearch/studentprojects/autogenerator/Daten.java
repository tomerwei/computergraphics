package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;

public class Daten {

	public Daten(int vector) {
		this.carVektor = vector;
	}
	
	private final int carVektor;

	private List<Vector> x = new ArrayList<Vector>();
	private List<Vector> y = new ArrayList<Vector>();
	private List<Vector> z = new ArrayList<Vector>();

	public List<Vector> getX() {
		return x;
	}

	public void setX(List<Vector> x) {
		this.x = x;
	}

	public List<Vector> getY() {
		return y;
	}

	public void setY(List<Vector> y) {
		this.y = y;
	}

	public List<Vector> getZ() {
		return z;
	}

	public void setZ(List<Vector> z) {
		this.z = z;
	}
	
}

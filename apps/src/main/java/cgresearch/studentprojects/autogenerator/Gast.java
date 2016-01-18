package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.primitives.Cuboid;

public class Gast extends Cuboid {
	
	private final int carVektor = 28;

	private IVector3 center;
	private double breite;
	private double hoehe;
	private double laenge;

	public Gast(IVector3 center, double breite, double hoehe, double laenge) {
		super(center, breite, hoehe, laenge);
		this.breite = breite;
		this.hoehe = hoehe;
		this.laenge = laenge;
		this.center = center;
	}

	public double getBreite() {
		return breite;
	}

	public void setBreite(double breite) {
		this.breite = breite;
	}

	public double getHoehe() {
		return hoehe;
	}

	public void setHoehe(double hoehe) {
		this.hoehe = hoehe;
	}

	public double getLaenge() {
		return laenge;
	}

	public void setLaenge(double laenge) {
		this.laenge = laenge;
	}

	public IVector3 getCenter() {
		return center;
	}

	public void setCenter(IVector3 center) {
		this.center = center;
	}
}

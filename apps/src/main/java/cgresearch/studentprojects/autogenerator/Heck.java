package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.primitives.Cuboid;

public class Heck extends Cuboid {
	
	private final int carVektor = 28;

	private Vector center;
	private double breite;
	private double hoehe;
	private double laenge;

	public Heck(Vector center, double breite, double hoehe, double laenge) {
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

	public Vector getCenter() {
		return center;
	}

	public void setCenter(Vector center) {
		this.center = center;
	}

}

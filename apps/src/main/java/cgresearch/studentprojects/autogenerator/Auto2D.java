package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

public class Auto2D {
	
	private final int carVektor = 28;

	private Chassis2D chassis;
	private Front2D front;
	private Gast2D gast;
	private Heck2D heck;

	public Auto2D(Chassis2D chassis, Front2D front, Gast2D gast, Heck2D heck) {
		this.chassis = chassis;
		this.front = front;
		this.gast = gast;
		this.heck = heck;
	}

	public Auto2D(double hoehe, double laenge, int min, int max,
			int frontHoehe, int heckHoehe, int degree, int fronth, int frontv,
			int gastv, int gasth, int heckh, int heckv, int chassisv,
			int chassish, int gastFrontHor, int gastFrontVer, int gastHeckHor, int gastHeckVer) {
		this.chassis = generateChassis(hoehe, laenge, degree, chassisv,
				chassish);
		this.front = generateFront(hoehe, laenge, min, frontHoehe, degree,
				fronth, frontv);
		this.heck = generateHeck(hoehe, laenge, max, heckHoehe, degree, heckh,
				heckv);
		this.gast = generateGast(hoehe, laenge, min, max, degree, gastv, gasth,
				gastFrontHor, gastFrontVer, gastHeckHor, gastHeckVer);
	}

	private Chassis2D generateChassis(double hoehe, double laenge, int degree,
			int chassisv, int chassish) {

		// Hoehe von Chassis = 1/10 von Autos Hoehe
		double h = hoehe / 10;

		// Laenge von Chassis = Laenge des Autos
		double l = laenge;

		// Postition von Chassis = 0,0,0
		Vector p = VectorMatrixFactory.newVector(0, 0, 0);

		Chassis2D erg = new Chassis2D(p, h, l, degree, chassisv, chassish);
		return erg;
	}

	private Front2D generateFront(double hoehe, double laenge, int lmin,
			int frontHoehe, int degree, int fronth, int frontv) {

		// Hoehe von Front = 1/2 von Autos Hoehe
		double h = (hoehe - this.chassis.getHoehe()) * frontHoehe / 100;

		// Laenge von Front = 1/3 Laenge des Autos
		double l = laenge * ((double) lmin / 100);

		// Postition von Front = links von Chassis Center und um haelfte von
		// Front Hoehe hoeher
		Vector p = VectorMatrixFactory.newVector(0 - laenge / 2 + l / 2,
				this.chassis.getHoehe() / 2 + h / 2, 0);

		Front2D erg = new Front2D(p, h, l, degree, fronth, frontv);
		return erg;
	}

	private Heck2D generateHeck(double hoehe, double laenge, int lmax,
			int heckHoehe, int degree, int heckh, int heckv) {

		// Hoehe von Heck = 1/2 von Autos Hoehe
		double h = (hoehe - this.chassis.getHoehe()) * heckHoehe / 100;

		// Laenge von Heck = 1/3 Laenge des Autos
		double l = laenge * ((100 - (double) lmax) / 100);

		// Postition von Heck = rechts von Chassis Center und um haelfte von
		// Heck Hoehe hoeher
		Vector p = VectorMatrixFactory.newVector(this.chassis.getLaenge()
				/ 2 - l / 2, this.chassis.getHoehe() / 2 + h / 2, 0);

		Heck2D erg = new Heck2D(p, h, l, degree, heckh, heckv);
		return erg;
	}

	private Gast2D generateGast(double hoehe, double laenge, int lmin,
			int lmax, int degree, int gastv, int gasth, int gastFrontHor, int gastFrontVer, 
			int gastHeckHor, int gastHeckVer) {

		// Hoehe von Gast = Hoehe von Auto minus Hoehe von Chassis
		double h = (hoehe - this.chassis.getHoehe());

		// Laenge von Gast = 1/3 Laenge des Autos
		double l = laenge * (((double) lmax - (double) lmin) / 100);

		// Postition von Gast = rechts von Front Center und um haelfte von
		// Gast Hoehe hoeher
		Vector p = VectorMatrixFactory.newVector(
				0 - this.chassis.getLaenge() / 2 + this.front.getLaenge() + l
						/ 2, this.chassis.getHoehe() / 2 + h / 2, 0);

		Vector frontPunkt = this.front.getC();
		Vector heckPunkt = this.heck.getB();

		Gast2D erg = new Gast2D(p, h, l, degree, frontPunkt, heckPunkt, gastv,
				gasth, gastFrontHor, gastFrontVer, gastHeckHor, gastHeckVer);
		return erg;
	}

	public Chassis2D getChassis() {
		return chassis;
	}

	public void setChassis(Chassis2D chassis) {
		this.chassis = chassis;
	}

	public Front2D getFront() {
		return front;
	}

	public void setFront(Front2D front) {
		this.front = front;
	}

	public Gast2D getGast() {
		return gast;
	}

	public void setGast(Gast2D gast) {
		this.gast = gast;
	}

	public Heck2D getHeck() {
		return heck;
	}

	public void setHeck(Heck2D heck) {
		this.heck = heck;
	}

}

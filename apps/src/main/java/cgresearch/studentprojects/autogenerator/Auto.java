package cgresearch.studentprojects.autogenerator;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

public class Auto {
	private Chassis chassis;
	private Front front;
	private Gast gast;
	private Heck heck;

	public Auto(Chassis chassis, Front front, Gast gast, Heck heck) {
		super();
		this.chassis = chassis;
		this.front = front;
		this.gast = gast;
		this.heck = heck;
	}


	public Auto(double hoehe, double laenge, double breite, int min, int max) {
		this.chassis = generateChassis(hoehe, laenge, breite);
		this.front = generateFront(hoehe, laenge, breite, min);
		this.gast = generateGast(hoehe, laenge, breite, min, max);
		this.heck = generateHeck(hoehe, laenge, breite, max);
	}

	private Chassis generateChassis(double hoehe, double laenge, double breite) {

		// Beite von Chassis = Breite des Autos
		double b = breite;

		// Hoehe von Chassis = 1/10 von Autos Hoehe
		double h = hoehe / 10;

		// Laenge von Chassis = Laenge des Autos
		double l = laenge;

		// Postition von Chassis = 0,0,0
		IVector3 p = VectorMatrixFactory.newIVector3(0, 0, 0);

		Chassis erg = new Chassis(p, b, h, l);
		return erg;
	}

	private Front generateFront(double hoehe, double laenge, double breite,
			int lmin) {

		// Beite von Front = Breite des Autos
		double b = breite;

		// Hoehe von Front = 1/3 von Autos Hoehe
		double h = hoehe / 3;

		// Laenge von Front = 1/3 Laenge des Autos
		double l = laenge * ((double) lmin / 100);
		
		// Postition von Front = links von Chassis Center und um haelfte von
		// Front Hoehe hoeher
		IVector3 p = VectorMatrixFactory.newIVector3(0, this.chassis.getHoehe()
				/ 2 + h / 2, 0 - this.chassis.getLaenge() / 2 + l / 2);

		Front erg = new Front(p, b, h, l);
		return erg;
	}

	private Gast generateGast(double hoehe, double laenge, double breite,
			int lmin, int lmax) {

		// Beite von Gast = Breite des Autos
		double b = breite;

		// Hoehe von Gast = Hoehe von Auto minus Hoehe von Chassis
		double h = (hoehe - this.chassis.getHoehe());

		// Laenge von Gast = 1/3 Laenge des Autos
		double l = laenge * (((double) lmax - (double) lmin) / 100);

		// Postition von Gast = rechts von Front Center und um haelfte von
		// Gast Hoehe hoeher
		IVector3 p = VectorMatrixFactory.newIVector3(0, this.chassis.getHoehe()
				/ 2 + h / 2,
				0 - this.chassis.getLaenge() / 2 + this.front.getLaenge() + l
						/ 2);

		Gast erg = new Gast(p, b, h, l);
		return erg;
	}

	private Heck generateHeck(double hoehe, double laenge, double breite,
			int lmax) {

		// Beite von Heck = Breite des Autos
		double b = breite;

		// Hoehe von Heck = 1/2 von Autos Hoehe
		double h = hoehe / 2;

		// Laenge von Heck = 1/3 Laenge des Autos
		double l = laenge * ((100 - (double) lmax) / 100);

		// Postition von Heck = rechts von Chassis Center und um haelfte von
		// Heck Hoehe hoeher
		IVector3 p = VectorMatrixFactory.newIVector3(0, this.chassis.getHoehe()
				/ 2 + h / 2, this.chassis.getLaenge() / 2 - l / 2);

		Heck erg = new Heck(p, b, h, l);
		return erg;
	}

	public Chassis getChassis() {
		return chassis;
	}

	public void setChassis(Chassis chassis) {
		this.chassis = chassis;
	}

	public Front getFront() {
		return front;
	}

	public void setFront(Front front) {
		this.front = front;
	}

	public Gast getGast() {
		return gast;
	}

	public void setGast(Gast gast) {
		this.gast = gast;
	}

	public Heck getHeck() {
		return heck;
	}

	public void setHeck(Heck heck) {
		this.heck = heck;
	}

}

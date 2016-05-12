package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;

public class Analyzer {

	// Einkommentieren fuer Schmetterlinge
	// private final int butVektor = 32;
	// private final int butSkalar = 10;

	// Einkommentieren fuer Farhzeuge
	private final int butVektor = 18;
	private final int butSkalar = 7;

	
	private PCA pcaX = new PCA();
	private PCA pcaY = new PCA();
	private PCA pcaZ = new PCA();
	private List<Vector> eigenX = new ArrayList<Vector>();
	private List<Vector> eigenY = new ArrayList<Vector>();
	private List<Vector> eigenZ = new ArrayList<Vector>();
	private Vector valueX = new Vector(butVektor);
	private Vector valueY = new Vector(butVektor);
	private Vector valueZ = new Vector(butVektor);
	private List<Vector> Bx = new ArrayList<Vector>();
	private List<Vector> By = new ArrayList<Vector>();
	private List<Vector> Bz = new ArrayList<Vector>();
	private List<Vector> Btx = new ArrayList<Vector>();
	private List<Vector> Bty = new ArrayList<Vector>();
	private List<Vector> Btz = new ArrayList<Vector>();
	private int dimension;

	// Einkommentieren fuer Farhzeuge
	public void applyPCA(Data2D18 data) {

		// Einkommentieren fuer Schmetterlinge
		// public void applyPCA(ButData32 data) {
		int i;
		dimension = data.getX().get(0).getDimension();

		System.out.println("Dimension: " + dimension);

		// PCA for X-Vektor
		pcaX.clear();
		System.out.println("X-Werte:");
		i = 0;
		for (Vector v : data.getX()) {
			pcaX.add(v);
			System.out.println("Vektor " + i + ":");
			System.out.println(v);
			i++;
		}
		pcaX.applyPCA();
		for (int j = 0; j < data.getX().get(0).getDimension(); j++) {
			eigenX.add(pcaX.getEigenVector(j));
			valueX.set(j, pcaX.getEigenValue(j));

		}

		Bx.clear();
		for (int j = 0; j < butSkalar; j++) {
			Bx.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("Bx dimension: " + Bx.size());

		Btx.clear();
		for (int j = 0; j < dimension; j++) {
			Vector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, Bx.get(k).get(j));
			}
			Btx.add(v);
		}
		System.out.println("Btx dimension: " + Btx.size());

		System.out.println("Eigenvektoren X:");
		for (Vector v : eigenX) {
			System.out.println("Eigevektor " + eigenX.indexOf(v) + ":");
			System.out.println(v);
		}

		// PCA for Y-Vektor
		pcaY.clear();
		System.out.println("Y-Werte:");
		i = 0;
		for (Vector v : data.getY()) {
			pcaY.add(v);
			System.out.println("Vektor " + i + ":");
			System.out.println(v);
			i++;
		}
		pcaY.applyPCA();
		for (int j = 0; j < data.getY().get(0).getDimension(); j++) {
			eigenY.add(pcaY.getEigenVector(j));
			valueY.set(j, pcaX.getEigenValue(j));
		}

		By.clear();
		for (int j = 0; j < butSkalar; j++) {
			By.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("By dimension: " + By.size());

		Bty.clear();
		for (int j = 0; j < dimension; j++) {
			Vector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, By.get(k).get(j));
			}
			Bty.add(v);
		}
		System.out.println("Bty dimension: " + Bty.size());

		System.out.println("Eigenvektoren Y:");
		for (Vector v : eigenY) {
			System.out.println("Eigevektor " + eigenY.indexOf(v) + ":");
			System.out.println(v);
		}

		// PCA for Z-Vektor
		pcaZ.clear();
		System.out.println("Z-Werte:");
		i = 0;
		for (Vector v : data.getZ()) {
			pcaZ.add(v);
			System.out.println("Vektor " + i + ":");
			System.out.println(v);
			i++;
		}
		pcaZ.applyPCA();
		for (int j = 0; j < data.getZ().get(0).getDimension(); j++) {
			eigenZ.add(pcaZ.getEigenVector(j));
			valueZ.set(j, pcaX.getEigenValue(j));
		}

		Bz.clear();
		for (int j = 0; j < butSkalar; j++) {
			Bz.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("Bz dimension: " + Bz.size());

		Btz.clear();
		for (int j = 0; j < dimension; j++) {
			Vector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, Bz.get(k).get(j));
			}
			Btz.add(v);
		}
		System.out.println("Btz dimension: " + Btz.size());

		System.out.println("Eigenvektoren Z:");
		for (Vector v : eigenZ) {
			System.out.println("Eigevektor " + eigenZ.indexOf(v) + ":");
			System.out.println(v);
		}

		System.out.println("Eigenwerte X:");
		System.out.println(valueX);
		System.out.println("Eigenwerte Y:");
		System.out.println(valueY);
		System.out.println("Eigenwerte Z:");
		System.out.println(valueZ);

		System.out.println("Bx");
		System.out.println(Bx.get(6).get(0));
		System.out.println("Btx");
		System.out.println(Btx.get(0).get(6));
		System.out.println("By");
		System.out.println(By.get(6).get(0));
		System.out.println("Bty");
		System.out.println(Bty.get(0).get(6));

		Bx.clear();
		for (int j = 0; j < butVektor; j++) {
			Bx.add(eigenX.get(dimension - j - 1));
		}
		By.clear();
		for (int j = 0; j < butVektor; j++) {
			By.add(eigenY.get(dimension - j - 1));
		}
		Bz.clear();
		for (int j = 0; j < butVektor; j++) {
			Bz.add(eigenZ.get(dimension - j - 1));
		}

		// Einkommentieren wenn nicht reduziert

		// Btx.clear();
		// for (int j = 0; j < dimension; j++) {
		// Vector v = new Vector(carVektor);
		// for (int k = 0; k < carVektor; k++) {
		// v.set(k, Bx.get(k).get(j));
		// }
		// Btx.add(v);
		// }
		// Bty.clear();
		// for (int j = 0; j < dimension; j++) {
		// Vector v = new Vector(carVektor);
		// for (int k = 0; k < carVektor; k++) {
		// v.set(k, By.get(k).get(j));
		// }
		// Bty.add(v);
		// }
		// Btz.clear();
		// for (int j = 0; j < dimension; j++) {
		// Vector v = new Vector(carVektor);
		// for (int k = 0; k < carVektor; k++) {
		// v.set(k, Bz.get(k).get(j));
		// }
		// Btz.add(v);
		// }

		System.out.println("Bx dim: " + Bx.size());
		System.out.println("By dim: " + By.size());
		System.out.println("Bz dim: " + Bz.size());

		System.out.println("Btx dim: " + Btx.size());
		System.out.println("Bty dim: " + Bty.size());
		System.out.println("Btz dim: " + Btz.size());

	}

	public List<Vector> getEigenX() {
		return eigenX;
	}

	public void setEigenX(List<Vector> eigenX) {
		this.eigenX = eigenX;
	}

	public List<Vector> getEigenY() {
		return eigenY;
	}

	public void setEigenY(List<Vector> eigenY) {
		this.eigenY = eigenY;
	}

	public List<Vector> getEigenZ() {
		return eigenZ;
	}

	public void setEigenZ(List<Vector> eigenZ) {
		this.eigenZ = eigenZ;
	}

	public Vector getValueX() {
		return valueX;
	}

	public void setValueX(Vector valueX) {
		this.valueX = valueX;
	}

	public Vector getValueY() {
		return valueY;
	}

	public void setValueY(Vector valueY) {
		this.valueY = valueY;
	}

	public Vector getValueZ() {
		return valueZ;
	}

	public void setValueZ(Vector valueZ) {
		this.valueZ = valueZ;
	}

	public PCA getPcaX() {
		return pcaX;
	}

	public void setPcaX(PCA pcaX) {
		this.pcaX = pcaX;
	}

	public PCA getPcaY() {
		return pcaY;
	}

	public void setPcaY(PCA pcaY) {
		this.pcaY = pcaY;
	}

	public PCA getPcaZ() {
		return pcaZ;
	}

	public void setPcaZ(PCA pcaZ) {
		this.pcaZ = pcaZ;
	}

	public List<Vector> getBx() {
		return Bx;
	}

	public void setBx(List<Vector> bx) {
		Bx = bx;
	}

	public List<Vector> getBy() {
		return By;
	}

	public void setBy(List<Vector> by) {
		By = by;
	}

	public List<Vector> getBz() {
		return Bz;
	}

	public void setBz(List<Vector> bz) {
		Bz = bz;
	}

	public List<Vector> getBtx() {
		return Btx;
	}

	public void setBtx(List<Vector> btx) {
		Btx = btx;
	}

	public List<Vector> getBty() {
		return Bty;
	}

	public void setBty(List<Vector> bty) {
		Bty = bty;
	}

	public List<Vector> getBtz() {
		return Btz;
	}

	public void setBtz(List<Vector> btz) {
		Btz = btz;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

}

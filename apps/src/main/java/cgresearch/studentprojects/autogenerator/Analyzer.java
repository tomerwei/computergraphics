package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector;
import cgresearch.core.math.Vector;

public class Analyzer {

	// private final int carVektor = 18;
	// private final int skalar = 7;
	private final int butVektor = 32;
	private final int butSkalar = 10;

	// private final int butVektor = 18;
	// private final int butSkalar = 7;

	private PCA pcaX = new PCA();
	private PCA pcaY = new PCA();
	private PCA pcaZ = new PCA();
	private List<IVector> eigenX = new ArrayList<IVector>();
	private List<IVector> eigenY = new ArrayList<IVector>();
	private List<IVector> eigenZ = new ArrayList<IVector>();
	private IVector valueX = new Vector(butVektor);
	private IVector valueY = new Vector(butVektor);
	private IVector valueZ = new Vector(butVektor);
	private List<IVector> Bx = new ArrayList<IVector>();
	private List<IVector> By = new ArrayList<IVector>();
	private List<IVector> Bz = new ArrayList<IVector>();
	private List<IVector> Btx = new ArrayList<IVector>();
	private List<IVector> Bty = new ArrayList<IVector>();
	private List<IVector> Btz = new ArrayList<IVector>();
	private int dimension;

	// public void applyPCA(Data2D data) {
	// public void applyPCA(Data2D18 data) {
	public void applyPCA(ButData32 data) {
		int i;
		dimension = data.getX().get(0).getDimension();

		System.out.println("Dimension: " + dimension);

		// PCA for X-Vektor
		pcaX.clear();
		System.out.println("X-Werte:");
		i = 0;
		for (IVector v : data.getX()) {
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
			IVector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, Bx.get(k).get(j));
			}
			Btx.add(v);
		}
		System.out.println("Btx dimension: " + Btx.size());

		System.out.println("Eigenvektoren X:");
		for (IVector v : eigenX) {
			System.out.println("Eigevektor " + eigenX.indexOf(v) + ":");
			System.out.println(v);
		}

		// PCA for Y-Vektor
		pcaY.clear();
		System.out.println("Y-Werte:");
		i = 0;
		for (IVector v : data.getY()) {
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
			IVector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, By.get(k).get(j));
			}
			Bty.add(v);
		}
		System.out.println("Bty dimension: " + Bty.size());

		System.out.println("Eigenvektoren Y:");
		for (IVector v : eigenY) {
			System.out.println("Eigevektor " + eigenY.indexOf(v) + ":");
			System.out.println(v);
		}

		// PCA for Z-Vektor
		pcaZ.clear();
		System.out.println("Z-Werte:");
		i = 0;
		for (IVector v : data.getZ()) {
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
			IVector v = new Vector(butSkalar);
			for (int k = 0; k < butSkalar; k++) {
				v.set(k, Bz.get(k).get(j));
			}
			Btz.add(v);
		}
		System.out.println("Btz dimension: " + Btz.size());

		System.out.println("Eigenvektoren Z:");
		for (IVector v : eigenZ) {
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
		// IVector v = new Vector(carVektor);
		// for (int k = 0; k < carVektor; k++) {
		// v.set(k, Bx.get(k).get(j));
		// }
		// Btx.add(v);
		// }
		// Bty.clear();
		// for (int j = 0; j < dimension; j++) {
		// IVector v = new Vector(carVektor);
		// for (int k = 0; k < carVektor; k++) {
		// v.set(k, By.get(k).get(j));
		// }
		// Bty.add(v);
		// }
		// Btz.clear();
		// for (int j = 0; j < dimension; j++) {
		// IVector v = new Vector(carVektor);
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

	public List<IVector> getEigenX() {
		return eigenX;
	}

	public void setEigenX(List<IVector> eigenX) {
		this.eigenX = eigenX;
	}

	public List<IVector> getEigenY() {
		return eigenY;
	}

	public void setEigenY(List<IVector> eigenY) {
		this.eigenY = eigenY;
	}

	public List<IVector> getEigenZ() {
		return eigenZ;
	}

	public void setEigenZ(List<IVector> eigenZ) {
		this.eigenZ = eigenZ;
	}

	public IVector getValueX() {
		return valueX;
	}

	public void setValueX(IVector valueX) {
		this.valueX = valueX;
	}

	public IVector getValueY() {
		return valueY;
	}

	public void setValueY(IVector valueY) {
		this.valueY = valueY;
	}

	public IVector getValueZ() {
		return valueZ;
	}

	public void setValueZ(IVector valueZ) {
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

	public List<IVector> getBx() {
		return Bx;
	}

	public void setBx(List<IVector> bx) {
		Bx = bx;
	}

	public List<IVector> getBy() {
		return By;
	}

	public void setBy(List<IVector> by) {
		By = by;
	}

	public List<IVector> getBz() {
		return Bz;
	}

	public void setBz(List<IVector> bz) {
		Bz = bz;
	}

	public List<IVector> getBtx() {
		return Btx;
	}

	public void setBtx(List<IVector> btx) {
		Btx = btx;
	}

	public List<IVector> getBty() {
		return Bty;
	}

	public void setBty(List<IVector> bty) {
		Bty = bty;
	}

	public List<IVector> getBtz() {
		return Btz;
	}

	public void setBtz(List<IVector> btz) {
		Btz = btz;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

}

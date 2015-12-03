package cgresearch.studentprojects.autogenerator;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.IVector;
import cgresearch.core.math.Vector;

public class Analyzer {

	private PCA pcaX = new PCA();
	private PCA pcaY = new PCA();
	private PCA pcaZ = new PCA();
	private List<IVector> eigenX = new ArrayList<IVector>();
	private List<IVector> eigenY = new ArrayList<IVector>();
	private List<IVector> eigenZ = new ArrayList<IVector>();
	private IVector valueX = new Vector(28);
	private IVector valueY = new Vector(28);
	private IVector valueZ = new Vector(28);
	private List<IVector> Bx = new ArrayList<IVector>();
	private List<IVector> By = new ArrayList<IVector>();
	private List<IVector> Bz = new ArrayList<IVector>();
	private List<IVector> Btx = new ArrayList<IVector>();
	private List<IVector> Bty = new ArrayList<IVector>();
	private List<IVector> Btz = new ArrayList<IVector>();
	private int dimension;

	public void applyPCA(Data2D data) {
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
		for (int j = 0; j < 10; j++) {
			Bx.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("Bx dimension: " + Bx.size());

		Btx.clear();
		for (int j = 0; j < dimension; j++) {
			IVector v = new Vector(10);
			for (int k = 0; k < 10; k++) {
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
		for (int j = 0; j < 10; j++) {
			By.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("By dimension: " + By.size());

		Bty.clear();
		for (int j = 0; j < dimension; j++) {
			IVector v = new Vector(10);
			for (int k = 0; k < 10; k++) {
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
		for (int j = 0; j < 10; j++) {
			Bz.add(eigenX.get(dimension - j - 1));
		}
		System.out.println("Bz dimension: " + Bz.size());

		Btz.clear();
		for (int j = 0; j < dimension; j++) {
			IVector v = new Vector(10);
			for (int k = 0; k < 10; k++) {
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
		System.out.println(Bx.get(9).get(0));
		System.out.println("Btx");
		System.out.println(Btx.get(0).get(9));
		System.out.println("By");
		System.out.println(By.get(9).get(0));
		System.out.println("Bty");
		System.out.println(Bty.get(0).get(9));
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

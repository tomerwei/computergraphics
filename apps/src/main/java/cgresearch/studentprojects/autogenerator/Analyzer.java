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

	public void applyPCA(Data2D data) {
		int i;

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
			// valueX.set(data.getX().get(0).getDimension() - 1 - j,
			// pcaX.getEigenValue(j));
			valueX.set(j, pcaX.getEigenValue(j));

		}
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
			// valueY.set(data.getY().get(0).getDimension() - 1 - j,
			// pcaY.getEigenValue(j));
			valueY.set(j, pcaX.getEigenValue(j));
		}
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
			// valueZ.set(data.getZ().get(0).getDimension() - 1 - j,
			// pcaZ.getEigenValue(j));
			valueZ.set(j, pcaX.getEigenValue(j));
		}
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

}

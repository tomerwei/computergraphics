package cgresearch.studentprojects.registration;

/**
 * class to save the distance and the associated nearest point 
 * 
 * @param nearestPoints
 * @param base
 * @param register
 * 
 */

public class TrimmedDistance {

	final double distance;

	final int indexForBase;
	final int indexForRegister;

	public TrimmedDistance(double distance, int indexForBase, int indexForRegister) {
		this.distance = distance;
		this.indexForBase = indexForBase;
		this.indexForRegister = indexForRegister;
	}



	public double getDistance() {
		return this.distance;
	}

	public int getIndexForBase() {
		return this.indexForBase;
	}
	
	public int getIndexForRegister() {
		return this.indexForRegister;
	}



	@Override
	public String toString() {
		return getIndexForBase() + " ; " + indexForRegister + " ; " + getDistance() +"\n";
	}

}

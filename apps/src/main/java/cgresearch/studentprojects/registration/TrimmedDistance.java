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

	final int index;

	public TrimmedDistance(double distance, int index) {
		this.distance = distance;
		this.index = index;
	}



	public double getDistance() {
		return this.distance;
	}

	public int getIndex() {
		return this.index;
	}



	@Override
	public String toString() {
		return getIndex() + " ; " + getDistance() +"\n";
	}

}

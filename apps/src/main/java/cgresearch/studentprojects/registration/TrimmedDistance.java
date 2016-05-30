package cgresearch.studentprojects.registration;

public class TrimmedDistance {
	
	final double distance;

    final double index;

    public TrimmedDistance(double distance, int index) {
        this.distance = distance;
        this.index = index;
    }
    
    

    public double getDistance() {
        return this.distance;
    }

    public double getIndex() {
        return this.index;
    }
    
    

    @Override
    public String toString() {
        return getDistance() + ";" + getIndex()+"\n";
    }

}

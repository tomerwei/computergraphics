package cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects;

/**
 * The Class VirtualPoint is a Virtual Point and hold the Informations of an X,Y and Z Position.
 * @author Thorben Watzl
 */
public class VirtualPoint {
	
	/** The x Position. */
	private double x;
	
	/** The y Position. */
	private double y;
	
	/** The z Position. */
	private double z;
	
	/** The direction. */
	private VirtualDirection direction;
	
	/**
	 * Instantiates a new virtual point.
	 *
	 * @param x the x Position
	 * @param y the y Position
	 * @param z the z Position
	 */
	public VirtualPoint(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Instantiates a new virtual point.
	 *
	 * @param x the x Position
	 * @param y the y Position
	 * @param z the z Position
	 * @param direction the direction
	 */
	public VirtualPoint(double x, double y, double z, VirtualDirection direction){
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}

	/**
	 * Gets the x Position.
	 *
	 * @return the x Position
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y Position.
	 *
	 * @return the y Position
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z Position.
	 *
	 * @return the z Position
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public VirtualDirection getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(VirtualDirection direction) {
		this.direction = direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VirtualPoint other = (VirtualPoint) obj;
		if (direction != other.direction)
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
}

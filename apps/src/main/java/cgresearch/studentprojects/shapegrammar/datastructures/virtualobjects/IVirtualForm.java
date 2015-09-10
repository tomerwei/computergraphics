package cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects;

/**
 * Interface For Virtual Forms.
 *
 * @author Thorben Watzl
 */
public interface IVirtualForm {
	
	/**
	 * Return The Name From The Virtual Form.
	 *
	 * @return Name From The Virtual Form
	 */
	String getName();
	
	/**
	 * Return The Width Of The Virtual Form.
	 *
	 * @return Width Of The Virtual Form
	 */
	double getWidth();
	
	/**
	 * Sets the width.
	 */
	void setWidth(double width);
	
	/**
	 * Return The Height Of The Virtual Form.
	 *
	 * @return Height Of The Virtual Form
	 */
	double getHeight();
	
	/**
	 * Sets the height.
	 */
	void setHeight(double height);
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	VirtualPoint getPosition();
	
	/**
	 * Sets the position.
	 */
	void setPosition(VirtualPoint point);
}

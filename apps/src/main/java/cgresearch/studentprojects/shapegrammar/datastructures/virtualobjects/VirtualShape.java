package cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects;

/**
 * The Class VirtualShape is a Virtual Shape that hold the information of the position and the Size.
 * @author Thorben Watzl
 */
public class VirtualShape implements IVirtualForm {
	
	/** The width. */
	private double width;
	
	/** The height. */
	private double height;
	
	/** The position. */
	private VirtualPoint position;
	
	/** The name. */
	private String name;
	
	/** The texture. */
	private String texture;
	
	/**
	 * Instantiates a new virtual shape.
	 *
	 * @param name the name
	 * @param position the position
	 * @param width the width
	 * @param height the height
	 */
	public VirtualShape(String name, VirtualPoint position, double width, double height){
		this.name = name;
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(VirtualPoint position) {
		this.position = position;
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#getWidth()
	 */
	public double getWidth() {
		return width;
	}
	
	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#getHeight()
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public VirtualPoint getPosition() {
		return position;
	}

	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * Sets the texture.
	 *
	 * @param texture the new texture
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#getName()
	 */
	public String getName() {
		return name;
	}	
}

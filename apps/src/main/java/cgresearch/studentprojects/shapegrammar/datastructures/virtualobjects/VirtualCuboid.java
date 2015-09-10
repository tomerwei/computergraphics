package cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class VirtualCuboid.
 * 
 * @author Thorben Watzl
 */
public class VirtualCuboid implements IVirtualForm {
	
	/** The cuboid sides. */
	private Map<String, VirtualShape> cuboidSides;
	
	/** The width. */
	private double width;
	
	/** The height. */
	private double height;
	
	/** The lendth. */
	private double length;
	
	/** The name. */
	private String name; 
	
	
	/** The position. */
	private VirtualPoint position;
	
	/**
	 * Instantiates a new virtual cuboid.
	 *
	 * @param name the name
	 * @param position the position
	 * @param width the width
	 * @param height the height
	 * @param lendth the lendth
	 */
	public VirtualCuboid(String name, VirtualPoint position, double width, double height, double lendth){
		cuboidSides = new HashMap<String, VirtualShape>();
		this.width = width;
		this.height = height;
		this.length = lendth;
		this.name = name;
		position.setDirection(VirtualDirection.Front);
		this.position = position;
		calculateSides();
	}
	
	/**
	 * Gets the cuboid side.
	 *
	 * @param side the side Name
	 * @return the cuboid side
	 */
	public VirtualShape getCuboidSide(String side) {
		return cuboidSides.get(side);
	}
	
	/**
	 * Gets the top side.
	 *
	 * @return the top side
	 */
	public VirtualShape getTop() {
		return cuboidSides.get("top");
	}

	/**
	 * Gets the left side.
	 *
	 * @return the left side
	 */
	public VirtualShape getLeft() {
		return cuboidSides.get("left");
	}

	/**
	 * Gets the right side.
	 *
	 * @return the right side
	 */
	public VirtualShape getRight() {
		return cuboidSides.get("right");
	}

	/**
	 * Gets the front side.
	 *
	 * @return the front side
	 */
	public VirtualShape getFront() {
		return cuboidSides.get("front");
	}

	/**
	 * Gets the back side.
	 *
	 * @return the back side
	 */
	public VirtualShape getBack() {
		return cuboidSides.get("back");
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#getName()
	 */
	public String getName() {
		return name;
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
	 * Gets the lendth.
	 *
	 * @return the lendth
	 */
	public double getLendth() {
		return length;
	}
	
	
	/**
	 * Sets the lendth.
	 *
	 * @param lendth the new lendth
	 */
	public void setLendth(double lendth) {
		this.length = lendth;
		calculateSides();
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#setWidth(double)
	 */
	public void setWidth(double width) {
		this.width = width;
		calculateSides();
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#setHeight(double)
	 */
	public void setHeight(double height) {
		this.height = height;
		calculateSides();
	}
	
	/**
	 * Sets the size.
	 *
	 * @param width the width
	 * @param height the height
	 * @param length the length
	 */
	public void setSize(double width, double height, double length){
		this.width = width;
		this.height = height;
		this.length = length;
		calculateSides();
	}
	
	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#getPosition()
	 */
	public VirtualPoint getPosition() {
		return position;
	}

	/* (nicht-Javadoc)
	 * @see edu.haw.cg.buildingbuilder.datastructures.virtualobjects.IVirtualForm#setPosition(edu.haw.cg.buildingbuilder.datastructures.virtualobjects.VirtualPoint)
	 */
	public void setPosition(VirtualPoint point) {
		position = point;
		calculateSides();
	}
	
	/**
	 * Calculate sides.
	 */
	private void calculateSides(){
		cuboidSides.put("front", new VirtualShape("front", position, width, height));
		cuboidSides.put("back", new VirtualShape("back", new VirtualPoint(position.getX()+width, position.getY(), position.getZ()-length, VirtualDirection.Back), width, height));
		cuboidSides.put("left", new VirtualShape("left", new VirtualPoint(position.getX(), position.getY(), position.getZ()-length, VirtualDirection.Left), length, height));
		cuboidSides.put("right", new VirtualShape("right", new VirtualPoint(position.getX()+width, position.getY(), position.getZ(), VirtualDirection.Right), length, height));
		cuboidSides.put("top", new VirtualShape("top", new VirtualPoint(position.getX(), position.getY()+height, position.getZ(), VirtualDirection.Top), width, length));
		cuboidSides.put("bot", new VirtualShape("bot", new VirtualPoint(position.getX(), position.getY(), position.getZ()-length, VirtualDirection.Bot), width, length));
	}

	/**
	 * Gets the bot side.
	 *
	 * @return the bot side
	 */
	public VirtualShape getBot() {
		return cuboidSides.get("bot");
	}
}

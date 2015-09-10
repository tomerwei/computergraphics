package cgresearch.studentprojects.shapegrammar.settings;

import cgresearch.studentprojects.shapegrammar.datastructures.tree.RuleTree;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.VirtualFormTree;


/**
 * The Class BuildingSettings contains the settings of a building.
 * @author Thorben Watzl
 */
public class BuildingSettings{
	
	/** The rule tree. */
	private RuleTree ruleTree;
	
	/** The form tree. */
	private VirtualFormTree formTree;
	
	/** The width. */
	private double width;
	
	/** The height. */
	private double height;
	
	/** The length. */
	private double length;
	
	/** The x. */
	private double x;
	
	/** The z. */
	private double z;
	
	/** The building dir. */
	private String buildingDir;
	
	/**
	 * Instantiates a new building settings.
	 */
	public BuildingSettings(){
		ruleTree = new RuleTree();
		formTree = new VirtualFormTree();
		width = 0;
		height = 0;
		length = 0;
		x = 0;
		z = 0;
		buildingDir = "";
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return width;
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
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return height;
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
	 * Gets the building dir.
	 *
	 * @return the building dir
	 */
	public String getBuildingDir() {
		return buildingDir;
	}

	/**
	 * Sets the building dir.
	 *
	 * @param buildingDir the new building dir
	 */
	public void setBuildingDir(String buildingDir) {
		this.buildingDir = buildingDir;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the new length
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * Gets the rule tree.
	 *
	 * @return the rule tree
	 */
	public RuleTree getRuleTree() {
		return ruleTree;
	}

	/**
	 * Sets the rule tree.
	 *
	 * @param ruleTree the new rule tree
	 */
	public void setRuleTree(RuleTree ruleTree) {
		this.ruleTree = ruleTree;
	}

	/**
	 * Gets the form tree.
	 *
	 * @return the form tree
	 */
	public VirtualFormTree getFormTree() {
		return formTree;
	}

	/**
	 * Sets the form tree.
	 *
	 * @param formTree the new form tree
	 */
	public void setFormTree(VirtualFormTree formTree) {
		this.formTree = formTree;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Sets the z.
	 *
	 * @param z the new z
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
}

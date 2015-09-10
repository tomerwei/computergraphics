package cgresearch.studentprojects.shapegrammar.settings;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CitySettings store all city settings.
 * @author Thorben Watzl
 */
public class CitySettings {
	
	/** The number buildings x input. */
	private double numberBuildingsXInput;
	
	/** The number buildings Z input. */
	private double numberBuildingsZInput;
	
	/** The min width. */
	private double minWidth;
	
	/** The max width. */
	private double maxWidth;
	
	/** The min height. */
	private double minHeight;
	
	/** The max height. */
	private double maxHeight;
	
	/** The min lendth. */
	private double minLength;
	
	/** The max lendth. */
	private double maxLength;
	
	/** The building rules dir. */
	private List<String> buildingRulesDir;
	
	/**
	 * Instantiates a new city settings.
	 */
	public CitySettings() {
		buildingRulesDir = new ArrayList<String>();
	}
	
	/**
	 * Gets the number buildings x input.
	 *
	 * @return the number buildings x input
	 */
	public double getNumberBuildingsXInput() {
		return numberBuildingsXInput;
	}
	
	/**
	 * Sets the number buildings x input.
	 *
	 * @param numberBuildingsXInput the new number buildings x input
	 */
	public void setNumberBuildingsXInput(double numberBuildingsXInput) {
		this.numberBuildingsXInput = numberBuildingsXInput;
	}
	
	/**
	 * Gets the number buildings y input.
	 *
	 * @return the number buildings y input
	 */
	public double getNumberBuildingsZInput() {
		return numberBuildingsZInput;
	}
	
	/**
	 * Sets the number buildings y input.
	 *
	 * @param numberBuildingsYInput the new number buildings y input
	 */
	public void setNumberBuildingsZInput(double numberBuildingsYInput) {
		this.numberBuildingsZInput = numberBuildingsYInput;
	}
	
	/**
	 * Gets the min width.
	 *
	 * @return the min width
	 */
	public double getMinWidth() {
		return minWidth;
	}
	
	/**
	 * Sets the min width.
	 *
	 * @param minWidth the new min width
	 */
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
	}
	
	/**
	 * Gets the max width.
	 *
	 * @return the max width
	 */
	public double getMaxWidth() {
		return maxWidth;
	}
	
	/**
	 * Sets the max width.
	 *
	 * @param maxWidth the new max width
	 */
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	/**
	 * Gets the min height.
	 *
	 * @return the min height
	 */
	public double getMinHeight() {
		return minHeight;
	}
	
	/**
	 * Sets the min height.
	 *
	 * @param minHeight the new min height
	 */
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
	}
	
	/**
	 * Gets the max height.
	 *
	 * @return the max height
	 */
	public double getMaxHeight() {
		return maxHeight;
	}
	
	/**
	 * Sets the max height.
	 *
	 * @param maxHeight the new max height
	 */
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	/**
	 * Gets the min lendth.
	 *
	 * @return the min lendth
	 */
	public double getMinLength() {
		return minLength;
	}
	
	/**
	 * Sets the min lendth.
	 *
	 * @param minLendth the new min lendth
	 */
	public void setMinLength(double minLength) {
		this.minLength = minLength;
	}
	
	/**
	 * Gets the max lendth.
	 *
	 * @return the max lendth
	 */
	public double getMaxLength() {
		return maxLength;
	}
	
	/**
	 * Sets the max lendth.
	 *
	 * @param maxLendth the new max lendth
	 */
	public void setMaxLength(double maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * Gets the building rules dir.
	 *
	 * @return the building rules dir
	 */
	public List<String> getBuildingRulesDir() {
		return buildingRulesDir;
	}
	
	/**
	 * Sets the building rules dir.
	 *
	 * @param buildingRulesDir the new building rules dir
	 */
	public void setBuildingRulesDir(List<String> buildingRulesDir) {
		this.buildingRulesDir = buildingRulesDir;
	}
}

package cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects;

import java.util.List;

import cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair;

/**
 * The Class VirtualMethod.
 * This Class is a virtual Method with Method name, Mehtod Parameter and Delegation 
 * @author Thorben Watzl
 */
public class VirtualMethod {
	
	/** The name. */
	private String name;
	
	/** The parameters. */
	private String parameters;
	
	/** The delegation. */
	private List<IRulenameValuePair> delegation;
	
	/** Is True when the Method repeated. */
	private boolean repeat;
	
	/**
	 * Instantiates a new virtual method.
	 *
	 * @param name the name
	 * @param parameters the parameters
	 * @param delegation the delegation
	 * @param repeat the repeat
	 */
	public VirtualMethod(String name, String parameters, 
			List<IRulenameValuePair> delegation, boolean repeat) {
		this.name = name;
		this.parameters = parameters;
		this.delegation = delegation;
		this.repeat = repeat;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Gets the delegation.
	 *
	 * @return the delegation
	 */
	public List<IRulenameValuePair> getDelegation() {
		return delegation;
	}

	/**
	 * Checks if is repeat.
	 *
	 * @return true, if is repeat
	 */
	public boolean isRepeat() {
		return repeat;
	}
}

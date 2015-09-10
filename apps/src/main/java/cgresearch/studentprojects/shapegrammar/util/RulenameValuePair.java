package cgresearch.studentprojects.shapegrammar.util;

import java.util.List;

/**
 * The Class RulenameValuePair contains the rulename and the value from the spit delegation.
 *
 * @author Thorben Watzl.
 */
public class RulenameValuePair implements IRulenameValuePair{
	
	/** The rulename. */
	private String rulename;
	
	/** The values. */
	private List<String> values;
	
	/**
	 * Instantiates a new rulename value pair.
	 *
	 * @param rulename the rulename
	 * @param values the values
	 */
	public RulenameValuePair(String rulename, List<String> values) {
		this.rulename = rulename;
		this.values = values;
	}

	
	/* (nicht-Javadoc)
	 * @see cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair#getRulename()
	 */
	public String getRulename() {
		return rulename;
	}

	
	/* (nicht-Javadoc)
	 * @see cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair#getValues()
	 */
	public List<String> getValues() {
		return values;
	}
}

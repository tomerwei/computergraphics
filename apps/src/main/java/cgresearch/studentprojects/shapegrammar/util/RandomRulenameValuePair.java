package cgresearch.studentprojects.shapegrammar.util;

import java.util.List;

/**
 * The Class RulenameValuePair contains the rulename and the value from the spit delegation.
 *
 * @author Thorben Watzl.
 */
public class RandomRulenameValuePair implements IRulenameValuePair{
	
	/** The selectedRuleValuePair. */
	private IRulenameValuePair selectedRuleValuePair;
	
	/** The random rulenameValuePairs. */
	private RandomCollection<IRulenameValuePair> rulenameValuePairs;
	
	/**
	 * Instantiates a new random rulename value pair.
	 *
	 * @param rulename the rulename
	 * @param values the values
	 * @param probability of the RulenameValuePair
	 */
	public RandomRulenameValuePair(String rulename, List<String> values, double probability) {
		rulenameValuePairs = new RandomCollection<IRulenameValuePair>();
		RulenameValuePair tempRulenameValuePair = new RulenameValuePair(rulename, values);
		rulenameValuePairs.add(probability, tempRulenameValuePair);
	}
	
	/**
	 * Instantiates a new random rulename value pair.
	 *
	 * @param randomCollection the RandomCollection for the RulenameValuePairs
	 */
	public RandomRulenameValuePair(RandomCollection<IRulenameValuePair> randomCollection) {
		rulenameValuePairs = randomCollection;
	}
	
	/**
	 * Instantiates a new random rulename value pair.
	 */
	public RandomRulenameValuePair() {
		rulenameValuePairs = new RandomCollection<IRulenameValuePair>();
	}

	
	/* (nicht-Javadoc)
	 * @see cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair#getRulename()
	 */
	public String getRulename() {
		if(selectedRuleValuePair == null){
			selectedRuleValuePair = rulenameValuePairs.getRandomElement();
		}
		if(selectedRuleValuePair == null){
			return null;
		}
		return selectedRuleValuePair.getRulename();
	}

	
	/* (nicht-Javadoc)
	 * @see cgresearch.studentprojects.shapegrammar.util.IRulenameValuePair#getValues()
	 */
	public List<String> getValues() {
		if(selectedRuleValuePair == null){
			selectedRuleValuePair = rulenameValuePairs.getRandomElement();
		}
		if(selectedRuleValuePair == null){
			return null;
		}
		return selectedRuleValuePair.getValues();
	}
	
	/**
	 * Reset selected.
	 */
	public void resetSelected(){
		selectedRuleValuePair = null;
	}
	
	/**
	 * Adds the RulenameValuePair.
	 *
	 * @param rulename the rulename
	 * @param values the values
	 * @param probability the probability
	 */
	public void add(String rulename, List<String> values, double probability) {
		RulenameValuePair tempRulenameValuePair = new RulenameValuePair(rulename, values);
		rulenameValuePairs.add(probability, tempRulenameValuePair);
	}
	
	public List<IRulenameValuePair> getAllElements() {
		return rulenameValuePairs.getAllElements();
	}
}

package cgresearch.studentprojects.shapegrammar.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import cgresearch.core.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class RandomCollection is a list and give random Elements.
 *
 * @param <T> the generic type
 * @author Thorben Watzl
 */
public class RandomCollection<T>{
	
	/** The random elements. */
	private final NavigableMap<Double, T> randomElements;
	
	/** The random. */
	private final Random random;
	
	/** The given. */
	private double given;
	
	/** The max. */
	private final double max;
	
	/**
	 * Instantiates a new random collection.
	 */
	public RandomCollection(){
		this(100);
	}
	
	/**
	 * Instantiates a new random collection.
	 *
	 * @param maxValue the max value
	 */
	public RandomCollection(double maxValue){
		max = maxValue;
		randomElements = new TreeMap<Double, T>();
		random = new Random();
	}
	
	/**
	 * Adds the.
	 *
	 * @param probability the probability
	 * @param item the item
	 */
	public void add(double probability, T item){
		if(probability <= 0){
			Logger.getInstance().message("Item not added because probability <= 0!");
			return;
		}
		given += probability;
		if(given > max){
			Logger.getInstance().message("Items probability over " + max);
			return;
		}
		randomElements.put(probability, item);
	}
	
	/**
	 * Gets the random element.
	 *
	 * @return the random element
	 */
	public T getRandomElement(){
		double value = random.nextDouble() * max;
		if(randomElements.size() > 1){
			return randomElements.ceilingEntry(value).getValue();
		}else{
			double rand = random.nextDouble() * max;
			if(rand <= value){
				return randomElements.firstEntry().getValue();
			}else{
				return null;
			}
		}
	}
	
	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return randomElements.size();
	}
	
	/**
	 * Gets the all elements.
	 *
	 * @return the all elements
	 */
	public List<T> getAllElements() {
		List<T> returnValue = new ArrayList<T>();
		for (T elemend : randomElements.values()) {
			returnValue.add(elemend);
		}
		return returnValue;
	}
}

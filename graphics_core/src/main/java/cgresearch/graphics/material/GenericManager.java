package cgresearch.graphics.material;

import java.util.HashMap;
import java.util.Map;

import cgresearch.core.logging.Logger;

/**
 * This manager contains and manages all resources of a type.
 * 
 * @author Philipp Jenke
 *
 */
public class GenericManager<T> {
	/**
	 * Map with all resources
	 */
	private Map<String, T> resources = new HashMap<String, T>();

	/**
	 * Private singleton constructor.
	 */
	public GenericManager() {
	}

	/**
	 * Register shader
	 */
	public void addResource(String id, T resource) {
		if (resources.containsKey(id)) {
			Logger.getInstance().error(
					"Resource id " + id
							+ " already exists. Cannot add resource.");
			return;
		}
		resources.put(id, resource);
	}

	/**
	 * Getter for a shader by string
	 */
	public T getResource(String key) {
		return resources.get(key);
	}

	/**
	 * Generate an id candidate of the given length.
	 */
	private String generateId(int length) {
		String baseChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String id = "";
		for (int i = 0; i < length; i++) {
			id += baseChars.charAt((int) (Math.random() * baseChars.length()));
		}
		return id;
	}

	/**
	 * Generate a new unique (unused) id.
	 * 
	 * @return
	 */
	public String generateId() {
		String id = generateId(8);
		while (idExists(id)) {
			id = generateId(8);
		}
		return id;
	}

	/**
	 * Check if the given id is already in use.
	 */
	public boolean idExists(String id) {
		return resources.keySet().contains(id);
	}

	/**
	 * Getter.
	 */
	public int getNumberOfResources() {
		return resources.size();
	}

	/**
	 * Getter for a key at a specified index - so sweet implementation for index
	 * access. Better ideas?
	 */
	public String getKey(int index) {
		if (index < resources.size()) {
			return (String) (resources.keySet().toArray()[index]);
		} else {
			return "";
		}
	}
}

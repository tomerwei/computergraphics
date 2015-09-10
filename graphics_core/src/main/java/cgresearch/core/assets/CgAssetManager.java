package cgresearch.core.assets;

import java.io.InputStream;

/**
 * Central manager instance. Use only this singleton to access assets.
 * 
 * @author Philipp Jenke
 *
 */
public class CgAssetManager {

	/**
	 * This strategy is replace based on the current environment (e.g. file
	 * system, android sandbox).
	 */
	private IAssetManagerStrategy managerStrategy;

	/**
	 * Singleton instance.
	 */
	private static CgAssetManager instance = null;

	/**
	 * Constructor;
	 */
	private CgAssetManager() {
		managerStrategy = new FileSystemAssetManagerStrategy();
	}

	/**
	 * Access the singleton instance
	 */
	public static CgAssetManager getInstance() {
		if (instance == null) {
			instance = new CgAssetManager();
		}
		return instance;
	}

	/**
	 * Setter for the strategy.
	 */
	public void setManagerStrategy(IAssetManagerStrategy newStrategy) {
		this.managerStrategy = newStrategy;
	}

	/**
	 * Return a stream for a local asset path
	 */
	public InputStream getInputStream(String localAssetPath) {
		return managerStrategy.getInputStream(localAssetPath);
	}

}

package cgresearch.core.assets;

import java.io.InputStream;

public interface IAssetManagerStrategy {

	/**
	 * Return a stream for a local asset path
	 */
	public InputStream getInputStream(String localAssetPath);

}

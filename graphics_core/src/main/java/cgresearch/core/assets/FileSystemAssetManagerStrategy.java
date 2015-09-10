package cgresearch.core.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cgresearch.core.logging.Logger;

public class FileSystemAssetManagerStrategy implements IAssetManagerStrategy {

	@Override
	public InputStream getInputStream(String localAssetPath) {
		String path = ResourcesLocator.getInstance().getPathToResource(
				localAssetPath);
		if (path == null) {
			return null;
		}
		try {
			return new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			Logger.getInstance().exception(
					"Failed to open input stream for asset " + localAssetPath,
					e);
			return null;
		}
	}

}

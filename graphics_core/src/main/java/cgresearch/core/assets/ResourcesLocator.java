/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.assets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;

/**
 * This class manages the paths for all resources. It is implemented as a
 * singleton.
 * 
 * @author Philipp Jenke
 * 
 */
public class ResourcesLocator {

	/**
	 * Reference to the Singleton object.
	 */
	private static ResourcesLocator instance = null;

	/**
	 * List of paths
	 */
	private List<String> paths = new ArrayList<String>();

	/**
	 * No public constructor available.
	 */
	private ResourcesLocator() {
		// Empty string is required if the path for a given resource is
		// complete.
		paths.add("");
	}

	/**
	 * Getter for the Singleton instance.
	 */
	public static ResourcesLocator getInstance() {
		if (instance == null) {
			instance = new ResourcesLocator();
		}
		return instance;
	}

	/**
	 * Add a new path to the list of path
	 */
	public void addPath(String pathName) {
		if (new File(pathName).exists()) {
			paths.add(pathName);
			Logger.getInstance().message("Added resource path: " + pathName);
		} else {
			Logger.getInstance().message(
					"Ignored invalid resource path: " + pathName);
		}
	}

	/**
	 * Checks all existing resource paths, if the provided resource (
	 * resourcePath) can be found.
	 * 
	 * @return Returns valid path name, null if the resource cannot be found.
	 */
	public String getPathToResource(String resourcePath) {
		for (String path : paths) {
			if (new File(path + resourcePath).exists()) {
				return path + resourcePath;
			} else if (new File(path + File.separator + resourcePath).exists()) {
				return path + File.separator + resourcePath;
			}
		}
		return null;
	}

	/**
	 * Parse an INI file for additional resource paths.
	 * 
	 * @param iniFilename
	 *            Filename of the INI file.
	 */
	public void parseIniFile(String iniFilename) {
		if (!new File(iniFilename).exists()) {
			Logger.getInstance().message(
					"Failed to find resources INI file " + iniFilename);
			return;
		}
		try {
			InputStream is = new FileInputStream(iniFilename);
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				addPath(strLine.trim());
			}
			is.close();
		} catch (FileNotFoundException e) {
			Logger.getInstance().exception(
					"Failed to parse resource INI file " + iniFilename, e);
		} catch (IOException e) {
			Logger.getInstance().exception(
					"Failed to parse resource INI file " + iniFilename, e);
		}

	}

}

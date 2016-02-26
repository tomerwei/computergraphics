package cgresearch.graphics.misc;

import java.io.File;

/**
 * This class controls the movie export.
 * 
 * @author Philipp Jenke
 *
 */
public class MovieExport {

	/**
	 * Singleton instance.
	 */
	private static MovieExport instance = null;

	/**
	 * This path is used for the exported frames.
	 */
	private String exportPath = "/Users/abo781/Desktop/cg/";

	/**
	 * This base filename for the frames. A counter number and the file
	 * extension are appended.
	 */
	private String frameBaseFilename = "frame";

	/**
	 * If this flag is active the movie export is running.
	 */
	private boolean isActive = false;

	/**
	 * Private constructor required for singleton pattern.
	 */
	private MovieExport() {
	}

	/**
	 * Access the singleton instance.
	 * 
	 * @return
	 */
	public static MovieExport getInstance() {
		if (instance == null) {
			instance = new MovieExport();
		}
		return instance;
	}

	/**
	 * Return the current filename based on the current timer value.
	 */
	public String getCurrentFrameFilename() {
		String filename = exportPath
				+ frameBaseFilename
				+ String.format("%05d", AnimationTimer.getInstance().getValue())
				+ ".png";
		return filename;
	}

	/**
	 * Start recording.
	 */
	public void start() {
		isActive = true;
	}

	/**
	 * Stop recording.
	 */
	public void stop() {
		isActive = false;
	}

	/**
	 * Check if the movie recording is active.
	 */
	public boolean isRunning() {
		return isActive;
	}

	/**
	 * Setter.
	 */
	public void setExportPath(String exportPath) {
		if (exportPath.length() > 0 && !exportPath.endsWith(File.separator)) {
			exportPath += File.separator;
		}
		this.exportPath = exportPath;
	}

	/**
	 * Setter
	 */
	public void setFrameBaseFilename(String frameBaseFilename) {
		this.frameBaseFilename = frameBaseFilename;
	}

	/**
	 * Getter.
	 */
	public String getExportPath() {
		return exportPath;
	}

	/**
	 * Getter.
	 */
	public String getFrameBaseFilename() {
		return frameBaseFilename;
	}
}

package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

import cgresearch.core.logging.Logger;

/**
 * Helper functionality for JOGL.
 * 
 * @author Philipp Jenke
 *
 */
public class JoglHelper {
	/**
	 * Checks if there have been any GL errors. Throws an exception with the
	 * error codes in the message if there have been.
	 * 
	 * @param gl
	 *            Used to make GL calls.
	 * @param sFuncName
	 *            Name of GL function we're checking for errors after.
	 */
	public static boolean hasGLError(GL2 gl, String sFuncName) {

		int iError;
		StringBuilder sb = null;
		do {
			iError = gl.glGetError();
			if (iError != GL2.GL_NO_ERROR) {
				if (sb == null)
					sb = new StringBuilder();
				sb.append(sFuncName + " failed, GL error: 0x"
						+ Integer.toHexString(iError) + "\n");
			}
		} while (iError != GL2.GL_NO_ERROR);

		if (sb != null) {
			Logger.getInstance().error(sb.toString());
			return true;
		}
		return false;
	}
}

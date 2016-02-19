/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.fileio;

import cgresearch.core.math.Vector;

public class ObjMaterial {
	private String name = "";
	private String textureFilename = "";
	private Vector diffuseColor = null;

	public ObjMaterial(final String name, final String textureFilename) {
		this.name = name;
		this.textureFilename = textureFilename;
	}

	public ObjMaterial(final String name) {
		this.name = name;
	}

	/**
	 * @param t
	 */
	public void setTextureFilename(String t) {
		textureFilename = t;
	}

	public String getName() {
		return name;
	}

	public String getTextureFilename() {
		return textureFilename;
	}

	public void setDiffuseColor(Vector color) {
		this.diffuseColor = color;
	}

	public Vector getDiffuseColor() {
		return diffuseColor;
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.fileio;

import cgresearch.core.math.IVector3;

public class ObjMaterial {
	private String name = "";
	private String textureFilename = "";
	private IVector3 diffuseColor = null;

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

	public void setDiffuseColor(IVector3 color) {
		this.diffuseColor = color;
	}

	public IVector3 getDiffuseColor() {
		return diffuseColor;
	}
}

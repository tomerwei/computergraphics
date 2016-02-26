package cgresearch.graphics.material;

import java.awt.image.BufferedImage;

/**
 * Representation of  texture.
 *
 */
public class CgTexture {
	/**
	 * Name of the texture file. Empty string if no texture is associated.
	 */
	private String textureFilename = "";

	/**
	 * Texture object. Null if no texture is loaded. Has priority over texture
	 * file.
	 */
	private BufferedImage textureImage = null;

	/**
	 * Flag indicating if the texture is loaded yet.
	 */
	private boolean isLoaded = false;

	/**
	 * Constructor for texture image from file.
	 */
	public CgTexture(String textureFilename) {
		this.textureFilename = textureFilename;
	}

	/**
	 * Constructor for texture image from buffered image.
	 */
	public CgTexture(BufferedImage textureImage) {
		this.textureImage = textureImage;
	}

	/**
	 * Getter
	 */
	public String getTextureFilename() {
		return textureFilename;
	}

	/**
	 * Setter
	 */
	public void setTextureFilename(String filename) {
		this.textureFilename = filename;
		isLoaded = false;
	}

	/**
	 * Getter
	 */
	public BufferedImage getTextureImage() {
		return textureImage;
	}

	/**
	 * Setter
	 */
	public void setTextureImage(BufferedImage image) {
		this.textureImage = image;
		isLoaded = false;
	}

	/**
	 * Getter.
	 */
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * Setter.
	 */
	public void setIsLoaded(boolean b) {
		isLoaded = b;
	}
}

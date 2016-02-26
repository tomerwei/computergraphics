package cgresearch.graphics.fileio;

/**
 * Represents the structure of the point information in an ascii file. We
 * assume, that we have a list of double values and a separating expression in
 * between. All tokens which cannot be converted into a double are ignored.
 * 
 * @author Philipp Jenke
 *
 */
public class AsciiPointFormat {

	/**
	 * Normal indices
	 */
	private int[] normalIndices = { -1, -1, -1 };

	/**
	 * Position indices
	 */
	private int[] positionIndices = { -1, -1, -1 };

	/**
	 * Color indices
	 */
	private int[] colorIndices = { -1, -1, -1 };

	/**
	 * Scaling factor for the color values;
	 */
	private double colorScale = 1;

	/**
	 * Separating string. Default char: empty space.
	 */
	private String separator = " ";

	/**
	 * Constructor
	 */
	public AsciiPointFormat() {

	}

	/**
	 * Setter for the position indices. -1 indicates that the position is not
	 * contained.
	 */
	public AsciiPointFormat setPosition(int indexX, int indexY, int indexZ) {
		positionIndices[0] = indexX;
		positionIndices[1] = indexY;
		positionIndices[2] = indexZ;
		return this;
	}

	/**
	 * Setter for the normal indices. -1 indicates that the normal is not
	 * contained.
	 */
	public AsciiPointFormat setNormal(int indexX, int indexY, int indexZ) {
		normalIndices[0] = indexX;
		normalIndices[1] = indexY;
		normalIndices[2] = indexZ;
		return this;
	}

	/**
	 * Setter for the color indices. -1 indicates that the color is not
	 * contained.
	 */
	public AsciiPointFormat setColor(int indexX, int indexY, int indexZ) {
		colorIndices[0] = indexX;
		colorIndices[1] = indexY;
		colorIndices[2] = indexZ;
		return this;
	}

	/**
	 * Setter for the scale factor of the color values
	 */
	public AsciiPointFormat setColorScale(double colorScale) {
		this.colorScale = colorScale;
		return this;
	}

	/**
	 * Setter for the separating string
	 */
	public AsciiPointFormat setSeparator(String separator) {
		this.separator = separator;
		return this;
	}

	/**
	 * Getter.
	 */
	public double getColorScale() {
		return colorScale;
	}

	/**
	 * Getter.
	 */
	public int getPositionIndex(int index) {
		return positionIndices[index];
	}

	/**
	 * Getter.
	 */
	public int getNormalIndex(int index) {
		return normalIndices[index];
	}

	/**
	 * Getter.
	 */
	public int getColorIndex(int index) {
		return colorIndices[index];
	}

	/**
	 * Getter.
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Return the number of tokens (identified by the max index) in this format.
	 * Note: required valid format with indices provided starting from 0.
	 */
	public int getNumberOfTokens() {
		int maxIndex = -1;
		for (int i = 0; i < 3; i++) {
			if (colorIndices[i] > maxIndex) {
				maxIndex = colorIndices[i];
			}
			if (normalIndices[i] > maxIndex) {
				maxIndex = normalIndices[i];
			}
			if (positionIndices[i] > maxIndex) {
				maxIndex = positionIndices[i];
			}
		}
		return maxIndex + 1;
	}

	/**
	 * Return the name of the token at the specified index.
	 */
	public String getTokenName(int index) {
		for (int i = 0; i < 3; i++) {
			if (getPositionIndex(i) == index) {
				return "pos" + i;
			} else if (getColorIndex(i) == index) {
				return "col" + i;
			} else if (getNormalIndex(i) == index) {
				return "nor" + i;
			}
		}
		return "undef";
	}
}

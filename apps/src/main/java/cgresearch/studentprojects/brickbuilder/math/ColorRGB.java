/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.math;

import java.util.Arrays;
import java.util.List;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Implementation of the IColorRGB interface.
 *
 * @author Chris Michael Marquardt
 */
public class ColorRGB implements IColorRGB {
	/**
	 * Color array. Int, because byte is signed (-128 <= x <= 127) and short useless.
	 */
	private int[] color;
	
	public ColorRGB(int red, int green, int blue) {
		this.color = new int[] {red % 256, green % 256, blue % 256};
	}
	
	public ColorRGB(int color) {
		this.color = new int[] {(color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff};
	}
	
	public ColorRGB(IVector3 color) {
		this.color = new int[] {(int) color.get(0) % 256, (int) color.get(1) % 256, 
				(int) color.get(2) % 256};
	}
	
	public ColorRGB(byte[] color) {
		this.color = new int[] {color[0] & 0xff, color[1] & 0xff, color[2] & 0xff};
	}
	
	@Override
	public int getRed() {
		return color[0];
	}

	@Override
	public int getGreen() {
		return color[1];
	}

	@Override
	public int getBlue() {
		return color[2];
	}

	@Override
	public int getAsInteger() {
		return (color[0] << 16) + (color[1] << 8) + color[2];
	}

	@Override
	public IVector3 getAsVector() {
		return VectorMatrixFactory.newIVector3(color[0], color[1], color[2]);
	}

	@Override
	public byte[] getAsByteArray() {
		return new byte[] {(byte) color[0], (byte) color[1], (byte) color[2]};
	}

	@Override
	public IColorRGB getNearestColor(List<IColorRGB> list) {
		if (list.size() == 0) return null;		
		int diff = Integer.MAX_VALUE;
		IColorRGB col = null;
		for (IColorRGB c : list) {
			int tdiff = Math.abs(this.getRed() - c.getRed()) + 
					Math.abs(this.getGreen() - c.getGreen()) +
					Math.abs(this.getBlue() - c.getBlue());
			if (tdiff < diff) {
				diff = tdiff;
				col = c;
			}
		}
		return col;
	}
	
	@Override
	public String toString() {
		return "ColorRGB: "+Arrays.toString(color);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(color);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColorRGB other = (ColorRGB) obj;
		if (getAsInteger() != other.getAsInteger())
			return false;
		return true;
	}

}

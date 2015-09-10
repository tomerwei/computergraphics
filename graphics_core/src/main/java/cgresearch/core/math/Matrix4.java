/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Implementation of a matrix in 4-space.
 * 
 * @author Philipp Jenke
 * 
 */
public class Matrix4 implements IMatrix4 {
	/**
	 * Entries of the matrix
	 */
	private final IVector4[] rows = { VectorMatrixFactory.newIVector4(),
			VectorMatrixFactory.newIVector4(),
			VectorMatrixFactory.newIVector4(),
			VectorMatrixFactory.newIVector4() };

	/**
	 * Constructor.
	 */
	public Matrix4() {
	}

	/**
	 * Constructor with initial values.
	 */
	public Matrix4(IVector4 row0, IVector4 row1, IVector4 row2, IVector4 row3) {
		rows[MathHelpers.INDEX_0] = row0;
		rows[MathHelpers.INDEX_1] = row1;
		rows[MathHelpers.INDEX_2] = row2;
		rows[MathHelpers.INDEX_3] = row3;
	}

	@Override
	public void setRow(final int index, IVector4 row) {
		rows[index] = row;
	}

	@Override
	public IVector4 multiply(final IVector4 other) {
		IVector4 result = VectorMatrixFactory.newIVector4();
		for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
			result.set(i, getRow(i).multiply(other));
		}

		return result;
	}

	@Override
	public IVector4 getRow(final int index) {
		return rows[index];
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix4#setRow(int, int, double)
	 */
	@Override
	public void setRow(int rowIndex, int columnIndex, double value) {
		rows[rowIndex].set(columnIndex, value);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix4#get(int, int)
	 */
	@Override
	public double get(int rowIndex, int columnIndex) {
		return rows[rowIndex].get(columnIndex);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix4#toArray()
	 */
	@Override
	public double[] toArray() {
		double[] doubleArray = { get(0, 0), get(0, 1), get(0, 2), get(0, 3),
				get(1, 0), get(1, 1), get(1, 2), get(1, 3), get(2, 0),
				get(2, 1), get(2, 2), get(2, 3), get(3, 0), get(3, 1),
				get(3, 2), get(3, 3) };
		return doubleArray;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix4#getTransposed()
	 */
	@Override
	public IMatrix4 getTransposed() {
		return VectorMatrixFactory.newIMatrix4(get(0, 0), get(1, 0), get(2, 0),
				get(3, 0), get(0, 1), get(1, 1), get(2, 1), get(3, 1),
				get(0, 2), get(1, 2), get(2, 2), get(3, 2), get(0, 3),
				get(1, 3), get(2, 3), get(3, 3));
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix4#data()
	 */
	@Override
	public double[] data() {
		double[] data = { rows[0].get(0), rows[0].get(1), rows[0].get(2),
				rows[0].get(3), rows[1].get(0), rows[1].get(1), rows[1].get(2),
				rows[1].get(3), rows[2].get(0), rows[2].get(1), rows[2].get(2),
				rows[2].get(3), rows[3].get(0), rows[3].get(1), rows[3].get(2),
				rows[3].get(3) };
		return data;
	}

	@Override
	public int hashCode() {
		return rows[0].hashCode() + rows[1].hashCode() + rows[2].hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Matrix4)) {
			return false;
		}
		Matrix4 otherMatrix = (Matrix4) other;
		for (int i = 0; i < 4; i++) {
			if (!rows[i].equals(otherMatrix.rows[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IMatrix4 multiply(IMatrix4 other) {
		IVector4[] a = new IVector4[] { getRow(0), getRow(1), getRow(2),
				getRow(3) };
		IVector4[] b = new IVector4[] { other.getColumn(0), other.getColumn(1),
				other.getColumn(2), other.getColumn(3) };
		IMatrix4 result = VectorMatrixFactory.newIMatrix4();
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 4; column++) {
				result.set(row, column, a[row].multiply(b[column]));
			}
		}
		return result;
	}

	@Override
	public IVector4 getColumn(int i) {
		return VectorMatrixFactory.newIVector4(get(0, i), get(1, i), get(2, i),
				get(3, i));

	}

	@Override
	public void set(int row, int column, double value) {
		rows[row].set(column, value);
	}

	@Override
	public String toString() {
		int precision = 5;
		return String.format("/ %." + precision + "f, %." + precision + "f, %."
				+ precision + "f, %." + precision + "f \\\n| %." + precision
				+ "f, %." + precision + "f, %." + precision + "f, %."
				+ precision + "f |\n| %." + precision + "f, %." + precision
				+ "f, %." + precision + "f, %." + precision + "f |\n\\ %."
				+ precision + "f, %." + precision + "f, %." + precision
				+ "f, %." + precision + "f /", get(0, 0), get(0, 1), get(0, 2),
				get(0, 3), get(1, 0), get(1, 1), get(1, 2), get(1, 3),
				get(2, 0), get(2, 1), get(2, 2), get(2, 3), get(3, 0),
				get(3, 1), get(3, 2), get(3, 3));

	}
};

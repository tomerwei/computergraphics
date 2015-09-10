/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

/**
 * Implementation of a 3-dimensional matrix.
 * 
 * @author Philipp Jenke
 * 
 */
public class Matrix3 implements IMatrix3 {

	/**
	 * Entries of the matrix
	 */
	private final IVector3[] rows = { VectorMatrixFactory.newIVector3(),
			VectorMatrixFactory.newIVector3(),
			VectorMatrixFactory.newIVector3() };

	/**
	 * Constructor.
	 */
	public Matrix3() {
	}

	/**
	 * Constructor with initial values.
	 */
	public Matrix3(double v00, double v01, double v02, double v10, double v11,
			double v12, double v20, double v21, double v22) {
		rows[0] = VectorMatrixFactory.newIVector3(v00, v01, v02);
		rows[1] = VectorMatrixFactory.newIVector3(v10, v11, v12);
		rows[2] = VectorMatrixFactory.newIVector3(v20, v21, v22);
	}

	/**
	 * Constructor with initial values.
	 */
	public Matrix3(IVector3 row1, IVector3 row2, IVector3 row3) {
		rows[MathHelpers.INDEX_0] = row1;
		rows[MathHelpers.INDEX_1] = row2;
		rows[MathHelpers.INDEX_2] = row3;
	}

	@Override
	public void setRow(final int index, IVector3 row) {
		rows[index] = row;
	}

	@Override
	public IVector3 multiply(final IVector3 other) {
		IVector3 result = VectorMatrixFactory.newIVector3();
		for (int i = 0; i < MathHelpers.DIMENSION_3; i++) {
			result.set(i, getRow(i).multiply(other));
		}

		return result;
	}

	@Override
	public IVector3 getRow(final int index) {
		return rows[index];
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix3#data()
	 */
	@Override
	public double[] data() {
		double[] data = { rows[0].get(0), rows[0].get(1), rows[0].get(2),
				rows[1].get(0), rows[1].get(1), rows[1].get(2), rows[2].get(0),
				rows[2].get(1), rows[2].get(2) };
		return data;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix3#makeHomogenious()
	 */
	@Override
	public IMatrix4 makeHomogenious() {
		IVector4 row0 = rows[0].getHomogenious();
		row0.set(3, 0);
		IVector4 row1 = rows[1].getHomogenious();
		row1.set(3, 0);
		IVector4 row2 = rows[2].getHomogenious();
		row2.set(3, 0);
		return VectorMatrixFactory.newIMatrix4(row0, row1, row2,
				VectorMatrixFactory.newIVector4(0, 0, 0, 1));
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix3#getTransposed()
	 */
	@Override
	public IMatrix3 getTransposed() {
		return new Matrix3(get(0, 0), get(1, 0), get(2, 0), get(0, 1),
				get(1, 1), get(2, 1), get(0, 2), get(1, 2), get(2, 2));
	}

	/**
	 * @param Row
	 *            index
	 * @param Colum
	 *            index
	 * @return Value at the given position
	 */
	public double get(int rowIndex, int columnIndex) {
		return rows[rowIndex].get(columnIndex);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Matrix3)) {
			return false;
		}
		Matrix3 otherMatrix = (Matrix3) other;
		for (int i = 0; i < 3; i++) {
			if (!rows[i].equals(otherMatrix.rows[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix3#set(int, int, double)
	 */
	@Override
	public void set(int rowIndex, int columnIndex, double d) {
		rows[rowIndex].set(columnIndex, d);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see edu.haw.cg.math.IMatrix3#add(edu.haw.cg.math.IMatrix3)
	 */
	@Override
	public IMatrix3 add(IMatrix3 other) {
		return VectorMatrixFactory.newIMatrix3(rows[0].add(other.getRow(0)),
				rows[1].add(other.getRow(1)), rows[2].add(other.getRow(2)));
	}

	@Override
	public String toString() {
		int precision = 5;
		return String.format("/ %." + precision + "f, %." + precision + "f, %."
				+ precision + "f \\\n| %." + precision + "f, %." + precision
				+ "f, %." + precision + "f |\n\\ %." + precision + "f, %."
				+ precision + "f, %." + precision + "f /", get(0, 0),
				get(0, 1), get(0, 2), get(1, 0), get(1, 1), get(1, 2),
				get(2, 0), get(2, 1), get(2, 2));

	}

	@Override
	public int hashCode() {
		return rows[0].hashCode() + rows[1].hashCode() + rows[2].hashCode();
	}

	@Override
	public void copy(IMatrix3 other) {
		rows[0].copy(other.getRow(0));
		rows[1].copy(other.getRow(1));
		rows[2].copy(other.getRow(2));
	}

	@Override
	public double[] data4x4() {
		double[] data = { rows[0].get(0), rows[0].get(1), rows[0].get(2), 0,
				rows[1].get(0), rows[1].get(1), rows[1].get(2), 0,
				rows[2].get(0), rows[2].get(1), rows[2].get(2), 0, 0, 0, 0, 1 };
		return data;
	}

	@Override
	public IMatrix3 multiply(IMatrix3 other) {
		IVector3[] a = new IVector3[] { getRow(0), getRow(1), getRow(2) };
		IVector3[] b = new IVector3[] { other.getColumn(0), other.getColumn(1),
				other.getColumn(2) };
		IMatrix3 result = new Matrix3();
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				result.set(row, column, a[row].multiply(b[column]));
			}
		}
		return result;
	}

	@Override
	public IVector3 getColumn(int columnIndex) {
		return VectorMatrixFactory.newIVector3(get(0, columnIndex),
				get(1, columnIndex), get(2, columnIndex));
	}
}

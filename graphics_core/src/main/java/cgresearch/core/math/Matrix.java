package cgresearch.core.math;

public class Matrix implements IMatrix {

	private double[][] values;

	public Matrix(int numberOfRows, int numberOfColumns) {
		if (numberOfColumns < 1 || numberOfColumns < 1) {
			throw new IllegalArgumentException();
		}
		values = new double[numberOfRows][numberOfColumns];
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            Matrix to be cloned.
	 */
	public Matrix(IMatrix other) {
		this(other.getNumberOfRows(), other.getNumberOfColumns());
		for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
				set(rowIndex, columnIndex, other.get(rowIndex, columnIndex));
			}
		}
	}

	@Override
	public int getNumberOfRows() {
		return values.length;
	}

	@Override
	public int getNumberOfColumns() {
		return values[0].length;
	}

	@Override
	public double get(int rowIndex, int columnIndex) {
		return values[rowIndex][columnIndex];
	}

	@Override
	public void set(int rowIndex, int columnIndex, double value) {
		values[rowIndex][columnIndex] = value;
	}

	@Override
	public IVector multiply(IVector other) {
		if (getNumberOfColumns() != other.getDimension()) {
			throw new IllegalArgumentException();
		}
		IVector result = new Vector(getNumberOfRows());
		for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
			double value = 0;
			for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
				value += get(rowIndex, columnIndex) * other.get(columnIndex);
			}
			result.set(rowIndex, value);
		}
		return result;
	}

	@Override
	public IMatrix multiply(IMatrix other) {
		if (getNumberOfColumns() != other.getNumberOfRows()) {
			throw new IllegalArgumentException();
		}
		IMatrix result = new Matrix(getNumberOfRows(), other.getNumberOfColumns());
		for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < other.getNumberOfColumns(); columnIndex++) {
				double value = 0;
				for (int i = 0; i < getNumberOfColumns(); i++) {
					value += get(rowIndex, i) * other.get(i, columnIndex);
				}
				result.set(rowIndex, columnIndex, value);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		String content = "";
		for (int rowIndex = 0; rowIndex < getNumberOfRows(); rowIndex++) {
			for (int columnIndex = 0; columnIndex < getNumberOfColumns(); columnIndex++) {
				content += String.format("%4.3f ", get(rowIndex, columnIndex));
			}
			content += "\n";
		}
		return content;
	}

	// Erweitert von Vitalij Kagaidj
	@Override
	public IMatrix add(IMatrix other) {
		IMatrix result = null;

		if (this.getNumberOfColumns() == other.getNumberOfColumns()
				&& this.getNumberOfRows() == other.getNumberOfRows()) {
			result = new Matrix(getNumberOfRows(), getNumberOfColumns());

			for (int i = 0; i < result.getNumberOfRows(); i++) {
				for (int j = 0; j < result.getNumberOfColumns(); j++) {
					result.set(i, j, this.get(i, j) + other.get(i, j));
				}
			}

		} else {
			System.err.println("Dimensions Conflict!");
		}

		return result;
	}

}

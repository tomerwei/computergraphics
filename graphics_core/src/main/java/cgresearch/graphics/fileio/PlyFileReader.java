/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.fileio;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.assets.CgAssetManager;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Reader for PLY meshes - currently only supports triangle meshes.
 * 
 * @author Philipp Jenke
 * 
 */
public class PlyFileReader {

	/**
	 * Ply header object - read from the PLY file.
	 */
	private PlyHeader header = new PlyHeader();

	/**
	 * Different states used to distinguish the vertex and facet properties.
	 */
	private State readHeaderState;

	/**
	 * Constructor.
	 */
	public PlyFileReader() {
	}

	/**
	 * Parse a PLY file and fill a triangle mesh.
	 * 
	 * @param filename
	 *            Filename of the PLY file.
	 * @return Created mesh.
	 */
	public ITriangleMesh readFile(final String filename) {
		ITriangleMesh mesh = new TriangleMesh();

		// Find absolute filename

		Logger.getInstance().message("Reading PLY file " + filename);

		try {
			InputStream is = CgAssetManager.getInstance().getInputStream(
					filename);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(is);
			header.clear();

			// Read header
			readHeader(in);
			// Read vertices and facets
			readVertices(in, mesh);
			// Read triangles
			readFaces(in, mesh);

			// Finalize mesh
			mesh.computeTriangleNormals();
			mesh.computeVertexNormals();

			// Cleanup
			in.close();
			Logger.getInstance().message(
					"Successfully read PLY file with "
							+ mesh.getNumberOfVertices() + " vertices and "
							+ mesh.getNumberOfTriangles() + " triangles.");

		} catch (Exception e) {
			Logger.getInstance().exception(
					"Failed to read mesh from PLY file: ", e);
		}

		return mesh;
	}

	/**
	 * State of reading the header.
	 */
	private enum State {
		NONE, VERTEX, FACET
	};

	/**
	 * Read the header.
	 */
	private void readHeader(DataInputStream br) throws IOException {
		readHeaderState = State.NONE;

		// Each PLY file starts with a PLY-line.
		String line = readLineIgnoreComment(br);
		if (!line.toUpperCase().equals("PLY")) {
			Logger.getInstance().error(
					"PlyReader: file seems to habe invalid format.");
		}

		while (!line.toUpperCase().contains("END_HEADER")) {
			if (line.toUpperCase().contains("FORMAT")) {
				readFormat(line);
			} else if (line.toUpperCase().contains("ELEMENT")) {
				readHeaderElement(line);
			} else if (line.toUpperCase().contains("PROPERTY")) {
				readHeaderProperty(line);
			}
			line = readLineIgnoreComment(br);
		}
	}

	/**
	 * Read lines until a line which is not a comment is found. Return that
	 * line.
	 */
	private String readLineIgnoreComment(DataInputStream dis)
			throws IOException {
		String line = readLine(dis);
		while (line.toUpperCase().contains("COMMENT")) {
			line = readLine(dis);
		}
		return line;
	}

	/**
	 * Parse a header property line.
	 */
	private void readHeaderProperty(String line) {
		String[] tokens = line.split("\\s+");
		if (readHeaderState == State.VERTEX) {
			header.addVertexPropery(tokens);
		} else if (readHeaderState == State.FACET) {
			header.addFacetProperty(tokens);
		}
	}

	/**
	 * Read a header line with an element.
	 */
	private void readHeaderElement(String line) {
		String[] tokens = line.split("\\s+");
		if (tokens[1].toUpperCase().equals("VERTEX")) {
			header.setNumberOfVertices(Integer.parseInt(tokens[2]));
			readHeaderState = State.VERTEX;
		} else if (tokens[1].toUpperCase().equals("FACE")) {
			header.setNumberOfFacets(Integer.parseInt(tokens[2]));
			readHeaderState = State.FACET;
		}
	}

	/**
	 * Read the format from a string.
	 */
	private void readFormat(String line) {
		PlyHeader.Format format = PlyHeader.Format.UNKNOWN;
		String[] tokens = line.split("\\s+");
		if (tokens.length > 2) {
			if (tokens[1].toUpperCase().equals("ASCII")) {
				format = PlyHeader.Format.ASCII;
			} else if (tokens[1].toUpperCase().equals("BINARY_BIG_ENDIAN")) {
				format = PlyHeader.Format.BINARY_BIG_ENDIAN;
			}

		}
		header.setFormat(format);
	}

	/**
	 * Read a line from the stream.
	 * 
	 * @throws IOException
	 */
	private String readLine(DataInputStream dis) throws IOException {
		String line = "";
		char c = readChar(dis);
		while (c != '\n') {
			line += c;
			c = readChar(dis);
		}
		return line.trim();
	}

	/**
	 * Read a char from the stream.
	 */
	private char readChar(DataInputStream dis) throws IOException {
		char c = (char) (dis.readUnsignedByte());
		return c;
	}

	/**
	 * Read the vertices from the buffered reader to the mesh.
	 */
	private void readFaces(DataInputStream dis, ITriangleMesh mesh)
			throws IOException {
		if (header.getFormat() == PlyHeader.Format.ASCII) {
			readFacetsAscii(dis, mesh);
		} else if (header.getFormat() == PlyHeader.Format.BINARY_BIG_ENDIAN) {
			readFacetsBinary(dis, mesh);
		} else {
			Logger.getInstance().error("PLY: unsupported format.");
		}
	}

	/**
	 * Read the facets in the binary format
	 */
	private void readFacetsBinary(DataInputStream in, ITriangleMesh mesh)
			throws IOException {

		List<Integer> vertexIndices = new ArrayList<Integer>();

		for (int facetIndex = 0; facetIndex < header.getNumberOfFacets(); facetIndex++) {

			for (int propertyIndex = 0; propertyIndex < header
					.getNumberOfFacetProperties(); propertyIndex++) {
				PlyProperty property = header.getFacetProperty(propertyIndex);
				if (property.getName() == PlyProperty.ProperyName.VERTEX_INDICES) {

					int numberOfVertexIndices = -1;
					if (property.getType(1) == PlyProperty.DataType.UCHAR) {
						numberOfVertexIndices = in.readUnsignedByte();
					} else {
						Logger.getInstance()
								.error("Unsupported format for number of facet vertices.");
						return;
					}

					while (vertexIndices.size() < numberOfVertexIndices) {
						vertexIndices.add(-1);
					}

					for (int vertexIndex = 0; vertexIndex < numberOfVertexIndices; vertexIndex++) {
						if (property.getType(2) == PlyProperty.DataType.SHORT) {
							vertexIndices.set(vertexIndex,
									(int) (in.readShort()));
						} else if (property.getType(2) == PlyProperty.DataType.INT
								|| property.getType(2) == PlyProperty.DataType.INT32) {
							vertexIndices.set(vertexIndex, in.readInt());
						} else {
							Logger.getInstance().error(
									"Unsupported vertex index format.");
							return;
						}
					}
					if (numberOfVertexIndices == 3) {
						// Triangle
						Triangle triangle = new Triangle(vertexIndices.get(0),
								vertexIndices.get(1), vertexIndices.get(2));
						mesh.addTriangle(triangle);
					} else if (numberOfVertexIndices == 4) {
						// Quad
						mesh.addTriangle(new Triangle(vertexIndices.get(0),
								vertexIndices.get(1), vertexIndices.get(2)));
						mesh.addTriangle(new Triangle(vertexIndices.get(0),
								vertexIndices.get(2), vertexIndices.get(3)));
					} else {
						Logger.getInstance().error(
								"PlyFileReader: unsupported facet type.");
						return;
					}

				} else {
					// Read all bytes which belong to the property
					for (int i = 0; i < property.getSizeInBytes(); i++) {
						in.readByte();
					}
				}
			}
		}
	}

	/**
	 * Read the facets in the ascii format
	 */
	private boolean readFacetsAscii(DataInputStream dis, ITriangleMesh mesh)
			throws IOException {
		for (int i = 0; i < header.getNumberOfFacets(); i++) {
			String line = readLineIgnoreComment(dis);
			String[] tokens = line.split("\\s+");
			if (tokens.length != 4) {
				return false;
			}
			Triangle triangle = new Triangle(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
			mesh.addTriangle(triangle);
		}
		return true;
	}

	/**
	 * Read the vertices from the buffered reader to the mesh.
	 */
	private void readVertices(DataInputStream in, ITriangleMesh mesh)
			throws IOException {
		if (header.getFormat() == PlyHeader.Format.ASCII) {
			readVerticesAscii(in, mesh);
		} else if (header.getFormat() == PlyHeader.Format.BINARY_BIG_ENDIAN) {
			readVerticesBinary(in, mesh);
		} else {
			Logger.getInstance().error("PLY file: unsupported format");
		}

	}

	/**
	 * Read the vertices in BINARY_BIG_ENDIAN format.
	 */
	private void readVerticesBinary(DataInputStream in, ITriangleMesh mesh)
			throws IOException {
		for (int vertexIndex = 0; vertexIndex < header.getNumberOfVertices(); vertexIndex++) {
			float x = -1, y = -1, z = -1;
			for (int propertyIndex = 0; propertyIndex < header
					.getNumberOfVertexProperties(); propertyIndex++) {
				PlyProperty property = header.getVertexProperty(propertyIndex);
				if (property.getName() == PlyProperty.ProperyName.X) {
					x = in.readFloat();
				} else if (property.getName() == PlyProperty.ProperyName.Y) {
					y = in.readFloat();
				} else if (property.getName() == PlyProperty.ProperyName.Z) {
					z = in.readFloat();
				} else {
					// Read all bytes which belong to the property
					for (int i = 0; i < property.getSizeInBytes(); i++) {
						in.read();
					}
				}
			}
			Vertex vertex = new Vertex(VectorMatrixFactory.newVector(x, y, z));
			mesh.addVertex(vertex);
		}
	}

	/**
	 * Read the vertices in ASCII format.
	 */
	private void readVerticesAscii(DataInputStream dis, ITriangleMesh mesh)
			throws IOException {
		for (int i = 0; i < header.getNumberOfVertices(); i++) {
			String line = readLineIgnoreComment(dis);
			String[] tokens = line.split("\\s+");

			// Read position
			Vertex vertex = readPosition(tokens);

			// Read normal
			if (header.hasVertexNormal()) {
				Vector normal = readNormal(tokens);
				if (normal != null) {
					vertex.setNormal(normal);
				}
			}
			mesh.addVertex(vertex);
		}
	}

	/**
	 * Read a normal from a vertex line. Return null on error.
	 */
	private Vector readNormal(String[] tokens) {
		int xIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.NX);
		int yIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.NY);
		int zIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.NZ);
		if (xIndex >= 0 && yIndex >= 0 && zIndex >= 0 && xIndex < tokens.length
				&& yIndex < tokens.length && zIndex < tokens.length) {
			Vector normal = VectorMatrixFactory.newVector(
					Double.parseDouble(tokens[xIndex]),
					Double.parseDouble(tokens[yIndex]),
					Double.parseDouble(tokens[zIndex]));
			return normal;
		}
		return null;
	}

	/**
	 * Parse the position coordinates, return a vertex object. Return null on
	 * error.
	 */
	private Vertex readPosition(String[] tokens) {
		int xIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.X);
		int yIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.Y);
		int zIndex = header
				.getVertexPropertyPosition(PlyProperty.ProperyName.Z);

		// Read position
		if (xIndex >= 0 && yIndex >= 0 && zIndex >= 0 && xIndex < tokens.length
				&& yIndex < tokens.length && zIndex < tokens.length) {
			Vector position = VectorMatrixFactory.newVector(
					Double.parseDouble(tokens[xIndex]),
					Double.parseDouble(tokens[yIndex]),
					Double.parseDouble(tokens[zIndex]));
			return new Vertex(position);
		}
		return null;
	}
}

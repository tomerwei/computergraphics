package cgresearch.graphics.fileio;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import cgresearch.core.assets.CgAssetManager;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Reader for the STL triangle mesh format.
 * 
 * @author Philipp Jenke
 * 
 *         Format: UINT8[80] - Dateikopf (Header) UINT32 - Anzahl der Dreiecke
 *         foreach triangle REAL32[3] - Normalenvektor REAL32[3] - Vertex 1
 *         REAL32[3] - Vertex 2 REAL32[3] - Vertex 3 UINT16 - Attribute byte
 *         count end
 *
 */
public class StlFileReader {

  public ITriangleMesh read(String filename) {

    Logger.getInstance().message("Reading STL file " + filename + " ...");

    try {
      InputStream is = CgAssetManager.getInstance().getInputStream(filename);
      if (is == null) {
        Logger.getInstance().error("Failed to create input stream for file " + filename);
        return null;
      }

      // Get the object of DataInputStream
      DataInputStream dataInputStream = new DataInputStream(is);

      // Read header
      readHeader(dataInputStream);
      // Read number of triangles
      long numberOfTriangles = readNumberOfTriangles(dataInputStream);
      // Read triangley
      ITriangleMesh mesh = readTriangles(dataInputStream, numberOfTriangles);

      mesh.computeTriangleNormals();
      return mesh;
    } catch (Exception e) {
      Logger.getInstance().error("Failed to import STL file " + filename);
    }

    return null;
  }

  /**
   * Read information for a triangle.
   * 
   * // REAL32[3] - Normalenvektor // REAL32[3] - Vertex 1 // REAL32[3] - Vertex
   * 2 // REAL32[3] - Vertex 3 // UINT16 - Attribute byte count
   */
  private ITriangleMesh readTriangles(DataInputStream dataInputStream, long numberOfTriangles) {
    ITriangleMesh mesh = new TriangleMesh();
    for (int i = 0; i < numberOfTriangles; i++) {
      readVector(dataInputStream); // Normal - discard
      Vector a = readVector(dataInputStream);
      Vector b = readVector(dataInputStream);
      Vector c = readVector(dataInputStream);
      int aIndex = mesh.addVertex(new Vertex(a));
      int bIndex = mesh.addVertex(new Vertex(b));
      int cIndex = mesh.addVertex(new Vertex(c));
      mesh.addTriangle(aIndex, bIndex, cIndex);
      readAttributeByteContent(dataInputStream); // Discard
    }
    return mesh;
  }

  private void readAttributeByteContent(DataInputStream dataInputStream) {
    try {
      dataInputStream.readByte();
      dataInputStream.readByte();
    } catch (IOException e) {
      Logger.getInstance().error("Error reading attribute byte content.");
    }
  }

  private Vector readVector(DataInputStream dataInputStream) {
    try {
      double x = dataInputStream.readFloat();
      double y = dataInputStream.readFloat();
      double z = dataInputStream.readFloat();
      return VectorFactory.createVector3(x, y, z);
    } catch (IOException e) {
      Logger.getInstance().error("Failed to read vector.");
    }
    return null;
  }

  private long readNumberOfTriangles(DataInputStream dataInputStream) {
    try {
      // return dataInputStream.readInt();

      long value = ((dataInputStream.readByte() & 0xFF) << 0) | ((dataInputStream.readByte() & 0xFF) << 8)
          | ((dataInputStream.readByte() & 0xFF) << 16) | ((dataInputStream.readByte() & 0xFF) << 24);
      return value;

    } catch (IOException e) {
      Logger.getInstance().error("Error reading number of triangles.");
    }
    return -1;
  }

  /**
   * Read header, discard information.
   */
  private void readHeader(DataInputStream dataInputStream) {
    try {
      for (int i = 0; i < 80; i++) {
        dataInputStream.readByte();
      }
    } catch (IOException e) {
      Logger.getInstance().error("Error reading header.");
    }
  }
}

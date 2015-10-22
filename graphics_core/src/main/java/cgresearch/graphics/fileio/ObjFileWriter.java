/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;

/**
 * @author Philipp Jenke
 * 
 */
public class ObjFileWriter {

  /**
   * Name of the material in the material library file.
   */
  private final String materialName = "default_material";

  /**
   * Constructor.
   */
  public ObjFileWriter() {
  }

  /**
   * Wrote the mesh as Wavefront OBJ to file.
   */
  public void writeToFile(String filename, ITriangleMesh mesh) {
    BufferedWriter writer = null;
    String materialLibFilename = "";
    try {
      writer = new BufferedWriter(new FileWriter(filename));
      writeHeader(writer, mesh);
      materialLibFilename = createMaterialLibFilename(filename, mesh);
      writeMaterialFile(writer, materialLibFilename);
      writeUseMaterial(writer);
      writeVertices(writer, mesh);
      writeTextureCoordinates(writer, mesh);
      writeFacets(writer, mesh);
      writer.close();
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write OBJ file", e);
    }
    Logger.getInstance()
        .message("Successfully wrote OBJ file " + filename + " with material lib file " + materialLibFilename + ".");

  }

  /**
   * @param writer
   */
  private void writeUseMaterial(BufferedWriter writer) {
    try {
      writer.write("usemtl " + materialName + "\n");
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write OBJ file", e);
    }

  }

  /**
   * Create a material lib for the mesh (in the same directory).
   */
  private String createMaterialLibFilename(String filename, ITriangleMesh mesh) {
    String directory = new File(filename).getParent();
    String fileNameWithoutPath = new File(filename).getName();
    String filenameWithoutExtension = fileNameWithoutPath.substring(0, fileNameWithoutPath.lastIndexOf("."));
    String materialLibFilename = filenameWithoutExtension + ".mtl";

    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(directory + "/" + materialLibFilename));
      writer.write("newmtl " + materialName + "\n");
      if (mesh.getMaterial().getTextureId() != null) {
        CgTexture texture = ResourceManager.getTextureManagerInstance().getResource(mesh.getMaterial().getTextureId());
        writer.write("Kd " + texture.getTextureFilename());
      } else {
        // TODO: Currently no color is exported.
      }
      writer.close();
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write OBJ material library file", e);
    }

    return materialLibFilename;
  }

  /**
   * Write the material lib to the OBJ file.
   */
  private void writeMaterialFile(BufferedWriter writer, String materialLibFilename) {
    try {
      writer.write("mtllib " + materialLibFilename + "\n");
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write OBJ file", e);
    }
  }

  /**
   * Write the facets to file.
   */
  private void writeTextureCoordinates(BufferedWriter writer, ITriangleMesh mesh) {
    for (int texCoordIndex = 0; texCoordIndex < mesh.getNumberOfTextureCoordinates(); texCoordIndex++) {
      IVector3 texCoord = mesh.getTextureCoordinate(texCoordIndex);
      try {
        writer.write("vt " + texCoord.get(0) + " " + texCoord.get(1) + "\n");
      } catch (IOException e) {
        Logger.getInstance().exception("Failed to write OBJ file", e);
      }
    }

  }

  /**
   * Write the facets to file.
   */
  private void writeFacets(BufferedWriter writer, ITriangleMesh mesh) {
    for (int facetIndex = 0; facetIndex < mesh.getNumberOfTriangles(); facetIndex++) {
      ITriangle triangle = mesh.getTriangle(facetIndex);
      try {
        String line = "f ";
        for (int i = 0; i < 3; i++) {
          line += (triangle.get(i) + 1);
          line += (triangle.getTextureCoordinate(i) >= 0) ? "/" + (triangle.getTextureCoordinate(i) + 1) : "";
          line += " ";
        }
        writer.write(line + "\n");
      } catch (IOException e) {
        Logger.getInstance().exception("Failed to write OBJ file", e);
      }
    }

  }

  /**
   * Write the vertices to file.
   */
  private void writeVertices(BufferedWriter writer, ITriangleMesh mesh) {

    for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
      IVertex vertex = mesh.getVertex(vertexIndex);
      try {
        writer.write("v " + vertex.getPosition().get(0) + " " + vertex.getPosition().get(1) + " "
            + vertex.getPosition().get(2) + "\n");
      } catch (IOException e) {
        Logger.getInstance().exception("Failed to write OBJ file", e);
      }
    }
  }

  /**
   * Write the file header
   */
  private void writeHeader(BufferedWriter writer, ITriangleMesh mesh) {
    try {
      writer.write("# OBJ file writer, Computer Graphics Lab\n");
      writer.write("# Prof. Philipp Jenke, Hochschule für Angewandte Wissenschaften, Hamburg\n");
      Calendar calendar = Calendar.getInstance();
      writer.write("# " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "."
          + calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
          + calendar.get(Calendar.MINUTE) + "\n");
      writer.write("# #vertices:" + mesh.getNumberOfVertices() + ", #texture coordinates: "
          + mesh.getNumberOfTextureCoordinates() + ", #triangles: " + mesh.getNumberOfTriangles() + "\n");
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write OBJ file", e);
    }

  }
}

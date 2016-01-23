/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.fileio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import cgresearch.core.assets.CgAssetManager;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;

/**
 * This class provides functionality to read 3D meshes from a OBJ files.
 * Attention: the reader is not complete - it only reads vertices and triangles
 * 
 * @author Philipp Jenke
 * 
 */
public class ObjFileReader {

  /**
   * Container for the read triangle meshes
   */
  private List<ITriangleMesh> meshes = new ArrayList<ITriangleMesh>();

  /**
   * Reference to the currently processed mesh.
   */
  private ITriangleMesh currentMesh = null;

  /**
   * Information about the materials in the OBJ file.
   */
  private ObjMaterials materials = new ObjMaterials();

  /**
   * Mapping between the index in the OBJ file and the index in the vertex list
   * in the current triangle mesh. Newly created for each mesh in the OBJ file.
   */
  private Map<Integer, Integer> vertexIndexMapping = null;

  /**
   * Mapping between the index in the OBJ file and the index in the texture
   * coordinates list in the current triangle mesh. Newly created for each mesh
   * in the OBJ file.
   */
  private Map<Integer, Integer> textureCoordIndexMapping = null;

  /**
   * List of all vertices read from the OBJ file.
   */
  private List<Vertex> vertices = new ArrayList<Vertex>();

  /**
   * List of all texture coordinates read from the OBJ file.
   */
  private List<IVector3> textureCoordinates = new ArrayList<IVector3>();

  /**
   * Remember the directory of the input filename in order to read the material
   * library.
   */
  private String directory = "";

  /**
   * Constructor.
   */
  public ObjFileReader() {
  }

  /**
   * Read all meshes from an OBJ file. A new mesh is returned (tbd!) for each
   * material.
   * 
   * @param filename
   *          Filename of the OBJ file.
   * @return CgNode containing the meshes.
   */
  public List<ITriangleMesh> readFile(final String filename) {

    meshes.clear();
    vertexIndexMapping = null;
    textureCoordIndexMapping = null;
    vertices.clear();
    textureCoordinates.clear();

    Logger.getInstance().debug("Reading OBJ file " + filename);

    // Setup
    currentMesh = new TriangleMesh();
    vertexIndexMapping = new HashMap<Integer, Integer>();
    textureCoordIndexMapping = new HashMap<Integer, Integer>();
    directory = new File(filename).getParent() + "/";

    // Start parsing
    String strLine = "";
    try {
      InputStream is = CgAssetManager.getInstance().getInputStream(filename);
      if (is == null) {
        Logger.getInstance().error("Failed to create input stream for file " + filename);
        return meshes;
      }

      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(is);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        // Print the content on the console
        parseLine(strLine, directory);
      }
      // Close the input stream
      in.close();

      if (currentMesh.getNumberOfTriangles() > 0) {
        meshes.add(currentMesh);
      }
    } catch (Exception e) {
      Logger.getInstance().exception("Failed to read mesh from OBJ file", e);
    }

    for (int i = 0; i < meshes.size(); i++) {
      Logger.getInstance()
          .debug("Read " + meshes.get(i).getNumberOfVertices() + " vertices, " + meshes.get(i).getNumberOfTriangles()
              + " triangles and " + meshes.get(i).getNumberOfTextureCoordinates() + " texture coordinates.");

      meshes.get(i).computeTriangleNormals();
      meshes.get(i).computeVertexNormals();

    }

    return meshes;
  }

  /**
   * Parse a line and add the read information to the mesh.
   * 
   * @param strLine
   * @param targetMesh
   */
  private void parseLine(String strLine, final String directory) {
    String line = trim(strLine);
    String operator = getOperator(line);
    if (operator.equals("v")) {
      parseVertex(line);
    } else if (operator.equals("f")) {
      parseFace(line);
    } else if (operator.equals("vt")) {
      parseTextureCoordinate(line);
    } else if (operator.equals("mtllib")) {
      parseMaterialFile(line);
    } else if (operator.equals("usemtl")) {
      parseUseMaterial(line);
    }
  }

  /**
   * Parse a command to use a material. Set the material for the current mesh.
   * 
   * @param line
   *          Command line.
   */
  private void parseUseMaterial(String line) {
    String[] components = splitEmptySpace(line);
    if (components.length == 2) {
      String materialName = components[1];

      // Finalize current mesh.
      if (currentMesh.getNumberOfTriangles() > 0) {
        meshes.add(currentMesh);
        vertexIndexMapping = new HashMap<Integer, Integer>();
        textureCoordIndexMapping = new HashMap<Integer, Integer>();
        currentMesh = new TriangleMesh();
      }

      // Start new mesh.

      ObjMaterial mat = materials.getMaterial(materialName);
      if (mat != null) {

        // Texture
        String textureFilename = mat.getTextureFilename();
        if (textureFilename.length() > 0) {
          CgTexture texture = new CgTexture(directory + textureFilename);
          String id = ResourceManager.getTextureManagerInstance().generateId();
          ResourceManager.getTextureManagerInstance().addResource(id, texture);
          currentMesh.getMaterial().setTextureId(id);
        } else {
          currentMesh.getMaterial().setNoTexture();
        }

        // Color
        IVector3 diffuseColor = mat.getDiffuseColor();
        if (diffuseColor != null) {
          currentMesh.getMaterial().setReflectionDiffuse(diffuseColor);
        }
      }
    }
  }

  /**
   * Read a material library.
   * 
   * @param line
   *          Command line containing the library file in the OBJ file.
   */
  private void parseMaterialFile(String line) {
    ObjMaterialIO materialIO = new ObjMaterialIO();
    String[] components = splitEmptySpace(line);
    if (components.length == 2) {
      materials = materialIO.readMaterialFile(directory + "/" + components[1]);
    }
  }

  private static String trim(String strLine) {
    String line = strLine.trim();

    line = line.replaceAll("  ", " ");
    int l = line.length();

    while (l < line.length()) {
      l = line.length();
      line = line.replaceAll("  ", " ");
    }
    return line;
  }

  /**
   * Parse a line which represents a texture coordinate.
   * 
   * @param strLine
   * @param targetMesh
   */
  private void parseTextureCoordinate(String strLine) {

    String line = trim(strLine);
    String[] allCoords = line.split(" ");

    float u = 0;
    float v = 0;
    if (allCoords.length >= 3) {
      u = getFloatValue(allCoords[1]);
      v = getFloatValue(allCoords[2]);
    }

    textureCoordinates.add(VectorMatrixFactory.newIVector3(u, v, 0));
  }

  private float getFloatValue(String string) {
    if (string.length() == 0) {
      return 0;
    }
    return Float.valueOf(string);
  }

  /**
   * Parse a line which represents a face.
   * 
   * @param strLine
   * @param targetMesh
   */
  private void parseFace(String strLine) {
    String[] allCoords = splitEmptySpace(strLine);

    int[] vertexIndices = { -1, -1, -1, -1 };
    int[] texCoordIndices = { -1, -1, -1, -1 };

    if (allCoords.length == 4) {
      // Triangle mesh
      for (int i = 0; i < 3; i++) {
        String coordinateString = allCoords[i + 1];
        vertexIndices[i] = getFirstEntryForFaceCoordinate(coordinateString) - 1;
        texCoordIndices[i] = getSecondEntryForFaceCoordinate(coordinateString) - 1;

        // Check if the vertex is already in the current mesh
        if (vertexIndexMapping.get(vertexIndices[i]) == null) {
          vertexIndexMapping.put(vertexIndices[i], currentMesh.addVertex(vertices.get(vertexIndices[i])));
        }

        // Check if the texture coordinate is already in the current
        // mesh
        if (textureCoordIndexMapping.get(texCoordIndices[i]) == null) {
          if (textureCoordinates.size() > 0 && texCoordIndices[i] >= 0) {
            textureCoordIndexMapping.put(texCoordIndices[i],
                currentMesh.addTextureCoordinate(textureCoordinates.get(texCoordIndices[i])));
          } else {
            textureCoordIndexMapping.put(texCoordIndices[i], -1);
          }
        }
      }
      currentMesh
          .addTriangle(new Triangle(vertexIndexMapping.get(vertexIndices[0]), vertexIndexMapping.get(vertexIndices[1]),
              vertexIndexMapping.get(vertexIndices[2]), textureCoordIndexMapping.get(texCoordIndices[0]),
              textureCoordIndexMapping.get(texCoordIndices[1]), textureCoordIndexMapping.get(texCoordIndices[2])));

    } else if (allCoords.length == 5) {
      // Quad meshes -> converted to triangle meshes

      for (int i = 0; i < 4; i++) {
        String coordinateString = allCoords[i + 1];
        vertexIndices[i] = getFirstEntryForFaceCoordinate(coordinateString) - 1;
        texCoordIndices[i] = getSecondEntryForFaceCoordinate(coordinateString) - 1;

        // Check if the vertex is already in the current mesh
        if (vertexIndexMapping.get(vertexIndices[i]) == null) {
          vertexIndexMapping.put(vertexIndices[i], currentMesh.addVertex(vertices.get(vertexIndices[i])));
        }

        // Check if the texture coordinate is already in the current
        // mesh
        if (textureCoordIndexMapping.get(texCoordIndices[i]) == null) {
          if (textureCoordinates.size() > 0 && texCoordIndices[i] >= 0) {
            textureCoordIndexMapping.put(texCoordIndices[i],
                currentMesh.addTextureCoordinate(textureCoordinates.get(texCoordIndices[i])));
          } else {
            textureCoordIndexMapping.put(texCoordIndices[i], -1);
          }
        }
      }
      currentMesh
          .addTriangle(new Triangle(vertexIndexMapping.get(vertexIndices[0]), vertexIndexMapping.get(vertexIndices[1]),
              vertexIndexMapping.get(vertexIndices[2]), textureCoordIndexMapping.get(texCoordIndices[0]),
              textureCoordIndexMapping.get(texCoordIndices[1]), textureCoordIndexMapping.get(texCoordIndices[2])));
      currentMesh
          .addTriangle(new Triangle(vertexIndexMapping.get(vertexIndices[0]), vertexIndexMapping.get(vertexIndices[2]),
              vertexIndexMapping.get(vertexIndices[3]), textureCoordIndexMapping.get(texCoordIndices[0]),
              textureCoordIndexMapping.get(texCoordIndices[2]), textureCoordIndexMapping.get(texCoordIndices[3])));
    }
  }

  private int getSecondEntryForFaceCoordinate(String coordinateString) {
    String coord = coordinateString;
    int indexOfSlash = coord.indexOf('/');
    if (indexOfSlash < 0) {
      return getFirstEntryForFaceCoordinate(coord);
    }

    coord = coord.substring(indexOfSlash + 1);
    return getFirstEntryForFaceCoordinate(coord);
  }

  /**
   * Return the first parameter in a slash-separated list: "1/2/3" -> "1"
   * 
   * @param coordinateString
   * @return
   */
  private int getFirstEntryForFaceCoordinate(String coordinateString) {
    String[] components = splitEmptySlash(coordinateString);
    if (components.length > 0) {
      if (components[0].length() == 0) {
        return -1;
      }
      return Integer.parseInt(components[0]);
    } else {
      return -1;
    }
  }

  /**
   * Parse a line which represents a vertex.
   * 
   * @param strLine
   * @param targetMesh
   */
  private void parseVertex(String strLine) {
    String[] components = splitEmptySpace(strLine);

    if (components.length == 4) {
      float x = Float.parseFloat(components[1]);
      float y = Float.parseFloat(components[2]);
      float z = Float.parseFloat(components[3]);
      vertices.add(new Vertex(VectorMatrixFactory.newIVector3(x, y, z), VectorMatrixFactory.newIVector3(1, 0, 0)));
    }
  }

  /**
   * Return a list of components in the line, split along the empty spaces.
   * 
   * @param line
   * @return
   */
  private static String[] splitEmptySpace(String line) {
    return line.trim().split("\\s+");
  }

  /**
   * Return a list of components in the line, split along the slashes ("/")
   * 
   * @param line
   * @return
   */
  private String[] splitEmptySlash(String line) {
    return line.trim().split("/");
  }

  /**
   * Extract the operator char from a line.
   * 
   * @param strLine
   * @return String representing the operator
   */
  public static String getOperator(String strLine) {
    String[] components = splitEmptySpace(strLine);
    if (components.length > 0) {
      return components[0];
    } else {
      return "";
    }
  }
}

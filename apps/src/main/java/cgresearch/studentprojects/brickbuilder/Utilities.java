package cgresearch.studentprojects.brickbuilder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;

public class Utilities {
  public static final int CELLSIZE = 20;
  public static final int MAXCOLORS = 250;

  private Utilities() {
  }

  /**
   * Creates a texture from the color array and adds it with a unique id to the
   * resource manager.
   * 
   * @param colors
   * @return the unique id of the texture
   */
  public static String createTextureFromColors(List<IColorRGB> colors) {
    BufferedImage tex = new BufferedImage((colors.size() < MAXCOLORS ? colors.size() : MAXCOLORS) * CELLSIZE,
        ((colors.size() < MAXCOLORS ? 0 : colors.size() / MAXCOLORS) + 1) * CELLSIZE, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < colors.size(); x++)
      drawQuad(tex, (x % MAXCOLORS) * CELLSIZE, (x / MAXCOLORS) * CELLSIZE, CELLSIZE, colors.get(x).getAsInteger());
    String id = ResourceManager.getTextureManagerInstance().generateId();
    ResourceManager.getTextureManagerInstance().addResource(id, new CgTexture(tex));
    return id;
  }

  /**
   * Draws a box with a 1pixel black border and fills it with the color.
   * 
   * @param tex
   *          texture
   * @param startX
   *          x position
   * @param startY
   *          y position
   * @param size
   *          box size
   * @param color
   *          color
   */
  public static void drawQuad(BufferedImage tex, int startX, int startY, int size, int color) {
    // draw border
    for (int x = startX; x < startX + size; x++) {
      tex.setRGB(x, startY, 0); // schwarz = 0
      tex.setRGB(x, startY + size - 1, 0);
    }
    for (int y = startY; y < startY + size; y++) {
      tex.setRGB(startX, y, 0);
      tex.setRGB(startX + size - 1, y, 0);
    }

    // fill rest
    for (int y = startY + 1; y < startY + size - 1; y++) {
      for (int x = startX + 1; x < startX + size - 1; x++) {
        tex.setRGB(x, y, color);
      }
    }
  }

  /**
   * Adds a box with the given dimensions at the given position to the mesh.
   * 
   * @param mesh
   *          source mesh
   * @param dim
   *          dimensions
   * @param pos
   *          position
   * @param colorX
   *          color x
   * @param colorY
   *          color y
   * @param sizeX
   *          width of texture
   * @param sizeY
   *          height of texture
   * @param cellSize
   *          size of the box in the texture
   */
  public static void addBox(ITriangleMesh mesh, Vector dim, Vector pos, int colorX, int colorY, int sizeX,
      int sizeY, int cellSize) {
    // create cube and alter
    ITriangleMesh cube = Utilities.createCube(colorX, colorY, sizeX, sizeY, cellSize);
    TriangleMeshTransformation.scale(cube, dim);
    TriangleMeshTransformation.translate(cube, pos);

    // copy mesh
    Map<Integer, Integer> vertex2vertex = new HashMap<Integer, Integer>();
    for (int i = 0; i < cube.getNumberOfVertices(); i++) {
      int v = mesh.addVertex(cube.getVertex(i));
      vertex2vertex.put(i, v);
    }

    Map<Integer, Integer> texCoord2texCoord = new HashMap<Integer, Integer>();
    for (int i = 0; i < cube.getNumberOfTextureCoordinates(); i++) {
      int v = mesh.addTextureCoordinate(cube.getTextureCoordinate(i));
      texCoord2texCoord.put(i, v);
    }

    for (int i = 0; i < cube.getNumberOfTriangles(); i++) {
      ITriangle t = cube.getTriangle(i);
      ITriangle u = new Triangle(vertex2vertex.get(t.getA()), vertex2vertex.get(t.getB()), vertex2vertex.get(t.getC()),
          texCoord2texCoord.get(t.getTextureCoordinate(0)), texCoord2texCoord.get(t.getTextureCoordinate(1)),
          texCoord2texCoord.get(t.getTextureCoordinate(2)));
      mesh.addTriangle(u);
    }
  }

  /**
   * Modified TriangleMeshFactory.createCube() for adding texture coordinates.
   * and using a specific texture.
   * 
   * @return
   */
  public static ITriangleMesh createCube(int colorX, int colorY, int sizeX, int sizeY, int cellSize) {
    ITriangleMesh mesh = new TriangleMesh();
    mesh.clear();

    int c00 = mesh.addTextureCoordinate(
        VectorMatrixFactory.newVector(colorX * cellSize * 1.0 / sizeX, colorY * cellSize * 1.0 / sizeY, 0)); // up
                                                                                                               // left
    int c01 = mesh.addTextureCoordinate(
        VectorMatrixFactory.newVector(colorX * cellSize * 1.0 / sizeX, (colorY + 1) * cellSize * 1.0 / sizeY, 0)); // down
                                                                                                                     // left
    int c10 = mesh.addTextureCoordinate(
        VectorMatrixFactory.newVector((colorX + 1) * cellSize * 1.0 / sizeX, colorY * cellSize * 1.0 / sizeY, 0)); // up
                                                                                                                     // right
    int c11 = mesh.addTextureCoordinate(VectorMatrixFactory.newVector((colorX + 1) * cellSize * 1.0 / sizeX,
        (colorY + 1) * cellSize * 1.0 / sizeY, 0)); // down right

    double d = 0.5;
    int v000 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(-d, -d, -d)));
    int v010 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(-d, d, -d)));
    int v110 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(d, d, -d)));
    int v100 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(d, -d, -d)));
    int v001 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(-d, -d, d)));
    int v011 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(-d, d, d)));
    int v111 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(d, d, d)));
    int v101 = mesh.addVertex(new Vertex(VectorMatrixFactory.newVector(d, -d, d)));

    // front
    mesh.addTriangle(new Triangle(v000, v100, v110, c01, c11, c10));
    mesh.addTriangle(new Triangle(v000, v110, v010, c01, c10, c00));
    // right
    mesh.addTriangle(new Triangle(v100, v101, v111, c01, c11, c10));
    mesh.addTriangle(new Triangle(v100, v111, v110, c01, c10, c00));
    // back
    mesh.addTriangle(new Triangle(v101, v001, v011, c01, c11, c10));
    mesh.addTriangle(new Triangle(v101, v011, v111, c01, c10, c00));
    // left
    mesh.addTriangle(new Triangle(v001, v000, v010, c01, c11, c10));
    mesh.addTriangle(new Triangle(v001, v010, v011, c01, c10, c00));
    // top
    mesh.addTriangle(new Triangle(v010, v110, v111, c01, c11, c10));
    mesh.addTriangle(new Triangle(v010, v111, v011, c01, c10, c00));
    // bottom
    mesh.addTriangle(new Triangle(v000, v100, v101, c01, c11, c10));
    mesh.addTriangle(new Triangle(v000, v101, v001, c01, c10, c00));

    mesh.computeTriangleNormals();
    // mesh.getMaterial().setRenderMode(Material.RenderMode.FLAT_SHADING);

    return mesh;
  }
}

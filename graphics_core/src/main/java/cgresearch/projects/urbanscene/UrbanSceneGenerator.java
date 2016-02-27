package cgresearch.projects.urbanscene;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Generator for urban scenes consisting of houses and streets.
 * 
 * @author Philipp Jenke
 */
public class UrbanSceneGenerator {
  /**
   * Width of the streets
   */
  private static final double STREET_WIDTH = 4;
  /**
   * Width of the properties
   */
  private static final double PROPERTY_WIDTH = 20;

  /**
   * List of triangle meshes containsing house models.
   */
  private List<ITriangleMesh> houseModels = new ArrayList<ITriangleMesh>();

  public UrbanSceneGenerator() {
    createHousModelList();
  }

  /**
   * Create a list of triangle mesh house models.
   */
  private void createHousModelList() {
    houseModels.add(loadScetchUp("sketchup/haus01.obj"));
    houseModels.add(loadScetchUp("sketchup/haus02.obj"));
    houseModels.add(loadScetchUp("sketchup/haus03.obj"));
    houseModels.add(loadScetchUp("sketchup/haus04.obj"));
  }

  /**
   * Load an OBJ house model mesh.
   */
  private ITriangleMesh loadScetchUp(String objFilename) {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return null;
    }
    ITriangleMesh mesh = new TriangleMesh();
    for (ITriangleMesh meshComponent : meshes) {
      mesh.unite(meshComponent);
    }
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;
  }

  /**
   * Create an urban scene
   * 
   * @param blocksInX
   *          Number of of blocks (with 4 houses) in x-direction.
   * @param blocksInY
   *          Number of blocks (with 4 houses) in y-direction.
   * @return
   */
  public CgNode buildScene(int blocksInX, int blocksInY) {
    CgNode sceneNode = new CgNode(null, "Scene");
    for (int i = 0; i < blocksInX; i++) {
      for (int j = 0; j < blocksInY; j++) {
        sceneNode.addChild(buildBlock(i, j));
      }
    }
    return sceneNode;
  }

  /**
   * Build a block with four buildings.
   * 
   * @param i
   *          x-index of the block.
   * @param j
   *          y-index of the block.
   * @return CgNode containing the block.
   */
  private CgNode buildBlock(int xIndex, int yIndex) {
    CgNode blockNode = new CgNode(null, "Block " + xIndex + ", " + yIndex);
    double blockWidth = STREET_WIDTH + 2 * PROPERTY_WIDTH;
    double blockHeight = blockWidth;
    Vector origin = VectorFactory.createVector3(xIndex * blockWidth, 0, yIndex * blockHeight);
    ITriangleMesh ground = getGroundPlane(origin, blockWidth, blockHeight);
    CgNode groundNode = new CgNode(ground, "Ground");
    blockNode.addChild(groundNode);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        Vector houseOrigin = origin.add(VectorFactory.createVector3(STREET_WIDTH + (2 * i + 1) * 0.5 * PROPERTY_WIDTH,
            0, STREET_WIDTH + (2 * j + 1) * 0.5 * PROPERTY_WIDTH));
        ITriangleMesh mesh = getHouseAtPosition(houseOrigin);
        CgNode houseNode = new CgNode(mesh, "House");
        blockNode.addChild(houseNode);
      }
    }
    return blockNode;
  }

  /**
   * Generates the ground for a block
   *
   * @param origin
   *          Origin of the block
   * @param blockWidth
   *          Width of the block
   * @param blockHeight
   *          Heightof the block
   * @return Ground plane as triangle mesh
   */
  private ITriangleMesh getGroundPlane(Vector origin, double blockWidth, double blockHeight) {
    ITriangleMesh mesh = new TriangleMesh();
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        Vector corner = origin.add(VectorFactory.createVector3(i * blockWidth, 0, j * blockHeight));
        mesh.addVertex(new Vertex(corner));
      }
    }
    mesh.addTriangle(1, 2, 0);
    mesh.addTriangle(1, 3, 2);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;
  }

  /**
   * Return a house model at a given position in space. Use random house from
   * database.
   * 
   * @param houseOrigin
   *          Center on the ground of the house.
   * @return House triangle mesh.
   */
  public ITriangleMesh getHouseAtPosition(Vector houseOrigin) {
    int index = (int) (Math.random() * houseModels.size());
    ITriangleMesh houseMesh = houseModels.get(index);
    ITriangleMesh mesh = new TriangleMesh(houseMesh);
    mesh.getMaterial().setReflectionSpecular(VectorFactory.createVector3(0, 0, 0));
    mesh.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.8, 0.8, 0.8));
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setThrowsShadow(true);
    // mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    TriangleMeshTransformation.multiply(mesh,
        MatrixFactory.createRotationMatrix(VectorFactory.createVector3(0, 1, 0), Math.random() * Math.PI * 2));
    TriangleMeshTransformation.translate(mesh, houseOrigin);
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    return mesh;
  }
}

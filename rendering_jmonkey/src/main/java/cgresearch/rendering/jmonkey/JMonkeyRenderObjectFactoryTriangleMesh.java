/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jmonkey;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;

/**
 * Factory for jMonkey triangle mesh render object.
 * 
 * @author Philipp Jenke
 * 
 */
public class JMonkeyRenderObjectFactoryTriangleMesh implements IRenderObjectsFactory<Node> {
  /**
   * Reference to the asset manager
   */
  protected final AssetManager assetManager;

  /**
   * Constructor
   */
  public JMonkeyRenderObjectFactoryTriangleMesh(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see
   * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
   * .Object, java.lang.Object)
   */
  @Override
  public Node createRenderObject(Node parentNode, CgNode cgNode) {
    ITriangleMesh triangleMesh = (ITriangleMesh) cgNode.getContent();
    Node node = new Node();
    parentNode.attachChild(node);
    Geometry geometry = createTriangleMeshMesh(assetManager, triangleMesh);
    node.attachChild(geometry);

    if (triangleMesh.getMaterial().getTextureId() != null) {
      Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
      CgTexture cgTexture = cgresearch.graphics.material.ResourceManager.getTextureManagerInstance()
          .getResource(triangleMesh.getMaterial().getTextureId());
      // String pathToResource =
      // ResourcesLocator.getInstance().getPathToResource(cgTexture.getTextureFilename());
      String pathToResource = cgTexture.getTextureFilename();
      Texture tex = assetManager.loadTexture(pathToResource);
      mat.setTexture("DiffuseMap", tex);
      // mat.set
      geometry.setMaterial(mat);
    } else {
      Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
      Vector color = triangleMesh.getMaterial().getReflectionDiffuse();
      mat.setColor("Diffuse", new ColorRGBA((float) color.get(0), (float) color.get(1), (float) color.get(2), 1));
      // mat.set
      geometry.setMaterial(mat);
    }

    Logger.getInstance().debug("Create JMonkey triangle mesh.");
    return node;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
   */
  @Override
  public Class<?> getType() {
    return ITriangleMesh.class;
  }

  /**
   * Create a geometry object for a triangle mesh.
   * 
   * @param assetManager
   *          Current asset manager.
   * @param triangleMesh
   *          Triangle mesh to be used.
   * @return New Geometry object containing the meshs.
   */
  private Geometry createTriangleMeshMesh(AssetManager assetManager, ITriangleMesh triangleMesh) {
    Mesh mesh = new Mesh();
    mesh.setMode(Mesh.Mode.Triangles);

    int size = triangleMesh.getNumberOfTriangles() * 3;

    float[] vertexBuffer = new float[size * 3];
    float[] colorBuffer = new float[size * 3];
    float[] normalBuffer = new float[size * 3];
    float[] texCoordsBuffer = new float[size * 2];
    int[] indexBuffer = new int[size];

    // Fill vertex and color buffer
    for (int triangleIndex = 0; triangleIndex < triangleMesh.getNumberOfTriangles(); triangleIndex++) {

      ITriangle t = triangleMesh.getTriangle(triangleIndex);

      for (int vertexInTriangleIndex = 0; vertexInTriangleIndex < 3; vertexInTriangleIndex++) {
        int vertexIndex = t.get(vertexInTriangleIndex);
        int texCoordIndex = t.getTextureCoordinate(vertexInTriangleIndex);
        IVertex vertex = triangleMesh.getVertex(vertexIndex);
        Vector pos = vertex.getPosition();
        Vector normal = vertex.getNormal();
        Vector color = triangleMesh.getMaterial().getReflectionDiffuse();
        Vector texCoord = triangleMesh.getTextureCoordinate(texCoordIndex);

        // Position
        vertexBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) pos.get(0);
        vertexBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) pos.get(1);
        vertexBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) pos.get(2);

        // Normal
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) normal.get(0);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) normal.get(1);
        normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) normal.get(2);

        // Color
        colorBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = (float) color.get(0);
        colorBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = (float) color.get(1);
        colorBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = (float) color.get(2);

        // Texture coords
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2] = (float) texCoord.get(0);
        texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2 + 1] = (float) texCoord.get(1);
      }
    }
    for (int i = 0; i < size; i++) {
      indexBuffer[i] = i;
    }

    mesh.setBuffer(Type.Position, 3, vertexBuffer);
    mesh.setBuffer(Type.Index, 1, indexBuffer);
    mesh.setBuffer(Type.Color, 3, colorBuffer);
    mesh.setBuffer(Type.Normal, 3, normalBuffer);
    mesh.setBuffer(Type.TexCoord, 2, texCoordsBuffer);
    mesh.updateBound();

    // Material mat = new Material(assetManager,
    // "Common/MatDefs/Light/Lighting.j3md");
    // // mat.setBoolean("VertexColor", true);
    // // mat.setColor("Diffuse", ColorRGBA.White);
    // // mat.setColor("Specular", ColorRGBA.White);
    // // mat.setFloat("Shininess", 64f); // [0,128]
    Geometry geom = new Geometry("triangle mesh", mesh);
    return geom;
  }
}

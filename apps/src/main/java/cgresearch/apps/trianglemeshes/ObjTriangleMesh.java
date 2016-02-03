/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.trianglemeshes;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.logging.Logger.VerboseMode;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.NodeMerger;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTransformation;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.StlFileReader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.LightSource.Type;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class ObjTriangleMesh extends CgApplication {

  /**
   * Constructor.
   */
  public ObjTriangleMesh() {
    // 3D Object
    // loadFenja();
    loadLotrCubeWithTextureAtlas();
    // loadScetchUp();
    // loadPlaneWithBunny();
    // loadMedivalHouse();
    // loadHulk();
    // loadNofretete();
    Logger.getInstance().setVerboseMode(VerboseMode.DEBUG);

    // Coordinate system
    // getCgRootNode().addChild(new CoordinateSystem());

    // Lights
    getCgRootNode().clearLights();

    LightSource light = new LightSource(Type.POINT);
    light.setPosition(VectorMatrixFactory.newIVector3(5, 5, 5));
    light.setDirection(VectorMatrixFactory.newIVector3(-1, -1, -1));
    light.setColor(VectorMatrixFactory.newIVector3(1, 1, 1));
    // light.setSpotOpeningAngle(20);
    getCgRootNode().addLight(light);

    LightSource light2 = new LightSource(Type.POINT);
    light2.setPosition(VectorMatrixFactory.newIVector3(5, 5, -5));
    light2.setDirection(VectorMatrixFactory.newIVector3(-1, -1, -1));
    light2.setColor(VectorMatrixFactory.newIVector3(1, 1, 1));
    // light2.setSpotOpeningAngle(20);
    getCgRootNode().addLight(light2);
  }

  private void loadNofretete() {
    StlFileReader reader = new StlFileReader();
    ITriangleMesh mesh = reader.read("meshes/nofretete/nofretete.stl");
    if (mesh != null) {
      mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      mesh.getMaterial().setReflectionAmbient(VectorMatrixFactory.newIVector3(Material.PALETTE0_COLOR3));
      mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE0_COLOR3));
      mesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(1, 1, 1));
      getCgRootNode().addChild(new CgNode(mesh, "Nofretete"));
    }
  }

  public void loadHulk() {
    // getCgRootNode().setUseBlending(true);
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/hulk/Hulk.obj");
    meshes.forEach(mesh -> {
      mesh.computeTriangleNormals();
      mesh.computeVertexNormals();
      mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
      mesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
      mesh.getMaterial().setTransparency(0.9);
      CgNode hulkNode = new CgNode(mesh, "hulk");
      getCgRootNode().addChild(hulkNode);
    });
  }

  public void loadMedivalHouse() {
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/medival/cornerhouse/cornerhouse.obj");
    for (ITriangleMesh mesh : meshes) {
      mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
      mesh.getMaterial().setReflectionAmbient(VectorMatrixFactory.newIVector3(1, 1, 1));
      mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(1, 1, 1));
      mesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0.05, 0.05, 0.05));
      mesh.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
      CgNode bunnyNode = new CgNode(mesh, "medival house");
      getCgRootNode().addChild(bunnyNode);
    }
    getCgRootNode().addChild(new CoordinateSystem());
  }

  public void loadPlaneWithBunny() {
    getCgRootNode().setUseBlending(true);
    Plane plane = new Plane(VectorMatrixFactory.newIVector3(0, 0, 0), VectorMatrixFactory.newIVector3(0, 1, 0));
    plane.getMaterial().setReflectionAmbient(Material.PALETTE2_COLOR1);
    // plane.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR1);
    plane.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0, 0, 0));
    plane.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
    plane.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    plane.getMaterial().setSpecularShininess(100);
    plane.getMaterial().setTransparency(1);
    getCgRootNode().addChild(new CgNode(plane, "plane"));
    // plane.getMaterial().addShaderId(Material.SHADER_WIREFRAME);

    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
    if (meshes.size() == 1) {
      ITriangleMesh bunny = meshes.get(0);
      bunny.fitToUnitBox();
      TriangleMeshTransformation.scale(bunny, 0.5);
      TriangleMeshTransformation.translate(bunny, VectorMatrixFactory.newIVector3(-1, 0.26, -1));
      bunny.computeTriangleNormals();
      bunny.computeVertexNormals();
      bunny.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      bunny.getMaterial().setReflectionAmbient(Material.PALETTE1_COLOR2);
      // bunny.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0,
      // 0, 0));
      bunny.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR2);
      bunny.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0.2, 0.2, 0.2));
      bunny.getMaterial().setTransparency(0.5);
      CgNode bunnyNode = new CgNode(bunny, "bunny");
      getCgRootNode().addChild(bunnyNode);
    }

  }

  public void loadScetchUp() {
    String objFilename = "scetchup/haus.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }

    ITriangleMesh mesh = new TriangleMesh();
    for (ITriangleMesh meshComponent : meshes) {
      mesh.unite(meshComponent);
    }
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();

    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.75, 0.75, 0.75));
    mesh.getMaterial().setReflectionSpecular(VectorMatrixFactory.newIVector3(0, 0, 0));
    getCgRootNode().addChild(new CgNode(mesh, "mesh"));
  }

  public void loadLotrCubeWithTextureAtlas() {
    String objFilename = "meshes/cube.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    ITriangleMesh mesh = meshes.get(0);
    mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
    String LOTR_TEXTURE_ATLAS = "LOTR_TEXTURE_ATLAS";
    ResourceManager.getTextureManagerInstance().addResource(LOTR_TEXTURE_ATLAS,
        new CgTexture("textures/lotr_texture_atlas.png"));
    mesh.getMaterial().setTextureId(LOTR_TEXTURE_ATLAS);
    getCgRootNode().addChild(new CgNode(mesh, "mesh"));
  }

  public void loadFenja() {
    String objFilename = "meshes/fenja02.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    // CgNode node = new CgNode(null, "cube");
    for (int i = 0; i < meshes.size(); i++) {
      ITriangleMesh mesh = meshes.get(i);
      // String texId = "tex_id_dhl_logo";
      // ResourceManager.getTextureManagerInstance().addResource(texId,
      // new CgTexture("textures/lego.png"));
      // mesh.getMaterial().setTextureId(texId);
      mesh.fitToUnitBox();
      mesh.getMaterial().setShaderId(Material.SHADER_COLOR);
      mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR2));
      NodeMerger.merge(mesh, 1e-5);
      mesh.computeTriangleNormals();
      mesh.computeVertexNormals();
      mesh.getMaterial().setRenderMode(Normals.PER_VERTEX);
      getCgRootNode().addChild(new CgNode(mesh, "mesh"));
    }
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new ObjTriangleMesh();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

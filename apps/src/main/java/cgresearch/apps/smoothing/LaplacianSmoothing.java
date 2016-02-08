/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.smoothing;

import java.util.List;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.algorithms.TriangleMeshTools;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class LaplacianSmoothing extends CgApplication {

  private ITriangleMesh smoothedMesh;
  private ITriangleMesh originalMesh;

  /**
   * Constructor.
   */
  public LaplacianSmoothing() {
    String objFilename = "meshes/cow.obj";
    ObjFileReader reader = new ObjFileReader();
    List<ITriangleMesh> meshes = reader.readFile(objFilename);
    if (meshes == null) {
      return;
    }
    originalMesh = meshes.get(0);
    originalMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    originalMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));
    originalMesh.computeTriangleNormals();
    originalMesh.computeVertexNormals();
    TriangleMeshTools.addNoise(originalMesh, 0.5e-1);
    getCgRootNode().addChild(new CgNode(originalMesh, "mesh"));

    smoothedMesh = new TriangleMesh(originalMesh);
    Transformation transformation = new Transformation();
    transformation.addTranslation(VectorMatrixFactory.newIVector3(1, 0, 0));
    CgNode transformationNode = new CgNode(transformation, "transformation");
    smoothedMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    smoothedMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR3));
    getCgRootNode().addChild(transformationNode);
    transformationNode.addChild(new CgNode(smoothedMesh, "smoothed mesh"));
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    LaplacianSmoothing app = new LaplacianSmoothing();
    SmoothingGui gui = new SmoothingGui(app.getOriginalMesh(), app.getSmoothedMesh());
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(gui);
  }

  private ITriangleMesh getOriginalMesh() {
    return originalMesh;
  }

  public ITriangleMesh getSmoothedMesh() {
    return smoothedMesh;
  }
}

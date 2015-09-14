/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.smoothing;

import java.util.List;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.LaplaceSmoothing;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshTools;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.Transformation;
import cgresearch.rendering.jogl.JoglAppLauncher;

/**
 * Demo frame to work with triangle meshes clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class LaplacianSmoothing extends CgApplication {

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
    ITriangleMesh mesh = meshes.get(0);
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR4));
    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
    TriangleMeshTools.addNoise(mesh, 0.5e-1);
    getCgRootNode().addChild(new CgNode(mesh, "mesh"));

    ITriangleMesh smoothedMesh = new TriangleMesh(mesh);
    Transformation transformation = new Transformation();
    transformation.addTranslation(VectorMatrixFactory.newIVector3(1, 0, 0));
    CgNode transformationNode = new CgNode(transformation, "transformation");
    smoothedMesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    smoothedMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(Material.PALETTE2_COLOR3));
    getCgRootNode().addChild(transformationNode);
    transformationNode.addChild(new CgNode(smoothedMesh, "smoothed mesh"));

    // Apply one step of smoothing
    LaplaceSmoothing.smooth(smoothedMesh, 3);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    CgApplication app = new LaplacianSmoothing();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

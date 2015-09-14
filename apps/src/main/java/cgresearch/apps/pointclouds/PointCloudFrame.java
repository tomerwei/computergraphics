/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.pointclouds;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.fileio.AsciiPointFormat;
import cgresearch.graphics.fileio.AsciiPointsReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.JoglAppLauncher;

/**
 * Demo frame to work with point clouds.
 * 
 * @author Philipp Jenke
 * 
 */
public class PointCloudFrame extends CgApplication {

  /**
   * Constructor.
   */
  public PointCloudFrame() {
    String filename = "pointclouds/face.ascii.points";
    AsciiPointsReader reader = new AsciiPointsReader();
    IPointCloud pointCloud =
        reader.readFromFile(filename, new AsciiPointFormat().setPosition(0, 1, 2).setNormal(3, 4, 5).setColor(6, 7, 8));
    pointCloud.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    pointCloud.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(0.25, 0.75, 0.25));
    getCgRootNode().addChild(new CgNode(pointCloud, "point cloud"));

    // ITriangleMesh cubeMesh = TriangleMeshFactory.createCube();
    // IMatrix3 R = VectorMatrixFactory.getRotationMatrix(
    // VectorMatrixFactory.newIVector3(1, 0, 0),
    // 20.0 * Math.PI / 180.0);
    // TriangleMeshTransformation.transform(cubeMesh, R);
    // IPointCloud pointCloud = TriangleMeshSampler.sample(cubeMesh, 500);
    // pointCloud.getMaterial().setShaderId(Material.SHADER_COLOR);
    // for (int i = 0; i < pointCloud.getNumberOfPoints(); i++) {
    // pointCloud.getPoint(i).setColor(
    // VectorMatrixFactory.newIVector3(Math.random(),
    // Math.random(), Math.random()));
    // }

    CgNode node1 = new CgNode(pointCloud, "PointCloud");
    getCgRootNode().addChild(node1);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new PointCloudFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

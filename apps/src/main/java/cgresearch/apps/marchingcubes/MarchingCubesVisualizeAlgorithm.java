/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.marchingcubes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.jogamp.opengl.GL2;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.implicitfunction.MarchingCubes;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.datastructures.primitives.Sphere;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.rendering.jogl.core.JoglRenderable;

/**
 * Central frame for the mesh exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class MarchingCubesVisualizeAlgorithm extends CgApplication implements JoglRenderable {

  private final IVector3 COLOR_INSIDE = VectorMatrixFactory.newIVector3(0.25, 0.75, 0.25);
  private final IVector3 COLOR_OUTSIDE = VectorMatrixFactory.newIVector3(0.75, 0.25, 0.25);
  private final Cube cube;
  private List<Sphere> spheres;
  private ITriangleMesh mesh = new TriangleMesh();;

  /**
   * Constructor.
   */
  public MarchingCubesVisualizeAlgorithm(Cube cube) {
    this.cube = cube;
    cube.addObserver(this);
    
    CgNode cubeNode = createCube();
    getCgRootNode().addChild(cubeNode);
    
    CgNode meshNode = new CgNode(mesh, "triangles");
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
    mesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR4);
    getCgRootNode().addChild(meshNode);
    
    setSphereColors();
    createTriangles();

    getCgRootNode().addChild(new CoordinateSystem());
  }

  private void createTriangles() {
    mesh.clear();
    mesh.unite(MarchingCubes.createTriangles(cube.getCornerPoint(0), cube.getCornerPoint(1), cube.getCornerPoint(2),
        cube.getCornerPoint(3), cube.getCornerPoint(4), cube.getCornerPoint(5), cube.getCornerPoint(6),
        cube.getCornerPoint(7), cube.getValue(0), cube.getValue(1), cube.getValue(2), cube.getValue(3),
        cube.getValue(4), cube.getValue(5), cube.getValue(6), cube.getValue(7)));
    mesh.updateRenderStructures();
  }

  /**
   * Set the sphere colors according to the current classification.
   */
  private void setSphereColors() {
    for (int i = 0; i < 8; i++) {
      spheres.get(i).getMaterial()
          .setReflectionDiffuse((cube.getValue(i) <= cube.getIsoValue()) ? COLOR_INSIDE : COLOR_OUTSIDE);
      spheres.get(i).updateRenderStructures();
    }
  }

  private CgNode createCube() {
    CgNode groupNode = new CgNode(null, "cube");

    // Spheres showing the classification
    spheres = new ArrayList<Sphere>();
    double sphereRadius = 0.15;
    for (int i = 0; i < 8; i++) {
      spheres.add(new Sphere(cube.getCornerPoint(i), sphereRadius));
    }
    for (Sphere sphere : spheres) {
      sphere.getMaterial().setReflectionDiffuse(COLOR_INSIDE);
      sphere.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      sphere.getMaterial().setRenderMode(Normals.PER_VERTEX);
      groupNode.addChild(new CgNode(sphere, "sphere"));
    }

    // Cube lines
    List<Line3D> lines = new ArrayList<Line3D>();
    for (int i = 0; i < 4; i++) {
      lines.add(new Line3D(cube.getCornerPoint(i), cube.getCornerPoint((i + 1) % 4)));
      lines.add(new Line3D(cube.getCornerPoint(i + 4), cube.getCornerPoint((i + 1) % 4 + 4)));
      lines.add(new Line3D(cube.getCornerPoint(i), cube.getCornerPoint(i + 4)));
    }

    for (Line3D line : lines) {
      line.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR4);
      line.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
      groupNode.addChild(new CgNode(line, "line"));
    }

    return groupNode;
  }

  @Override
  public void draw3D(GL2 gl) {
  }

  @Override
  public BoundingBox getBoundingBox() {
    return null;
  }

  @Override
  public void update(Observable o, Object arg) {
    setSphereColors();
    createTriangles();
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    Cube cube = new Cube();
    MarchingCubesVisualizeAlgorithm app = new MarchingCubesVisualizeAlgorithm(cube);
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new MarchingCubesVisualizeAlgorithmGui(cube));
  }
}

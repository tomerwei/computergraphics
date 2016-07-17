/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.raytracing;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.primitives.Sphere;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.LightSource.Type;

/**
 * Test the raytracer.
 */
public class RaytracingFrame extends CgApplication {

  /**
   * Constructor.
   */
  public RaytracingFrame() {
    getCgRootNode().clearLights();
    LightSource light = new LightSource(Type.POINT);
    light.setPosition(VectorFactory.createVector3(5, 5, 5));
    getCgRootNode().addLight(light);

    Sphere sphere1 = new Sphere(VectorFactory.createVector3(1, 0, 1), 0.5);
    sphere1.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(1, 0, 0));
    sphere1.getMaterial().setReflection(0);
    sphere1.getMaterial().setRefraction(0);
    sphere1.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    CgNode sphere1Node = new CgNode(sphere1, "sphere1");
    getCgRootNode().addChild(sphere1Node);

    Sphere sphere2 = new Sphere(VectorFactory.createVector3(-1, 0, -1), 0.5);
    sphere2.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.25, .55, .55));
    sphere2.getMaterial().setReflection(0.5);
    sphere2.getMaterial().setRefraction(0);
    sphere2.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    CgNode sphere2Node = new CgNode(sphere2, "sphere2");
    getCgRootNode().addChild(sphere2Node);

    Sphere sphere3 = new Sphere(VectorFactory.createVector3(0, 0, 0), 0.5);
    sphere3.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.55, .25, .55));
    sphere3.getMaterial().setReflection(1);
    sphere3.getMaterial().setRefraction(0);
    sphere3.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    CgNode sphere3Node = new CgNode(sphere3, "sphere3");
    getCgRootNode().addChild(sphere3Node);

    // ObjFileReader reader = new ObjFileReader();
    // ITriangleMesh mesh = reader.readFile("meshes/teddy.obj").get(0);
    // ITriangleMesh mesh = TriangleMeshFactory.createTetrahedron();
    // mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    // mesh.getMaterial().setReflectionDiffuse(Material.PALETTE1_COLOR1);
    // mesh.getMaterial().setReflection(0);
    // mesh.getMaterial().setRefraction(0);
    // getCgRootNode().addChild(new CgNode(mesh, "tetrahedron"));

    Plane plane = new Plane(VectorFactory.createVector3(0, -1, 0), VectorFactory.createVector3(0, 1, 0));
    plane.getMaterial().setReflectionDiffuse(VectorFactory.createVector3(0.8, 0.8, 0.8));
    plane.getMaterial().setReflection(0.5);
    plane.getMaterial().setRefraction(0.0);
    plane.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    CgNode planeNode = new CgNode(plane, "plane");
    getCgRootNode().addChild(planeNode);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    RaytracingFrame frame = new RaytracingFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(frame);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    RaytracingGui gui = new RaytracingGui(frame.getCgRootNode());
    appLauncher.addCustomUi(gui);
  }
}

package cgresearch.apps.curves;

import java.util.Observable;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.IVector3;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.curves.TensorProductSurface;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Play around with tensor product surfaces.
 * 
 * @author Philipp Jenke
 *
 */
public class TensorProductSurfaceFrame extends CgApplication {

  /**
   * Surface object
   */
  private final TensorProductSurface surface;

  /**
   * Triangle Mesh to render the surface;
   */
  private ITriangleMesh mesh = new TriangleMesh();

  /**
   * Constructor.
   */
  public TensorProductSurfaceFrame() {
    surface = new TensorProductSurface(3, 3);
    surface.addObserver(this);

    for (int i = 0; i <= surface.getDegreeU(); i++) {
      for (int j = 0; j <= surface.getDegreeV(); j++) {
        surface.getControlPoint(i, j).set(1, Math.random() * 0.2 - 0.1);
      }
    }

    new ControlPointInteraction(surface);

    createMesh4Surface();
    mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
    mesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);
    CgNode node = new CgNode(mesh, "surface");
    getCgRootNode().addChild(node);
  }

  private void createMesh4Surface() {
    int tesselation = 15;
    mesh.clear();

    // Vertices
    for (int i = 0; i < tesselation; i++) {
      double u = (double) i / (double) (tesselation - 1);
      for (int j = 0; j < tesselation; j++) {
        double v = (double) j / (double) (tesselation - 1);
        IVector3 p = surface.eval(u, v);
        mesh.addVertex(new Vertex(p));

        if (i > 0 && j > 0) {
          mesh.addTriangle(
              new Triangle((j - 1) * tesselation + (i - 1), (j) * tesselation + (i - 1), (j - 1) * tesselation + (i)));
          mesh.addTriangle(
              new Triangle((j) * tesselation + (i - 1), (j) * tesselation + (i), (j - 1) * tesselation + (i)));
        }
      }
    }

    mesh.computeTriangleNormals();
    mesh.computeVertexNormals();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o == surface) {
      createMesh4Surface();
      mesh.updateRenderStructures();
    }
    super.update(o, arg);
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new TensorProductSurfaceFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
  }
}

package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

import cgresearch.graphics.datastructures.primitives.Arrow;
import cgresearch.graphics.datastructures.primitives.IPrimitive;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;

/**
 * Render content for primitives.
 * 
 * TODO: Currently only implemented for arrows.
 * 
 * @author Philipp Jenke
 */
public class RenderContentPrimitive extends RenderContentTriangleMesh {

  private final IPrimitive primitive;
  private final ITriangleMesh mesh;

  public RenderContentPrimitive(IPrimitive primitive, ITriangleMesh mesh) {
    super(mesh);
    this.primitive = primitive;
    this.mesh = mesh;
  }

  @Override
  public void draw3D(GL2 gl) {
    if (primitive.needsUpdateRenderStructures()) {
      if (primitive instanceof Arrow) {
        Arrow arrow = (Arrow) primitive;
        mesh.clear();
        mesh.copyFrom(TriangleMeshFactory.createArrow(arrow));
        mesh.computeTriangleNormals();
        mesh.getMaterial().copyFrom(arrow.getMaterial());
        mesh.updateRenderStructures();
      }
    }

    super.draw3D(gl);
  }

}

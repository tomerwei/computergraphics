/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLOffscreenAutoDrawable;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.algorithms.TriangleMeshTransformation;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.rendering.jogl.core.RenderContentTriangleMesh;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Parity count + Z-Buffer algorithm to voxelize a 3d-model as proposed in
 * "Simplification and Repair of Polygonal Models Using Volumetric Techniques"
 * by Fakir S. Nooruddin and Greg Turk and
 * "A Depth Buffer Based Voxelization Algorithm" by Aggeliki Karabassi, George
 * Papaioannou and Theoharis Theoharis and the program "binvox" by Patrick Min
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelizationParityCountOpenGL implements
    IVoxelizationAlgorithm, GLEventListener {

  private GLU glu = null;
  private RenderContentTriangleMesh node = null;

  @Override
  public IVoxelCloud transformMesh2Cloud(
      ITriangleMesh meshOriginal, int resolutionAxisX,
      Vector voxelScale) {
    BoundingBox box = meshOriginal.getBoundingBox();
    Vector location = box.getLowerLeft();
    double width =
        box.getUpperRight().subtract(box.getLowerLeft())
            .get(MathHelpers.INDEX_0);
    double height =
        box.getUpperRight().subtract(box.getLowerLeft())
            .get(MathHelpers.INDEX_1);
    double depth =
        box.getUpperRight().subtract(box.getLowerLeft())
            .get(MathHelpers.INDEX_2);

    // get resolutions
    double xRes = width / resolutionAxisX;
    double yRes =
        (voxelScale.get(MathHelpers.INDEX_1) / voxelScale
            .get(MathHelpers.INDEX_0)) * xRes;
    double zRes =
        (voxelScale.get(MathHelpers.INDEX_2) / voxelScale
            .get(MathHelpers.INDEX_0)) * xRes;
    int resolutionAxisY = (int) Math.ceil(height / yRes);
    int resolutionAxisZ = (int) Math.ceil(depth / zRes);

    // the algorithm needs a NxNxN space, so we look for the max and set it
    // for all axis
    int resolutionAxisAll =
        Math.max(
            Math.max(resolutionAxisX, resolutionAxisY),
            resolutionAxisZ);

    // adjust location of x, y, z
    double offsetX =
        ((resolutionAxisAll * xRes) - width) * 0.5;
    double offsetY =
        ((resolutionAxisAll * yRes) - height) * 0.5;
    double offsetZ =
        ((resolutionAxisAll * zRes) - depth) * 0.5;
    location =
        location.subtract(VectorFactory.createVector3(
            offsetX, offsetY, offsetZ));

    // create voxel cloud
    IVoxelCloud cloud =
        new VoxelCloud(location,
            VectorFactory.createVector3(xRes, yRes,
                zRes), new VectorInt3(resolutionAxisAll,
                resolutionAxisAll, resolutionAxisAll));

    // copy mesh and transform to fit the unit box
    ITriangleMesh mesh = copyMesh(meshOriginal);
    mesh.fitToUnitBox();
    TriangleMeshTransformation.translate(mesh,
        VectorFactory.createVector3(0.5, 0.5, 0.5));
    mesh.getMaterial().setReflectionDiffuse(
        VectorFactory.createVector3(1.0, 0, 0));

    // create a rendercontent node to use cg_jogl rendering
    node = new RenderContentTriangleMesh(mesh);

    // let the OpenGL offscreen magic begin
    GLProfile glp = GLProfile.getDefault();
    GLCapabilities caps = new GLCapabilities(glp);
    GLOffscreenAutoDrawable draw =
        GLDrawableFactory.getFactory(glp)
            .createOffscreenAutoDrawable(null, caps, null,
                resolutionAxisAll, resolutionAxisAll);
    draw.addGLEventListener(this);

    // GLCanvas canvas = new GLCanvas(caps);
    // canvas.addGLEventListener(this);
    // Frame frame = new Frame("OpenGL");
    // frame.setSize(300, 300);
    // frame.add(canvas);
    // frame.setVisible(true);

    // Z-Buffer algorithm to reduce the number of voxels

    return cloud;
  }

  /**
   * Copies a mesh from another (without texture coordinates and material).
   * 
   * @param src
   *          source mesh
   * @return copied mesh
   */
  private ITriangleMesh copyMesh(ITriangleMesh src) {
    ITriangleMesh mesh = new TriangleMesh();
    // vertices
    for (int i = 0; i < src.getNumberOfVertices(); i++) {
      mesh.addVertex(new Vertex(src.getVertex(i)));
    }
    // faces
    for (int i = 0; i < src.getNumberOfTriangles(); i++) {
      mesh.addTriangle(new Triangle(src.getTriangle(i)));
    }
    // no need for texture coordinates
    return mesh;
  }

  @Override
  public void display(GLAutoDrawable draw) {
    System.out.println("render");
    GL2 gl = draw.getGL().getGL2();

    // clear
    gl.glClear(GL2.GL_COLOR_BUFFER_BIT
        | GL2.GL_DEPTH_BUFFER_BIT);

    // set view
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
    glu.gluLookAt(1, 0.5, 0.5, 0.5, 0.5, 0.5, 0, 0, 1);

    // draw
    node.draw3D(gl);
    gl.glFlush();
    gl.glFinish();

    // read depth buffer
    ByteBuffer ibb =
        ByteBuffer.allocateDirect(4 * 300 * 300);
    FloatBuffer bf = ibb.asFloatBuffer();
    gl.glReadPixels(0, 0, 300, 300, GL2.GL_DEPTH_COMPONENT,
        GL2.GL_FLOAT, bf);

    System.out.println(bf.capacity());
    System.out.println(bf.limit());

    Map<Float, Integer> map = new HashMap<Float, Integer>();
    for (int i = 0; i < bf.capacity(); i++) {
      Float f = bf.get(i);
      if (map.containsKey(f))
        map.put(f, map.get(f) + 1);
      else
        map.put(f, 1);
    }

    System.out.println(map);
  }

  @Override
  public void dispose(GLAutoDrawable draw) {

  }

  @Override
  public void init(GLAutoDrawable draw) {
    GL2 gl = draw.getGL().getGL2();
    glu = new GLU();

    // Enable buffers for rendering
    gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);
    gl.glEnableClientState(GLPointerFunc.GL_TEXTURE_COORD_ARRAY);
    gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);

    // General settings
    gl.glDisable(GLLightingFunc.GL_LIGHTING);
    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
  }

//  /**
//   * Initialize OpenGL for the depth reading.
//   * 
//   * @param gl
//   */
//  private void initDepthReading(GL2 gl) {
//    gl.glEnable(GL2.GL_DEPTH_TEST);
//    gl.glDisable(GL2.GL_BLEND);
//
//    gl.glLoadIdentity();
//    gl.glOrtho(-0.5, 0.5, -0.5, 0.5, 0.0, 1.0);
//  }

//  /**
//   * Initialize OpenGL for the color reading.
//   * 
//   * @param gl
//   */
//  private void initColorReading(GL2 gl) {
//    gl.glDisable(GL2.GL_DEPTH_TEST);
//    gl.glEnable(GL2.GL_BLEND);
//    gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE);
//    gl.glDisable(GL2.GL_DITHER);
//    gl.glColor3ub((byte) 255, (byte) 128, (byte) 1);
//    gl.glDisable(GL2.GL_CULL_FACE);
//    gl.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 1);
//  }

  @Override
  public void reshape(GLAutoDrawable draw, int arg1,
      int arg2, int arg3, int arg4) {

  }

}

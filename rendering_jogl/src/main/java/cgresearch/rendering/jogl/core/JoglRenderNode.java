/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.core;

import java.util.Observable;
import java.util.Observer;

import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.graphics.scenegraph.Transformation;
import com.jogamp.opengl.GL2;

import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgNodeStateChange;
import cgresearch.rendering.jogl.material.JoglShader;

/**
 * This node is used on the JOGL render scene graph.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglRenderNode implements Observer {

  /**
   * Shader attribute location.
   */
  private static final int ATTRIBUTE_LOCATION_BARYCENTRIC = 1;

  /**
   * Reference to the scene graph node.
   */
  protected final CgNode cgNode;

  /**
   * Content to be rendered (renderer for the content of the CgNode).
   */
  private final IRenderContent renderContent;

  /**
   * Constructor.
   */
  public JoglRenderNode(CgNode cgNode, IRenderContent renderContent) {
    this.cgNode = cgNode;
    this.renderContent = renderContent;
  }

  /*
   * (nicht-Javadoc)
   * 
   * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
   */
  @Override
  public void update(Observable o, Object arg) {
    // Update the cached material information
    if (o instanceof CgNode && arg instanceof CgNodeStateChange) {
      CgNodeStateChange state = (CgNodeStateChange) arg;
      if (state.getState() == CgNodeStateChange.ChangeStates.CHANGED) {
        renderContent.updateRenderStructures();
      }
    }
  }

  /**
   * Draw the content
   * 
   * @param gl
   */
  public void draw3D(GL2 gl) {
    draw3D(gl, null, null);
  }

  /**
   * Draw the content
   */
  public void draw3D(GL2 gl, LightSource lightSource, Transformation transformation) {
    if (cgNode == null || !cgNode.isVisible()) {
      return;
    }

    if (cgNode.getContent() != null) {
      // Material
      Material material = cgNode.getContent().getMaterial();
      setupMaterial(gl, material);

      if (material.hasShader()) {
        // Material uses shaders
        for (int shaderIndex = 0; shaderIndex < material.getNumberOfShaders(); shaderIndex++) {
          setupShader(gl, material, shaderIndex);
          if (lightSource != null) {
            renderContent.draw3D(gl, lightSource, transformation);
          } else {
            renderContent.draw3D(gl);
          }
        }
      } else {
        // No shader specified - fixed function pipeline
        if (lightSource != null) {
          renderContent.draw3D(gl, lightSource, transformation);
        } else {
          renderContent.draw3D(gl);
        }
      }
    }
  }

  /**
   * Cleanup after the node (and its children are drawn). Override if needed.
   */
  public void afterDraw(GL2 gl) {
    if (renderContent != null) {
      renderContent.afterDraw(gl);
    }
  }

  /**
   * Enable the shader.
   */
  private void setupShader(GL2 gl, Material material, int shaderIndex) {
    // Enable shader
    String shaderId = material.getShaderId(shaderIndex);
    CgGlslShader shader = ResourceManager.getShaderManagerInstance().getResource(shaderId);
    if (shader != null) {
      if (material.getShaderId(shaderIndex) == Material.SHADER_WIREFRAME) {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
      } else {
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
      }
      JoglShader.use(shader, gl);
      gl.glBindAttribLocation(shader.getShaderProgram(), ATTRIBUTE_LOCATION_BARYCENTRIC, "barycentric");
      int location = gl.glGetUniformLocation(shader.getShaderProgram(), "camera_position");
      gl.glUniform3f(location, (float) Camera.getInstance().getEye().get(0),
          (float) Camera.getInstance().getEye().get(1), (float) Camera.getInstance().getEye().get(2));
    }

  }

  /**
   * Setup the material information.
   */
  private void setupMaterial(GL2 gl, Material material) {
    if (cgNode != null && !cgNode.isVisible() && material != null) {
      return;
    }

    gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, material.getSpecularShininess());
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, material.getReflectionAmbient().floatData(), 0);
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, material.getReflectionDiffuse().floatData(), 0);
    gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, material.getReflectionSpecular().floatData(), 0);

  }

  /**
   * Getter.
   */
  public CgNode getCgNode() {
    return cgNode;
  }
}

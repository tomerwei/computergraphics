package cgresearch.rendering.jogl.core;

import com.jogamp.opengl.GL2;

import cgresearch.core.math.Vector;
import cgresearch.graphics.material.CgGlslShader;

/**
 * Set variables which can be used in the shaders
 * 
 * @author Philipp Jenke
 *
 */
public class ShaderVariables {

  /**
   * These constant names need to be referred to in the shader.
   */
  private static final String CAMERA_POSITION = "camera_position";
  private static final String TRANSPARENCY = "transparency";

  /**
   * Set the current camera position.
   */
  public static void setCameraPosition(GL2 gl, CgGlslShader shader, Vector pos) {
    int location = gl.glGetUniformLocation(shader.getShaderProgram(), CAMERA_POSITION);
    gl.glUniform3f(location, (float) pos.get(0), (float) pos.get(1), (float) pos.get(2));
  }

  /**
   * Set the transparency for the object.
   */
  public static void setTransparency(GL2 gl, CgGlslShader shader, double transparency) {
    int location = gl.glGetUniformLocation(shader.getShaderProgram(), TRANSPARENCY);
    gl.glUniform1f(location, (float) transparency);
  }
}

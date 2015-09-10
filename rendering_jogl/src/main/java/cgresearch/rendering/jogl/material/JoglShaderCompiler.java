package cgresearch.rendering.jogl.material;

import com.jogamp.opengl.GL2;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.rendering.jogl.core.JoglRenderable;
import cgresearch.ui.resources.IGlslShaderCompiler;

public class JoglShaderCompiler implements
    IGlslShaderCompiler, JoglRenderable {

  /**
   * Flag to indicate compile requirement.
   * 
   */
  private boolean compileRequired = false;

  /**
   * Type of the shader to be compiled.
   */
  private int shaderType;

  /**
   * Source of the shader.
   */
  private String source;

  /**
   * Constructor.
   */
  public JoglShaderCompiler() {
  }

  @Override
  public void compile(int shaderType, String source) {
    compileRequired = true;
    this.shaderType = shaderType;
    this.source = source;
  }

  @Override
  public void draw3D(GL2 gl2) {
    if (compileRequired) {
      int id =
          JoglShader.compileShaderFromSource(gl2,
              shaderType, source);
      if (id < 0) {
        Logger.getInstance().error(
            "Failed to compile shader");
      } else {
        Logger.getInstance().message(
            "Successfully compiled shader");
      }
      compileRequired = false;
    }
  }

  @Override
  public BoundingBox getBoundingBox() {
    return null;
  }

}

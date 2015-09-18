package cgresearch.rendering.jogl.ui;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.IGlslShaderCompiler;
import cgresearch.ui.resources.ResourceEditor;

public class ShaderEditorResourceEditor implements ResourceEditor {

  /**
   * Compiler used to compile the shader.
   */
  private final IGlslShaderCompiler glslCompiler;

  public ShaderEditorResourceEditor(IGlslShaderCompiler glslCompiler) {
    this.glslCompiler = glslCompiler;
  }

  @Override
  public void apply(Object resource) {
    if (resource instanceof CgGlslShader) {
      new ShaderEditor((CgGlslShader) resource, glslCompiler);
    } else {
      Logger.getInstance().error("Resource must be of type " + CgGlslShader.class.getName());
    }
  }

}

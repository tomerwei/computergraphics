/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.ui;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.ui.SwingUserInterface;
import cgresearch.ui.menu.ResourcesMenu;
import cgresearch.ui.resources.ResourceManagerEditor;

/**
 * Base class for all applications.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglSwingUserInterface extends SwingUserInterface {

  /**
   * Constructor
   */
  public JoglSwingUserInterface(CgApplication app, JoglFrame frame) {
    super(app);
    JoglMenu joglMenu = new JoglMenu();
    registerApplicationMenu(joglMenu);
    ResourceManagerEditor shaderEditor =
        new ResourceManagerEditor(ResourceManager.getShaderManagerInstance(), "Shaders");
    ResourcesMenu resourcesMenu = new ResourcesMenu(shaderEditor, app.getCgRootNode());
    shaderEditor.addResourceEditor(CgGlslShader.class.getName(),
        new ShaderEditorResourceEditor(frame.getShaderCompiler()));
    registerApplicationMenu(resourcesMenu);
  }
}

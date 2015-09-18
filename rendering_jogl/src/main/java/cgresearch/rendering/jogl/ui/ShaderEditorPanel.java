package cgresearch.rendering.jogl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.IGlslShaderCompiler;
import cgresearch.rendering.jogl.material.JoglShader;

/**
 * An editor panel for a shader file (vertex oder fragment shader). Implements
 * JoglRenderable for GL context access.
 * 
 * @author Philipp Jenke
 *
 */
public class ShaderEditorPanel extends JPanel implements ActionListener {

  private final IGlslShaderCompiler compiler;

  /**
   * 
   */
  private static final long serialVersionUID = -1731321576656144422L;

  /**
   * Constants
   */
  private static final String ACTION_COMMAND_COMPILE = "ACTION_COMMAND_COMPILE";
  private static final String ACTION_COMMAND_SAVE = "ACTION_COMMAND_SAVE";
  private static final String ACTION_COMMAND_FILENAME = "ACTION_COMMAND_FILENAME";

  /**
   * Type of the processed shader (vertex, fragment)
   */
  private final CgGlslShader.ShaderType type;

  /**
   * Processed shader.
   */
  private final CgGlslShader shader;

  /**
   * Text area containing the shader source.
   */
  private final JTextArea textAreaXSource;
  private final JButton buttonFilename;

  /**
   * Constructor
   */
  public ShaderEditorPanel(CgGlslShader shader, CgGlslShader.ShaderType type, IGlslShaderCompiler compiler) {
    this.type = type;
    this.shader = shader;
    this.compiler = compiler;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Shader type caption
    String caption = (type == CgGlslShader.ShaderType.VERTEX) ? "Vertex shader" : "Fragment shader";
    JLabel label = new JLabel(caption);
    add(label);

    // Display current filename
    buttonFilename = new JButton(getShortFilename(shader.getShaderSourceFilename(type)));
    buttonFilename.addActionListener(this);
    buttonFilename.setActionCommand(ACTION_COMMAND_FILENAME);
    add(buttonFilename);

    // Text area with shader source
    textAreaXSource = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textAreaXSource);
    String filename = shader.getShaderSourceFilename(type);
    textAreaXSource.setText(CgGlslShader.readShaderSource(filename));
    add(scrollPane);

    // Buttons: Compile and Save
    JPanel panelButtons = new JPanel();
    panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
    add(panelButtons);

    JButton buttonCompile = new JButton("Compile");
    buttonCompile.addActionListener(this);
    buttonCompile.setActionCommand(ACTION_COMMAND_COMPILE);
    panelButtons.add(buttonCompile);

    JButton buttonSave = new JButton("Save");
    buttonSave.addActionListener(this);
    buttonSave.setActionCommand(ACTION_COMMAND_SAVE);
    panelButtons.add(buttonSave);

  }

  /**
   * Return a shortened version of the filename.
   */
  private String getShortFilename(String shaderSourceFilename) {
    int MAX_LENGTH = 40;
    if (shaderSourceFilename.length() > MAX_LENGTH) {
      return shaderSourceFilename.substring(shaderSourceFilename.length() - MAX_LENGTH);
    }
    return null;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_COMMAND_COMPILE)) {
      if (compiler == null) {
        Logger.getInstance().error("No shader compiler set!");
      } else {
        compiler.compile(JoglShader.getGlShaderType(type), textAreaXSource.getText());
      }
    } else if (e.getActionCommand().equals(ACTION_COMMAND_SAVE)) {
      saveSourceToFile();
    } else if (e.getActionCommand().equals(ACTION_COMMAND_FILENAME)) {
      selectShaderFilename();
    }
  }

  private void selectShaderFilename() {
    // Let user choose file
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      String absoluteFilename = fileChooser.getSelectedFile().getAbsolutePath();
      if (type == CgGlslShader.ShaderType.VERTEX) {
        shader.setVertexShaderFilename(absoluteFilename);
      } else {
        shader.setFragmentShaderFilename(absoluteFilename);
      }
      buttonFilename.setText(getShortFilename(shader.getShaderSourceFilename(type)));
    }
  }

  private void saveSourceToFile() {
    // Try to find existing file
    String absoluteFilename = ResourcesLocator.getInstance().getPathToResource(shader.getShaderSourceFilename(type));
    if (absoluteFilename == null) {
      // Let user choose file
      JFileChooser fileChooser = new JFileChooser();
      if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        absoluteFilename = fileChooser.getSelectedFile().getAbsolutePath();
      }
    }
    if (absoluteFilename != null) {
      CgGlslShader.saveShader(textAreaXSource.getText(), absoluteFilename);
      shader.setCompiled(false);
    }
  }

}

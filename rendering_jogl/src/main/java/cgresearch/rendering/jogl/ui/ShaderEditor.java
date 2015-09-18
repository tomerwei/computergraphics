package cgresearch.rendering.jogl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.IGlslShaderCompiler;

/**
 * Editor for a shader. Implements the Renderable interface to get access to the
 * OpenGL context.
 * 
 * @author Philipp Jenke
 *
 */
public class ShaderEditor extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5718050432771001955L;

	/**
	 * Constants
	 */
	private static final String ACTION_COMMAND_OK = "ACTION_COMMAND_OK";

	/**
	 * References to the shader panels.
	 */
	private final ShaderEditorPanel vertexShaderPanel;
	private final ShaderEditorPanel fragmentShaderPanel;

	/**
	 * Constructor.
	 */
  public ShaderEditor(CgGlslShader shader, IGlslShaderCompiler compiler) {
		super("Shader Editor");

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel panelEditors = new JPanel();
		panelEditors.setLayout(new BoxLayout(panelEditors, BoxLayout.X_AXIS));
		getContentPane().add(panelEditors);

		vertexShaderPanel = new ShaderEditorPanel(shader,
				CgGlslShader.ShaderType.VERTEX, compiler);
		panelEditors.add(vertexShaderPanel);
		fragmentShaderPanel = new ShaderEditorPanel(shader,
				CgGlslShader.ShaderType.FRAGMENT, compiler);
		panelEditors.add(fragmentShaderPanel);

		JButton buttonOk = new JButton("Close");
		buttonOk.setActionCommand(ACTION_COMMAND_OK);
		buttonOk.addActionListener(this);
		getContentPane().add(buttonOk);

		setLocation(800, 0);
		setSize(500, 500);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_OK)) {
			setVisible(false);
		}
	}
}

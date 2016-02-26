package cgresearch.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.lights.LightEditor;
import cgresearch.ui.resources.ResourceManagerEditor;

/**
 * Menu for the resources.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourcesMenu extends CgApplicationMenu implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7335296098660392166L;

	/**
	 * Reference to the shader editor.
	 */
	private final ResourceManagerEditor shaderEditor;

	/**
	 * Action commands
	 */
	private static final String ACTION_COMMAND_SHADER = "ACTION_COMMAND_SHADER";
	private static final String ACTION_COMMAND_TEXTURE = "ACTION_COMMAND_TEXTURE";
	private static final String ACTION_COMMAND_LIGHT = "ACTION_COMMAND_LIGHT";

	private final LightEditor lightEditor;

	/**
	 * Constructor
	 */
	public ResourcesMenu(ResourceManagerEditor shaderEditor, CgRootNode rootNode) {
		super("Rendering");
		this.shaderEditor = shaderEditor;
		lightEditor = new LightEditor(rootNode);

		JMenuItem menuShader = new JMenuItem("Shader");
		add(menuShader);
		menuShader.addActionListener(this);
		menuShader.setActionCommand(ACTION_COMMAND_SHADER);

		JMenuItem menuTexture = new JMenuItem("Texture");
		add(menuTexture);
		menuTexture.addActionListener(this);
		menuTexture.setActionCommand(ACTION_COMMAND_TEXTURE);

		JMenuItem menuLight = new JMenuItem("Light");
		add(menuLight);
		menuLight.addActionListener(this);
		menuLight.setActionCommand(ACTION_COMMAND_LIGHT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_SHADER)) {
			shaderEditor.setVisible(true);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_TEXTURE)) {
			ResourceManagerEditor textureEditor = new ResourceManagerEditor(
					ResourceManager.getTextureManagerInstance(), "Textures");
			textureEditor.setVisible(true);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_LIGHT)) {
			lightEditor.setVisible(true);
		}

	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui.scenegraph;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.GenericManager;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Info and edit dialog for a Material object.
 * 
 * @author Philipp Jenke
 */
public class InfoEditMaterial extends InfoEditDialog implements ActionListener,
		Observer {

	/**
     * 
     */
	private static final long serialVersionUID = -7236596434608267762L;

	/**
	 * Action commands.
	 */
	private static String ACTION_SOPHISTICATED_RENDERING = "ACTION_SOPHISTICATED_RENDERING";
	private static String ACTION_RENDER_MODE = "ACTION_RENDER_MODE";
	private static String ACTION_REFLECTION_AMBIENT = "ACTION_REFLECTION_AMBIENT";
	private static String ACTION_REFLECTION_DIFFUSE = "ACTION_REFLECTION_DIFFUSE";
	private static String ACTION_REFLECTION_SPECULAR = "ACTION_REFLECTION_SPECULAR";
	private static String ACTION_TEXTURE = "ACTION_TEXTURE";
	private static String ACTION_SHADER = "ACTION_VERTEX_SHADER";
	private static String ACTION_SHININESS = "ACTION_SHININESS";
	private static String ACTION_SHADOW = "ACTION_SHADOW";

	/**
	 * Reference to the material object.
	 */
	private final CgNode node;

	/**
	 * Texture selection by id
	 */
	private JComboBox<String> comboBoxTexture = new JComboBox<String>();

	/**
	 * Diffuse reflection
	 */
	private JButton buttonReflectionDiffuse = new JButton();

	/**
	 * Ambient reflection
	 */
	private JButton buttonReflectionAmbient = new JButton();

	/**
	 * Specular reflection
	 */
	private JButton buttonReflectionSpecular = new JButton();

	/**
	 * This label represents the vertex shader filename
	 */
	private JComboBox<String> comboBoxShader = new JComboBox<String>();

	/**
	 * This label represents the render mode
	 */
	private JComboBox<String> comboRenderMode = new JComboBox<String>();

	/**
	 * This label represents the state of the sophisticated render mode.
	 */
	private JCheckBox checkBoxSophisticatedRendering = new JCheckBox(
			"Edge Highlighting");

	/**
	 * Shininess of the specular component.
	 */
	private JTextField textShininess = new JTextField("");

	/**
	 * This label represents whether the object throws a shadow
	 */
	private JCheckBox checkBoxThrowsShadow = new JCheckBox("Throws ShadowType");

	/**
	 * Constructor.
	 */
	public InfoEditMaterial(JScrollPane parent, CgNode node) {
		super(parent);
		this.node = node;

		if (node != null && node.getContent() != null
				&& node.getContent().getMaterial() != null) {
			node.getContent().getMaterial().addObserver(this);
		}

		createGui();
	}

	/**
	 * Create the GUI components
	 */
	private void createGui() {

		// Default initialization of the components
		GenericManager<CgTexture> textureManager = ResourceManager
				.getTextureManagerInstance();
		for (int i = 0; i < textureManager.getNumberOfResources(); i++) {
			comboBoxTexture.addItem(textureManager.getKey(i));
		}
		GenericManager<CgGlslShader> shaderManager = ResourceManager
				.getShaderManagerInstance();
		for (int i = 0; i < shaderManager.getNumberOfResources(); i++) {
			comboBoxShader.addItem(shaderManager.getKey(i));
		}
		buttonReflectionDiffuse.setText("");
		checkBoxSophisticatedRendering.setSelected(false);
		for (Material.Normals renderMode : Material.Normals.values()) {
			comboRenderMode.addItem(renderMode.toString());
		}


		// Register events
		checkBoxSophisticatedRendering
				.setActionCommand(ACTION_SOPHISTICATED_RENDERING);
		comboRenderMode.setActionCommand(ACTION_RENDER_MODE);
		buttonReflectionAmbient.setActionCommand(ACTION_REFLECTION_AMBIENT);
		buttonReflectionDiffuse.setActionCommand(ACTION_REFLECTION_DIFFUSE);
		buttonReflectionSpecular.setActionCommand(ACTION_REFLECTION_SPECULAR);
		comboBoxTexture.setActionCommand(ACTION_TEXTURE);
		comboBoxShader.setActionCommand(ACTION_SHADER);
		textShininess.setActionCommand(ACTION_SHININESS);
		checkBoxThrowsShadow.setActionCommand(ACTION_SHADOW);

		setLabels();

		// Register listener
		checkBoxSophisticatedRendering.addActionListener(this);
		comboRenderMode.addActionListener(this);
		buttonReflectionAmbient.addActionListener(this);
		buttonReflectionDiffuse.addActionListener(this);
		buttonReflectionSpecular.addActionListener(this);
		comboBoxTexture.addActionListener(this);
		comboBoxShader.addActionListener(this);
		textShininess.addActionListener(this);
		checkBoxThrowsShadow.addActionListener(this);

		// Arrange components on GUI
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new GridLayout(12, 2));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(new JLabel("Texture: "));
		panel.add(comboBoxTexture);

		panel.add(new JLabel("Ambient Reflection: "));
		panel.add(buttonReflectionAmbient);

		panel.add(new JLabel("Diffuse Reflection: "));
		panel.add(buttonReflectionDiffuse);

		panel.add(new JLabel("Specular Reflection: "));
		panel.add(buttonReflectionSpecular);

		panel.add(new JLabel("Shader: "));
		panel.add(comboBoxShader);

		panel.add(new JLabel("Render Mode: "));
		panel.add(comboRenderMode);

		panel.add(new JLabel("Edge Highlighting: "));
		panel.add(checkBoxSophisticatedRendering);

		panel.add(new JLabel("Shininess: "));
		panel.add(textShininess);

		panel.add(new JLabel("Throws ShadowType: "));
		panel.add(checkBoxThrowsShadow);
	}

	/**
     * 
     */
	private void setLabels() {
		if (node == null || node.getContent() == null
				|| node.getContent().getMaterial() == null) {
			return;
		}
		Material material = node.getContent().getMaterial();

		// Set the values from a material object
		comboBoxTexture.setSelectedItem(material.getTextureId());
		if (material.hasShader()) {
			comboBoxShader.setSelectedItem(material.getShaderId(0));
		} else {
			comboBoxShader.setSelectedItem("");
		}
		buttonReflectionAmbient.setText(material.getReflectionAmbient()
				.toString());
		buttonReflectionDiffuse.setText(material.getReflectionDiffuse()
				.toString());
		buttonReflectionSpecular.setText(material.getReflectionSpecular()
				.toString());
		comboRenderMode.setSelectedItem(material.getRenderMode().toString());
		checkBoxSophisticatedRendering.setSelected(material
				.isShowSophisticatesMesh());
		textShininess.setText("" + material.getSpecularShininess());
		checkBoxThrowsShadow.setSelected(material.isThrowingShadow());
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (node == null || node.getContent() == null
				|| node.getContent().getMaterial() == null) {
			return;
		}
		Material material = node.getContent().getMaterial();

		if (e.getActionCommand().equals(ACTION_SOPHISTICATED_RENDERING)) {
			material.setShowSophisticatesMesh(checkBoxSophisticatedRendering
					.isSelected());
			node.getContent().updateRenderStructures();
		} else if (e.getActionCommand().equals(ACTION_RENDER_MODE)) {
			String renderModeString = (String) (comboRenderMode
					.getSelectedItem());
			Material.Normals renderMode = Material.Normals
					.valueOf(renderModeString);
			material.setRenderMode(renderMode);
			node.getContent().updateRenderStructures();
		} else if (e.getActionCommand().equals(ACTION_REFLECTION_AMBIENT)) {
			IVector3 c = material.getReflectionAmbient();
			Color color = JColorChooser.showDialog(null,
					"Select ambient reflection", new Color((float) c.get(0),
							(float) c.get(1), (float) c.get(2)));
			if (color != null) {
				material.setReflectionAmbient(VectorMatrixFactory.newIVector3(
						color.getRed() / 255.0, color.getGreen() / 255.0,
						color.getBlue() / 255.0));
			}
			buttonReflectionAmbient.setText(material.getReflectionAmbient()
					.toString());
			node.getContent().updateRenderStructures();
		} else if (e.getActionCommand().equals(ACTION_REFLECTION_DIFFUSE)) {
			IVector3 c = material.getReflectionDiffuse();
			Color color = JColorChooser.showDialog(null,
					"Select diffuse reflection", new Color((float) c.get(0),
							(float) c.get(1), (float) c.get(2)));
			if (color != null) {
				material.setReflectionDiffuse(VectorMatrixFactory.newIVector3(
						color.getRed() / 255.0, color.getGreen() / 255.0,
						color.getBlue() / 255.0));
			}
			buttonReflectionDiffuse.setText(material.getReflectionDiffuse()
					.toString());
			node.getContent().updateRenderStructures();
		} else if (e.getActionCommand().equals(ACTION_REFLECTION_SPECULAR)) {
			IVector3 c = material.getReflectionSpecular();
			Color color = JColorChooser.showDialog(null,
					"Select specular reflection", new Color((float) c.get(0),
							(float) c.get(1), (float) c.get(2)));
			if (color != null) {
				material.setReflectionSpecular(VectorMatrixFactory.newIVector3(
						color.getRed() / 255.0, color.getGreen() / 255.0,
						color.getBlue() / 255.0));
			}
			buttonReflectionSpecular.setText(material.getReflectionSpecular()
					.toString());
			node.getContent().updateRenderStructures();
		} else if (e.getActionCommand().equals(ACTION_TEXTURE)) {
			String id = (String) comboBoxTexture.getSelectedItem();
			if (id != null && id.length() > 0) {
				material.setTextureId(id);
				node.getContent().updateRenderStructures();
			}
		} else if (e.getActionCommand().equals(ACTION_SHADER)) {
			String id = (String) comboBoxShader.getSelectedItem();
			if (id != null && id.length() > 0) {
				material.setShaderId(id);
				node.getContent().updateRenderStructures();
			}
		} else if (e.getActionCommand().equals(ACTION_SHININESS)) {
			try {
				float shininess = Float.parseFloat(textShininess.getText());
				material.setSpecularShininess(shininess);
			} catch (Exception ex) {
			}
		} else if (e.getActionCommand().equals(ACTION_SHADOW)) {
			material.setThrowsShadow(checkBoxThrowsShadow.isSelected());
			node.getContent().updateRenderStructures();
		}
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(java.util.Observable o, Object arg) {
		// Update the labels if the material object changed.
		setLabels();
	}

}

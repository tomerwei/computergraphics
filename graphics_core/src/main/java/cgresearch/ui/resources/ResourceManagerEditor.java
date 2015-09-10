package cgresearch.ui.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import cgresearch.graphics.material.CgGlslShader;
import cgresearch.graphics.material.GenericManager;

/**
 * Generic editor for resource managers. Implements the JoglRenderable interface
 * to get access to the GL context.
 * 
 * @author Philipp Jenke
 *
 */
public class ResourceManagerEditor extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3454679676513065909L;

	/**
	 * Constants
	 */
	private static final String ACTION_COMMAND_OK = "ACTION_COMMAND_OK";

	/**
	 * Constructor
	 */
	public ResourceManagerEditor(final GenericManager<?> resourceManager,
			String title) {
		this(resourceManager, title, null);
	}

	/**
	 * Constructor
	 */
	public ResourceManagerEditor(final GenericManager<?> resourceManager,
			String title, final IGlslShaderCompiler glslCompiler) {
		super(title);

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JTable table = new JTable(
				new ResourceManagerTableModel(resourceManager));
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		getContentPane().add(scrollPane);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					Object resource = resourceManager
							.getResource(resourceManager.getKey(row));
					if (resource instanceof CgGlslShader) {
						new ShaderEditor((CgGlslShader) resource, glslCompiler);
					}
				}
			}
		});

		JButton buttonOk = new JButton("Close");
		buttonOk.setActionCommand(ACTION_COMMAND_OK);
		buttonOk.addActionListener(this);
		getContentPane().add(buttonOk);

		setLocation(600, 0);
		setSize(300, 300);
		setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_OK)) {
			setVisible(false);
		}
	}
}

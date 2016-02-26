package cgresearch.ui.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
   * Mapping between resource class name and lambda to handle resource object.
   */
  private Map<String, ResourceEditor> resourceEditorMap = new HashMap<String, ResourceEditor>();

  /**
   * Constructor
   */
  public ResourceManagerEditor(final GenericManager<?> resourceManager, String title) {
    super(title);

    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

    JTable table = new JTable(new ResourceManagerTableModel(resourceManager));
    JScrollPane scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);
    getContentPane().add(scrollPane);

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          JTable target = (JTable) e.getSource();
          int row = target.getSelectedRow();
          Object resource = resourceManager.getResource(resourceManager.getKey(row));
          if (resourceEditorMap.get(resource.getClass().getName()) != null) {
            resourceEditorMap.get(resource.getClass().getName()).apply(resource);
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

  /**
   * Register a resource editor for a specified class.
   * 
   * @param className
   *          Class name.
   * @param editor
   *          Editor instance.
   */
  public void addResourceEditor(String className, ResourceEditor editor) {
    resourceEditorMap.put(className, editor);
  }
}

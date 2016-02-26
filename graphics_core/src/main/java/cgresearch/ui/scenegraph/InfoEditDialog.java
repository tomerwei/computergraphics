/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui.scenegraph;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Generic class as parent for all info/edit dialogs.
 * 
 * @author Philipp Jenke
 * 
 */
public abstract class InfoEditDialog extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -639521463222932568L;

    /**
     * Reference to the parent scroll pane.
     */
    protected final JScrollPane parent;

    /**
     * Constructor.
     * 
     * @param parent
     */
    public InfoEditDialog(JScrollPane parent) {
        this.parent = parent;
        parent.setViewportView(this);

    }

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.gui;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import cgresearch.core.logging.Logger;

/**
 * Specialized version of a label to display a connection state.
 *
 * @author Philipp Jenke
 *
 */
public class ConnectionLabel extends JLabel {

    /**
     *
     */
    private static final long serialVersionUID = 2952250927619355055L;

    /**
     * This icon is used to indicate the connected state.
     */
    private final Icon iconConnected;
    /**
     * This icon is used to indicate the disconnected state.
     */
    private final Icon iconDisconnected;

    /**
     * Filename of the connected filename.
     */
    private static final String ICON_CONNECTED_FILENAME = "icons/connected.png";

    /**
     * Filename of the disconnected filename.
     */
    private static final String ICON_DISCONNECTED_FILENAME = "icons/disconnected.png";

    /**
     * Constructor
     */
    public ConnectionLabel(String text) {

        if (!(new File(ICON_CONNECTED_FILENAME).exists())) {
            Logger.getInstance().message(
                    "Icon " + ICON_CONNECTED_FILENAME + " not found.");
        }
        if (!(new File(ICON_DISCONNECTED_FILENAME).exists())) {
            Logger.getInstance().message(
                    "Icon " + ICON_DISCONNECTED_FILENAME + " not found.");
        }

        iconConnected = new ImageIcon(ICON_CONNECTED_FILENAME, "connected");
        iconDisconnected = new ImageIcon(ICON_DISCONNECTED_FILENAME,
                "disconnected");

        setText(text + " ");
        setIcon(iconConnected);
        setHorizontalTextPosition(JLabel.LEFT);
    }

    /**
     * Sets the state connected/disconnect.
     *
     * @param connected
     *            New state.
     */
    public void setSelected(boolean connected) {
        if (connected) {
            setIcon(iconConnected);
        } else {
            setIcon(iconDisconnected);
        }
    }

}

package cgresearch.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cgresearch.graphics.scenegraph.IconLoader;

/**
 * Displays information about the software
 * 
 * @author Philipp Jenke
 *
 */
public class HelpMenu extends CgApplicationMenu implements ActionListener {

	/**
	 * Action command.
	 */

	private static final String ACTION_COMMAND_ABOUT = "ACTION_COMMAND_ABOUT";

	/**
	 * Filename of the logo image file.
	 */
	private static final String logoFilename = "icons/logo_haw_hamburg.png";

	/**
	 * Constructor.
	 */
	public HelpMenu() {
		super("Help");

		// About
		JMenuItem itemAbout = new JMenuItem("About");
		itemAbout.addActionListener(this);
		itemAbout.setActionCommand(ACTION_COMMAND_ABOUT);
		add(itemAbout);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_ABOUT)) {
			Icon icon = IconLoader.getIcon(logoFilename);
			JOptionPane.showMessageDialog(null, "<html><body>"
					+ "Computer Graphics Lab Research Software<br>"
					+ "Hochschule für Angewandte Wissenschaften Hamburg<br>"
					+ "University of Applied Sciences Hamburg<br>"
					+ "Prof. Philipp Jenke<br><br>"
					+ "<b>Contributers:</b><br>" + "Chris Marquardt"
					+ "</body></html>", "About",
					JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}
}

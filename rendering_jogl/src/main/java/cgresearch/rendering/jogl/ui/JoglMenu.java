/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jogl.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import cgresearch.core.logging.Logger;
import cgresearch.ui.menu.CgApplicationMenu;

/**
 * Menu for the JOGL rendering system.
 * 
 * @author Philipp Jenke
 * 
 */
public class JoglMenu extends CgApplicationMenu implements ActionListener {

	/**
	 * Constants
	 */
	private static final long serialVersionUID = 2288252429432335819L;
	private static final String ACTION_COMMAND_MOVIE_EXPORT = "ACTION_COMMAND_MOVIE_EXPORT";
	private static final String ACTION_COMMAND_CAMERA = "ACTION_COMMAND_CAMERA";

	/**
	 * Constructor.
	 */
	public JoglMenu() {
		super("Jogl");

		JMenuItem menuItemTakeScreenshot = new JMenuItem("Take Screenshot");
		menuItemTakeScreenshot.addActionListener(this);
		menuItemTakeScreenshot
				.setActionCommand(JoglFrame.ActionCommands.ACTION_COMMAND_TAKE_SCREENSHOT
						.toString());
		add(menuItemTakeScreenshot);

		JMenuItem menuMovieExport = new JMenuItem("Movie Export");
		add(menuMovieExport);
		menuMovieExport.addActionListener(this);
		menuMovieExport.setActionCommand(ACTION_COMMAND_MOVIE_EXPORT);

		JMenuItem menuCamera = new JMenuItem("Camera");
		add(menuCamera);
		menuCamera.addActionListener(this);
		menuCamera.setActionCommand(ACTION_COMMAND_CAMERA);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_MOVIE_EXPORT)) {
			new MovieExportWidget();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_CAMERA)) {
			new CameraSettingsWidget();
		} else if (e.getActionCommand().equals(
				JoglFrame.ActionCommands.ACTION_COMMAND_TAKE_SCREENSHOT
						.toString())) {
			Logger.getInstance().error(
					"Screenshot export currently not supported.");
		}
	}

}

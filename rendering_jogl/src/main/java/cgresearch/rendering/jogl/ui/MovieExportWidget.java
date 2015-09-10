package cgresearch.rendering.jogl.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.graphics.misc.MovieExport;

public class MovieExportWidget extends JFrame implements ActionListener {

	/**
	 * Constants
	 */
	private static final long serialVersionUID = 1L;
	private static final String ACTION_COMMAND_START = "ACTION_COMMAND_START";
	private static final String ACTION_COMMAND_STOP = "ACTION_COMMAND_STOP";

	/**
	 * Constructor.
	 */
	public MovieExportWidget() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		add(panel);

		// Start
		JButton buttonStart = new JButton("Start recording");
		buttonStart.setActionCommand(ACTION_COMMAND_START);
		buttonStart.addActionListener(this);
		panel.add(buttonStart);

		// Stop
		JButton buttonStop = new JButton("Stop recording");
		buttonStop.setActionCommand(ACTION_COMMAND_STOP);
		buttonStop.addActionListener(this);
		panel.add(buttonStop);

		// Export path
		JLabel labelExportPath = new JLabel("Export path:");
		JTextField textFieldExportPath = new JTextField(MovieExport
				.getInstance().getExportPath());
		panel.add(labelExportPath);
		panel.add(textFieldExportPath);

		// Frame base filename
		JLabel labelFrameBaseFilename = new JLabel("Frame base filename:");
		JTextField textFieldFrameBaseFilename = new JTextField(MovieExport
				.getInstance().getFrameBaseFilename());
		panel.add(labelFrameBaseFilename);
		panel.add(textFieldFrameBaseFilename);

		setSize(400, 200);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_START)) {
			MovieExport.getInstance().start();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_STOP)) {
			MovieExport.getInstance().stop();
		}
	}

}

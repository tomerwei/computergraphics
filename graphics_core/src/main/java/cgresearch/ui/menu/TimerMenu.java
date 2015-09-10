package cgresearch.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.misc.AnimationTimer;

public class TimerMenu extends CgApplicationMenu implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action commands
	 */
	private static final String ACTION_COMMMAND_RUN = "ACTION_COMMMAND_RUN";
	private static final String ACTION_COMMMAND_SETTINGS = "ACTION_COMMMAND_SETTINGS";

	/**
	 * Constructor.
	 */
	public TimerMenu() {
		super("Timer");

		JMenuItem itemRun = new JMenuItem("Toggle Run/Stop");
		itemRun.addActionListener(this);
		itemRun.setActionCommand(ACTION_COMMMAND_RUN);
		add(itemRun);

		JMenuItem itemSettings = new JMenuItem("Settings");
		itemSettings.addActionListener(this);
		itemSettings.setActionCommand(ACTION_COMMMAND_SETTINGS);
		add(itemSettings);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMMAND_RUN)) {
			AnimationTimer timer = AnimationTimer.getInstance();
			if (timer.isRunning()) {
				timer.stop();
			} else {
				String input = JOptionPane
						.showInputDialog("Please enter timeout interval");
				if (input == null) {
					return;
				}
				int timeoutInterval = 0;
				try {
					timeoutInterval = Integer.parseInt(input);
				} catch (NumberFormatException nfe) {
					Logger.getInstance().exception("Invalid timeout interval",
							nfe);
					return;
				}
				timer.startTimer(timeoutInterval);
			}
		} else {
			if (e.getActionCommand().equals(ACTION_COMMMAND_SETTINGS)) {
				AnimationTimerSettingsWidget settingsWidget = new AnimationTimerSettingsWidget();
				settingsWidget.setVisible(true);
			}
		}
	}

}

package cgresearch.ui.menu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.misc.AnimationTimer;

/**
 * Settings of the animation timer
 */
public class AnimationTimerSettingsWidget extends JFrame implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action commands
	 */
	private static final String ACTION_COMMAND_OK = "ACTION_COMMAND_OK";

	/**
	 * References to the GUI components.
	 */
	private JTextField textFieldMin;
	private JTextField textFieldMax;

	/**
	 * Constructor
	 */
	public AnimationTimerSettingsWidget() {
		AnimationTimer timer = AnimationTimer.getInstance();

		JPanel panel = new JPanel();
		GridLayout gridLayout = new GridLayout(3, 2);
		panel.setLayout(gridLayout);
		add(panel);

		JLabel labelMin = new JLabel("Minimum value:");
		panel.add(labelMin);
		textFieldMin = new JTextField("" + timer.getMinValue());
		panel.add(textFieldMin);

		JLabel labelMax = new JLabel("Maximum value:");
		panel.add(labelMax);
		textFieldMax = new JTextField("" + timer.getMaxValue());
		panel.add(textFieldMax);

		JButton buttonOk = new JButton("Ok!");
		buttonOk.addActionListener(this);
		buttonOk.setActionCommand(ACTION_COMMAND_OK);
		panel.add(buttonOk);

		setSize(150, 100);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_OK)) {
			try {
				int minValue = Integer.parseInt(textFieldMin.getText());
				int maxValue = Integer.parseInt(textFieldMax.getText());
				AnimationTimer timer = AnimationTimer.getInstance();
				timer.setMinValue(minValue);
				timer.setMaxValue(maxValue);
			} catch (NumberFormatException nfe) {
				Logger.getInstance().exception("Failed to parse integer value",
						nfe);
			} finally {
				setVisible(false);
			}

		}
	}
}

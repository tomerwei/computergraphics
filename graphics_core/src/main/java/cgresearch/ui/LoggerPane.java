/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import cgresearch.core.logging.Logger;

/**
 * Frame for the logger messages.
 * 
 * @author Philipp Jenke
 * 
 */
public class LoggerPane extends JPanel implements Observer {

	/**
     * 
     */
	private static final long serialVersionUID = 6826624003987600148L;

	/**
	 * Text is displayed in this text field.
	 */
	private JTextArea textField = new JTextArea();

	/**
	 * Constructor.
	 */
	public LoggerPane() {
		// JScrollPane scrollPane = new JScrollPane(textField);
		add(textField);
		// setVisible(true);
		Logger.getInstance().addObserver(this);
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Logger && arg instanceof String) {
			final String t = (String) arg;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					textField.setText(textField.getText() + t + "\n");
				}
			});
		}
	}

}

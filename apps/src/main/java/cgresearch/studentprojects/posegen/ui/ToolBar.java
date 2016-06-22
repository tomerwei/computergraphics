package cgresearch.studentprojects.posegen.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import cgresearch.core.logging.Logger;

public class ToolBar extends JToolBar implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private HashMap<String, ToggleButtonIcon> actionCommandMap = new HashMap<>();
	private HashMap<String, JButton> buttonMap = new HashMap<>();
	private Integer indexCounter = 0; // Used to create custom ActionCommands
										// for each Button

	public void addIcon(ToggleButtonIcon customMouseInteractionButton) {
		JButton button = new JButton();
		button.setIcon(customMouseInteractionButton.getCurrentIcon());
		String actionCommand = indexCounter.toString(); // Create an
														// actionCommand for
														// this button
		button.setActionCommand(actionCommand);
		indexCounter++;
		button.addActionListener(this);
		this.actionCommandMap.put(actionCommand, customMouseInteractionButton);
		buttonMap.put(actionCommand, button);

		add(button);
	}

	public void setUpButtons() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Constructor
	 */
	public ToolBar() {

		setUpButtons();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommandClicked = e.getActionCommand();
		ToggleButtonIcon button = actionCommandMap.get(actionCommandClicked);
		button.clicked(); // Call before getting new icon, to update
		buttonMap.get(actionCommandClicked).setIcon(button.getCurrentIcon());
		this.updateUI();

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		Logger.getInstance().message("QuickOptionToolBar - key");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		Logger.getInstance().message("QuickOptionToolBar - key");
	}
}

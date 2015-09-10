package cgresearch.apps.hlsvis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import cgresearch.ui.IApplicationControllerGui;

public class HlsVIsGui extends IApplicationControllerGui implements
		ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public HlsVIsGui(TimeDisplay timeDisplay) {
		add(timeDisplay);
	}

	@Override
	public String getName() {
		return "HLS Vis";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

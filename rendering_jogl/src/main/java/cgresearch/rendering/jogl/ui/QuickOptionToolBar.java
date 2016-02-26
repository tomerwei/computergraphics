package cgresearch.rendering.jogl.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.picking.Picking;
import cgresearch.graphics.scenegraph.IconLoader;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class QuickOptionToolBar extends JToolBar implements ActionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action command constants
	 */
	private final String ACTION_COMMAND_CENTER = "ACTION_COMMAND_CENTER";
	private final String ACTION_COMMAND_PLAY = "ACTION_COMMAND_PLAY";
	private final String ACTION_COMMAND_STOP = "ACTION_COMMAND_STOP";
	private final String ACTION_COMMAND_ZOOM_IN = "ACTION_COMMAND_ZOOM_IN";
	private final String ACTION_COMMAND_ZOOM_OUT = "ACTION_COMMAND_ZOOM_OUT";
	private final String ACTION_COMMAND_PICKING = "ACTION_COMMAND_PICKING";

	/**
	 * Icon filenames
	 */
	private final String ICON_CENTER = "icons/center.png";
	private final String ICON_PLAY = "icons/play.png";
	private final String ICON_STOP = "icons/stop.png";
	private final String ICON_ZOOM_IN = "icons/zoom_in.png";
	private final String ICON_ZOOM_OUT = "icons/zoom_out.png";
	private final String ICON_PICKING = "icons/picking.png";
	private final String ICON_PICKING_ACTIVE = "icons/picking_active.png";

	/**
	 * GUI components
	 */
	private JTextField textFieldAnimation;

	/**
	 * Toggle icons for picking
	 */
	private ImageIcon iconPicking, iconPickingActive;
	private JButton buttonPicking;

	/**
	 * Constructor
	 */
	public QuickOptionToolBar() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Center view
		ImageIcon iconCenter = IconLoader.getIcon(ICON_CENTER);
		JButton buttonCenter = new JButton();
		buttonCenter.setIcon(iconCenter);
		buttonCenter.setActionCommand(ACTION_COMMAND_CENTER);
		buttonCenter.addActionListener(this);
		add(buttonCenter);

		// Zoom in
		ImageIcon iconZoomIn = IconLoader.getIcon(ICON_ZOOM_IN);
		JButton buttonZoomIn = new JButton();
		buttonZoomIn.setIcon(iconZoomIn);
		buttonZoomIn.setActionCommand(ACTION_COMMAND_ZOOM_IN);
		buttonZoomIn.addActionListener(this);
		add(buttonZoomIn);

		// Zoom out
		ImageIcon iconZoomOut = IconLoader.getIcon(ICON_ZOOM_OUT);
		JButton buttonZoomOut = new JButton();
		buttonZoomOut.setIcon(iconZoomOut);
		buttonZoomOut.setActionCommand(ACTION_COMMAND_ZOOM_OUT);
		buttonZoomOut.addActionListener(this);
		add(buttonZoomOut);

		// Timer interval
		textFieldAnimation = new JTextField();
		textFieldAnimation.setText("30");
		textFieldAnimation.setMaximumSize(new Dimension(60, 40));
		add(textFieldAnimation);

		// Timer start
		ImageIcon iconPlay = IconLoader.getIcon(ICON_PLAY);
		JButton buttonPlay = new JButton();
		buttonPlay.setIcon(iconPlay);
		buttonPlay.setActionCommand(ACTION_COMMAND_PLAY);
		buttonPlay.addActionListener(this);
		add(buttonPlay);

		// Timer stop
		ImageIcon iconStop = IconLoader.getIcon(ICON_STOP);
		JButton buttonStop = new JButton();
		buttonStop.setIcon(iconStop);
		buttonStop.setActionCommand(ACTION_COMMAND_STOP);
		buttonStop.addActionListener(this);
		add(buttonStop);

		// Picking
		iconPicking = IconLoader.getIcon(ICON_PICKING);
		iconPickingActive = IconLoader.getIcon(ICON_PICKING_ACTIVE);
		buttonPicking = new JButton();
		buttonPicking.setIcon(iconPicking);
		buttonPicking.setActionCommand(ACTION_COMMAND_PICKING);
		buttonPicking.addActionListener(this);
		add(buttonPicking);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_CENTER)) {
			Camera.getInstance().setCenterViewRequired();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_PLAY)) {
			int interval = Integer.parseInt(textFieldAnimation.getText());
			AnimationTimer.getInstance().startTimer(interval);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_STOP)) {
			AnimationTimer.getInstance().stop();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_ZOOM_IN)) {
			Camera.getInstance().zoom(0.05);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_ZOOM_OUT)) {
			Camera.getInstance().zoom(-0.05);
		} else if (e.getActionCommand().equals(ACTION_COMMAND_PICKING)) {
			if (Picking.getInstance().isActive()) {
				buttonPicking.setIcon(iconPicking);
				Picking.getInstance().setActive(false);
			} else {
				buttonPicking.setIcon(iconPickingActive);
				Picking.getInstance().setActive(true);
			}
		}
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

package cgresearch.ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.graphics.misc.AnimationTimer;

/**
 * GUI panel for the timer pane.
 *
 */
public class AnimationTimerPanel extends JPanel implements Observer,
		ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Label displaying the current time.
	 */
	private JLabel label;

	/**
	 * Slider for the time state.
	 */
	private JSlider slider;

	/**
	 * Constructor.
	 */
	public AnimationTimerPanel() {
		slider = new JSlider();
		slider.setMinimum((int) AnimationTimer.getInstance().getMinValue());
		slider.setMaximum((int) AnimationTimer.getInstance().getMaxValue());
		slider.setValue((int) AnimationTimer.getInstance().getValue());
		slider.addChangeListener(this);

		label = new JLabel("" + AnimationTimer.getInstance().getValue());
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(slider);
		add(label);
		AnimationTimer.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof AnimationTimer) {
			AnimationTimer timer = AnimationTimer.getInstance();
			label.setText("" + timer.getValue());
			slider.setMinimum((int) (timer.getMinValue()));
			slider.setMaximum((int) (timer.getMaxValue()));
			slider.setValue((int) (timer.getValue()));
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == slider) {
			AnimationTimer.getInstance().setValue(slider.getValue());
		}
	}
}

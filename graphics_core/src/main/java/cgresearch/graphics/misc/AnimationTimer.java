package cgresearch.graphics.misc;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Central timing instance of the scene.
 * 
 * @author Philipp Jenke
 *
 */
public class AnimationTimer extends Observable {

	/**
	 * Current time.
	 */
	private long value = 0;

	/**
	 * Smallest timer value
	 */
	private long minValue = 0;

	/**
	 * The time is reset, when this time is reached.
	 */
	private long maxValue = 10;

	/**
	 * Timer object for a running timer.
	 */
	private Timer timer = null;

	/**
	 * Flag to indicate if the timer task is running.
	 */
	private boolean isRunning = false;

	/**
	 * The timer is implemented using the singleton pattern.
	 */
	private static AnimationTimer instance = null;

	/**
	 * Constructor.
	 */
	private AnimationTimer() {
		value = minValue;
	}

	/**
	 * Get the singleton instance.
	 */
	public static AnimationTimer getInstance() {
		if (instance == null) {
			instance = new AnimationTimer();
		}
		return instance;
	}

	/**
	 * Getter.
	 */
	public long getMinValue() {
		return minValue;
	}

	/**
	 * Getter.
	 */
	public long getMaxValue() {
		return maxValue;
	}

	/**
	 * Getter
	 */
	public synchronized long getValue() {
		return value;
	}

	/**
	 * Setter
	 */
	public synchronized void setValue(long value) {

		if (value == this.value) {
			return;
		}

		this.value = value;
		if (this.value < minValue) {
			value = minValue;
		} else if (this.value > maxValue) {
			this.value = minValue;
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Setter
	 */
	public void setMinValue(long minValue) {
		this.minValue = minValue;
		if (maxValue < minValue) {
			maxValue = minValue;
		}

	}

	/**
	 * Setter
	 */
	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
		if (minValue > maxValue) {
			minValue = maxValue;
		}
		fixValue();
	}

	/**
	 * set a valid value based on min and max.
	 */
	private void fixValue() {
		if (value < minValue) {
			setValue(minValue);
		} else if (value > maxValue) {
			setValue(maxValue);
		} else {
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Start the timer.
	 */
	public void startTimer(int timeoutInterval) {
		if (timer != null) {
			stop();
		}

		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				setValue(getValue() + 1);
			}
		}, timeoutInterval, timeoutInterval);
		isRunning = true;
	}

	/**
	 * Getter: Check if the timer task is currently running.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Stop the timer task.
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		isRunning = false;
	}

	/**
	 * Move one step.
	 */
	public void step() {
		setValue(getValue() + 1);
	}
}

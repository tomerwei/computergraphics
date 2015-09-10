package cgresearch.apps.hlsvis;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

/**
 * Display time
 * 
 * @author Philipp Jenke
 *
 */
public class TimeDisplay extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5568371428171009417L;

	/**
	 * Constructor
	 */
	public TimeDisplay() {
		setSize(200, 50);
	}

	/**
	 * Update current time.
	 */
	public void setTime(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		setText(formatter.format(time));
	}

}

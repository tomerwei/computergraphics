package cgresearch.studentprojects.scanner.laser;

/**
 * A Class that can be implemented as a Laser for some visual tests without
 * hardware.
 */
public class SimulatorLaser implements ILaser {

	private static final float center = 0.2554f;
	private float currentValue;

	public SimulatorLaser() {
		currentValue = center + 0.05f;
	}

	@Override
	public float getDistance() {
		return currentValue - center;
	}

	@Override
	public float getRawDistance() {
		return currentValue;
	}

	public float waitForDistance() {
		return currentValue - center;
	}

	public boolean resetDistance() {
		currentValue = 0;
		return true;
	}

	@Override
	public void setCenter(float distance) {
		// Simulator doesn't need this method
	}

	@Override
	public void startScan() {
		// Simulator doesn't need this method
	}

	@Override
	public void endScan() {
		// Simulator doesn't need this method
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public void laserOn() {

	}

	@Override
	public void laserOff() {

	}

	@Override
	public void setPort(String port) {
	}

}

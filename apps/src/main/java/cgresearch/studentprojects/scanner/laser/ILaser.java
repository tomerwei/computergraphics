package cgresearch.studentprojects.scanner.laser;

/**
 * This Java Interface is used for setting up a distance sensor.
 */
public interface ILaser {

	/**
	 * Returns the distance of the Scanned Object in meters
	 * calculated from the center of the rotation/object.
	 *
	 * @return and float value of the distance in meters
	 */
	float getDistance();

	/**
	 * Does the same like getDistance but triggers an active wait until
	 * the sensor data arrived
	 *
	 * @return returns the value of the destance in meters
	 */
	float waitForDistance();

	/**
	 * Returns the distance of the Scanned Object in raw format. That means the
	 * zero point is set by the distance sensor and not in the middle of the
	 * rotating plate
	 *
	 * @return the raw value returned by the distance sensor
	 */
	float getRawDistance();

	/**
	 * Resets the internal saved distance data. This is used for a new initialization
	 *
	 * @return true if the value could be resettet otherhand it returns false
	 */
	boolean resetDistance();

	/**
	 * Sets the center of the rotation/object in meters.
	 *
	 * @param distance is the distance from scanner to center
	 */
	void setCenter(float distance);

	/**
	 * Prepares the scanner for a scan
	 */
	void startScan();

	/**
	 * Prepares the scanner for the end of the scan
	 */
	void endScan();

	/**
	 * Savety question for connection
	 * @return returns true if it is connected
	 */
	boolean isConnected();

	/**
	 * Turns the laser on without measurment
	 */
	void laserOn();

	/**
	* Shut the no measurment laser off
	*/
	void laserOff();

	/**
	* Sets the right COM Port for the distance sensor
	*/
	void setPort(String port);
}

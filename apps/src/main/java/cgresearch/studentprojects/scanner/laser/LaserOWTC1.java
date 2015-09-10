package cgresearch.studentprojects.scanner.laser;

import cgresearch.core.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class LaserOWTC1 implements ILaser {

	/**
	 * generates a new instance of the scanner
	 */
	private final static String COMMAND_INIT				= "dg";

	/**
	 * start the endless tracking
	 */
	private final static String COMMAND_TRACKING			= "h";

	/**
	 * stops the endless tracking and all things with the laser
	 */
	private final static String COMMAND_SHUTDOWN_LASER		= "c";

	/**
 	 * turns the laser on (without measurment)
	 */
	private final static String COMMAND_LASER_ON = "o";

	/**
	 * turns the laser off (without measurment)
	 */
	private final static String COMMAND_LASER_OFF = "p";

	/**
	 * An instance of the serial port for connection with the scanner
	 */
	private SerialPort serialPort;

	/**
	 * the internal id of the scanner (needed for multiply scanners and connection)
	 */
	private String scannerId;

	/**
	 * shows if the scanner is right now tracking or not
	 */
	private boolean isTracking;

	/**
	 * checks if the laser is connected at the moment
	 */
	private boolean isConnected;

	/**
	 * the current value of the scan (the zero point is the front side of the scanner)
	 */
	private float currentValue;

	/**
	 * the distance to the center
	 */
	private float center;

	/**
	 * Default constructor. This handels automaticly the generation of the scanner
	 * @param portName the port which is used by the scanner
	 */
	public LaserOWTC1(String portName) {
		isTracking = false;
		isConnected = false;
		currentValue = Float.MAX_VALUE;
		center = 0.2554f;

		setPort(portName);
	}

	/**
	 * Asks if the Scanner application has get the port
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Builds the command string for scanner.
	 *
	 * @param command the static command in the LaserOWTC1 Class
	 * @return a command String for the serial port
	 */
	private String command(String command) {
		return scannerId + command + "\n";
	}

	/**
	 * starts the endless tracking
	 */
	public void startTracking() {
		try {
			serialPort.writeBytes(command(COMMAND_TRACKING).getBytes());
			System.out.println("Start: " + command(COMMAND_TRACKING));
			isTracking = true;
		} catch (SerialPortException e) {
			Logger.getInstance().exception("Can't shutdown the Laser", e);
		}
	}

	/**
	 * shuts the lasor on for aiming and testing
	 */
	public void laserOn() {
		try {
			serialPort.writeBytes(command(COMMAND_LASER_ON).getBytes());
		} catch (SerialPortException e) {
			Logger.getInstance().exception("Can't turn on the laser", e);
		}
	}

	/**
	 * laser off
	 */
	public void laserOff() {
		try {
			serialPort.writeBytes(command(COMMAND_LASER_OFF).getBytes());
		} catch (SerialPortException e) {
			Logger.getInstance().exception("Can't turn off the laser", e);
		}
	}

	/**
	 * shut the laser down after a endless scan
	 */
	public void shutdownLaser() {
		try {
			serialPort.writeBytes((command(COMMAND_SHUTDOWN_LASER)).getBytes());
			isTracking = false;
		} catch (SerialPortException e) {
			Logger.getInstance().exception("Can't shutdown the Laser", e);
		}
	}

	/**
	 * Closes the Serial Port of the Scanner
	 */
	public void closePort() {
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			Logger.getInstance().exception("Can't close the Serial Port", e);
		}
	}

	//Only for testing purpose
	public static void main(String args[]) {
		new LaserOWTC1("COM6");
	}

	@Override
	public float getDistance() {
		if(isTracking) {
			return currentValue - center;
		}else{
			return Float.POSITIVE_INFINITY;
		}
	}

	@Override
	public float getRawDistance() {
		if(isTracking) {
			return currentValue;
		}else{
			return Float.POSITIVE_INFINITY;
		}
	}

  @Override
	public boolean resetDistance() {
		currentValue = Float.POSITIVE_INFINITY;
		return currentValue == 0;
	}

	@Override
	public float waitForDistance() {
		resetDistance();
		while(currentValue == Float.POSITIVE_INFINITY) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return currentValue;
	}

	@Override
	public void setCenter(float distance) {
		center = distance;
	}

	@Override
	public void startScan() {
		startTracking();
	}

	@Override
	public void endScan() {
		shutdownLaser();
	}

	/**
	 * This SerialPortEventListener
	 *
	 * @author Joschka Schulz
	 *
	 */
	class SerialPortReader implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			if(event.isRXCHAR() && event.getEventValue() > 0){//If data is available
				try {
					String bytes = new String(serialPort.readBytes(event.getEventValue()));
					if(bytes.substring(2,4).equals(COMMAND_INIT)) {
						scannerId = "s" + bytes.substring(1,2);
						isConnected = true;
					}else if(bytes.subSequence(2, 3).equals(COMMAND_TRACKING)) {
						try{
							currentValue = Float.parseFloat(bytes.substring(4))/10000;
						}catch(Exception e) {

						}
					}else if(bytes.subSequence(2, 3).equals(COMMAND_SHUTDOWN_LASER)){
						//Cancels the measuring
					}
				}
				catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}
		}
	}

	@Override
	public void setPort(String port) {
		serialPort = new SerialPort(port);
		try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_19200,
                                 SerialPort.DATABITS_7,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_EVEN);
            serialPort.addEventListener(new SerialPortReader());

            //Starting with reading the scanner ID
            serialPort.writeBytes((COMMAND_INIT + "\n").getBytes());
        }
        catch (SerialPortException ex) {
            Logger.getInstance().exception("Exception of the serial port", ex);
        }
	}
}

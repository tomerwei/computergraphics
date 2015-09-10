/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.motors;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

/**
 * Encapsulation of the Tinkerforge components
 * 
 * @author Philipp Jenke
 * 
 */
public class TinkerforgeStack extends Observable implements IStack {
    /**
     * Connection information: IP address.
     */
    private static final String host = "localhost";

    /**
     * Connection information: port.
     */
    private static final int port = 4223;

    /**
     * Reference to the IP connection.
     */
    private IPConnection ipcon = null;

    /**
     * UID of the first motor.
     */
    private static final String MOTOR1_UID = "6eStmv";

    /**
     * UID of the second motor.
     */
    private static final String MOTOR2_UID = "67PFAJ";

    /**
     * First stepper motor.
     */
    private StepperMotor motor1 = null;

    /**
     * Second stepper motor.
     */
    private StepperMotor motor2 = null;

    /**
     * This flag is true of a connection to the Tinkerforge stack exists.
     */
    boolean isConnected = false;

    /**
     * Constructor.
     */
    public TinkerforgeStack() {
        ipcon = new IPConnection();
        motor1 = new StepperMotor(MOTOR2_UID, ipcon);
        motor2 = new StepperMotor(MOTOR1_UID, ipcon);
        motor1.setAcceleration(1000);
        motor2.setAcceleration(1000);
    }

    /**
     * Create connection to the stack.
     */
    public void connect() {
        try {
            ipcon.connect(host, port);
            isConnected = true;
            motor1.setup();
            motor2.setup();
            stateHasChanged();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (AlreadyConnectedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnect from stack.
     */
    public void disconnect() {
        try {
            motor1.stop();
            motor1.disable();
            motor2.stop();
            motor2.disable();
            ipcon.disconnect();
            isConnected = false;
            stateHasChanged();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter
     * 
     * @return Motor 1 object.
     */
    public StepperMotor getMotor1() {
        return motor1;
    }

    /**
     * Getter
     * 
     * @return Motor 2 object.
     */
    public StepperMotor getMotor2() {
        return motor2;
    }

    /**
     * Getter.
     * 
     * @return True if a connection to the stack exists.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * State of the motor has changed - inform observers.
     */
    private void stateHasChanged() {
        // Set update info to observers
        setChanged();
        notifyObservers();
    }
}

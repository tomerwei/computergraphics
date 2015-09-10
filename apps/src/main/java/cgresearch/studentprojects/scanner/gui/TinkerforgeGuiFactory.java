/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.studentprojects.scanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import cgresearch.studentprojects.scanner.motors.TinkerforgeStack;

/**
 * Create a GUI for the Tinkerforge components (two stepper motors).
 * 
 * @author Philipp Jenke
 * 
 */
public class TinkerforgeGuiFactory implements ActionListener, Observer {

    /**
     * Action commands.
     */
    private final String DISCONNECT = "DISCONNECT";
    private final String CONNECT = "CONNECT";

    /**
     * Reference to the tinkerforge object.
     */
    private final TinkerforgeStack tinkerforge;

    private ConnectionLabel checkBoxIsConnected = null;

    /**
     * Constructor.
     */
    public TinkerforgeGuiFactory(TinkerforgeStack tinkerforge) {
        this.tinkerforge = tinkerforge;
        tinkerforge.addObserver(this);
    }

    public JPanel createGui() {
        // Tinkerforge panel
        JPanel panelTinkerforge = new JPanel();
        panelTinkerforge.setLayout(new BorderLayout());
        panelTinkerforge.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Tinkerforge"));

        // Panel for the connection.
        JPanel panelConnection = new JPanel();
        panelConnection.setLayout(new BoxLayout(panelConnection,
                BoxLayout.Y_AXIS));

        panelTinkerforge.add(panelConnection, BorderLayout.NORTH);

        // Connect to Tinkerforge
        JButton buttonConnect = new JButton("Connect to Tinkerforge");
        buttonConnect.setActionCommand(CONNECT);
        buttonConnect.addActionListener(this);
        panelConnection.add(buttonConnect);

        // Disconnect from Tinkerforge
        JButton buttonDisconnect = new JButton("Disconnect from Tinkerforge");
        buttonDisconnect.setActionCommand(DISCONNECT);
        buttonDisconnect.addActionListener(this);
        panelConnection.add(buttonDisconnect);

        checkBoxIsConnected = new ConnectionLabel("connected");
        checkBoxIsConnected.setSelected(tinkerforge.isConnected());
        panelConnection.add(checkBoxIsConnected);

        // GUI for first motor.
        MotorGuiFactory motor1GuiFactory = new MotorGuiFactory(
                tinkerforge.getMotor1());
        JPanel panelMotor1 = motor1GuiFactory.createMotorGui("Motor 1");
        panelTinkerforge.add(panelMotor1, BorderLayout.LINE_START);

        // GUI for second motor.
        MotorGuiFactory motor2GuiFactory = new MotorGuiFactory(
                tinkerforge.getMotor2());
        JPanel panelMotor2 = motor2GuiFactory.createMotorGui("Motor 2");
        panelTinkerforge.add(panelMotor2, BorderLayout.LINE_END);

        return panelTinkerforge;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(CONNECT)) {
            tinkerforge.connect();
        } else if (event.getActionCommand().equals(DISCONNECT)) {
            tinkerforge.disconnect();
        }
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == tinkerforge) {
            checkBoxIsConnected.setSelected(tinkerforge.isConnected());
        }
    }

}

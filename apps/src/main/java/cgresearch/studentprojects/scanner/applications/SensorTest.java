package cgresearch.studentprojects.scanner.applications;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.studentprojects.scanner.gui.DistanceSensorGuiListener;
import cgresearch.studentprojects.scanner.gui.TinkerforgeGuiFactory;
import cgresearch.studentprojects.scanner.motors.TinkerforgeStack;

public class SensorTest extends JFrame implements WindowListener {
    /**
     * 
     */
    private static final long serialVersionUID = 2299933964233744217L;

    /**
     * Tinkerforge object.
     */
    private TinkerforgeStack tinkerforge = new TinkerforgeStack();

    /**
     * Constructor
     */
    public SensorTest() {
        setupGui();

        setTitle("Stepper test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setSize(650, 500);
    }

    /**
     * Create the user interface.
     */
    private void setupGui() {

        // Layout of the content pane
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        getContentPane().add(mainPanel);

        TinkerforgeGuiFactory tinkerforgeGuiFactory = new TinkerforgeGuiFactory(
                tinkerforge);
        mainPanel.add(tinkerforgeGuiFactory.createGui());
        mainPanel.add(createDistanceSensorGui());
    }

    /**
     * Create a GUI for the distance sensor.
     */
    private Component createDistanceSensorGui() {
        JPanel panelDistanceSensor = new JPanel();
        panelDistanceSensor.setLayout(new BoxLayout(panelDistanceSensor,
                BoxLayout.Y_AXIS));
        panelDistanceSensor
                .setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        "Distance Sensor"));

        DistanceSensorGuiListener listener = new DistanceSensorGuiListener();

        JButton buttonConnect = new JButton("Connect");
        panelDistanceSensor.add(buttonConnect);
        buttonConnect.addActionListener(listener);

        JButton buttonDisconnect = new JButton("Disconnect");
        panelDistanceSensor.add(buttonDisconnect);
        buttonDisconnect.addActionListener(listener);

        JButton buttonGetDistance = new JButton("Get distance");
        panelDistanceSensor.add(buttonGetDistance);
        buttonGetDistance.addActionListener(listener);

        return panelDistanceSensor;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    @Override
    public void windowActivated(WindowEvent e) {
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    @Override
    public void windowClosed(WindowEvent e) {
        tinkerforge.disconnect();
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    @Override
    public void windowClosing(WindowEvent e) {
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
     * )
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
     * )
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    @Override
    public void windowIconified(WindowEvent e) {
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * Program entry point.
     */
    public static void main(String args[]) throws Exception {
        ResourcesLocator.getInstance().parseIniFile("resources.ini");
        new SensorTest();
    }
}

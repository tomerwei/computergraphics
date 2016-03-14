package electronics.gyro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.ui.IApplicationControllerGui;

public class GyroApplicationGui extends IApplicationControllerGui
    implements ActionListener, KeyListener, ChangeListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Action Commands.
   */
  private final String ACTION_COMMAND_CONNECT = "ACTION_COMMAND_CONNECT";
  private final String ACTION_COMMAND_DISCONNECT = "ACTION_COMMAND_DISCONNECT";

  /**
   * Reference to the simulation object.
   */
  private final GyroApplicationModel model;

  private JComboBox<String> comboBoxPort;
  private JSlider sliderServo;

  /**
   * Constructor.
   */
  public GyroApplicationGui(GyroApplicationModel model) {
    this.model = model;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    comboBoxPort = new JComboBox<String>();
    for (String port : model.getPorts()) {
      comboBoxPort.addItem(port);
    }
    selectPreferredPort();
    add(comboBoxPort);

    JButton buttonConnect = new JButton("Connect");
    buttonConnect.addActionListener(this);
    buttonConnect.setActionCommand(ACTION_COMMAND_CONNECT);
    add(buttonConnect);

    // JButton buttonDisconnect = new JButton("Disconnect");
    // buttonDisconnect.addActionListener(this);
    // buttonDisconnect.setActionCommand(ACTION_COMMAND_DISCONNECT);
    // add(buttonDisconnect);

    sliderServo = new JSlider(0, 180, 0);
    sliderServo.addChangeListener(this);
    add(sliderServo);
  }

  /**
   * Selects the port that most likely connects to an attached Arduin
   */
  private void selectPreferredPort() {
    for (int i = 0; i < comboBoxPort.getItemCount(); i++) {
      if (comboBoxPort.getItemAt(i).contains("usbmodem1421")) {
        comboBoxPort.setSelectedIndex(i);
        break;
      }
    }
  }

  @Override
  public String getName() {
    return "Gyro Test Application";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_COMMAND_CONNECT)) {
      model.connect((String) comboBoxPort.getSelectedItem());
    } else if (e.getActionCommand().equals(ACTION_COMMAND_DISCONNECT)) {
      model.disconnect();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    model.setServoAngle(sliderServo.getValue());
    model.getGyro().requestValues();
  }
}

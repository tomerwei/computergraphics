package electronics.gyro;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import cgresearch.ui.IApplicationControllerGui;

public class GyroApplicationGui extends IApplicationControllerGui implements ActionListener, KeyListener {

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

  /**
   * Constructor.
   */
  public GyroApplicationGui(GyroApplicationModel model) {
    this.model = model;

    setLayout(new GridLayout(3, 1));

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

    JButton buttonDisconnect = new JButton("Disconnect");
    buttonDisconnect.addActionListener(this);
    buttonDisconnect.setActionCommand(ACTION_COMMAND_DISCONNECT);
    add(buttonDisconnect);
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
}

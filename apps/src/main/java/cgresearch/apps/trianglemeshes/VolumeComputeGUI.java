package cgresearch.apps.trianglemeshes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;

import cgresearch.ui.IApplicationControllerGui;

public class VolumeComputeGUI extends IApplicationControllerGui implements ActionListener, KeyListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private VolumeComputeVisualization vis;
  private JCheckBox checkBoxShowSpat;

  /**
   * Constructor.
   */
  public VolumeComputeGUI(VolumeComputeVisualization vis) {
    this.vis = vis;
    checkBoxShowSpat = new JCheckBox("Show spat volume");
    checkBoxShowSpat.setSelected(vis.getShowSpat());
    checkBoxShowSpat.addActionListener(this);
    add(checkBoxShowSpat);
  }

  @Override
  public String getName() {
    return "Volume Computation";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    vis.toggleShowSpat();
    checkBoxShowSpat.setSelected(vis.getShowSpat());
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

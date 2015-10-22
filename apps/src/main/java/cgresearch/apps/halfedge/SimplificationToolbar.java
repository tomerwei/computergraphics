/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.halfedge;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import cgresearch.ui.IApplicationControllerGui;

/**
 * Toolbar to control the simplification process.
 * 
 * @author Philipp Jenke
 * 
 */
public class SimplificationToolbar extends IApplicationControllerGui {

  /**
   * 
   */
  private static final long serialVersionUID = -6897703772468146995L;

  /**
   * Action command for the simplify button.
   */
  public static final String ACTION_COMMMAND_SIMPLIFY = "ACTION_COMMMAND_SIMPLIFY";

  /**
   * Constructor
   */
  public SimplificationToolbar(ActionListener actionListener) {
    JButton buttonSimplify = new JButton("Simplify");
    buttonSimplify.setActionCommand(ACTION_COMMMAND_SIMPLIFY);
    buttonSimplify.addActionListener(actionListener);
    add(buttonSimplify);
    setVisible(true);
  }

  @Override
  public String getName() {
    return "Simplification";
  }

}

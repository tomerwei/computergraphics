package cgresearch.apps.smoothing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.LaplaceSmoothing;
import cgresearch.ui.IApplicationControllerGui;

public class SmoothingGui extends IApplicationControllerGui implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Action Commands.
   */
  private final String ACTION_COMMAND_SMOOTH = "ACTION_COMMAND_SMOOTH";
  private final String ACTION_COMMAND_RESET = "ACTION_COMMAND_RESET";

  private ITriangleMesh originalMesh;
  private ITriangleMesh smoothedMesh;

  /**
   * Constructor.
   */
  public SmoothingGui(ITriangleMesh originalMesh, ITriangleMesh smoothedMesh) {
    this.originalMesh = originalMesh;
    this.smoothedMesh = smoothedMesh;

    setLayout(new GridLayout(0, 2));

    JButton buttonSmooth = new JButton("SMOOTH");
    buttonSmooth.addActionListener(this);
    buttonSmooth.setActionCommand(ACTION_COMMAND_SMOOTH);
    add(buttonSmooth);

    JButton buttonReset = new JButton("RESET");
    buttonReset.addActionListener(this);
    buttonReset.setActionCommand(ACTION_COMMAND_RESET);
    add(buttonReset);

  }

  @Override
  public String getName() {
    return "Cloth simulation";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_COMMAND_SMOOTH)) {
      // Apply one step of smoothing
      LaplaceSmoothing.smooth(smoothedMesh, 1);
    } else if (e.getActionCommand().equals(ACTION_COMMAND_RESET)) {
      for (int i = 0; i < smoothedMesh.getNumberOfVertices(); i++) {
        smoothedMesh.getVertex(i).getPosition().copy(originalMesh.getVertex(i).getPosition());
        smoothedMesh.updateRenderStructures();
      }
    }
  }
}

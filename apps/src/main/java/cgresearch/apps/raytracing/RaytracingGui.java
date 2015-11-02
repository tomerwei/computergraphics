package cgresearch.apps.raytracing;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.projects.raytracing.ImageViewer;
import cgresearch.projects.raytracing.Raytracer;
import cgresearch.ui.IApplicationControllerGui;

public class RaytracingGui extends IApplicationControllerGui implements
    ActionListener, KeyListener {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  /**
   * Action Commands.
   */
  private final String ACTION_COMMAND_RAYTRACE = "ACTION_COMMAND_RAYTRACE";

  private final Raytracer raytracer;

  /**
   * UI elements
   */
  private final JCheckBox cbPhong, cbShadowRays, cbUsePlaneCheckerBoard;
  private final JTextField textFieldRecursionDepth, textFieldResX,
      textFieldResY;

  /**
   * Constructor.
   */
  public RaytracingGui(CgRootNode rootNode) {
    setRootNode(rootNode);

    setLayout(new GridLayout(7, 2));

    // Phong lighting
    JLabel labelPhong = new JLabel("Use Phong lighting");
    add(labelPhong);
    cbPhong = new JCheckBox();
    cbPhong.setSelected(true);
    add(cbPhong);

    // ShadowType rays
    JLabel labelShadowRays = new JLabel("Use shadow rays");
    add(labelShadowRays);
    cbShadowRays = new JCheckBox();
    cbShadowRays.setSelected(true);
    add(cbShadowRays);

    // Checker board texture for planes
    JLabel labelPlaneCheckerBoard = new JLabel("Use plane checker board");
    add(labelPlaneCheckerBoard);
    cbUsePlaneCheckerBoard = new JCheckBox();
    cbUsePlaneCheckerBoard.setSelected(true);
    add(cbUsePlaneCheckerBoard);

    // Recursion depth
    JLabel labelRecursionDepth = new JLabel("Recursion depth");
    add(labelRecursionDepth);
    textFieldRecursionDepth = new JTextField();
    textFieldRecursionDepth.setText("5");
    add(textFieldRecursionDepth);

    // Resolution (x)
    JLabel labelResolutionX = new JLabel("Resolution x");
    add(labelResolutionX);
    textFieldResX = new JTextField();
    textFieldResX.setText("640");
    add(textFieldResX);

    // Resolution (y)
    JLabel labelResolutionY = new JLabel("Resolution y");
    add(labelResolutionY);
    textFieldResY = new JTextField();
    textFieldResY.setText("480");
    add(textFieldResY);

    // Start raytracing
    JButton buttonReset = new JButton("Raytrace");
    buttonReset.addActionListener(this);
    buttonReset.setActionCommand(ACTION_COMMAND_RAYTRACE);
    add(buttonReset);

    raytracer = new Raytracer(getRootNode());
  }

  @Override
  public String getName() {
    return "Raytracer";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ACTION_COMMAND_RAYTRACE)) {
      raytracer.setup(cbPhong.isSelected(), cbShadowRays.isSelected(),
          Integer.parseInt(textFieldRecursionDepth.getText()),
          cbUsePlaneCheckerBoard.isSelected());
      Image image =
          raytracer.render(Integer.parseInt(textFieldResX.getText()),
              Integer.parseInt(textFieldResY.getText()));
      new ImageViewer(image);
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

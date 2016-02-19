package cgresearch.apps.marchingcubes;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.graphics.datastructures.implicitfunction.ImplicitFunction3DSphere;
import cgresearch.graphics.datastructures.implicitfunction.ImplicitFunction3DTorus;
import cgresearch.graphics.datastructures.implicitfunction.ImplicitFunctionGourSat;
import cgresearch.graphics.datastructures.implicitfunction.ImplicitFunctionSuperquadric;
import cgresearch.ui.IApplicationControllerGui;

/**
 * User interface for the marching cubes frame
 * 
 * @author Philipp Jenke
 *
 */
public class MarchingCubesGui extends IApplicationControllerGui implements ChangeListener, ItemListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * GUI components
   */
  // private JSlider sliderX = null;
  // private JSlider sliderY = null;
  private JComboBox<String> comboBoxFunctions;

  /**
   * Vis object.
   */
  // private ImplicitFunctionVisualization vis = null;

  /**
   * Settings values
   */
  // private int isoValue = 0;
  // private Vector center = VectorMatrixFactory.newVector(0, 0, 0);
  // private Vector dx = VectorMatrixFactory.newVector(1, 0, 0);
  // private Vector dy = VectorMatrixFactory.newVector(0, 1, 0);

  private enum Functions {
    Torus, Sphere, Superquadric, GourSat
  }

  private final MarchingCubesFrame frame;

  /**
   * Constructor
   */
  public MarchingCubesGui(MarchingCubesFrame frame) {
    // this.vis = vis;
    this.frame = frame;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setLayout(new FlowLayout());

    add(new JLabel("Select function:"));
    comboBoxFunctions = new JComboBox<String>();
    for (Functions function : Functions.values()) {
      comboBoxFunctions.addItem(function.name());
    }
    comboBoxFunctions.addItemListener(this);
    add(comboBoxFunctions);

    // sliderX = new JSlider();
    // sliderX.addChangeListener(this);
    // sliderX.setValue(0);
    // add(sliderX);
    //
    // sliderY = new JSlider();
    // sliderY.addChangeListener(this);
    // sliderY.setValue(0);
    // add(sliderY);

    // Initial create
    // vis.create(implicitFunction, center, dx, dy, 4, isoValue);
  }

  @Override
  public String getName() {
    return "Implicit functions";
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // if (sliderX != null && sliderY != null && vis != null) {
    // double alpha = (double) sliderX.getValue() / 100.0 * Math.PI * 2.0;
    // double beta = (double) sliderY.getValue() / 100.0 * Math.PI * 2.0;
    // Matrix rotX =
    // VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newVector(1,
    // 0, 0), alpha);
    // Matrix rotY =
    // VectorMatrixFactory.getRotationMatrix(VectorMatrixFactory.newVector(0,
    // 1, 0), beta);
    // vis.create(implicitFunction, center, rotY.multiply(rotX.multiply(dx)),
    // rotY.multiply(rotX.multiply(dy)), 4,
    // isoValue);
    // }
  }

  /**
   * Setter
   */
  // public void setVis(ImplicitFunctionVisualization vis) {
  // this.vis = vis;
  // }

  @Override
  public void itemStateChanged(ItemEvent e) {
    String functionName = (String) comboBoxFunctions.getSelectedItem();
    Functions function = Functions.valueOf(functionName);
    switch (function) {
      case Torus:
        frame.createMesh(new ImplicitFunction3DTorus(1, 0.5));
        break;
      case Sphere:
        frame.createMesh(new ImplicitFunction3DSphere(1));
        break;
      case Superquadric:
        frame.createMesh(new ImplicitFunctionSuperquadric(1, 0.5));
        break;
      case GourSat:
        frame.createMesh(new ImplicitFunctionGourSat());
        break;
      default:
        break;
    }
  }
}

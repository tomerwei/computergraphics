package cgresearch.apps.marchingcubes;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.ui.IApplicationControllerGui;

/**
 * User interface for the marching cubes frame
 * 
 * @author Philipp Jenke
 *
 */
public class MarchingCubesVisualizeAlgorithmGui extends IApplicationControllerGui implements ChangeListener {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * GUI components
   */
  private final Cube cube;

  private JSlider sliderIsoValue = new JSlider(-10, 10, 0);
  private List<JSlider> sliderValues = new ArrayList<JSlider>();

  /**
   * Constructor
   */
  public MarchingCubesVisualizeAlgorithmGui(Cube cube) {
    this.cube = cube;

    setLayout(new GridLayout(9, 2));
    add(new JLabel("Iso value"));
    add(sliderIsoValue);
    sliderIsoValue.addChangeListener(this);
    sliderIsoValue.setValue((int) cube.getIsoValue());
    for (int i = 0; i < 8; i++) {
      JSlider textFiledValue = new JSlider(-10, 10, (int) cube.getValue(i));
      add(new JLabel("Value " + i));
      sliderValues.add(textFiledValue);
      textFiledValue.addChangeListener(this);
      add(textFiledValue);
    }
  }

  @Override
  public String getName() {
    return "MC Algorithm";
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    try {
      cube.setIsoValue(sliderIsoValue.getValue());
      for (int i = 0; i < 8; i++) {
        cube.setValue(i, sliderValues.get(i).getValue());
      }
    } catch (NumberFormatException nfe) {
    }
  }
}

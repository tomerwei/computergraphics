package cgresearch.apps.curves;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import cgresearch.graphics.datastructures.curves.CurveModel;
import cgresearch.ui.IApplicationControllerGui;

public class CurveFrameGui extends IApplicationControllerGui
    implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = -2905392842363473708L;

  private JPanel mainPanel = new JPanel();

  private JComboBox<String> comboCurve;

  private final CurveModel curveModel;

  /**
   * Constructor.
   */
  public CurveFrameGui(CurveModel curveModel) {
    this.curveModel = curveModel;
    add(mainPanel);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    comboCurve = new JComboBox<String>();

    for (CurveModel.CurveType type : CurveModel.CurveType.values()) {
      comboCurve.addItem(type.toString());
    }
    comboCurve.setSelectedIndex(CurveModel.CurveType.BEZIER.ordinal());
    comboCurve.addActionListener(this);

    mainPanel.add(comboCurve);

    CurvePropertiesWidget widget =
        new CurvePropertiesWidget(curveModel, "Curve");
    mainPanel.add(widget);
  }

  @Override
  public String getName() {
    return "Curves";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    curveModel.generateCurve(
        CurveModel.CurveType.valueOf(comboCurve.getSelectedItem().toString()));
    curveModel.getCurve().updateRenderStructures();
  }
}

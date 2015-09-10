package cgresearch.apps.curves;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import cgresearch.graphics.datastructures.curves.ICurve;
import cgresearch.ui.IApplicationControllerGui;

public class CurveFrameGui extends IApplicationControllerGui {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2905392842363473708L;

	private JPanel mainPanel = new JPanel();

	/**
	 * Constructor.
	 */
	public CurveFrameGui() {
		add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	}

	@Override
	public String getName() {
		return "Curves";
	}

	/**
	 * Register an additional curve
	 * 
	 * @param curve
	 */
	public void registerCurve(ICurve curve, String caption) {
		CurvePropertiesWidget widget = new CurvePropertiesWidget(curve, caption);
		mainPanel.add(widget);
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.curves;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.graphics.datastructures.curves.CurveModel;

/**
 * Widget with properties for the user interface.
 * 
 * @author Philipp Jenke
 * 
 */
public class CurvePropertiesWidget extends JPanel implements ChangeListener,
		Observer {

	/**
     * 
     */
	private static final long serialVersionUID = 1023016815914520533L;

	/**
	 * Slider object.
	 */
	private JSlider parameterSlider;

	/**
	 * Minimum value of the slider.
	 */
	private final int SLIDER_MIN = 0;

	/**
	 * Max value of the slider.
	 */
	private final int SLIDER_MAX = 100;

	/**
	 * Reference to the curve
	 */
	private final CurveModel curveModel;

	/**
	 * Constructor.
	 */
	public CurvePropertiesWidget(CurveModel curve, String caption) {
		this.curveModel = curve;
		curve.addObserver(this);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(new JLabel(caption));
		parameterSlider = new JSlider(SLIDER_MIN, SLIDER_MAX);
		add(parameterSlider);
		parameterSlider.addChangeListener(this);
		parameterSlider.setValue(computePositionFromParameter());
	}

	/**
	 * Compute the slider position for the current curve parameter.
	 */
	private int computePositionFromParameter() {
		double position = (curveModel.getCurve().getParameter() - curveModel.getCurve().getParamMin())
				/ (curveModel.getCurve().getParamMax() - curveModel.getCurve().getParamMin());
		position = position * (SLIDER_MAX - SLIDER_MIN) + SLIDER_MIN;
		return (int) position;
	}

	/**
	 * Compute the parameter from the current slider position.
	 */
	private double computeParameterFromPosition() {
		double parameter = (parameterSlider.getValue() - SLIDER_MIN)
				/ (double) (SLIDER_MAX - SLIDER_MIN);
		parameter = parameter * (curveModel.getCurve().getParamMax() - curveModel.getCurve().getParamMin())
				+ curveModel.getCurve().getParamMin();
		return parameter;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == parameterSlider) {
			curveModel.setT(computeParameterFromPosition());
		}
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		parameterSlider.setValue(computePositionFromParameter());
	}
}

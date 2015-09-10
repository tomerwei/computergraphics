package cgresearch.apps.marchingcubes;

import javax.swing.BoxLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.core.math.IMatrix3;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.implicitfunction.IImplicitFunction3D;
import cgresearch.graphics.misc.ImplicitFunctionVisualization;
import cgresearch.ui.IApplicationControllerGui;

/**
 * User interface for the marching cubes frame
 * 
 * @author Philipp Jenke
 *
 */
public class MarchingCubesGui extends IApplicationControllerGui implements
		ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * GUI components
	 */
	private JSlider sliderX = null;
	private JSlider sliderY = null;

	/**
	 * Vis object.
	 */
	private ImplicitFunctionVisualization vis = null;

	/**
	 * Implicit function used
	 */
	private IImplicitFunction3D implicitFunction = null;

	/**
	 * Settings values
	 */
	private int isoValue = 0;
	private IVector3 center = VectorMatrixFactory.newIVector3(0, 0, 0);
	private IVector3 dx = VectorMatrixFactory.newIVector3(1, 0, 0);
	private IVector3 dy = VectorMatrixFactory.newIVector3(0, 1, 0);

	/**
	 * Constructor
	 */
	public MarchingCubesGui(ImplicitFunctionVisualization vis,
			IImplicitFunction3D implicitFunction) {
		this.vis = vis;
		this.implicitFunction = implicitFunction;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		sliderX = new JSlider();
		sliderX.addChangeListener(this);
		sliderX.setValue(0);
		add(sliderX);

		sliderY = new JSlider();
		sliderY.addChangeListener(this);
		sliderY.setValue(0);
		add(sliderY);

		// Initial create
		vis.create(implicitFunction, center, dx, dy, 4, isoValue);
	}

	@Override
	public String getName() {
		return "Implicit functions";
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (sliderX != null && sliderY != null && vis != null) {
			double alpha = (double) sliderX.getValue() / 100.0 * Math.PI * 2.0;
			double beta = (double) sliderY.getValue() / 100.0 * Math.PI * 2.0;
			IMatrix3 rotX = VectorMatrixFactory.getRotationMatrix(
					VectorMatrixFactory.newIVector3(1, 0, 0), alpha);
			IMatrix3 rotY = VectorMatrixFactory.getRotationMatrix(
					VectorMatrixFactory.newIVector3(0, 1, 0), beta);
			vis.create(implicitFunction, center,
					rotY.multiply(rotX.multiply(dx)),
					rotY.multiply(rotX.multiply(dy)), 4, isoValue);
		}
	}

	/**
	 * Setter
	 */
	public void setVis(ImplicitFunctionVisualization vis) {
		this.vis = vis;

	}

}

package cgresearch.studentprojects.autogenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.swing.RangeSlider;

import cgresearch.graphics.datastructures.primitives.Cuboid;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUI extends IApplicationControllerGui implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel params = new JPanel(new GridLayout(0, 1));
	TitledBorder param = BorderFactory.createTitledBorder("Parameters");

	JLabel heigth = new JLabel("Height / cm");
	JSlider hs = new JSlider(JSlider.HORIZONTAL, 100, 300, 100);

	JLabel length = new JLabel("Length / cm");
	JSlider ls = new JSlider(JSlider.HORIZONTAL, 100, 600, 400);

	JLabel width = new JLabel("Width / cm");
	JSlider ws = new JSlider(JSlider.HORIZONTAL, 100, 300, 200);

	JPanel props = new JPanel();
	TitledBorder slide = BorderFactory.createTitledBorder("Front/Gast/Heck");
	RangeSlider slider = new RangeSlider(1, 100, 33, 66);

	JPanel preset = new JPanel();
	TitledBorder art = BorderFactory.createTitledBorder("Karroserieart");
	JRadioButton pickup = new JRadioButton("Pickup");
	JRadioButton sedan = new JRadioButton("Sedan");
	JRadioButton kombi = new JRadioButton("Kombi");
	JRadioButton hatch = new JRadioButton("Hatchback");

	ButtonGroup radios = new ButtonGroup();

	public GeneratorGUI(CgRootNode rootNode) {
		// Load PLY
		setRootNode(rootNode);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Radios

		radios.add(pickup);
		radios.add(sedan);
		radios.add(kombi);
		radios.add(hatch);

		pickup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hs.setValue(206);
				ws.setValue(193);
				ls.setValue(515);

			}
		});

		hatch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hs.setValue(152);
				ws.setValue(179);
				ls.setValue(410);

			}
		});

		sedan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hs.setValue(175);
				ws.setValue(191);
				ls.setValue(477);

			}
		});

		kombi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				hs.setValue(152);
				ws.setValue(179);
				ls.setValue(447);

			}
		});

		// Sliders

		ws.setMajorTickSpacing(100);
		ws.setMinorTickSpacing(10000);
		ws.setPaintTicks(true);
		ws.setPaintLabels(true);

		hs.setMajorTickSpacing(100);
		hs.setMinorTickSpacing(10000);
		hs.setPaintTicks(true);
		hs.setPaintLabels(true);

		ls.setMajorTickSpacing(100);
		ls.setMinorTickSpacing(10000);
		ls.setPaintTicks(true);
		ls.setPaintLabels(true);

		ls.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		hs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		ws.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		preset.setBorder(art);
		preset.setSize(100, 300);
		preset.add(pickup);
		preset.add(sedan);
		preset.add(kombi);
		preset.add(hatch);

		params.setBorder(param);
		params.setSize(100, 300);
		params.add(heigth);
		params.add(hs);
		params.add(length);
		params.add(ls);
		params.add(width);
		params.add(ws);

		props.setBorder(slide);
		props.setSize(100, 300);
		props.add(slider);

		add(preset);
		add(params);
		add(props);

		JButton genrate = new JButton("Generate");
		genrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateAuto();
			}

		});
		add(genrate);

	}

	@Override
	public String getName() {
		return "GUI";
	}

	private void generateAuto() {

		double h = (double) hs.getValue() / 100;
		double l = (double) ls.getValue() / 100;
		double w = (double) ws.getValue() / 100;

		Auto auto = new Auto(h, l, w, slider.getLowValue(),
				slider.getHighValue());

		CgNode father = new CgNode(null, "auto");

		Cuboid front = auto.getFront();
		CgNode frontNode = new CgNode(front, "front");
		father.addChild(frontNode);

		Cuboid gast = auto.getGast();
		CgNode gastNode = new CgNode(gast, "gast");
		father.addChild(gastNode);

		Cuboid heck = auto.getHeck();
		CgNode heckNode = new CgNode(heck, "heck");
		father.addChild(heckNode);

		Cuboid chassis = auto.getChassis();
		CgNode chassisNode = new CgNode(chassis, "chassis");
		father.addChild(chassisNode);

		getRootNode().addChild(father);

		

	}

	public void remove() {
		getRootNode().removeAllChildren();
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}

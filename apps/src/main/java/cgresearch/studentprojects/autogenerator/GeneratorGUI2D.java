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

import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Cuboid;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUI2D extends IApplicationControllerGui implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel size = new JPanel(new GridLayout(0, 1));
	TitledBorder sizeBorder = BorderFactory.createTitledBorder("Abmessungen");

	JLabel hoehe = new JLabel("Hoehe / cm");
	JSlider hs = new JSlider(JSlider.HORIZONTAL, 100, 300, 200);

	JLabel laenge = new JLabel("Laenge / cm");
	JSlider ls = new JSlider(JSlider.HORIZONTAL, 100, 600, 500);

	JLabel frontHoehe = new JLabel("Front Hoehe / %");
	JSlider fhs = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	JLabel heckHoehe = new JLabel("Heck Hoehe / %");
	JSlider hhs = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	JPanel props = new JPanel();
	TitledBorder slide = BorderFactory.createTitledBorder("Front/Gast/Heck");
	RangeSlider slider = new RangeSlider(1, 100, 33, 66);

	JPanel gast = new JPanel(new GridLayout(0, 1));
	TitledBorder gastBorder = BorderFactory.createTitledBorder("Gast");

	JLabel gastVorne = new JLabel("Gast vorne / %");
	JSlider gvs = new JSlider(JSlider.HORIZONTAL, 0, 50, 30);

	JLabel gastHinten = new JLabel("Gast hinten / %");
	JSlider ghs = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);

	JLabel gastFrontHor = new JLabel("Gast-Front horizonzal / %");
	JSlider gfronthors = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
	
	JLabel gastFrontVer = new JLabel("Gast-Front vertical / %");
	JSlider gfrontvers = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel gastHeckHor = new JLabel("Gast-Heck horizontal / %");
	JSlider gheckhors = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
	
	JLabel gastHeckVer = new JLabel("Gast-Heck vertical / %");
	JSlider gheckvers = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JPanel chassis = new JPanel(new GridLayout(0, 1));
	TitledBorder chassisBorder = BorderFactory.createTitledBorder("Chassis");

	JLabel chassisVorne = new JLabel("Chassis vorne / %");
	JSlider cvs = new JSlider(JSlider.HORIZONTAL, 0, 50, 3);

	JLabel chassisHinten = new JLabel("Chassis hinten / %");
	JSlider chs = new JSlider(JSlider.HORIZONTAL, 0, 50, 3);

	JPanel front = new JPanel(new GridLayout(0, 1));
	TitledBorder frontBorder = BorderFactory.createTitledBorder("Front");

	JLabel frontHor = new JLabel("Front horizontal / %");
	JSlider fhors = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);

	JLabel frontVer = new JLabel("Front vertical / %");
	JSlider fvers = new JSlider(JSlider.HORIZONTAL, 0, 100, 15);

	JPanel heck = new JPanel(new GridLayout(0, 1));
	TitledBorder heckBorder = BorderFactory.createTitledBorder("Heck");

	JLabel heckHor = new JLabel("Heck horizontal / %");
	JSlider hhors = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);

	JLabel heckVer = new JLabel("Heck vertical / %");
	JSlider hvers = new JSlider(JSlider.HORIZONTAL, 0, 100, 15);

	public GeneratorGUI2D(CgRootNode rootNode) {

		setRootNode(rootNode);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Sliders

		hs.setMajorTickSpacing(100);
		hs.setMinorTickSpacing(10000);
		hs.setPaintTicks(true);
		hs.setPaintLabels(true);

		ls.setMajorTickSpacing(100);
		ls.setMinorTickSpacing(10000);
		ls.setPaintTicks(true);
		ls.setPaintLabels(true);

		fhs.setMajorTickSpacing(10);
		fhs.setMinorTickSpacing(100);
		fhs.setPaintTicks(true);
		fhs.setPaintLabels(true);

		hhs.setMajorTickSpacing(10);
		hhs.setMinorTickSpacing(100);
		hhs.setPaintTicks(true);
		hhs.setPaintLabels(true);

		gvs.setMajorTickSpacing(10);
		gvs.setMinorTickSpacing(100);
		gvs.setPaintTicks(true);
		gvs.setPaintLabels(true);

		ghs.setMajorTickSpacing(10);
		ghs.setMinorTickSpacing(100);
		ghs.setPaintTicks(true);
		ghs.setPaintLabels(true);

		gfronthors.setMajorTickSpacing(10);
		gfronthors.setMinorTickSpacing(100);
		gfronthors.setPaintTicks(true);
		gfronthors.setPaintLabels(true);
		
		gfrontvers.setMajorTickSpacing(10);
		gfrontvers.setMinorTickSpacing(100);
		gfrontvers.setPaintTicks(true);
		gfrontvers.setPaintLabels(true);

		gheckhors.setMajorTickSpacing(10);
		gheckhors.setMinorTickSpacing(100);
		gheckhors.setPaintTicks(true);
		gheckhors.setPaintLabels(true);
		
		gheckvers.setMajorTickSpacing(10);
		gheckvers.setMinorTickSpacing(100);
		gheckvers.setPaintTicks(true);
		gheckvers.setPaintLabels(true);

		cvs.setMajorTickSpacing(10);
		cvs.setMinorTickSpacing(100);
		cvs.setPaintTicks(true);
		cvs.setPaintLabels(true);

		chs.setMajorTickSpacing(10);
		chs.setMinorTickSpacing(100);
		chs.setPaintTicks(true);
		chs.setPaintLabels(true);

		fhors.setMajorTickSpacing(10);
		fhors.setMinorTickSpacing(100);
		fhors.setPaintTicks(true);
		fhors.setPaintLabels(true);

		fvers.setMajorTickSpacing(10);
		fvers.setMinorTickSpacing(100);
		fvers.setPaintTicks(true);
		fvers.setPaintLabels(true);

		hhors.setMajorTickSpacing(10);
		hhors.setMinorTickSpacing(100);
		hhors.setPaintTicks(true);
		hhors.setPaintLabels(true);

		hvers.setMajorTickSpacing(10);
		hvers.setMinorTickSpacing(100);
		hvers.setPaintTicks(true);
		hvers.setPaintLabels(true);

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

		fhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		hhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		gvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		ghs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		gfronthors.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});
		
		gfrontvers.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		gheckhors.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});
		
		gheckvers.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		cvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		chs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		fhors.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		fvers.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		hhors.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		hvers.addChangeListener(new ChangeListener() {

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

		size.setBorder(sizeBorder);
		size.setSize(100, 300);
		size.add(hoehe);
		size.add(hs);
		size.add(laenge);
		size.add(ls);
		size.add(frontHoehe);
		size.add(fhs);
		size.add(heckHoehe);
		size.add(hhs);

		props.setBorder(slide);
		props.setSize(100, 300);
		props.add(slider);

		gast.setBorder(gastBorder);
		gast.setSize(100, 300);
		gast.add(gastVorne);
		gast.add(gvs);
		gast.add(gastHinten);
		gast.add(ghs);
		gast.add(gastFrontHor);
		gast.add(gfronthors);
		gast.add(gastFrontVer);
		gast.add(gfrontvers);
		gast.add(gastHeckHor);
		gast.add(gheckhors);
		gast.add(gastHeckVer);
		gast.add(gheckvers);

		chassis.setBorder(chassisBorder);
		chassis.setSize(100, 300);
		chassis.add(chassisVorne);
		chassis.add(cvs);
		chassis.add(chassisHinten);
		chassis.add(chs);

		front.setBorder(frontBorder);
		front.setSize(100, 300);
		front.add(frontHor);
		front.add(fhors);
		front.add(frontVer);
		front.add(fvers);

		heck.setBorder(heckBorder);
		heck.setSize(100, 300);
		heck.add(heckHor);
		heck.add(hhors);
		heck.add(heckVer);
		heck.add(hvers);

		add(size);
		add(props);
		add(gast);
		add(chassis);
		add(front);
		add(heck);

		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateAuto();
			}

		});
		add(generate);

	}

	@Override
	public String getName() {
		return "GUI";
	}

	private void generateAuto() {

		int i = 1;

		double hoehe = (double) hs.getValue() / 100;
		double laenge = (double) ls.getValue() / 100;
		int min = slider.getLowValue();
		int max = slider.getHighValue();
		int frontHoehe = fhs.getValue();
		int heckHoehe = hhs.getValue();

		int degree = 2;

		int fronth = fhors.getValue();
		int frontv = fvers.getValue();

		int gastv = gvs.getValue();
		int gasth = ghs.getValue();

		int heckh = hhors.getValue();
		int heckv = hvers.getValue();

		int chassisv = cvs.getValue();
		int chassish = chs.getValue();

		int gastFrontHor = gfronthors.getValue();
		int gastFrontVer = gfrontvers.getValue();
		int gastHeckHor = gheckhors.getValue();
		int gastHeckVer = gheckvers.getValue();

		Auto2D auto = new Auto2D(hoehe, laenge, min, max, frontHoehe,
				heckHoehe, degree, fronth, frontv, gastv, gasth, heckh, heckv,
				chassisv, chassish, gastFrontHor, gastFrontVer, gastHeckHor, gastHeckVer);

		CgNode father = new CgNode(null, "auto");

		// for (Line3D line : auto.getFront().getLines()) {
		// CgNode node = new CgNode(line, "front" + i);
		// father.addChild(node);
		// i++;
		// }
		//
		// i = 1;
		//
		// for (Line3D line : auto.getGast().getLines()) {
		// CgNode node = new CgNode(line, "gast" + i);
		// father.addChild(node);
		// i++;
		// }
		//
		// i = 1;
		//
		// for (Line3D line : auto.getHeck().getLines()) {
		// CgNode node = new CgNode(line, "heck" + i);
		// father.addChild(node);
		// i++;
		// }
		//
		// i = 1;
		//
		// for (Line3D line : auto.getChassis().getLines()) {
		// CgNode node = new CgNode(line, "chassis" + i);
		// father.addChild(node);
		// i++;
		// }
		//
		// i = 1;

		for (BezierCurve curve : auto.getFront().getCurves()) {
			CgNode node = new CgNode(curve, "front" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (BezierCurve curve : auto.getGast().getCurves()) {
			CgNode node = new CgNode(curve, "gast" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (BezierCurve curve : auto.getHeck().getCurves()) {
			CgNode node = new CgNode(curve, "heck" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (BezierCurve curve : auto.getChassis().getCurves()) {
			CgNode node = new CgNode(curve, "chassis" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		getRootNode().addChild(father);

	}

	public void remove() {
		getRootNode().removeAllChildren();
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}

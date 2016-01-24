package cgresearch.studentprojects.autogenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUIBut extends IApplicationControllerGui implements ActionListener {

	private static final long serialVersionUID = 1L;

	JPanel size = new JPanel(new GridLayout(0, 1));
	TitledBorder sizeBorder = BorderFactory.createTitledBorder("Butterfly");

	JLabel bh = new JLabel("Body Hoehe");
	JSlider bhs = new JSlider(JSlider.HORIZONTAL, 100, 300, 170);

	JLabel bl = new JLabel("Body Laenge");
	JSlider bls = new JSlider(JSlider.HORIZONTAL, 0, 50, 29);

	JLabel twh = new JLabel("Top wings Hoehe");
	JSlider twhs = new JSlider(JSlider.HORIZONTAL, 10, 150, 150);

	JLabel twl = new JLabel("Top wings Laenge");
	JSlider twls = new JSlider(JSlider.HORIZONTAL, 10, 150, 150);

	JLabel bwh = new JLabel("Bottom wings Hoehe");
	JSlider bwhs = new JSlider(JSlider.HORIZONTAL, 10, 150, 93);

	JLabel bwl = new JLabel("Bottom wings Laenge");
	JSlider bwls = new JSlider(JSlider.HORIZONTAL, 10, 150, 67);

	JLabel wmb = new JLabel("Wings Mitte Breite");
	JSlider wmbs = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	JLabel wm = new JLabel("Wings Mitte");
	JSlider wms = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	Butterfly butterfly;

	public GeneratorGUIBut(CgRootNode rootNode) {

		setRootNode(rootNode);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		bhs.setMajorTickSpacing(100);
		bhs.setMinorTickSpacing(10000);
		bhs.setPaintTicks(true);
		bhs.setPaintLabels(true);

		bls.setMajorTickSpacing(100);
		bls.setMinorTickSpacing(10000);
		bls.setPaintTicks(true);
		bls.setPaintLabels(true);

		twhs.setMajorTickSpacing(100);
		twhs.setMinorTickSpacing(10000);
		twhs.setPaintTicks(true);
		twhs.setPaintLabels(true);

		twls.setMajorTickSpacing(100);
		twls.setMinorTickSpacing(10000);
		twls.setPaintTicks(true);
		twls.setPaintLabels(true);

		bwhs.setMajorTickSpacing(100);
		bwhs.setMinorTickSpacing(10000);
		bwhs.setPaintTicks(true);
		bwhs.setPaintLabels(true);

		bwls.setMajorTickSpacing(100);
		bwls.setMinorTickSpacing(10000);
		bwls.setPaintTicks(true);
		bwls.setPaintLabels(true);

		wms.setMajorTickSpacing(10);
		wms.setMinorTickSpacing(100);
		wms.setPaintTicks(true);
		wms.setPaintLabels(true);

		wmbs.setMajorTickSpacing(10);
		wmbs.setMinorTickSpacing(100);
		wmbs.setPaintTicks(true);
		wmbs.setPaintLabels(true);

		bhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bls.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twls.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwls.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		wmbs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		wms.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		size.setBorder(sizeBorder);
		size.setSize(100, 300);
		size.add(bh);
		size.add(bhs);
		size.add(bl);
		size.add(bls);
		size.add(twh);
		size.add(twhs);
		size.add(twl);
		size.add(twls);
		size.add(bwh);
		size.add(bwhs);
		size.add(bwl);
		size.add(bwls);
		size.add(wmb);
		size.add(wmbs);
		size.add(wm);
		size.add(wms);
		add(size);

		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateButterfly();
			}

		});
		add(generate);

	}

	public void remove() {
		int children = getRootNode().getNumChildren();
		for (int i = 0; i < children; i++) {
			CgNode temp = getRootNode().getChildNode(i);
			if (temp.getName().equals("butterfly")) {
				getRootNode().getChildNode(i).deleteNode();
				break;
			}
		}
	}

	private void generateButterfly() {
		System.out.println("Generate");

		double bh = (double) bhs.getValue() / 100;
		double bl = (double) bls.getValue() / 100;
		double twh = (double) twhs.getValue() / 100;
		double twl = (double) twls.getValue() / 100;
		double bwh = (double) bwhs.getValue() / 100;
		double bwl = (double) bwls.getValue() / 100;

		System.out.println("bh: " + bh);
		System.out.println("bl: " + bl);
		System.out.println("twh: " + twh);
		System.out.println("twl: " + twl);
		System.out.println("bwh: " + bwh);
		System.out.println("bwl: " + bwl);

		int wingMitteBreite = wmbs.getValue();
		int wingProzent = wms.getValue();

		if (wingProzent + wingMitteBreite / 2 > 100)
			wingProzent = 100 - wingMitteBreite / 2;
		if (wingProzent - wingMitteBreite / 2 < 0)
			wingProzent = 0 + wingMitteBreite / 2;

		double wmb = (double) wingMitteBreite * bh / 100;

		butterfly = null;
		butterfly = new Butterfly(bh, bl, wingProzent, wmb, twh, twl, bwh, bwl);

		CgNode father = new CgNode(null, "butterfly");

		int i = 1;

		// for (Line3D line : butterfly.getBody().getLines()) {
		// CgNode node = new CgNode(line, "body" + i);
		// father.addChild(node);
		// i++;
		// }

		for (BezierCurve bc : butterfly.getBody().getCurves()) {
			CgNode node = new CgNode(bc, "body" + i);
			father.addChild(node);
			i++;
		}

		i = 1;

		// for (Line3D line : butterfly.getLeftTopWing().getLines()) {
		// CgNode node = new CgNode(line, "leftTop" + i);
		// father.addChild(node);
		// i++;
		// }

		for (BezierCurve bc : butterfly.getLeftTopWing().getCurves()) {
			CgNode node = new CgNode(bc, "leftTop" + i);
			father.addChild(node);
			i++;
		}

		i = 1;

		// for (Line3D line : butterfly.getLeftBottomWing().getLines()) {
		// CgNode node = new CgNode(line, "leftBottom" + i);
		// father.addChild(node);
		// i++;
		// }

		for (BezierCurve bc : butterfly.getLeftBottomWing().getCurves()) {
			CgNode node = new CgNode(bc, "leftBottom" + i);
			father.addChild(node);
			i++;
		}

		i = 1;

		// for (Line3D line : butterfly.getRightTopWing().getLines()) {
		// CgNode node = new CgNode(line, "rightTop" + i);
		// father.addChild(node);
		// i++;
		// }

		for (BezierCurve bc : butterfly.getRightTopWing().getCurves()) {
			CgNode node = new CgNode(bc, "rightTop" + i);
			father.addChild(node);
			i++;
		}

		i = 1;

		// for (Line3D line : butterfly.getRightBottomWing().getLines()) {
		// CgNode node = new CgNode(line, "rightBottom" + i);
		// father.addChild(node);
		// i++;
		// }

		for (BezierCurve bc : butterfly.getRightBottomWing().getCurves()) {
			CgNode node = new CgNode(bc, "rightBottom" + i);
			father.addChild(node);
			i++;
		}

		i = 1;

		getRootNode().addChild(father);

	}

	public void actionPerformed(ActionEvent arg0) {
	}

	@Override
	public String getName() {
		return "GUI";
	}
}

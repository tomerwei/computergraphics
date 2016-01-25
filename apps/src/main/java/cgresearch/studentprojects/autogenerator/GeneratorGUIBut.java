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

	JLabel twth = new JLabel("Top Wing Top Horizontal");
	JSlider twths = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twtv = new JLabel("Top Wing Top Vertical");
	JSlider twtvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twlrh = new JLabel("Top Wing Left/Right Horizontal");
	JSlider twlrhs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twlrv = new JLabel("Top Wing Left/Right Vertical");
	JSlider twlrvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twtph = new JLabel("Top Wing Treffpunkt Horizontal");
	JSlider twtphs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twtpv = new JLabel("Top Wing Treffpunkt Vertical");
	JSlider twtpvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twbh = new JLabel("Top Wing Bottom Horizontal");
	JSlider twbhs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel twbv = new JLabel("Top Wing Bottom Vertical");
	JSlider twbvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel bwlrh = new JLabel("Bottom Wing Left/Right Horizontal");
	JSlider bwlrhs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel bwlrv = new JLabel("Bottom Wing Left/Right Vertical");
	JSlider bwlrvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel bwbh = new JLabel("Bottom Wing Bottom Horizontal");
	JSlider bwbhs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

	JLabel bwbv = new JLabel("Bottom Wing Bottom Vertical");
	JSlider bwbvs = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);

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

		twths.setMajorTickSpacing(100);
		twths.setMinorTickSpacing(10000);
		twths.setPaintTicks(true);
		twths.setPaintLabels(true);

		twtvs.setMajorTickSpacing(100);
		twtvs.setMinorTickSpacing(10000);
		twtvs.setPaintTicks(true);
		twtvs.setPaintLabels(true);

		twlrhs.setMajorTickSpacing(100);
		twlrhs.setMinorTickSpacing(10000);
		twlrhs.setPaintTicks(true);
		twlrhs.setPaintLabels(true);

		twlrvs.setMajorTickSpacing(100);
		twlrvs.setMinorTickSpacing(10000);
		twlrvs.setPaintTicks(true);
		twlrvs.setPaintLabels(true);

		twtphs.setMajorTickSpacing(100);
		twtphs.setMinorTickSpacing(10000);
		twtphs.setPaintTicks(true);
		twtphs.setPaintLabels(true);

		twtpvs.setMajorTickSpacing(100);
		twtpvs.setMinorTickSpacing(10000);
		twtpvs.setPaintTicks(true);
		twtpvs.setPaintLabels(true);

		twbhs.setMajorTickSpacing(100);
		twbhs.setMinorTickSpacing(10000);
		twbhs.setPaintTicks(true);
		twbhs.setPaintLabels(true);

		twbvs.setMajorTickSpacing(100);
		twbvs.setMinorTickSpacing(10000);
		twbvs.setPaintTicks(true);
		twbvs.setPaintLabels(true);

		bwlrhs.setMajorTickSpacing(100);
		bwlrhs.setMinorTickSpacing(10000);
		bwlrhs.setPaintTicks(true);
		bwlrhs.setPaintLabels(true);

		bwlrvs.setMajorTickSpacing(100);
		bwlrvs.setMinorTickSpacing(10000);
		bwlrvs.setPaintTicks(true);
		bwlrvs.setPaintLabels(true);

		bwbhs.setMajorTickSpacing(100);
		bwbhs.setMinorTickSpacing(10000);
		bwbhs.setPaintTicks(true);
		bwbhs.setPaintLabels(true);

		bwbvs.setMajorTickSpacing(100);
		bwbvs.setMinorTickSpacing(10000);
		bwbvs.setPaintTicks(true);
		bwbvs.setPaintLabels(true);

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

		twths.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twtvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twlrhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twlrvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twtphs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twtpvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twbhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		twbvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwlrhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwlrvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwbhs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwbvs.addChangeListener(new ChangeListener() {

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
		size.add(twth);
		size.add(twths);
		size.add(twtv);
		size.add(twtvs);
		size.add(twlrh);
		size.add(twlrhs);
		size.add(twlrv);
		size.add(twlrvs);
		size.add(twtph);
		size.add(twtphs);
		size.add(twtpv);
		size.add(twtpvs);
		size.add(twbh);
		size.add(twbhs);
		size.add(twbv);
		size.add(twbvs);
		size.add(bwlrh);
		size.add(bwlrhs);
		size.add(bwlrv);
		size.add(bwlrvs);
		size.add(bwbh);
		size.add(bwbhs);
		size.add(bwbv);
		size.add(bwbvs);
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

		int wingMitteBreite = wmbs.getValue();
		int wingProzent = wms.getValue();

		if (wingProzent + wingMitteBreite / 2 > 100)
			wingProzent = 100 - wingMitteBreite / 2;
		if (wingProzent - wingMitteBreite / 2 < 0)
			wingProzent = 0 + wingMitteBreite / 2;

		double wmb = (double) wingMitteBreite * bh / 100;

		double twth = (double) twths.getValue() / 100;
		double twtv = (double) twtvs.getValue() / 100;

		double twlrh = (double) twlrhs.getValue() / 100;
		double twlrv = (double) twlrvs.getValue() / 100;

		double twtph = (double) twtphs.getValue() / 100;
		double twtpv = (double) twtpvs.getValue() / 100;

		double twbh = (double) twbhs.getValue() / 100;
		double twbv = (double) twbvs.getValue() / 100;

		double bwlrh = (double) bwlrhs.getValue() / 100;
		double bwlrv = (double) bwlrvs.getValue() / 100;

		double bwbh = (double) bwbhs.getValue() / 100;
		double bwbv = (double) bwbvs.getValue() / 100;

		butterfly = null;
		butterfly = new Butterfly(bh, bl, wingProzent, wmb, twh, twl, bwh, bwl, twth, twtv, twlrh, twlrv, twtph, twtpv,
				twbh, twbv, bwlrh, bwlrv, bwbh, bwbv);

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

package cgresearch.studentprojects.autogenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.core.math.IVector;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.datastructures.primitives.Line3D;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.Material.Normals;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUIBut extends IApplicationControllerGui implements ActionListener {

	private final int butVektor = 32;
	private final int skalar = 10;

	private static final long serialVersionUID = 1L;

	ITriangleMesh triangleMesh = new TriangleMesh();
	Analyzer analyzer = new Analyzer();
	ButData data = new ButData();
	ButData32 data32 = new ButData32();

	JPanel size = new JPanel(new GridLayout(0, 1));
	TitledBorder sizeBorder = BorderFactory.createTitledBorder("Butterfly");

	JLabel bh = new JLabel("Body Hoehe");
	JSlider bhs = new JSlider(JSlider.HORIZONTAL, 100, 300, 170);

	JLabel bl = new JLabel("Body Laenge");
	JSlider bls = new JSlider(JSlider.HORIZONTAL, 0, 50, 29);

	JLabel twh = new JLabel("Top wings Hoehe");
	JSlider twhs = new JSlider(JSlider.HORIZONTAL, 10, 300, 150);

	JLabel twl = new JLabel("Top wings Laenge");
	JSlider twls = new JSlider(JSlider.HORIZONTAL, 10, 300, 150);

	JLabel bwh = new JLabel("Bottom wings Hoehe");
	JSlider bwhs = new JSlider(JSlider.HORIZONTAL, 10, 300, 93);

	JLabel bwl = new JLabel("Bottom wings Laenge");
	JSlider bwls = new JSlider(JSlider.HORIZONTAL, 10, 300, 67);

	JLabel wmb = new JLabel("Wings Mitte Breite");
	JSlider wmbs = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	JLabel wm = new JLabel("Wings Mitte");
	JSlider wms = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

	JLabel twth = new JLabel("Top Wing Top Horizontal");
	JSlider twths = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twtv = new JLabel("Top Wing Top Vertical");
	JSlider twtvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twlrh = new JLabel("Top Wing Left/Right Horizontal");
	JSlider twlrhs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twlrv = new JLabel("Top Wing Left/Right Vertical");
	JSlider twlrvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twtph = new JLabel("Top Wing Treffpunkt Horizontal");
	JSlider twtphs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twtpv = new JLabel("Top Wing Treffpunkt Vertical");
	JSlider twtpvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twbh = new JLabel("Top Wing Bottom Horizontal");
	JSlider twbhs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel twbv = new JLabel("Top Wing Bottom Vertical");
	JSlider twbvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwlrh = new JLabel("Bottom Wing Left/Right Horizontal");
	JSlider bwlrhs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwlrv = new JLabel("Bottom Wing Left/Right Vertical");
	JSlider bwlrvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwlr2h = new JLabel("Bottom Wing Left/Right 2 Horizontal");
	JSlider bwlr2hs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwlr2v = new JLabel("Bottom Wing Left/Right 2 Vertical");
	JSlider bwlr2vs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwbh = new JLabel("Bottom Wing Bottom Horizontal");
	JSlider bwbhs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwbv = new JLabel("Bottom Wing Bottom Vertical");
	JSlider bwbvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwb2h = new JLabel("Bottom Wing Bottom 2 Horizontal");
	JSlider bwb2hs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwb2v = new JLabel("Bottom Wing Bottom 2 Vertical");
	JSlider bwb2vs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwtph = new JLabel("Bottom Wing Treffpunkt Horizontal");
	JSlider bwtphs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JLabel bwtpv = new JLabel("Bottom Wing Treffpunkt Vertical");
	JSlider bwtpvs = new JSlider(JSlider.HORIZONTAL, -150, 150, 0);

	JPanel bild = new JPanel(new GridLayout(0, 1));
	TitledBorder bildBorder = BorderFactory.createTitledBorder("Bild Bewegen");

	JButton up = new JButton("Up");
	JButton down = new JButton("Down");
	JButton left = new JButton("Left");
	JButton right = new JButton("Right");
	JButton save = new JButton("Speichern");
	JButton fromData = new JButton("Generate form Data");

	Butterfly butterfly;

	public GeneratorGUIBut(CgRootNode rootNode) {

		String BUTTER_BILD = "BUTTER_BILD";
		ResourceManager.getTextureManagerInstance().addResource(BUTTER_BILD, new CgTexture(""));

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

		bwlr2hs.setMajorTickSpacing(100);
		bwlr2hs.setMinorTickSpacing(10000);
		bwlr2hs.setPaintTicks(true);
		bwlr2hs.setPaintLabels(true);

		bwlr2vs.setMajorTickSpacing(100);
		bwlr2vs.setMinorTickSpacing(10000);
		bwlr2vs.setPaintTicks(true);
		bwlr2vs.setPaintLabels(true);

		bwbhs.setMajorTickSpacing(100);
		bwbhs.setMinorTickSpacing(10000);
		bwbhs.setPaintTicks(true);
		bwbhs.setPaintLabels(true);

		bwbvs.setMajorTickSpacing(100);
		bwbvs.setMinorTickSpacing(10000);
		bwbvs.setPaintTicks(true);
		bwbvs.setPaintLabels(true);

		bwb2hs.setMajorTickSpacing(100);
		bwb2hs.setMinorTickSpacing(10000);
		bwb2hs.setPaintTicks(true);
		bwb2hs.setPaintLabels(true);

		bwb2vs.setMajorTickSpacing(100);
		bwb2vs.setMinorTickSpacing(10000);
		bwb2vs.setPaintTicks(true);
		bwb2vs.setPaintLabels(true);

		bwtphs.setMajorTickSpacing(100);
		bwtphs.setMinorTickSpacing(10000);
		bwtphs.setPaintTicks(true);
		bwtphs.setPaintLabels(true);

		bwtpvs.setMajorTickSpacing(100);
		bwtpvs.setMinorTickSpacing(10000);
		bwtpvs.setPaintTicks(true);
		bwtpvs.setPaintLabels(true);

		up.setEnabled(false);
		down.setEnabled(false);
		left.setEnabled(false);
		right.setEnabled(false);

		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveBildUp();
			}
		});

		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveBildDown();
			}
		});

		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveBildLeft();
			}
		});

		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moveBildRight();
			}
		});

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

		bwlr2hs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwlr2vs.addChangeListener(new ChangeListener() {

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

		bwb2hs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwb2vs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwtphs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bwtpvs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateButterfly();
			}
		});

		bild.setBorder(bildBorder);
		bild.setSize(100, 300);
		bild.add(up);
		bild.add(down);
		bild.add(left);
		bild.add(right);

		size.setBorder(sizeBorder);
		size.setSize(100, 300);
		size.add(bh);
		size.add(bhs);
		size.add(bl);
		size.add(bls);
		size.add(wmb);
		size.add(wmbs);
		size.add(wm);
		size.add(wms);
		size.add(twh);
		size.add(twhs);
		size.add(twl);
		size.add(twls);
		size.add(bwh);
		size.add(bwhs);
		size.add(bwl);
		size.add(bwls);
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
		size.add(bwlr2h);
		size.add(bwlr2hs);
		size.add(bwlr2v);
		size.add(bwlr2vs);
		size.add(bwbh);
		size.add(bwbhs);
		size.add(bwbv);
		size.add(bwbvs);
		size.add(bwb2h);
		size.add(bwb2hs);
		size.add(bwb2v);
		size.add(bwb2vs);
		size.add(bwtph);
		size.add(bwtphs);
		size.add(bwtpv);
		size.add(bwtpvs);
		add(size);
		add(bild);

		JButton bild = new JButton("Bild hochladen");
		bild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeBild();
				loadBild();
			}

		});
		add(bild);

		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateButterfly();
			}

		});
		add(generate);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveButterfly();
			}

		});
		save.setEnabled(false);
		add(save);

		JButton serialize = new JButton("Serialisieren");
		serialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serialize();
			}

		});
		add(serialize);

		JButton deserialize = new JButton("Deserialisieren");
		deserialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				deserialize();
			}

		});
		add(deserialize);

		fromData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateFromData();
			}

		});
		fromData.setEnabled(false);
		add(fromData);

		JButton applyPCA = new JButton("PCA verwenden");
		applyPCA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				applyPCA();
			}

		});
		add(applyPCA);

	}

	public void generateFromData() {

		int autozahl = 29;
		ButModel car = new ButModel(this.data32.getX().get(autozahl), this.data32.getY().get(autozahl),
				this.data32.getZ().get(autozahl));

		// EigenAuto

		// Reduziert

		IVector x = new Vector(skalar);
		IVector y = new Vector(skalar);

		// Nicht reduziert

		// IVector x = new Vector(butVektor);
		// IVector y = new Vector(butVektor);

		IVector xc = new Vector(butVektor);
		IVector yc = new Vector(butVektor);

		for (int i = 0; i < butVektor; i++) {
			xc.set(i, car.getX().get(i) - analyzer.getPcaX().getCentroid().get(i));
			yc.set(i, car.getY().get(i) - analyzer.getPcaY().getCentroid().get(i));
		}

		
		System.out.println("Apha X");
		for (int i = 0; i < skalar; i++) {

			double xx = 0;
			double yy = 0;

			for (int j = 0; j < butVektor; j++) {
				xx += analyzer.getBtx().get(j).get(i) * xc.get(j);
				yy += analyzer.getBty().get(j).get(i) * yc.get(j);
			}

			x.set(i, xx);
			y.set(i, yy);

			System.out.println(x.get(i));
		}
		
		System.out.println("Apha Y");
		for (int i = 0; i < skalar; i++) {

			System.out.println(y.get(i));
		}

		System.out.println();

		// Nicht reduziert

		// for (int i = skalar; i < butVektor; i++) {
		// x.set(i, 0);
		// y.set(i, 0);
		// }

		// Eigen Auto new

		IVector xn = new Vector(butVektor);
		IVector yn = new Vector(butVektor);

		for (int i = 0; i < butVektor; i++) {
			double xx = 0;
			double yy = 0;

			for (int j = 0; j < skalar; j++) {

				xx += analyzer.getEigenX().get(butVektor - 1 - j).get(i) * x.get(j);
				yy += analyzer.getEigenY().get(butVektor - 1 - j).get(i) * y.get(j);
			}

			xn.set(i, xx);
			yn.set(i, yy);
		}

		// New Auto

		IVector ax = new Vector(butVektor);
		IVector ay = new Vector(butVektor);
		IVector az = new Vector(butVektor);

		System.out.println("Alpha Y");
		
		for (int i = 0; i < butVektor; i++) {
			double xx = 0;
			double yy = 0;

			// Nicht reduziert

			// for (int j = 0; j < butVektor; j++) {

			// Reduziert

			
			
			for (int j = 0; j < skalar; j++) {

				// New Auto

				// xx += analyzer.getBx().get(j).get(i) * xn.get(j);
				// yy += analyzer.getBy().get(j).get(i) * yn.get(j);

				// Dasselbe Auto

				xx += analyzer.getBx().get(j).get(i) * x.get(j);
				yy += analyzer.getBy().get(j).get(i) * y.get(j);

				if (i == 0) {
					// System.out.println("Alpha x " + j + ": " + x.get(j));
					System.out.println(y.get(j));
				}
			}

			ax.set(i, xx);
			ay.set(i, yy);
		}

		for (int i = 0; i < butVektor; i++) {
			double xi = 0;
			double yi = 0;

			xi = ax.get(i) + analyzer.getPcaX().getCentroid().get(i);
			yi = ay.get(i) + analyzer.getPcaY().getCentroid().get(i);

			ax.set(i, xi);
			ay.set(i, yi);
		}

		for (int i = 0; i < butVektor; i++) {
			az.set(i, 0);
		}

		ButModel carnew = new ButModel(ax, ay, az);

		CgNode father = new CgNode(null, "butterfly1");

		int i = 1;
		for (BezierCurve c : car.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve " + i);
			father.addChild(node);
			i++;
		}

		getRootNode().addChild(father);

		CgNode father2 = new CgNode(null, "butterfly2");

		int i2 = 1;
		for (BezierCurve c : carnew.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve2 " + i2);

			father2.addChild(node);
			i2++;
		}

		getRootNode().addChild(father2);

		fromData.setEnabled(false);

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

		double bwlr2h = (double) bwlr2hs.getValue() / 100;
		double bwlr2v = (double) bwlr2vs.getValue() / 100;

		double bwbh = (double) bwbhs.getValue() / 100;
		double bwbv = (double) bwbvs.getValue() / 100;

		double bwb2h = (double) bwb2hs.getValue() / 100;
		double bwb2v = (double) bwb2vs.getValue() / 100;

		double bwtph = (double) bwtphs.getValue() / 100;
		double bwtpv = (double) bwtpvs.getValue() / 100;

		butterfly = null;
		butterfly = new Butterfly(bh, bl, wingProzent, wmb, twh, twl, bwh, bwl, twth, twtv, twlrh, twlrv, twtph, twtpv,
				twbh, twbv, bwlrh, bwlrv, bwbh, bwbv, bwtph, bwtpv, bwb2h, bwb2v, bwlr2h, bwlr2v);

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

		save.setEnabled(true);

	}

	public void removeBild() {

		up.setEnabled(false);
		down.setEnabled(false);
		left.setEnabled(false);
		right.setEnabled(false);

		triangleMesh = new TriangleMesh();

		int children = getRootNode().getNumChildren();
		for (int i = 0; i < children; i++) {
			CgNode temp = getRootNode().getChildNode(i);
			if (temp.getName().equals("bild")) {
				getRootNode().getChildNode(i).deleteNode();
				break;
			}
		}
	}

	public void moveBildUp() {

		triangleMesh.getVertex(0).getPosition().set(1, triangleMesh.getVertex(0).getPosition().get(1) + 0.05);

		triangleMesh.getVertex(1).getPosition().set(1, triangleMesh.getVertex(1).getPosition().get(1) + 0.05);

		triangleMesh.getVertex(2).getPosition().set(1, triangleMesh.getVertex(2).getPosition().get(1) + 0.05);

		triangleMesh.getVertex(3).getPosition().set(1, triangleMesh.getVertex(3).getPosition().get(1) + 0.05);

		triangleMesh.updateRenderStructures();
	}

	public void moveBildDown() {

		triangleMesh.getVertex(0).getPosition().set(1, triangleMesh.getVertex(0).getPosition().get(1) - 0.05);

		triangleMesh.getVertex(1).getPosition().set(1, triangleMesh.getVertex(1).getPosition().get(1) - 0.05);

		triangleMesh.getVertex(2).getPosition().set(1, triangleMesh.getVertex(2).getPosition().get(1) - 0.05);

		triangleMesh.getVertex(3).getPosition().set(1, triangleMesh.getVertex(3).getPosition().get(1) - 0.05);

		triangleMesh.updateRenderStructures();
	}

	public void moveBildLeft() {

		triangleMesh.getVertex(0).getPosition().set(0, triangleMesh.getVertex(0).getPosition().get(0) - 0.05);

		triangleMesh.getVertex(1).getPosition().set(0, triangleMesh.getVertex(1).getPosition().get(0) - 0.05);

		triangleMesh.getVertex(2).getPosition().set(0, triangleMesh.getVertex(2).getPosition().get(0) - 0.05);

		triangleMesh.getVertex(3).getPosition().set(0, triangleMesh.getVertex(3).getPosition().get(0) - 0.05);

		triangleMesh.updateRenderStructures();
	}

	public void moveBildRight() {

		triangleMesh.getVertex(0).getPosition().set(0, triangleMesh.getVertex(0).getPosition().get(0) + 0.05);

		triangleMesh.getVertex(1).getPosition().set(0, triangleMesh.getVertex(1).getPosition().get(0) + 0.05);

		triangleMesh.getVertex(2).getPosition().set(0, triangleMesh.getVertex(2).getPosition().get(0) + 0.05);

		triangleMesh.getVertex(3).getPosition().set(0, triangleMesh.getVertex(3).getPosition().get(0) + 0.05);

		triangleMesh.updateRenderStructures();
	}

	public void loadBild() {

		int a = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-3, -0.4, -0.1)));
		int b = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(3, -0.4, -0.1)));
		int c = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(-3, 2.6, -0.1)));
		int d = triangleMesh.addVertex(new Vertex(VectorMatrixFactory.newIVector3(3, 2.6, -0.1)));

		int ta = triangleMesh.addTextureCoordinate(VectorMatrixFactory.newIVector3(0, 0, -1));
		int tb = triangleMesh.addTextureCoordinate(VectorMatrixFactory.newIVector3(1, 0, -1));
		int tc = triangleMesh.addTextureCoordinate(VectorMatrixFactory.newIVector3(0, 1, -1));
		int td = triangleMesh.addTextureCoordinate(VectorMatrixFactory.newIVector3(1, 1, -1));

		triangleMesh.addTriangle(new Triangle(a, b, c, ta, tb, tc));
		triangleMesh.addTriangle(new Triangle(b, c, d, tb, tc, td));

		triangleMesh.computeTriangleNormals();
		triangleMesh.computeVertexNormals();

		triangleMesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
		triangleMesh.getMaterial().setRenderMode(Normals.PER_FACET);
		triangleMesh.getMaterial().setReflectionAmbient(VectorMatrixFactory.newIVector3(1, 1, 1));
		triangleMesh.getMaterial().setReflectionDiffuse(VectorMatrixFactory.newIVector3(1, 1, 1));

		final JFileChooser fc = new JFileChooser(
				"C:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\butterflies\\");
		fc.showOpenDialog(this);
		File chosenBild = fc.getSelectedFile();

		String path = chosenBild.getPath();

		ResourceManager.getTextureManagerInstance().getResource("BUTTER_BILD").setTextureFilename(path);
		System.out.println(path);

		triangleMesh.getMaterial().setTextureId("BUTTER_BILD");

		CgNode bild = new CgNode(triangleMesh, "bild");
		getRootNode().addChild(bild);

		up.setEnabled(true);
		down.setEnabled(true);
		left.setEnabled(true);
		right.setEnabled(true);

	}

	public void saveButterfly() {
		ButModel bm = new ButModel();

		bm.getCurves().add(this.butterfly.getBody().getTopLeft());
		bm.getCurves().add(this.butterfly.getBody().getTopRight());
		bm.getCurves().add(this.butterfly.getRightTopWing().getTop());
		bm.getCurves().add(this.butterfly.getRightTopWing().getRight());
		bm.getCurves().add(this.butterfly.getRightTopWing().getBottom());
		bm.getCurves().add(this.butterfly.getRightBottomWing().getRight());
		bm.getCurves().add(this.butterfly.getRightBottomWing().getBottom());
		bm.getCurves().add(this.butterfly.getBody().getBottomRight());
		bm.getCurves().add(this.butterfly.getBody().getBottomLeft());
		bm.getCurves().add(this.butterfly.getLeftBottomWing().getBottom());
		bm.getCurves().add(this.butterfly.getLeftBottomWing().getLeft());
		bm.getCurves().add(this.butterfly.getLeftTopWing().getBottom());
		bm.getCurves().add(this.butterfly.getLeftTopWing().getLeft());
		bm.getCurves().add(this.butterfly.getLeftTopWing().getTop());

		bm.fillPoints();
		for (IVector3 v : bm.getPoints()) {
			System.out.println(v.get(0) + " / " + v.get(1) + " / " + v.get(2));
		}
		bm.fillArrays();

		System.out.println(bm.getX().getDimension());
		System.out.println(bm.getY().getDimension());
		System.out.println(bm.getZ().getDimension());

		for (int i = 0; i < bm.getX().getDimension(); i++) {
			System.out.println("x = " + bm.getX().get(i));
		}

		for (int i = 0; i < bm.getY().getDimension(); i++) {
			System.out.println("y = " + bm.getY().get(i));
		}

		for (int i = 0; i < bm.getZ().getDimension(); i++) {
			System.out.println("z = " + bm.getZ().get(i));
		}

		this.data.getX().add(bm.getX());
		this.data.getY().add(bm.getY());
		this.data.getZ().add(bm.getZ());

		System.out.println("Data " + this.data.getX().get(0).get(0));
		System.out.println("Data " + this.data.getY().get(0).get(0));
		System.out.println("Data " + this.data.getZ().get(0).get(0));

		save.setEnabled(false);
	}

	public void applyPCA() {
		analyzer.applyPCA(data32);
	}

	public void serialize() {
		try {
			FileOutputStream fileOut = new FileOutputStream(
					"c:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\butterflies\\data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.data);
			out.close();
			fileOut.close();

		} catch (IOException e) {
		}
	}

	public void deserialize() {
		ButData d = null;
		try {
			FileInputStream fileIn = new FileInputStream(
					"c:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\butterflies\\data.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			d = (ButData) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Analyzer class not found");
			c.printStackTrace();
		}
		this.data = null;
		this.data = d;
		// System.out.println(this.data.getX().get(0).get(0) + "/" +
		// this.data.getY().get(0).get(0) + "/"
		// + this.data.getZ().get(0).get(0));
		// System.out.println("Size " + this.data.getX().size());
		// System.out.println("Size " + this.data.getY().size());
		// System.out.println("Size " + this.data.getZ().size());

		IVector newX = new Vector(butVektor);
		IVector newY = new Vector(butVektor);
		IVector newZ = new Vector(butVektor);

		for (IVector iv : data.getX()) {
			newX = null;
			newX = new Vector(butVektor);
			newX.set(0, iv.get(0));
			newX.set(1, iv.get(1));
			newX.set(2, iv.get(2));
			newX.set(3, iv.get(4));
			newX.set(4, iv.get(5));
			newX.set(5, iv.get(7));
			newX.set(6, iv.get(8));
			newX.set(7, iv.get(10));
			newX.set(8, iv.get(11));
			newX.set(9, iv.get(13));
			newX.set(10, iv.get(14));
			newX.set(11, iv.get(16));
			newX.set(12, iv.get(17));
			newX.set(13, iv.get(18));
			newX.set(14, iv.get(20));
			newX.set(15, iv.get(21));
			newX.set(16, iv.get(22));
			newX.set(17, iv.get(24));
			newX.set(18, iv.get(25));
			newX.set(19, iv.get(27));
			newX.set(20, iv.get(28));
			newX.set(21, iv.get(30));
			newX.set(22, iv.get(31));
			newX.set(23, iv.get(32));
			newX.set(24, iv.get(34));
			newX.set(25, iv.get(35));
			newX.set(26, iv.get(36));
			newX.set(27, iv.get(38));
			newX.set(28, iv.get(39));
			newX.set(29, iv.get(41));
			newX.set(30, iv.get(42));
			newX.set(31, iv.get(44));

			data32.getX().add(newX);
		}

		for (IVector iv : data.getY()) {
			newY = null;
			newY = new Vector(butVektor);
			newY.set(0, iv.get(0));
			newY.set(1, iv.get(1));
			newY.set(2, iv.get(2));
			newY.set(3, iv.get(4));
			newY.set(4, iv.get(5));
			newY.set(5, iv.get(7));
			newY.set(6, iv.get(8));
			newY.set(7, iv.get(10));
			newY.set(8, iv.get(11));
			newY.set(9, iv.get(13));
			newY.set(10, iv.get(14));
			newY.set(11, iv.get(16));
			newY.set(12, iv.get(17));
			newY.set(13, iv.get(18));
			newY.set(14, iv.get(20));
			newY.set(15, iv.get(21));
			newY.set(16, iv.get(22));
			newY.set(17, iv.get(24));
			newY.set(18, iv.get(25));
			newY.set(19, iv.get(27));
			newY.set(20, iv.get(28));
			newY.set(21, iv.get(30));
			newY.set(22, iv.get(31));
			newY.set(23, iv.get(32));
			newY.set(24, iv.get(34));
			newY.set(25, iv.get(35));
			newY.set(26, iv.get(36));
			newY.set(27, iv.get(38));
			newY.set(28, iv.get(39));
			newY.set(29, iv.get(41));
			newY.set(30, iv.get(42));
			newY.set(31, iv.get(44));

			data32.getY().add(newY);
		}

		for (IVector iv : data.getZ()) {
			newZ = null;
			newZ = new Vector(butVektor);
			newZ.set(0, iv.get(0));
			newZ.set(1, iv.get(1));
			newZ.set(2, iv.get(2));
			newZ.set(3, iv.get(4));
			newZ.set(4, iv.get(5));
			newZ.set(5, iv.get(7));
			newZ.set(6, iv.get(8));
			newZ.set(7, iv.get(10));
			newZ.set(8, iv.get(11));
			newZ.set(9, iv.get(13));
			newZ.set(10, iv.get(14));
			newZ.set(11, iv.get(16));
			newZ.set(12, iv.get(17));
			newZ.set(13, iv.get(18));
			newZ.set(14, iv.get(20));
			newZ.set(15, iv.get(21));
			newZ.set(16, iv.get(22));
			newZ.set(17, iv.get(24));
			newZ.set(18, iv.get(25));
			newZ.set(19, iv.get(27));
			newZ.set(20, iv.get(28));
			newZ.set(21, iv.get(30));
			newZ.set(22, iv.get(31));
			newZ.set(23, iv.get(32));
			newZ.set(24, iv.get(34));
			newZ.set(25, iv.get(35));
			newZ.set(26, iv.get(36));
			newZ.set(27, iv.get(38));
			newZ.set(28, iv.get(39));
			newZ.set(29, iv.get(41));
			newZ.set(30, iv.get(42));
			newZ.set(31, iv.get(44));

			data32.getZ().add(newZ);
		}

		fromData.setEnabled(true);

	}

	public void actionPerformed(ActionEvent arg0) {
	}

	@Override
	public String getName() {
		return "GUI";
	}
}

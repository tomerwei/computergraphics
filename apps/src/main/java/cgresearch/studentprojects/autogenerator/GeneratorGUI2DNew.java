package cgresearch.studentprojects.autogenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cgresearch.core.math.IVector;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.curves.BezierCurve;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUI2DNew extends IApplicationControllerGui implements ActionListener {

	private final int carVektor = 18;
	private final int skalar = 7;

	private static final long serialVersionUID = 1L;

	Analyzer analyzer = new Analyzer();
	Data2D data = new Data2D();
	Data2D18 data18 = new Data2D18();

	JPanel xkoef = new JPanel(new GridLayout(0, 1));
	TitledBorder xkoefBorder = BorderFactory.createTitledBorder("X-Eigenkoeffizienten");

	JLabel xkoef1 = new JLabel("Eigenkoeffizient 1");
	JSlider xkoef1s = new JSlider(JSlider.HORIZONTAL, -150, 170, 0);

	JLabel xkoef2 = new JLabel("Eigenkoeffizient 2");
	JSlider xkoef2s = new JSlider(JSlider.HORIZONTAL, -130, 110, 0);

	JLabel xkoef3 = new JLabel("Eigenkoeffizient 3");
	JSlider xkoef3s = new JSlider(JSlider.HORIZONTAL, -100, 110, 0);

	JLabel xkoef4 = new JLabel("Eigenkoeffizient 4");
	JSlider xkoef4s = new JSlider(JSlider.HORIZONTAL, -70, 80, 0);

	JLabel xkoef5 = new JLabel("Eigenkoeffizient 5");
	JSlider xkoef5s = new JSlider(JSlider.HORIZONTAL, -50, 70, 0);

	JLabel xkoef6 = new JLabel("Eigenkoeffizient 6");
	JSlider xkoef6s = new JSlider(JSlider.HORIZONTAL, -35, 55, 0);

	JLabel xkoef7 = new JLabel("Eigenkoeffizient 7");
	JSlider xkoef7s = new JSlider(JSlider.HORIZONTAL, -30, 25, 0);

	JPanel ykoef = new JPanel(new GridLayout(0, 1));
	TitledBorder ykoefBorder = BorderFactory.createTitledBorder("Y-Eigenkoeffizienten");

	JLabel ykoef1 = new JLabel("Eigenkoeffizient 1");
	JSlider ykoef1s = new JSlider(JSlider.HORIZONTAL, -20, 35, 0);

	JLabel ykoef2 = new JLabel("Eigenkoeffizient 2");
	JSlider ykoef2s = new JSlider(JSlider.HORIZONTAL, -40, 55, 0);

	JLabel ykoef3 = new JLabel("Eigenkoeffizient 3");
	JSlider ykoef3s = new JSlider(JSlider.HORIZONTAL, -30, 20, 0);

	JLabel ykoef4 = new JLabel("Eigenkoeffizient 4");
	JSlider ykoef4s = new JSlider(JSlider.HORIZONTAL, -35, 45, 0);

	JLabel ykoef5 = new JLabel("Eigenkoeffizient 5");
	JSlider ykoef5s = new JSlider(JSlider.HORIZONTAL, -20, 10, 0);

	JLabel ykoef6 = new JLabel("Eigenkoeffizient 6");
	JSlider ykoef6s = new JSlider(JSlider.HORIZONTAL, -25, 25, 0);

	JLabel ykoef7 = new JLabel("Eigenkoeffizient 7");
	JSlider ykoef7s = new JSlider(JSlider.HORIZONTAL, -35, 20, 0);

	Auto2D auto;

	public GeneratorGUI2DNew(CgRootNode rootNode) {

		deserialize();
		applyPCA();

		String AUTO_BILD = "AUTO_BILD";
		ResourceManager.getTextureManagerInstance().addResource(AUTO_BILD, new CgTexture(""));

		setRootNode(rootNode);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Sliders

		xkoef1s.setMajorTickSpacing(100);
		xkoef1s.setMinorTickSpacing(1000000);
		xkoef1s.setPaintTicks(true);
		xkoef1s.setPaintLabels(true);

		xkoef2s.setMajorTickSpacing(100);
		xkoef2s.setMinorTickSpacing(1000000);
		xkoef2s.setPaintTicks(true);
		xkoef2s.setPaintLabels(true);

		xkoef3s.setMajorTickSpacing(100);
		xkoef3s.setMinorTickSpacing(1000000);
		xkoef3s.setPaintTicks(true);
		xkoef3s.setPaintLabels(true);

		xkoef4s.setMajorTickSpacing(100);
		xkoef4s.setMinorTickSpacing(1000000);
		xkoef4s.setPaintTicks(true);
		xkoef4s.setPaintLabels(true);

		xkoef5s.setMajorTickSpacing(100);
		xkoef5s.setMinorTickSpacing(1000000);
		xkoef5s.setPaintTicks(true);
		xkoef5s.setPaintLabels(true);

		xkoef6s.setMajorTickSpacing(100);
		xkoef6s.setMinorTickSpacing(1000000);
		xkoef6s.setPaintTicks(true);
		xkoef6s.setPaintLabels(true);

		xkoef7s.setMajorTickSpacing(100);
		xkoef7s.setMinorTickSpacing(1000000);
		xkoef7s.setPaintTicks(true);
		xkoef7s.setPaintLabels(true);

		ykoef1s.setMajorTickSpacing(100);
		ykoef1s.setMinorTickSpacing(1000000);
		ykoef1s.setPaintTicks(true);
		ykoef1s.setPaintLabels(true);

		ykoef2s.setMajorTickSpacing(100);
		ykoef2s.setMinorTickSpacing(1000000);
		ykoef2s.setPaintTicks(true);
		ykoef2s.setPaintLabels(true);

		ykoef3s.setMajorTickSpacing(100);
		ykoef3s.setMinorTickSpacing(1000000);
		ykoef3s.setPaintTicks(true);
		ykoef3s.setPaintLabels(true);

		ykoef4s.setMajorTickSpacing(100);
		ykoef4s.setMinorTickSpacing(1000000);
		ykoef4s.setPaintTicks(true);
		ykoef4s.setPaintLabels(true);

		ykoef5s.setMajorTickSpacing(100);
		ykoef5s.setMinorTickSpacing(1000000);
		ykoef5s.setPaintTicks(true);
		ykoef5s.setPaintLabels(true);

		ykoef6s.setMajorTickSpacing(100);
		ykoef6s.setMinorTickSpacing(1000000);
		ykoef6s.setPaintTicks(true);
		ykoef6s.setPaintLabels(true);

		ykoef7s.setMajorTickSpacing(100);
		ykoef7s.setMinorTickSpacing(1000000);
		ykoef7s.setPaintTicks(true);
		ykoef7s.setPaintLabels(true);

		xkoef.setBorder(xkoefBorder);
		xkoef.setSize(100, 300);
		xkoef.add(xkoef1);
		xkoef.add(xkoef1s);
		xkoef.add(xkoef2);
		xkoef.add(xkoef2s);
		xkoef.add(xkoef3);
		xkoef.add(xkoef3s);
		xkoef.add(xkoef4);
		xkoef.add(xkoef4s);
		xkoef.add(xkoef5);
		xkoef.add(xkoef5s);
		xkoef.add(xkoef6);
		xkoef.add(xkoef6s);
		xkoef.add(xkoef7);
		xkoef.add(xkoef7s);

		ykoef.setBorder(ykoefBorder);
		ykoef.setSize(100, 300);
		ykoef.add(ykoef1);
		ykoef.add(ykoef1s);
		ykoef.add(ykoef2);
		ykoef.add(ykoef2s);
		ykoef.add(ykoef3);
		ykoef.add(ykoef3s);
		ykoef.add(ykoef4);
		ykoef.add(ykoef4s);
		ykoef.add(ykoef5);
		ykoef.add(ykoef5s);
		ykoef.add(ykoef6);
		ykoef.add(ykoef6s);
		ykoef.add(ykoef7);
		ykoef.add(ykoef7s);

		xkoef1s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef2s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef3s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef4s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef5s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef6s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		xkoef7s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef1s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef2s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef3s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef4s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef5s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef6s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		ykoef7s.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateFromData();
			}
		});

		add(xkoef);
		add(ykoef);

		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				remove();
				generateFromData();
			}

		});
		add(generate);

	}

	@Override
	public String getName() {
		return "GUI";
	}

	public void remove() {
		int children = getRootNode().getNumChildren();
		for (int i = 0; i < children; i++) {
			CgNode temp = getRootNode().getChildNode(i);
			if (temp.getName().equals("auto")) {
				getRootNode().getChildNode(i).deleteNode();
				break;
			}
		}
	}

	public void generateFromData() {

		IVector xn = new Vector(carVektor);
		IVector yn = new Vector(carVektor);

		double xek1 = (double) xkoef1s.getValue() / 100;
		double xek2 = (double) xkoef2s.getValue() / 100;
		double xek3 = (double) xkoef3s.getValue() / 100;
		double xek4 = (double) xkoef4s.getValue() / 100;
		double xek5 = (double) xkoef5s.getValue() / 100;
		double xek6 = (double) xkoef6s.getValue() / 100;
		double xek7 = (double) xkoef7s.getValue() / 100;

		xn.set(0, xek1);
		xn.set(1, xek2);
		xn.set(2, xek3);
		xn.set(3, xek4);
		xn.set(4, xek5);
		xn.set(5, xek6);
		xn.set(6, xek7);

		double yek1 = (double) ykoef1s.getValue() / 100;
		double yek2 = (double) ykoef2s.getValue() / 100;
		double yek3 = (double) ykoef3s.getValue() / 100;
		double yek4 = (double) ykoef4s.getValue() / 100;
		double yek5 = (double) ykoef5s.getValue() / 100;
		double yek6 = (double) ykoef6s.getValue() / 100;
		double yek7 = (double) ykoef7s.getValue() / 100;

		yn.set(0, yek1);
		yn.set(1, yek2);
		yn.set(2, yek3);
		yn.set(3, yek4);
		yn.set(4, yek5);
		yn.set(5, yek6);
		yn.set(6, yek7);
		
		// New Auto

		IVector ax = new Vector(carVektor);
		IVector ay = new Vector(carVektor);
		IVector az = new Vector(carVektor);

		for (int i = 0; i < carVektor; i++) {
			double xx = 0;
			double yy = 0;

			// Reduziert

			for (int j = 0; j < skalar; j++) {

				// New Auto

				System.out.println(xn.get(0));

				xx += analyzer.getBx().get(j).get(i) * xn.get(j);
				yy += analyzer.getBy().get(j).get(i) * yn.get(j);

			}

			ax.set(i, xx);
			ay.set(i, yy);
		}

		for (int i = 0; i < carVektor; i++) {
			double xi = 0;
			double yi = 0;

			xi = ax.get(i) + analyzer.getPcaX().getCentroid().get(i);
			yi = ay.get(i) + analyzer.getPcaY().getCentroid().get(i);

			ax.set(i, xi);
			ay.set(i, yi);
		}

		for (int i = 0; i < carVektor; i++) {
			az.set(i, 0);
		}

		Car carnew = new Car(ax, ay, az);

		CgNode father2 = new CgNode(null, "auto");

		int i2 = 1;
		for (BezierCurve c : carnew.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve " + i2);

			father2.addChild(node);
			i2++;
		}

		getRootNode().addChild(father2);

	}

	public void deserialize() {
		Data2D d = null;
		try {
			FileInputStream fileIn = new FileInputStream(
					"c:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\data.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			d = (Data2D) in.readObject();
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

		IVector newX = new Vector(carVektor);
		IVector newY = new Vector(carVektor);
		IVector newZ = new Vector(carVektor);

		for (IVector iv : data.getX()) {
			newX = null;
			newX = new Vector(carVektor);
			newX.set(0, iv.get(0));
			newX.set(1, iv.get(1));
			newX.set(2, iv.get(2));
			newX.set(3, iv.get(4));
			newX.set(4, iv.get(5));
			newX.set(5, iv.get(7));
			newX.set(6, iv.get(8));
			newX.set(7, iv.get(10));
			newX.set(8, iv.get(12));
			newX.set(9, iv.get(13));
			newX.set(10, iv.get(15));
			newX.set(11, iv.get(16));
			newX.set(12, iv.get(18));
			newX.set(13, iv.get(19));
			newX.set(14, iv.get(21));
			newX.set(15, iv.get(22));
			newX.set(16, iv.get(24));
			newX.set(17, iv.get(26));

			data18.getX().add(newX);
		}

		for (IVector iv : data.getY()) {
			newY = null;
			newY = new Vector(carVektor);
			newY.set(0, iv.get(0));
			newY.set(1, iv.get(1));
			newY.set(2, iv.get(2));
			newY.set(3, iv.get(4));
			newY.set(4, iv.get(5));
			newY.set(5, iv.get(7));
			newY.set(6, iv.get(8));
			newY.set(7, iv.get(10));
			newY.set(8, iv.get(12));
			newY.set(9, iv.get(13));
			newY.set(10, iv.get(15));
			newY.set(11, iv.get(16));
			newY.set(12, iv.get(18));
			newY.set(13, iv.get(19));
			newY.set(14, iv.get(21));
			newY.set(15, iv.get(22));
			newY.set(16, iv.get(24));
			newY.set(17, iv.get(26));

			data18.getY().add(newY);
		}

		for (IVector iv : data.getZ()) {
			newZ = null;
			newZ = new Vector(carVektor);
			newZ.set(0, iv.get(0));
			newZ.set(1, iv.get(1));
			newZ.set(2, iv.get(2));
			newZ.set(3, iv.get(4));
			newZ.set(4, iv.get(5));
			newZ.set(5, iv.get(7));
			newZ.set(6, iv.get(8));
			newZ.set(7, iv.get(10));
			newZ.set(8, iv.get(12));
			newZ.set(9, iv.get(13));
			newZ.set(10, iv.get(15));
			newZ.set(11, iv.get(16));
			newZ.set(12, iv.get(18));
			newZ.set(13, iv.get(19));
			newZ.set(14, iv.get(21));
			newZ.set(15, iv.get(22));
			newZ.set(16, iv.get(24));
			newZ.set(17, iv.get(26));

			data18.getZ().add(newZ);
		}

	}

	public void applyPCA() {
		analyzer.applyPCA(data18);
	}

	public void actionPerformed(ActionEvent arg0) {
	}

}

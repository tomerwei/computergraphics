package cgresearch.studentprojects.autogenerator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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

import cgresearch.core.math.Matrix;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Vector;
import cgresearch.core.math.Matrix;
import cgresearch.core.math.PrincipalComponentAnalysis;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.BasisFunctionBezier;
import cgresearch.graphics.datastructures.curves.Curve;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class GeneratorGUI2D extends IApplicationControllerGui implements ActionListener {

	private final int carVektor = 18;
	private final int skalar = 7;

	private static final long serialVersionUID = 1L;

	ITriangleMesh triangleMesh = new TriangleMesh();
	Analyzer analyzer = new Analyzer();
	Data2D data = new Data2D();
	Data2D18 data18 = new Data2D18();

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
	JLabel fgs1 = new JLabel("Vorne / %");
	JSlider slider1 = new JSlider(JSlider.HORIZONTAL, 0, 100, 31);
	JLabel fgs2 = new JLabel("Hinten / %");
	JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 66);

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

	JPanel bild = new JPanel(new GridLayout(0, 1));
	TitledBorder bildBorder = BorderFactory.createTitledBorder("Bild Bewegen");

	JButton up = new JButton("Up");
	JButton down = new JButton("Down");
	JButton left = new JButton("Left");
	JButton right = new JButton("Right");
	JButton save = new JButton("Speichern");
	JButton fromData = new JButton("Generate form Data");

	Auto2D auto;

	public GeneratorGUI2D(CgRootNode rootNode) {

		String AUTO_BILD = "AUTO_BILD";
		ResourceManager.getTextureManagerInstance().addResource(AUTO_BILD, new CgTexture(""));

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

		slider1.setMajorTickSpacing(10);
		slider1.setMinorTickSpacing(100);
		slider1.setPaintTicks(true);
		slider1.setPaintLabels(true);

		slider2.setMajorTickSpacing(10);
		slider2.setMinorTickSpacing(100);
		slider2.setPaintTicks(true);
		slider2.setPaintLabels(true);

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

		slider1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
			}
		});

		slider2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				remove();
				generateAuto();
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
		props.add(fgs1);
		props.add(slider1);
		props.add(fgs2);
		props.add(slider2);

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
				generateAuto();
			}

		});
		add(generate);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAuto();
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
		// add(serialize);

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

		JButton testPCA = new JButton("PCA testen");
		testPCA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				testPCA();
			}

		});
		add(testPCA);

		JButton applyPCA = new JButton("PCA verwenden");
		applyPCA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				applyPCA();
			}

		});
		add(applyPCA);

	}

	@Override
	public String getName() {
		return "GUI";
	}

	private void generateAuto() {

		int i = 1;

		double hoehe = (double) hs.getValue() / 100;
		double laenge = (double) ls.getValue() / 100;
		int min = slider1.getValue();
		int max = slider2.getValue();
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

		auto = null;
		auto = new Auto2D(hoehe, laenge, min, max, frontHoehe, heckHoehe, degree, fronth, frontv, gastv, gasth, heckh,
				heckv, chassisv, chassish, gastFrontHor, gastFrontVer, gastHeckHor, gastHeckVer);

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

		for (Curve curve : auto.getFront().getCurves()) {
			CgNode node = new CgNode(curve, "front" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (Curve curve : auto.getGast().getCurves()) {
			CgNode node = new CgNode(curve, "gast" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (Curve curve : auto.getHeck().getCurves()) {
			CgNode node = new CgNode(curve, "heck" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		for (Curve curve : auto.getChassis().getCurves()) {
			CgNode node = new CgNode(curve, "chassis" + i);

			father.addChild(node);
			i++;
		}

		i = 1;

		getRootNode().addChild(father);

		save.setEnabled(true);

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

	public void removeBild() {

		up.setEnabled(false);
		down.setEnabled(false);
		left.setEnabled(false);
		right.setEnabled(false);

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

		int a = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(-3, -0.4, -0.1)));
		int b = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(3, -0.4, -0.1)));
		int c = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(-3, 2.6, -0.1)));
		int d = triangleMesh.addVertex(new Vertex(VectorFactory.createVector3(3, 2.6, -0.1)));

		int ta = triangleMesh.addTextureCoordinate(VectorFactory.createVector3(0, 0, -1));
		int tb = triangleMesh.addTextureCoordinate(VectorFactory.createVector3(1, 0, -1));
		int tc = triangleMesh.addTextureCoordinate(VectorFactory.createVector3(0, 1, -1));
		int td = triangleMesh.addTextureCoordinate(VectorFactory.createVector3(1, 1, -1));

		triangleMesh.addTriangle(new Triangle(a, b, c, ta, tb, tc));
		triangleMesh.addTriangle(new Triangle(b, c, d, tb, tc, td));

		triangleMesh.computeTriangleNormals();
		triangleMesh.computeVertexNormals();

		triangleMesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);

		final JFileChooser fc = new JFileChooser(
				"C:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\");
		fc.showOpenDialog(this);
		File chosenBild = fc.getSelectedFile();

		String path = chosenBild.getPath();

		ResourceManager.getTextureManagerInstance().getResource("AUTO_BILD").setTextureFilename(path);
		System.out.println(path);

		triangleMesh.getMaterial().setTextureId("AUTO_BILD");

		CgNode bild = new CgNode(triangleMesh, "bild");
		getRootNode().addChild(bild);

		up.setEnabled(true);
		down.setEnabled(true);
		left.setEnabled(true);
		right.setEnabled(true);

	}

	public void saveAuto() {
		Car car = new Car();
		car.getCurves().add(this.auto.getFront().getLeft());
		car.getCurves().add(this.auto.getFront().getTop());
		car.getCurves().add(this.auto.getGast().getLeft());
		car.getCurves().add(this.auto.getGast().getTop());
		car.getCurves().add(this.auto.getGast().getRight());
		car.getCurves().add(this.auto.getHeck().getTop());
		car.getCurves().add(this.auto.getHeck().getRight());
		car.getCurves().add(this.auto.getChassis().getRight());
		car.getCurves().add(this.auto.getChassis().getBottom());
		car.getCurves().add(this.auto.getChassis().getLeft());

		car.fillPoints();
		for (Vector v : car.getPoints()) {
			System.out.println(v.get(0) + " / " + v.get(1) + " / " + v.get(2));
		}
		car.fillArrays();

		System.out.println(car.getX().getDimension());
		System.out.println(car.getY().getDimension());
		System.out.println(car.getZ().getDimension());

		for (int i = 0; i < car.getX().getDimension(); i++) {
			System.out.println("x = " + car.getX().get(i));
		}

		for (int i = 0; i < car.getY().getDimension(); i++) {
			System.out.println("y = " + car.getY().get(i));
		}

		for (int i = 0; i < car.getZ().getDimension(); i++) {
			System.out.println("z = " + car.getZ().get(i));
		}

		this.data.getX().add(car.getX());
		this.data.getY().add(car.getY());
		this.data.getZ().add(car.getZ());

		System.out.println("Data " + this.data.getX().get(0).get(0));
		System.out.println("Data " + this.data.getY().get(0).get(0));
		System.out.println("Data " + this.data.getZ().get(0).get(0));

		save.setEnabled(false);
	}

	public void serialize() {
		try {
			FileOutputStream fileOut = new FileOutputStream(
					"c:\\Users\\Vitos\\git\\cg\\computergraphics\\assets\\studentprojects\\autogenerator\\data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.data);
			out.close();
			fileOut.close();

		} catch (IOException e) {
		}
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
		// System.out.println(this.data.getX().get(0).get(0) + "/" +
		// this.data.getY().get(0).get(0) + "/"
		// + this.data.getZ().get(0).get(0));
		// System.out.println("Size " + this.data.getX().size());
		// System.out.println("Size " + this.data.getY().size());
		// System.out.println("Size " + this.data.getZ().size());

		Vector newX = new Vector(carVektor);
		Vector newY = new Vector(carVektor);
		Vector newZ = new Vector(carVektor);

		for (Vector iv : data.getX()) {
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

		for (Vector iv : data.getY()) {
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

		for (Vector iv : data.getZ()) {
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

		fromData.setEnabled(true);

	}

	public void generateFromData() {

		int autozahl = 3;
		Car car = new Car(this.data18.getX().get(autozahl), this.data18.getY().get(autozahl),
				this.data18.getZ().get(autozahl));

		// EigenAuto

		// Reduziert

		Vector x = new Vector(skalar);
		Vector y = new Vector(skalar);

		// Nicht reduziert

		// Vector x = new Vector(carVektor);
		// Vector y = new Vector(carVektor);

		Vector xc = new Vector(carVektor);
		Vector yc = new Vector(carVektor);

		for (int i = 0; i < carVektor; i++) {
			xc.set(i, car.getX().get(i) - analyzer.getPcaX().getCentroid().get(i));
			yc.set(i, car.getY().get(i) - analyzer.getPcaY().getCentroid().get(i));
		}

		for (int i = 0; i < skalar; i++) {

			double xx = 0;
			double yy = 0;

			for (int j = 0; j < carVektor; j++) {
				xx += analyzer.getBtx().get(j).get(i) * xc.get(j);
				yy += analyzer.getBty().get(j).get(i) * yc.get(j);
			}

			x.set(i, xx);
			y.set(i, yy);

			System.out.println("Alpha x " + i + ": " + x.get(i));
		}

		System.out.println();

		// Nicht reduziert

		// for (int i = skalar; i < carVektor; i++) {
		// x.set(i, 0);
		// y.set(i, 0);
		// }

		// Eigen Auto new

		Vector xn = new Vector(carVektor);
		Vector yn = new Vector(carVektor);

		for (int i = 0; i < carVektor; i++) {
			double xx = 0;
			double yy = 0;

			for (int j = 0; j < skalar; j++) {

				xx += analyzer.getEigenX().get(carVektor - 1 - j).get(i) * x.get(j);
				yy += analyzer.getEigenY().get(carVektor - 1 - j).get(i) * y.get(j);
			}

			xn.set(i, xx);
			yn.set(i, yy);
		}

		// New Auto

		Vector ax = new Vector(carVektor);
		Vector ay = new Vector(carVektor);
		Vector az = new Vector(carVektor);

		for (int i = 0; i < carVektor; i++) {
			double xx = 0;
			double yy = 0;

			// Nicht reduziert

			// for (int j = 0; j < carVektor; j++) {

			// Reduziert

			for (int j = 0; j < skalar; j++) {

				// New Auto

//				 xx += analyzer.getBx().get(j).get(i) * xn.get(j);
//				 yy += analyzer.getBy().get(j).get(i) * yn.get(j);

				// Dasselbe Auto

				xx += analyzer.getBx().get(j).get(i) * x.get(j);
				yy += analyzer.getBy().get(j).get(i) * y.get(j);

				if (i == 0) {
					// System.out.println("Alpha x " + j + ": " + x.get(j));
					System.out.println("Alpha y " + j + ": " + y.get(j));
				}
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

		CgNode father = new CgNode(null, "auto1");

		int i = 1;
		for (Curve c : car.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve " + i);
			father.addChild(node);
			i++;
		}

		getRootNode().addChild(father);

		CgNode father2 = new CgNode(null, "auto2");

		int i2 = 1;
		for (Curve c : carnew.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve2 " + i2);

			father2.addChild(node);
			i2++;
		}

		getRootNode().addChild(father2);

		fromData.setEnabled(false);

	}

	public void testPCA() {

		List<Vector> alphasX = new ArrayList<Vector>();
		List<Vector> alphasY = new ArrayList<Vector>();
		Vector xs = new Vector(carVektor);
		Vector ys = new Vector(carVektor);

		for (int num = 0; num < 30; num++) {
			Car car = new Car(this.data.getX().get(num), this.data.getY().get(num), this.data.getZ().get(num));

			Vector x = new Vector(carVektor);
			Vector y = new Vector(carVektor);

			Vector xc = new Vector(carVektor);
			Vector yc = new Vector(carVektor);

			for (int i = 0; i < carVektor; i++) {
				xc.set(i, car.getX().get(i) - analyzer.getPcaX().getCentroid().get(i));
				yc.set(i, car.getY().get(i) - analyzer.getPcaY().getCentroid().get(i));
			}

			for (int i = 0; i < carVektor; i++) {

				double xx = 0;
				double yy = 0;

				for (int j = 0; j < carVektor; j++) {
					xx += analyzer.getBtx().get(j).get(i) * xc.get(j);
					yy += analyzer.getBty().get(j).get(i) * yc.get(j);
				}

				// System.out.println("XX: " + xx);

				x.set(i, xx);
				y.set(i, yy);
			}

			alphasX.add(x);
			alphasY.add(y);

		}

		double xxx = 0;
		double yyy = 0;

		for (int i = 0; i < carVektor; i++) {

			for (Vector v : alphasX) {

				xxx += v.get(i);
				System.out.println(v.get(i) + "+" + "     (" + xxx + ")");
			}
			for (Vector v : alphasY) {
				yyy += v.get(i);
			}

			System.out.println("= " + xxx);

			xxx = xxx / 30.0;
			yyy = yyy / 30.0;

			xs.set(i, xxx);
			ys.set(i, yyy);

		}

		// xs und ys Mittelwerte

		Vector ax = new Vector(carVektor);
		Vector ay = new Vector(carVektor);
		Vector az = new Vector(carVektor);

		for (int i = 0; i < carVektor; i++) {
			double xx = 0;
			double yy = 0;

			for (int j = 0; j < carVektor; j++) {

				// System.out.println("Sehr klein? " + xs.get(j));

				xx += analyzer.getBx().get(j).get(i) * xs.get(j);
				yy += analyzer.getBy().get(j).get(i) * ys.get(j);
			}

			ax.set(i, xx);
			ay.set(i, yy);
		}

		for (int i = 0; i < carVektor; i++) {
			double xi = 0;
			double yi = 0;

			xi = ax.get(i) + analyzer.getPcaX().getCentroid().get(i);
			yi = ay.get(i) + analyzer.getPcaY().getCentroid().get(i);

			// ax.set(i, analyzer.getPcaX().getCentroid().get(i));
			// ay.set(i, analyzer.getPcaY().getCentroid().get(i));

			ax.set(i, xi);
			ay.set(i, yi);
		}

		for (int i = 0; i < carVektor; i++) {
			az.set(i, 0);
		}

		Car newcar = new Car(ax, ay, az);

		CgNode father2 = new CgNode(null, "auto2");

		int i2 = 1;
		for (Curve c : newcar.getCurves()) {
			CgNode node = new CgNode(c, "BezierCurve2 " + i2);

			father2.addChild(node);
			i2++;
		}

		getRootNode().addChild(father2);

	}

	public void applyPCA() {
//		analyzer.applyPCA(data18);
	}

	public void actionPerformed(ActionEvent arg0) {
	}

}

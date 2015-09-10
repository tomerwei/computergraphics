package cgresearch.studentprojects.urbanreconstruction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cgresearch.core.math.Vector3;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Noise;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.points.TriangleMeshSampler;
import cgresearch.graphics.datastructures.primitives.Plane;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMeshFactory;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.ui.IApplicationControllerGui;

public class UrbanGUI extends IApplicationControllerGui {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1773531470805482138L;
	MeshExtractor me = new MeshExtractor();
	public static List<Plane> planes = new ArrayList<Plane>();
	private Ransac ran;
	private static IPointCloud source;
	private static List<Point> next;
	private static List<Point> remove;
	private static IPointCloud result;
	public static List<IPointCloud> points = new ArrayList<IPointCloud>();
	public static List<ITriangleMesh> meshes = new ArrayList<ITriangleMesh>();
	JLabel pointsT = new JLabel();

	public List<TriangleMesh> triangleMesh = new ArrayList<TriangleMesh>();

	public UrbanGUI() {
		// Load PLY

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton plyButton = new JButton("Load .PLY");
		plyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadPly();
			}

		});
		add(plyButton);

		JButton createButton = new JButton("Create cube");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createCube();
			}

		});
		add(createButton);

		// Number of points
		JPanel pointsP = new JPanel();
		JLabel pointsL = new JLabel("Number of Points:");
		pointsP.add(pointsL);
		// JLabel pointsT = new JLabel();
		pointsP.add(pointsT);
		add(pointsP);

		// Options
		JPanel repeatsP = new JPanel();
		JLabel repeatsL = new JLabel("Times to repeat:");
		repeatsP.add(repeatsL);
		final JTextField repeatsT = new JTextField(3);
		repeatsP.add(repeatsT);
		add(repeatsP);

		JPanel supportP = new JPanel();
		JLabel supportL = new JLabel("Forseeable support:");
		supportP.add(supportL);
		final JTextField supportT = new JTextField("2500", 5);
		supportP.add(supportT);
		add(supportP);

		JPanel TresholdP = new JPanel();
		JLabel TresholdL = new JLabel("Treshold t:");
		TresholdP.add(TresholdL);
		final JTextField TresholdT = new JTextField("0.002", 5);
		TresholdP.add(TresholdT);
		add(TresholdP);

		// Run Ransac

		JButton runRan = new JButton("Run RANSAC");
		runRan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ran.setForseeable_support(Integer.parseInt(supportT.getText()));
				ran.setT(Double.parseDouble(TresholdT.getText()));
				runRan(Integer.parseInt(repeatsT.getText()));
			}

		});
		add(runRan);

		JButton showPlains = new JButton("Show plains");
		showPlains.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPlains();
			}

		});
		add(showPlains);

		JButton mesh = new JButton("Extract mesh");
		mesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				extractMesh();
			}

		});
		add(mesh);

		ran = new Ransac(Integer.parseInt(supportT.getText()),
				Double.parseDouble(TresholdT.getText()));
	}

	@Override
	public String getName() {
		return "GUI";
	}

	private static List<Point> copyListOfPoints(IPointCloud pc) {
		List<Point> res = new ArrayList<Point>();

		for (int i = 0; i < pc.getNumberOfPoints(); i++) {
			res.add(pc.getPoint(i));
		}

		return res;
	}

	private File choosePly() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		// In response to a button click:
		fc.showOpenDialog(this);
		File ply = fc.getSelectedFile();

		return ply;
	}

	private void loadPly() {
		PlyConverter pc = new PlyConverter(choosePly());
		source = pc.getPointCloud();
		next = copyListOfPoints(source);
		CgNode node = new CgNode(source, "Source");
		getRootNode().addChild(node);
		pointsT.setText("" + source.getNumberOfPoints());
	}

	private void createCube() {
		ITriangleMesh mesh = TriangleMeshFactory.createCube();
		IPointCloud pointCloud = TriangleMeshSampler.sample(mesh, 5000);
		Noise.addNoise(pointCloud, 0.02f);
		source = pointCloud;
		next = copyListOfPoints(source);
		CgNode node = new CgNode(source, "Cube");
		getRootNode().addChild(node);
		pointsT.setText("" + source.getNumberOfPoints());
	}

	private void showPlains() {

		IPointCloud xyz = new PointCloud();
		xyz.addPoint(new Point(new Vector3(0, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 1, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 2, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 3, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 4, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 5, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 6, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 7, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 8, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 9, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 10, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(1, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(2, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(3, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(4, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(5, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(6, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(7, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(8, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(9, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(10, 0, 0), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 1), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 2), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 3), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 4), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 5), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 6), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 7), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 8), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 9), new Vector3(),
				new Vector3()));
		xyz.addPoint(new Point(new Vector3(0, 0, 10), new Vector3(),
				new Vector3()));

		CgNode coord = new CgNode(xyz, "Coord ");
		getRootNode().addChild(coord);

		int count = 1;
		for (IPointCloud pc : points) {
			CgNode node = new CgNode(pc, "Plane " + count);
			getRootNode().addChild(node);
			count++;
		}
	}

	private void extractMesh() {
		int n = 0;
		// me.convertToReal();
		for (IPointCloud pc : me.ebene) {
			n++;
			CgNode node1 = new CgNode(pc, "Ebene mit Rausch " + n);
			getRootNode().addChild(node1);

			for (int i = 0; i < pc.getNumberOfPoints(); i++) {
				pc.getPoint(i).getPosition().set(2, 0);

				System.out.println("Ebene " + n + " Point " + i);
			}
			CgNode node2 = new CgNode(pc, "Ebene ohne Rausch " + n);
			getRootNode().addChild(node2);
		}

		// Convert back

		me.convertBack();
		n = 0;
		snap();
		for (IPointCloud pc : points) {
			n++;
			CgNode node1 = new CgNode(pc, "New Point " + n);
			getRootNode().addChild(node1);

			CgNode node2 = new CgNode(doMesh(pc), "New Ebene " + n);
			getRootNode().addChild(node2);
		}

	}

	public ITriangleMesh doMesh(IPointCloud pc) {
		ITriangleMesh tm = new TriangleMesh();
		int a = tm.addVertex(new Vertex(pc.getPoint(0).getPosition()));
		int b = tm.addVertex(new Vertex(pc.getPoint(1).getPosition()));
		int c = tm.addVertex(new Vertex(pc.getPoint(2).getPosition()));
		int d = tm.addVertex(new Vertex(pc.getPoint(3).getPosition()));
		tm.addTriangle(new Triangle(a, b, c));
		tm.addTriangle(new Triangle(b, c, d));
		// tm.fitToUnitBox();
		tm.computeTriangleNormals();
		tm.computeVertexNormals();
		meshes.add(tm);

		// System.out.println("Ebene " + (meshes.size() + 1) + ": ");
		// for (int i = 0; i < 4; i++) {
		// System.out.println("Point " + (i + 1) + ":");
		// System.out.println("x " + tm.getVertex(i).getPosition().get(0)
		// + " y " + tm.getVertex(i).getPosition().get(1) + " z "
		// + tm.getVertex(i).getPosition().get(2));
		//
		// }
		return tm;
	}

	public void snap() {

		List<Point> toSnap = new ArrayList<Point>();
		List<List<Integer>> toJoin = new ArrayList<List<Integer>>();

		boolean[] check;
		Point[] res;

		for (IPointCloud pc : points) {
			for (int i = 0; i < pc.getNumberOfPoints(); i++) {
				toSnap.add(pc.getPoint(i));
			}
		}

		check = new boolean[toSnap.size()];
		res = new Point[toSnap.size()];

		for (int i = 0; i < toSnap.size(); i++) {
			res[i] = null;
			check[i] = false;
		}

		for (Point p : toSnap) {

			if (!check[toSnap.indexOf(p)]) {

				Map<Double, Integer> d = new HashMap<Double, Integer>();
				List<Double> d2 = new ArrayList<Double>();

				for (int i = 0; i < toSnap.size(); i++) {
					double l = dist(p, toSnap.get(i));
					d.put(l, i);
					d2.add(l);
				}

				java.util.Collections.sort(d2);

				List<Integer> join = new ArrayList<Integer>();

				join.add(d.get(d2.get(0)));
				join.add(d.get(d2.get(1)));
				join.add(d.get(d2.get(2)));

				toJoin.add(join);

				check[d.get(d2.get(0))] = true;
				check[d.get(d2.get(1))] = true;
				check[d.get(d2.get(2))] = true;
			}
		}

		for (List<Integer> l : toJoin) {

			double sum_x = 0;
			double sum_y = 0;
			double sum_z = 0;

			// for (int i = 0; i < l.size(); i++) {
			// sum_x += toSnap.get(l.get(i)).getPosition().get(0);
			// sum_y += toSnap.get(l.get(i)).getPosition().get(1);
			// sum_z += toSnap.get(l.get(i)).getPosition().get(2);
			// }
			//
			// sum_x = sum_x / 3;
			// sum_y = sum_y / 3;
			// sum_z = sum_z / 3;

			// for (int i = 0; i < l.size(); i++) {
			// toSnap.get(l.get(i)).getPosition().set(0, sum_x);
			// toSnap.get(l.get(i)).getPosition().set(1, sum_y);
			// toSnap.get(l.get(i)).getPosition().set(2, sum_z);
			// }

			sum_x = toSnap.get(l.get(0)).getPosition().get(0);
			sum_y = toSnap.get(l.get(0)).getPosition().get(1);
			sum_z = toSnap.get(l.get(0)).getPosition().get(2);

			for (int i = 0; i < l.size(); i++) {
				toSnap.get(l.get(i)).getPosition().set(0, sum_x);
				toSnap.get(l.get(i)).getPosition().set(1, sum_y);
				toSnap.get(l.get(i)).getPosition().set(2, sum_z);
			}

		}

		for (List<Integer> l : toJoin) {
			for (Integer i : l)
				System.out.print(i + " ");
			System.out.println();
		}

		// toSnap.clear();
		//
		// for (int i = 0; i < res.length; i++) {
		// toSnap.add(res[i]);
		// }

		for (Point p : toSnap) {
			System.out.println(p.getPosition().getNorm());
		}

	}

	public double dist(Point p1, Point p2) {
		return (p1.getPosition().subtract(p2.getPosition())).getNorm();
	}

	private void runRan(int anzahl) {

		while (anzahl != 0) {
			ran.runRansac(next);
			result = ran.getResult(next);
			// if (result.getNumberOfPoints() > 500)
			planes.add(ran.getPlane());
			points.add(result);

			remove = copyListOfPoints(result);
			next.removeAll(remove);
			anzahl--;
		}
	}

}

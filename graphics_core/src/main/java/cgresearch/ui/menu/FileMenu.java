/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.AsciiPointFormat;
import cgresearch.graphics.fileio.AsciiPointsReader;
import cgresearch.graphics.fileio.MoCapImporter;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.fileio.PlyFileReader;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * File menu
 * 
 * @author Philipp Jenke
 * 
 */
public class FileMenu extends CgApplicationMenu implements ActionListener {

	/**
	 * Action commands.
	 */
	private static final String MENU_ACTION_COMMAND_IMPORT_OBJ = "MENU_IMPORT_OBJ";
	private static final String MENU_ACTION_COMMAND_IMPORT_PLY = "MENU_IMPORT_PLY";
	private static final String MENU_ACTION_COMMAND_IMPORT_MOCAP = "MENU_IMPORT_MOCAP";
	private static final String MENU_ACTION_COMMAND_IMPORT_ASCII_POINTS = "MENU_IMPORT_ASCII_POINTS";
	private static final String MENU_ACTION_COMMAND_EXIT = "MENU_EXIT";

	/**
	 * @param name
	 */
	public FileMenu() {
		super("File");

		// Obj
		JMenuItem itemObj = new JMenuItem("Import OBJ mesh");
		itemObj.setActionCommand(MENU_ACTION_COMMAND_IMPORT_OBJ);
		itemObj.addActionListener(this);
		add(itemObj);

		// Ply
		JMenuItem itemPly = new JMenuItem("Import PLY mesh");
		itemPly.setActionCommand(MENU_ACTION_COMMAND_IMPORT_PLY);
		itemPly.addActionListener(this);
		add(itemPly);

		// MoCap data
		JMenuItem itemMoCap = new JMenuItem("Import MoCap data");
		itemMoCap.setActionCommand(MENU_ACTION_COMMAND_IMPORT_MOCAP);
		itemMoCap.addActionListener(this);
		add(itemMoCap);

		// Ascii points
		JMenuItem itemAsciiPoints = new JMenuItem("Import ASCII points");
		itemAsciiPoints
				.setActionCommand(MENU_ACTION_COMMAND_IMPORT_ASCII_POINTS);
		itemAsciiPoints.addActionListener(this);
		// add(itemAsciiPoints);

		addSeparator();

		// Exit
		JMenuItem itemExit = new JMenuItem("Exit");
		itemExit.setActionCommand(MENU_ACTION_COMMAND_EXIT);
		itemExit.addActionListener(this);
		add(itemExit);
	}

	/**
     * 
     */
	private static final long serialVersionUID = -1236846344301142295L;

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(MENU_ACTION_COMMAND_IMPORT_OBJ)) {
			importObj();
		} else if (e.getActionCommand().equals(MENU_ACTION_COMMAND_IMPORT_PLY)) {
			importPly();
		} else if (e.getActionCommand()
				.equals(MENU_ACTION_COMMAND_IMPORT_MOCAP)) {
			importMoCap();
		} else if (e.getActionCommand().equals(
				MENU_ACTION_COMMAND_IMPORT_ASCII_POINTS)) {
			importAsciiPoints();
		} else if (e.getActionCommand().equals(MENU_ACTION_COMMAND_EXIT)) {
			System.exit(0);
		}

	}

	/**
	 * Import point cloud from ascii file.
	 */
	private void importAsciiPoints() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String filename = fileChooser.getSelectedFile().getAbsolutePath();
		AsciiPointsReader reader = new AsciiPointsReader();
		IPointCloud pointCloud = reader.readFromFile(filename,
				new AsciiPointFormat());
		if (pointCloud != null) {
			rootNode.addChild(new CgNode(pointCloud, "point cloud"));
		}
	}

	/**
	 * Import motion capture data.
	 */
	private void importMoCap() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String filename = fileChooser.getSelectedFile().getAbsolutePath();
		MoCapImporter reader = new MoCapImporter();
		CgNode node = reader.readFromFile(filename);
		if (node != null) {
			rootNode.addChild(node);
		}
	}

	/**
	 * Import PLY data.
	 */
	private void importPly() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String objFilename = fileChooser.getSelectedFile().getAbsolutePath();
		PlyFileReader reader = new PlyFileReader();
		ITriangleMesh mesh = reader.readFile(objFilename);

		if (mesh != null) {
			CgNode cgNode = new CgNode(mesh, "Ply mesh");
			rootNode.addChild(cgNode);
		}
	}

	/**
	 * Import OBJ data.
	 */
	private void importObj() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String objFilename = fileChooser.getSelectedFile().getAbsolutePath();
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> meshes = reader.readFile(objFilename);
		CgNode cgNode = new CgNode(null, "meshes");
		for (ITriangleMesh mesh : meshes) {
			cgNode.addChild(new CgNode(mesh, "mesh"));
		}
		rootNode.addChild(cgNode);
	}

}

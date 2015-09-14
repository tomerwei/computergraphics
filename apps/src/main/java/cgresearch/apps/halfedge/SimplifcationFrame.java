/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.halfedge;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.halfedge.HalfEdgeDatastructureFactory;
import cgresearch.graphics.datastructures.halfedge.HalfEdgeDatastructureOperations;
import cgresearch.graphics.datastructures.halfedge.IHalfEdgeDatastructure;
import cgresearch.graphics.datastructures.halfedge.TriangleMeshHalfEdgeConverter;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.JoglAppLauncher;

/**
 * Central widget to test the half edge simplification functionality.
 * 
 * @author Philipp Jenke
 * 
 */
public class SimplifcationFrame extends CgApplication implements ActionListener {

	/**
	 * Half edge data structure
	 */
	private IHalfEdgeDatastructure ds = null;

	/**
	 * Constructor.
	 */
	public SimplifcationFrame() {
		// Add a toobar
		SimplificationToolbar toolbar = new SimplificationToolbar(this);
		toolbar.setSize(200, 50);
		toolbar.setVisible(true);

		HalfEdgeDatastructureFactory factory = new HalfEdgeDatastructureFactory();
		ds = factory.createHalfEdgeDatastructure();

		// Read mesh from file
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> meshes = reader.readFile("meshes/sphere.obj");
		if (meshes.size() == 1) {
			ITriangleMesh mesh = meshes.get(0);
			mesh.fitToUnitBox();
			// Convert triangle mesh to half edge data structure
			TriangleMeshHalfEdgeConverter converter = new TriangleMeshHalfEdgeConverter();
			ds = converter.convert(mesh);
			ds.checkConsistency();
		}

		// Create a triangle mesh for rendering
		createShapeAndAddToSceneGraph();
	}

	/**
	 * Create a shape object from the half edge data structure and add it to the
	 * scene graph.
	 */
	private void createShapeAndAddToSceneGraph() {
		// Create shape and insert into scene graph
		TriangleMeshHalfEdgeConverter converter = new TriangleMeshHalfEdgeConverter();
		ITriangleMesh mesh = converter.convert(ds);
		CgNode meshNode = new CgNode(mesh, "Mesh");
		getCgRootNode().addChild(meshNode);

	}

	/**
	 * Apply one simplification step
	 */
	private void simplify() {
		HalfEdgeDatastructureOperations ops = new HalfEdgeDatastructureOperations();
		ops.collapse(ds, ds.getHalfEdge(0));
		ds.checkConsistency();
		createShapeAndAddToSceneGraph();
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals(
				SimplificationToolbar.ACTION_COMMMAND_SIMPLIFY)) {
			simplify();
		}
	}

	/**
	 * Program entry point
	 */
	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(new SimplifcationFrame());
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
	}

}

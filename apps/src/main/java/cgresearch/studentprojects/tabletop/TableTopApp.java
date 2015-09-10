package cgresearch.studentprojects.tabletop;

import java.util.List;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;

/**
 * Testing class for the tablettop project.
 *
 */
public class TableTopApp extends CgApplication {

	/**
	 * Constructor
	 */
	public TableTopApp() {
		// Testing code: load a triangle mesh and display it
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
		if (meshes.size() > 0) {
			ITriangleMesh mesh = meshes.get(0);
			mesh.getMaterial().setShaderId(Material.SHADER_PHONG_SHADING);
			getCgRootNode().addChild(new CgNode(mesh, "bunny"));
		}
	}

	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		TableTopApp app = new TableTopApp();
		JoglFrame joglFrame = new JoglFrame(app);
		new JoglSwingUserInterface(app, joglFrame);

	}

}

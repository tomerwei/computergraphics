package cgresearch.studentprojects.comicgenerator;

import java.util.List;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.JoglSwingUserInterface;

/**
 * Testing class for the comic generator master project.
 *
 */
public class ComicGeneratorFrame extends CgApplication {

	/**
	 * Constructor
	 */
	public ComicGeneratorFrame() {
		// Testing code: load a triangle mesh and display it
		ObjFileReader reader = new ObjFileReader();
		List<ITriangleMesh> meshes = reader.readFile("meshes/bunny.obj");
		getCgRootNode().addChild(new CgNode(meshes.get(0), "bunny"));
	}

	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		ComicGeneratorFrame app = new ComicGeneratorFrame();
		JoglFrame joglFrame = new JoglFrame(app);
		new JoglSwingUserInterface(app, joglFrame);
	}
}

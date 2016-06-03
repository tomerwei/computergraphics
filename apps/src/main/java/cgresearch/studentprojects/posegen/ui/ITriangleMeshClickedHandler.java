package cgresearch.studentprojects.posegen.ui;

import java.util.HashMap;
import java.util.List;

import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Interface with callback for clicked triangles in MeshPicking
 * 
 * @author Lars Porep
 * 
 */
public interface ITriangleMeshClickedHandler {

	/**
	 * Callback method, called with clicked Triangles.
	 * 
	 * @param pickedTriangles
	 *            A Hashmap with the clicked meshes and the corresponding
	 *            clicked triangles in each one Just clicked meshes and
	 *            triangles are passed. Otherwise empty
	 */
	public void trianglesClicked(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles);
}

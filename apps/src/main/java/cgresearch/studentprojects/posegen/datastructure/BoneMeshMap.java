package cgresearch.studentprojects.posegen.datastructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

/**
 * Mapping Bones > Mesh (Triangles)
 * 
 * @author Lars
 *
 */
public class BoneMeshMap {

	private ITriangleMesh mesh = null;

	private HashMap<Integer, List<ITriangle>> boneTriangleMap = new HashMap<>();

	public BoneMeshMap(ITriangleMesh mesh) {
		this.mesh = mesh;
	}

	public void linkBoneToTriangles(Integer boneId, List<ITriangle> triangles) {
		if (!boneTriangleMap.containsKey(boneId)) { // New bone
			boneTriangleMap.put(boneId, new LinkedList<ITriangle>());
		}

		List<ITriangle> list = boneTriangleMap.get(boneId);
		list.addAll(triangles);

		// Remove duplicates
		Set<ITriangle> listAsSet = new HashSet<>(list);
		boneTriangleMap.remove(boneId);
		boneTriangleMap.put(boneId, new LinkedList<>(listAsSet));

		System.out.println("SizeListOld: " + list.size() + " SizeAfterSet'ing: " + listAsSet.size());
	}

	public SelectedMesh getLinkedTriangles(Integer boneId) {
		
		return new SelectedMesh(mesh, boneTriangleMap.get(boneId));
		//return boneTriangleMap.get(boneId);
	}
}

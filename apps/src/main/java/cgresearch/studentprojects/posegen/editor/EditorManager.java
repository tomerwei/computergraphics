package cgresearch.studentprojects.posegen.editor;

import cgresearch.core.math.Vector;
import cgresearch.studentprojects.posegen.datastructure.BoneMeshMap;
import cgresearch.studentprojects.posegen.datastructure.SelectedMesh;

/**
 * Handler for interaction with mesh etc.
 * @author Lars
 *
 */
public class EditorManager {
	private final BoneMeshMap boneMeshMap;
	
	public EditorManager(BoneMeshMap boneMeshMap){
		this.boneMeshMap = boneMeshMap;
	}
	
	
	public void rotateBoneId(Integer boneId, double deg, Vector rotationPosition){
		
		SelectedMesh selectedMesh = boneMeshMap.getLinkedTriangles(boneId);
//		List<ITriangle> triangles = 
//		for(ITriangle triangle : triangles){
//			triangle.getA()
//		}
		selectedMesh.moveTriangles(deg, rotationPosition);
	}
	
}

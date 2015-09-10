package cgresearch.studentprojects.shapegrammar.datastructures.building;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettingManager;

public class Building {
	private List<ITriangleMesh> meshes;
	
	/**
	 * Default Constructor For The Building Class
	 */
	public Building(){
		meshes = new ArrayList<ITriangleMesh>();
	}
	
	/**
	 * Add Mesh To The Building
	 * @param mesh ITriangleMesh From A Wall Part
	 */
	public void addMesh(ITriangleMesh mesh){
		meshes.add(mesh);
	}
	
	/**
	 * Visualize The Building. 
	 * All Meshes In The List Will Add On The CgNode
	 */
	public void visualization(CgNode rootNode){
		CgNode building = new CgNode(null, BuildingSettingManager.getInstance().getActualSettings().getBuildingDir());
		rootNode.addChild(building);
		for (ITriangleMesh mesh : meshes) {
			CgNode meshNode = new CgNode(mesh, meshes.indexOf(mesh)+"-Wall");
			building.addChild(meshNode);
		}
		
		Logger.getInstance().message("Building Vizualisiert");
	}
}


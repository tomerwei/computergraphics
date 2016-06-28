package cgresearch.studentprojects.registration;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.ui.IApplicationControllerGui;

public class Surface extends CgApplication{

	/**
	 * TODO: UPdate Dreate usw überarbeiten, damit das richtig funzt
	 */

	private final CgRootNode rootNode;
	CgNode basePointCloudNode;

	public Surface(CgRootNode rootNode){
		this.rootNode = rootNode;

	}

	public void create(IPointCloud pointCloud){

		basePointCloudNode = new CgNode(pointCloud, "pointCloud");
		rootNode.addChild(basePointCloudNode);


	}

	public void update(){
		rootNode.update(rootNode, getCgRootNode());
	}

	public void deleteNode(IPointCloud pointCloud){
		basePointCloudNode.deleteNode();
		rootNode.update(rootNode, getCgRootNode());

	}


}

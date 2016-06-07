package cgresearch.studentprojects.posegen.datastructure;

import java.util.ArrayList;
import java.util.List;

import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;

public class SkelettNode extends CgNode {
	
	public SkelettNode(ICgNodeContent content, String name){
		//Content irrelevant, soll nur groupen.
		super(content, name);
	}
	
	public List<Bone> getBones(){
		List<Bone> bones = new ArrayList<>();
		for(int i=0; i<this.getNumChildren(); i++){
			CgNode node = this.getChildNode(i);
			ICgNodeContent content = node.getContent();
			if(content instanceof Bone){
				bones.add((Bone)content);
			}
		}
		
		return bones;
	}
}

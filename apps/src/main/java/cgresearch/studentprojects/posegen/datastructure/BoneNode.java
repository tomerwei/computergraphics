package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.graphics.scenegraph.CgNode;

public class BoneNode extends CgNode {

	public BoneNode(Bone content, String name) {
		super(content, name);
		// this.addChild(new BoneRotationPickable(new Vector(0.0,0.0,0.0)));
	}

	public Bone getBone() {
		return (Bone) this.getContent();

	}
}

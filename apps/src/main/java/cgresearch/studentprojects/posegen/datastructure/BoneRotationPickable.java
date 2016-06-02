package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.picking.CgApplicationPickable;
import cgresearch.graphics.picking.PickingItem;

public class BoneRotationPickable extends CgApplicationPickable {

	private final Bone bone;
	private final String id;

	public BoneRotationPickable(Bone bone) {
		super();
		this.bone = bone;
		Vector boneStartPosition = bone.getStartPosition();
		PickingItem item = new PickingItem(boneStartPosition);
		this.id = item.getId();
		addPickingItem(item);
		// VectorFactory.createVector3(bonePosition.get(0), bonePosition.get(1),
		// bonePosition.get(2))
		// this.addPickingItem(new PickingItem(bonePosition));
	}

	@Override
	public void itemPicked(String id) {
		Logger.getInstance().message("ID: " + id + " - Bone joint picked");
	}

	@Override
	public void itemMoved(String id) {
		// TODO Auto-generated method stub
		if (this.id.equals(id)) {
			Logger.getInstance().message("ID: " + id + " - Rotated by 1.0 degree");
			bone.rotateUmBoneStart(1.0);
		}

	}

}

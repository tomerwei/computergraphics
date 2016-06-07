package cgresearch.studentprojects.posegen.editor;

import java.util.HashMap;
import java.util.List;

import cgresearch.studentprojects.posegen.datastructure.Bone;

/**
 * Registry for current status of editor frame: i.E. currently selected Bone
 * etc.
 * 
 * @author Lars
 *
 */
public class EditorStatus {

	private HashMap<Integer, Bone> bonesList = new HashMap<>(); // Map BoneId ->
																// Bone

	private Integer currentlySelectedBoneId = null;

	public EditorStatus(List<Bone> bones) {
		initBonesList(bones);
	}

	public void selectBone(Bone bone) {
		Integer boneId = bone.getId();

		// A bone is selected (not null), disable it
		if (!(null == currentlySelectedBoneId)) {
			bonesList.get(currentlySelectedBoneId).setColorNotSelected();
		}
		bonesList.get(boneId).setColorSelected();
		currentlySelectedBoneId = boneId;
	}

	private void initBonesList(List<Bone> bones) {
		for (Bone bone : bones) {
			bonesList.put(bone.getId(), bone);
		}
	}

}

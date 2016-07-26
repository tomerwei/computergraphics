package cgresearch.studentprojects.posegen.datastructure;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.ICgNodeContent;

public class SkelettNode extends CgNode {
	
	public SkelettNode(ICgNodeContent content, String name){
		//Content irrelevant, soll nur groupen.
		super(content, name);
		generateHumanSkelett();
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
	
	public void generateHumanSkelett(){
		BoneNode rootBase = generateBone(null, new Vector(0.0,0.0,0.0), "rootNode");
		
		BoneNode waistToTop = generateBone(rootBase.getBone(), new Vector(0.0, 0.650, 0.0), "waistToTop");
		BoneNode shoulderToLeft = generateBone(waistToTop.getBone(), new Vector(-0.3, 0.040, 0.0), "shoulderToLeft");
		BoneNode shoulderToRight = generateBone(waistToTop.getBone(), new Vector(0.3, 0.040, 0.0), "shoulderToRight");
		
		BoneNode leftUpperArm = generateBone(shoulderToLeft.getBone(), new Vector(-0.25, -0.2, 0.0), "leftUpperArm");
		BoneNode leftLowerArm = generateBone(leftUpperArm.getBone(), new Vector(-0.3, -0.2, 0.0), "leftLowerArm");
		BoneNode leftHand = generateBone(leftLowerArm.getBone(), new Vector(-0.15, -0.15, 0.0), "leftHand");
		
		BoneNode rightUpperArm = generateBone(shoulderToRight.getBone(), new Vector(0.25, -0.2, 0.0), "rightUpperArm");
		BoneNode rightLowerArm = generateBone(rightUpperArm.getBone(), new Vector(0.3, -0.2, 0.0), "rightLowerArm");
		BoneNode rightHand = generateBone(rightLowerArm.getBone(), new Vector(0.15, -0.15, 0.0), "rightHand");
		
		BoneNode neck = generateBone(waistToTop.getBone(), new Vector(0.0, 0.25, 0.0), "neck");
		BoneNode head = generateBone(neck.getBone(), new Vector(0.0, 0.25, 0.0), "head");

		BoneNode leftWaist = generateBone(rootBase.getBone(), new Vector(-0.2, -0.10, 0.0), "leftWaist");
		BoneNode leftUpperLeg = generateBone(leftWaist.getBone(), new Vector(-0.025, -0.60, 0.0), "leftUpperLeg");
		BoneNode leftLowerLeg = generateBone(leftUpperLeg.getBone(), new Vector(-0.025, -0.55, 0.0), "leftLowerLeg");
		BoneNode leftFoot = generateBone(leftLowerLeg.getBone(), new Vector(-0.05, -0.10, 0.0), "leftFoot");

		BoneNode rightWaist = generateBone(rootBase.getBone(), new Vector(0.2, -0.10, 0.0), "rightWaist");
		BoneNode rightUpperLeg = generateBone(rightWaist.getBone(), new Vector(0.025, -0.60, 0.0), "rightUpperLeg");
		BoneNode rightLowerLeg = generateBone(rightUpperLeg.getBone(), new Vector(0.025, -0.55, 0.0), "rightLowerLeg");
		BoneNode rightFoot = generateBone(rightLowerLeg.getBone(), new Vector(0.05, -0.10, 0.0), "rightFoot");
		// BoneNode rightLowerLowerLeg = addBone(rightLowerLeg.getBone(), new
		// Vector(0.05, -0.80, 0.0), "rightLowerLowerLeg");

		this.addChild(waistToTop);
		this.addChild(shoulderToLeft);
		this.addChild(shoulderToRight);
		this.addChild(leftUpperArm);
		this.addChild(leftLowerArm);
		this.addChild(leftHand);
		this.addChild(rightUpperArm);
		this.addChild(rightLowerArm);
		this.addChild(rightHand);
		this.addChild(neck);
		this.addChild(head);

		this.addChild(leftWaist);
		this.addChild(leftUpperLeg);
		this.addChild(leftLowerLeg);
		this.addChild(leftFoot);

		this.addChild(rightWaist);
		this.addChild(rightUpperLeg);
		this.addChild(rightLowerLeg);
		this.addChild(rightFoot);
	}
	
	/**
	 * 
	 * @param parentBone
	 *            Parent bone to attach this new bone to
	 * @param offset
	 *            Offset from the base (parentBone), towards the end of this new
	 *            one
	 * @param boneName
	 *            Name of the bone
	 * @return The newly created BoneNode.
	 */
	private static BoneNode generateBone(Bone parentBone, Vector offset, String boneName) {
		Bone newBone = new Bone(parentBone, offset);
		BoneNode newBoneNode = new BoneNode(newBone, boneName);
		CgNode boneStartPickupNode = new CgNode(newBone.getStartPositionPickup(), "BoneStartPositionPickup");
		newBoneNode.addChild(boneStartPickupNode);
		if (null != newBone.getEndPositionPickup()) {
			CgNode boneEndPickupNode = new CgNode(newBone.getEndPositionPickup(), "BoneEndPositionPickup");
			newBoneNode.addChild(boneEndPickupNode);
			// wird noch aufgebaut das skelett, desweegn ist noch kein child
			// bone da und der bone weiss nicht das er kein blatt ist
		}

		// new BoneRotationPickable(newBone, editorManager);
		return newBoneNode;
	}
}

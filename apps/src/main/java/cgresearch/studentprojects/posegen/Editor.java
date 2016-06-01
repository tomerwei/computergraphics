package cgresearch.studentprojects.posegen;

import java.util.Observable;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.studentprojects.posegen.datastructure.Bone;
import cgresearch.studentprojects.posegen.datastructure.BoneNode;
import cgresearch.studentprojects.posegen.datastructure.BoneRotationPickable;
import cgresearch.studentprojects.posegen.datastructure.Canvas;
import cgresearch.studentprojects.posegen.datastructure.CanvasNode;
import cgresearch.studentprojects.posegen.datastructure.SkelettNode;

public class Editor extends CgApplication {

	private CgNode root;

	public Editor(){
		root = getCgRootNode();
		CgNode skelett = generateSkelett();
		
		Canvas canvas = new Canvas();
		CanvasNode canvasNode = new CanvasNode(canvas, "Canvas1");
		
//		Light
		
//		root.addChild(light);
		root.addChild(canvasNode);
		
		root.addChild(skelett);
//		Picking.getInstance().setScaling(1.0);
//		BoneRotationPickable boneRotationPickable = new BoneRotationPickable();
	}
	
	  @Override
	  public void update(Observable o, Object arg) {
//	    if (o == surface) {
//	      createMesh4Surface();
//	      mesh.updateRenderStructures();
//	    }
	    super.update(o, arg);
	  }
	
	public static void main(String[] args) {
	    // TODO Auto-generated method stub
		ResourcesLocator.getInstance().parseIniFile("resources.ini");

		
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
//		appLauncher.addRenderable(renderable);//:JoglFrame in appLauncher nimmt Renderfactories register...()
		CgApplication app = new Editor();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		
		appLauncher.setUiSystem(UI.JOGL_SWING);

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
	private static BoneNode addBone(Bone parentBone, Vector offset, String boneName) {
		Bone newBone = new Bone(parentBone, offset);
		BoneNode newBoneNode = new BoneNode(newBone, boneName);
		//BoneRotationPickable boneRotationPickable = 
		new BoneRotationPickable(newBone);
		return newBoneNode;
	}

	// private static BoneNode addBone(BoneNode parentBoneNode, Vector offset,
	// String boneName){
	// return addBone(parentBoneNode.getBone(), offset, boneName);
	// }

	private SkelettNode generateSkelett() {
		
		SkelettNode skelett = new SkelettNode(null, "Skelett1");

		BoneNode waistToTop = addBone(null, new Vector(0.0, 1.0, 0.0), "waistToTop");
		BoneNode shoulderToLeft = addBone(waistToTop.getBone(), new Vector(-0.5, 0.0, 0.0), "shoulderToLeft");
		BoneNode shoulderToRight = addBone(waistToTop.getBone(), new Vector(0.5, 0.0, 0.0), "shoulderToRight");
		BoneNode leftUpperArm = addBone(shoulderToLeft.getBone(), new Vector(-0.2, -0.6, 0.0), "leftUpperArm");
		BoneNode leftLowerArm = addBone(leftUpperArm.getBone(), new Vector(-0.05, -0.4, 0.0), "leftLowerArm");
		BoneNode rightUpperArm = addBone(shoulderToRight.getBone(), new Vector(0.2, -0.6, 0.0), "rightUpperArm");
		BoneNode rightLowerArm = addBone(rightUpperArm.getBone(), new Vector(0.05, -0.4, 0.0), "rightLowerArm");
		BoneNode neck = addBone(waistToTop.getBone(), new Vector(0.0, 0.35, 0.0), "neck");
		BoneNode head = addBone(neck.getBone(), new Vector(0.0, 0.45, 0.0), "head");

		BoneNode leftWaist = addBone(null, new Vector(-0.5, 0.0, 0.0), "leftWaist");
		BoneNode leftUpperLeg = addBone(leftWaist.getBone(), new Vector(-0.15, -1.0, 0.0), "leftUpperLeg");
		BoneNode leftLowerLeg = addBone(leftUpperLeg.getBone(), new Vector(-0.05, -0.80, 0.0), "leftLowerLeg");

		BoneNode rightWaist = addBone(null, new Vector(0.5, 0.0, 0.0), "rightWaist");
		BoneNode rightUpperLeg = addBone(rightWaist.getBone(), new Vector(0.0, -1.0, 0.0), "rightUpperLeg");
		BoneNode rightLowerLeg = addBone(rightUpperLeg.getBone(), new Vector(0.05, -0.80, 0.0), "rightLowerLeg");

		skelett.addChild(waistToTop);
		skelett.addChild(shoulderToLeft);
		skelett.addChild(shoulderToRight);
		skelett.addChild(leftUpperArm);
		skelett.addChild(leftLowerArm);
		skelett.addChild(rightUpperArm);
		skelett.addChild(rightLowerArm);
		skelett.addChild(neck);
		skelett.addChild(head);

		skelett.addChild(leftWaist);
		skelett.addChild(leftUpperLeg);
		skelett.addChild(leftLowerLeg);

		skelett.addChild(rightWaist);
		skelett.addChild(rightUpperLeg);
		skelett.addChild(rightLowerLeg);

		// rightUpperLeg.getBone().moveBoneEndByOffset(new Vector(0.5, 0.0,
		// 0.0));

		rightUpperLeg.getBone().rotateUmBoneStart(45);
		leftUpperArm.getBone().rotateUmBoneStart(-90);
		rightLowerArm.getBone().rotateUmBoneStart(25);
		rightUpperArm.getBone().rotateUmBoneStart(25);

		leftLowerLeg.getBone().rotateUmBoneStart(25);

		head.getBone().rotateUmBoneStart(-20);

		// rightWaist.getBone().rotate(90.0);

		
//		VectorFactory.createVector3(0.3, 0.5, 0.0)
//		PickingItem pickingItem = new PickingItem(VectorFactory.createVector3(0.0, 1.0, 0.0));

//		boneRotationPickable.addPickingItem(pickingItem);
		
		
//		parentNode.addChild(skelett);
		return skelett;
//		root.addChild(boneRotationPickable);

		// Bone bone1 = new Bone(null, new Vector(0.0, 1.0, 0.0));
		// Bone bone2 = new Bone(bone1, new Vector(-0.5, 0.0, 0.0));
		// Bone bone3 = new Bone(bone1, new Vector(0.5, 0.0, 0.0));
		// BoneNode boneNode1 = new BoneNode(bone1, "waistToTop");
		// BoneNode boneNode2 = new BoneNode(bone2, "shoulderToLeft");
		// BoneNode boneNode3 = new BoneNode(bone3, "shoulderToRight");

		// LeftArm
		// Bone bone4 = new Bone(bone2, new Vector(-0.2, -0.6, 0.0));
		// Bone bone5 = new Bone(bone4, new Vector(-0.05, -0.4, 0.0));
		// BoneNode boneNode4 = new BoneNode(bone4, "leftUpperArm");
		// BoneNode boneNode5 = new BoneNode(bone5, "leftLowerArm");

		// RightArm
		// Bone bone6 = new Bone(bone3, new Vector(0.2, -0.6, 0.0));
		// Bone bone7 = new Bone(bone6, new Vector(0.05, -0.4, 0.0));
		// BoneNode boneNode6 = new BoneNode(bone6, "rightUpperArm");
		// BoneNode boneNode7 = new BoneNode(bone7, "rightLowerArm");

		// Neck
		// Bone bone8 = new Bone(bone1, new Vector(0.0, 0.35, 0.0));
		// BoneNode boneNode8 = new BoneNode(bone8, "neck");

		// Head
		// Bone bone9 = new Bone(bone8, new Vector(0.0, 0.45, 0.0));
		// BoneNode boneNode9 = new BoneNode(bone9, "head");

		// BEINE:

		// Bone leftWaist = new Bone(null, new Vector(-0.5, 0.0, 0.0));
		// BoneNode leftWaistNode = new BoneNode(leftWaist, "leftWaist");
		// skelett.addChild(leftWaistNode);
		// Bone leftUpperLeg = new Bone(leftWaist, new Vector(-0.15, -1.0,
		// 0.0));
		// BoneNode leftUpperLegNode = new BoneNode(leftUpperLeg,
		// "leftUpperLeg");
		// skelett.addChild(leftUpperLegNode);
		//
		// Bone leftLowerLeg = new Bone(leftUpperLeg, new Vector(-0.05, -0.80,
		// 0.0));
		// BoneNode leftLowerLegNode = new BoneNode(leftLowerLeg,
		// "leftLowerLeg");
		// skelett.addChild(leftLowerLegNode);

		// RECHTES BEIN
		// Bone rightWaist = new Bone(null, new Vector(0.5, 0.0, 0.0));
		// BoneNode rightWaistNode = new BoneNode(rightWaist, "rightWaist");
		// skelett.addChild(rightWaistNode);
		// Bone rightUpperLeg = new Bone(rightWaist, new Vector(0.15, -1.0,
		// 0.0));
		// BoneNode rightUpperLegNode = new BoneNode(rightUpperLeg,
		// "rightUpperLeg");
		// skelett.addChild(rightUpperLegNode);
		//
		// Bone rightLowerLeg = new Bone(rightUpperLeg, new Vector(0.05, -0.80,
		// 0.0));
		// BoneNode rightLowerLegNode = new BoneNode(rightLowerLeg,
		// "rightLowerLeg");
		// skelett.addChild(rightLowerLegNode);

		// skelett.addChild(boneNode1);
		// skelett.addChild(boneNode2);
		// skelett.addChild(boneNode3);
		// skelett.addChild(boneNode4);
		// skelett.addChild(boneNode5);
		// skelett.addChild(boneNode6);
		// skelett.addChild(boneNode7);
		// skelett.addChild(boneNode8);
		// skelett.addChild(boneNode9);
		// root.addChild(skelett);
	}

	// @Override
	// public void update(Observable o, Object arg) {
	// super.update(o, arg);
	//
	// }
}

package cgresearch.studentprojects.posegen.editor;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.studentprojects.posegen.datastructure.Bone;
import cgresearch.studentprojects.posegen.datastructure.BoneMeshMap;
import cgresearch.studentprojects.posegen.datastructure.BoneNode;
import cgresearch.studentprojects.posegen.datastructure.BoneRotationPickable;
import cgresearch.studentprojects.posegen.datastructure.BoneStartPositionPickup;
import cgresearch.studentprojects.posegen.datastructure.Canvas;
import cgresearch.studentprojects.posegen.datastructure.CanvasNode;
import cgresearch.studentprojects.posegen.datastructure.SkelettNode;
import cgresearch.studentprojects.posegen.ui.ITriangleMeshClickedHandler;
import cgresearch.studentprojects.posegen.ui.TriangleMeshInteractionTool;
import cgresearch.studentprojects.posegen.ui.TriangleMeshPicking;

public class Editor extends CgApplication {

	private EditorStatus editorStatus;
	private static EditorManager editorManager;
	private CgNode root;
	private SkelettNode skelett;
	private Canvas canvas = new Canvas(); // init here to forward the ref
	private static JoglFrame joglFrame = null; // To be set in main

	private BoneMeshMap boneMeshMap;

	public Editor() {
		root = getCgRootNode();

		boneMeshMap = new BoneMeshMap(canvas);
		editorManager = new EditorManager(boneMeshMap); // Has to be called
														// befor bone added

		skelett = generateSkelett();
		editorStatus = new EditorStatus(skelett.getBones());

		root.addChild(skelett);

	}

	public void initEditorContent() {
		// canvas = new Canvas();
		CanvasNode canvasNode = new CanvasNode(canvas, "Canvas1");

		TriangleMeshPicking meshPicking = initTrianglePicking();
		TriangleMeshPicking bonePicking = initBonePicking(skelett.getBones());
		TriangleMeshPicking boneStartPosition = initBoneStartPositionPicking(skelett.getBones());

		TriangleMeshInteractionTool meshInteractionTool = new TriangleMeshInteractionTool(joglFrame);
		meshInteractionTool.addMeshPicking(meshPicking);
		meshInteractionTool.addMeshPicking(bonePicking);
		meshInteractionTool.addMeshPicking(boneStartPosition);

		root.addChild(canvasNode);
	}

	private TriangleMeshPicking initBonePicking(List<Bone> bones) {
		TriangleMeshPicking meshPicking = new TriangleMeshPicking();

		meshPicking.registerAllPickableMesh(bones);

		ITriangleMeshClickedHandler boneMeshClickedHandler = new ITriangleMeshClickedHandler() {
			@Override
			public void trianglesClicked(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {

				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				if (iterator.hasNext()) { // Select the first bone selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof Bone) {
						editorStatus.selectBone(((Bone) mesh));
						canvas.enableWireframe();
					}
				}
			}

			@Override
			public void trianglesDragged(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMoved(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles, Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMouseDown(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMouseRelease(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

		};
		meshPicking.registerTriangleMeshClickedHandler(boneMeshClickedHandler);
		return meshPicking;
	}

	private TriangleMeshPicking initBoneStartPositionPicking(List<Bone> bones) {
		TriangleMeshPicking meshPicking = new TriangleMeshPicking();

		List<BoneStartPositionPickup> boneStartPositionPickupList = new ArrayList<>();
		Iterator<Bone> boneIterator = bones.iterator();
		while (boneIterator.hasNext()) {
			Bone bone = boneIterator.next();
			BoneStartPositionPickup boneStartPositionPickup = bone.getStartPositionPickup();
			boneStartPositionPickupList.add(boneStartPositionPickup);
		}

		meshPicking.registerAllPickableMesh(boneStartPositionPickupList);

		ITriangleMeshClickedHandler boneStartPositionClickedHandler = new ITriangleMeshClickedHandler() {

			@Override
			public void trianglesDragged(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				if (iterator.hasNext()) { // Select the first
											// BoneStartPositionPickup selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof BoneStartPositionPickup) {
						((BoneStartPositionPickup) mesh).dragged(coordsClicked);
					}
				}
			}

			@Override
			public void trianglesMouseDown(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				if (iterator.hasNext()) { // Select the first
											// BoneStartPositionPickup selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof BoneStartPositionPickup) {
						((BoneStartPositionPickup) mesh).pickedUp();
					}
				}
			}

			@Override
			public void trianglesMouseRelease(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				if (iterator.hasNext()) { // Select the first
											// BoneStartPositionPickup selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof BoneStartPositionPickup) {
						((BoneStartPositionPickup) mesh).dropped();
					}
				}
			}

			@Override
			public void trianglesClicked(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMoved(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles, Vector coordsClicked) {
				// TODO Auto-generated method stub

			}
		};
		meshPicking.registerTriangleMeshClickedHandler(boneStartPositionClickedHandler);
		return meshPicking;
	}

	private TriangleMeshPicking initTrianglePicking() {
		TriangleMeshPicking meshPicking = new TriangleMeshPicking();
		meshPicking.registerPickableMesh(canvas);
		ITriangleMeshClickedHandler triangleMeshClickedHandler = new ITriangleMeshClickedHandler() {
			@Override
			public void trianglesClicked(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					ITriangleMesh mesh = iterator.next();
					boneMeshMap.linkBoneToTriangles(editorStatus.getCurrentSelectedBone(), pickedTriangles.get(mesh));
					// for (ITriangle triangle : pickedTriangles.get(mesh)) {
					//
					// triangle.setVisible(false);
					// }
				}
			}

			@Override
			public void trianglesDragged(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMoved(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles, Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMouseDown(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

			@Override
			public void trianglesMouseRelease(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub

			}

		};
		meshPicking.registerTriangleMeshClickedHandler(triangleMeshClickedHandler);
		return meshPicking;
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
	}

	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");

		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		Editor app = new Editor();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		joglFrame = appLauncher.getJoglFrame();
		app.initEditorContent(); // Has to be called after renderSystem is set
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
		CgNode boneStartPickupNode = new CgNode(newBone.getStartPositionPickup(), "BoneStartPositionPickup");
		newBoneNode.addChild(boneStartPickupNode);

		// new BoneRotationPickable(newBone, editorManager);
		return newBoneNode;
	}

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
		BoneNode rightUpperLeg = addBone(rightWaist.getBone(), new Vector(0.15, -1.0, 0.0), "rightUpperLeg");
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

		// rightUpperLeg.getBone().rotateUmBoneStart(45);
		// leftUpperArm.getBone().rotateUmBoneStart(-90);
		// rightLowerArm.getBone().rotateUmBoneStart(25);
		// rightUpperArm.getBone().rotateUmBoneStart(25);

		// leftLowerLeg.getBone().rotateUmBoneStart(25);

		// head.getBone().rotateUmBoneStart(-20);

		return skelett;
	}
}

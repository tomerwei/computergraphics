package cgresearch.studentprojects.posegen.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.graphics.scenegraph.LightSource;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.studentprojects.posegen.datastructure.Bone;
import cgresearch.studentprojects.posegen.datastructure.BoneEndPositionPickup;
import cgresearch.studentprojects.posegen.datastructure.BoneMeshMap;
import cgresearch.studentprojects.posegen.datastructure.BoneStartPositionPickup;
import cgresearch.studentprojects.posegen.datastructure.Canvas;
import cgresearch.studentprojects.posegen.datastructure.CanvasNode;
import cgresearch.studentprojects.posegen.datastructure.IBoneMovePositionPickup;
import cgresearch.studentprojects.posegen.datastructure.SkelettNode;
import cgresearch.studentprojects.posegen.ui.ITriangleMeshClickedHandler;
import cgresearch.studentprojects.posegen.ui.TriangleMeshInteractionTool;
import cgresearch.studentprojects.posegen.ui.TriangleMeshPicking;

public class Editor extends CgApplication {

	private EditorStatus editorStatus;
	private static EditorManager editorManager;
	private CgRootNode root;
	private SkelettNode skelett;
	private Canvas canvas = new Canvas(); // init here to forward the ref
	private static JoglFrame joglFrame = null; // To be set in main

	boolean selectSingleMode = false; // SELECT SINGLE OR ALL PICKED position
										// movers at once
	private boolean boneMeshMapNeedsUpdate = true; // A bone has been moved.
													// autoLink needs to be
													// called
	private BoneMeshMap boneMeshMap;

	public Editor() {
		root = getCgRootNode();

		editorManager = new EditorManager(boneMeshMap); // Has to be called
														// befor bone added

		skelett = generateSkelett();
		boneMeshMap = new BoneMeshMap(canvas, skelett.getBones());
		editorStatus = new EditorStatus(skelett.getBones());

		LightSource light1 = new LightSource(LightSource.Type.POINT).setPosition(new Vector(-1.0, 5, 0.5));
		LightSource light2 = new LightSource(LightSource.Type.POINT).setPosition(new Vector(-1.0, 5, 0.5));
		LightSource light3 = new LightSource(LightSource.Type.POINT).setPosition(new Vector(1.0, 5, -0.5));
		LightSource light4 = new LightSource(LightSource.Type.POINT).setPosition(new Vector(1.0, 5, -0.5));
		LightSource light5 = new LightSource(LightSource.Type.POINT).setPosition(new Vector(1.0, -5, -0.5));
		light1.setColor(new Vector(1, 1, 1));
		light2.setColor(new Vector(1, 1, 1));
		light3.setColor(new Vector(1, 1, 1));
		light4.setColor(new Vector(1, 1, 1));
		light5.setColor(new Vector(1, 1, 1));
		root.addLight(light1);
		root.addLight(light2);
		root.addLight(light3);
		root.addLight(light4);
		root.addLight(light5);

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

		joglFrame.getCanvasView();
		root.addChild(canvasNode);
	}

	/**
	 * Tests if the mesh needs to be autolinked and links it if needed.
	 */
	private void autoLinkBonesToMeshIfDirty() {

		if (boneMeshMapNeedsUpdate) {
			System.out.println("AutoLinked");
			boneMeshMap.autoLinkTrianglesToBones();
			boneMeshMapNeedsUpdate = false;
		}

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
						// canvas.enableWireframe();
						Bone selectedBone = ((Bone) mesh);
						autoLinkBonesToMeshIfDirty();
						selectedBone.rotateUmDrehpunkt(-3.0, selectedBone.getStartPosition());
						// skelett.getBones().get(3).rotateUmDrehpunkt(4, new
						// Vector(0, 0, 0));
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

		List<IBoneMovePositionPickup> bonePositionPickupList = new ArrayList<>();
		Iterator<Bone> boneIterator = bones.iterator();
		while (boneIterator.hasNext()) {
			Bone bone = boneIterator.next();
			BoneStartPositionPickup boneStartPositionPickup = bone.getStartPositionPickup();
			bonePositionPickupList.add(boneStartPositionPickup);

			BoneEndPositionPickup boneEndPositionPickup = bone.getEndPositionPickup();
			if (null != boneEndPositionPickup) {
				bonePositionPickupList.add(boneEndPositionPickup);
			}

		}

		meshPicking.registerAllPickableMesh(bonePositionPickupList);

		ITriangleMeshClickedHandler boneStartPositionClickedHandler = new ITriangleMeshClickedHandler() {

			@Override
			public void trianglesDragged(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO Auto-generated method stub
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				while (iterator.hasNext()) { // Select the first
												// BoneStartPositionPickup
												// selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof IBoneMovePositionPickup) {
						boneMeshMapNeedsUpdate = true;
						((IBoneMovePositionPickup) mesh).dragged(coordsClicked);

					}
					if (selectSingleMode == true) {
						break;
					}
				}
			}

			@Override
			public void trianglesMouseDown(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				while (iterator.hasNext()) { // Select the first
												// BoneStartPositionPickup
												// selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof IBoneMovePositionPickup) {
						boneMeshMapNeedsUpdate = true;
						((IBoneMovePositionPickup) mesh).pickedUp();
					}
					if (selectSingleMode == true) {
						break;
					}
				}
			}

			@Override
			public void trianglesMouseRelease(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				while (iterator.hasNext()) { // Select the first
												// BoneStartPositionPickup
												// selected
					ITriangleMesh mesh = iterator.next();
					if (mesh instanceof IBoneMovePositionPickup) {
						boneMeshMapNeedsUpdate = true;
						((IBoneMovePositionPickup) mesh).dropped();
					}
					if (selectSingleMode == true) {
						break;
					}
				}
			}

			@Override
			public void trianglesClicked(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// System.out.println("AutoLinked");
				// boneMeshMap.autoLinkTrianglesToBones();

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
					List<IVertex> verticesToLink = new ArrayList<>();
					for (ITriangle triangle : pickedTriangles.get(mesh)) {
						verticesToLink.add(mesh.getVertex(triangle.getA()));
						verticesToLink.add(mesh.getVertex(triangle.getB()));
						verticesToLink.add(mesh.getVertex(triangle.getC()));

					}
					List<IVertex> noDuplicates = new ArrayList<IVertex>(new LinkedHashSet<IVertex>(verticesToLink));
					// ORIG
					// boneMeshMap.linkBoneToTriangles(editorStatus.getCurrentSelectedBone(),
					// noDuplicates);
				}
			}

			@Override
			public void trianglesDragged(HashMap<ITriangleMesh, List<ITriangle>> pickedTriangles,
					Vector coordsClicked) {
				// TODO UNTESTED
				Set<ITriangleMesh> keySet = pickedTriangles.keySet();
				Iterator<ITriangleMesh> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					ITriangleMesh mesh = iterator.next();
					List<IVertex> verticesToLink = new ArrayList<>();
					for (ITriangle triangle : pickedTriangles.get(mesh)) {
						verticesToLink.add(mesh.getVertex(triangle.getA()));
						verticesToLink.add(mesh.getVertex(triangle.getB()));
						verticesToLink.add(mesh.getVertex(triangle.getC()));

					}
					List<IVertex> noDuplicates = new ArrayList<IVertex>(new LinkedHashSet<IVertex>(verticesToLink));

					// ORIG
					// boneMeshMap.linkBoneToTriangles(editorStatus.getCurrentSelectedBone(),
					// noDuplicates);

					// old triangle based
					// ITriangleMesh mesh = iterator.next();
					// boneMeshMap.linkBoneToTriangles(editorStatus.getCurrentSelectedBone(),
					// pickedTriangles.get(mesh).get(0));
				}

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

	private SkelettNode generateSkelett() {

		SkelettNode skelett = new SkelettNode(null, "Skelett1");

		// skelett.addChild(rightLowerLowerLeg);

		// rightUpperLeg.getBone().rotateUmBoneStart(45);
		// leftUpperArm.getBone().rotateUmBoneStart(-90);
		// rightLowerArm.getBone().rotateUmBoneStart(25);
		// rightUpperArm.getBone().rotateUmBoneStart(25);

		// leftLowerLeg.getBone().rotateUmBoneStart(25);

		// head.getBone().rotateUmBoneStart(-20);

		return skelett;
	}
}

package cgresearch.studentprojects.posegen.ui;

import java.awt.BorderLayout;
import java.util.HashMap;

import cgresearch.rendering.jogl.ui.JoglCanvas;
import cgresearch.rendering.jogl.ui.JoglFrame;
import cgresearch.rendering.jogl.ui.QuickOptionToolBar;

public class TriangleMeshInteractionTool {

	/**
	 * Creates a Toolbar with buttons
	 * Adds Toolbar with its buttons to a frame
	 * Links canvas-mouseinteractions to TriangleMeshPicking to react to input 
	 * 
	 * Combines buttons, TriangleMeshPicking interaction and links it to a frame/canvas
	 */
	private int meshPickingId = 0; // Counter for MeshPicking ids to find inside
									// hashmap
	private HashMap<Integer, TriangleMeshPicking> meshPickingMap = new HashMap<>();

	private ToolBar toolBar;

	private JoglCanvas view; 
	
	public TriangleMeshInteractionTool(JoglFrame frameToAddButtonsTo) {
		this.toolBar = new ToolBar();
		frameToAddButtonsTo.add(toolBar,BorderLayout.EAST);
		view = frameToAddButtonsTo.getCanvasView();
	}

	public void addMeshPicking(TriangleMeshPicking meshPicking) {
		meshPicking.addJogleCanvas(view); //Enable it to listen to Canvas Events like mouseClicks
		meshPickingMap.put(meshPickingId, meshPicking);
		addButtonForMesh(meshPickingId); // Mesh with id has to be put into map
											// first.
		meshPickingId++; // Aktualisiere ID Counter
	}

	private void addButtonForMesh(Integer id) {
		String iconNotActivePath = "icons/picking.png";
		String iconActivePath = "icons/picking_active.png";

		ToggleButtonIcon button = new ToggleButtonIcon(iconNotActivePath, iconActivePath) {
			@Override
			public void clicked() {
				super.clicked(); // Toggle icon
				boolean newActiveStatus = !meshPickingMap.get(id).isActive();
				meshPickingMap.get(id).setActive(newActiveStatus);
				toolBar.updateUI(); //Korrekt?
			}
		};
		toolBar.addIcon(button);
		toolBar.updateUI(); //Korrekt?
		//TODO mark as dirty? Redraw ui?
	}

}

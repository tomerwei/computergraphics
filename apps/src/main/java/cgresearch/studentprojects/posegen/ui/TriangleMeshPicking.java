package cgresearch.studentprojects.posegen.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Ray3D;
import cgresearch.core.math.Vector;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.rendering.jogl.ui.JoglCanvas;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * Central singleton instance for mesh picking.
 * 
 * @author Lars Porep
 *
 */
public class TriangleMeshPicking implements Observable, MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {

	/**
	 * List of pickable meshes.
	 */
	private List<ITriangleMesh> pickingItems = new ArrayList<ITriangleMesh>();
	private List<ITriangleMeshClickedHandler> clickedHandler = new ArrayList<>();
	/**
	 * This flag indicates if the picking mode is active
	 */
	private boolean active = false;

	private JoglCanvas joglCanvas = null;

	public TriangleMeshPicking() {

	}

	public List<ITriangle> getTriangles(ITriangleMesh triangleMesh) {
		List<ITriangle> triangles = new LinkedList<>();

		for (int index = 0; index < triangleMesh.getNumberOfTriangles(); index++) {
			triangles.add(triangleMesh.getTriangle(index));
		}

		return triangles;
	}

	public void addJogleCanvas(JoglCanvas view) {
		if (null != joglCanvas) {
			Logger.getInstance().error("addJogleCanvas in MeshPicking should only be set once!");
			return;
		}
		joglCanvas = view;
		joglCanvas.addMouseListener(this);
		joglCanvas.addMouseMotionListener(this);
		joglCanvas.addMouseWheelListener(this);
		joglCanvas.addKeyListener(this);
	}

	/**
	 * Getter.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Setter.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Test if this MeshPicking instance should handle clicks
	 * 
	 * @return boolean
	 */
	private boolean activeCheck() {
		if (!isActive()) {
			Logger.getInstance().message("MeshPicking isActive == false");
			return false; // Not active
		}
		if (pickingItems.size() == 0) {
			return false;
		}
		if (clickedHandler.size() == 0) {
			Logger.getInstance()
					.error("No clickhandler for MeshPicking provided. Add at least one ITriangleMeshClickedHandler");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param x
	 *            mouse clicked x
	 * @param y
	 *            mouse clicked y
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 * @param perspectiveAngle
	 * @return HashMap<ITriangleMesh, List<ITriangle>> Map with meshes and list
	 *         of selected Triangles for each one
	 */
	public HashMap<ITriangleMesh, List<ITriangle>> checkForIntersections(int x, int y, int width, int height,
			double perspectiveAngle) {
		Ray3D ray = getRayFromClickCoordinates(x, y, width, height, perspectiveAngle);
		// Für jedes triangleMesh eine liste mit allen getroffenen triangles
		HashMap<ITriangleMesh, List<ITriangle>> pickedMap = new HashMap<>();
		for (ITriangleMesh mesh : pickingItems) {
			mesh.toString();
			List<ITriangle> pickedTriangles = new ArrayList<>();
			// TODO optimierung anstatt triangles, gleich den index jeweils
			// merken und speichern wenn getroffen
			for (ITriangle triangle : getTriangles(mesh)) {
				if (isIntersectRayTriangle(ray, triangle, mesh)) {
					pickedTriangles.add(triangle);
					triangle.setVisible(false);
					mesh.updateRenderStructures();
				}
			}
			if (pickedTriangles.size() > 0) {
				pickedMap.put(mesh, pickedTriangles);
			}
		}
		return pickedMap;
	}

	/**
	 * 
	 * @param x
	 *            mouse clicked x
	 * @param y
	 *            mouse clicked y
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 * @param perspectiveAngle
	 * @return HashMap<ITriangleMesh, List<ITriangle>> Map with meshes and list
	 *         of selected Triangles for each one
	 */
	public void handleSelectionClick(int x, int y, int width, int height, double perspectiveAngle) {
		if (!activeCheck()) {
			return;
		}
		HashMap<ITriangleMesh, List<ITriangle>> intersections = checkForIntersections(x, y, width, height,
				perspectiveAngle);

		for (ITriangleMeshClickedHandler handler : clickedHandler) {
			handler.trianglesClicked(intersections);
		}

	}

	private static final double EPSILON = 0.00000001d;

	// Ray / Triangle Intersection
	// Moller and Trumbore algorithm:: Frei nach
	// https://github.com/sgothel/jogl-utils/blob/master/src/net/java/joglutils/msg/impl/RayTriangleIntersection.java
	/**
	 * 
	 * @param ray
	 *            A Ray3D pointing into the szene
	 * @param triangle
	 *            The triangle to check for collision with the ray
	 * @param mesh
	 *            The mesh containing the triangle to get its coords
	 * @return boolean. True == intersection % false == no intersection
	 */
	private boolean isIntersectRayTriangle(Ray3D ray, ITriangle triangle, ITriangleMesh mesh) {
		Vector vert0 = new Vector(mesh.getVertex(triangle.getA()).getPosition());
		Vector vert1 = new Vector(mesh.getVertex(triangle.getB()).getPosition());
		Vector vert2 = new Vector(mesh.getVertex(triangle.getC()).getPosition());

		// Find vectors for two edges sharing vert0
		Vector edge1 = vert1.subtract(vert0);
		Vector edge2 = vert2.subtract(vert0);

		// Begin calculating determinant -- also used to calculate U parameter
		Vector pvec = ray.getDirection().cross(edge2);

		// If determinant is near zero, ray lies in plane of triangle
		double det = edge1.multiply(pvec);

		if (det > -EPSILON && det < EPSILON) {
			return false;
		}

		double invDet = 1.0d / det;

		// Calculate distance from vert0 to ray origin
		Vector tvec = ray.getPoint().subtract(vert0);

		// Calculate U parameter and test bounds
		double u = tvec.multiply(pvec) * invDet;
		if (u < 0.0f || u > 1.0f) {
			return false;
		}
		// Prepare to test V parameter
		Vector qvec = tvec.cross(edge1);

		// Calculate V parameter and test bounds
		double v = ray.getDirection().multiply(qvec) * invDet;
		if (v < 0.0d || (u + v) > 1.0d) {
			return false;
		}

		// Calculate t, ray intersects triangle
		// Wird derzeit nicht verwendet da derzeit nur boolean zurück gegeben
		// wird
		// double t = edge2.multiply(qvec) * invDet;

		// result == tuv.set(t, u, v);
		return true;
	}

	/**
	 * Compute a ray from the clicked screen coordinates.
	 */
	private Ray3D getRayFromClickCoordinates(int mouse_x, int mouse_y, int width, int height, double perspectiveAngle) {
		Vector dir = Camera.getInstance().getRef().subtract(Camera.getInstance().getEye());
		dir.normalize();
		// points 'right'
		Vector dx = dir.cross(Camera.getInstance().getUp());
		dx.normalize();
		// points 'down'
		Vector dy = dir.cross(dx);
		dy.normalize();

		double ly = Math.tan(perspectiveAngle * Math.PI / (2.0 * 180.0));
		double lx = (double) width / (double) height * ly;
		dx = dx.multiply(lx);
		dy = dy.multiply(ly);
		double lambdaX = (mouse_x - width / 2.0) / (width / 2.0);
		double lambdaY = (mouse_y - height / 2.0) / (height / 2.0);

		// Ray3D ray = new Ray3D(Camera.getInstance().getEye(),dir);
		Ray3D ray = new Ray3D(Camera.getInstance().getEye(), dir.add(dx.multiply(lambdaX)).add(dy.multiply(lambdaY)));
		return ray;
	}

	/**
	 * Register a pickableMesh to get informed about picking events.
	 * 
	 * @param PickableMesh
	 *            a pickable mesh
	 */
	public void registerPickableMesh(ITriangleMesh pickableMesh) {
		pickingItems.add(pickableMesh);
	}

	public void registerTriangleMeshClickedHandler(ITriangleMeshClickedHandler triangleMeshClickedHandler) {
		clickedHandler.add(triangleMeshClickedHandler);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		handleSelectionClick(e.getX(), e.getY(), joglCanvas.getWidth(), joglCanvas.getHeight(),
				Camera.getInstance().getOpeningAngle());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}

package cgresearch.graphics.picking;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.Ray3D;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.camera.Camera;

/**
 * Central singleton instance for the picking.
 * 
 * @author Philipp Jenke
 *
 */
public class Picking extends Observable {

	/**
	 * Angle threshold for picking in degrees
	 */
	private static final double PICKING_ANGLE_THRESHOLD = 2;

	/**
	 * Shared list of pickable items for all pickables.
	 */
	private static List<PickingItem> pickingItems = new ArrayList<PickingItem>();

	/**
	 * List of registered pickables
	 */
	private static List<CgApplicationPickable> pickables = new ArrayList<CgApplicationPickable>();

	/**
	 * This is the id of the currently selected picking item.
	 */
	private static String selectedItem = null;

	/**
	 * Scaling value for the rendering.
	 */
	private static double scaling = 0.05;

	/**
	 * Singleton instance.
	 */
	private static Picking instance = null;

	/**
	 * This flag indicates if the picking mode is active
	 */
	private boolean active = false;

	/**
	 * Private singleton constructor.
	 */
	private Picking() {
	}

	/**
	 * Singleton instance access.
	 * 
	 * @return
	 */
	public static Picking getInstance() {
		if (instance == null) {
			instance = new Picking();
		}
		return instance;
	}

	/**
	 * Getter
	 */
	public int getNumberOfPickingItems() {
		return pickingItems.size();
	}

	/**
	 * Getter.
	 */
	public PickingItem getPickingItem(int index) {
		return pickingItems.get(index);
	}

	/**
	 * Return the currently selected item.
	 */
	public PickingItem getSelectedItem() {
		for (PickingItem item : pickingItems) {
			if (item.getId().equals(selectedItem)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Getter.
	 */
	public PickingItem getPickingItem(String id) {
		for (PickingItem item : pickingItems) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Return the id of the currently select picking item. Return null if no
	 * item is selected.
	 */
	public String getSelectedItemId() {
		return selectedItem;
	}

	/**
	 * Setter for the currently selected picking item
	 * 
	 * @param id
	 */
	public void setSelectedItem(String id) {
		selectedItem = id;

		// Inform pickables
		for (CgApplicationPickable pickable : pickables) {
			pickable.itemPicked(id);
		}
	}

	/**
	 * Getter.
	 */
	public double getScaling() {
		return scaling;
	}

	/**
	 * Setter.
	 */
	public void setScaling(double d) {
		scaling = d;
	}

	/**
	 * Setter.
	 */
	public void addItem(PickingItem pickingItem) {
		pickingItems.add(pickingItem);
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
	 * Handle a click selection event
	 * 
	 * @param x
	 *            X-coordinate on screen
	 * @param y
	 *            Y coordinate on screen
	 * @param width
	 *            Width of the screen
	 * @param height
	 *            Height of the screen.
	 * @param perspectiveAngle
	 *            Angle used in perspective projection.
	 */
	public void handleSelectionClick(int x, int y, int width, int height,
			double perspectiveAngle) {
		Ray3D ray = getRayFromClickCoordinates(x, y, width, height,
				perspectiveAngle);
		PickingItem pickedItem = null;
		double pickedDistance = -1;
		for (PickingItem item : pickingItems) {
			Vector u = item.getPosition().subtract(
					Camera.getInstance().getEye());
			Vector v = ray.getDirection();
			double angleRadiens = Math.acos(u.multiply(v)
					/ (u.getNorm() * v.getNorm()));
			double angleDegrees = Math.abs(MathHelpers
					.radiens2degree(angleRadiens));
			double distance = u.getNorm();
			if (angleDegrees < PICKING_ANGLE_THRESHOLD) {
				if (pickedItem == null) {
					pickedItem = item;
					pickedDistance = distance;
				} else {
					if (distance < pickedDistance) {
						pickedItem = item;
						pickedDistance = distance;
					}
				}
			}
		}

		setSelectedItem(pickedItem);
	}

	/**
	 * Select new picked item
	 */
	private void setSelectedItem(PickingItem pickedItem) {
		if (pickedItem != null) {
			setSelectedItem(pickedItem.getId());

			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Compute a ray from the clicked screen coordinates.
	 */
	private Ray3D getRayFromClickCoordinates(int x, int y, int width,
			int height, double perspectiveAngle) {
		Vector dir = Camera.getInstance().getRef()
				.subtract(Camera.getInstance().getEye());
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
		double lambdaX = (x - width / 2.0) / (width / 2.0);
		double lambdaY = (y - height / 2.0) / (height / 2.0);

		Ray3D ray = new Ray3D(Camera.getInstance().getEye(), dir.add(
				dx.multiply(lambdaX)).add(dy.multiply(lambdaY)));
		return ray;
	}

	public void moveX(float deltaX) {
		PickingItem selectedItem = Picking.getInstance().getSelectedItem();
		if (selectedItem != null) {
			Vector dx = VectorFactory.createVector3(1, 0, 0);
			selectedItem.setPosition(selectedItem.getPosition().add(
					dx.multiply(deltaX * scaling * 0.2)));

			// Inform pickables
			for (CgApplicationPickable pickable : pickables) {
				pickable.itemMoved(selectedItem.getId());
			}
			itemMoved(selectedItem.getId());
		}
	}

	private void itemMoved(String id) {
		setChanged();
		notifyObservers();
	}

	public void moveY(float deltaX) {
		PickingItem selectedItem = Picking.getInstance().getSelectedItem();
		if (selectedItem != null) {
			Vector dy = VectorFactory.createVector3(0, 1, 0);
			selectedItem.setPosition(selectedItem.getPosition().add(
					dy.multiply(deltaX * scaling * 0.2)));

			// Inform pickables
			for (CgApplicationPickable pickable : pickables) {
				pickable.itemMoved(selectedItem.getId());
			}
			itemMoved(selectedItem.getId());
		}
	}

	public void moveZ(float deltaX) {
		PickingItem selectedItem = Picking.getInstance().getSelectedItem();
		if (selectedItem != null) {
			Vector dz = VectorFactory.createVector3(0, 0, 1);
			selectedItem.setPosition(selectedItem.getPosition().add(
					dz.multiply(deltaX * scaling * 0.2)));

			// Inform pickables
			for (CgApplicationPickable pickable : pickables) {
				pickable.itemMoved(selectedItem.getId());
			}
			itemMoved(selectedItem.getId());
		}
	}

	/**
	 * Regsiter a pickable to get informed about picking events.
	 * 
	 * @param cgApplicationPickable
	 */
	public static void registerPickable(
			CgApplicationPickable cgApplicationPickable) {
		pickables.add(cgApplicationPickable);
	}
}

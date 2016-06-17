package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;

public abstract class IBoneMovePositionPickup extends TriangleMesh{
	public abstract void pickedUp();

	public abstract void dropped();

	public abstract void dragged(Vector newPositionAsScreenCoords);
}

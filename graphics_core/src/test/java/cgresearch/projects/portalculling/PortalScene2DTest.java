package cgresearch.projects.portalculling;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.Ray2D;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.projects.portalculling.PortalCell;
import cgresearch.projects.portalculling.PortalEdge;
import cgresearch.projects.portalculling.PortalScene2D;
import cgresearch.projects.portalculling.ViewVolume2D;

/**
 * JUnit test class for the portal scene
 * 
 * @author Philipp Jenke
 *
 */
public class PortalScene2DTest {

	/**
	 * Create a simple scene for testing purposes.
	 */
	private PortalScene2D createDummyScene() {
		PortalScene2D scene = new PortalScene2D();
		scene.clear();
		scene.addNode(VectorMatrixFactory.newVector(-0.5, 0, -0.5));
		scene.addNode(VectorMatrixFactory.newVector(0.5, 0, -0.5));
		scene.addNode(VectorMatrixFactory.newVector(0.5, 0, 0.5));
		scene.addNode(VectorMatrixFactory.newVector(-0.5, 0, 0.5));
		PortalEdge e01 = new PortalEdge(PortalEdge.State.WALL, 0, 1);
		PortalEdge e12 = new PortalEdge(PortalEdge.State.WALL, 1, 2);
		PortalEdge e23 = new PortalEdge(PortalEdge.State.WALL, 2, 3);
		PortalEdge e30 = new PortalEdge(PortalEdge.State.WALL, 3, 0);
		PortalEdge e02 = new PortalEdge(PortalEdge.State.PORTAL, 0, 2);
		int e01Index = scene.addEdge(e01);
		int e12Index = scene.addEdge(e12);
		int e23Index = scene.addEdge(e23);
		int e30Index = scene.addEdge(e30);
		int e02Index = scene.addEdge(e02);
		scene.addCell(new PortalCell(e01Index, e12Index, e02Index));
		scene.addCell(new PortalCell(e02Index, e23Index, e30Index));
		return scene;
	}

	@Test
	public void testGetOppositeCell() {
		PortalScene2D scene = createDummyScene();
		assertEquals(scene.getOppositeCell(0, 4), 1);
		assertEquals(scene.getOppositeCell(1, 4), 0);
	}

	@Test
	public void testComputeStartCell() {
		PortalScene2D scene = createDummyScene();
		assertEquals(0, scene.getStartCellIndex(VectorMatrixFactory
				.newVector(0, 0, -0.25)));
		assertEquals(1, scene.getStartCellIndex(VectorMatrixFactory
				.newVector(0, 0, 0.25)));
	}

	@Test
	public void testCellContainsPoint() {
		PortalScene2D scene = createDummyScene();
		assertEquals(
				true,
				scene.cellContainsPoint(scene.getCell(0),
						VectorMatrixFactory.newVector(0, 0, -0.25)));
		assertEquals(
				false,
				scene.cellContainsPoint(scene.getCell(1),
						VectorMatrixFactory.newVector(0, 0, -0.25)));
	}

	@Test
	public void testIntersectRays() {
		Ray2D ray1 = new Ray2D(VectorMatrixFactory.newVector(1, 0, 1),
				VectorMatrixFactory.newVector(4, 0, 2));
		Ray2D ray2 = new Ray2D(VectorMatrixFactory.newVector(2, 0, 3),
				VectorMatrixFactory.newVector(1, 0, -1));
		assertEquals(0.5, ray1.intersect(ray2).lambda1, 1e-5);
		assertEquals(1, ray1.intersect(ray2).lambda2, 1e-5);

		Ray2D ray3 = new Ray2D(VectorMatrixFactory.newVector(1, 0, 1),
				VectorMatrixFactory.newVector(1, 0, 1));
		Ray2D ray4 = new Ray2D(VectorMatrixFactory.newVector(2, 0, 3),
				VectorMatrixFactory.newVector(1, 0, 1));
		assertEquals(null, ray3.intersect(ray4));
	}

	@Test
	public void testIntersectViewVolumeEdge() {
		PortalScene2D scene = createDummyScene();
		ViewVolume2D viewVolume = new ViewVolume2D(
				VectorMatrixFactory.newVector(0, 0, -0.25),
				VectorMatrixFactory.newVector(-1, 0, 1),
				VectorMatrixFactory.newVector(1, 0, 1));
		scene.intersect(scene.getEdge(4), viewVolume);
	}
}

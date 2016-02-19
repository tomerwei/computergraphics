package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.tree.Intersection;

/**
 * Test intersection methods.
 * 
 * @author Philipp Jenke
 *
 */
public class IntersectionTest {

  @Test
  public void testTriangleBoxIntersection() {
    BoundingBox boundingBox = new BoundingBox();
    boundingBox.add(VectorMatrixFactory.newVector(-0.5, -0.5, -0.5));
    boundingBox.add(VectorMatrixFactory.newVector(0.5, 0.5, 0.5));
    assertTrue(Intersection.intersect(boundingBox, VectorMatrixFactory.newVector(0, 0, 0),
        VectorMatrixFactory.newVector(0.1, -0.1, 0.4), VectorMatrixFactory.newVector(-0.4, 0, 0.1)));
    assertFalse(Intersection.intersect(boundingBox, VectorMatrixFactory.newVector(0.6, 0.6, 0.6),
        VectorMatrixFactory.newVector(0.7, 0.8, 0.8), VectorMatrixFactory.newVector(0.6, 0.9, 1)));
    assertTrue(Intersection.intersect(boundingBox, VectorMatrixFactory.newVector(-0.6, -0.6, 0.3),
        VectorMatrixFactory.newVector(0.6, 0.6, 0.3), VectorMatrixFactory.newVector(0.2, 0.3, 0.3)));
  }
}

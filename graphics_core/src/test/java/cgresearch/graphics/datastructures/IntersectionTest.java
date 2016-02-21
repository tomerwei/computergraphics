package cgresearch.graphics.datastructures;

import static org.junit.Assert.*;

import org.junit.Test;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.VectorFactory;
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
    boundingBox.add(VectorFactory.createVector3(-0.5, -0.5, -0.5));
    boundingBox.add(VectorFactory.createVector3(0.5, 0.5, 0.5));
    assertTrue(Intersection.intersect(boundingBox, VectorFactory.createVector3(0, 0, 0),
        VectorFactory.createVector3(0.1, -0.1, 0.4), VectorFactory.createVector3(-0.4, 0, 0.1)));
    assertFalse(Intersection.intersect(boundingBox, VectorFactory.createVector3(0.6, 0.6, 0.6),
        VectorFactory.createVector3(0.7, 0.8, 0.8), VectorFactory.createVector3(0.6, 0.9, 1)));
    assertTrue(Intersection.intersect(boundingBox, VectorFactory.createVector3(-0.6, -0.6, 0.3),
        VectorFactory.createVector3(0.6, 0.6, 0.3), VectorFactory.createVector3(0.2, 0.3, 0.3)));
  }
}

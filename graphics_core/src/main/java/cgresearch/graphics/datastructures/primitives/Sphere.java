/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a sphere.
 * 
 * @author Philipp Jenke
 * 
 */
public class Sphere extends IPrimitive {

    /**
     * Center of the sphere.
     */
    private Vector center = VectorMatrixFactory.newVector(3);

    /**
     * Radius of the sphere.
     */
    private double radius = 1;

    /**
     * Constructor.
     */
    public Sphere() {
    }

    /**
     * Constructor.
     */
    public Sphere(Vector center, double radius) {
        this.center.copy(center);
        this.radius = radius;
    }

    /**
     * Getter.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Getter.
     */
    public Vector getCenter() {
        return center;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox bbox = new BoundingBox();
        bbox.add(center.subtract(VectorMatrixFactory.newVector(radius,
                radius, radius)));
        bbox.add(center.add(VectorMatrixFactory.newVector(radius, radius,
                radius)));
        return bbox;
    }
}

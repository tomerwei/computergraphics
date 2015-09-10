/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.primitives;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Representation of a axis-aligned cuboid.
 * 
 * @author Philipp Jenke
 * 
 */
public class Cuboid extends IPrimitive {

    /**
     * Center of the cuboid.
     */
    private IVector3 center = VectorMatrixFactory.newIVector3();

    /**
     * Dimension of the cuboid in x, y and z-direction.
     */
    private IVector3 dimensions = VectorMatrixFactory.newIVector3(1, 1, 1);

    /**
     * Constructor
     */
    public Cuboid() {
    }

    /**
     * Constructor
     */
    public Cuboid(IVector3 center, double x, double y, double z) {
        this.center.copy(center);
        dimensions.set(0, x);
        dimensions.set(1, y);
        dimensions.set(2, z);
    }

    /**
     * Constructor
     */
    public Cuboid(IVector3 center, IVector3 dimensions) {
        this.center.copy(center);
        this.dimensions = dimensions;
    }

    /**
     * Getter.
     */
    public IVector3 getCenter() {
        return center;
    }

    /**
     * Getter.
     */
    public IVector3 getDimensions() {
        return dimensions;
    }

    /**
     * Getter.
     */
    public double getDimensionX() {
        return dimensions.get(0);
    }

    /**
     * Getter.
     */
    public double getDimensionY() {
        return dimensions.get(1);
    }

    /**
     * Getter.
     */
    public double getDimensionZ() {
        return dimensions.get(2);
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox bbox = new BoundingBox();
        bbox.add(getLowerLeft());
        bbox.add(getUpperRight());
        return bbox;
    }

    /**
     * @return
     */
    private IVector3 getUpperRight() {
        return center.add(VectorMatrixFactory.newIVector3(
                dimensions.get(0) / 2.0, dimensions.get(1) / 2.0,
                dimensions.get(2) / 2.0));
    }

    /**
     * @return
     */
    private IVector3 getLowerLeft() {
        return center.subtract(VectorMatrixFactory.newIVector3(
                dimensions.get(0) / 2.0, dimensions.get(1) / 2.0,
                dimensions.get(2) / 2.0));
    }

}

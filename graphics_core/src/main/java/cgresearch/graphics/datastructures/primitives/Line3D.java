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
 * Representation of an arrow
 * 
 * @author Philipp Jenke
 * 
 */
public class Line3D extends IPrimitive {

    /**
     * Start point of the arrow.
     */
    public IVector3 start = VectorMatrixFactory.newIVector3(0, 0, 0);

    /**
     * End point of the arrow.
     */
    public IVector3 end = VectorMatrixFactory.newIVector3(1, 0, 0);

    /**
     * Constructor.
     */
    public Line3D() {
    }

    /**
     * Constructor.
     */
    public Line3D(IVector3 start, IVector3 end) {
        this.start.copy(start);
        this.end.copy(end);
    }

    /**
     * Getter.
     */
    public IVector3 getStart() {
        return start;
    }

    /**
     * Getter.
     */
    public IVector3 getEnd() {
        return end;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.datastructures.IBoundingBoxed#getBoundingBox()
     */
    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox bbox = new BoundingBox();
        bbox.add(start);
        bbox.add(end);
        return bbox;
    }

}

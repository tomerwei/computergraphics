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
 * Representation of an arrow
 * 
 * @author Philipp Jenke
 * 
 */
public class Arrow extends IPrimitive {

    /**
     * Start point of the arrow.
     */
    public Vector start = VectorMatrixFactory.newVector(0, 0, 0);

    /**
     * End point of the arrow.
     */
    public Vector end = VectorMatrixFactory.newVector(1, 0, 0);

    /**
     * Constructor.
     */
    public Arrow() {
    }

    /**
     * Constructor.
     */
    public Arrow(Vector start, Vector end) {
        this.start.copy(start);
        this.end.copy(end);
    }

    /**
     * Getter.
     */
    public Vector getStart() {
        return start;
    }

    /**
     * Getter.
     */
    public Vector getEnd() {
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

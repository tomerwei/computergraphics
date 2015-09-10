/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.implicitfunction;

import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Implicit function of a superquadric.
 * 
 * @author Philipp Jenke
 * 
 */
public class ImplicitFunctionSuperquadric implements IImplicitFunction3D {

    /**
     * Configuration parameter 1.
     */
    private final double e1;

    /**
     * Configuration parameter 2.
     */
    private final double e2;

    /**
     * Constructor.
     */
    public ImplicitFunctionSuperquadric(double e1, double e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.datastructures.implicitfunction.IImplicitFunction3D#f(edu.
     * haw.cg.math.IVector3)
     */
    @Override
    public double f(IVector3 p) {
        IVector3 center = VectorMatrixFactory.newIVector3(0, 0, 0);
        IVector3 extend = VectorMatrixFactory.newIVector3(1, 1, 1);
        return Math.pow(
                Math.pow((p.get(0) - center.get(0)) / extend.get(0), 2.0 / e2)
                        + Math.pow((p.get(1) - center.get(1)) / extend.get(1),
                                2.0 / e2), e2 / e1)
                + Math.pow((p.get(2) - center.get(2)) / extend.get(2), 2.0 / e1)
                - 1;
    }
}

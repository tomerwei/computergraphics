/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.implicitfunction;

import cgresearch.core.math.IVector3;

/**
 * Implicit function of a 3-dimensional sphere.
 * 
 * @author Philipp Jenke
 * 
 */
public class ImplicitFunction3DSphere implements IImplicitFunction3D {

    /**
     * Radius of the sphere.
     */
    private double radius;

    /**
     * Constructor.
     * 
     * @param radius
     *            Initial value for the radius.
     */
    public ImplicitFunction3DSphere(double radius) {
        this.radius = radius;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.marchingcubes.ImplicitFunction3D#f(javax.vecmath.Point3d
     * )
     */
    @Override
    public double f(IVector3 p) {
        return p.get(0) * p.get(0) + p.get(1) * p.get(1) + p.get(2) * p.get(2)
                - radius * radius;
    }

}

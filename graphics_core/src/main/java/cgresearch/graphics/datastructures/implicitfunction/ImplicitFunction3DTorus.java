/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.implicitfunction;

import cgresearch.core.math.Vector;

/**
 * Implicit function of a 3-dimensional torus.
 * 
 * @author Philipp Jenke
 * 
 */
public class ImplicitFunction3DTorus implements IImplicitFunction3D {

    /**
     * Outer radius of the torus.
     */
    private double radiusOuter;

    /**
     * Inner radius of the torus.
     */
    private double radiusInner;

    /**
     * Constructor.
     * 
     * @param radiusOuter
     *            Initial value for the outer radius.
     * @param radiusInner
     *            Initial value for the inner radius.
     */
    public ImplicitFunction3DTorus(double radiusOuter, double radiusInner) {
        this.radiusOuter = radiusOuter;
        this.radiusInner = radiusInner;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.cg1.exercises.marchingcubes.ImplicitFunction3D#f(javax.vecmath.Point3d
     * )
     */
    @Override
    public double f(Vector p) {
        return Math
                .pow((Math.pow(p.get(0), 2) + Math.pow(p.get(1), 2)
                        + Math.pow(p.get(2), 2) + Math.pow(radiusOuter, 2) - Math
                        .pow(radiusInner, 2)), 2)
                - 4.0
                * Math.pow(radiusOuter, 2)
                * (Math.pow(p.get(0), 2) + Math.pow(p.get(1), 2));
    }
}

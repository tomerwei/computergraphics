/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.implicitfunction;

import cgresearch.core.math.Vector;

/**
 * Parent interface for implicit functions.
 * 
 * @author Philipp Jenke
 * 
 */
public interface IImplicitFunction3D {

    /**
     * Evaluates the implicit function a given spatial location.
     * 
     * @param p
     *            The function is evaluated at this point.
     * @return Function value.
     */
    public double f(Vector p);

}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.core.math;

import org.junit.Test;

import cgresearch.core.math.PrincipalComponentAnalysis;
import cgresearch.core.math.VectorMatrixFactory;

/**
 * Test class for the PCA class.
 * 
 * @author Philipp Jenke
 * 
 */
public class PrincipalComponentAnalysisTest {

    @Test
    public void testPrincipalComponentAnalysis() {
        PrincipalComponentAnalysis pca = new PrincipalComponentAnalysis();
        for (int i = 0; i < 10; i++) {
            pca.add(VectorMatrixFactory.newVector(Math.random() * 10.0,
                    Math.random() * 10.0, Math.random() * 0.5 - 0.25));
        }
        pca.applyPCA();
        System.out.println("normal: " + pca.getNormal());
        System.out.println("centroid: " + pca.getCentroid());
        System.out.println("tangentU: " + pca.getTangentU());
        System.out.println("tangentV: " + pca.getTangentV());
        System.out.println("eigenValues: " + pca.getEigenValues());

    }

}

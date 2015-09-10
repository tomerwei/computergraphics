/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.points;

import java.util.Random;

import cgresearch.core.math.IVector3;

/**
 * Add noise to a point cloid
 * 
 * @author Philipp Jenke
 * 
 */
public class Noise {

    /**
     * Add random Gaussian noise to a point cloud.
     */
    public static void addNoise(IPointCloud pointCloud, double standardDeviation) {

        for (int pointIndex = 0; pointIndex < pointCloud.getNumberOfPoints(); pointIndex++) {
            IVector3 oldPosition = pointCloud.getPoint(pointIndex)
                    .getPosition();
            pointCloud
                    .getPoint(pointIndex)
                    .getPosition()
                    .set(0, oldPosition.get(0) + randomNoise(standardDeviation));
            pointCloud
                    .getPoint(pointIndex)
                    .getPosition()
                    .set(1, oldPosition.get(1) + randomNoise(standardDeviation));
            pointCloud
                    .getPoint(pointIndex)
                    .getPosition()
                    .set(2, oldPosition.get(2) + randomNoise(standardDeviation));
        }
    }

    /**
     * Compute a random noise value given some noise size (sigma).
     */
    private static double randomNoise(double standardDeviation) {
        return new Random().nextGaussian() * standardDeviation;
    }

}

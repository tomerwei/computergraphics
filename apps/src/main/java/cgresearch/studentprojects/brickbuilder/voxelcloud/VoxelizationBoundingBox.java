/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Bounding box algorithm to voxelize a 3d-model as proposed in
 * "faBrickation: Fast 3D Printing of Functional Objects by Integrating Construction Kit Building Blocks"
 * by Stefanie Mueller, Tobias Mohr, Kerstin Guenther, Johannes Frohnhofen,
 * Patrick Baudisch
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelizationBoundingBox implements IVoxelizationAlgorithm {

  @Override
  public IVoxelCloud transformMesh2Cloud(ITriangleMesh mesh, int resolutionAxisX, Vector voxelScale) {
    BoundingBox box = mesh.getBoundingBox();
    Vector location = box.getLowerLeft();
    double width = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_0);
    double height = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_1);
    double depth = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_2);

    // get resolutions
    double xRes = width / resolutionAxisX;
    double yRes = (voxelScale.get(MathHelpers.INDEX_1) / voxelScale.get(MathHelpers.INDEX_0)) * xRes;
    double zRes = (voxelScale.get(MathHelpers.INDEX_2) / voxelScale.get(MathHelpers.INDEX_0)) * xRes;
    int resolutionAxisY = (int) Math.ceil(height / yRes);
    int resolutionAxisZ = (int) Math.ceil(depth / zRes);

    // adjust location of y, z
    double offsetY = ((resolutionAxisY * yRes) - height) * 0.5;
    double offsetZ = ((resolutionAxisZ * zRes) - depth) * 0.5;
    location = location.subtract(VectorFactory.createVector3(0, offsetY, offsetZ));

    // create voxel cloud
    IVoxelCloud cloud = new VoxelCloud(location, VectorFactory.createVector3(xRes, yRes, zRes),
        new VectorInt3(resolutionAxisX, resolutionAxisY, resolutionAxisZ));
    for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
      for (int y = 0; y < cloud.getResolutions().getY(); y++) {
        for (int x = 0; x < cloud.getResolutions().getX(); x++) {
          VectorInt3 p = new VectorInt3(x, y, z);
          Vector ll = location.add(VectorFactory.createVector3(xRes * x, yRes * y, zRes * z));
          Vector ur = location.add(VectorFactory.createVector3(xRes * (x + 1), yRes * (y + 1), zRes * (z + 1)));
          if (vertexInBox(mesh, ll, ur))
            cloud.setVoxelAt(p, VoxelType.INTERIOR);
        }
      }
    }

    return cloud;
  }

  private boolean vertexInBox(ITriangleMesh mesh, Vector ll, Vector ur) {
    for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
      IVertex v = mesh.getVertex(i);
      double[] l = ll.data();
      double[] u = ur.data();
      double[] p = v.getPosition().data();
      if (p[0] >= l[0] && p[0] <= u[0] && p[1] >= l[1] && p[1] <= u[1] && p[2] >= l[2] && p[2] <= u[2])
        return true;
    }
    return false;
  }

}

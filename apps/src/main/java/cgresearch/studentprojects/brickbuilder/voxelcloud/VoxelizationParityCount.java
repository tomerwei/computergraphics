/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.studentprojects.brickbuilder.math.ColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Parity count algorithm to voxelize a 3d-model as proposed in
 * "Simplification and Repair of Polygonal Models Using Volumetric Techniques"
 * by Fakir S. Nooruddin and Greg Turk
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelizationParityCount implements IVoxelizationAlgorithm {

  @Override
  public IVoxelCloud transformMesh2Cloud(final ITriangleMesh mesh, int resolutionAxisX, IVector3 voxelScale) {
    BufferedImage tex = null;

    if (mesh.getMaterial().hasTexture()) {
      // get new resourcemanager texture id
      String id = mesh.getMaterial().getTextureId();
      CgTexture cgTex = ResourceManager.getTextureManagerInstance().getResource(id);

      // load texture
      if (cgTex.getTextureImage() != null) {
        tex = cgTex.getTextureImage();
      } else {
        String absoluteTextureFilename = ResourcesLocator.getInstance().getPathToResource(cgTex.getTextureFilename());
        if (absoluteTextureFilename != null) {
          try {
            tex = ImageIO.read(new File(absoluteTextureFilename));
          } catch (IOException e) {
            Logger.getInstance().exception("transformMesh2Cloud: texture read error", e);
            tex = null;
          }
        } else {
          Logger.getInstance().error("transformMesh2Cloud: no texture to load from");
        }
      }
    }

    BoundingBox box = mesh.getBoundingBox();
    IVector3 location = box.getLowerLeft();
    double width = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_0);
    double height = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_1);
    double depth = box.getUpperRight().subtract(box.getLowerLeft()).get(MathHelpers.INDEX_2);

    // get resolutions
    double xRes = width / resolutionAxisX;
    double yRes = (voxelScale.get(MathHelpers.INDEX_1) / voxelScale.get(MathHelpers.INDEX_0)) * xRes;
    double zRes = (voxelScale.get(MathHelpers.INDEX_2) / voxelScale.get(MathHelpers.INDEX_0)) * xRes;
    int resolutionAxisY = (int) Math.ceil(height / yRes);
    int resolutionAxisZ = (int) Math.ceil(depth / zRes);

    // the algorithm needs a NxNxN space, so we look for the max and set it for
    // all axis
    final int resolutionAxisAll = Math.max(Math.max(resolutionAxisX, resolutionAxisY), resolutionAxisZ);

    // adjust location of x, y, z
    double offsetX = ((resolutionAxisAll * xRes) - width) * 0.5;
    double offsetY = ((resolutionAxisAll * yRes) - height) * 0.5;
    double offsetZ = ((resolutionAxisAll * zRes) - depth) * 0.5;
    location = location.subtract(VectorMatrixFactory.newIVector3(offsetX, offsetY, offsetZ));
    final IVector3 locationCenter = location.add(VectorMatrixFactory.newIVector3(xRes * 0.5, yRes * 0.5, zRes * 0.5));

    // create voxel cloud
    IVoxelCloud cloud = new VoxelCloud(location, VectorMatrixFactory.newIVector3(xRes, yRes, zRes),
        new VectorInt3(resolutionAxisAll, resolutionAxisAll, resolutionAxisAll));

    final int[] count = new int[resolutionAxisAll * resolutionAxisAll * resolutionAxisAll];
    final IVector3[][] colorPoints = new IVector3[3][resolutionAxisAll * resolutionAxisAll * resolutionAxisAll];
    final int[][] colorTriangles = new int[3][resolutionAxisAll * resolutionAxisAll * resolutionAxisAll];

    // calc triangle bounding boxes
    final double[][][] bbs = new double[mesh.getNumberOfTriangles()][2][3];
    for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
      bbs[i] = new double[2][3];
      bbs[i][0] = new double[] { Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY };
      bbs[i][1] = new double[] { Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY };
      calcBoundingBox(mesh, mesh.getTriangle(i), bbs[i]);
    }

    //
    final double[] res = new double[] { xRes, yRes, zRes };
    final CountDownLatch latch = new CountDownLatch(3);

    // cast NxN rays through the model along the three axis
    for (int i = 0; i < 3; i++) {
      final int axiz = i;
      new Thread() {
        @Override
        public void run() {
          castRay(mesh, axiz, resolutionAxisAll, res, locationCenter, count, bbs, colorPoints[axiz],
              colorTriangles[axiz]);
          latch.countDown();
        }
      }.start();
    }

    // wait for threads
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // count them
    for (int z = 0; z < resolutionAxisAll; z++) {
      for (int y = 0; y < resolutionAxisAll; y++) {
        for (int x = 0; x < resolutionAxisAll; x++) {
          // positive vote?
          int index = (resolutionAxisAll * resolutionAxisAll * z) + (resolutionAxisAll * y) + x;
          if (count[index] > 0) {
            IVectorInt3 vec = new VectorInt3(x, y, z);
            IVector3 pos = locationCenter.add(VectorMatrixFactory.newIVector3(x * res[0], y * res[1], z * res[2]));

            // color?
            if (tex != null) {
              // get closest point to calc color
              double d = Double.POSITIVE_INFINITY;
              int axiz = -1;
              for (int i = 0; i < 3; i++) {
                if (colorPoints[i][index] != null) {
                  double n = pos.subtract(colorPoints[i][index]).getNorm();
                  if (n < d) {
                    d = n;
                    axiz = i;
                  }
                }
              }
              // set color if we have a point
              if (axiz > -1) {
                cloud.setVoxelColor(vec,
                    getColorFromUV(mesh, tex, colorPoints[axiz][index], colorTriangles[axiz][index]));
              }
            }

            cloud.setVoxelAt(vec, VoxelType.INTERIOR);
          }
        }
      }
    }

    return cloud;
  }

  /**
   * Calculates the bounding box of a triangle.
   * 
   * @param mesh
   *          triangle mesh
   * @param t
   *          triangle
   * @param ds
   *          bounding box data to store at
   */
  private void calcBoundingBox(ITriangleMesh mesh, ITriangle t, double[][] ds) {
    for (int i = 0; i < 3; i++) {
      IVector3 v = mesh.getVertex(t.get(i)).getPosition();
      if (v.get(0) < ds[0][0])
        ds[0][0] = v.get(0);
      if (v.get(1) < ds[0][1])
        ds[0][1] = v.get(1);
      if (v.get(2) < ds[0][2])
        ds[0][2] = v.get(2);
      if (v.get(0) > ds[1][0])
        ds[1][0] = v.get(0);
      if (v.get(1) > ds[1][1])
        ds[1][1] = v.get(1);
      if (v.get(2) > ds[1][2])
        ds[1][2] = v.get(2);
    }
  }

  /**
   * Return all intersects of the ray.
   * 
   * @param mesh
   *          mesh
   * @param rayLoc
   *          ray location
   * @param rayDir
   *          ray direction
   * @param bbs
   *          triangle bounding boxes
   * @return
   */
  private Map<IVector3, Integer> findIntersects(ITriangleMesh mesh, IVector3 rayLoc, IVector3 rayDir,
      double[][][] bbs) {
    Map<IVector3, Integer> intersects = new HashMap<IVector3, Integer>();
    for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
      // check triangle bounding box
      if (!checkBoundingBox(rayLoc, rayDir, bbs[i]))
        continue;
      // get ray triangle intersect point
      IVector3 r = rayTriangleIntersect(rayLoc, rayDir, mesh, mesh.getTriangle(i));
      if (r != null)
        intersects.put(r, i);
    }
    return intersects;
  }

  /**
   * Checks if a ray is going through a given bounding box.
   * 
   * @param rayLoc
   *          ray destination
   * @param rayDir
   *          ray direction
   * @param bb
   *          bounding box
   * @return
   */
  private boolean checkBoundingBox(IVector3 rayLoc, IVector3 rayDir, double[][] bb) {
    int a = (rayDir.get(0) == 1 ? 1 : 0);
    int b = (rayDir.get(2) == 1 ? 1 : 2);
    if (rayLoc.get(a) < bb[0][a] || rayLoc.get(b) < bb[0][b])
      return false;
    if (rayLoc.get(a) > bb[1][a] || rayLoc.get(b) > bb[1][b])
      return false;
    return true;
  }

  /**
   * Casts NxN rays through the mesh from the specified axiz.
   * 
   * @param mesh
   *          mesh
   * @param axiz
   *          axiz (0 = X, 1 = Y, 2 = Z)
   * @param res
   *          resolution for all axiz
   * @param step
   *          voxel length for each axiz
   * @param locCenter
   *          voxel 0,0 center
   * @param count
   *          count array
   * @param bbs
   *          bounding boxes of the triangles
   * @param colorPoints
   *          triangle point to calc the color of
   * @param colorTriangles
   *          triangle to calc the color of
   */
  private void castRay(ITriangleMesh mesh, final int axiz, int res, double[] step, IVector3 locCenter, int[] count,
      double[][][] bbs, IVector3[] colorPoints, int[] colorTriangles) {
    // create ray direction
    IVector3 dir = VectorMatrixFactory.newIVector3(0, 0, 0);
    dir.set(axiz, 1);
    // create intersects comperator
    Comparator<IVector3> comp = new Comparator<IVector3>() {
      @Override
      public int compare(IVector3 a, IVector3 b) {
        return a.get(axiz) > b.get(axiz) ? 1 : a.get(axiz) < b.get(axiz) ? -1 : 0;
      }

    };
    // calc other axiz
    int a = (axiz == 0 ? 1 : 0);
    int b = (axiz == 2 ? 1 : 2);
    int[] pos = new int[3];
    // loop through other axiz
    for (pos[a] = 0; pos[a] < res; pos[a]++) {
      for (pos[b] = 0; pos[b] < res; pos[b]++) {
        // calc ray start location
        IVector3 loc = locCenter.add(VectorMatrixFactory.newIVector3((axiz == 0 ? -step[0] : pos[0] * step[0]),
            (axiz == 1 ? -step[1] : pos[1] * step[1]), (axiz == 2 ? -step[2] : pos[2] * step[2])));
        Map<IVector3, Integer> intersectsMap = findIntersects(mesh, loc, dir, bbs);
        // broken ray? no vote
        if (intersectsMap.size() % 2 != 0)
          continue;
        List<IVector3> intersects = new ArrayList<IVector3>(intersectsMap.keySet());
        Collections.sort(intersects, comp);

        // lets vote
        int c = 0;
        double l = locCenter.get(axiz);
        for (pos[axiz] = 0; pos[axiz] < res; pos[axiz]++) {
          // we need to only check the axiz value
          if (c < intersects.size() && l >= intersects.get(c).get(axiz))
            c++;

          int index = (res * res * pos[2]) + (res * pos[1]) + pos[0];
          if (c % 2 == 0) {
            // even => exterior
            count[index]--;
          } else {
            // odd => interior
            count[index]++;
          }

          // set point and triangle of the nearest intersect to calc the color
          // from it
          if (intersectsMap.size() > 0) {
            double d1 = (c > 0 ? Math.abs(l - intersects.get(c - 1).get(axiz)) : Double.POSITIVE_INFINITY);
            double d2 = (c < intersects.size() ? Math.abs(l - intersects.get(c).get(axiz)) : Double.POSITIVE_INFINITY);
            if (d1 < d2) {
              colorPoints[index] = intersects.get(c - 1);
              colorTriangles[index] = intersectsMap.get(intersects.get(c - 1));
            } else {
              colorPoints[index] = intersects.get(c);
              colorTriangles[index] = intersectsMap.get(intersects.get(c));
            }
          }

          l += step[axiz];
        }
      }
    }
  }

  /**
   * Returns the point where the ray intersects the triangle or null if they
   * dont. Source:
   * http://www.lighthouse3d.com/tutorials/maths/ray-triangle-intersection/
   * 
   * @param rayP
   *          ray destination
   * @param rayD
   *          ray direction
   * @param mesh
   *          triangle mesh
   * @param t
   *          triangle
   * @return intersect point or null
   */
  private IVector3 rayTriangleIntersect(IVector3 rayP, IVector3 rayD, ITriangleMesh mesh, ITriangle t) {
    // e1 = v1 - v0
    IVector3 e1 = mesh.getVertex(t.getB()).getPosition().subtract(mesh.getVertex(t.getA()).getPosition());
    // e2 = v2 - v0
    IVector3 e2 = mesh.getVertex(t.getC()).getPosition().subtract(mesh.getVertex(t.getA()).getPosition());
    // h = crossP(d, e2)
    IVector3 h = rayD.cross(e2);
    // a = dot(e1, h)
    double a = e1.multiply(h);
    //
    if (a > -0.00001 && a < 0.00001)
      return null;
    // f = 1/a
    double f = 1 / a;
    // s = p - v0
    IVector3 s = rayP.subtract(mesh.getVertex(t.getA()).getPosition());
    // u = f * dot(s, h)
    double u = f * s.multiply(h);
    //
    if (u < 0.0 || u > 1.0)
      return null;
    // q = cross(s, e1)
    IVector3 q = s.cross(e1);
    // v = f * dot(d, q)
    double v = f * rayD.multiply(q);
    //
    if (v < 0.0 || u + v > 1.0)
      return null;
    // w = f * dot(e2, q)
    double w = f * e2.multiply(q);
    //
    if (w > 0.00001)
      return rayP.add(rayD.multiply(w));

    return null;
  }

  /**
   * Reads the color of a texture at the given point of the triangle.
   * 
   * @param mesh
   *          mesh
   * @param tex
   *          texture
   * @param point
   *          point of triangle
   * @param index
   *          triangle index
   * @return
   */
  private static IColorRGB getColorFromUV(ITriangleMesh mesh, BufferedImage tex, IVector3 point, int index) {
    // source:
    // http://answers.unity3d.com/questions/383804/calculate-uv-coordinates-of-3d-point-on-plane-of-m.html
    ITriangle triangle = mesh.getTriangle(index);
    IVector3[] vertices = new IVector3[] { mesh.getVertex(triangle.getA()).getPosition(),
        mesh.getVertex(triangle.getB()).getPosition(), mesh.getVertex(triangle.getC()).getPosition() };
    IVector3[] texCoords = new IVector3[] { mesh.getTextureCoordinate(triangle.getTextureCoordinate(0)),
        mesh.getTextureCoordinate(triangle.getTextureCoordinate(1)),
        mesh.getTextureCoordinate(triangle.getTextureCoordinate(2)) };
    IVector3 verticesAPoint = vertices[0].subtract(point);
    IVector3 verticesBPoint = vertices[1].subtract(point);
    IVector3 verticesCPoint = vertices[2].subtract(point);

    double a = vertices[0].subtract(vertices[1]).cross(vertices[0].subtract(vertices[2])).getNorm();
    double a0 = verticesBPoint.cross(verticesCPoint).getNorm() / a;
    double a1 = verticesCPoint.cross(verticesAPoint).getNorm() / a;
    double a2 = verticesAPoint.cross(verticesBPoint).getNorm() / a;

    IVector3 uv = texCoords[0].multiply(a0).add(texCoords[1].multiply(a1).add(texCoords[2].multiply(a2)));
    double u = uv.get(0) > 1.0 ? uv.get(0) - Math.floor(uv.get(0)) : uv.get(0);
    double v = uv.get(1) > 1.0 ? uv.get(1) - Math.floor(uv.get(1)) : uv.get(1);
    int color = tex.getRGB((int) ((tex.getWidth() - 1) * u), tex.getHeight() - 1 - (int) ((tex.getHeight() - 1) * v));
    return new ColorRGB(color);
  }

  // private void intersectTest() {
  // ITriangleMesh mesh2 = new TriangleMesh();
  // Vertex v0 = new Vertex(VectorMatrixFactory.newIVector3(2, 0, -1));
  // Vertex v1 = new Vertex(VectorMatrixFactory.newIVector3(2, 0, 1));
  // Vertex v2 = new Vertex(VectorMatrixFactory.newIVector3(2, 1, 0));
  // int i0 = mesh2.addVertex(v0);
  // int i1 = mesh2.addVertex(v1);
  // int i2 = mesh2.addVertex(v2);
  // Triangle t = new Triangle(i0, i1, i2);
  // mesh2.addTriangle(t);
  //
  // IVector3 p = VectorMatrixFactory.newIVector3(0, -0.1, 0);
  // IVector3 d = VectorMatrixFactory.newIVector3(1, 0, 0);
  //
  // System.out.println(rayTriangleIntersect(p, d, mesh2, t));
  // }
}
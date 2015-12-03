package cgresearch.graphics.datastructures.tree;

/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static java.lang.Math.min;

import java.util.List;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.primitives.Plane;

import static java.lang.Math.max;

/**
 * This class includes some utility methods for computing intersection between
 * bounding volumes and triangles.
 * 
 * @author Kirill
 */
public class Intersection {

  private static final int X = 0;
  private static final int Y = 1;
  private static final int Z = 2;

  private static final void findMinMax(double x0, double x1, double x2, IVector3 minMax) {
    minMax.set(x0, x0, 0);
    if (x1 < minMax.get(X)) {
      minMax.set(0, x1);
    }
    if (x1 > minMax.get(Y)) {
      minMax.set(1, x1);
    }
    if (x2 < minMax.get(X)) {
      minMax.set(0, x2);
    }
    if (x2 > minMax.get(Y)) {
      minMax.set(1, x2);
    }
  }

  // private boolean axisTest(float a, float b, float fa, float fb, Vector3f v0,
  // Vector3f v1, )
  // private boolean axisTestX01(float a, float b, float fa, float fb,
  // Vector3f center, Vector3f ext,
  // Vector3f v1, Vector3f v2, Vector3f v3){
  // float p0 = a * v0.y - b * v0.z;
  // float p2 = a * v2.y - b * v2.z;
  // if(p0 < p2){
  // min = p0;
  // max = p2;
  // } else {
  // min = p2;
  // max = p0;
  // }
  // float rad = fa * boxhalfsize.y + fb * boxhalfsize.z;
  // if(min > rad || max < -rad)
  // return false;
  // }

  public static boolean intersect(BoundingBox bbox, IVector3 v1, IVector3 v2, IVector3 v3) {
    // use separating axis theorem to test overlap between triangle and box
    // need to test for overlap in these directions:
    // 1) the {x,y,z}-directions (actually, since we use the AABB of the
    // triangle
    // we do not even need to test these)
    // 2) normal of the triangle
    // 3) crossproduct(edge from tri, {x,y,z}-directin)
    // this gives 3x3=9 more tests

    IVector3 center = bbox.getCenter();
    IVector3 extent = bbox.getExtent();

    // float min,max,p0,p1,p2,rad,fex,fey,fez;
    // float normal[3]

    // This is the fastest branch on Sun
    // move everything so that the boxcenter is in (0,0,0)
    IVector3 tmp0 = v1.subtract(center);
    IVector3 tmp1 = v2.subtract(center);
    IVector3 tmp2 = v3.subtract(center);

    // compute triangle edges
    IVector3 e0 = tmp1.subtract(tmp0); // tri edge 0
    IVector3 e1 = tmp2.subtract(tmp1); // tri edge 1
    IVector3 e2 = tmp0.subtract(tmp2); // tri edge 2

    // Bullet 3:
    // test the 9 tests first (this was faster)
    double min, max;
    double p0, p1, p2, rad;
    double fex = Math.abs(e0.get(X));
    double fey = Math.abs(e0.get(Y));
    double fez = Math.abs(e0.get(Z));

    // AXISTEST_X01(e0[Z], e0[Y], fez, fey);
    p0 = e0.get(Z) * tmp0.get(Y) - e0.get(Y) * tmp0.get(Z);
    p2 = e0.get(Z) * tmp2.get(Y) - e0.get(Y) * tmp2.get(Z);
    min = min(p0, p2);
    max = max(p0, p2);
    rad = fez * extent.get(Y) + fey * extent.get(Z);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Y02(e0[Z], e0[X], fez, fex);
    p0 = -e0.get(Z) * tmp0.get(X) + e0.get(X) * tmp0.get(Z);
    p2 = -e0.get(Z) * tmp2.get(X) + e0.get(X) * tmp2.get(Z);
    min = min(p0, p2);
    max = max(p0, p2);
    rad = fez * extent.get(X) + fex * extent.get(Z);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Z12(e0[Y], e0[X], fey, fex);
    p1 = e0.get(Y) * tmp1.get(X) - e0.get(X) * tmp1.get(Y);
    p2 = e0.get(Y) * tmp2.get(X) - e0.get(X) * tmp2.get(Y);
    min = min(p1, p2);
    max = max(p1, p2);
    rad = fey * extent.get(X) + fex * extent.get(Y);
    if (min > rad || max < -rad) {
      return false;
    }

    fex = Math.abs(e1.get(X));
    fey = Math.abs(e1.get(Y));
    fez = Math.abs(e1.get(Z));

    // AXISTEST_X01(e1[Z], e1[Y], fez, fey);
    p0 = e1.get(Z) * tmp0.get(Y) - e1.get(Y) * tmp0.get(Z);
    p2 = e1.get(Z) * tmp2.get(Y) - e1.get(Y) * tmp2.get(Z);
    min = min(p0, p2);
    max = max(p0, p2);
    rad = fez * extent.get(Y) + fey * extent.get(Z);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Y02(e1[Z], e1[X], fez, fex);
    p0 = -e1.get(Z) * tmp0.get(X) + e1.get(X) * tmp0.get(Z);
    p2 = -e1.get(Z) * tmp2.get(X) + e1.get(X) * tmp2.get(Z);
    min = min(p0, p2);
    max = max(p0, p2);
    rad = fez * extent.get(X) + fex * extent.get(Z);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Z0(e1[Y], e1[X], fey, fex);
    p0 = e1.get(Y) * tmp0.get(X) - e1.get(X) * tmp0.get(Y);
    p1 = e1.get(Y) * tmp1.get(X) - e1.get(X) * tmp1.get(Y);
    min = min(p0, p1);
    max = max(p0, p1);
    rad = fey * extent.get(X) + fex * extent.get(Y);
    if (min > rad || max < -rad) {
      return false;
    }
    //
    fex = Math.abs(e2.get(X));
    fey = Math.abs(e2.get(Y));
    fez = Math.abs(e2.get(Z));

    // AXISTEST_X2(e2[Z], e2[Y], fez, fey);
    p0 = e2.get(Z) * tmp0.get(Y) - e2.get(Y) * tmp0.get(Z);
    p1 = e2.get(Z) * tmp1.get(Y) - e2.get(Y) * tmp1.get(Z);
    min = min(p0, p1);
    max = max(p0, p1);
    rad = fez * extent.get(Y) + fey * extent.get(Z);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Y1(e2[Z], e2[X], fez, fex);
    p0 = -e2.get(Z) * tmp0.get(X) + e2.get(X) * tmp0.get(Z);
    p1 = -e2.get(Z) * tmp1.get(X) + e2.get(X) * tmp1.get(Z);
    min = min(p0, p1);
    max = max(p0, p1);
    rad = fez * extent.get(X) + fex * extent.get(Y);
    if (min > rad || max < -rad) {
      return false;
    }

    // AXISTEST_Z12(e2[Y], e2[X], fey, fex);
    p1 = e2.get(Y) * tmp1.get(X) - e2.get(X) * tmp1.get(Y);
    p2 = e2.get(Y) * tmp2.get(X) - e2.get(X) * tmp2.get(Y);
    min = min(p1, p2);
    max = max(p1, p2);
    rad = fey * extent.get(X) + fex * extent.get(Y);
    if (min > rad || max < -rad) {
      return false;
    }

    // Bullet 1:
    // first test overlap in the {x,y,z}-directions
    // find min, max of the triangle each direction, and test for overlap in
    // that direction -- this is equivalent to testing a minimal AABB around
    // the triangle against the AABB

    IVector3 minMax = VectorMatrixFactory.newIVector3(0, 0, 0);

    // test in X-direction
    findMinMax(tmp0.get(X), tmp1.get(X), tmp2.get(X), minMax);
    if (minMax.get(X) > extent.get(X) || minMax.get(Y) < -extent.get(X)) {
      return false;
    }

    // test in Y-direction
    findMinMax(tmp0.get(Y), tmp1.get(Y), tmp2.get(Y), minMax);
    if (minMax.get(X) > extent.get(Y) || minMax.get(Y) < -extent.get(Y)) {
      return false;
    }

    // test in Z-direction
    findMinMax(tmp0.get(Z), tmp1.get(Z), tmp2.get(Z), minMax);
    if (minMax.get(X) > extent.get(Z) || minMax.get(Y) < -extent.get(Z)) {
      return false;
    }

    // // Bullet 2:
    // // test if the box intersects the plane of the triangle
    // // compute plane equation of triangle: normal * x + d = 0

    IVector3 a = v2.subtract(v1);
    IVector3 b = v3.subtract(v1);
    IVector3 normal = a.cross(b);
    Plane plane = new Plane(v1, normal);

    if (!planeBoxIntersect(bbox, plane)) {
      return false;
    }

    // Vector3f normal = new Vector3f();
    // e0.cross(e1, normal);

    // Plane p = vars.plane;
    // p.setPlanePoints(v1, v2, v3);
    // if (bbox.whichSide(p) == Plane.Side.Negative) {
    // return false;
    // }
    //
    // if(!planeBoxOverlap(normal,v0,boxhalfsize)) return false;

    return true; /* box and triangle overlaps */
  }

  /**
   * Returns true if the plane intersects the bounding box.
   * 
   * @param bbox
   *          Checked bounding box.
   * @param plane
   *          Checked plane.
   * @return True if the plane intersects the bounding box.
   */
  private static boolean planeBoxIntersect(BoundingBox bbox, Plane plane) {
    List<IVector3> corners = bbox.computeCornerPoints();
    boolean hasNegativeSide = false;
    boolean hasPositiveSide = false;
    for (IVector3 point : corners) {
      if (plane.isInPositiveHalfSpace(point)) {
        hasPositiveSide = true;
      } else {
        hasNegativeSide = true;
      }
    }
    return hasNegativeSide && hasPositiveSide;
  }
}

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.math;

import java.util.Arrays;

/**
 * Implementation of the VectorInt3 interface.
 * 
 * @author Chris Michael Marquardt
 */
public class VectorInt3 implements IVectorInt3 {
  private int[] dim = null;

  /**
   * Constructor
   * 
   * @param x
   * @param y
   * @param z
   */
  public VectorInt3(int x, int y, int z) {
    dim = new int[] { x, y, z };
  }

  @Override
  public int getX() {
    return dim[0];
  }

  @Override
  public int getY() {
    return dim[1];
  }

  @Override
  public int getZ() {
    return dim[2];
  }

  @Override
  public int[] get() {
    return new int[] { dim[0], dim[1], dim[2] };
  }

  @Override
  public int getProduct() {
    return dim[0] * dim[1] * dim[2];
  }

  @Override
  public String toString() {
    return "i(" + dim[0] + ", " + dim[1] + ", " + dim[2] + ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(dim);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    VectorInt3 other = (VectorInt3) obj;
    if (!Arrays.equals(dim, other.dim))
      return false;
    return true;
  }

  @Override
  public VectorInt3 add(VectorInt3 other) {
    return new VectorInt3(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
  }

  @Override
  public VectorInt3 sub(VectorInt3 other) {
    return new VectorInt3(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
  }
}

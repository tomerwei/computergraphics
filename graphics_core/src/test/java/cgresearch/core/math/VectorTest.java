package cgresearch.core.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class VectorTest {

  @Test
  public void testConstructor() {
    IVector vector = new Vector(3);
    vector.set(0, 1);
    vector.set(1, -2);
    vector.set(2, 3);
    assertEquals(1, vector.get(0), 1e-5);
    assertEquals(-2, vector.get(1), 1e-5);
    assertEquals(3, vector.get(2), 1e-5);
  }

  @Test
  public void testAdd() {
    IVector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    IVector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    IVector r = a.add(b);
    assertEquals(-1, r.get(0), 1e-5);
    assertEquals(2.5, r.get(1), 1e-5);
    assertEquals(4, r.get(2), 1e-5);
  }

  @Test
  public void testSubtract() {
    IVector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    IVector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    IVector r = a.subtract(b);
    assertEquals(3, r.get(0), 1e-5);
    assertEquals(1.5, r.get(1), 1e-5);
    assertEquals(2, r.get(2), 1e-5);
  }

  @Test
  public void testInnerProduct() {
    IVector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    IVector b = new Vector(3);
    b.set(0, -2);
    b.set(1, 0.5);
    b.set(2, 1);
    double s = a.multiply(b);
    assertEquals(2, s, 1e-5);
  }

  @Test
  public void testMultiplyScalar() {
    IVector a = new Vector(3);
    a.set(0, 1);
    a.set(1, 2);
    a.set(2, 3);
    IVector r = a.multiply(-0.5);
    assertEquals(-0.5, r.get(0), 1e-5);
    assertEquals(-1, r.get(1), 1e-5);
    assertEquals(-1.5, r.get(2), 1e-5);
  }

  @Test
  public void testNorm() {
    IVector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getNorm();
    assertEquals(5, norm, 1e-5);
  }

  @Test
  public void testSqrNorm() {
    IVector a = new Vector(3);
    a.set(0, 0);
    a.set(1, 4);
    a.set(2, 3);
    double norm = a.getSqrNorm();
    assertEquals(25, norm, 1e-5);
  }
}

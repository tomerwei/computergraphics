package cgresearch.studentprojects.urbanreconstruction;

import cgresearch.core.math.Matrix;
import cgresearch.core.math.MatrixFactory;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.Point;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.datastructures.primitives.Plane;

public class MeshExtractor {

  public Matrix R;
  public Matrix RT;
  public IPointCloud[] ebene;

  public void convert() {
    ebene = new IPointCloud[UrbanGUI.planes.size()];
    for (int i = 0; i < UrbanGUI.planes.size(); i++) {
      R = null;
      RT = null;
      ebene[i] = new PointCloud();
      Plane pl = UrbanGUI.planes.get(i);
      R = MatrixFactory.createCoordinateFrameZ(pl.getNormal());
      RT = R.getTransposed();
      IPointCloud temp = UrbanGUI.points.get(i);

      for (int j = 0; j < temp.getNumberOfPoints(); j++) {

        Vector sub = temp.getPoint(j).getPosition().subtract(pl.getPoint());
        Vector pos = RT.multiply(sub);

        Point point = new Point(pos, // Position
            temp.getPoint(j).getColor(), // Color
            temp.getPoint(j).getNormal() // Norm
        );

        ebene[i].addPoint(point);

      }

    }
  }

  public void convertBack() {
    UrbanGUI.points.clear();
    System.out.println("Points size " + UrbanGUI.points.size());
    for (int i = 0; i < ebene.length; i++) {
      R = null;
      Plane pl = UrbanGUI.planes.get(i);
      R = MatrixFactory.createCoordinateFrameZ(pl.getNormal());
      IPointCloud pc = new PointCloud();
      IPointCloud temp = findMeshPoint(ebene[i]);
      for (int j = 0; j < temp.getNumberOfPoints(); j++) {

        Vector pos = R.multiply(temp.getPoint(j).getPosition());
        pos = pos.add(pl.getPoint());

        Point point = new Point(pos, // Position
            temp.getPoint(j).getColor(), // Color
            temp.getPoint(j).getNormal() // Norm
        );

        pc.addPoint(point);

      }

      UrbanGUI.points.add(pc);

    }
  }

  public IPointCloud findMeshPoint(IPointCloud pc) {
    IPointCloud res = new PointCloud();
    Point sxsy = new Point(new Vector(Double.MAX_VALUE, Double.MAX_VALUE, 0), new Vector(3), new Vector(3));
    Point sxby = new Point(new Vector(Double.MAX_VALUE, Double.MIN_VALUE, 0), new Vector(3), new Vector(3));
    Point bxsy = new Point(new Vector(Double.MIN_VALUE, Double.MAX_VALUE, 0), new Vector(3), new Vector(3));
    Point bxby = new Point(new Vector(Double.MIN_VALUE, Double.MIN_VALUE, 0), new Vector(3), new Vector(3));

    double x1 = Double.MAX_VALUE;
    double x2 = Double.MIN_VALUE;
    double y1 = Double.MAX_VALUE;
    double y2 = Double.MIN_VALUE;

    for (int i = 0; i < pc.getNumberOfPoints(); i++) {
      if (pc.getPoint(i).getPosition().get(0) < x1)
        x1 = pc.getPoint(i).getPosition().get(0);
      if (pc.getPoint(i).getPosition().get(0) > x2)
        x2 = pc.getPoint(i).getPosition().get(0);
      if (pc.getPoint(i).getPosition().get(1) < y1)
        y1 = pc.getPoint(i).getPosition().get(1);
      if (pc.getPoint(i).getPosition().get(1) > y2)
        y2 = pc.getPoint(i).getPosition().get(1);

    }

    sxsy = new Point(new Vector(x1, y1, 0), new Vector(3), new Vector(3));
    sxby = new Point(new Vector(x1, y2, 0), new Vector(3), new Vector(3));
    bxsy = new Point(new Vector(x2, y1, 0), new Vector(3), new Vector(3));
    bxby = new Point(new Vector(x2, y2, 0), new Vector(3), new Vector(3));

    res.addPoint(sxsy);
    res.addPoint(sxby);
    res.addPoint(bxsy);
    res.addPoint(bxby);

    return res;
  }

  public static void main(String[] args) {
    new MeshExtractor();
  }
}

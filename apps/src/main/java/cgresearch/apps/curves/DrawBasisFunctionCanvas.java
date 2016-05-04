package cgresearch.apps.curves;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.curves.CurveModel;

import java.awt.Point;

import java.awt.Color;

/**
 * Draw a set of basis functions.
 * 
 * @author Philipp Jenke
 *
 */
public class DrawBasisFunctionCanvas extends Canvas implements Observer {

  /**
   * 
   */
  private static final long serialVersionUID = 6003855750884259773L;

  private final CurveModel curveModel;

  private List<Vector> colors = new ArrayList<Vector>();

  public DrawBasisFunctionCanvas(CurveModel curveModel) {
    this.curveModel = curveModel;
    curveModel.addObserver(this);
  }

  public void paint(Graphics g) {

    // Draw coordinate system
    g.setColor(new Color(0, 0, 0));
    drawLine(g, getPoint(0, 0), getPoint(1, 0));
    drawLine(g, getPoint(0, 0), getPoint(0, 1));

    // Highlight current weights
    drawParameterLine(g, curveModel.getT());
    for (int i = 0; i <= curveModel.getCurve().getDegree(); i++) {
      g.setColor(new Color((float) getColor(i).get(0),
          (float) getColor(i).get(1), (float) getColor(i).get(2)));
      drawCurrentWeight(g, i, curveModel.getT());
    }
    // Draw basis functions
    for (int i = 0; i <= curveModel.getCurve().getDegree(); i++) {
      g.setColor(new Color((float) getColor(i).get(0),
          (float) getColor(i).get(1), (float) getColor(i).get(2)));
      drawBasisFunction(g, i);
    }
  }

  public void drawParameterLine(Graphics g, double t) {
    double fMin = Double.POSITIVE_INFINITY;
    double fMax = Double.NEGATIVE_INFINITY;
    for (int i = 0; i <= curveModel.getCurve().getDegree(); i++) {
      double fi = curveModel.getCurve().getBasisFunction().eval(i, t,
          curveModel.getCurve().getDegree());
      fMin = Math.min(fMin, fi);
      fMax = Math.max(fMax, fi);
    }
    g.setColor(new Color(1, 1, 0));
    drawLine(g, getPoint(t, fMin), getPoint(t, fMax));
  }

  /**
   * Highlight the current weight of the basis function for the given t.
   */
  private void drawCurrentWeight(Graphics g, int index, double t) {
    int circleWidth = 6;
    int circleHeight = 6;
    int textOffset = 6;
    double fi = curveModel.getCurve().getBasisFunction().eval(index, t,
        curveModel.getCurve().getDegree());
    Point p = getPoint(t, fi);
    g.drawOval(p.x - circleWidth / 2, p.y - circleHeight / 2, circleWidth,
        circleHeight);
    g.drawString(String.format("%.2f", fi), p.x + textOffset, p.y + textOffset);
  }

  private void drawBasisFunction(Graphics g, int index) {
    final int RESOLUTION = 50;

    for (int i = 0; i < RESOLUTION; i++) {
      double t = (double) i / (double) (RESOLUTION);
      double tPlus = (double) (i + 1) / (double) (RESOLUTION);
      double fi = curveModel.getCurve().getBasisFunction().eval(index, t,
          curveModel.getCurve().getDegree());
      double fiPlus = curveModel.getCurve().getBasisFunction().eval(index,
          tPlus, curveModel.getCurve().getDegree());
      drawLine(g, getPoint(t, fi), getPoint(tPlus, fiPlus));
    }
  }

  /**
   * Draws a line from start to end.
   */
  private void drawLine(Graphics g, Point start, Point end) {
    g.drawLine(start.x, start.y, end.x, end.y);
  }

  /**
   * Computes canvas coordinates for a point in [0..1, -1...1].
   */
  private Point getPoint(double x, double y) {
    int xOffset = 20;
    int yOffset = 20;
    double scaleX = getWidth() - 2 * xOffset;
    double scaleY = getHeight() / 2.0 - yOffset;
    int baseLine = getHeight() / 2;
    return new Point((int) (xOffset + x * scaleX),
        (int) (baseLine - y * scaleY));
  }

  /**
   * Returns the color for the basis function with the specified index.
   */
  private Vector getColor(int index) {
    while (index >= colors.size()) {
      colors.add(VectorFactory.createVector3(Math.random(), Math.random(),
          Math.random()));
    }
    return colors.get(index);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof CurveModel) {
      repaint();
    }
  }
}

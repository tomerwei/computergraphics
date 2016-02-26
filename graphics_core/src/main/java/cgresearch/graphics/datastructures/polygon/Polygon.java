package cgresearch.graphics.datastructures.polygon;

import java.util.LinkedList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.graphics.material.PolygonMaterial;
import cgresearch.graphics.scenegraph.ICgNodeContent;

/**
 * Representation of a polygon in 3-space.
 * 
 * @author Philipp Jenke
 */
public class Polygon extends ICgNodeContent {

  public enum Type {
    OPEN, CLOSED
  };

  private List<PolygonVertex> points = new LinkedList<PolygonVertex>();
  private List<PolygonEdge> edges = new LinkedList<PolygonEdge>();
  private Type type = Type.CLOSED;

  public Polygon() {
    material = new PolygonMaterial();
  }

  public void addPoint(PolygonVertex p) {
    points.add(p);
  }

  public int getNumPoints() {
    return points.size();
  }

  public PolygonVertex getPoint(int index) {
    return points.get(index);
  }

  @Override
  public BoundingBox getBoundingBox() {
    BoundingBox bbox = new BoundingBox();
    for (PolygonVertex p : points) {
      bbox.add(p.getPosition());
    }
    return bbox;
  }

  public void clear() {
    points.clear();
    edges.clear();
  }

  /**
   * Collapse edge, remove edge from list, remove edge-end from list. Returns
   * the remaining point.
   */
  public PolygonVertex collapse(PolygonEdge polygonEdge, Vector newPosition) {
    PolygonVertex start = polygonEdge.getStartVertex();
    PolygonVertex end = polygonEdge.getEndVertex();
    edges.remove(polygonEdge);
    if (!points.remove(end)) {
      Logger.getInstance().error("Failed to remove point.");
    }
    ;
    start.getPosition().copy(newPosition);
    start.setOutgoingEdge(end.getOutgoingEdge());
    if (end.getOutgoingEdge() != null) {
      end.getOutgoingEdge().setStartVertex(start);
    }
    return start;
  }

  /**
   * Scale polygon to [-1,1]^2 und center at origin
   */
  public void fitToUnitBox() {
    BoundingBox bbox = new BoundingBox();
    for (PolygonVertex p : points) {
      bbox.add(p.getPosition());
    }
    for (int i = 0; i < points.size(); i++) {
      points.get(i).getPosition()
          .copy((points.get(i).getPosition().subtract(bbox.getCenter())).multiply(2.0 / bbox.getMaxExtend()));
    }
  }

  /**
   * Copy points
   */
  public void copy(Polygon other) {
    clear();
    for (int i = 0; i < other.points.size(); i++) {
      addPoint(new PolygonVertex(other.points.get(i).getPosition()));
    }
    for (int edgeIndex = 0; edgeIndex < other.edges.size(); edgeIndex++) {
      PolygonEdge edge = other.edges.get(edgeIndex);
      int startIndex = other.points.indexOf(edge.getStartVertex());
      int endIndex = other.points.indexOf(edge.getEndVertex());
      addEdge(startIndex, endIndex);
    }
  }

  @Override
  public PolygonMaterial getMaterial() {
    return (PolygonMaterial) material;
  }

  public Vector getEdgeColor(int index) {
    return edges.get(index).getColor();
  }

  public void setEdgeColor(int index, Vector color) {
    edges.get(index).getColor().copy(color);
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public boolean isClosed() {
    return type == Type.CLOSED;
  }

  public void addPoint(Vector position) {
    addPoint(new PolygonVertex(position));
  }

  public int getNumEdges() {
    return edges.size();
  }

  public PolygonEdge getEdge(int edgeIndex) {
    return edges.get(edgeIndex);
  }

  public void addEdge(int startIndex, int endIndex) {
    PolygonVertex p0 = points.get(startIndex);
    PolygonVertex p1 = points.get(endIndex);
    PolygonEdge edge = new PolygonEdge(p0, p1);
    p0.setOutgoingEdge(edge);
    p1.setIncomingEdge(edge);
    edges.add(edge);
  }

  /**
   * Insert new point in middle of edge
   */
  public void splitEdge(PolygonEdge edge) {
    PolygonVertex p = edge.getStartVertex();
    PolygonVertex q = edge.getEndVertex();
    PolygonVertex newVertex = new PolygonVertex((p.getPosition().add(q.getPosition())).multiply(0.5));
    edge.setEndVertex(newVertex);
    newVertex.setIncomingEdge(edge);
    PolygonEdge newEdge = new PolygonEdge(newVertex, q);
    points.add(newVertex);
    edges.add(newEdge);
  }
}

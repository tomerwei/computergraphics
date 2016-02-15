package cgresearch.graphics.fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cgresearch.core.assets.CgAssetManager;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.polygon.Polygon;

/**
 * Imports (own) polygon format from file (list of coordinates.
 * 
 * @author Philipp Jenke
 */
public class PolygonIO {

  /**
   * Remember the last point for relative coordinates.s
   */
  private IVector3 lastPoint = VectorMatrixFactory.newIVector3(0, 0, 0);

  private Polygon polygon;

  public Polygon readPolygon(String filename) {
    polygon = new Polygon();

    String strLine = "";
    try {
      InputStream is = CgAssetManager.getInstance().getInputStream(filename);
      if (is == null) {
        Logger.getInstance().error("Failed to create input stream for file " + filename);
        return null;
      }

      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(is);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      // Read type
      Polygon.Type type = Polygon.Type.valueOf(br.readLine());
      polygon.setType(type);

      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        // Print the content on the console
        IVector3 point = parseLine(strLine);
        if (point != null) {
          polygon.addPoint(point);
          if (polygon.getNumPoints() > 1) {
            polygon.addEdge(polygon.getNumPoints() - 2, polygon.getNumPoints() - 1);
          }
        }
      }

      // Closed polygons
      if (polygon.isClosed()) {
        polygon.addEdge(polygon.getNumPoints() - 1, 0);
      }
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to parse polygon file.", e);
    }

    Logger.getInstance().message("Successfully read polygon with " + polygon.getNumPoints() + " points and "
        + polygon.getNumEdges() + " edges.");

    return polygon;
  }

  /**
   * Read line, convert to (2D) point.
   */
  private IVector3 parseLine(String line) {
    String[] tokens = line.trim().split("\\s+");
    if (tokens.length != 3) {
      return null;
    }
    try {
      double x = Double.valueOf(tokens[0].replace(',', '.'));
      double y = Double.valueOf(tokens[1].replace(',', '.'));
      double z = Double.valueOf(tokens[2].replace(',', '.'));
      return VectorMatrixFactory.newIVector3(x, y, z);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Write polygon to file.
   * 
   * Format: x,y (one line for each point).
   */
  public void writePolygon(Polygon polygon, String filename) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(filename));
      writer.write(polygon.getType().toString() + "\n");
      for (int i = 0; i < polygon.getNumPoints(); i++) {
        writer.write(toLine(polygon.getPoint(i).getPosition()) + "\n");
      }
      writer.close();
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write polygon to file", e);
    }
    Logger.getInstance()
        .message("Successfully wrote polygon with " + polygon.getNumPoints() + " points to " + filename + ".");
  }

  /**
   * Create line for point in polygon file.
   */
  private String toLine(IVector3 point) {
    return String.format("%.5f %.5f %.5f", point.get(0), point.get(1), point.get(2));
  }

  /**
   * Imports a polygon from an SVG path (not a complete SVG file!)
   */
  public Polygon importFromSvgPath(String filename) {
    polygon = new Polygon();

    String strLine = "";
    try {
      InputStream is = CgAssetManager.getInstance().getInputStream(filename);
      if (is == null) {
        Logger.getInstance().error("Failed to create input stream for file " + filename);
        return null;
      }

      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(is);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        String[] tokens = strLine.trim().split("\\s+");
        parseTokens(null, tokens, 0);
      }
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to parse polygon file.", e);
    }

    Logger.getInstance().message("Successfully read polygon with " + polygon.getNumPoints() + " points.");

    return polygon;
  }

  private enum State {
    MOVE, MOVE_RELATIVE, CURVE_RELATIVE, CURVE, LINE, LINE_RELATIVE
  }

  /**
   * State-based parsing of token list.
   */
  private void parseTokens(State state, String[] tokens, int tokenIndex) {

    if (tokenIndex >= tokens.length - 1) {
      // Reached end
      return;
    }

    // Check if a new command is provided
    switch (tokens[tokenIndex].charAt(0)) {
      case 'm': {
        parseTokens(State.MOVE_RELATIVE, tokens, tokenIndex + 1);
        return;
      }
      case 'M': {
        parseTokens(State.MOVE, tokens, tokenIndex + 1);
        return;
      }
      case 'c': {
        parseTokens(State.CURVE_RELATIVE, tokens, tokenIndex + 1);
        return;
      }
      case 'C': {
        parseTokens(State.CURVE, tokens, tokenIndex + 1);
        return;
      }
      case 'l': {
        parseTokens(State.LINE_RELATIVE, tokens, tokenIndex + 1);
        return;
      }
      case 'L': {
        parseTokens(State.LINE, tokens, tokenIndex + 1);
        return;
      }
      default:
        String token = tokens[tokenIndex];
        if (token.matches("[a-zA-Z]")) {
          Logger.getInstance().error("Unhandled command: " + token);
          return;
        }
    }

    // Use state to read next coordinates
    switch (state) {
      case MOVE:
        addPointToPolygon(state, tokens, tokenIndex, false);
        return;
      case MOVE_RELATIVE:
        addPointToPolygon(state, tokens, tokenIndex, true);
        return;
      case LINE:
        addPointToPolygon(state, tokens, tokenIndex, false);
        return;
      case LINE_RELATIVE:
        addPointToPolygon(state, tokens, tokenIndex, true);
        return;
      case CURVE:
        addPointToPolygon(state, tokens, tokenIndex + 2, false);
        return;
      case CURVE_RELATIVE:
        addPointToPolygon(state, tokens, tokenIndex + 2, true);
        return;
    }
  }

  /**
   * Add the point at the index tokenIndex to the polygon, recursively call
   * parser for next index, compute relative position if required.
   */
  public void addPointToPolygon(State state, String[] tokens, int tokenIndex, boolean relative) {
    IVector3 point;
    point = readPoint(tokens[tokenIndex]);
    if (point != null) {
      if (relative) {
        point = lastPoint.add(point);
      }
      polygon.addPoint(point);
      lastPoint = point;
    }
    parseTokens(state, tokens, tokenIndex + 1);
  }

  /**
   * Parse point from String.
   */
  private IVector3 readPoint(String coordinates) {
    try {
      String[] tokens = coordinates.split("\\s*,\\s*");
      return VectorMatrixFactory.newIVector3(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), 0);
    } catch (Exception e) {
      return null;
    }
  }
}

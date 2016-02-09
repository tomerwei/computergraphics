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
import cgresearch.graphics.datastructures.Polygon;

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

  public Polygon readPolygon(String filename) {
    Polygon polygon = new Polygon();

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
        // Print the content on the console
        IVector3 point = parseLine(strLine);
        if (point != null) {
          polygon.addPoint(point);
        }
      }
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to parse polygon file.", e);
    }

    Logger.getInstance().message("Successfully read polygon with " + polygon.getNumPoints() + " points.");

    return polygon;
  }

  /**
   * Read line, convert to (2D) point.
   */
  private IVector3 parseLine(String line) {
    String[] tokens = line.trim().split("\\s+");
    if (tokens.length != 2) {
      return null;
    }
    try {
      return VectorMatrixFactory.newIVector3(Double.valueOf(tokens[0].replace(',', '.')),
          Double.valueOf(tokens[1].replace(',', '.')), 0);
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
      for (int i = 0; i < polygon.getNumPoints(); i++) {
        writer.write(toLine(polygon.getPoint(i)) + "\n");
      }
      writer.close();
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to write polygon to file", e);
    }
  }

  /**
   * Create line for point in polygon file.
   */
  private String toLine(IVector3 point) {
    return String.format("%.5f %.5f", point.get(0), point.get(1));
  }

  /**
   * Imports a polygon from an SVG path (not a complete SVG file!)
   */
  public Polygon importFromSvgPath(String filename) {
    Polygon polygon = new Polygon();

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
        parseTokens(null, polygon, tokens);
      }
    } catch (IOException e) {
      Logger.getInstance().exception("Failed to parse polygon file.", e);
    }

    Logger.getInstance().message("Successfully read polygon with " + polygon.getNumPoints() + " points.");

    return polygon;
  }

  private enum State {
    MOVE, RELAVIVE_CURVE
  }

  private void parseTokens(State state, Polygon polygon, String[] tokens) {
    // switch (tokens[0] == 'm') {
    //
    // }
  }
}

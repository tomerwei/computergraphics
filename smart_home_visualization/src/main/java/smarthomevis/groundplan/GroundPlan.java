package smarthomevis.groundplan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFPolyline;
import org.kabeja.dxf.DXFVertex;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;

import cgresearch.core.assets.ResourcesLocator;

public class GroundPlan {
  public void read(String filename, String layerid) {

    FileInputStream in = null;
    try {
      String absoluteFilename = ResourcesLocator.getInstance().getPathToResource(filename);
      in = new FileInputStream(new File(absoluteFilename));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }

    Parser parser = ParserBuilder.createDefaultParser();
    try {

      // parse
      parser.parse(in, DXFParser.DEFAULT_ENCODING);

      // get the documnet and the layer
      DXFDocument doc = parser.getDocument();
      DXFLayer layer = doc.getDXFLayer(layerid);

      // get all polylines from the layer
      List<?> plines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);

      // work with the first polyline
      doSomething((DXFPolyline) plines.get(0));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void doSomething(DXFPolyline pline) {

    // iterate over all vertex of the polyline
    for (int i = 0; i < pline.getVertexCount(); i++) {

      DXFVertex vertex = pline.getVertex(i);
      System.out.println(vertex);

      // do something like collect the data and
      // build a mesh for a FEM system
    }
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    GroundPlan plan = new GroundPlan();
    plan.read("studentprojects/groundplan/draft.dxf", "MAUERWERK");
  }
}

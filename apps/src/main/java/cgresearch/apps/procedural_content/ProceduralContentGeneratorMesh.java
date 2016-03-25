package cgresearch.apps.procedural_content;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.graphics.scenegraph.CgNode;

public class ProceduralContentGeneratorMesh implements ProceduralContentGenerator {

  private static final String MESH = "Mesh";
  private static final String ATTR_FILENAME = "filename";

  @Override
  public String getElementTag() {
    return MESH;
  }

  @Override
  public CgNode parseElement(Element element) {
    CgNode node = new CgNode(null, MESH);
    if (element != null && element.getNodeName().equals(MESH)) {
      try {
        NamedNodeMap attributeMap = element.getAttributes();
        Node filenameNode = attributeMap.getNamedItem(ATTR_FILENAME);
        String filename = filenameNode.getNodeValue();
        if (filename.toUpperCase().endsWith("OBJ")) {
          ObjFileReader reader = new ObjFileReader();
          List<ITriangleMesh> meshes = reader.readFile(filename);
          if (meshes.size() == 1) {
            node.setContent(meshes.get(0));
          } else if (meshes.size() > 0) {
            for (ITriangleMesh mesh : meshes) {
              node.addChild(new CgNode(mesh, "Meshpart"));
            }
          }
        } else {
          Logger.getInstance().error("Unsupported mesh type: " + filename);
        }

      } catch (Exception e) {
        Logger.getInstance().error("Failed to generate " + MESH);
      }
    } else {
      Logger.getInstance().error("Error parsing " + MESH);
    }
    return node;
  }
}

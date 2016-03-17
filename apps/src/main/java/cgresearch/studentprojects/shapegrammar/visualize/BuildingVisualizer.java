package cgresearch.studentprojects.shapegrammar.visualize;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.Node;
import cgresearch.studentprojects.shapegrammar.datastructures.tree.VirtualFormTree;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualPoint;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.IVirtualForm;
import cgresearch.studentprojects.shapegrammar.datastructures.virtualobjects.VirtualShape;
import cgresearch.studentprojects.shapegrammar.grammar.ShapeGrammar;
import cgresearch.studentprojects.shapegrammar.settings.BuildingSettings;

/**
 * The Class BuildingVisualizer. This Class wait for changes in the building and
 * visualize the buildings. This is an Observer
 * 
 * @author Thorben Watzl
 */
public class BuildingVisualizer {

	/**
	 * Start visualization.
	 */
	public List<ITriangleMesh> generateBuilding(BuildingSettings settings) {
		ShapeGrammar shapeGrammar = new ShapeGrammar();
		VirtualFormTree formTree = shapeGrammar.performGrammar(settings);
		List<ITriangleMesh> building = new ArrayList<ITriangleMesh>();
		generateBuilding(building, formTree.getRootNode());
		return building;
	}

	/**
	 * Visualize building.
	 *
	 * @param node
	 *            the node
	 */
	private void generateBuilding(List<ITriangleMesh> building, Node<IVirtualForm> node) {
		if (node.getChildren().isEmpty()) {
			VirtualShape shape = (VirtualShape) node.getData();
			VirtualPoint position = shape.getPosition();
			ITriangleMesh mesh = new TriangleMesh();
			mesh.getMaterial().setTextureId(shape.getTexture());
			mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
			mesh.getMaterial().setReflectionSpecular(VectorFactory.createVector3(0, 0, 0));
			mesh.getMaterial().setReflectionAmbient(VectorFactory.createVector3(1, 1, 1));
			mesh.getMaterial().setSpecularShininess(50);
			Vector v1;
			Vector v2;
			Vector v3;
			Vector v4;
			int a = 0;
			int b = 0;
			int c = 0;
			int d = 0;
			int ta = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 0, 0));
			int tb = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 0, 0));
			int tc = mesh.addTextureCoordinate(VectorFactory.createVector3(1, 1, 0));
			int td = mesh.addTextureCoordinate(VectorFactory.createVector3(0, 1, 0));
			switch (shape.getPosition().getDirection()) {
			case Top:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX() + shape.getWidth(), position.getY(), position.getZ());
				v3 = VectorFactory.createVector3(position.getX() + shape.getWidth(), position.getY(),
						position.getZ() - shape.getHeight());
				v4 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ() - shape.getHeight());
				break;
			case Left:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ() + shape.getWidth());
				v3 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(),
						position.getZ() + shape.getWidth());
				v4 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(), position.getZ());
				break;
			case Right:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ() - shape.getWidth());
				v3 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(),
						position.getZ() - shape.getWidth());
				v4 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(), position.getZ());
				break;
			case Front:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX() + shape.getWidth(), position.getY(), position.getZ());
				v3 = VectorFactory.createVector3(position.getX() + shape.getWidth(),
						position.getY() + shape.getHeight(), position.getZ());
				v4 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(), position.getZ());
				break;
			case Back:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX() - shape.getWidth(), position.getY(), position.getZ());
				v3 = VectorFactory.createVector3(position.getX() - shape.getWidth(),
						position.getY() + shape.getHeight(), position.getZ());
				v4 = VectorFactory.createVector3(position.getX(), position.getY() + shape.getHeight(), position.getZ());
				break;
			case Bot:
				v1 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ());
				v2 = VectorFactory.createVector3(position.getX() + shape.getWidth(), position.getY(), position.getZ());
				v3 = VectorFactory.createVector3(position.getX() + shape.getWidth(), position.getY(),
						position.getZ() + shape.getHeight());
				v4 = VectorFactory.createVector3(position.getX(), position.getY(), position.getZ() + shape.getHeight());
				break;
			default:
				v1 = VectorFactory.createVector3(0, 0, 0);
				v2 = VectorFactory.createVector3(1, 0, 0);
				v3 = VectorFactory.createVector3(1, 1, 0);
				v4 = VectorFactory.createVector3(0, 1, 0);
				break;
			}
			a = mesh.addVertex(new Vertex(v1));
			b = mesh.addVertex(new Vertex(v2));
			c = mesh.addVertex(new Vertex(v3));
			d = mesh.addVertex(new Vertex(v4));
			Triangle triangle1 = new Triangle(a, b, c, ta, tb, tc);
			Triangle triangle2 = new Triangle(d, a, c, td, ta, tc);
			mesh.addTriangle(triangle1);
			mesh.addTriangle(triangle2);
			mesh.computeTriangleNormals();
			mesh.computeVertexNormals();
			building.add(mesh);
		} else {
			for (Node<IVirtualForm> child : node.getChildren()) {
				generateBuilding(building, child);
			}
		}
	}
}

package cgresearch.studentprojects.posegen.datastructure;

import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;

public class SelectedMesh {

	private ITriangleMesh mesh;
	private List<ITriangle> selectedTriangles;

	public SelectedMesh(ITriangleMesh mesh, List<ITriangle> selectedTriangles) {
		this.mesh = mesh;
		this.selectedTriangles = selectedTriangles;
	}

	public void moveTriangles(double deg) {
		if (selectedTriangles == null) {
			System.out.println("selectedTriangles == NULL");
			return;
		}
		if (selectedTriangles.size() == 0) {
			System.out.println("Size=0");
		}
		if (mesh == null) {
			System.out.println("MESH  == NULL");
		}

		for (ITriangle triangle : selectedTriangles) {
			IVertex vexAVertex = mesh.getVertex(triangle.getA());
			IVertex vexBVertex = mesh.getVertex(triangle.getB());
			IVertex vexCVertex = mesh.getVertex(triangle.getC());

			if (!(vexAVertex instanceof VertexMutable) || !(vexBVertex instanceof VertexMutable)
					|| !(vexCVertex instanceof VertexMutable)) {
				Logger.getInstance()
						.error("Tried to manipulate a Vertex not of Type 'VertexMutable' inside SelectedMesh");
				return;
			}
			VertexMutable vexA = (VertexMutable) vexAVertex;
			VertexMutable vexB = (VertexMutable) vexBVertex;
			VertexMutable vexC = (VertexMutable) vexCVertex;

			// Überlappt Anzeige fehler weil beide gleichen Z value
			double x = vexA.position.get(0) + 0.01;
			double y = vexA.position.get(1) + 0.01;
			double z = vexA.position.get(2);
			vexA.position.set(x, y, z);

			x = vexB.position.get(0) + 0.01;
			y = vexB.position.get(1) + 0.01;
			z = vexB.position.get(2);
			vexB.position.set(x, y, z);

			x = vexC.position.get(0) + 0.01;
			y = vexC.position.get(1) + 0.01;
			z = vexC.position.get(2);
			vexC.position.set(x, y, z);

			mesh.updateRenderStructures();
		}
	}
}

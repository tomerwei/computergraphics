package cgresearch.studentprojects.posegen.datastructure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
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

	public void moveTriangles(double deg, Vector rotationPosition) {
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

		List<VertexMutable> verticesToRotate = new ArrayList<>();
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
//			double x = vexA.position.get(0) + 0.01;
//			double y = vexA.position.get(1) + 0.01;
//			double z = vexA.position.get(2);
//			vexA.position.set(x, y, z);
//
//			x = vexB.position.get(0) + 0.01;
//			y = vexB.position.get(1) + 0.01;
//			z = vexB.position.get(2);
//			vexB.position.set(x, y, z);
//
//			x = vexC.position.get(0) + 0.01;
//			y = vexC.position.get(1) + 0.01;
//			z = vexC.position.get(2);
//			vexC.position.set(x, y, z);
			
			verticesToRotate.add(vexA);
			verticesToRotate.add(vexB);
			verticesToRotate.add(vexC);
			
			mesh.updateRenderStructures();
		}
		
		rotateAllVerticesOnce(deg, rotationPosition, verticesToRotate);
	}
	
	/**
	 * Rotates all vertices, but only once each, if list contains a Vertex multiple times
	 * @param winkelDeg Deg in rad 
	 * @param drehPunkt rotation point
	 * @param vertices list of vertices.
	 */
	public void rotateAllVerticesOnce(double rad, Vector drehPunkt, List<VertexMutable> vertices){
		List<VertexMutable> noDuplicates = new ArrayList<VertexMutable>(new LinkedHashSet<VertexMutable>(vertices));
		for(VertexMutable vex : noDuplicates){
			rotateUmDrehpunkt(rad, drehPunkt, vex);
		}
	}
	
	public double degToRad(double deg) {
		return (deg * Math.PI / 180);
	}
	
	private void rotateUmDrehpunkt(double winkelDeg, Vector drehPunkt, VertexMutable vex) {

		
		double winkelRad = degToRad(winkelDeg);
//		winkelRad = degToRad(winkelRad);
		double xEnd = vex.getPosition().get(0);
		double yEnd = vex.getPosition().get(1);
		double zEnd = vex.getPosition().get(2);

		double xDrehpunkt = drehPunkt.get(0);
		double yDrehpunkt = drehPunkt.get(1);

		double xStrich = xDrehpunkt + Math.cos(winkelRad) * (xEnd - xDrehpunkt)
				- Math.sin(winkelRad) * (yEnd - yDrehpunkt);
		double yStrich = yDrehpunkt + Math.sin(winkelRad) * (xEnd - xDrehpunkt)
				+ Math.cos(winkelRad) * (yEnd - yDrehpunkt);
		double zStrich = zEnd;
		
		vex.setPosition(new Vector(xStrich, yStrich, zStrich));
	}

}

package cgresearch.studentprojects.posegen.datastructure;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;

/*
 * SELECTED MESH == ? BoneMeshMap nur andere methoden?
 */
public class SelectedMesh {

	private ITriangleMesh mesh;
	private List<ValuePair<IVertex,Double>> selectedVertices;

	public SelectedMesh(ITriangleMesh mesh, List<ValuePair<IVertex,Double>> selectedVertices) {
		this.mesh = mesh;
		this.selectedVertices = selectedVertices;
	}

	public void rotateTrianglePositions(double deg, Vector rotationPosition) {
		if (selectedVertices == null) {
			System.out.println("selectedTriangles == NULL");
			return;
		}
		if (selectedVertices.size() == 0) {
			System.out.println("Size=0");
		}
		if (mesh == null) {
			System.out.println("MESH  == NULL");
		}
		
		ValuePair<VertexMutable,Double> valueMutablePair;
		List<ValuePair<VertexMutable,Double>> verticesToRotate = new ArrayList<>();
		for (ValuePair<IVertex,Double> valuePair : selectedVertices) {
			// IVertex vexAVertex = mesh
			// IVertex vexBVertex = mesh.getVertex(triangle.getB());
			// IVertex vexCVertex = mesh.getVertex(triangle.getC());

			IVertex vexAVertex = valuePair.getValue1(); 
			if (!(vexAVertex instanceof VertexMutable)) {
				Logger.getInstance()
						.error("Tried to manipulate a Vertex not of Type 'VertexMutable' inside SelectedMesh");
				return;
			}
			VertexMutable vexA = (VertexMutable) vexAVertex;

			valueMutablePair = new ValuePair<VertexMutable, Double>(vexA, valuePair.getValue2());
			verticesToRotate.add(valueMutablePair);

			mesh.updateRenderStructures();
		}

		rotateAllVerticesOnce(deg, rotationPosition, verticesToRotate);
	}

	/**
	 * Rotates all vertices, but only once each, if list contains a Vertex
	 * multiple times
	 * 
	 * @param winkelDeg
	 *            Deg in rad
	 * @param drehPunkt
	 *            rotation point
	 * @param vertices
	 *            list of vertices.
	 */
	public void rotateAllVerticesOnce(double rad, Vector drehPunkt, List<ValuePair<VertexMutable,Double>> vertices) {
		List<ValuePair<VertexMutable,Double>> noDuplicates = new ArrayList<ValuePair<VertexMutable,Double>>(new LinkedHashSet<ValuePair<VertexMutable,Double>>(vertices));
		for (ValuePair<VertexMutable,Double> vexPair : noDuplicates) {
			rotateUmDrehpunkt(rad, drehPunkt, vexPair);
		}
	}

	public double degToRad(double deg) {
		return (deg * Math.PI / 180);
	}

	private void rotateUmDrehpunkt(Double winkelDeg, Vector drehPunkt, ValuePair<VertexMutable,Double> vexPair) {
		VertexMutable vex = vexPair.getValue1();
		winkelDeg = winkelDeg * vexPair.getValue2(); //Weight factor
//		if(winkelDeg.equals(0.0) || winkelDeg.isNaN()){
//			return;
//		}
//		System.out.println("WinkelDeg: " + winkelDeg);
		double winkelRad = degToRad(winkelDeg);
		// winkelRad = degToRad(winkelRad);
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

		// Vector realNewCords =
		//If overlapping with neighbor vertices. Just pushed up to neighbor vertex
		vex.trySetPosition(new Vector(xStrich, yStrich, zStrich));

	}

}

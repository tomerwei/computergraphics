/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.datastructures.halfedge;

import java.util.HashMap;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Provides conversion functionality from a @see TriangleMesh zu a half edge
 * date structure.
 * 
 * @author Philipp Jenke
 * 
 */
public class TriangleMeshHalfEdgeConverter {

    /**
     * Create a half edge data structure from a Triangle mesh.
     * 
     * @return Half edge data structure.
     */
    public IHalfEdgeDatastructure convert(ITriangleMesh triangleMesh) {
        IHalfEdgeDatastructure halfEdgeDatastructure = new HalfEdgeDatastructure();

        // Create vertices
        for (int vertexIndex = 0; vertexIndex < triangleMesh
                .getNumberOfVertices(); vertexIndex++) {
            HalfEdgeVertex vertex = new HalfEdgeVertex(triangleMesh.getVertex(
                    vertexIndex).getPosition());
            halfEdgeDatastructure.addVertex(vertex);
        }

        // Create facets and half edges
        for (int triangleIndex = 0; triangleIndex < triangleMesh
                .getNumberOfTriangles(); triangleIndex++) {
            Triangle triangle = triangleMesh.getTriangle(triangleIndex);

            // Create facet
            IHalfEdgeFacet facet = new HalfEdgeTriangle();
            halfEdgeDatastructure.addFacet(facet);

            // Create half edges
            HalfEdge[] halfEdges = new HalfEdge[3];
            for (int halfEdgeIndex = 0; halfEdgeIndex < 3; halfEdgeIndex++) {
                halfEdges[halfEdgeIndex] = new HalfEdge();
            }

            for (int halfEdgeIndex = 0; halfEdgeIndex < 3; halfEdgeIndex++) {
                // Set half edge properties: vertices
                HalfEdgeVertex startVertex = halfEdgeDatastructure
                        .getVertex(triangle.get(halfEdgeIndex));
                halfEdges[halfEdgeIndex].setVertex(startVertex);

                // Set half edge properties: prev and next
                halfEdges[halfEdgeIndex]
                        .setNext(halfEdges[(halfEdgeIndex + 1) % 3]);
                halfEdges[halfEdgeIndex]
                        .setPrev(halfEdges[(halfEdgeIndex + 2) % 3]);

                // Set half edge properties: facet
                halfEdges[halfEdgeIndex].setFacet(facet);

                // Set Vertex properties: halfEdge
                startVertex.setHalfEdge(halfEdges[halfEdgeIndex]);

                // Add half edge to data structure
                halfEdgeDatastructure.addHalfEdge(halfEdges[halfEdgeIndex]);
            }
            // Set facet properties: halfEdge
            facet.setHalfEdge(halfEdges[0]);
        }

        // Link opposite edges
        for (int halfEdgeIndex = 0; halfEdgeIndex < halfEdgeDatastructure
                .getNumberOfHalfEdges(); halfEdgeIndex++) {
            for (int halfEdgeIndexUpper = halfEdgeIndex + 1; halfEdgeIndexUpper < halfEdgeDatastructure
                    .getNumberOfHalfEdges(); halfEdgeIndexUpper++) {

                HalfEdge halfEdge1 = halfEdgeDatastructure
                        .getHalfEdge(halfEdgeIndex);
                HalfEdge halfEdge2 = halfEdgeDatastructure
                        .getHalfEdge(halfEdgeIndexUpper);
                if (halfEdge1.getOpposite() == null
                        && halfEdge1.getVertex() == halfEdge2.getNext()
                                .getVertex()
                        && halfEdge1.getNext().getVertex() == halfEdge2
                                .getVertex()) {
                    halfEdge1.setOpposite(halfEdge2);
                    halfEdge2.setOpposite(halfEdge1);
                }
            }
        }

        Logger.getInstance().message(
                "Successfully created half edge data structure with "
                        + halfEdgeDatastructure.getNumberOfVertices()
                        + " vertices, "
                        + halfEdgeDatastructure.getNumberOfFacets()
                        + " facets and "
                        + halfEdgeDatastructure.getNumberOfHalfEdges()
                        + " half edges.");

        return halfEdgeDatastructure;
    }

    /**
     * Convert a half edge data structure to a triangle mesh. The half edge
     * facets must be triangles.
     * 
     * @param halfEdgeTriangleMesh
     *            Half edge data structure to be converted.
     * @return Triangle mesh. Returns null, if conversion failed.
     */
    public ITriangleMesh convert(IHalfEdgeDatastructure halfEdgeTriangleMesh) {
        TriangleMesh triangleMesh = new TriangleMesh();

        HashMap<HalfEdgeVertex, Integer> vertexToIndexMap = new HashMap<HalfEdgeVertex, Integer>();

        // Add vertices
        for (int vertexIndex = 0; vertexIndex < halfEdgeTriangleMesh
                .getNumberOfVertices(); vertexIndex++) {
            HalfEdgeVertex halfEdgeVertex = halfEdgeTriangleMesh
                    .getVertex(vertexIndex);
            Vertex vertex = new Vertex(halfEdgeVertex.getPosition());
            int index = triangleMesh.addVertex(vertex);
            vertexToIndexMap.put(halfEdgeVertex, index);
        }

        // Add triangles
        for (int facetIndex = 0; facetIndex < halfEdgeTriangleMesh
                .getNumberOfFacets(); facetIndex++) {
            IHalfEdgeFacet facet = halfEdgeTriangleMesh.getFacet(facetIndex);
            if (facet instanceof HalfEdgeTriangle) {
                HalfEdgeTriangle halfEdgeTriangle = (HalfEdgeTriangle) facet;
                HalfEdge e0 = halfEdgeTriangle.getHalfEdge();
                HalfEdge e1 = e0.getNext();
                HalfEdge e2 = e1.getNext();
                if (e0 == null || e1 == null || e2 == null
                        || e2.getNext() != e0) {
                    Logger.getInstance()
                            .error("Invalid half edge structure, aborting conversion.");
                    return null;
                }
                HalfEdgeVertex v0 = e0.getVertex();
                HalfEdgeVertex v1 = e1.getVertex();
                HalfEdgeVertex v2 = e2.getVertex();
                if (!vertexToIndexMap.containsKey(v0)
                        || !vertexToIndexMap.containsKey(v1)
                        || !vertexToIndexMap.containsKey(v2)) {
                    Logger.getInstance()
                            .error("Invalid data structure, vertex missing - aborting conversion.");
                    return null;
                }
                int index0 = vertexToIndexMap.get(v0);
                int index1 = vertexToIndexMap.get(v1);
                int index2 = vertexToIndexMap.get(v2);
                if (index0 == -1 || index1 == -1 || index2 == -1) {
                    Logger.getInstance()
                            .error("Invalid half edge structure, aborting conversion.");
                    return null;
                }
                Triangle triangle = new Triangle(index0, index1, index2);
                triangleMesh.addTriangle(triangle);
            } else {
                Logger.getInstance()
                        .error("Only tringle facets supported in half edge datatstructure to triangle mesh converter.");
                return null;
            }
        }

        triangleMesh.computeTriangleNormals();
        triangleMesh.computeVertexNormals();
        return triangleMesh;
    }
}

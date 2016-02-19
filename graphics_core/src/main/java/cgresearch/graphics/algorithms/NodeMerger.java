/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.algorithms;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.trianglemesh.ITriangle;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;

/**
 * Merge the nodes (vertices) of a triange mesh withing a given distance.
 * 
 * @author Philipp Jenke
 * 
 */
public class NodeMerger {

    /**
     * Merge all vertives in the input triangle mesh which are closer than the
     * given epsilion distance. The input mesh is not altered.
     * 
     * @return A new triangle mesh with merged nodes.
     */
    public static ITriangleMesh merge(ITriangleMesh inputMesh, double epsilon) {
        TriangleMesh outputMesh = new TriangleMesh();

        for (int triangleIndex = 0; triangleIndex < inputMesh
                .getNumberOfTriangles(); triangleIndex++) {
            ITriangle triangle = inputMesh.getTriangle(triangleIndex);

            Vector a = inputMesh.getVertex(triangle.getA()).getPosition();
            Vector b = inputMesh.getVertex(triangle.getB()).getPosition();
            Vector c = inputMesh.getVertex(triangle.getC()).getPosition();

            outputMesh.addTriangle(new Triangle(getVertexIndexInNewMesh(a,
                    outputMesh, epsilon), getVertexIndexInNewMesh(b,
                    outputMesh, epsilon), getVertexIndexInNewMesh(c,
                    outputMesh, epsilon), triangle.getTextureCoordinate(0),
                    triangle.getTextureCoordinate(1), triangle
                            .getTextureCoordinate(2)));
        }

        Logger.getInstance().message(
                "NodeMerger: simplified from "
                        + inputMesh.getNumberOfVertices() + " verticed to "
                        + outputMesh.getNumberOfVertices());

        outputMesh.computeTriangleNormals();
        outputMesh.computeVertexNormals();

        return outputMesh;

    }

    /**
     * Check if the vertex exists in the mesh already (up to epsilon distance).
     * If not, add it to the mesh. Returns the index of the vertex in the mesh.
     * 
     * @param Vector
     *            vertexPosition;
     * @param ITriangleMesh
     *            Mesh object
     * @param epsisilon
     *            Epsilon distance.
     * @return
     */
    private static int getVertexIndexInNewMesh(Vector position,
            ITriangleMesh mesh, double epsilon) {

        for (int vertexIndex = 0; vertexIndex < mesh.getNumberOfVertices(); vertexIndex++) {
            if (getSquaredDistance(mesh.getVertex(vertexIndex).getPosition(),
                    position) <= epsilon * epsilon) {
                return vertexIndex;
            }
        }
        Vertex vertex = new Vertex(position);
        return mesh.addVertex(vertex);
    }

    /**
     * Compute the squared distance between two positions.
     * 
     * @param position
     *            First position
     * @param position2
     *            Second position
     * @return Distance.
     */
    private static double getSquaredDistance(Vector position,
            Vector position2) {
        return position.subtract(position2).getSqrNorm();
    }
}

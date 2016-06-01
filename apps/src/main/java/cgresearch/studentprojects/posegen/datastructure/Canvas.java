package cgresearch.studentprojects.posegen.datastructure;

import javax.management.RuntimeErrorException;

import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.Material;

/**
 * 
 * @author Lars A canvas made of triangles to texture with an image.
 *
 */
public class Canvas extends TriangleMesh {

	// ALTERNATIV LINE SEGMENT STATT TRIANGLE MESH?!
	public Canvas() {
		super();
		// IVertex vertexTopLeft = new Vertex(VectorFactory.createVector3(0.0,
		// 0.9, 0.0));
		// IVertex vertexBottomRight = new
		// Vertex(VectorFactory.createVector3(0.9, 0.0, 0.0));
		// IVertex vertexBottomLeft = new
		// Vertex(VectorFactory.createVector3(0.0, 0.0, 0.0));
		//
		// int indexTopLeft = this.addVertex(vertexTopLeft);
		// int indexBottomRight = this.addVertex(vertexBottomRight);
		// int indexBottomLeft = this.addVertex(vertexBottomLeft);
		// this.addTriangle(indexBottomLeft, indexBottomRight, indexTopLeft);

		createCanvas(-0.75, -1.5, 1.5, 3.0, 0.1);
	}

	/**
	 * @param offsetX
	 *            starting position of first Squares
	 * @param offsetY
	 *            starting position of first Squares
	 * @param totalWidth
	 *            Width in pxl
	 * @param totalHeight
	 *            height in pixel.
	 * @param squaresize
	 *            How wide and high is one tile
	 */
	public void createCanvas(double offsetX, double offsetY, double totalWidth, double totalHeight, double squaresize) {
		if (totalWidth <= 0 || totalHeight <= 0 || squaresize <= 0) {
			throw new RuntimeException("Got Non-Positive Input");
		}

		int amountWide = (int) (totalWidth / squaresize); // Tiles wide
		int amountHeight = (int) (totalHeight / squaresize);

		int indexBottomLeft;
		int indexTopLeft;
		int indexBottomRight;
		int indexTopRight;
		// int triangleCounter = 0;
		for (int x = 0; x < amountWide; x++) {
			for (int y = 0; y < amountHeight; y++) {
				// Left side of triangles (Top and bottom)
				double posLeftX = (x * squaresize) + offsetX;
				double posRightX = (x * squaresize) + squaresize + offsetX;

				double posBottomY = (y * squaresize) + offsetY; // bottom
				double posTopY = (y * squaresize) + squaresize + offsetY; // top

				IVertex vertexBottomLeft = new Vertex(VectorFactory.createVector3(posLeftX, posBottomY, -0.1));
				IVertex vertexTopLeft = new Vertex(VectorFactory.createVector3(posLeftX, posTopY, -0.1));
				IVertex vertexBottomRight = new Vertex(VectorFactory.createVector3(posRightX, posBottomY, -0.1));
				IVertex vertexTopRight = new Vertex(VectorFactory.createVector3(posRightX, posTopY, -0.1));
				indexBottomLeft = this.addVertex(vertexBottomLeft);
				indexTopLeft = this.addVertex(vertexTopLeft);
				indexBottomRight = this.addVertex(vertexBottomRight);
				indexTopRight = this.addVertex(vertexTopRight);
				this.addTriangle(indexBottomLeft, indexTopRight, indexTopLeft);
				this.addTriangle(indexBottomLeft, indexBottomRight, indexTopRight);

				// this.setTriangleVisible(triangleCounter++, false);
				// triangleCounter++;
			}
		}
		this.material.addShaderId(Material.SHADER_WIREFRAME);
		this.material.setTransparency(1.0);

		updateRenderStructures();

	}
}

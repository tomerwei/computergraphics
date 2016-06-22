package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;

/**
 * 
 * @author Lars A canvas made of triangles to texture with an image.
 *
 */
public class Canvas extends TriangleMesh {

	public void enableWireframe(){
		this.getMaterial().setShaderId(Material.SHADER_TEXTURE_NO_LIGHT);
//		this.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
		this.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
		this.getMaterial().setTextureUsesAlpha(true);
		this.needsUpdateRenderStructures();
	}
	
	public void disableWireframe(){
		this.getMaterial().setShaderId(Material.SHADER_TEXTURE_NO_LIGHT);
//		enableWireframe();
		this.getMaterial().setTextureUsesAlpha(true);
	}
	// ALTERNATIV LINE SEGMENT STATT TRIANGLE MESH?!
	public Canvas() {
		super();
		loadTexture();
		createCanvas(-2, -1.5, 4, 3, 0.1); // 4:3
		this.getMaterial().setTextureId("pirate"); // 640*480 / 4:3
		disableWireframe();
//		this.getMaterial().setShaderId(Material.SHADER_TEXTURE);
//		this.getMaterial().addShaderId(Material.SHADER_WIREFRAME);
		// this.getMaterial().setShaderId(Material.SHADER_WIREFRAME);
		// this.getMaterial().setReflectionSpecular(VectorFactory.createVector3(0,
		// 0, 0));
		// this.getMaterial().setReflectionAmbient(VectorFactory.createVector3(0.2,
		// 0.2, 0.2));
		// this.getMaterial().setSpecularShininess(50);
		this.getMaterial().setTransparency(0.5);
	}

	public void loadTexture() {
		// C:\8Bachelor\baproject\computergraphics\assets\posegen
		CgTexture pirateTexture = new CgTexture("posegen/piratePose.png");
		String pirateTextureId = "pirate";
		ResourceManager.getTextureManagerInstance().addResource(pirateTextureId, pirateTexture);
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

		// How far to look back for an allready existing vertex
		int lookback = amountHeight * 4; // Last row, 4 vertices p. quad

		for (int x = 0; x < amountWide; x++) {
			for (int y = 0; y < amountHeight; y++) { // Height has to be
														// innerloop. Only
														// tested if a vertex is
														// allready there in the
														// last: height*n ::
														// tryToAddOrGetExisting(..,
														// int)
				// Left side of triangles (Top and bottom)
				double posLeftX = (x * squaresize) + offsetX;
				double posRightX = (x * squaresize) + squaresize + offsetX;

				double posBottomY = (y * squaresize) + offsetY; // bottom
				double posTopY = (y * squaresize) + squaresize + offsetY; // top

				VertexMutable vertexBottomLeft = new VertexMutable(
						VectorFactory.createVector3(posLeftX, posBottomY, -0.1));
				// VertexMutable vertexBottomLeft2 = new
				// VertexMutable(VectorFactory.createVector3(posLeftX,
				// posBottomY, -0.1));

				VertexMutable vertexTopLeft = new VertexMutable(VectorFactory.createVector3(posLeftX, posTopY, -0.1));
				VertexMutable vertexBottomRight = new VertexMutable(
						VectorFactory.createVector3(posRightX, posBottomY, -0.1));
				VertexMutable vertexTopRight = new VertexMutable(VectorFactory.createVector3(posRightX, posTopY, -0.1));
				// VertexMutable vertexTopRight2 = new
				// VertexMutable(VectorFactory.createVector3(posRightX, posTopY,
				// -0.1));

				Vector normal = new Vector(0.0, 0.0, -1.0);
				normal.normalize();
				vertexBottomLeft.setNormal(normal);
				vertexTopLeft.setNormal(normal);
				vertexBottomRight.setNormal(normal);
				vertexTopRight.setNormal(normal);

				indexBottomLeft = tryToAddOrGetExisting(vertexBottomLeft, lookback);
				indexTopLeft = tryToAddOrGetExisting(vertexTopLeft, lookback);
				indexBottomRight = tryToAddOrGetExisting(vertexBottomRight, lookback);
				indexTopRight = tryToAddOrGetExisting(vertexTopRight, lookback);

				double x_faktorTexCoords = ((double) x+1) / amountWide;
				double y_faktorTexCoords = ((double) y+1) / amountHeight;
				double x_bonusLast = ((double) x) / amountWide;
				double y_bonusLast = ((double) y) / amountHeight;
				
				int ta = this.addTextureCoordinate(new Vector(x_bonusLast, y_bonusLast, 0.0));
				int tb = this.addTextureCoordinate(new Vector(1.0 * x_faktorTexCoords, y_bonusLast, 0.0));
				int tc = this.addTextureCoordinate(new Vector(1.0 * x_faktorTexCoords, 1.0 * y_faktorTexCoords, 0.0));
				int td = this.addTextureCoordinate(new Vector(x_bonusLast, 1.0 * y_faktorTexCoords, 0.0));

				Triangle triangle1 = new Triangle(indexBottomLeft, indexTopRight, indexTopLeft, ta, tc, td);
				Triangle triangle2 = new Triangle(indexBottomLeft, indexBottomRight, indexTopRight, ta, tb, tc);
				this.addTriangle(triangle1);
				this.addTriangle(triangle2);
			}
		}
		// this.material.addShaderId(Material.SHADER_WIREFRAME);
		// this.material.addShaderId(Material.SHADER_PHONG_SHADING);
		// this.material.setTransparency(1.0);

		updateRenderStructures();
		System.out.println(this.getNumberOfVertices());
	}

	// private int tryToAddOrGetExisting(VertexMutable vertex) {
	// Vector position = vertex.getPosition();
	// int numberOfVertices = this.getNumberOfVertices();
	// for (int i = 0; i < numberOfVertices; i++) {
	// if (this.getVertex(i).getPosition().equals(position)) {
	// return i;
	// }
	// }
	// return this.addVertex(vertex);
	// }

	/**
	 * Is there allready a vertex with the given coords. If there is a vertex ->
	 * Returns the index of the vertex. Not there -> adds vertex, and returns
	 * index
	 * 
	 * @param vertex
	 * @return
	 */
	private int tryToAddOrGetExisting(VertexMutable vertex, int testOnlyLastN) {
		Vector position = vertex.getPosition();
		int numberOfVertices = this.getNumberOfVertices();
		for (int i = numberOfVertices - 1; i > numberOfVertices - testOnlyLastN; i--) {
			if (i < 0 || i >= numberOfVertices) {
				break; // Out of range
			}
			if (this.getVertex(i).getPosition().equals(position)) {
				return i;
			}
		}
		return this.addVertex(vertex);
	}
}

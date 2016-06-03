package cgresearch.studentprojects.posegen.datastructure;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorFactory;
import cgresearch.graphics.datastructures.trianglemesh.IVertex;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;

/**
 * 
 * @author Lars A canvas made of triangles to texture with an image.
 *
 */
public class Canvas extends TriangleMesh {

	// ALTERNATIV LINE SEGMENT STATT TRIANGLE MESH?!
	public Canvas() {
		super();
		loadTexture();
		createCanvas(-2, -1.5, 4, 3, 0.1); // 4:3
		this.getMaterial().setTextureId("pirate"); // 640*480 / 4:3
		this.getMaterial().setShaderId(Material.SHADER_TEXTURE);
		// this.getMaterial().setReflectionSpecular(VectorFactory.createVector3(0,
		// 0, 0));
		// this.getMaterial().setReflectionAmbient(VectorFactory.createVector3(0.2,
		// 0.2, 0.2));
		// this.getMaterial().setSpecularShininess(50);
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

		// double x_bonusLast = 0.0;// ((double)x-1)/amountWide;
		// double y_bonusLast = 0.0;// ((double)y-1)/amountHeight;

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

				Vector normal = new Vector(0.0, 0.0, -1.0);
				normal.normalize();
				vertexBottomLeft.setNormal(normal);
				vertexTopLeft.setNormal(normal);
				vertexBottomRight.setNormal(normal);
				vertexTopRight.setNormal(normal);

				indexBottomLeft = this.addVertex(vertexBottomLeft);
				indexTopLeft = this.addVertex(vertexTopLeft);
				indexBottomRight = this.addVertex(vertexBottomRight);
				indexTopRight = this.addVertex(vertexTopRight);

				double x_faktorTexCoords = ((double) x) / amountWide;
				double y_faktorTexCoords = ((double) y) / amountHeight;
				double x_bonusLast = ((double) x - 1) / amountWide;
				double y_bonusLast = ((double) y - 1) / amountHeight;

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

	}
}

package cgresearch.apps.hlsvis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;

/**
 * Create triangle meshes for height fields from images.
 * 
 * @author Philipp Jenke
 *
 */
public class HeightField {

	/**
	 * Image objects
	 */
	private BufferedImage imageHeightField = null;

	/**
	 * Id of the scene surface texture
	 */
	private String TEXTURE_ID = "COLOR_MAP";

	/**
	 * Max height (value 1 in the height map) of the scene height field.
	 */
	private final double maxHeight;

	/**
	 * Constructor
	 */
	public HeightField(String heightFieldFilename, String colorFilename,
			double maxHeight) {
		this.maxHeight = maxHeight;
		try {
			imageHeightField = ImageIO.read(new File(heightFieldFilename));
		} catch (IOException e) {
			Logger.getInstance().exception(
					"Failed to load height field image " + heightFieldFilename
							+ " or color image " + colorFilename, e);
		}

		// Generate texture
		ResourceManager.getTextureManagerInstance().addResource(TEXTURE_ID,
				new CgTexture(colorFilename));
	}

	/**
	 * Create a triangle mesh for a height field given as an image.
	 */
	public ITriangleMesh createHeightFieldMesh(int resolutionU, int resolutionV) {

		ITriangleMesh mesh = new TriangleMesh();
		double deltaU = 1.0 / (resolutionU - 1);
		double deltaV = 1.0 / (resolutionV - 1);

		for (int i = 0; i < resolutionU; i++) {
			for (int j = 0; j < resolutionV; j++) {
				float x = (float) i / (float) resolutionU;
				float y = (float) j / (float) resolutionV;
				double height = getHeight(x, y);
				Vertex vertex = new Vertex(VectorMatrixFactory.newIVector3(
						deltaU * i, height, deltaV * j));
				mesh.addVertex(vertex);
				mesh.addTextureCoordinate(VectorMatrixFactory.newIVector3(i
						* deltaU, 1 - j * deltaV, 0));
			}
		}

		for (int i = 1; i < resolutionU; i++) {
			for (int j = 1; j < resolutionV; j++) {
				int i00 = (i - 1) * resolutionU + (j - 1);
				int i01 = i * resolutionU + (j - 1);
				int i11 = i * resolutionU + j;
				int i10 = (i - 1) * resolutionU + j;
				mesh.addTriangle(new Triangle(i00, i01, i11, i00, i01, i11));
				mesh.addTriangle(new Triangle(i00, i11, i10, i00, i11, i10));
			}
		}

		mesh.computeTriangleNormals();
		mesh.computeVertexNormals();

		mesh.getMaterial().setTextureId(TEXTURE_ID);
		mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
		mesh.getMaterial().setReflectionDiffuse(Material.PALETTE2_COLOR2);

		return mesh;
	}

	/**
	 * Get the current height field value.
	 */
	public double getHeight(double x, double y) {
		int i = (int) (x * imageHeightField.getWidth());
		int j = (int) (y * imageHeightField.getHeight());
		return ((imageHeightField.getRGB(i, j) >> 16) & 0x000000FF) / 255.0
				* maxHeight;
	}

	public double getMaxHeight() {
		return maxHeight;
	}
}

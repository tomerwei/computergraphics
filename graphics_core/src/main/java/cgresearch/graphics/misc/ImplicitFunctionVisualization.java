package cgresearch.graphics.misc;

import java.awt.Color;
import java.awt.image.BufferedImage;

import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.implicitfunction.IImplicitFunction3D;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Triangle;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.Vertex;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.Material;
import cgresearch.graphics.material.ResourceManager;

/**
 * Create a visualization of a implicit function (texture image).
 * 
 * @author Philipp Jenke
 *
 */
public class ImplicitFunctionVisualization {

	/**
	 * This image contains the result. Only valid after create() was called.
	 */
	private BufferedImage image = null;

	/**
	 * This texture shows the sampled area. Only valid after create() was
	 * called.
	 */
	private CgTexture texture = null;

	/**
	 * This mesh represents the sampled area. Only valid after create() was
	 * called.
	 */
	private ITriangleMesh mesh = null;

	/**
	 * Constructor
	 */
	public ImplicitFunctionVisualization(int resolution) {
		image = new BufferedImage(resolution, resolution,
				BufferedImage.TYPE_INT_RGB);
		texture = new CgTexture(image);
		mesh = new TriangleMesh();
		initializeMesh();
		String id = ResourceManager.getTextureManagerInstance().generateId();
		ResourceManager.getTextureManagerInstance().addResource(id, texture);
		mesh.getMaterial().setTextureId(id);
		mesh.getMaterial().setShaderId(Material.SHADER_TEXTURE);
	}

	/**
	 * Create an initial version of the mesh
	 */
	private void initializeMesh() {
		int i00 = mesh.addVertex(new Vertex());
		int i01 = mesh.addVertex(new Vertex());
		int i10 = mesh.addVertex(new Vertex());
		int i11 = mesh.addVertex(new Vertex());
		int t00 = mesh.addTextureCoordinate(VectorMatrixFactory.newVector(0,
				0, 0));
		int t01 = mesh.addTextureCoordinate(VectorMatrixFactory.newVector(0,
				1, 0));
		int t10 = mesh.addTextureCoordinate(VectorMatrixFactory.newVector(1,
				0, 0));
		int t11 = mesh.addTextureCoordinate(VectorMatrixFactory.newVector(1,
				1, 0));
		mesh.addTriangle(new Triangle(i00, i01, i11, t00, t01, t11));
		mesh.addTriangle(new Triangle(i00, i11, i10, t00, t11, t10));
	}

	/**
	 * Create an image to represent the implicit function.
	 * 
	 * @param implicitFunction
	 *            Function to be visualized.
	 * @param center
	 *            Center of the image in 3D space.
	 * @param size
	 *            Size of the sampled area, relative to the center.
	 */
	public void create(IImplicitFunction3D implicitFunction, Vector center,
			Vector dx, Vector dy, double size, double isoValue) {
		setMesh(center, size, dx, dy);
		setImage(implicitFunction, center, dx, dy, size, isoValue);
	}

	/**
	 * Set the required image values.
	 */
	private void setImage(IImplicitFunction3D implicitFunction,
			Vector center, Vector dx, Vector dy, double size,
			double isoValue) {
		Vector minMaxValues = getMinMaxValues(implicitFunction, center, dx,
				dy, size);

		// Create image
		int resolution = image.getWidth();
		for (int i = 0; i < resolution; i++) {
			double alpha = ((double) i / (double) (resolution - 1) - 0.5)
					* size;
			for (int j = 0; j < resolution; j++) {
				double beta = ((double) j / (double) (resolution - 1) - 0.5)
						* size;
				Vector x = center.add(dx.multiply(alpha)).add(
						dy.multiply(beta));
				double value = implicitFunction.f(x);
				image.setRGB(i, j, getColor(isoValue, value, minMaxValues));
			}
		}
		texture.setIsLoaded(false);
	}

	/**
	 * Stochastically determine the min/max values of the implicit function.
	 */
	private Vector getMinMaxValues(IImplicitFunction3D implicitFunction,
			Vector center, Vector dx, Vector dy, double size) {
		Vector minMax = VectorMatrixFactory.newVector(
				Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0);
		Vector upperLeft = center.subtract(dx.multiply(size * 0.5)).subtract(
				dy.multiply(size * 0.5));
		int NUMBER_OF_SAMPLES = 1000;
		for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
			double randomX = Math.random();
			double randomY = Math.random();
			Vector x = upperLeft.add(dx.multiply(size * randomX)).add(
					dy.multiply(size * randomY));
			double value = implicitFunction.f(x);
			if (value < minMax.get(0)) {
				minMax.set(0, value);
			}
			if (value > minMax.get(1)) {
				minMax.set(1, value);
			}
		}
		return minMax;
	}

	/**
	 * Return a color for the given value.
	 */
	private int getColor(double isoValue, double value, Vector minMaxValues) {
		if (value < isoValue) {
			double lambda = (value - minMaxValues.get(0))
					/ (isoValue - minMaxValues.get(0));
			lambda = Math.max(Math.min(1, lambda), 0);
			Vector col = Material.PALETTE2_COLOR3.multiply(lambda).add(
					Material.PALETTE2_COLOR4.multiply(1 - lambda));
			return new Color((float) col.get(0), (float) col.get(1),
					(float) col.get(2)).getRGB();
		} else {
			double lambda = (value - isoValue)
					/ (minMaxValues.get(1) - isoValue);
			lambda = Math.max(Math.min(1, lambda), 0);
			Vector col = Material.PALETTE2_COLOR2.multiply(1 - lambda).add(
					Material.PALETTE2_COLOR4.multiply(lambda));
			return new Color((float) col.get(0), (float) col.get(1),
					(float) col.get(2)).getRGB();
		}
	}

	/**
	 * Create the mesh to hold the texture image.
	 */
	private void setMesh(Vector center, double size, Vector dx, Vector dy) {

		double offset = 0.5 * size;

		Vector v00 = center.subtract(dx.multiply(offset)).subtract(
				dy.multiply(offset));
		Vector v01 = center.subtract(dx.multiply(offset)).add(
				dy.multiply(offset));
		Vector v10 = center.add(dx.multiply(offset)).subtract(
				dy.multiply(offset));
		Vector v11 = center.add(dx.multiply(offset)).add(dy.multiply(offset));

		mesh.getVertex(0).getPosition().copy(v00);
		mesh.getVertex(1).getPosition().copy(v01);
		mesh.getVertex(2).getPosition().copy(v10);
		mesh.getVertex(3).getPosition().copy(v11);
		mesh.getTextureCoordinate(0).copy(
				VectorMatrixFactory.newVector(0, 0, 0));
		mesh.getTextureCoordinate(1).copy(
				VectorMatrixFactory.newVector(0, 1, 0));
		mesh.getTextureCoordinate(2).copy(
				VectorMatrixFactory.newVector(1, 0, 0));
		mesh.getTextureCoordinate(3).copy(
				VectorMatrixFactory.newVector(1, 1, 0));
		mesh.computeTriangleNormals();
		mesh.computeVertexNormals();
		mesh.updateRenderStructures();
	}

	/**
	 * Return the created image.
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Return a texture object of the image.
	 */
	public CgTexture getTexture() {
		return texture;
	}

	/**
	 * Return a triangle mesh, which represents sampled area with texture
	 * coordinates.
	 */
	public ITriangleMesh getTriangleMesh() {
		return mesh;
	}

}

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.studentprojects.brickbuilder.Utilities;
import cgresearch.studentprojects.brickbuilder.math.ColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Transforms voxel clouds to trianglemeshes for rendering. 
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelCloudMesher {	
	/**
	 * Transforms a voxel cloud to a trianglemesh.
	 * @param cloud	source voxel cloud
	 * @return
	 */
	public static ITriangleMesh transformCloud2Mesh(IVoxelCloud cloud, boolean onlySurface) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		
		// gather colors for texture
		List<IColorRGB> colors = new ArrayList<IColorRGB>();
		colors.add(new ColorRGB(0, 0, 0)); // black (no color)
		colors.add(new ColorRGB(255, 255, 255)); // add white (interior)
		colors.add(new ColorRGB(255, 0, 0)); // red (shell)
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					VectorInt3 p = new VectorInt3(x, y, z);
					if (cloud.getVoxelAt(p) == VoxelType.SURFACE) {
						IColorRGB c = cloud.getVoxelColor(p);
						if (c != null && !colors.contains(c)) {
							colors.add(c);
						}
					}
				}
			}
		}
		
		// create texture and add it to the mesh
		String id = Utilities.createTextureFromColors(colors);
		mesh.getMaterial().setTextureId(id);
		BufferedImage tex = ResourceManager.getTextureManagerInstance().getResource(id).getTextureImage();
		
//		try {
//			ImageIO.write(tex, "PNG", new File("C:\\Users\\Teetasse\\Desktop\\a.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// loop through cloud
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					VectorInt3 p = new VectorInt3(x, y, z);
					if (cloud.getVoxelAt(p) != VoxelType.EXTERIOR) {
						// surface => get color
						if (cloud.getVoxelAt(p)  == VoxelType.SURFACE) {
							int c = colors.indexOf(cloud.getVoxelColor(p));		
							if (c < 0) c = 0;
							Utilities.addBox(mesh, cloud.getVoxelDimensions(), cloud.getVoxelLocation(p),
								(c % Utilities.MAXCOLORS), (c / Utilities.MAXCOLORS), 
								tex.getWidth(), tex.getHeight(), Utilities.CELLSIZE);	
						}
						else if (!onlySurface) {
							// shell => red (2,0)
							if (cloud.getVoxelAt(p)  == VoxelType.SHELL) {
								Utilities.addBox(mesh, cloud.getVoxelDimensions(), cloud.getVoxelLocation(p),
									2, 0, tex.getWidth(), tex.getHeight(), Utilities.CELLSIZE);	
							}
							// not surface => white (1,0)
							else {
								Utilities.addBox(mesh, cloud.getVoxelDimensions(), cloud.getVoxelLocation(p),
									1, 0, tex.getWidth(), tex.getHeight(), Utilities.CELLSIZE);	
							}
						}
					}
				}
			}
		}
		
		return mesh;
	}
	
	public static ITriangleMesh transformCloud2MeshRandomColor(IVoxelCloud cloud) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		String id = ResourceManager.getTextureManagerInstance().generateId();
		ResourceManager.getTextureManagerInstance().addResource(id, new CgTexture("textures\\colors40x40.png"));
		mesh.getMaterial().setTextureId(id);
		
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					VectorInt3 p = new VectorInt3(x, y, z);
						Utilities.addBox(mesh, cloud.getVoxelDimensions(), cloud.getVoxelLocation(p), 
								(int) (4 * Math.random()), (int) (3 * Math.random() + 1),
								40, 40, 10);	
				}
			}
		}
		
		return mesh;
	}
	
	/**
	 * Transforms only the specified voxels of the voxel cloud to a trianglemesh. 
	 * @param cloud		source voxel cloud
	 * @param type		voxel type to display
	 * @return
	 */
	public static ITriangleMesh transformCloud2Mesh(IVoxelCloud cloud, VoxelType type) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		String id = ResourceManager.getTextureManagerInstance().generateId();
		ResourceManager.getTextureManagerInstance().addResource(id, new CgTexture("textures\\colors40x40.png"));
		mesh.getMaterial().setTextureId(id);
		
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					VectorInt3 p = new VectorInt3(x, y, z);
					if (cloud.getVoxelAt(p) == type)
						Utilities.addBox(mesh, cloud.getVoxelDimensions(), cloud.getVoxelLocation(p), 
								(int) (4 * Math.random()), (int) (3 * Math.random() + 1),
								40, 40, 10);	
				}
			}
		}
		
		return mesh;
	}
}

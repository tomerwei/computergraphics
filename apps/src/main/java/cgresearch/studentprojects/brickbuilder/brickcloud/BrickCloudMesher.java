package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.datastructures.trianglemesh.TriangleMesh;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.studentprojects.brickbuilder.Utilities;
import cgresearch.studentprojects.brickbuilder.math.ColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;

public class BrickCloudMesher {
	
	public static ITriangleMesh transformCloud2MeshDebugComponents(IBrickCloud cloud) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		
		// texture
		String id = ResourceManager.getTextureManagerInstance().generateId();
		ResourceManager.getTextureManagerInstance().addResource(id, new CgTexture("textures\\colors40x40.png"));
		mesh.getMaterial().setTextureId(id);
		
		List<BrickInstance> bricks = cloud.getAllBricks();
		List<Set<String>> cc = cloud.getBrickGraphInspector().connectedSets();
		
		for (BrickInstance b : bricks) {
//			int colorX = (int) (4 * Math.random());
//			int colorY = (int) (3 * Math.random() + 1);
			int i = cc.indexOf(cloud.getBrickGraphInspector().connectedSetOf(b.getBrickIdString()));
			if (i > 15) i = 15;
			int colorX = i % 4;
			int colorY = i / 4;
			
			Utilities.addBox(mesh, cloud.getBrickDimensions(b), 
					cloud.getBrickPosition(b), colorX, colorY, 40, 40, 10);
		}
		
		return mesh;
	}
	
	public static ITriangleMesh transformCloud2MeshDebugWeakBricks(IBrickCloud cloud) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		
		// texture
		String id = ResourceManager.getTextureManagerInstance().generateId();
		ResourceManager.getTextureManagerInstance().addResource(id, new CgTexture("textures\\colors40x40.png"));
		mesh.getMaterial().setTextureId(id);
		
		List<BrickInstance> bricks = cloud.getAllBricks();
		List<BrickInstance> wb = cloud.getWeakArticulationPoints();
		
		for (BrickInstance b : bricks) {
			int colorX = 0;
			int colorY = wb.contains(b) ? 3 : 0;
			
			Utilities.addBox(mesh, cloud.getBrickDimensions(b), 
					cloud.getBrickPosition(b), colorX, colorY, 40, 40, 10);
		}
		
		return mesh;
	}
	
	public static ITriangleMesh transformCloud2Mesh(IBrickCloud cloud) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		
		// texture
		// gather colors for texture
		List<IColorRGB> colors = new ArrayList<IColorRGB>();
		colors.add(new ColorRGB(255, 255, 255)); // white (no color)
		for (IColorRGB c : cloud.getBrickSet().getBrickColors()) colors.add(c);
		
		// create texture and add it to the mesh
		String id = Utilities.createTextureFromColors(colors);
		mesh.getMaterial().setTextureId(id);
		BufferedImage tex = ResourceManager.getTextureManagerInstance().getResource(id).getTextureImage();
		
		List<BrickInstance> bricks = cloud.getAllBricks();	
		for (BrickInstance b : bricks) {			
			int c = colors.indexOf(b.getColor());		
			if (c < 0) c = 0; // no color color			
			Utilities.addBox(mesh, cloud.getBrickDimensions(b), 
					cloud.getBrickPosition(b), (c % Utilities.MAXCOLORS), (c / Utilities.MAXCOLORS), 
					tex.getWidth(), tex.getHeight(), Utilities.CELLSIZE);
		}
		
		return mesh;
	}
	
	public static ITriangleMesh transformCloud2Mesh(IBrickCloud cloud, int heightMin, int heightMax) {
		ITriangleMesh mesh = new TriangleMesh();		
		mesh.clear();
		
		// texture
		// gather colors for texture
		List<IColorRGB> colors = new ArrayList<IColorRGB>();
		colors.add(new ColorRGB(255, 255, 255)); // white (no color)
		for (IColorRGB c : cloud.getBrickSet().getBrickColors()) colors.add(c);
		
		// create texture and add it to the mesh
		String id = Utilities.createTextureFromColors(colors);
		mesh.getMaterial().setTextureId(id);
		BufferedImage tex = ResourceManager.getTextureManagerInstance().getResource(id).getTextureImage();
		
		
		// only inside the range
		List<BrickInstance> bricks = cloud.getAllBricks();
		for (BrickInstance b : bricks) {			
			if (b.getPos().getY() < heightMin || b.getPos().getY() > heightMax) continue;
				
			int c = colors.indexOf(b.getColor());		
			if (c < 0) c = 0; // no color color
			Utilities.addBox(mesh, cloud.getBrickDimensions(b), 
					cloud.getBrickPosition(b), (c % Utilities.MAXCOLORS), (c / Utilities.MAXCOLORS), 
					tex.getWidth(), tex.getHeight(), Utilities.CELLSIZE);
		}
		
		return mesh;
	}
}

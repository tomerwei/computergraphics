/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.fileio.ObjFileReader;
import cgresearch.studentprojects.brickbuilder.math.ColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Reads a brick cloud from a file.
 * See BricklCloudWriter for file format.
 * 
 * @author Chris Michael Marquardt
 */
public class BrickCloudReader {
	
	/**
	 * Reads a brick cloud from a file.
	 * @param file
	 * @return
	 */
	public static IBrickCloud readFromFile(String file) {
		IBrickCloud cloud = null;
		String path = ResourcesLocator.getInstance().getPathToResource(file);
		
		if (path == null) {
			Logger.getInstance().error("BRICK file "+file+" not found!");
			return null;
		}
		
        try {
        	DataInputStream reader = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
        	cloud = readFile(reader, path);
            reader.close();
        } catch (IOException e) {
            Logger.getInstance().exception("Failed to read BRICK file", e);
            return null;
        }
        
        Logger.getInstance().message("Successfully read BRICK file "+file+".");
        
        return cloud;
	}

	/**
	 * Reads the file and returns the new brick cloud.
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private static IBrickCloud readFile(DataInputStream reader, String path) throws IOException {
		IVector3 loc = null;
		IVectorInt3 res = null;
		int colors = 0;
		int brickTypes = 0;
		int bricks = 0;
		
		// read header
		StringBuilder s = new StringBuilder();
		while (!s.toString().startsWith("data")) {
			char c;
			s = new StringBuilder();
			while ((c = (char) reader.readByte()) != '\n') s.append(c);
			
			if (s.toString().startsWith("loc"))
				loc = readVector3(s.toString());
			else if (s.toString().startsWith("res")) {
				int[] v = readInt(s.toString());
				if (v.length != 3) continue;
				res = new VectorInt3(v[0], v[1], v[2]);
			}
			else if (s.toString().startsWith("bri")) {
				int[] v = readInt(s.toString());
				if (v.length != 1) continue;
				brickTypes = v[0];
			}	
			else if (s.toString().startsWith("col")) {
				int[] v = readInt(s.toString());
				if (v.length != 1) continue;
				colors = v[0];
			}	
			else if (s.toString().startsWith("cnt")) {
				int[] v = readInt(s.toString());
				if (v.length != 1) continue;
				bricks = v[0];
			}	
		}
		
		if (loc == null || res == null)
			throw new RuntimeException("loc or res is missing!");
		
		// try to read all meshes and save them
		Map<String, ITriangleMesh> meshes = new HashMap<String, ITriangleMesh>();
		ObjFileReader fread = new ObjFileReader();
		String dirName = (path.contains(File.separator) ? path.substring(0, path.lastIndexOf(File.separator)) : "");
		String fileName = (path.contains(File.separator) ? path.substring(path.lastIndexOf(File.separator)) : path);
		fileName = (fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName);
		
		String root = ResourcesLocator.getInstance().getPathToResource(dirName+fileName+"-rootmesh.obj");
		if (root != null) meshes.put("root", fread.readFile(root).get(0));
		
		for (int i = 0; i < brickTypes; i++) {
			String other = ResourcesLocator.getInstance().getPathToResource(dirName+fileName+"-child"+i+"mesh.obj");
			if (other != null) meshes.put(""+i, fread.readFile(other).get(0));
		}
		
		// read brick types
		IBrickSet brickSet = readBrickSet(reader, brickTypes, meshes);
		IBrickCloud cloud = new BrickCloud(loc, res, brickSet);
		readBrickColors(reader, cloud, colors);
		readBricks(reader, cloud, bricks);
		return cloud;
	}

	private static IVector3 readVector3(String s) {
		String[] data = s.trim().split("\\s+");
		if (data.length != 4) return null;
		double[] d = new double[3];
		try {
			for (int i = 0; i < 3; i++) d[i] = Double.parseDouble(data[i+1]);
		}
		catch (Exception e) {
			return null;
		}
		return VectorMatrixFactory.newIVector3(d[0], d[1], d[2]);
	}
	
	private static int[] readInt(String s) {
		String[] data = s.trim().split("\\s+");
		int[] r = new int[data.length-1];
		try {
			for (int i = 0; i < data.length-1; i++) r[i] = Integer.parseInt(data[i+1]);
		}
		catch (Exception e) {
			return new int[0];
		}
		return r;
	}
	
	private static IBrickSet readBrickSet(DataInputStream reader, int typeCount, Map<String, ITriangleMesh> meshes) throws IOException {		
		// read 1st brick type (root brick)
		int type = reader.readByte();
		if (type != 0) throw new RuntimeException("root brick is missing!");
		double dimX = reader.readDouble();
		double dimY = reader.readDouble();
		double dimZ = reader.readDouble();
		RootBrick rootBrick = new RootBrick(VectorMatrixFactory.newIVector3(dimX, dimY, dimZ), meshes.get("root"));
		IBrickSet brickSet = new BrickSet(rootBrick);
		
		// read other types
		List<IChildBrick> bricks = new ArrayList<IChildBrick>();
		Map<IBrick, List<Integer>> brickPrecessors = new HashMap<IBrick, List<Integer>>();
		
		for (int i = 0; i < typeCount; i++) {
			type = reader.readByte();
			IChildBrick brick = null;
			
			if (type == 1) {
				// Composed Brick
				int resX = reader.readByte();
				int resY = reader.readByte();
				int resZ = reader.readByte();
				brick = new ComposedBrick(rootBrick, new VectorInt3(resX, resY, resZ));
			}
			else {
				// Special Brick
				BrickType specialType = BrickType.values()[reader.readByte()];
				int count = reader.readShort();
				List<IVectorInt3> unitPos = new ArrayList<IVectorInt3>();			
				for (int j = 0; j < count; j++) {
					int posX = reader.readByte();
					int posY = reader.readByte();
					int posZ = reader.readByte();
					unitPos.add(new VectorInt3(posX, posY, posZ));
				}
				brick = new SpecialBrick(rootBrick, unitPos, meshes.get(""+i), specialType);
			}
			
			// get precessors
			int count = reader.readByte();			
			List<Integer> precessors = new ArrayList<Integer>();
			for (int j = 0; j < count; j++) precessors.add((int) reader.readByte());
			
			// add to our lists
			bricks.add(brick);
			brickPrecessors.put(brick,  precessors);
		}
		
		// add them actually
		for (IChildBrick brick : bricks) {
			IBrick[] precs = new IBrick[brickPrecessors.get(brick).size()];
			for (int j = 0; j < brickPrecessors.get(brick).size(); j++) {
				int index = brickPrecessors.get(brick).get(j);
				if (index == 0) precs[j] = rootBrick;
				else precs[j] = bricks.get(index - 1);
			}
			
			brickSet.addChildBrick(brick, precs);
		}
		
		return brickSet;
	}

	private static void readBrickColors(DataInputStream reader, IBrickCloud cloud, int colorCount) throws IOException {
		for (int i = 0; i < colorCount; i++) {
			byte[] c = new byte[3];
			reader.read(c);
			cloud.getBrickSet().addBrickColor(new ColorRGB(c));
		}
	}
	
	private static void readBricks(DataInputStream reader, IBrickCloud cloud, int brickCount) throws IOException {
		int max = Math.max(Math.max(cloud.getResolutions().getX(),
				cloud.getResolutions().getY()), cloud.getResolutions().getZ());
		int t = (max <= 255 ? 0 : max <= 65535 ? 1 : 2);
		
		for (int i = 0; i < brickCount; i++) {
			int type = reader.readInt();			
			IBrick brickType = null;
			if (type == 0) brickType = cloud.getBrickSet().getRootBrick();
			else brickType = cloud.getBrickSet().getChildBricks().get(type - 1);
			
			int x, y, z;
			if (t == 0) {
				x = reader.readByte();
				y = reader.readByte();
				z = reader.readByte();
			}
			else if (t == 1) {
				x = reader.readShort();
				y = reader.readShort();
				z = reader.readShort();
			}
			else {
				x = reader.readInt();
				y = reader.readInt();
				z = reader.readInt();
			}
			
			BrickRotation rot = BrickRotation.values()[reader.readByte()];
			int col = reader.readInt();			
			IColorRGB color = (col > -1 ? cloud.getBrickSet().getBrickColors().get(col) : null);
			
			cloud.addBrick(brickType, new VectorInt3(x, y, z), rot, color);
		}
	}
}

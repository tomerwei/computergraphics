/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.Vector;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.studentprojects.brickbuilder.math.ColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Reads a voxel cloud from a file.
 * See VoxelCloudWriter for file format.
 * 
 * @author Chris Michael Marquardt
 */
public class VoxelCloudReader {
	
	/**
	 * Reads a voxel cloud from a file.
	 * @param file
	 * @return
	 */
	public static IVoxelCloud readFromFile(String file) {
		DataInputStream reader = null;
		IVoxelCloud cloud = null;
		String path = ResourcesLocator.getInstance().getPathToResource(file);
		
		if (path == null) {
			Logger.getInstance().error("VOX file "+file+" not found!");
			return null;
		}
		
        try {
        	reader = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
        	cloud = readHeader(reader);
            readVoxel(reader, cloud);
            reader.close();
        } catch (IOException e) {
            Logger.getInstance().exception("Failed to read VOX file", e);
            return null;
        }
        
        Logger.getInstance().message("Successfully read VOX file "+file+".");
        return cloud;
	}

	/**
	 * Reads the header and returns the new voxel cloud.
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private static IVoxelCloud readHeader(DataInputStream reader) throws IOException {
		Vector loc = null;
		Vector dim = null;
		VectorInt3 res = null;
		int colors = 0;
		StringBuilder s = new StringBuilder();
		
		while (!s.toString().startsWith("data")) {
			char c;
			s = new StringBuilder();
			while ((c = (char) reader.readByte()) != '\n') s.append(c);
			
			if (s.toString().startsWith("loc"))
				loc = readVector(s.toString());
			else if (s.toString().startsWith("dim"))
				dim = readVector(s.toString());
			else if (s.toString().startsWith("res")) {
				int[] v = readInt(s.toString());
				if (v.length != 3) continue;
				res = new VectorInt3(v[0], v[1], v[2]);
			}
			else if (s.toString().startsWith("col")) {
				int[] v = readInt(s.toString());
				if (v.length != 1) continue;
				colors = v[0];
			}				
		}
		
		if (loc == null || dim == null || res == null)
			throw new RuntimeException("loc, dim or res is missing!");
		
		IVoxelCloud cloud = new VoxelCloud(loc, dim, res);		
		readColors(reader, cloud, colors);
		return cloud;
	}

	private static Vector readVector(String s) {
		String[] data = s.trim().split("\\s+");
		if (data.length != 4) return null;
		double[] d = new double[3];
		try {
			for (int i = 0; i < 3; i++) d[i] = Double.parseDouble(data[i+1]);
		}
		catch (Exception e) {
			return null;
		}
		return VectorMatrixFactory.newVector(d[0], d[1], d[2]);
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
	
	/**
	 * Reads the colors.
	 * @param reader
	 * @param cloud
	 * @param colors
	 * @throws IOException
	 */
	private static void readColors(DataInputStream reader, IVoxelCloud cloud, int colors) throws IOException {
		int max = Math.max(Math.max(cloud.getResolutions().getX(),
				cloud.getResolutions().getY()), cloud.getResolutions().getZ());
		int t = (max <= 255 ? 0 : max <= 65535 ? 1 : 2);
		
		for (int i = 0; i < colors; i++) {
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
			byte[] c = new byte[3];
			reader.read(c);
			cloud.setVoxelColor(new VectorInt3(x, y, z), new ColorRGB(c));
		}
	}
	
	/**
	 * Reads the voxel types.
	 * @param reader
	 * @param cloud
	 * @throws IOException
	 */
	private static void readVoxel(DataInputStream reader, IVoxelCloud cloud) throws IOException {
		int z = 0, y = 0, x = 0;
		int length = cloud.getResolutions().getX() * cloud.getResolutions().getY() * cloud.getResolutions().getZ();
		for (int i = 0; i < length; i++) {
			VoxelType type = VoxelType.values()[reader.readByte()];
			cloud.setVoxelAt(new VectorInt3(x, y, z), type);
				
			if (++x >= cloud.getResolutions().getX()) {
				x = 0;
				if (++y >= cloud.getResolutions().getY()) {
					y = 0;
					z++;
				}
			}
		}
	}
}

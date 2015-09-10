/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cgresearch.core.logging.Logger;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Writes a voxel cloud into a file.
 * 
 * Header (String):	
 * # A comment							Comments
 * loc x y z 							Lower left corner location (double)
 * dim x y z							Voxel dimensions (double)
 * res x y z							Resolution (int)
 * col number							Number of voxel with a color
 * 
 * Data (Binary):
 * data									Start of the data section
 * x1 y1 z1 c1 x2 y2 z2 c2 ...			Color of the voxel at x,y,z (byte/short/int + byte[3])
 * type1 type2 ...						Voxel type (byte) order: 000 .. 100 .. N00 .. 010 .. NN0 .. 001 .. NNN
 * 							
 * @author Chris Michael Marquardt
 */
public class VoxelCloudWriter {
	private VoxelCloudWriter() {}
	
	/**
	 * Writes a voxel cloud into the file.
	 * @param file
	 * @param cloud
	 */
	public static void writeToFile(String file, IVoxelCloud cloud) {
		DataOutputStream writer = null;
		
        try {
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            writeHeader(writer, cloud);
            writer.writeBytes("data\n");
            writeColors(writer, cloud);
            writeVoxel(writer, cloud);
            writer.close();
        } catch (IOException e) {
            Logger.getInstance().exception("Failed to write VOX file", e);
            return;
        }
        
        Logger.getInstance().message("Successfully wrote VOX file "+file+".");
	}

	/**
	 * Writes the header.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeHeader(DataOutputStream writer, IVoxelCloud cloud) throws IOException {
		 writer.writeBytes("# VOX file writer\n");
		 writer.writeBytes("loc "+cloud.getLocationLowerLeft().get(0)+" "+
				 cloud.getLocationLowerLeft().get(1)+" "+
				 cloud.getLocationLowerLeft().get(2)+"\n");
		 writer.writeBytes("dim "+cloud.getVoxelDimensions().get(0)+" "+
				 cloud.getVoxelDimensions().get(1)+" "+
				 cloud.getVoxelDimensions().get(2)+"\n");
		 writer.writeBytes("res "+cloud.getResolutions().getX()+" "+
				 cloud.getResolutions().getY()+" "+
				 cloud.getResolutions().getZ()+"\n");
		 writer.writeBytes("col "+cloud.getNumberOfVoxelWithColor()+"\n");
	}
	
	/**
	 * Writes the colors.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeColors(DataOutputStream writer, IVoxelCloud cloud) throws IOException {
		int max = Math.max(Math.max(cloud.getResolutions().getX(),
				cloud.getResolutions().getY()), cloud.getResolutions().getZ());
		int t = (max <= 255 ? 0 : max <= 65535 ? 1 : 2);
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					IColorRGB c = cloud.getVoxelColor(new VectorInt3(x, y, z));
					if (c != null) {
						if (t == 0) {
							writer.writeByte(x);
							writer.writeByte(y);
							writer.writeByte(z);
						}
						else if (t == 1) {
							writer.writeShort(x);
							writer.writeShort(y);
							writer.writeShort(z);
						}
						else {
							writer.writeInt(x);
							writer.writeInt(y);
							writer.writeInt(z);
						}
						writer.write(c.getAsByteArray());
					}
				}
			}
		}
	}
	
	/**
	 * Writes the voxels types.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeVoxel(DataOutputStream writer, IVoxelCloud cloud) throws IOException {
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y <  cloud.getResolutions().getY(); y++) {
				for (int x = 0; x <  cloud.getResolutions().getX(); x++) {
					writer.writeByte(cloud.getVoxelAt(new VectorInt3(x, y, z)).ordinal());
				}
			}
		}
	}
}

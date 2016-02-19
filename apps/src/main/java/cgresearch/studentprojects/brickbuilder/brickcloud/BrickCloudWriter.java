/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;
import cgresearch.graphics.fileio.ObjFileWriter;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Writes a brick cloud (+ brick set) into a file.
 * Additional the brick models (if not null) will be saved by ObjFileWriter.
 * 
 * Header (String):	
 * # A comment							Comments
 * loc x y z 							Lower left corner location (double)
 * res x y z							Resolution (int)
 * bri number							Number of brick types in the set (without root)
 * col number							Number of brick colors in the set
 * cnt number							Number of bricks in the cloud
 * 
 * Data (Binary):
 * data									Start of the data section
 * type ...								Brick types (byte) (1st needs to be the root brick)
 * 		dimX dimY dimZ ...				when a root brick (3x double)
 * 		resX resY resZ ...				when a composed brick (3x byte)
 * 		specialType unitCount unitX1 unitY1 unitZ1 ... when a special brick (byte short 3x byte)
 * color1 color2 ...					Brick type colors (int)
 * type posX posY posZ rot col ... 		Bricks (int 3x byte/short/int byte int)
 * 							
 * @author Chris Michael Marquardt
 */
public class BrickCloudWriter {
	private BrickCloudWriter() {}
	
	/**
	 * Writes a brick cloud into the file.
	 * @param file
	 * @param cloud
	 */
	public static void writeToFile(String path, String file, IBrickCloud cloud) {
		// brick file
        try {
        	DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path+file)));
            writeHeader(writer, cloud);
            writer.writeBytes("data\n");
            writeTypes(writer, cloud);
            writeColors(writer, cloud);
            writeBricks(writer, cloud);
            writer.close();
        } catch (IOException e) {
            Logger.getInstance().exception("Failed to write BRICK file", e);
            return;
        }        
        Logger.getInstance().message("Successfully wrote BRICK file "+file+".");
        
        // meshes
        ObjFileWriter fw = new ObjFileWriter();
        String fileName = (file.contains(".") ? file.substring(0, file.lastIndexOf(".")) : file);
        
        if (cloud.getBrickSet().getRootBrick().getModel() != null)     	
        	fw.writeToFile(path+fileName+"-rootmesh.obj", cloud.getBrickSet().getRootBrick().getModel());
        for (int i = 0; i < cloud.getBrickSet().getChildBricks().size(); i++) {
        	IChildBrick brick = cloud.getBrickSet().getChildBricks().get(i);
        	if (brick.getModel() != null)
        		fw.writeToFile(path+fileName+"-child"+i+"mesh.obj", brick.getModel());
        }
	}

	/**
	 * Writes the header.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeHeader(DataOutputStream writer, IBrickCloud cloud) throws IOException {
		 writer.writeBytes("# BRICK file writer\n");
		 writer.writeBytes("loc "+cloud.getLocationLowerLeft().get(0)+" "+
				 cloud.getLocationLowerLeft().get(1)+" "+
				 cloud.getLocationLowerLeft().get(2)+"\n");
		 writer.writeBytes("res "+cloud.getResolutions().getX()+" "+
				 cloud.getResolutions().getY()+" "+
				 cloud.getResolutions().getZ()+"\n");
		 writer.writeBytes("bri "+cloud.getBrickSet().getChildBricks().size()+"\n");
		 writer.writeBytes("col "+cloud.getBrickSet().getBrickColors().size()+"\n");
		 writer.writeBytes("cnt "+cloud.getNumberOfBricks()+"\n");
	}
	
	/**
	 * Writes the brick type colors.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeColors(DataOutputStream writer, IBrickCloud cloud) throws IOException {
		for (IColorRGB col : cloud.getBrickSet().getBrickColors()) {
			writer.write(col.getAsByteArray());
		}
	}
	
	/**
	 * Writes the brick types.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeTypes(DataOutputStream writer, IBrickCloud cloud) throws IOException {
		writeTypeRoot(writer, ((RootBrick) cloud.getBrickSet().getRootBrick()));
		for (IChildBrick child : cloud.getBrickSet().getChildBricks()) {
			if (child instanceof ComposedBrick) {
				writeTypeComposed(writer, ((ComposedBrick) child));
			}
			else {
				writeTypeSpecial(writer, ((SpecialBrick) child));
			}
			writeBrickPrecessors(writer, cloud, child);
		}
	}

	private static void writeTypeRoot(DataOutputStream writer, RootBrick brick) throws IOException {
		writer.writeByte(BrickType.ROOT.ordinal());
		writer.writeDouble(brick.getDimensions().get(0));
		writer.writeDouble(brick.getDimensions().get(1));
		writer.writeDouble(brick.getDimensions().get(2));
	}
	
	private static void writeTypeComposed(DataOutputStream writer, ComposedBrick brick) throws IOException {
		writer.writeByte(BrickType.COMPOSED.ordinal());
		writer.writeByte(brick.getResolution().getX());
		writer.writeByte(brick.getResolution().getY());
		writer.writeByte(brick.getResolution().getZ());
	}
	
	private static void writeTypeSpecial(DataOutputStream writer, SpecialBrick brick) throws IOException {
		writer.writeByte(BrickType.SPECIAL.ordinal());
		writer.writeByte(brick.getBrickType().ordinal());
		writer.writeShort(brick.getUnitPositions().size());
		for (VectorInt3 pos : brick.getUnitPositions()) {
			writer.writeByte(pos.getX());
			writer.writeByte(pos.getY());
			writer.writeByte(pos.getZ());
		}
	}
	
	private static void writeBrickPrecessors(DataOutputStream writer, IBrickCloud cloud, IChildBrick brick) throws IOException {
		// get precessors
		List<IBrick> precessors = new ArrayList<IBrick>();
		for (IChildBrick b : cloud.getBrickSet().getNextBricks(cloud.getBrickSet().getRootBrick())) {
			if (b == brick) precessors.add(cloud.getBrickSet().getRootBrick());
		}
		for (IChildBrick b : cloud.getBrickSet().getChildBricks()) {
			if (b == brick) continue;
			for (IChildBrick bb : cloud.getBrickSet().getNextBricks(b)) {
				if (bb == brick) precessors.add(b);
			}
		}
		
		// write size and precessors ids
		writer.writeByte(precessors.size());
		for (IBrick b : precessors) {
			writer.writeByte(cloud.getBrickSet().getChildBricks().indexOf(b) + 1);
		}
	}
	
	/**
	 * Writes the bricks.
	 * @param writer
	 * @param cloud
	 * @throws IOException
	 */
	private static void writeBricks(DataOutputStream writer, IBrickCloud cloud) throws IOException {
		int max = Math.max(Math.max(cloud.getResolutions().getX(),
				cloud.getResolutions().getY()), cloud.getResolutions().getZ());
		int t = (max <= 255 ? 0 : max <= 65535 ? 1 : 2);

		for (BrickInstance brick : cloud.getAllBricks()) {
			writer.writeInt(getBrickNumber(cloud, brick));
			if (t == 0) {
				writer.writeByte(brick.getPos().getX());
				writer.writeByte(brick.getPos().getY());
				writer.writeByte(brick.getPos().getZ());
			}
			else if (t == 1) {
				writer.writeShort(brick.getPos().getX());
				writer.writeShort(brick.getPos().getY());
				writer.writeShort(brick.getPos().getZ());
			}
			else {
				writer.writeInt(brick.getPos().getX());
				writer.writeInt(brick.getPos().getY());
				writer.writeInt(brick.getPos().getZ());
			}
			writer.writeByte(brick.getRotation().ordinal());
			writer.writeInt(getBrickColor(cloud, brick));
		}
	}
	
	private static int getBrickNumber(IBrickCloud cloud, BrickInstance brick) {
		return cloud.getBrickSet().getChildBricks().indexOf(brick.getBrick()) + 1;
	}
	
	private static int getBrickColor(IBrickCloud cloud, BrickInstance brick) {
		return cloud.getBrickSet().getBrickColors().indexOf(brick.getColor());
	}
}

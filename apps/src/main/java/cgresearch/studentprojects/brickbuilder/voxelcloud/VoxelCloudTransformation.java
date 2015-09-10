/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.voxelcloud;

import java.util.ArrayList;
import java.util.List;

import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

public class VoxelCloudTransformation {
	
	/**
	 * Returns a new voxel cloud with marked surface voxels.
	 * @param src	source voxel cloud
	 * @return
	 */
	public static IVoxelCloud markSurfaceVoxels(IVoxelCloud src) {
		IVoxelCloud dest = new VoxelCloud((VoxelCloud) src);	
		markSurface(dest, src, 0, -1);
		markSurface(dest, src, 1, -1);
		markSurface(dest, src, 2, -1);
		return dest;
	}
	
	/**
	 * Return a new voxel cloud with marked surface and shell voxels.
	 * (A surface voxel is as shell voxel.)
	 * @param src			source voxel cloud
	 * @param shellSize		size of the shell (<2 means no shell)
	 * @return
	 */
	public static IVoxelCloud markSurfaceVoxels(IVoxelCloud src, int shellSize) {
		IVoxelCloud dest = new VoxelCloud((VoxelCloud) src);	
		markSurface(dest, src, 0, shellSize);
		markSurface(dest, src, 1, shellSize);
		markSurface(dest, src, 2, shellSize);
		return dest;		
	}
	
	/**
	 * Marks voxel as surface and shell for the given axiz.
	 * @param dest			destination voxel cloud
	 * @param src			source voxel cloud
	 * @param axiz			axiz (0 = X, 1 = Y, 2 = Z)
	 * @param shellSize		size of the shell (<2 means no shell)
	 */
	private static void markSurface(IVoxelCloud dest, IVoxelCloud src, int axiz, int shellSize) {
		// calc other axiz
		int a = (axiz == 0 ? 1 : 0);
		int b = (axiz == 2 ? 1 : 2);
		int[] pos = new int[3];		
		// loop through other axiz
		for (pos[a] = 0; pos[a] < src.getResolutions().get()[a]; pos[a]++) {
			for (pos[b] = 0; pos[b] < src.getResolutions().get()[b]; pos[b]++) {
				// go through axiz
				VoxelType last = VoxelType.EXTERIOR;
				for (pos[axiz] = 0; pos[axiz] < src.getResolutions().get()[axiz]; pos[axiz]++) {
					IVectorInt3 v = new VectorInt3(pos[0], pos[1], pos[2]);
					VoxelType t = src.getVoxelAt(v);
					// if type changed
					if (last != t) {
						// interior => surface
						if (t == VoxelType.INTERIOR) {
							dest.setVoxelAt(v, VoxelType.SURFACE);
							if (shellSize > 1) markShell(dest, src, axiz, pos, shellSize, true);
						}
						// exterior => last one is surface
						else if (t == VoxelType.EXTERIOR) {
							IVectorInt3 vPrev = new VectorInt3(pos[0] - (axiz == 0 ? 1 : 0),
									pos[1] - (axiz == 1 ? 1 : 0),
									pos[2] - (axiz == 2 ? 1 : 0));
							dest.setVoxelAt(vPrev, VoxelType.SURFACE);
							if (shellSize > 1) 
								markShell(dest, src, axiz, new int[] {vPrev.getX(), vPrev.getY(), vPrev.getZ()},
										shellSize, false);
						}
						last = t;
					}
					
					// special last of axiz treatment
					if (pos[axiz] == src.getResolutions().get()[axiz]-1 &&
							t == VoxelType.INTERIOR) {
						// if last of axiz is a interior voxel it definitly is a surface voxel
						dest.setVoxelAt(v, VoxelType.SURFACE);
						if (shellSize > 1) markShell(dest, src, axiz, pos, shellSize, true);
					}
				}
			}
		}
	}
	
	/**
	 * Marks from a given position all shell voxels if the fit the condition.
	 * @param dest			destination voxel cloud
	 * @param src			source voxel cloud
	 * @param axiz			axiz (0 = X, 1 = Y, 2 = Z)
	 * @param pos			position of the start voxel
	 * @param shellSize		size of the shell (<2 means no shell)
	 * @param forward		moving along positive axiz (or negative)
	 */
	private static void markShell(IVoxelCloud dest, IVoxelCloud src, int axiz, int[] pos, int shellSize, boolean forward) {
		for (int i = 1; i < shellSize; i++) {
			VectorInt3 vNext = new VectorInt3(pos[0] + (axiz == 0 ? i : 0) * (forward ? 1 : -1),
					pos[1] + (axiz == 1 ? i : 0) * (forward ? 1 : -1),
					pos[2] + (axiz == 2 ? i : 0) * (forward ? 1 : -1));
			
			List<VoxelType> list = getNeighbors(src, new int[] {vNext.getX(), vNext.getY(), vNext.getZ()});
			boolean interior = true;			
			for (VoxelType v : list) if (v == VoxelType.EXTERIOR) interior = false;
			if (interior) dest.setVoxelAt(vNext, VoxelType.SHELL);
		}
	}
	
	/**
	 * Returns a list with all voxel types around a given voxel.
	 * @param src	source voxel cloud
	 * @param pos	voxel position
	 * @return
	 */
	private static List<VoxelType> getNeighbors(IVoxelCloud src, int[] pos) {
		List<VoxelType> list = new ArrayList<VoxelType>();
		VoxelType t = src.getVoxelAt(new VectorInt3(pos[0]+1, pos[1], pos[2]));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);	// out of bounds => exterior
		t = src.getVoxelAt(new VectorInt3(pos[0]-1, pos[1], pos[2]));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);
		t = src.getVoxelAt(new VectorInt3(pos[0], pos[1]+1, pos[2]));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);
		t = src.getVoxelAt(new VectorInt3(pos[0], pos[1]-1, pos[2]));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);
		t = src.getVoxelAt(new VectorInt3(pos[0], pos[1], pos[2]+1));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);
		t = src.getVoxelAt(new VectorInt3(pos[0], pos[1], pos[2]-1));
		if (t != null) list.add(t);
		else list.add(VoxelType.EXTERIOR);
		return list;
	}
	
	/**
	 * Removes unnecessary colors of a cloud.
	 * @param cloud
	 */
	public static void removeUnnecessaryColors(IVoxelCloud cloud) {
		for (int z = 0; z < cloud.getResolutions().getZ(); z++) {
			for (int y = 0; y < cloud.getResolutions().getY(); y++) {
				for (int x = 0; x < cloud.getResolutions().getX(); x++) {
					IVectorInt3 v = new VectorInt3(x, y, z);
					if (cloud.getVoxelAt(v) != VoxelType.SURFACE)
						cloud.clearVoxelColor(v);
				}
			}
		}
	}
}

/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Instance of a brick at a specific position in a brick cloud.
 */
public class BrickInstance {	
	public static long id = 0;
	public static Map<Long, BrickInstance> brickMap = new HashMap<Long, BrickInstance>();
	
	private IBrick brick;
	private IVectorInt3 pos;
	private IColorRGB color;
	private BrickRotation rotation;
	private List<IVectorInt3> units;
	private long brickId;
	
	public BrickInstance(IBrick brick, IVectorInt3 pos, BrickRotation rot) {
		this(brick, pos, rot, null);
	}
	
	public BrickInstance(IBrick brick, IVectorInt3 pos, BrickRotation rot, IColorRGB color) {
		this.brickId = id;
		brickMap.put(id++, this);
		
		this.brick = brick;
		this.pos = pos;
		this.rotation = rot;
		this.color = color;
		this.units = null;
	}

	public long getBrickId() {
		return brickId;
	}
	
	public String getBrickIdString() {
		return ""+brickId;
	}
	
	public IBrick getBrick() {
		return brick;
	}

	public IVectorInt3 getPos() {
		return pos;
	}
	
	public IColorRGB getColor() {
		return color;
	}

	public BrickRotation getRotation() {
		return rotation;
	}
	
	@Override
	public String toString() {
		return ""+brickId;
//		return brick.getBrickType().name()+"/"+pos;
	}
	
	public List<IVectorInt3> getBrickUnitPositions() {
		if (units == null) units = getBrickUnitPositions(brick, pos, rotation);
		return units;
	}
	
	/**
	 * Get every unit positions for this brick a the given position + rotation.
	 * @param brick
	 * @param pos
	 * @param rot
	 * @return
	 */
	public static List<IVectorInt3> getBrickUnitPositions(IBrick brick, IVectorInt3 pos, BrickRotation rot) {
		List<IVectorInt3> units = new ArrayList<IVectorInt3>();
		for (IVectorInt3 v : brick.getUnitPositions()) {
			switch (rot) {
				case XDIR_POS: 
					units.add(pos.add(v));
					break;					
				case XDIR_NEG:
					units.add(new VectorInt3(
							pos.getX() - v.getX(), pos.getY() + v.getY(), pos.getZ() + v.getZ()));
					break;
				case ZDIR_POS:
					units.add(new VectorInt3(
							pos.getX() + v.getZ(), pos.getY() + v.getY(), pos.getZ() + v.getX()));
					break;
				case ZDIR_NEG:
					units.add(new VectorInt3(
							pos.getX() - v.getZ(), pos.getY() + v.getY(), pos.getZ() + v.getX()));
					break;
			}
		}		
		return units;
		
	}
}

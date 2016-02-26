/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.List;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.Vector;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickRotation;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Interface for a brick cloud which holds a graphical view of the brick connections.
 * 
 * @author Chris Michael Marquardt
 */
public interface IBrickCloud {
	/**
	 * Get the bounding box of the cloud.
	 * @return
	 */
	public BoundingBox getBoundingBox();
	
	/**
	 * Get the dimensions of the brick cloud.
	 * @return
	 */
	public Vector getDimensions();
	
	/**
	 * Get the location of the lower left corner of the brick cloud.
	 * @return
	 */
	public Vector getLocationLowerLeft();
	
	/**
	 * Get the resolution of every axis of the brick cloud.
	 * @return
	 */
	public VectorInt3 getResolutions();	
	
	/**
	 * Get the brick set associated with this cloud.
	 * @return
	 */
	public IBrickSet getBrickSet();
	
	/**
	 * Get the number of connected components.
	 * @return
	 */
	public int getNumberOfConnectedComponents();
	
	/**
	 * Get the number of weak points.
	 * A weak point is a brick which connects two subgraphs (of a size > 1) alone.
	 * @return
	 */
	public int getNumberOfWeakPoints();
	
	/**
	 * Get the brick instance at the given position.
	 * @param pos
	 * @return
	 */
	public BrickInstance getBrickAt(VectorInt3 pos);
	
	/**
	 * Adds a brick at the given position.
	 * @param brick
	 * @param pos
	 * @param rot
	 */
	public void addBrick(IBrick brick, VectorInt3 pos, BrickRotation rot);
	
	/**
	 * Adds a brick at the given position with a color info.
	 * @param brick
	 * @param pos
	 * @param rot
	 * @param color
	 */
	public void addBrick(IBrick brick, VectorInt3 pos, BrickRotation rot, IColorRGB color);
	
	/**
	 * Removes a brick at the given position.
	 * @param pos
	 */
	public void removeBrick(VectorInt3 pos);
	
	/**
	 * Removes a brick at the given position and adds the root brick instead.
	 * @param pos
	 */
	public void removeBrickAndFill(VectorInt3 pos);
	
	/**
	 * Could the brick be placed at the given position 
	 * (removing other bricks but not splitting them)?
	 * @param brick
	 * @param pos
	 * @param rot
	 */
	public boolean canBrickReplaceBricksAt(IBrick brick, VectorInt3 pos, BrickRotation rot);
	public boolean canBrickReplaceBricksAt(IBrick brick, VectorInt3 pos, BrickRotation rot, IColorRGB col);
	
	/**
	 * Get the count of connections made by this brick to other bricks (over/under it)
	 * @param brick
	 * @param pos
	 * @param rot
	 * @return
	 */
	public int getBrickConnections(IBrick brick, VectorInt3 pos, BrickRotation rot);
	
	/**
	 * Returns a random brick at the given height.
	 * @param height
	 * @return
	 */
	public BrickInstance getRandomBrick(int height);
	
	/**
	 * Returns the number of bricks at the given height.
	 * @param height
	 * @return
	 */
	public int getNumberOfBricks(int height);
	
	/**
	 * Returns the number of all bricks.
	 * @return
	 */
	public int getNumberOfBricks();
	
	/**
	 * Returns all brick instances.
	 * @return
	 */
	public List<BrickInstance> getAllBricks();
	
	/**
	 * Get the brick mid position.
	 * @param brick
	 * @return
	 */
	public Vector getBrickPosition(BrickInstance brick);
	
	/**
	 * Get the brick dimensions.
	 * @param brick
	 * @return
	 */
	public Vector getBrickDimensions(BrickInstance brick);
	
	/**
	 * Get the graph representing the brick connections.
	 * @return
	 */
	public ListenableUndirectedGraph<String, DefaultEdge> getBrickGraph();
	
	/**
	 * Get a list with all neighbours of a brick.
	 * @return
	 */
	public List<BrickInstance> getBrickNeighbors(BrickInstance brick);
	
	/**
	 * Get a string representation of the given layer.
	 * @param height
	 * @return
	 */
	public String showLayer(int height);
	
	/**
	 * Get the connectivity inspector of the brick graph.
	 * @return
	 */
	public ConnectivityInspector<String, DefaultEdge> getBrickGraphInspector();
	
	public List<BrickInstance> getWeakArticulationPoints();
}

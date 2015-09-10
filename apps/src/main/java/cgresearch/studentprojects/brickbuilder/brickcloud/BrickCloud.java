/**
 * Chris Michael Marquardt
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * Bachelorarbeit 2014
 */
package cgresearch.studentprojects.brickbuilder.brickcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.BlockCutpointGraph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

import cgresearch.core.math.BoundingBox;
import cgresearch.core.math.IVector3;
import cgresearch.core.math.MathHelpers;
import cgresearch.core.math.VectorMatrixFactory;
import cgresearch.studentprojects.brickbuilder.brickcloud.BrickRotation;
import cgresearch.studentprojects.brickbuilder.math.IColorRGB;
import cgresearch.studentprojects.brickbuilder.math.IVectorInt3;
import cgresearch.studentprojects.brickbuilder.math.VectorInt3;

/**
 * Implementation of the brick cloud interface.
 * 
 * @author Chris Michael Marquardt
 */
public class BrickCloud implements IBrickCloud {
	/**
	 * Location of the cloud (lower left corner).
	 */
	private IVector3 locationLowerLeft;
	/**
	 * Resolution of the cloud in every 3 axis.
	 */
	private IVectorInt3 resolution;
	/**
	 * Bounding box of the brick cloud.
	 */
	private BoundingBox boundingBox;
	/**
	 * Used brick set the cloud is made of.
	 */
	private IBrickSet brickSet;
	/**
	 * Graph of the brick connections.
	 */
	private ListenableUndirectedGraph<String, DefaultEdge> brickGraph; 
	/**
	 * Inspector of the brick graph.
	 */
	private ConnectivityInspector<String, DefaultEdge> brickGraphInspector;
	/**
	 * List of all bricks mapped to the height.
	 */
	private Map<Integer, List<BrickInstance>> brickList;
	/**
	 * Array of the cloud containing the bricks.
	 */
	private BrickInstance[][][] brickArray;
	
	/**
	 * Constructor
	 * @param locationLowerLeft
	 * @param resolution
	 * @param brickSet
	 */
	public BrickCloud(IVector3 locationLowerLeft, IVectorInt3 resolution, IBrickSet brickSet) {
		this.locationLowerLeft = VectorMatrixFactory.newIVector3(locationLowerLeft);
		this.resolution = resolution;
		this.brickSet = brickSet;
		
		this.boundingBox = new BoundingBox(this.locationLowerLeft,
				this.locationLowerLeft.add(VectorMatrixFactory.newIVector3(
						brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_0) * this.resolution.getX(), 
						brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_1) * this.resolution.getY(),
						brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_2) * this.resolution.getZ()
						)));
		this.brickGraph = new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		this.brickGraphInspector = new ConnectivityInspector<String, DefaultEdge>(this.brickGraph);
		this.brickGraph.addGraphListener(this.brickGraphInspector);
		this.brickList = new HashMap<Integer, List<BrickInstance>>();
		for (int y = 0; y < resolution.getY(); y++) this.brickList.put(y, new ArrayList<BrickInstance>());
		this.brickArray = new BrickInstance[resolution.getZ()][resolution.getY()][resolution.getX()];
	}
	
	public BrickCloud(BrickCloud cloud) {
		this.locationLowerLeft = cloud.locationLowerLeft;
		this.resolution = cloud.resolution;
		this.brickSet = cloud.brickSet;		
		this.boundingBox = cloud.boundingBox;
		
		this.brickGraph = new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		this.brickGraphInspector = new ConnectivityInspector<String, DefaultEdge>(this.brickGraph);
		this.brickGraph.addGraphListener(this.brickGraphInspector);
		Graphs.addGraph(this.brickGraph, cloud.brickGraph);
		
		this.brickList = new HashMap<Integer, List<BrickInstance>>();
		for (int y = 0; y < resolution.getY(); y++) {
			this.brickList.put(y, new ArrayList<BrickInstance>(cloud.brickList.get(y)));
		}
		this.brickArray = new BrickInstance[resolution.getZ()][resolution.getY()][resolution.getX()];
		for (int x = 0; x < resolution.getX(); x++)
			for (int y = 0; y < resolution.getY(); y++)
				for (int z = 0; z < resolution.getZ(); z++)
					this.brickArray[z][y][x] = cloud.brickArray[z][y][x];
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public IVector3 getDimensions() {
		return VectorMatrixFactory.newIVector3(
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_0) * resolution.getX(),
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_1) * resolution.getY(),
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_2) * resolution.getZ());
	}

	@Override
	public IVector3 getLocationLowerLeft() {
		return VectorMatrixFactory.newIVector3(locationLowerLeft);
	}

	@Override
	public IVectorInt3 getResolutions() {
		return resolution;
	}

	@Override
	public IBrickSet getBrickSet() {
		return brickSet;
	}

	@Override
	public int getNumberOfConnectedComponents() {
		return brickGraphInspector.connectedSets().size();
	}

	@Override
	public int getNumberOfWeakPoints() {
		return getWeakArticulationPoints().size();
	}

	@Override
	public BrickInstance getBrickAt(IVectorInt3 pos) {
		return brickArray[pos.getZ()][pos.getY()][pos.getX()];
	}
	
	@Override
	public void addBrick(IBrick brick, IVectorInt3 pos, BrickRotation rot) {
		addBrick(brick, pos, rot, null);
	}

	@Override
	public void addBrick(IBrick brick, IVectorInt3 pos, BrickRotation rot, IColorRGB color) {
		IColorRGB col = (color != null ? color.getNearestColor(brickSet.getBrickColors()) : null);
		BrickInstance b = new BrickInstance(brick, pos, rot, col);
		brickList.get(pos.getY()).add(b);
		brickGraph.addVertex(b.getBrickIdString());
		
		for (IVectorInt3 v : b.getBrickUnitPositions()) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();
			
			// delete old brick
			BrickInstance old = brickArray[posZ][posY][posX];
			if (old != null) removeBrick(old.getPos());			
			
			brickArray[posZ][posY][posX] = b;
					
			if (posY-1 > -1 && brickArray[posZ][posY-1][posX] != null)
				brickGraph.addEdge(b.getBrickIdString(), brickArray[posZ][posY-1][posX].getBrickIdString());
			if (posY+1 < resolution.getY() && brickArray[posZ][posY+1][posX] != null)
				brickGraph.addEdge(b.getBrickIdString(), brickArray[posZ][posY+1][posX].getBrickIdString());
		}
	}
	
	@Override
	public void removeBrick(IVectorInt3 pos) {
		BrickInstance b = brickArray[pos.getZ()][pos.getY()][pos.getX()];
		if (b == null) return;
		
		brickList.get(pos.getY()).remove(b);
		brickGraph.removeVertex(b.getBrickIdString());
		
		for (IVectorInt3 v : b.getBrickUnitPositions()) {
			brickArray[v.getZ()][v.getY()][v.getX()] = null;
		}
	}

	@Override
	public void removeBrickAndFill(IVectorInt3 pos) {
		BrickInstance b = brickArray[pos.getZ()][pos.getY()][pos.getX()];
		if (b == null) return;
		
		brickList.get(pos.getY()).remove(b);
		brickGraph.removeVertex(b.getBrickIdString());
		
		for (IVectorInt3 v : b.getBrickUnitPositions()) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();
					
			BrickInstance r = new BrickInstance(brickSet.getRootBrick(),
					new VectorInt3(posX, posY, posZ), BrickRotation.XDIR_POS, b.getColor());				
			brickArray[posZ][posY][posX] = r;
			brickList.get(pos.getY()).add(r);
			brickGraph.addVertex(r.getBrickIdString());
					
			if (posY-1 > -1 && brickArray[posZ][posY-1][posX] != null)
				brickGraph.addEdge(r.getBrickIdString(), brickArray[posZ][posY-1][posX].getBrickIdString());
			if (posY+1 < resolution.getY() && brickArray[posZ][posY+1][posX] != null)
				brickGraph.addEdge(r.getBrickIdString(), brickArray[posZ][posY+1][posX].getBrickIdString());
		}
	}

	@Override
	public boolean canBrickReplaceBricksAt(IBrick brick, IVectorInt3 pos, BrickRotation rot) {		
		List<IVectorInt3> unitList = BrickInstance.getBrickUnitPositions(brick, pos, rot);
		// loop though each unit
		for (IVectorInt3 v : unitList) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();				
//			System.out.println(posX+"/"+posY+"/"+posZ);			
			// inside cloud?
			if (posX < 0 || posX >= resolution.getX()) return false;
			if (posY < 0 || posY >= resolution.getY()) return false;
			if (posZ < 0 || posZ >= resolution.getZ()) return false;		
			// get brick at the space
			BrickInstance b = brickArray[posZ][posY][posX];	
			// is there a brick?
			if (b == null) return false;
			// same brick?
			if (brick == b.getBrick()) return false;
			// no special brick?
			if (b.getBrick() instanceof SpecialBrick) return false;
			// is brick "inside" of given brick?
			List<IVectorInt3> units = b.getBrickUnitPositions();
			for (IVectorInt3 u : units) if (!unitList.contains(u)) return false;
		}
		
		return true;
	}
	
	public boolean canBrickReplaceBricksAt(IBrick brick, IVectorInt3 pos, BrickRotation rot, IColorRGB col) {		
		List<IVectorInt3> unitList = BrickInstance.getBrickUnitPositions(brick, pos, rot);
		IColorRGB color = col;
		
		// loop though each unit
		for (IVectorInt3 v : unitList) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();				
//			System.out.println(posX+"/"+posY+"/"+posZ);			
			// inside cloud?
			if (posX < 0 || posX >= resolution.getX()) return false;
			if (posY < 0 || posY >= resolution.getY()) return false;
			if (posZ < 0 || posZ >= resolution.getZ()) return false;		
			// get brick at the space
			BrickInstance b = brickArray[posZ][posY][posX];	
			// is there a brick?
			if (b == null) return false;
			// same brick?
			if (brick == b.getBrick()) return false;
			// no special brick?
			if (b.getBrick() instanceof SpecialBrick) return false;
			// is brick "inside" of given brick?
			List<IVectorInt3> units = b.getBrickUnitPositions();
			for (IVectorInt3 u : units) if (!unitList.contains(u)) return false;
			// check for same color (of each brick)?
			if (color == null)  {
				color = b.getColor();
			}
			else {
				if (b.getColor() != null && color != b.getColor()) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int getBrickConnections(IBrick brick, IVectorInt3 pos, BrickRotation rot) {
		Set<BrickInstance> foundBricks = new HashSet<BrickInstance>();		
		// loop though each unit
		for (IVectorInt3 v : BrickInstance.getBrickUnitPositions(brick, pos, rot)) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();			
			// get bricks over/under it
			if (posY+1 < resolution.getY()) foundBricks.add(brickArray[posZ][posY+1][posX]);
			if (posY-1 > -1) foundBricks.add(brickArray[posZ][posY-1][posX]);	
		}	
		foundBricks.remove(null);
		return foundBricks.size();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("BrickCloud ("+getNumberOfBricks()+")");
		s.append(System.getProperty("line.separator"));		
		for (int y = 0; y < resolution.getY(); y++) s.append(showLayer(y));
		return s.toString();
	}

	@Override
	public String showLayer(int y) {
		StringBuilder s = new StringBuilder();
		s.append("Ebene "+y+":");
		s.append(System.getProperty("line.separator"));
		for (int z = 0; z < resolution.getZ(); z++) {
			for (int x = 0; x < resolution.getX(); x++) {
				int i = brickList.get(y).indexOf(brickArray[z][y][x]);
				s.append((i < 0 ? " " : i < 100 ? i < 10 ? "  " : " " : "")+i+" ");
			}
			s.append(System.getProperty("line.separator"));
		}
		return s.toString();
	}
	
	@Override
	public BrickInstance getRandomBrick(int height) {
		Random rand = new Random();
		List<BrickInstance> list = brickList.get(height);
		if (list == null) return null;	
		return list.get(rand.nextInt(list.size()));
	}

	@Override
	public int getNumberOfBricks(int height) {
		return (brickList.get(height) != null ? brickList.get(height).size() : 0);
	}

	@Override
	public int getNumberOfBricks() {
		int sum = 0;
		for (int y = 0; y < resolution.getY(); y++) sum += brickList.get(y).size();
		return sum;
	}

	@Override
	public List<BrickInstance> getAllBricks() {
		List<BrickInstance> list = new ArrayList<BrickInstance>();
		for (int y = 0; y < resolution.getY(); y++) list.addAll(brickList.get(y));
		return list;
	}

	@Override
	public IVector3 getBrickPosition(BrickInstance brick) {
		IVector3 location = VectorMatrixFactory.newIVector3(locationLowerLeft);
		location = location.add(brickSet.getRootBrick().getDimensions().multiply(0.5));
		location = location.add(VectorMatrixFactory.newIVector3(
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_0) * brick.getPos().getX(),
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_1) * brick.getPos().getY(),
				brickSet.getRootBrick().getDimensions().get(MathHelpers.INDEX_2) * brick.getPos().getZ()));
		location = location.add(getBrickDimensions(brick).multiply(0.5));
		return location;
	}

	@Override
	public IVector3 getBrickDimensions(BrickInstance brick) {
		IVector3 dim = brick.getBrick().getDimensions();
		switch (brick.getRotation()) {			
			case ZDIR_POS:
			case ZDIR_NEG:
				return VectorMatrixFactory.newIVector3(dim.get(2), dim.get(1), dim.get(0));
			case XDIR_POS: 
			case XDIR_NEG:
			default:
				return dim;	
		}
	}

	@Override
	public ListenableUndirectedGraph<String, DefaultEdge> getBrickGraph() {
		ListenableUndirectedGraph<String, DefaultEdge> g = 
				new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		for (String v : brickGraph.vertexSet()) g.addVertex(v);
		for (DefaultEdge e : brickGraph.edgeSet()) g.addEdge(brickGraph.getEdgeSource(e), brickGraph.getEdgeTarget(e));
		return g;
	}

	@Override
	public List<BrickInstance> getBrickNeighbors(BrickInstance brick) {
		Set<BrickInstance> set = new HashSet<BrickInstance>();		
		for (IVectorInt3 v : brick.getBrickUnitPositions()) {
			int posX = v.getX();
			int posY = v.getY();
			int posZ = v.getZ();
			if (posZ-1 > -1) set.add(brickArray[posZ-1][posY][posX]);
			if (posZ+1 < resolution.getZ()) set.add(brickArray[posZ+1][posY][posX]);
			if (posX-1 > -1) set.add(brickArray[posZ][posY][posX-1]);
			if (posX+1 < resolution.getX()) set.add(brickArray[posZ][posY][posX+1]);
		}
		set.remove(null);
		set.remove(brick);
		return new ArrayList<BrickInstance>(set);
	}
	
	
	public static void main(String[] args) {
		ListenableUndirectedGraph<String, DefaultEdge> g = 
				new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		
		g.addVertex("a1");
		g.addVertex("a2");
		g.addVertex("a3");
		g.addVertex("b1");
		g.addVertex("b2");
		g.addVertex("b3");
		g.addVertex("c1");
		g.addVertex("c2");
		g.addVertex("c3");
		g.addVertex("d");
		g.addVertex("e1");
		g.addVertex("e2");
		g.addVertex("e3");
		
		g.addEdge("a1", "a2");
		g.addEdge("a2", "a3");
		g.addEdge("b1", "b2");
		g.addEdge("b2", "b3");
		g.addEdge("c1", "c2");
		g.addEdge("c2", "c3");
		g.addEdge("a3", "d");
		g.addEdge("b3", "d");
		g.addEdge("c3", "d");
//		g.addEdge("d", "e1");
//		g.addEdge("d", "e2");
//		g.addEdge("d", "e3");
		g.addEdge("e1", "e2");
		g.addEdge("e2", "e3");
		
		BronKerboschCliqueFinder<String, DefaultEdge> f = 
				new BronKerboschCliqueFinder<String, DefaultEdge>(g);
		
		System.out.println(f.getAllMaximalCliques());
		
		BlockCutpointGraph<String, DefaultEdge> b = 
				new BlockCutpointGraph<String, DefaultEdge>(g);
		
		System.out.println(b.getCutpoints());
	}

	@Override
	public ConnectivityInspector<String, DefaultEdge> getBrickGraphInspector() {
		return brickGraphInspector;
	}
	
	private class Data {
		List<String> adjList = new ArrayList<String>();
		boolean explored = false, articulationPoint = false;
		int discTime = 0, low = 0;
		String parent = null;
	}
	
	/**
	 * Source:
	 * http://kartikkukreja.wordpress.com/2013/11/09/articulation-points-or-cut-vertices-in-a-graph/
	 * http://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/
	 * @param brickGraph
	 * @return
	 */
	public List<BrickInstance> getWeakArticulationPoints() {
		Map<String, Data> vertexData = new HashMap<String, Data>();
		int time = 0;
		
		// loop through vertices and init data
		for (String vertex : brickGraph.vertexSet()) {
			vertexData.put(vertex, new Data());
		}
		
		// init adjList
		for (DefaultEdge edge : brickGraph.edgeSet()) {
			String source = brickGraph.getEdgeSource(edge);
			String target = brickGraph.getEdgeTarget(edge);
			vertexData.get(source).adjList.add(target);
			vertexData.get(target).adjList.add(source);
		}

		// loop through vertices
		for (String vertex : brickGraph.vertexSet()) {
			if (!vertexData.get(vertex).explored)
				time = depthFirstSearchWAP(brickGraph, vertexData, vertex, time);
		}		
		
		List<BrickInstance> weakBricks = new ArrayList<BrickInstance>();
		for (String v : vertexData.keySet()) {
			if (vertexData.get(v).articulationPoint) {
//				System.out.println(v);
				weakBricks.add(BrickInstance.brickMap.get(Long.parseLong(v)));
			}
		}
		
		return weakBricks;
	}
	
	private int depthFirstSearchWAP(ListenableUndirectedGraph<String, DefaultEdge> graph,
			Map<String, Data> vertexData, String u, int time) {	
		Data uData = vertexData.get(u);
		uData.explored = true;
		int numChilds = 0;
		uData.discTime = ++time;
		uData.low = uData.discTime;
		
		for (String v : uData.adjList) {
			Data vData = vertexData.get(v);
			if (!vData.explored) {
				numChilds++;
				vData.parent = u;
				time = depthFirstSearchWAP(graph, vertexData, v, time);
				uData.low = Math.min(uData.low, vData.low);
				
				if (uData.parent == null && numChilds > 1)
					uData.articulationPoint = true;
				// modification to only allow subgraphs > 1
				else if (uData.parent != null && vData.low >= uData.discTime && vData.adjList.size() > 1) 
					uData.articulationPoint = true;
			}
			else if (v != uData.parent)
				uData.low = Math.min(uData.low, vData.discTime);
		}
		
		return time;
	}
}

/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.jmonkey;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.core.IRenderObjectsFactory;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;

/**
 * Factory for jMonkey point cloud render object.
 * 
 * @author Philipp Jenke
 * 
 */
public class JMonkeyRenderObjectFactoryPointCloud implements
        IRenderObjectsFactory<Node> {

    /**
     * Reference to the asset manager
     */
    protected final AssetManager assetManager;

    /**
     * Constructor
     */
    public JMonkeyRenderObjectFactoryPointCloud(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see
     * edu.haw.cg.rendering.IRenderObjectsFactory#createRenderObject(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public Node createRenderObject(Node parentNode, CgNode cgNode) {
        IPointCloud pointCloud = (IPointCloud) cgNode.getContent();
        Node node = new Node();
        parentNode.attachChild(node);
        Geometry geometry = createPointCloudMesh(assetManager, pointCloud);
        node.attachChild(geometry);
        return node;
    }

    /*
     * (nicht-Javadoc)
     * 
     * @see edu.haw.cg.rendering.IRenderObjectsFactory#getType()
     */
    @Override
    public Class<?> getType() {
        return IPointCloud.class;
    }

    /**
     * Create a geometry object for a point cloud.
     * 
     * @param assetManager
     *            Current asset manager.
     * @param pointCloud
     *            Point cloud to be used.
     * @return New Geometry object containing the point cloud.
     */
    private Geometry createPointCloudMesh(AssetManager assetManager,
            IPointCloud pointCloud) {
        Mesh pointMesh = new Mesh();
        pointMesh.setPointSize(2);
        pointMesh.setMode(Mesh.Mode.Points);

        float[] vertexBuffer = new float[pointCloud.getNumberOfPoints() * 3];
        float[] colorBuffer = new float[pointCloud.getNumberOfPoints() * 3];
        int[] indexBuffer = new int[pointCloud.getNumberOfPoints()];
        for (int i = 0; i < pointCloud.getNumberOfPoints(); i++) {
            Vector position = pointCloud.getPoint(i).getPosition();
            Vector color = pointCloud.getPoint(i).getColor();
            vertexBuffer[i * 3] = (float) position.get(0);
            vertexBuffer[i * 3 + 1] = (float) position.get(1);
            vertexBuffer[i * 3 + 2] = (float) position.get(2);
            colorBuffer[i * 3] = (float) color.get(0);
            colorBuffer[i * 3 + 1] = (float) color.get(1);
            colorBuffer[i * 3 + 2] = (float) color.get(2);
            indexBuffer[i] = i;
        }
        pointMesh.setBuffer(Type.Position, 3, vertexBuffer);
        pointMesh.setBuffer(Type.Index, 1, indexBuffer);
        pointMesh.setBuffer(Type.Color, 3, colorBuffer);
        pointMesh.updateBound();

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        Geometry geom = new Geometry("Point Cloud", pointMesh);
        geom.setMaterial(mat);

        return geom;
    }

}

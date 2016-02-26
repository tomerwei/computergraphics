package cgresearch.ui.scenegraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;

public class InfoEditTriangleMesh extends InfoEditDefault implements
        ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String COMPUTE_FACE_NORMALS = "COMPUTE_FACE_NORMALS";
    private static final String COMPUTE_VERTEX_NORMALS = "COMPUTE_VERTEX_NORMALS";
    private static final String CENTER = "CENTER";
    private static final String INVERT_FACE_NORMALS = "INVERT_FACE_NORMALS";

    /**
     * Constructor.
     */
    public InfoEditTriangleMesh(JScrollPane parent, SceneGraphViewerNode node) {
        super(parent, node);
    }

    @Override
    protected void createGuiComponents() {
        super.createGuiComponents();

        ITriangleMesh triangleMesh = getTriangleMesh();

        addToLayout(new JLabel("#Triangles:"), false);
        JLabel labelNumberTriangles = new JLabel(""
                + triangleMesh.getNumberOfTriangles());
        addToLayout(labelNumberTriangles, false);

        addToLayout(new JLabel("#Vertices:"), false);
        JLabel labelNumberVertices = new JLabel(""
                + triangleMesh.getNumberOfVertices());
        addToLayout(labelNumberVertices, false);

        // Compute the face normals.
        JButton buttonComputeFaceNormals = new JButton("Compute Face Normals");
        buttonComputeFaceNormals.addActionListener(this);
        buttonComputeFaceNormals.setActionCommand(COMPUTE_FACE_NORMALS);
        addToLayout(buttonComputeFaceNormals, true);

        // Compute the vertex normals
        JButton buttonComputeVertexNormals = new JButton(
                "Compute Vertex Normals");
        addToLayout(buttonComputeVertexNormals, true);
        buttonComputeVertexNormals.addActionListener(this);
        buttonComputeVertexNormals.setActionCommand(COMPUTE_VERTEX_NORMALS);

        // Center the mesh
        JButton buttonCenter = new JButton("Center");
        addToLayout(buttonCenter, true);
        buttonCenter.addActionListener(this);
        buttonCenter.setActionCommand(CENTER);

        // Invert the face normals
        JButton buttonInvertFaceNormals = new JButton("Invert Face Normals");
        addToLayout(buttonInvertFaceNormals, true);
        buttonInvertFaceNormals.addActionListener(this);
        buttonInvertFaceNormals.setActionCommand(INVERT_FACE_NORMALS);

    }

    /**
     * Get the triangle mesh represented in the node.
     */
    private ITriangleMesh getTriangleMesh() {
        return (ITriangleMesh) (getNode().getSceneGraphNode().getContent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        ITriangleMesh triangleMeshNode = getTriangleMesh();
        if (actionEvent.getActionCommand().equals(COMPUTE_FACE_NORMALS)) {
            triangleMeshNode.computeTriangleNormals();
        } else if (actionEvent.getActionCommand()
                .equals(COMPUTE_VERTEX_NORMALS)) {
            triangleMeshNode.computeVertexNormals();
        } else if (actionEvent.getActionCommand().equals(INVERT_FACE_NORMALS)) {
            triangleMeshNode.invertFaceNormals();
        } else if (actionEvent.getActionCommand().equals(CENTER)) {
            triangleMeshNode.fitToUnitBox();
        }
    }
}

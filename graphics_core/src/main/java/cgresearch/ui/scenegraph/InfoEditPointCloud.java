package cgresearch.ui.scenegraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import cgresearch.graphics.datastructures.points.IPointCloud;

public class InfoEditPointCloud extends InfoEditDefault implements
        ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -143084328801165788L;

    /**
     * Constructor.
     */
    public InfoEditPointCloud(JScrollPane parent, SceneGraphViewerNode node) {
        super(parent, node);
    }

    @Override
    protected void createGuiComponents() {
        super.createGuiComponents();

        IPointCloud pointCloud = getPointCloud();

        addToLayout(new JLabel("#Points:"), false);
        JLabel labelNumberPoints = new JLabel(""
                + pointCloud.getNumberOfPoints());
        addToLayout(labelNumberPoints, false);
    }

    /**
     * Get the triangle mesh represented in the node.
     */
    private IPointCloud getPointCloud() {
        return (IPointCloud) (getNode().getSceneGraphNode().getContent());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
    }
}

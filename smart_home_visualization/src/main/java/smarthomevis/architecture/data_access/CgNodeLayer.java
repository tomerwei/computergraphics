package smarthomevis.architecture.data_access;

import cgresearch.graphics.scenegraph.CgNode;
import org.mongodb.morphia.annotations.Transient;
import smarthomevis.architecture.data_access.Layer;

public class CgNodeLayer extends Layer {

    @Transient
    private CgNode node;

    public CgNodeLayer() {
        super();
    }

    public CgNode getNode() {
        return node;
    }

    public void setNode(CgNode node) {
        this.node = node;
        // TODO add Node to RootNode
        //Connector.getInstance().getCgRootNode().addChild(node);
    }
}

package smarthomevis.architecture.entities;

import cgresearch.graphics.scenegraph.CgNode;
import org.mongodb.morphia.annotations.Transient;

public class CgNodeLayer extends BaseEntity {

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

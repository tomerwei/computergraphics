package smarthomevis.architecture.logic;

import cgresearch.graphics.scenegraph.CgNode;

public class CgNodeLayer implements ILayer {

    private String name;
    private CgNode node;

    public CgNodeLayer(CgNode node) {
        this.node = node;
    }

    public CgNode getNode() {
        return node;
    }

    public void setNode(CgNode node) {
        this.node = node;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

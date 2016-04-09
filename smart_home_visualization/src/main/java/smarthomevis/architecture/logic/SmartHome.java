package smarthomevis.architecture.logic;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;

import java.util.ArrayList;
import java.util.List;

public class SmartHome extends CgApplication {

    private List<ILayer> layers;

    public SmartHome() {
        super();
        layers = new ArrayList<>();
    }

    public CgNodeLayer createCgNodeLayer(CgNode node) {
        CgNodeLayer layer = new CgNodeLayer(node);
        getCgRootNode().addChild(node);
        return layer;
    }

    public void addLayer(ILayer layer) {
        layers.add(layer);
    }

    public List<ILayer> getLayers() {
        return layers;
    }

    public void setLayers(List<ILayer> layers) {
        this.layers = layers;
    }
}

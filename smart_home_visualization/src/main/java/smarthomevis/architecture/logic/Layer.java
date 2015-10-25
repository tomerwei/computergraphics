package smarthomevis.architecture.logic;

import cgresearch.graphics.scenegraph.CgNode;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    private List<DataSource> dataSources;
    private CgNode rootNode;
    private CgNode node;
    private String name;
    private boolean visible;

    public Layer(String name, CgNode rootNode) {
        dataSources = new ArrayList<>();
        visible = true;
        this.rootNode = rootNode;
        this.name = name;
    }

}

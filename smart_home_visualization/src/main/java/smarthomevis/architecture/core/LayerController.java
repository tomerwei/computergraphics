package smarthomevis.architecture.core;

import cgresearch.graphics.scenegraph.CgNode;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.data_access.Layer;
import smarthomevis.architecture.data_access.Repository;

import java.util.ArrayList;
import java.util.List;

public class LayerController {

    private List<Layer> layers;
    private Repository<Layer> layerRepository;
    private CgNode rootNode;

    public LayerController(Datastore datastore, CgNode rootNode) {
        layers = new ArrayList<>();
        layerRepository = new Repository<>(datastore, Layer.class);
        this.rootNode = rootNode;
    }

    public Layer get(String id) {
        return layerRepository.get(new ObjectId(id));
    }

    public String save(String name) {
        Layer layer = new Layer();
        layer.setName(name);
        layers.add(layer);
        return layerRepository.save(layer).toString();
    }

    public void delete(String id) {
        layerRepository.delete(new ObjectId(id));
    }

    public void addDeviceToLayer(String deviceId, String layerId) {
        Layer layer = get(layerId);
        layer.addDevice(new ObjectId(deviceId));
    }

    public void addNodeToLayer(CgNode node, String layerId) {
        Layer layer = get(layerId);
        layer.toCgNodeLayer().setNode(node);
        layerRepository.delete(new ObjectId(layerId));
    }

    public Repository<Layer> getRepository() {
        return layerRepository;
    }
}

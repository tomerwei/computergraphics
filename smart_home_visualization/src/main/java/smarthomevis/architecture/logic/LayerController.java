package smarthomevis.architecture.logic;

import cgresearch.graphics.scenegraph.CgNode;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.entities.Layer;
import smarthomevis.architecture.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

public class LayerController implements Controller {

    private List<Layer> layers;
    private Repository<Layer> layerRepository;

    public LayerController() {
        layers = new ArrayList<>();
        Connector connector = new Connector();
        Datastore datastore = connector.connectToMongoDB("smarthome");
        layerRepository = new Repository<>(datastore, Layer.class);
    }

    @Override
    public Layer get(String id) {
        return layerRepository.get(new ObjectId(id));
    }

    @Override
    public String save(String name) {
        Layer layer = new Layer();
        layer.setName(name);
        layers.add(layer);
        return layerRepository.save(layer).toString();
    }

    @Override
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

    @Override
    public Repository<Layer> getRepository() {
        return layerRepository;
    }
}

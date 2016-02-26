package smarthomevis.architecture.rest;

import cgresearch.graphics.scenegraph.CgNode;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.data_access.Layer;
import smarthomevis.architecture.core.LayerController;

public class LayerResource extends ServerResource {

    private LayerController controller;
    private Datastore datastore;
    private CgNode rootNode;

    public LayerResource() {
        controller = new LayerController(datastore, rootNode);
    }

    @Get
    public String getLayer() {
        String id = getRequestAttributes().get("id").toString();
        Layer layer = controller.get(id);
        return layer.getName();
    }

    @Put
    public String createLayer(String name) {
        return controller.save(name);
    }

    protected void doInit() throws ResourceException {
        this.datastore = (Datastore) getContext().getAttributes().get("datastore");
        this.rootNode = (CgNode) getContext().getAttributes().get("rootCgNode");
    }
}

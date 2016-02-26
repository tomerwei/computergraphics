package smarthomevis.architecture.rest;

import cgresearch.graphics.scenegraph.CgNode;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.core.LayerController;

public class LayerDeviceResource extends ServerResource {

    private LayerController controller;
    private Datastore datastore;
    private CgNode rootNode;

    public LayerDeviceResource() {
        controller = new LayerController(datastore, rootNode);
    }

    @Put
    public void addDeviceToLayer() {
        String layerId = getRequestAttributes().get("id").toString();
        String deviceId = getRequestAttributes().get("deviceId").toString();
        controller.addDeviceToLayer(layerId, deviceId);
    }

    protected void doInit() throws ResourceException {
        this.datastore = (Datastore) getContext().getAttributes().get("datastore");
        this.rootNode = (CgNode) getContext().getAttributes().get("rootCgNode");
    }
}

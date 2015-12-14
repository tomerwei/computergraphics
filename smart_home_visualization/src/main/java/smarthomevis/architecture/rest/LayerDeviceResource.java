package smarthomevis.architecture.rest;

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.logic.LayerController;

public class LayerDeviceResource extends ServerResource {

    LayerController controller = new LayerController();

    @Put
    public void addDeviceToLayer() {
        String layerId = getRequestAttributes().get("id").toString();
        String deviceId = getRequestAttributes().get("deviceId").toString();
        controller.addDeviceToLayer(layerId, deviceId);
    }
}

package smarthomevis.architecture.rest;

import org.mongodb.morphia.Datastore;
import org.restlet.resource.*;
import smarthomevis.architecture.core.DeviceController;

public class DeviceResource extends ServerResource {

    private DeviceController deviceController;
    private Datastore datastore;

    public DeviceResource() {
        deviceController = new DeviceController(datastore);
    }

    @Get
    public String getDevice() {
        String id = getRequestAttributes().get("id").toString();
        return deviceController.getDeviceAsJson(id);
    }

    @Post("device:json")
    public String connectDevice(String device) {
        return deviceController.createDevice(device);
    }

    @Put
    public String updateDevice() {
        // TODO
        return "put";
    }

    @Delete
    public void deleteDevice() {
        String id = getRequestAttributes().get("id").toString();
        deviceController.deleteDevice(id);
    }

    @Override
    protected void doInit() throws ResourceException {
        this.datastore = (Datastore) getContext().getAttributes().get("datastore");
    }
}

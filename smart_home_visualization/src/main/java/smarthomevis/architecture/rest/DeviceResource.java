package smarthomevis.architecture.rest;

import org.restlet.resource.*;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.logic.DeviceController;

public class DeviceResource extends ServerResource {

    DeviceController controller = new DeviceController();

    @Get
    public String getDevice() {
        // TODO return as JSON
        String id = getRequestAttributes().get("id").toString();
        Device device = controller.get(id);
        return "Name: " + device.getName() + " --- Data: " + device.getData();
    }

    @Put
    public String connectDevice(String name) {
        return controller.save(name);
    }

    @Post
    public void updateDevice() {
        // TODO
    }

    @Delete
    public void deleteDevice() {
        String objectId = getRequestAttributes().get("id").toString();
        DeviceController controller = new DeviceController();
        controller.delete(objectId);
    }

}

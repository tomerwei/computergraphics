package smarthomevis.architecture.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.logic.Device;

public class DevicesResource extends ServerResource {

    private Repository<Device> deviceRepository;

    public DevicesResource() {
        deviceRepository = new Repository<>(Device.class);
    }

    @Get
    public String getAllDevices() {
        return JsonConverter.getGsonBuilder().create()
                .toJson(deviceRepository.getAll());
    }

    @Post("device:txt")
    public String createDevice(String deviceJson) {
        return saveDevice(deviceJson);
    }

    @Delete
    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }

    private String saveDevice(String deviceJson) {
        Device device = JsonConverter.buildDevice(deviceJson);
        return deviceRepository.save(device);
    }
}

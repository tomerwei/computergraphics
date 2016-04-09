package smarthomevis.architecture.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.logic.Device;

public class DeviceResource extends ServerResource {

    private Repository<Device> deviceRepository;

    public DeviceResource() {
        deviceRepository = new Repository<>(Device.class);
    }

    @Get
    public String getDevice() {
        String id = extractId();
        if (isValid(id)) {
            Device device = deviceRepository.get(id);
            return JsonConverter.convertToJson(device);
        } else {
            return notFoundError();
        }
    }

    @Put("device:txt")
    public void updateDevice(String deviceJson) {
        String id = extractId();
        if (isValid(id)) {
            Device device = JsonConverter.buildDevice(deviceJson);
            device.setId(id);
            deviceRepository.save(device);
        } else {
            notFoundError();
        }
    }

    @Delete
    public void deleteDevice() {
        String id = extractId();
        if (isValid(id)) {
            deviceRepository.delete(id);
        } else {
            notFoundError();
        }
    }

    private String extractId() {
        return getRequestAttributes().get("id").toString();
    }

    private boolean isValid(String id) {
        return id != null && deviceRepository.has(id);
    }

    private String notFoundError() {
        // TODO
        return "404";
    }
}

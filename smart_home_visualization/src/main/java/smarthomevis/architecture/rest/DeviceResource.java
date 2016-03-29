package smarthomevis.architecture.rest;

import org.bson.types.ObjectId;
import org.restlet.resource.*;
import smarthomevis.architecture.data_access.JsonConverter;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.entities.Device;

public class DeviceResource extends ServerResource {

    private Repository<Device> deviceRepository;

    public DeviceResource() {
        deviceRepository = new Repository<>(Device.class);
    }

    @Get
    public String getDevice() {
        String id = getRequestAttributes().get("id").toString();
        Device device = deviceRepository.get(new ObjectId(id));
        return JsonConverter.convertToJson(device);
    }

    @Put("device:txt")
    public void updateDevice(String deviceJson) {
        String id = getRequestAttributes().get("id").toString();
        if (deviceRepository.has(new ObjectId(id))) {
            Device device = JsonConverter.buildDevice(deviceJson);
            deviceRepository.save(device);
        }
    }

    @Post("device:txt")
    public String addDevice(String deviceJson) {
        Device device = JsonConverter.buildDevice(deviceJson);
        return deviceRepository.save(device).toString();
    }

    @Delete
    public void deleteDevice() {
        String id = getRequestAttributes().get("id").toString();
        deviceRepository.delete(new ObjectId(id));
    }
}

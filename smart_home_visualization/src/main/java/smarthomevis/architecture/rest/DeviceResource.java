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
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
        return "Error: A device with this ID could not be found.";
    }

    @Put("device:txt")
    public void updateDevice(String deviceJson) {
        String id = extractId();
        if (isValid(id)) {
            Device device = JsonConverter.buildDevice(deviceJson);
            device.setId(id);
            deviceRepository.save(device);
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
    }

    @Delete
    public void deleteDevice() {
        String id = extractId();
        if (isValid(extractId())) {
            deviceRepository.delete(id);
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
    }

    private String extractId() {
        return getRequestAttributes().get("id").toString();
    }

    private boolean isValid(String id) {
        return id != null && deviceRepository.has(id);
    }
}

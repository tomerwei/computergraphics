package smarthomevis.architecture.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.logic.DeviceLayer;

public class DeviceLayerResource extends ServerResource {

    private Repository<DeviceLayer> deviceLayerRepository;

    public DeviceLayerResource() {
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
    }

    @Get
    public String getDeviceLayer() {
        String id = extractId();
        if (isValid(id)) {
            DeviceLayer deviceLayer = deviceLayerRepository.get(id);
            return JsonConverter.convertToJson(deviceLayer);
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
        return "Error: A DeviceLayer with this ID could not be found.";
    }

    @Put("deviceLayer:txt")
    public void updateDeviceLayer(String deviceLayerJson) {
        String id = extractId();
        if (isValid(id)) {
            DeviceLayer deviceLayer = JsonConverter.buildDeviceLayer(deviceLayerJson);
            deviceLayer.setId(id);
            deviceLayerRepository.save(deviceLayer);
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
    }

    @Delete
    public void deleteDevice() {
        String id = extractId();
        if (isValid(id)) {
            deviceLayerRepository.delete(id);
        }
        // TODO Fehlercode zurueck, weil ID nicht existiert
    }

    private String extractId() {
        return getRequestAttributes().get("id").toString();
    }

    private boolean isValid(String id) {
        return id != null && deviceLayerRepository.has(id);
    }
}

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
        } else {
            return notFoundError();
        }
    }

    @Put("deviceLayer:txt")
    public void updateDeviceLayer(String deviceLayerJson) {
        String id = extractId();
        if (isValid(id)) {
            DeviceLayer deviceLayer = JsonConverter.buildDeviceLayer(deviceLayerJson);
            deviceLayer.setId(id);
            deviceLayerRepository.save(deviceLayer);
        } else {
            notFoundError();
        }
    }

    @Delete
    public void deleteDevice() {
        String id = extractId();
        if (isValid(id)) {
            deviceLayerRepository.delete(id);
        } else {
            notFoundError();
        }
    }

    private String extractId() {
        return getRequestAttributes().get("id").toString();
    }

    private boolean isValid(String id) {
        return id != null && deviceLayerRepository.has(id);
    }

    private String notFoundError() {
        // TODO
        return "404";
    }
}

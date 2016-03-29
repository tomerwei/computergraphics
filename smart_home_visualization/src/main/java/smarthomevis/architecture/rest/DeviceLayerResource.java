package smarthomevis.architecture.rest;

import org.bson.types.ObjectId;
import org.restlet.resource.*;
import smarthomevis.architecture.data_access.JsonConverter;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.entities.DeviceLayer;

public class DeviceLayerResource extends ServerResource {

    private Repository<DeviceLayer> deviceLayerRepository;

    public DeviceLayerResource() {
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
    }

    @Get
    public String getDeviceLayer() {
        String id = getRequestAttributes().get("id").toString();
        DeviceLayer deviceLayer = deviceLayerRepository.get(new ObjectId(id));
        return JsonConverter.convertToJson(deviceLayer);
    }

    @Put("device:txt")
    public void updateDevice(String deviceLayerJson) {
        String id = getRequestAttributes().get("id").toString();
        if (deviceLayerRepository.has(new ObjectId(id))) {
            DeviceLayer deviceLayer = JsonConverter.buildDeviceLayer(deviceLayerJson);
            deviceLayerRepository.save(deviceLayer);
        }
    }

    @Post("device:txt")
    public String addDevice(String deviceLayerJson) {
        DeviceLayer deviceLayer = JsonConverter.buildDeviceLayer(deviceLayerJson);
        return deviceLayerRepository.save(deviceLayer).toString();
    }

    @Delete
    public void deleteDevice() {
        String id = getRequestAttributes().get("id").toString();
        deviceLayerRepository.delete(new ObjectId(id));
    }
}

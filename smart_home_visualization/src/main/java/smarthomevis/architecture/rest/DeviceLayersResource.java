package smarthomevis.architecture.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.logic.DeviceLayer;

// This test needs MongoDB running to pass
//@Ignore
public class DeviceLayersResource extends ServerResource {

    private Repository<DeviceLayer> deviceLayerRepository;

    public DeviceLayersResource() {
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
    }

    @Get
    public String getAllDeviceLayers() {
        return JsonConverter.getGsonBuilder().create()
                .toJson(deviceLayerRepository.getAll());
    }

    @Post("deviceLayer:txt")
    public String createDeviceLayer(String deviceLayerJson) {
        return saveDeviceLayer(deviceLayerJson);
    }

    @Delete
    public void deleteAllDeviceLayers() {
        deviceLayerRepository.deleteAll();
    }

    private String saveDeviceLayer(String deviceLayerJson) {
        DeviceLayer deviceLayer = JsonConverter.buildDeviceLayer(deviceLayerJson);
        return deviceLayerRepository.save(deviceLayer);
    }

}

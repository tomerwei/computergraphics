package smarthomevis.architecture;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.logic.Device;
import smarthomevis.architecture.logic.DeviceLayer;

import static org.junit.Assert.*;

// This test needs MongoDB running to pass
@Ignore
public class JsonConverterTest {

    private static Repository<Device> deviceRepository;
    private static Repository<DeviceLayer> deviceLayerRepository;
    private Device device;
    private DeviceLayer deviceLayer;

    @BeforeClass
    public static void initialize() {
        deviceRepository = new Repository<>(Device.class);
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
    }

    @Before
    public void setUp() {
        deviceRepository.deleteAll();
        deviceLayerRepository.deleteAll();

        device = new Device();
        device.setName("Thermometer1");
        device.addEntry("21 °C");

        deviceLayer = new DeviceLayer();
        deviceLayer.setName("Kitchen");
    }

    @Test
    public void convertDeviceToJson() {
        String deviceId = deviceRepository.save(device);
        device = deviceRepository.get(deviceId);
        String json = JsonConverter.convertToJson(device);

        assertTrue(json.contains("\"name\":\"Thermometer1\""));
        assertTrue(json.contains(String.format("\"id\":\"%s\"", deviceId)));
        assertFalse(json.contains("version"));
    }

    @Test
    public void buildDeviceFromJson() {
        String deviceJson =
                "{\"entries\":{\"29/16/2016 6:16:25 PM\":\"21 °C\"}," +
                        "\"name\":\"Thermometer1\"}";
        Device builtDevice = JsonConverter.buildDevice(deviceJson);
        Device savedDevice = deviceRepository.get(deviceRepository.save(builtDevice));

        assertEquals(savedDevice.getName(), "Thermometer1");
        assertTrue(savedDevice.getEntries().containsKey("29/16/2016 6:16:25 PM"));
        assertTrue(savedDevice.getEntries().containsValue("21 °C"));
    }

    @Test
    public void updateDeviceFromJson() {
        String deviceId = deviceRepository.save(device);
        String deviceJson = String.format(
                "{\"entries\":{\"29/16/2016 6:16:25 PM\":\"21 °C\"}," +
                        "\"id\":\"%s\"," +
                        "\"name\":\"Thermometer1\"}", deviceId);
        Device builtDevice = JsonConverter.buildDevice(deviceJson);
        Device savedDevice = deviceRepository.get(deviceRepository.save(builtDevice));

        assertEquals(savedDevice.getName(), "Thermometer1");
        assertEquals(savedDevice.getId(), deviceId);
        assertTrue(savedDevice.getEntries().containsKey("29/16/2016 6:16:25 PM"));
        assertTrue(savedDevice.getEntries().containsValue("21 °C"));
    }

    @Test
    public void convertDeviceLayerToJson() {
        String deviceId = deviceRepository.save(device);
        deviceLayer.addDevice(deviceId);
        deviceLayer = deviceLayerRepository.get(deviceLayerRepository.save(deviceLayer));
        String json = JsonConverter.convertToJson(deviceLayer);

        assertTrue(json.contains(String.format("\"devices\":[\"%s\"]", deviceId)));
        assertTrue(json.contains("\"name\":\"Kitchen\""));
        assertFalse(json.contains("version"));
    }

    @Test
    public void buildDeviceLayerFromJson() {
        String deviceID = deviceRepository.save(device);
        String layerJson = String.format("{\"devices\":[\"%s\"]," +
                "\"name\":\"Kitchen\"}", deviceID);
        DeviceLayer builtLayer = JsonConverter.buildDeviceLayer(layerJson);
        DeviceLayer savedLayer = deviceLayerRepository.get(deviceLayerRepository.save(builtLayer));

        assertEquals("Kitchen", savedLayer.getName());
        assertTrue(savedLayer.getDevices().contains(deviceID));
    }

    @Test
    public void updateDeviceLayerFromJson() {
        String layerId = deviceLayerRepository.save(deviceLayer);
        String deviceId = deviceRepository.save(device);
        String layerJson = String.format("{\"devices\":[\"%s\"]," +
                "\"id\":\"%s\"," +
                "\"name\":\"Bedroom\"}", deviceId, layerId);
        DeviceLayer builtLayer = JsonConverter.buildDeviceLayer(layerJson);
        DeviceLayer savedLayer = deviceLayerRepository.get(deviceLayerRepository.save(builtLayer));

        assertEquals("Bedroom", savedLayer.getName());
        assertTrue(savedLayer.getDevices().contains(deviceId));
    }
}

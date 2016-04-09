package smarthomevis.architecture;

import org.junit.*;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.logic.Device;
import smarthomevis.architecture.logic.DeviceLayer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// This test needs MongoDB running to pass
@Ignore
public class DeviceLayerResourceTest {

    private static Repository<DeviceLayer> deviceLayerRepository;
    private static Repository<Device> deviceRepository;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/layers/";

    private String deviceId;
    private String deviceLayerId;

    @BeforeClass
    public static void initialize() {
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
        deviceRepository = new Repository<>(Device.class);
    }

    @AfterClass
    public static void clear() {
        deviceLayerRepository.deleteAll();
        deviceRepository.deleteAll();
    }

    @Before
    public void setUp() {
        deviceLayerRepository.deleteAll();
        deviceRepository.deleteAll();

        Device device = new Device();
        device.setName("Sensor_09");
        device.addEntry("Movement");
        deviceId = deviceRepository.save(device);

        DeviceLayer deviceLayer = new DeviceLayer();
        deviceLayer.setName("Kitchen");
        deviceLayer.addDevice(deviceId);
        deviceLayerId = deviceLayerRepository.save(deviceLayer);
    }

    @Test
    public void testGetWithIdReturnsLayer() throws IOException {
        client = new ClientResource(testUrl + deviceLayerId);

        String deviceLayerJson = JsonConverter.
                convertToJson(deviceLayerRepository.get(deviceLayerId));
        String responseJson = client.get().getText();

        assertEquals(deviceLayerJson, responseJson);
    }

    @Test
    public void testPutWithIdUpdatesDeviceLayer() throws IOException {
        client = new ClientResource(testUrl + deviceLayerId);

        String deviceLayerJson = String.format("{\"devices\":[\"%s\", \"12345\"]," +
                "\"name\":\"Living Room\"}", deviceId);
        client.put(deviceLayerJson);
        DeviceLayer updatedDeviceLayer = deviceLayerRepository.get(deviceLayerId);

        assertEquals(updatedDeviceLayer.getName(), "Living Room");
        assertTrue(updatedDeviceLayer.hasDevice(deviceId));
        assertTrue(updatedDeviceLayer.hasDevice("12345"));
    }

    @Test
    public void testDeleteWithIdDeletesDeviceLayer() {
        assertTrue(deviceLayerRepository.has(deviceLayerId));

        client = new ClientResource(testUrl + deviceLayerId);
        client.delete();

        assertFalse(deviceLayerRepository.has(deviceLayerId));
    }
}

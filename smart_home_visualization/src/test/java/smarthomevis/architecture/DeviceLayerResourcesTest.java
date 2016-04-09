package smarthomevis.architecture;

import org.junit.*;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.logic.Device;
import smarthomevis.architecture.logic.DeviceLayer;

import java.io.IOException;

import static org.junit.Assert.*;

// This test needs MongoDB running to pass
@Ignore
public class DeviceLayerResourcesTest {

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
    public void testGetReturnsAllDevices() throws IOException {
        DeviceLayer additionalLayer = new DeviceLayer();
        additionalLayer.setName("Living Room");
        String additionalLayerId = deviceLayerRepository.save(additionalLayer);

        client = new ClientResource(testUrl);
        String responseJson = client.get().getText();

        assertTrue(responseJson.contains(deviceLayerId));
        assertTrue(responseJson.contains(additionalLayerId));
        assertTrue(responseJson.contains(deviceId));
        assertTrue(responseJson.contains("Kitchen"));
        assertTrue(responseJson.contains("Living Room"));
    }

    @Test
    public void testPostCreatesDeviceLayerReturnsId() throws IOException {
        client = new ClientResource(testUrl);

        String deviceLayerJson = "{\"devices\":[\"5708d53400c3b31cac7d312d\", \"5708d53400c3b31cac7d312e\"],\"name\":\"Living Room\"}";
        String responseId = client.post(deviceLayerJson).getText();
        System.out.println(responseId);

        assertTrue(deviceLayerRepository.has(responseId));

        DeviceLayer layer = deviceLayerRepository.get(responseId);

        assertEquals(layer.getName(), "Living Room");
        assertTrue(layer.hasDevice("5708d53400c3b31cac7d312d"));
        assertTrue(layer.hasDevice("5708d53400c3b31cac7d312e"));
    }

    @Test
    public void testDeleteAllDeviceLayers() {
        assertTrue(deviceLayerRepository.has(deviceLayerId));

        client = new ClientResource(testUrl);
        client.delete();

        assertFalse(deviceLayerRepository.has(deviceLayerId));
    }

}

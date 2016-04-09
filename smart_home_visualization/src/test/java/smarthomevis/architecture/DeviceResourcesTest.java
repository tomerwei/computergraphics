package smarthomevis.architecture;

import org.junit.*;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.logic.Device;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.rest.DeviceResource;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// This test needs MongoDB running to pass
@Ignore
public class DeviceResourcesTest {

    private static DeviceResource resource;
    private static Repository<Device> deviceRepository;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/devices/";

    private String device1Id;
    private String device2Id;

    @BeforeClass
    public static void initialize() {
        resource = new DeviceResource();
        deviceRepository = new Repository<>(Device.class);
    }

    @AfterClass
    public static void clear() {
        deviceRepository.deleteAll();
    }

    @Before
    public void setUp() {
        deviceRepository.deleteAll();

        Device device1 = new Device();
        device1.setName("Sensor_09");
        device1.addEntry("Movement");
        device1Id = deviceRepository.save(device1);

        Device device2 = new Device();
        device2.setName("Thermometer_02");
        device2.addEntry("19°C");
        device2Id = deviceRepository.save(device2);
    }

    @Test
    public void testGetReturnsAllDevices() throws IOException {
        client = new ClientResource(testUrl);

        String device1Json = JsonConverter.convertToJson(deviceRepository.get(device1Id));
        String device2Json = JsonConverter.convertToJson(deviceRepository.get(device2Id));
        String responseJson = client.get().getText();

        assertTrue(responseJson.contains(device1Json));
        assertTrue(responseJson.contains(device2Json));
    }

    @Test
    public void testPostCreatesDeviceReturnsId() throws IOException {
        client = new ClientResource(testUrl);

        String deviceJson = "{\"name\":\"Sensor_05\"}";
        String responseId = client.post(deviceJson).getText();

        assertTrue(deviceRepository.has(responseId));
        assertEquals("Sensor_05", deviceRepository.get(responseId).getName());
    }

    @Test
    public void testDeleteAllDevices() {
        assertTrue(deviceRepository.has(device1Id));
        assertTrue(deviceRepository.has(device2Id));

        client = new ClientResource(testUrl);
        client.delete();

        assertFalse(deviceRepository.has(device1Id));
        assertFalse(deviceRepository.has(device2Id));
    }
}

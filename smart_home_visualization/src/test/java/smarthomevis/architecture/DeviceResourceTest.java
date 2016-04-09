package smarthomevis.architecture;

import org.junit.*;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.json.JsonConverter;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.logic.Device;

import java.io.IOException;

import static org.junit.Assert.*;

// This test needs MongoDB running to pass
@Ignore
public class DeviceResourceTest {

    private static Repository<Device> deviceRepository;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/devices/";

    private String deviceId;

    @BeforeClass
    public static void initialize() {
        deviceRepository = new Repository<>(Device.class);
    }

    @AfterClass
    public static void clear() {
        deviceRepository.deleteAll();
    }

    @Before
    public void setUp() {
        deviceRepository.deleteAll();

        Device device = new Device();
        device.setName("Sensor_09");
        device.addEntry("Movement");
        deviceId = deviceRepository.save(device);
    }

    @Test
    public void testGetWithIdReturnsDevice() throws IOException {
        client = new ClientResource(testUrl + deviceId);

        String deviceJson = JsonConverter.convertToJson(deviceRepository.get(deviceId));
        String responseJson = client.get().getText();

        assertEquals(deviceJson, responseJson);
    }

    @Test
    public void testPutWithIdUpdatesDevice() throws IOException {
        client = new ClientResource(testUrl + deviceId);

        String deviceJson = String.format(
                "{\"entries\":{\"29/16/2016 6:16:25 PM\":\"21 °C\"}," +
                        "\"id\":\"%s\"," +
                        "\"name\":\"Thermometer1\"}", deviceId);
        client.put(deviceJson);
        Device updatedDevice = deviceRepository.get(deviceId);

        assertEquals("Thermometer1", updatedDevice.getName());
        assertTrue(updatedDevice.getEntries().containsKey("29/16/2016 6:16:25 PM"));
        assertTrue(updatedDevice.getEntries().containsValue("21 °C"));
    }

    @Test
    public void testDeleteWithIdDeletesDevice() {
        assertTrue(deviceRepository.has(deviceId));

        client = new ClientResource(testUrl + deviceId);
        client.delete();

        assertFalse(deviceRepository.has(deviceId));
    }
}

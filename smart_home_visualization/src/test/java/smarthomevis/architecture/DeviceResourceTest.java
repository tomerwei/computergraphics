package smarthomevis.architecture;

import org.bson.types.ObjectId;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.JsonConverter;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.rest.DeviceResource;

import java.io.IOException;

import static org.junit.Assert.*;

// This test needs MongoDB running to pass
//@Ignore
public class DeviceResourceTest {

    private static DeviceResource resource;
    private static Repository<Device> deviceRepository;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/devices/";

    private Device device;

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

        device = new Device();
        device.setName("Sensor_09");
        device.addEntry("Movement");
    }

    @Test
    public void testGET() throws IOException {
        ObjectId id = deviceRepository.save(device);
        client = new ClientResource(testUrl + id.toString());

        String deviceJson = JsonConverter.convertToJson(deviceRepository.get(id));
        String responseJson = client.get().getText();

        assertEquals(deviceJson, responseJson);
    }

    @Test
    public void testPUT() throws IOException {
        ObjectId id = deviceRepository.save(device);
        client = new ClientResource(testUrl + id.toString());

        String deviceJson = String.format(
                "{\"entries\":{\"29/16/2016 6:16:25 PM\":\"21 °C\"}," +
                        "\"id\":\"%s\"," +
                        "\"name\":\"Thermometer1\"}", id);
        client.put(deviceJson);
        Device updatedDevice = deviceRepository.get(id);

        assertEquals("Thermometer1", updatedDevice.getName());
        assertTrue(updatedDevice.getEntries().containsKey("29/16/2016 6:16:25 PM"));
        assertTrue(updatedDevice.getEntries().containsValue("21 °C"));
    }

    @Test
    public void testPOST() throws IOException {
        client = new ClientResource(testUrl);

        String deviceJson = "{\"name\":\"Thermometer1\"}";
        String responseId = client.post(deviceJson).getText();

        assertTrue(deviceRepository.has(new ObjectId(responseId)));
        assertEquals("Thermometer1", deviceRepository.get(new ObjectId(responseId)).getName());
    }

    @Test
    public void testDELETE() {
        ObjectId id = deviceRepository.save(device);
        client = new ClientResource(testUrl + id.toString());
        client.delete();

        assertFalse(deviceRepository.has(id));
    }
}

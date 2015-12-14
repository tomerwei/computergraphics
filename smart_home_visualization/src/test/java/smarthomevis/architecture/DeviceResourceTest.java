package smarthomevis.architecture;

import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.logic.Connector;
import smarthomevis.architecture.persistence.Repository;

import java.io.IOException;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class DeviceResourceTest {

    private static Datastore datastore;
    private static Repository<Device> deviceRepository;

    private Device device;

    private static int port = 8183;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/devices/";

    @BeforeClass
    public static void initialize() throws Exception {
        datastore = Connector.getInstance().connectToMongoDB("smarthome");
        deviceRepository = new Repository<>(datastore, Device.class);
    }

    @Before
    public void setUp() {
        deleteDevices();
        device = new Device();
        device.setName("Thermometer_1");
    }

    @AfterClass
    public static void clear() {
        deleteDevices();
    }

    @Test
    public void testGetWithObjectId() throws IOException {
        String id = deviceRepository.save(device).toString();
        client = new ClientResource(testUrl + id);
        String response = client.get().getText();
        Assert.assertEquals(response, "Name: " + device.getName() + " --- Data: " + device.getData());
    }

    @Test
    public void testConnectDevice() throws IOException {
        String name = device.getName();
        client = new ClientResource(testUrl + name);
        String IdOfConnectedDevice = client.put(name).getText();
        Device connectedDevice = deviceRepository.get(new ObjectId(IdOfConnectedDevice));
        Assert.assertEquals(name, connectedDevice.getName());
    }

    @Test
    public void deleteWithObjectId() throws IOException {
        String name = device.getName();
        client = new ClientResource(testUrl + name);
        String IdOfConnectedDevice = client.put(name).getText();
        client = new ClientResource(testUrl + IdOfConnectedDevice);
        client.delete();
        Assert.assertEquals(null, deviceRepository.get(new ObjectId(IdOfConnectedDevice)));
    }

    private static void deleteDevices() {
        datastore.delete(datastore.createQuery(Object.class));
    }

}

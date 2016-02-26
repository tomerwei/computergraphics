package smarthomevis.architecture;

import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.core.DeviceController;
import smarthomevis.architecture.core.SmartHome;
import smarthomevis.architecture.data_access.Device;
import smarthomevis.architecture.data_access.ObjectIdDeserializer;
import smarthomevis.architecture.data_access.ObjectIdSerializer;
import smarthomevis.architecture.data_access.Repository;

import static org.junit.Assert.*;

@Ignore
public class DeviceControllerTest {

    private static Datastore datastore;
    private static DeviceController controller;
    private static Repository<Device> repository;
    private static GsonBuilder gson;

    private Device device;

    @BeforeClass
    public static void initialize() throws Exception {
        datastore = new SmartHome().initializeForTesting();
        controller = new DeviceController(datastore);
        repository = new Repository<>(datastore, Device.class);
        initializeGson();
    }

    @Before
    public void setUp() {
        device = new Device();
        device.setName("Thermometer_1");
        device.getDatedMeasurements().put("some Date", "23 °C");
    }

    @AfterClass
    public static void clear() {
        deleteDevices();
    }

    @Test
    public void testCreateDeviceIsSuccessful() {
        String id = controller.createDevice(gson.create().toJson(device));
        Device createdDeviceId = repository.get(new ObjectId(id));

        assertEquals(device.getName(), createdDeviceId.getName());
        assertEquals(device.getDatedMeasurements(), createdDeviceId.getDatedMeasurements());
    }

    @Test
    public void testGetDeviceIsSuccessful() {
        String id = repository.save(device).toString();
        String createdDeviceId = controller.getDeviceAsJson(id);

        assertTrue(createdDeviceId.contains("Thermometer_1"));
    }

    @Test
    public void updateDeviceIsSuccessful() {
        String id = controller.createDevice(gson.create().toJson(device));
        device.setName("Pyrometer_1");
        device.setId(new ObjectId(id));
        controller.updateDevice(gson.create().toJson(device));

        Device updatedDevice = repository.get(new ObjectId(id));
        assertEquals(updatedDevice.getName(), "Pyrometer_1");
    }

    private static void initializeGson() {
        gson = new GsonBuilder();
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdSerializer());
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer());
        gson.create();
    }

    private static void deleteDevices() {
        datastore.delete(datastore.createQuery(Object.class));
    }
}

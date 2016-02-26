package smarthomevis.architecture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Device;
import smarthomevis.architecture.data_access.ObjectIdDeserializer;
import smarthomevis.architecture.data_access.ObjectIdSerializer;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.core.SmartHome;

import java.io.IOException;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class DeviceResourceTest {

    private static Datastore datastore;
    private static Repository<Device> deviceRepository;
    private static GsonBuilder gson;

    private Device device;

    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/devices/";

    @BeforeClass
    public static void initialize() throws Exception {
        datastore = new SmartHome().initializeForTesting();
        deviceRepository = new Repository<>(datastore, Device.class);
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
    public void testGetIsSuccessful() {
    }

    @Test
    public void testPutIsSuccesful() {
    }

    @Test
    public void testPostIsSuccesful() {

    }

    @Test
    public void testDeleteIsSuccesful() {

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

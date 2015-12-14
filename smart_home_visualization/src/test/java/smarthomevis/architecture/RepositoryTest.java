package smarthomevis.architecture;

import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.logic.Connector;
import smarthomevis.architecture.logic.DeviceController;
import smarthomevis.architecture.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class RepositoryTest {

    private static final String NAME_FOR_TEST_DB = "smarthome";

    private static DeviceController controller;
    private static Datastore datastore;

    private static Repository<Device> deviceRepository;
    private Device device;

    @BeforeClass
    public static void initialize() {
        controller = new DeviceController();
        deviceRepository = controller.getRepository();
        datastore = Connector.getInstance().connectToMongoDB(NAME_FOR_TEST_DB);
    }

    @Before
    public void setUp() {
        deleteDevices();
        device = new Device();
        device.setName("setUp");
    }

    @AfterClass
    public static void clear() {
        deleteDevices();
    }

    @Test
    public void testSaveAndFind() {
        String before = device.getName();
        ObjectId id = deviceRepository.save(device);
        Device device2 = deviceRepository.get(id);
        String after = device2.getName();
        Assert.assertEquals(before, after);
    }

    @Test
    public void testFindAll() {
        List<Device> list = new ArrayList<>();

        Device device1 = new Device();
        device1.setName("Source1");
        deviceRepository.save(device1);
        list.add(device1);

        Device device2 = new Device();
        device2.setName("Source2");
        ObjectId id = deviceRepository.save(device2);
        list.add(device2);

        Device device3 = new Device();
        device3.setName("Source3");
        deviceRepository.save(device3);
        list.add(device3);

        Assert.assertEquals(3, deviceRepository.getAll().size());
        Assert.assertEquals("Source2", deviceRepository.get(id).getName());
        list.forEach(Device::getName);
    }

    @Test
    public void testDeleteWithHas() {
        ObjectId id = deviceRepository.save(device);
        Assert.assertTrue(deviceRepository.has(id));
        deviceRepository.delete(id);
        Assert.assertFalse(deviceRepository.has(id));
    }

    @Test
    public void testCountWithMultipleSameObjectSaves() {
        for (int i = 0; i < 5; i++) {
            deviceRepository.save(device);
        }
        Assert.assertEquals(1, deviceRepository.count());
    }

    @Test
    public void testCountWithDifferentObjectSaves() {
        Device device1 = new Device();
        device1.setName("Source1");
        deviceRepository.save(device1);

        Device device2 = new Device();
        device2.setName("Source2");
        deviceRepository.save(device2);

        Device device3 = new Device();
        device3.setName("Source3");
        deviceRepository.save(device3);

        Assert.assertEquals(3, deviceRepository.count());
    }

    private static void deleteDevices() {
        datastore.delete(datastore.createQuery(Device.class));
    }
}

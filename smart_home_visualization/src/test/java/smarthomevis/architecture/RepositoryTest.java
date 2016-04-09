package smarthomevis.architecture;

import org.junit.*;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.logic.Device;

import static org.junit.Assert.*;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class RepositoryTest {

    private static Repository<Device> deviceRepository;
    private Device device;

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
        device = new Device();
        device.setName("Sensor12");
        device.addEntry("Movement");
    }

    @Test
    public void testSaveAndFind() {
        Device savedDevice = deviceRepository.get(deviceRepository.save(device));

        assertEquals(device.getName(), savedDevice.getName());
        assertEquals(device.getEntries(), savedDevice.getEntries());
        assertNotNull(savedDevice.getId());
    }

    @Test
    public void testFindAll() {
        deviceRepository.save(new Device());
        deviceRepository.save(new Device());
        deviceRepository.save(new Device());

        assertEquals(3, deviceRepository.getAll().size());
    }

    @Test
    public void testDeleteWithHas() {
        String deviceId = deviceRepository.save(device);

        assertTrue(deviceRepository.has(deviceId));

        deviceRepository.delete(deviceId);

        assertFalse(deviceRepository.has(deviceId));
    }

    @Test
    public void testDeleteAll() {
        deviceRepository.save(new Device());
        deviceRepository.save(new Device());
        deviceRepository.save(new Device());

        assertEquals(3, deviceRepository.getAll().size());

        deviceRepository.deleteAll();

        assertEquals(0, deviceRepository.getAll().size());
    }

    @Test
    public void testCountWithMultipleSameObjectSaves() {
        for (int i = 0; i < 5; i++) {
            deviceRepository.save(device);
        }

        assertEquals(1, deviceRepository.count());
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

        assertEquals(3, deviceRepository.count());
    }
}

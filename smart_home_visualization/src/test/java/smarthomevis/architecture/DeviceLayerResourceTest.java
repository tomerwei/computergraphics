package smarthomevis.architecture;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.entities.DeviceLayer;
import smarthomevis.architecture.rest.DeviceLayerResource;

public class DeviceLayerResourceTest {

    private static DeviceLayerResource resource;
    private static Repository<DeviceLayer> deviceLayerRepository;
    private static ClientResource client;
    private static Device device;
    private static String testUrl = "http://localhost:8183/smarthome/layers/";

    @BeforeClass
    public static void initialize() {
        resource = new DeviceLayerResource();
        deviceLayerRepository = new Repository<>(DeviceLayer.class);
    }

    @AfterClass
    public static void clear() {
        deviceLayerRepository.deleteAll();

        device = new Device();
        device.setName("Sensor_09");
        device.addEntry("Movement");
        new Repository<>(Device.class).save(device);
    }

    @Before
    public void setUp() {
        deviceLayerRepository.deleteAll();
    }
}

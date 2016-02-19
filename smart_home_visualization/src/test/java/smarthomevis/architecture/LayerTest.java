package smarthomevis.architecture;

import cgresearch.core.math.Vector;
import cgresearch.graphics.datastructures.primitives.Cylinder;
import cgresearch.graphics.scenegraph.CgNode;
import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.data_access.Layer;
import smarthomevis.architecture.core.LayerController;
import smarthomevis.architecture.data_access.Repository;
import smarthomevis.architecture.core.SmartHome;

import java.io.IOException;

@Ignore
public class LayerTest {

    private static final String NAME_FOR_TEST_DB = "smarthome";
    private static ClientResource client;
    private static Datastore datastore;
    private static String testUrl = "http://localhost:8183/smarthome/layers/";

    private CgNode node;
    private LayerController layerController;

    @BeforeClass
    public static void initialize() throws Exception {
        datastore = new SmartHome().getDatastore();
    }

    @Before
    public void setUp() {
        node = new CgNode(new Cylinder(new Vector(0.0, 0.0, 0.0), new Vector(1.0, 1.0, 1.0), 1.0), "cylinder");
        layerController = new LayerController(datastore, new SmartHome().getCgRootNode());
    }

    @AfterClass
    public static void clear() {
        cleanupDatabase();
    }

    @Test
    public void testSaveLayerToDbIsSuccessful() {
        String id = layerController.save("kitchen");
        Repository<Layer> layerRepository = new Repository<>(datastore, Layer.class);
        Assert.assertTrue(layerRepository.has(new ObjectId(id)));
    }

    @Test
    public void testSaveLayerIsSuccessful() throws IOException {
        String name = "kitchen";
        client = new ClientResource(testUrl + name);
        String id = client.put(name).getText();
        Layer layer = layerController.get(id);
        Assert.assertEquals(name, layer.getName());
    }

    @Test
    public void addDeviceToLayerIsSuccessful() throws IOException {
        String layerName = "kitchen";
        client = new ClientResource(testUrl + layerName);
        String layerId = client.put(layerName).getText();

        String deviceName = "Thermometer_1";
        client.setReference("http://localhost:8183/smarthome/devices/" + deviceName);
        String deviceId = client.put(deviceName).getText();

        client.setReference(testUrl + layerId + "/devices/" + deviceId);
        client.put(deviceId);

        Assert.assertEquals(true, layerController.get(layerId).hasDevice(new ObjectId(deviceId)));
    }

    private static void cleanupDatabase() {
        datastore.delete(datastore.createQuery(Object.class));
    }
}

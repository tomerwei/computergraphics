package smarthomevis.architecture;

import org.junit.*;
import org.mongodb.morphia.Datastore;
import org.restlet.resource.ClientResource;
import smarthomevis.architecture.logic.DataSource;
import smarthomevis.architecture.persistence.Connector;
import smarthomevis.architecture.persistence.Repository;
import smarthomevis.architecture.rest.Server;

import java.io.IOException;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class testResource {

    private static final String NAME_FOR_TEST_DB = "testDB";

    private static Datastore datastore;
    private static Repository<DataSource> dataSourceRepository;

    private DataSource dataSource;

    private static int port = 8183;
    private static ClientResource client;
    private static String testUrl = "http://localhost:8183/smarthome/datasources/";

    @BeforeClass
    public static void initialize() throws Exception {
        Server.runServer(port);
        connectToMongoDB();
    }

    @Before
    public void setUp() {
        deleteDataSources();
        dataSource = new DataSource();
        dataSource.setName("MyName");
        dataSource.setData("2015-12-24: 19.5");
    }

    @AfterClass
    public static void clear() {
        deleteDataSources();
    }

    @Test
    public void testGetWithObjectId() throws IOException {
        String objectId = dataSourceRepository.save(dataSource).toString();
        client = new ClientResource(testUrl + objectId);
        String response = client.get().getText();
        System.out.println(response);
    }

    private static void connectToMongoDB() {
        Connector connector = new Connector();
        datastore = connector.connectToMongoDB(NAME_FOR_TEST_DB);
        dataSourceRepository = new Repository<>(datastore, DataSource.class);
    }

    private static void deleteDataSources() {
        datastore.delete(datastore.createQuery(DataSource.class));
    }

}

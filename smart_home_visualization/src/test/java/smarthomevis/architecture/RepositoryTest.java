package smarthomevis.architecture;

import org.bson.types.ObjectId;
import org.junit.*;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.logic.DataSource;
import smarthomevis.architecture.persistence.Connector;
import smarthomevis.architecture.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

// This test needs MongoDB running on localhost:27017 to pass
@Ignore
public class RepositoryTest {

    private static final String NAME_FOR_TEST_DB = "testDB";

    private static Datastore datastore;
    private static Repository<DataSource> dataSourceRepository;

    private DataSource dataSource;

    @BeforeClass
    public static void initialize() {
        connectToMongoDB();
    }

    @Before
    public void setUp() {
        deleteDataSources();
        dataSource = new DataSource();
        dataSource.setName("setUp");
        dataSource.setData("Hello MongoDB");
    }

    @AfterClass
    public static void clear() {
        deleteDataSources();
    }

    @Test
    public void testSaveAndFind() {
        String before = dataSource.getName();
        ObjectId id = dataSourceRepository.save(dataSource);
        DataSource dataSource2 = dataSourceRepository.get(DataSource.class, id);
        String after = dataSource2.getName();
        Assert.assertEquals(before, after);
    }

    @Test
    public void testFindAll() {
        List<DataSource> list = new ArrayList<>();

        DataSource dataSource1 = new DataSource();
        dataSource1.setName("Source1");
        dataSource1.setData("a bunch of data");
        dataSourceRepository.save(dataSource1);
        list.add(dataSource1);

        DataSource dataSource2 = new DataSource();
        dataSource2.setName("Source2");
        dataSource2.setData("a bunch of data");
        ObjectId id = dataSourceRepository.save(dataSource2);
        list.add(dataSource2);

        DataSource dataSource3 = new DataSource();
        dataSource3.setName("Source3");
        dataSource3.setData("a bunch of data");
        dataSourceRepository.save(dataSource3);
        list.add(dataSource3);

        Assert.assertEquals(3,
                dataSourceRepository.getAll(DataSource.class).size());
        Assert.assertEquals("Source2",
                dataSourceRepository.get(DataSource.class, id).getName());
        list.forEach(DataSource::getName);
    }

    @Test
    public void testDelete() {
        ObjectId id = dataSourceRepository.save(dataSource);
        Assert.assertEquals(1, dataSourceRepository.count(DataSource.class));
        dataSourceRepository.delete(DataSource.class, id);
        Assert.assertEquals(0, dataSourceRepository.count(DataSource.class));
    }

    @Test
    public void testCountWithMultipleSameObjectSaves() {
        for (int i = 0; i < 5; i++) {
            dataSourceRepository.save(dataSource);
        }
        Assert.assertEquals(1, dataSourceRepository.count(DataSource.class));
    }

    @Test
    public void testCountWithDifferentObjectSaves() {
        DataSource dataSource1 = new DataSource();
        dataSource1.setName("Source1");
        dataSource1.setData("a bunch of data");
        dataSourceRepository.save(dataSource1);

        DataSource dataSource2 = new DataSource();
        dataSource2.setName("Source2");
        dataSource2.setData("a bunch of data");
        dataSourceRepository.save(dataSource2);

        DataSource dataSource3 = new DataSource();
        dataSource3.setName("Source3");
        dataSource3.setData("a bunch of data");
        dataSourceRepository.save(dataSource3);

        Assert.assertEquals(3, dataSourceRepository.count(DataSource.class));
    }

    @Test
    public void testEquals() {
        ObjectId id = dataSourceRepository.save(dataSource);
        DataSource dataSource2 = dataSourceRepository.get(DataSource.class, id);
        Assert.assertTrue(dataSource.equals(dataSource2));
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

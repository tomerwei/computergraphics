package smarthomevis.architecture.core;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class SmartHome extends CgApplication {

    private static final String PACKAGE_LOCATION = "cg.smart_home_visualization.src.main.java.smarthomevis.architecture";
    private static final String RESOURCES_LOCATION = "smart_home_visualization/resources.ini";
    private static final String DATABASE_NAME = "smarthome";
    private static final String TEST_DATABASE_NAME = "test_smarthome";

    private static Datastore datastore;

    public SmartHome() {
        super();
    }

    public void initialize() {
        initializeApplication();
        datastore = connectToDatabase();
    }

    public Datastore initializeForTesting() {
        return connectToTestDatabase();
    }

    public Datastore getDatastore() {
        return datastore;
    }

    private void initializeApplication() {
        ResourcesLocator.getInstance().parseIniFile(RESOURCES_LOCATION);
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(this);
        //appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }

    private Datastore connectToDatabase() {
        final MongoClient mongoDbClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage(PACKAGE_LOCATION);
        Datastore datastore = morphia.createDatastore(mongoDbClient, DATABASE_NAME);
        datastore.ensureIndexes();

        return datastore;
    }

    private Datastore connectToTestDatabase() {
        final MongoClient mongoDbClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage(PACKAGE_LOCATION);
        Datastore datastore = morphia.createDatastore(mongoDbClient, TEST_DATABASE_NAME);
        datastore.ensureIndexes();

        return datastore;
    }
}

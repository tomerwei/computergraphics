package smarthomevis.architecture;

import cgresearch.AppLauncher;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import smarthomevis.architecture.config.Configuration;

public class SmartHome extends CgApplication {

    public SmartHome() {
        super();
    }

    public static Datastore connectToDatabase() {
        return connectToMongoDB(Configuration.getDatabaseName());
    }

    public void initialize() {
        initializeApplication();
    }

    private static Datastore connectToMongoDB(String databaseName) {
        final MongoClient mongoDbClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage(Configuration.getPackageLocation());
        Datastore datastore = morphia.createDatastore(mongoDbClient, databaseName);
        datastore.ensureIndexes();

        return datastore;
    }

    private void initializeApplication() {
        ResourcesLocator.getInstance().parseIniFile(Configuration.getResourcesLocation());
        JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
        appLauncher.create(this);
        appLauncher.setRenderSystem(AppLauncher.RenderSystem.JOGL);
        appLauncher.setUiSystem(AppLauncher.UI.JOGL_SWING);
    }
}

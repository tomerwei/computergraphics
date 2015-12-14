package smarthomevis.architecture.logic;

import cgresearch.graphics.scenegraph.CgNode;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

// is a Singleton
public class Connector {

    // Connects to a MongoDB Server running on localhost:27017 by default
    private static Connector instance = null;
    private static CgNode cgRootNode;

    private static final String PACKAGE_LOCATION = "cg.smart_home_visualization.src.main.java.smarthomevis.architecture";

    public static Connector getInstance() {
        if (instance == null) {
            return new Connector();
        }
        return instance;
    }

    public Datastore connectToMongoDB(String databaseName) {
        final MongoClient mongoClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage(PACKAGE_LOCATION);
        Datastore datastore = morphia.createDatastore(mongoClient, databaseName);
        datastore.ensureIndexes();

        return datastore;
    }

    public CgNode getCgRootNode() {
        return cgRootNode;
    }

    public void setCgRootNode(CgNode cgRootNode) {
        Connector.cgRootNode = cgRootNode;
    }
}

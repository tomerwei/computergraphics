package smarthomevis.architecture.persistence;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class Connector {

    // Connects to a MongoDB Server running on localhost:27017 by default

    public Datastore connectToMongoDB(String databaseName) {
        final MongoClient mongoClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage("cg.smart_home_visualization.src.main.java.smarthomevis.architecture");

        Datastore datastore = morphia.createDatastore(mongoClient, databaseName);
        datastore.ensureIndexes();
        return datastore;
    }
}

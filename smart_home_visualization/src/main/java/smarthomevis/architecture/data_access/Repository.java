package smarthomevis.architecture.data_access;

import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import smarthomevis.architecture.config.Configuration;

import java.util.List;

public class Repository<E extends BaseEntity> {

    private final Class<E> type;
    private Datastore datastore;

    public Repository(Class<E> type) {
        connectToDatabase();
        this.type = type;
    }

    public E get(String id) {
        return datastore.find(type).field("id").equal(new ObjectId(id)).get();
    }

    public List<E> getAll() {
        return datastore.find(type).asList();
    }

    public String save(E entity) {
        datastore.save(entity);
        return entity.getId();
    }

    public void delete(String id) {
        datastore.delete(get(id));
    }

    public void deleteAll() {
        datastore.delete(datastore.createQuery(type));
    }

    public boolean has(String id) {
        return get(id) != null;
    }

    public long count() {
        return datastore.find(type).countAll();
    }

    private void connectToDatabase() {
        final MongoClient mongoDbClient = new MongoClient();
        final Morphia morphia = new Morphia();

        morphia.mapPackage(Configuration.getPackageLocation());
        Datastore datastore = morphia.createDatastore(mongoDbClient, Configuration.getDatabaseName());
        datastore.ensureIndexes();

        this.datastore = datastore;
    }

}

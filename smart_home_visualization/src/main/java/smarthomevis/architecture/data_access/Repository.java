package smarthomevis.architecture.data_access;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.SmartHome;
import smarthomevis.architecture.entities.BaseEntity;

import java.util.List;

public class Repository<E extends BaseEntity> {

    private final Class<E> type;
    private Datastore datastore;

    public Repository(Class<E> type) {
        datastore = SmartHome.connectToDatabase();
        this.type = type;
    }

    public E get(final ObjectId id) {
        return datastore.find(type).field("id").equal(id).get();
    }

    public List<E> getAll() {
        return datastore.find(type).asList();
    }

    public ObjectId save(E entity) {
        datastore.save(entity);
        return entity.getId();
    }

    public void delete(final ObjectId id) {
        E entity = datastore.find(type).field("id").equal(id).get();
        datastore.delete(entity);
    }

    public void deleteAll() {
        datastore.delete(datastore.createQuery(type));
    }

    public boolean has(final ObjectId id) {
        return get(id) != null;
    }

    public long count() {
        return datastore.find(type).countAll();
    }

}

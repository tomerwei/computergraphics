package smarthomevis.architecture.persistence;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.logic.BaseEntity;

import java.util.List;

public class Repository<E extends BaseEntity> {

    private Datastore datastore;
    private Class<E> type;

    public Repository(Datastore datastore, Class<E> type) {
        this.datastore = datastore;
        this.type = type;
    }

    public ObjectId save(E entity) {
        datastore.save(entity);
        return entity.getId();
    }

    public E get(Class<E> objectClass, final ObjectId id) {
        return datastore.find(objectClass).field("id").equal(id).get();
    }

    public List<E> getAll(Class<E> objectClass) {
        return datastore.find(objectClass).asList();
    }

    public void delete(Class<E> objectClass, final ObjectId id) {
        E entity = datastore.find(objectClass).field("id").equal(id).get();
        datastore.delete(entity);
    }

    public long count(Class<E> objectClass) {
        return datastore.find(objectClass).countAll();
    }

}

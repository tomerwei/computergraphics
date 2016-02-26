package smarthomevis.architecture.data_access;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Version;

import java.util.Date;

public abstract class BaseEntity {

    @Id
    protected ObjectId id;

    transient protected Date creationDate;
    transient protected Date lastChanged;

    @Version
    transient private long version;

    public BaseEntity() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @PrePersist
    public void prePersist() {
        creationDate = (creationDate == null) ? new Date() : creationDate;
        lastChanged = (lastChanged == null) ? creationDate : new Date();
    }

}

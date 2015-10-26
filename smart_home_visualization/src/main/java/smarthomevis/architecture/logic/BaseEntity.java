package smarthomevis.architecture.logic;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Version;

import java.util.Date;

public abstract class BaseEntity {

    @Id
    protected ObjectId id;

    protected Date creationDate;
    protected Date lastChanged;

    @Version
    private long version;

    public BaseEntity() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    @PrePersist
    public void prePersist() {
        creationDate = (creationDate == null) ? new Date() : creationDate;
        lastChanged = (lastChanged == null) ? creationDate : new Date();
    }

}

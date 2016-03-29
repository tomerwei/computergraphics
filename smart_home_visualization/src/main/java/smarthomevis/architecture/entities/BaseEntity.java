package smarthomevis.architecture.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Version;
import smarthomevis.architecture.data_access.JsonConverter;

import java.util.Date;

public abstract class BaseEntity {

    @Id
    protected ObjectId id;

    protected Date creationDate;
    protected Date lastChanged;

    protected String name;

    @Version
    private transient long version;

    public BaseEntity() {
        super();
    }

    public ObjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        creationDate = (creationDate == null) ? new Date() : creationDate;
        lastChanged = (lastChanged == null) ? creationDate : new Date();
    }
}

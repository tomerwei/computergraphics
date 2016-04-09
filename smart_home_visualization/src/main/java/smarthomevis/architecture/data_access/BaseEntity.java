package smarthomevis.architecture.data_access;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.annotations.Version;
import smarthomevis.architecture.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseEntity {

    @Id
    protected ObjectId id;

    protected String creationDate;
    protected String lastChanged;

    protected String name;

    @Version
    protected transient long version;

    public BaseEntity() {
        super();
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        setCreationDate();
        setLastChanged();
    }

    private void setLastChanged() {
        lastChanged = (lastChanged == null) ? creationDate : format(new Date());
    }

    private void setCreationDate() {
        creationDate = (creationDate == null) ? format(new Date()) : creationDate;
    }

    private String format(Date date) {
        return Configuration.getSimpleDateFormat().format(date);
    }
}

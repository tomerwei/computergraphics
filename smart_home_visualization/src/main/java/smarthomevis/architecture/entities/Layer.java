package smarthomevis.architecture.entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import smarthomevis.architecture.persistence.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity("layers")
public class Layer extends BaseEntity {

    private String name;
    private List<ObjectId> devices;

    public Layer() {
        devices = new ArrayList<>();
    }

    public void addDevice(ObjectId objectId) {
        devices.add(objectId);
    }

    public boolean hasDevice(ObjectId objectId) {
        return devices.contains(objectId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectId> getDevices() {
        return devices;
    }

    public void setDevices(List<ObjectId> device) {
        this.devices = device;
    }

    public CgNodeLayer toCgNodeLayer() {
        CgNodeLayer cgNodeLayer = new CgNodeLayer();
        cgNodeLayer.setDevices(this.devices);
        cgNodeLayer.setName(this.name);
        return cgNodeLayer;
    }

}

package smarthomevis.architecture.entities;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class DeviceLayer extends BaseEntity {

    private List<ObjectId> devices;

    public DeviceLayer() {
        super();
        devices = new ArrayList<>();
    }

    public void addDevice(ObjectId objectId) {
        devices.add(objectId);
    }

    public boolean hasDevice(ObjectId objectId) {
        return devices.contains(objectId);
    }

    public List<ObjectId> getDevices() {
        return devices;
    }

    public void setDevices(List<ObjectId> device) {
        this.devices = device;
    }
}

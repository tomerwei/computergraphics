package smarthomevis.architecture.logic;

import org.mongodb.morphia.annotations.Entity;
import smarthomevis.architecture.data_access.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity("device_layers")
public class DeviceLayer extends BaseEntity implements ILayer {

    private List<String> devices;

    public DeviceLayer() {
        devices = new ArrayList<>();
    }

    public void addDevice(String deviceId) {
        devices.add(deviceId);
    }

    public void removeDevice(String deviceId) {
        devices.remove(deviceId);
    }

    public boolean hasDevice(String deviceId) {
        return devices.contains(deviceId);
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }
}

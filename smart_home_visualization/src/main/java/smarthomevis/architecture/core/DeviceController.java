package smarthomevis.architecture.core;

import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.data_access.Device;
import smarthomevis.architecture.data_access.ObjectIdDeserializer;
import smarthomevis.architecture.data_access.ObjectIdSerializer;
import smarthomevis.architecture.data_access.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceController {

    // TODO read from config
    private static final String DATE_FORMAT = "dd/mm/yyyy h:mm:ss a";
    private SimpleDateFormat simpleDateFormat;

    private Repository<Device> deviceRepository;
    private GsonBuilder gson;

    public DeviceController(Datastore datastore) {
        deviceRepository = new Repository<>(datastore, Device.class);
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        initializeGson();
    }

    private void initializeGson() {
        gson = new GsonBuilder();
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdSerializer());
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer());
        gson.create();
    }

    public String getDeviceAsJson(String id) {
        return gson.create().toJson(getDevice(id));
    }

    public String createDevice(String deviceJson) {
        Device device = gson.create().fromJson(deviceJson, Device.class);
        return deviceRepository.save(device).toString();
    }

    public void updateDevice(String deviceJson) {
        Device device = gson.create().fromJson(deviceJson, Device.class);
        deviceRepository.get(device.getId());
        deviceRepository.save(device);
    }

    public void deleteDevice(String id) {
        deviceRepository.delete(new ObjectId(id));
    }

    public void saveMeasurement(String id, String measurement) {
        String timestamp = simpleDateFormat.format(new Date());
        getDevice(id).getDatedMeasurements().put(timestamp, measurement);
    }

    public void clearMeasurements(String id) {
        getDevice(id).getDatedMeasurements().clear();
    }

    private Device getDevice(String id) {
        return deviceRepository.get(new ObjectId(id));
    }
}

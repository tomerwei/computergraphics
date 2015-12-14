package smarthomevis.architecture.logic;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.persistence.Repository;

public class DeviceController implements Controller {

    private Repository<Device> deviceRepository;

    public DeviceController() {
        Connector connector = new Connector();
        Datastore datastore = connector.connectToMongoDB("smarthome");
        deviceRepository = new Repository<>(datastore, Device.class);
    }

    @Override
    public Device get(String objectId) {
        return deviceRepository.get(new ObjectId(objectId));
    }

    public String save(String name) {
        Device device = new Device();
        device.setName(name);
        return deviceRepository.save(device).toString();
    }

    @Override
    public void delete(String objectId) {
        deviceRepository.delete(new ObjectId(objectId));
    }

    @Override
    public Repository<Device> getRepository() {
        return deviceRepository;
    }
}

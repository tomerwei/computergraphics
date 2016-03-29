package smarthomevis.architecture.data_access;

import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import smarthomevis.architecture.entities.Device;
import smarthomevis.architecture.entities.DeviceLayer;

public class JsonConverter {

    private static GsonBuilder gson;

    public static GsonBuilder getGsonBuilder() {
        gson = new GsonBuilder();
        registerSerializers();
        registerDeserializers();
        gson.create();

        return gson;
    }

    public static String convertToJson(Device device) {
        return getGsonBuilder().create().toJson(device);
    }

    public static String convertToJson(DeviceLayer deviceLayer) {
        return getGsonBuilder().create().toJson(deviceLayer);
    }

    public static Device buildDevice(String json) {
        return getGsonBuilder().create().fromJson(json, Device.class);
    }

    public static DeviceLayer buildDeviceLayer(String json) {
        return getGsonBuilder().create().fromJson(json, DeviceLayer.class);
    }

    private static void registerSerializers() {
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdSerializer());
    }

    private static void registerDeserializers() {
        gson.registerTypeAdapter(ObjectId.class, new ObjectIdDeserializer());
    }
}

package smarthomevis.architecture.data_access;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class ObjectIdSerializer implements JsonSerializer<ObjectId> {

    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}

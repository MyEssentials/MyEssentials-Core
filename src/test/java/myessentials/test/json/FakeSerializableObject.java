package myessentials.test.json;

import com.google.gson.*;
import myessentials.json.api.SerializerTemplate;

import java.lang.reflect.Type;

public class FakeSerializableObject {

    public int x = 0;
    public int y = 0;

    public static class Serializer extends SerializerTemplate<FakeSerializableObject> {

        @Override
        public void register(GsonBuilder builder) {
            builder.registerTypeAdapter(FakeSerializableObject.class, this);
        }

        @Override
        public FakeSerializableObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();
            FakeSerializableObject obj = new FakeSerializableObject();
            obj.x = jsonObj.get("x").getAsInt();
            obj.y = jsonObj.get("y").getAsInt();
            return obj;
        }

        @Override
        public JsonElement serialize(FakeSerializableObject src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("x", src.x);
            json.addProperty("y", src.y);
            return json;
        }
    }

}

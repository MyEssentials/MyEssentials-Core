package myessentials.test.json;

import com.google.gson.*;
import myessentials.json.api.SerializerTemplate;

import java.lang.reflect.Type;

public class FakeSerializableParentObject {

    public FakeSerializableObject obj;
    public int z = 0;

    public static class Serializer extends SerializerTemplate<FakeSerializableParentObject> {

        @Override
        public void register(GsonBuilder builder) {
            // REF: The registration of child type adapters is unintuitive
            builder.registerTypeAdapter(FakeSerializableParentObject.class, this);
            new FakeSerializableObject.Serializer().register(builder);
        }

        @Override
        public FakeSerializableParentObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();
            FakeSerializableParentObject objParent = new FakeSerializableParentObject();
            objParent.obj = context.deserialize(jsonObj.get("child"), FakeSerializableObject.class);
            objParent.z = jsonObj.get("z").getAsInt();
            return objParent;
        }

        @Override
        public JsonElement serialize(FakeSerializableParentObject src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.add("child", context.serialize(src.obj));
            json.addProperty("z", src.z);
            return json;
        }
    }

}

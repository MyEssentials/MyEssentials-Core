package myessentials.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public abstract class SerializerTemplate<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    public GsonBuilder createBuilder() {
        GsonBuilder builder = new GsonBuilder();
        register(builder);
        return builder;
    }

    public abstract void register(GsonBuilder builder);
}

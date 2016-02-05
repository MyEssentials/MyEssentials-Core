package myessentials.json.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * An easy way to register all the TypeAdapters to a Gson object.
 * Extend this if you want a serializer and call the createBuilder() method when instantiating the Gson object.
 * This will diminish greatly the amount of code when instantiating Gson object with lots of Serializers.
 */
public abstract class SerializerTemplate<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    public GsonBuilder createBuilder() {
        GsonBuilder builder = new GsonBuilder();
        register(builder);
        return builder;
    }

    public abstract void register(GsonBuilder builder);
}

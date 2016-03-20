package myessentials.test.json;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import myessentials.entities.api.Volume;
import myessentials.json.api.JsonConfig;

import java.util.ArrayList;
import java.util.List;

public class FakeJsonConfig extends JsonConfig<Volume, List<Volume>> {

    public FakeJsonConfig(String path) {
        super(path, "TestConfig");
        this.gsonType = new TypeToken<List<Volume>>(){}.getType();
        this.gson = new GsonBuilder().registerTypeAdapter(Volume.class, new Volume.Serializer()).setPrettyPrinting().create();
    }

    @Override
    protected List<Volume> newList() {
        return new ArrayList<Volume>();
    }
}

package myessentials.new_new_config.backend;

import com.google.gson.*;
import myessentials.MyEssentialsCore;
import myessentials.new_new_config.data.ConfigProperty;
import myessentials.new_new_config.data.ConfigPropertyContainer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.List;

public class JsonConfigBackend extends ConfigBackend {

    private File configFile;

    public JsonConfigBackend(File jsonConfigFile) {
        this.configFile = jsonConfigFile;
    }

    private JsonObject readFile() {
        JsonObject root = null;
        try {
            FileReader reader = new FileReader(configFile);
            JsonParser parser = new JsonParser();
            root = parser.parse(reader).getAsJsonObject();
        } catch (FileNotFoundException e) {
            MyEssentialsCore.instance.LOG.error("Failed to load config file {}", configFile.getName());
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return root;
    }

    private void writeFile(ConfigPropertyContainer properties) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(ConfigPropertyContainer.class, new ConfigPropertyContainer.Serializer()).setPrettyPrinting().create();
            gson.toJson(properties, new FileWriter(configFile));
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to write to config file: {}", configFile.getName());
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    /* --- LOAD --- */

    @Override
    public void load(ConfigPropertyContainer properties) {
        JsonObject root = readFile();
        if (root == null) {
            return;
        }

        for (ConfigProperty property : properties) {
            JsonElement value = getElement(root, property);
            if (value.isJsonArray()) {
                setField(property, value.getAsJsonArray());
            } else {
                setField(property, value.getAsJsonPrimitive());
            }
        }
    }

    private void setField(ConfigProperty property, JsonPrimitive json) {
        if (json.isBoolean()) {
            property.set(json.getAsBoolean());
        } else if (json.isNumber()) {
            property.set(json.getAsNumber());
        } else if (json.isString()) {
            property.set(json.getAsString());
        }
    }

    private void setField(ConfigProperty property, JsonArray json) {
        for (int i = 0; i < json.size(); i++) {
            setField(property, json.get(i).getAsJsonPrimitive());
        }
    }

    private JsonElement getElement(JsonObject root, ConfigProperty property) {
        if (property.annotation.group() != null) {
            JsonObject group = root.get(property.annotation.group()).getAsJsonObject();
            return group.get(property.annotation.name());
        } else {
            return root.get(property.annotation.name());
        }
    }

    /* --- SAVE --- */

    @Override
    public void save(ConfigPropertyContainer properties) {
        JsonObject root = readFile();
        if (root == null) {
            writeFile(properties);
        } else {
            for(ConfigProperty property : properties) {
                if(property.annotation.group() == null) {

                }
            }
        }
    }
}

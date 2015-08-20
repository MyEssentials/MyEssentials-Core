package myessentials.new_config.backends;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import myessentials.new_config.IConfigBackend;
import myessentials.new_config.data.ConfigGroupData;
import myessentials.new_config.data.ConfigData;
import myessentials.new_config.data.ConfigPropertyData;

import java.io.*;

public class JsonConfigBackend implements IConfigBackend {
    private final File file;

    public JsonConfigBackend (final File file) {
        this.file = file;
    }

    @Override
    public void load(ConfigData data) {
        try {
            FileReader fileReader = new FileReader(file);
            JsonParser parser = new JsonParser();
            JsonElement rootE = parser.parse(fileReader);
            if (!rootE.isJsonObject()) return;
            JsonObject root = rootE.getAsJsonObject();
            for (ConfigGroupData groupData : data.getGroups()) {
                if (!root.has(groupData.name())) continue;
                loadGroup(root.getAsJsonObject(groupData.name()), groupData);
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ConfigData data) {
        JsonObject rootObj = new JsonObject();
        for (ConfigGroupData groupData : data.getGroups()) {
            rootObj.add(groupData.name(), saveGroup(groupData));
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.setIndent("  ");
            Gson gson = new Gson();
            gson.toJson(rootObj, jsonWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroup(JsonObject jsonObject, ConfigGroupData groupData) {
        // Load Properties
        if (groupData.getProperties() != null) {
            for (ConfigPropertyData propData : groupData.getProperties()) {
                if (!jsonObject.has(groupData.name())) continue;
                loadProperty(jsonObject.get(propData.name()), propData);
            }
        }
        // Load Children
        if (groupData.getChildGroups() != null) {
            for (ConfigGroupData child : groupData.getChildGroups()) {
                if (!jsonObject.has(groupData.name())) continue;
                loadGroup(jsonObject.getAsJsonObject(child.name()), child);
            }
        }
    }

    private void loadProperty(JsonElement jsonElement, ConfigPropertyData propData) {
        propData.setValue(getValue(jsonElement, propData.getType()));
    }

    private Object getValue(JsonElement element, Class<?> type) {
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            Object[] objArr = new Object[jsonArray.size()];
            for (int i=0; i<jsonArray.size(); i++) {
                objArr[i] = getValue(jsonArray.get(i), type.getComponentType());
            }
            return objArr;
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
            if (Boolean.class.isAssignableFrom(type)) {
                return jsonPrimitive.getAsBoolean();
            } else if (Number.class.isAssignableFrom(type)) {
                return jsonPrimitive.getAsNumber();
            } else if (String.class.isAssignableFrom(type)) {
                return jsonPrimitive.getAsString();
            } else if (Character.class.isAssignableFrom(type)) {
                return jsonPrimitive.getAsCharacter();
            }
        }
        // TODO Allow complex types?
        return null;
    }

    private JsonObject saveGroup(ConfigGroupData groupData) {
        JsonObject jsonGroup = new JsonObject();
        // Save Properties
        for (ConfigPropertyData propData : groupData.getProperties()) {
            JsonElement e = saveProperty(propData);
            if (e == null) continue;
            jsonGroup.add(propData.name(), e);
        }
        // Save Children
        for (ConfigGroupData child : groupData.getChildGroups()) {
            jsonGroup.add(child.name(), saveGroup(child));
        }
        return jsonGroup;
    }

    private JsonElement saveProperty(ConfigPropertyData propData) {
        return saveValue(propData.getValue());
    }

    private JsonElement saveValue(Object val) {
        if (val.getClass().isArray()) {
            Object[] a = (Object[]) val;
            JsonArray jsonArray = new JsonArray();
            for (Object o : a) {
                jsonArray.add(saveValue(o));
            }
        } else if (isPrimitive(val)) {
            if (val instanceof Boolean) {
                return new JsonPrimitive((Boolean) val);
            } else if (val instanceof Number) {
                return new JsonPrimitive((Number) val);
            } else if (val instanceof String) {
                return new JsonPrimitive((String) val);
            } else if (val instanceof Character) {
                return new JsonPrimitive((Character) val);
            }
        }
        // TODO Allow complex types?
        return null;
    }

    private boolean isPrimitive(Object target) {
        if (target instanceof String) {
            return true;
        }

        Class<?> classOfPrimitive = target.getClass();
        for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
            if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
                return true;
            }
        }
        return false;
    }

    private static final Class<?>[] PRIMITIVE_TYPES = {
            int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class,
            Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
}

package myessentials.new_new_config.data;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigPropertyContainer extends ArrayList<ConfigProperty> {

    public ConfigPropertyContainer() {
    }

    public ConfigPropertyContainer(ConfigPropertyContainer other) {
        this.addAll(other);
    }

    public void removeGroup(String groupName) {
        for (Iterator<ConfigProperty> it = iterator(); it.hasNext(); ) {
            ConfigProperty property = it.next();
            if (property.annotation.group().equals(groupName)) {
                it.remove();
            }
        }
    }

    public ConfigPropertyContainer getAllFromGroup(String groupName) {
        ConfigPropertyContainer container = new ConfigPropertyContainer();
        for (ConfigProperty property : this) {
            if (property.annotation.group().equals(groupName)) {
                container.add(property);
            }
        }
        return container;
    }

    public static class Serializer implements JsonSerializer<ConfigPropertyContainer> {

        @Override
        public JsonElement serialize(ConfigPropertyContainer container, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            for (ConfigProperty property : container) {
                if (property.annotation.group() == null) {
                    json.add(property.annotation.name(), getElement(property));
                } else if (!json.has(property.annotation.group())) {
                    json.add(property.annotation.name(), getGroup(property.annotation.group(), container));
                }
            }
            return json;
        }

        private JsonElement getElement(ConfigProperty property) {
            if (property.getType().isArray()) {
                JsonArray array = new JsonArray();
                Object[] values = property.getAsArray();

                for (Object value : values) {
                    array.add(getPrimitive(value));
                }
                return array;
            } else {
                return getPrimitive(property.get());
            }
        }

        private JsonPrimitive getPrimitive(Object value) {
            JsonPrimitive json = null;
            if (value.getClass().isAssignableFrom(Number.class)) {
                json = new JsonPrimitive((Number) value);
            } else if (value.getClass().isAssignableFrom(Boolean.class)) {
                json = new JsonPrimitive((Boolean) value);
            } else if (value.getClass().isAssignableFrom(String.class)) {
                json = new JsonPrimitive((String) value);
            } else if (value.getClass().isAssignableFrom(Character.class)) {
                json = new JsonPrimitive((Character) value);
            }
            return json;
        }

        private JsonObject getGroup(String groupName, ConfigPropertyContainer container) {
            JsonObject json = new JsonObject();
            ConfigPropertyContainer groupContainer = container.getAllFromGroup(groupName);
            for (ConfigProperty property : groupContainer) {
                json.add(property.annotation.name(), getElement(property));
            }
            return json;
        }
    }
}

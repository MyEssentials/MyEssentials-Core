package myessentials.new_new_config;

import myessentials.new_new_config.backend.ConfigBackend;
import myessentials.new_new_config.backend.ForgeConfigBackend;
import myessentials.new_new_config.backend.JsonConfigBackend;
import myessentials.new_new_config.data.ConfigProperty;
import myessentials.new_new_config.data.ConfigPropertyContainer;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private ConfigBackend backend;

    public Config(Configuration config) {
        this.backend = new ForgeConfigBackend(config);
    }

    public Config(File config) {
        this.backend = new JsonConfigBackend(config);
    }

    public void load(Object instance) {

    }

    private List<ConfigProperty> getProperties(Object instance) throws IllegalAccessException {
        ConfigPropertyContainer properties = new ConfigPropertyContainer();
        for (Field field : instance.getClass().getFields()) {
            if (field.isAnnotationPresent(ConfigField.class)) {
                field.setAccessible(true);
                properties.add(new ConfigProperty(field, field.getAnnotation(ConfigField.class), field.get(instance)));
            }
        }
        return properties;
    }
}

package myessentials.config.api;

import myessentials.MyEssentialsCore;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Config class which contains most of the basic functionality needed in a config.
 * All the fields of the type ConfigProperty inside the extended class are automatically added to the loading list.
 * Other ConfigProperties can be added externally.
 */
public abstract class ConfigTemplate {

    protected String modID;
    protected Configuration config;
    protected List<ConfigProperty> properties = new ArrayList<ConfigProperty>();

    public void init(String forgeConfigPath, String modID) {
        init(new File(forgeConfigPath), modID);
    }

    public void init(File forgeConfigFile, String modID) {
        init(new Configuration(forgeConfigFile), modID);
    }

    public void init(Configuration forgeConfig, String modID) {
        this.config = forgeConfig;
        this.modID = modID;
        bind();
        reload();
    }

    public Configuration getForgeConfig() {
        return this.config;
    }

    public String getModID() {
        return this.modID;
    }

    /**
     * Adds a ConfigProperty instance which can then be loaded/saved in the config.
     */
    public void addBinding(ConfigProperty property) {
        addBinding(property, false);
    }

    /**
     * Adds a ConfigProperty instance which can then be loaded/saved in the config.
     * It will automatically reload if specified.
     */
    public void addBinding(ConfigProperty property, boolean reload) {
        this.properties.add(property);
        if (reload) {
            reload();
        }
    }

    /**
     * Loads the config file from the hard drive
     */
    public void reload() {
        config.load();
        for (ConfigProperty property : properties) {
            ConfigCategory category = config.getCategory(property.category);
            Property forgeProp;
            if (!category.containsKey(property.name)) {
                forgeProp = new Property(property.name, property.get().toString(), property.getType());
                forgeProp.comment = property.comment;
                category.put(property.name, forgeProp);
            } else {
                forgeProp = category.get(property.name);
                forgeProp.comment = property.comment;
            }
            setProperty(property, forgeProp);
        }
        config.save();
    }

    private void bind() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(ConfigProperty.class)) {
                try {
                    properties.add((ConfigProperty)field.get(this));
                } catch (IllegalAccessException ex) {
                    MyEssentialsCore.instance.LOG.error("Failed to access " + field.getName() + " while binding to config " + config.getConfigFile().getName());
                    MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setProperty(ConfigProperty property, Property forgeProp) {
        try {
            if (property.isClassType(Integer.class)) {
                property.set(forgeProp.getInt());
            } else if (property.isClassType(Double.class)) {
                property.set(forgeProp.getDouble());
            } else if (property.isClassType(Boolean.class)) {
                property.set(forgeProp.getBoolean());
            } else if (property.isClassType(String.class)) {
                property.set(forgeProp.getString());
            } else if (property.isClassType(Integer[].class)) {
                property.set(forgeProp.getIntList());
            } else if (property.isClassType(Double[].class)) {
                property.set(forgeProp.getDoubleList());
            } else if (property.isClassType(Boolean[].class)) {
                property.set(forgeProp.getBooleanList());
            } else if (property.isClassType(String[].class)) {
                property.set(forgeProp.getStringList());
            }
        } catch (RuntimeException ex) {
            MyEssentialsCore.instance.LOG.error("Config value of " + property.name + " in category " + property.category + " was not of the proper type!");
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }
}

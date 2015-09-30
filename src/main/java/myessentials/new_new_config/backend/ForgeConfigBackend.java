package myessentials.new_new_config.backend;

import com.google.common.collect.ImmutableMap;
import myessentials.new_new_config.data.ConfigProperty;
import myessentials.new_new_config.data.ConfigPropertyContainer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ForgeConfigBackend extends ConfigBackend {

    private Configuration forgeConfig;

    public ForgeConfigBackend(Configuration forgeConfig) {
        this.forgeConfig = forgeConfig;
    }

    public ForgeConfigBackend(File forgeConfigFile) {
        this.forgeConfig = new Configuration(forgeConfigFile);
    }

    @Override
    public void load(ConfigPropertyContainer properties) {
        for (ConfigProperty property : properties) {
            String group = property.annotation.group();
            String name = property.annotation.name();

            ConfigCategory category = forgeConfig.getCategory(group);
            setField(property, category.get(name));
        }
    }

    private void setField(ConfigProperty property, Property forgeProp) {
        switch (forgeProp.getType()) {
            case INTEGER:
                if (property.getType().isArray()) {
                    property.set(forgeProp.getIntList());
                } else {
                    property.set(forgeProp.getInt());
                }
                break;
            case BOOLEAN:
                if (property.getType().isArray()) {
                    property.set(forgeProp.getBooleanList());
                } else {
                    property.set(forgeProp.getBoolean());
                }
                break;
            case DOUBLE:
                if (property.getType().isArray()) {
                    property.set(forgeProp.getDoubleList());
                } else {
                    property.set(forgeProp.getDouble());
                }
                break;
            case STRING:
                if (property.getType().isArray()) {
                    property.set(forgeProp.getStringList());
                } else {
                    property.set(forgeProp.getString());
                }
                break;
        }
    }

    @Override
    public void save(ConfigPropertyContainer properties) {
        for (ConfigProperty property : properties) {
            ConfigCategory category = forgeConfig.getCategory(property.annotation.group());

            Property forgeProp = category.get(property.annotation.name());
            if(forgeProp == null) {
                if(property.getType().isArray()) {
                    forgeProp = new Property(property.annotation.name(), property.getAsString(), CONFIG_TYPES.get(property.getType()));
                } else {
                    forgeProp = new Property(property.annotation.name(), property.getAsStringArray(), CONFIG_TYPES.get(property.getType()));
                }
                category.put(property.annotation.name(), forgeProp);
            }

            forgeProp.comment = property.annotation.comment();
            setForgePropertyValue(forgeProp, property);
        }
    }

    private void setForgePropertyValue(Property forgeProp, ConfigProperty propData) {
        Property.Type type = CONFIG_TYPES.get(propData.getType());
        if (type == null) return;
        switch(type) {
            case INTEGER:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((int[]) propData.get());
                } else {
                    forgeProp.setValue((Integer) propData.get());
                }
                break;
            case DOUBLE:
                if (propData.getType().equals(Float.class) || propData.getType().equals(float.class)) {
                    if (propData.getType().isArray()) {
                        float[] vals = (float[]) propData.get();
                        double[] dVals = new double[vals.length];
                        for (int i=0; i<vals.length; i++) {
                            dVals[i] = vals[i];
                        }
                        forgeProp.setValues(dVals);
                    } else {
                        forgeProp.setValue((Float) propData.get());
                    }
                } else {
                    if (propData.getType().isArray()) {
                        forgeProp.setValues((double[]) propData.get());
                    } else {
                        forgeProp.setValue((Double) propData.get());
                    }
                }
                break;
            case BOOLEAN:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((boolean[]) propData.get());
                } else {
                    forgeProp.setValue((Boolean) propData.get());
                }
                break;
            case STRING:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((String[]) propData.get());
                } else {
                    forgeProp.setValue((String) propData.get());
                }
                break;
        }
    }

    /**
     * Maps classes to the appropriate Property.Type
     */
    private static final Map<Class<?>, Property.Type> CONFIG_TYPES = ImmutableMap.<Class<?>, Property.Type> builder().put(Integer.class, Property.Type.INTEGER)
            .put(int.class, Property.Type.INTEGER).put(Integer[].class, Property.Type.INTEGER)
            .put(int[].class, Property.Type.INTEGER).put(Double.class, Property.Type.DOUBLE)
            .put(double.class, Property.Type.DOUBLE).put(Double[].class, Property.Type.DOUBLE)
            .put(double[].class, Property.Type.DOUBLE).put(Float.class, Property.Type.DOUBLE)
            .put(float.class, Property.Type.DOUBLE).put(Float[].class, Property.Type.DOUBLE)
            .put(float[].class, Property.Type.DOUBLE).put(Boolean.class, Property.Type.BOOLEAN)
            .put(boolean.class, Property.Type.BOOLEAN).put(Boolean[].class, Property.Type.BOOLEAN)
            .put(boolean[].class, Property.Type.BOOLEAN).put(String.class, Property.Type.STRING)
            .put(String[].class, Property.Type.STRING).build();
}

package myessentials.new_config.backends;

import com.google.common.collect.ImmutableMap;
import myessentials.new_config.IConfigBackend;
import myessentials.new_config.data.ConfigGroupData;
import myessentials.new_config.data.ConfigData;
import myessentials.new_config.data.ConfigPropertyData;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.Map;

public class ForgeConfigBackend implements IConfigBackend {
    private final Configuration forgeConfig;

    public ForgeConfigBackend(final Configuration forgeConfig) {
        this.forgeConfig = forgeConfig;
    }

    @Override
    public void load(ConfigData data) {
        for (ConfigGroupData groupData : data.getGroups()) {
            loadGroup(null, groupData);
        }
    }

    @Override
    public void save(ConfigData data) {
        for (ConfigGroupData groupData : data.getGroups()) {
            saveGroup(null, groupData);
        }
        forgeConfig.save();
    }

    private void loadGroup(String parentName, ConfigGroupData groupData) {
        String catName = (parentName == null ? "" : (parentName + ".")) + groupData.name();
        ConfigCategory forgeCat = forgeConfig.getCategory(catName);
        forgeCat.setComment(groupData.comment());
        // Load Properties
        if (groupData.getProperties() != null) {
            for (ConfigPropertyData propData : groupData.getProperties()) {
                loadProperty(forgeCat, propData);
            }
        }
        // Load Children
        if (groupData.getChildGroups() != null) {
            for (ConfigGroupData childGroup : groupData.getChildGroups()) {
                loadGroup(catName, childGroup);
            }
        }
    }

    private void loadProperty(ConfigCategory cat, ConfigPropertyData propData) {
        if (cat.containsKey(propData.name())) {
            Property forgeProp = cat.get(propData.name());
            forgeProp.comment = propData.comment();
            setForgePropertyDefaultValue(forgeProp, propData);
            propData.setValue(getForgePropertyValue(forgeProp, propData));
        } else {
            Property forgeProp = makeProperty(propData);
            if (forgeProp == null) return;
            forgeProp.comment = propData.comment();
            setForgePropertyDefaultValue(forgeProp, propData);
            cat.put(forgeProp.getName(), forgeProp);
        }
    }

    private void saveGroup(String parentName, ConfigGroupData groupData) {
        String catName = (parentName == null ? "" : (parentName + ".")) + groupData.name();
        ConfigCategory forgeCat = forgeConfig.getCategory(catName);
        forgeCat.setComment(groupData.comment());
        // Save Properties
        if (groupData.getProperties() != null) {
            for (ConfigPropertyData propData : groupData.getProperties()) {
                saveProperty(forgeCat, propData);
            }
        }
        // Save Children
        if (groupData.getChildGroups() != null) {
            for (ConfigGroupData childGroup : groupData.getChildGroups()) {
                saveGroup(catName, childGroup);
            }
        }
    }

    private void saveProperty(ConfigCategory cat, ConfigPropertyData propData) {
        if (cat.containsKey(propData.name())) {
            Property forgeProp = cat.get(propData.name());
            forgeProp.comment = propData.comment();
            setForgePropertyDefaultValue(forgeProp, propData);
            setForgePropertyValue(forgeProp, propData);
        } else {
            Property forgeProp = makeProperty(propData);
            if (forgeProp == null) return;
            forgeProp.comment = propData.comment();
            setForgePropertyDefaultValue(forgeProp, propData);
            cat.put(forgeProp.getName(), forgeProp);
        }
    }

    private void setForgePropertyDefaultValue(Property forgeProp, ConfigPropertyData propData) {
        Property.Type type = CONFIG_TYPES.get(propData.getType());
        if (type == null) return;
        switch(type) {
            case INTEGER:
                if (propData.getType().isArray()) {
                    forgeProp.setDefaultValues((int[]) propData.getDefaultValue());
                } else {
                    forgeProp.setDefaultValue((Integer) propData.getDefaultValue());
                }
                break;
            case DOUBLE:
                if (propData.getType().equals(Float.class) || propData.getType().equals(float.class)) {
                    if (propData.getType().isArray()) {
                        float[] vals = (float[]) propData.getDefaultValue();
                        double[] dVals = new double[vals.length];
                        for (int i=0; i<vals.length; i++) {
                            dVals[i] = vals[i];
                        }
                        forgeProp.setDefaultValues(dVals);
                    } else {
                        forgeProp.setDefaultValue((Float) propData.getDefaultValue());
                    }
                } else {
                    if (propData.getType().isArray()) {
                        forgeProp.setDefaultValues((double[]) propData.getDefaultValue());
                    } else {
                        forgeProp.setDefaultValue((Double) propData.getDefaultValue());
                    }
                }
                break;
            case BOOLEAN:
                if (propData.getType().isArray()) {
                    forgeProp.setDefaultValues((boolean[]) propData.getDefaultValue());
                } else {
                    forgeProp.setDefaultValue((Boolean) propData.getDefaultValue());
                }
                break;
            case STRING:
                if (propData.getType().isArray()) {
                    forgeProp.setDefaultValues((String[]) propData.getDefaultValue());
                } else {
                    forgeProp.setDefaultValue((String) propData.getDefaultValue());
                }
                break;
        }
    }

    private void setForgePropertyValue(Property forgeProp, ConfigPropertyData propData) {
        Property.Type type = CONFIG_TYPES.get(propData.getType());
        if (type == null) return;
        switch(type) {
            case INTEGER:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((int[]) propData.getValue());
                } else {
                    forgeProp.setValue((Integer) propData.getValue());
                }
                break;
            case DOUBLE:
                if (propData.getType().equals(Float.class) || propData.getType().equals(float.class)) {
                    if (propData.getType().isArray()) {
                        float[] vals = (float[]) propData.getValue();
                        double[] dVals = new double[vals.length];
                        for (int i=0; i<vals.length; i++) {
                            dVals[i] = vals[i];
                        }
                        forgeProp.setValues(dVals);
                    } else {
                        forgeProp.setValue((Float) propData.getValue());
                    }
                } else {
                    if (propData.getType().isArray()) {
                        forgeProp.setValues((double[]) propData.getValue());
                    } else {
                        forgeProp.setValue((Double) propData.getValue());
                    }
                }
                break;
            case BOOLEAN:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((boolean[]) propData.getValue());
                } else {
                    forgeProp.setValue((Boolean) propData.getValue());
                }
                break;
            case STRING:
                if (propData.getType().isArray()) {
                    forgeProp.setValues((String[]) propData.getValue());
                } else {
                    forgeProp.setValue((String) propData.getValue());
                }
                break;
        }
    }

    private Object getForgePropertyValue(Property forgeProp, ConfigPropertyData propData) {
        Property.Type type = CONFIG_TYPES.get(propData.getType());
        if (type == null) return null;
        switch(type) {
            case INTEGER:
                if (propData.getType().isArray()) {
                    return forgeProp.getIntList();
                } else {
                    return forgeProp.getInt();
                }
            case DOUBLE:
                if (propData.getType().equals(Float.class) || propData.getType().equals(float.class)) {
                    if (propData.getType().isArray()) {
                        double[] vals = forgeProp.getDoubleList();
                        float[] ret = new float[vals.length];
                        for (int i = 0; i<vals.length; i++) {
                            ret[i] = (float) vals[i];
                        }
                        return ret;
                    } else {
                        return (float) forgeProp.getDouble();
                    }
                } else {
                    if (propData.getType().isArray()) {
                        return forgeProp.getDoubleList();
                    } else {
                        return forgeProp.getDouble();
                    }
                }
            case BOOLEAN:
                if (propData.getType().isArray()) {
                    return forgeProp.getBooleanList();
                } else {
                    return forgeProp.getBoolean();
                }
            case STRING:
                if (propData.getType().isArray()) {
                    return forgeProp.getStringList();
                } else {
                    return forgeProp.getString();
                }
        }

        return null;
    }

    private Property makeProperty(ConfigPropertyData propData) {
        Property.Type type = CONFIG_TYPES.get(propData.getType());
        if (type == null) return null;
        if (!propData.getType().isArray()) {
            Property forgeProp = new Property(propData.name(), String.valueOf(propData.getValue()), type);
            forgeProp.set(String.valueOf(propData.getValue()));
            return forgeProp;
        }

        switch(type) {
            case INTEGER: {
                Integer[] val = (Integer[]) propData.getValue();
                String[] propVal = new String[val.length];
                for (int i = 0; i < val.length; i++)
                    propVal[i] = String.valueOf(val[i]);
                Property forgeProp = new Property(propData.name(), propVal, type);
                forgeProp.set(propVal);
                return forgeProp;
            }
            case DOUBLE:
                if (propData.getType().equals(Float.class) || propData.getType().equals(float.class)) {
                    Float[] val = (Float[]) propData.getValue();
                    String[] propVal = new String[val.length];
                    for (int i = 0; i < val.length; i++)
                        propVal[i] = String.valueOf(val[i]);
                    Property forgeProp = new Property(propData.name(), propVal, type);
                    forgeProp.set(propVal);
                    return forgeProp;
                } else {
                    Double[] val = (Double[]) propData.getValue();
                    String[] propVal = new String[val.length];
                    for (int i = 0; i < val.length; i++)
                        propVal[i] = String.valueOf(val[i]);
                    Property forgeProp = new Property(propData.name(), propVal, type);
                    forgeProp.set(propVal);
                    return forgeProp;
                }
            case BOOLEAN: {
                Boolean[] val = (Boolean[]) propData.getValue();
                String[] propVal = new String[val.length];
                for (int i = 0; i < val.length; i++)
                    propVal[i] = String.valueOf(val[i]);
                Property forgeProp = new Property(propData.name(), propVal, type);
                forgeProp.set(propVal);
                return forgeProp;
            }
            case STRING:
                String[] propVal = (String[]) propData.getValue();
                Property forgeProp = new Property(propData.name(), propVal, type);
                forgeProp.set(propVal);
                return forgeProp;
        }

        return null;
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

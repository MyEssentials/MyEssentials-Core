package myessentials.new_config;

import myessentials.new_config.annotations.ConfigGroup;
import myessentials.new_config.annotations.ConfigProperty;
import myessentials.new_config.backends.ForgeConfigBackend;
import myessentials.new_config.backends.JsonConfigBackend;
import myessentials.new_config.data.ConfigGroupData;
import myessentials.new_config.data.ConfigPropertyData;
import myessentials.new_config.data.ConfigData;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigProcessor {
    public static ConfigData load(Class<?> clazz, Configuration config) {
        return load(clazz, new ForgeConfigBackend(config));
    }

    public static ConfigData load(Class<?> clazz, File configFile) {
        return load(clazz, new JsonConfigBackend(configFile));
    }

    public static ConfigData load(Class<?> clazz, IConfigBackend backend) {
        Map<String, ConfigGroupData> groups = genGroups(clazz, null, backend);
        ConfigData cfgData = new ConfigData(groups, backend);
        cfgData.load();
        return cfgData;
    }

    private static Map<String, ConfigGroupData> genGroups(Class<?> clazz, String parentFullName, IConfigBackend backend) {
        Map<String, ConfigGroupData> groupsMap = new HashMap<String, ConfigGroupData>();
        for (Class<?> c : clazz.getClasses()) {
            ConfigGroup groupAnnot = getConfigGroupAnnotation(c);
            if (groupAnnot == null) continue;
            String groupName = (groupAnnot.name() == null || groupAnnot.name().trim().isEmpty()) ? c.getSimpleName() : groupAnnot.name();
            String groupFullName = (parentFullName != null ? (parentFullName + ".") : "") + groupName;

            // load Properties
            Map<String, ConfigPropertyData> props = genProps(c, groupFullName, backend);

            // Load Children
            Map<String, ConfigGroupData> children = null;
            if (c.getClasses().length > 0) children = genGroups(c, groupFullName, backend);

            // Load Sub-Classes
            if (groupAnnot.classes() != null && groupAnnot.classes().length > 0) {
                for (Class<?> subClazz : groupAnnot.classes()) {
                    if (subClazz.getClasses().length > 0) children.putAll(genGroups(subClazz, groupFullName, backend));
                    props.putAll(genProps(subClazz, groupFullName, backend));
                }
            }
        }
        return groupsMap;
    }

    private static Map<String, ConfigPropertyData> genProps(Class<?> clazz, String groupFullName, IConfigBackend backend) {
        Map<String, ConfigPropertyData> propertiesMap = new HashMap<String, ConfigPropertyData>();
        for (Field field : clazz.getFields()) {
            ConfigProperty propAnnot = field.getAnnotation(ConfigProperty.class);
            if (propAnnot == null) continue;
            String propName = (propAnnot.name() == null || propAnnot.name().trim().isEmpty()) ? field.getName() : propAnnot.name();
            String propFullName = groupFullName + "." + propName;
            ConfigPropertyData prop = new ConfigPropertyData(field, propName, propFullName, propAnnot.comment(), propAnnot.command());
            // TODO Load from backend HERE
            propertiesMap.put(propName, prop);
        }
        return propertiesMap;
    }

    private static ConfigGroup getConfigGroupAnnotation(Class<?> clazz) {
        ConfigGroup annot = null;
        if (clazz.isAnnotationPresent(ConfigGroup.class)) {
            annot = clazz.getAnnotation(ConfigGroup.class);
        } else if (ConfigGroup.class.isAssignableFrom(clazz)) {
            try {
                annot = (ConfigGroup) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return annot;
    }
}

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

/**
 */
public class ConfigProcessor {
    public static IConfigBackend load(Class<?> clazz, Configuration config) {
        ConfigData data = generateConfigTree(clazz);
        ForgeConfigBackend configBackend = new ForgeConfigBackend(config, data);
        configBackend.load();
        return configBackend;
    }

    public static IConfigBackend load(Class<?> clazz, File configFile) {
        ConfigData data = generateConfigTree(clazz);
        JsonConfigBackend configBackend = new JsonConfigBackend(configFile, data);
        configBackend.load();
        return configBackend;
    }

    public static ConfigData generateConfigTree(Class<?> clazz) {
        return new ConfigData(generateGroupsMap(clazz));
    }

    private static Map<String, ConfigGroupData> generateGroupsMap(Class<?> clazz) {
        Map<String, ConfigGroupData> groupsMap = new HashMap<String, ConfigGroupData>();
        for (Class<?> c : clazz.getClasses()) {
            ConfigGroup groupAnnot = getConfigGroupAnnotation(c);
            if (groupAnnot == null) continue;
            String groupName = (groupAnnot.name() == null || groupAnnot.name().trim().isEmpty()) ? c.getSimpleName() : groupAnnot.name();
            Map<String, ConfigGroupData> children = null;
            if (c.getClasses().length > 0) children = generateGroupsMap(c);
            Map<String, ConfigPropertyData> properties = generatePropertiesMap(c);

            if (groupAnnot.classes() != null && groupAnnot.classes().length > 0) {
                for (Class<?> subClazz : groupAnnot.classes()) {
                    if (subClazz.getClasses().length > 0) children.putAll(generateGroupsMap(subClazz));
                    properties.putAll(generatePropertiesMap(subClazz));
                }
            }

            groupsMap.put(groupName, new ConfigGroupData(groupName, groupAnnot.comment(), children, properties));
        }
        return groupsMap;
    }

    private static Map<String, ConfigPropertyData> generatePropertiesMap(Class<?> clazz) {
        Map<String, ConfigPropertyData> propertiesMap = new HashMap<String, ConfigPropertyData>();
        for (Field field : clazz.getFields()) {
            ConfigProperty propAnnot = field.getAnnotation(ConfigProperty.class);
            if (propAnnot == null) continue;
            String propName = (propAnnot.name() == null || propAnnot.name().trim().isEmpty()) ? field.getName() : propAnnot.name();
            propertiesMap.put(propName, new ConfigPropertyData(field, propName, propAnnot.comment(), propAnnot.command()));
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

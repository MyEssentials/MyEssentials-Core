package myessentials.new_config.data;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ConfigGroupData {
    private final String name;
    private final String comment;

    private final ImmutableMap<String, ConfigGroupData> childGroups;
    private final ImmutableMap<String, ConfigPropertyData> properties;

    public ConfigGroupData(String name, String comment, Map<String, ConfigGroupData> childGroups, Map<String, ConfigPropertyData> properties) {
        this.name = name;
        this.comment = comment;
        this.childGroups = childGroups == null ? null : ImmutableMap.copyOf(childGroups);
        this.properties = properties == null ? null : ImmutableMap.copyOf(properties);
    }

    public String name() {
        return name;
    }

    public String comment() {
        return comment;
    }

    public ConfigGroupData getChildGroup(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        int index = name.indexOf(".");
        String groupName = name.substring(0, index);
        String rest = name.substring(index+1);
        ConfigGroupData group = childGroups.get(groupName);
        if (rest.trim().isEmpty()) {
            return group;
        } else {
            return group.getChildGroup(rest);
        }
    }

    public ConfigPropertyData getProperty(String name) {
        if (name == null) return null;
        return properties.get(name);
    }

    public ImmutableCollection<ConfigPropertyData> getProperties() {
        if (properties == null) return null;
        return properties.values();
    }

    public ImmutableCollection<ConfigGroupData> getChildGroups() {
        if (childGroups == null) return null;
        return childGroups.values();
    }
}

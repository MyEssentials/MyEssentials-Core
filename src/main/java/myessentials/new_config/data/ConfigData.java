package myessentials.new_config.data;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ConfigData {
    private final ImmutableMap<String, ConfigGroupData> configGroups;

    public ConfigData(Map<String, ConfigGroupData> configGroups) {
        this.configGroups = ImmutableMap.copyOf(configGroups);
    }

    public ConfigGroupData getConfigGroup(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        int indexOfDot = name.indexOf(".");
        String groupName = name.substring(0, indexOfDot);
        String rest = name.substring(indexOfDot+1);
        ConfigGroupData group = configGroups.get(groupName);
        if (rest.trim().isEmpty()) {
            return group;
        } else {
            return group.getChildGroup(rest);
        }
    }

    public ConfigPropertyData getConfigPropertyData(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        int lastIndexOfDot = name.lastIndexOf(".");
        String groupName = name.substring(0, lastIndexOfDot);
        String propName = name.substring(lastIndexOfDot+1);
        ConfigGroupData group = getConfigGroup(groupName);
        if (group == null) return null;
        return group.getProperty(propName);
    }

    public ImmutableCollection<ConfigGroupData> getGroups() {
        return configGroups.values();
    }
}

package myessentials.new_config;

import myessentials.new_config.data.ConfigData;

public interface IConfigBackend {
    void load(ConfigData data);

    void save(ConfigData data);
}

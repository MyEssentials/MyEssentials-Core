package myessentials.new_new_config.backend;

import myessentials.new_new_config.data.ConfigPropertyContainer;

public abstract class ConfigBackend {

    /**
     * Sets the values of the fields found in the properties from the config file.
     * If some properties aren't found it will add them to the config.
     */
    public abstract void load(ConfigPropertyContainer properties);

    /**
     * Saves all the values of the fields in the config file.
     */
    public abstract void save(ConfigPropertyContainer properties);
}

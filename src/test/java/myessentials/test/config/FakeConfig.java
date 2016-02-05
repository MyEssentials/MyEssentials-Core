package myessentials.test.config;

import myessentials.config.api.ConfigProperty;
import myessentials.config.api.ConfigTemplate;

import java.util.List;

public class FakeConfig extends ConfigTemplate {
    public List<ConfigProperty> getProperties() {
        return properties;
    }

    public ConfigProperty<Boolean> instanceProp = new ConfigProperty<Boolean>(
            "testingTheTestInstance", "tests",
            "This should be loaded properly",
            true);
}

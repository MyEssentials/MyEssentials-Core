package myessentials.test.config;


import junit.framework.Assert;
import metest.MinecraftRunner;
import myessentials.config.ConfigProperty;
import myessentials.test.MECTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(MinecraftRunner.class)
public class ConfigTest extends MECTest {

    private FakeConfig config;

    @Before
    public void initConfig() {
        // REF: Move this to METEST when it's needed
        File configDir = configFile.getParentFile();
        File testDir = new File(configDir, "/tests");
        if(!testDir.exists() || !testDir.isDirectory()) {
            testDir.mkdir();
        }
        File testConfigFile = new File(testDir, "/TestConfig.cfg");

        config = new FakeConfig();
        config.init(testConfigFile, "MyEssentials-Core");
    }

    @Test
    public void shouldAddProperty() {
        ConfigProperty<String> prop = new ConfigProperty<String>(
                "testingTheTest", "tests1",
                "Probably not needed",
                "BETTER");

        config.addBinding(prop, true);
        // REF: Add a resource folder to include premade config for loading
        Assert.assertEquals("ConfigProperty should not have changed after reloading", prop.get(), "BETTER");
    }
}

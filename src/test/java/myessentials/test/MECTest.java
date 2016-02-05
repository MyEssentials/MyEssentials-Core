package myessentials.test;

import metest.api.BaseTest;
import metest.core.Constants;
import org.junit.Before;

public class MECTest extends BaseTest {

    @Before
    public void initConfig() {
        TestConfig.instance.init(configFile, Constants.MOD_ID);
    }

}

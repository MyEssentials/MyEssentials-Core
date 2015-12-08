package myessentials.test;

import metest.BaseTest;
import metest.Constants;
import org.junit.Before;

public class MECTest extends BaseTest {

    @Before
    public void initConfig() {
        TestConfig.instance.init(configFile, Constants.MOD_ID);
    }

}

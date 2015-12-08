package myessentials.test;

import myessentials.test.config.ConfigTest;
import myessentials.test.datasource.DatasourceTest;
import myessentials.test.economy.EconomyTest;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({EconomyTest.class, DatasourceTest.class, ConfigTest.class})
public class MECTestSuite {

    @AfterClass
    public static void tearDown() {
        throw new RuntimeException("STAHP");
    }
}

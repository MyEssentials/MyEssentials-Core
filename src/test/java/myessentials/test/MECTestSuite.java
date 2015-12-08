package myessentials.test;

import metest.BaseSuite;
import metest.MinecraftServerStarter;
import myessentials.test.config.ConfigTest;
import myessentials.test.datasource.DatasourceTest;
import myessentials.test.economy.EconomyTest;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({EconomyTest.class, DatasourceTest.class, ConfigTest.class})
public class MECTestSuite extends BaseSuite {

    @AfterClass
    public static void tearDown() {
        System.out.println(MECTestSuite.class.getClassLoader().getClass().getName());
        try {
            MinecraftServerStarter.INSTANCE().getGame().getClass().getMethod("stopServer").invoke(MinecraftServerStarter.INSTANCE().getGame());
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }
}

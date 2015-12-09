package myessentials.test;

import metest.BaseSuite;
import myessentials.test.config.ConfigTest;
import myessentials.test.datasource.DatasourceTest;
import myessentials.test.economy.EconomyTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({EconomyTest.class, DatasourceTest.class, ConfigTest.class})
public class MECTestSuite extends BaseSuite {

}
